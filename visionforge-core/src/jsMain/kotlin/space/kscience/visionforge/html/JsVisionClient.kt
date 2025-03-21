package space.kscience.visionforge.html

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import org.w3c.dom.*
import org.w3c.dom.url.URL
import space.kscience.dataforge.context.*
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.MetaSerializer
import space.kscience.dataforge.meta.get
import space.kscience.dataforge.meta.int
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.asName
import space.kscience.dataforge.names.parseAsName
import space.kscience.visionforge.*
import space.kscience.visionforge.html.VisionTagConsumer.Companion.OUTPUT_CONNECT_ATTRIBUTE
import space.kscience.visionforge.html.VisionTagConsumer.Companion.OUTPUT_ENDPOINT_ATTRIBUTE
import space.kscience.visionforge.html.VisionTagConsumer.Companion.OUTPUT_FETCH_ATTRIBUTE
import space.kscience.visionforge.html.VisionTagConsumer.Companion.OUTPUT_NAME_ATTRIBUTE
import space.kscience.visionforge.html.VisionTagConsumer.Companion.OUTPUT_RENDERED
import kotlin.time.Duration.Companion.milliseconds

/**
 * A Kotlin-browser plugin that renders visions based on provided renderers and governs communication with the server.
 */
public class JsVisionClient : AbstractPlugin(), VisionClient {
    override val tag: PluginTag get() = Companion.tag
    override val visionManager: VisionManager by require(VisionManager)

    /**
     * Up-going tree traversal in search for endpoint attribute. If element is null, return window URL
     */
    private fun resolveEndpoint(element: Element?): String {
        if (element == null) return window.location.href
        val attribute = element.attributes[OUTPUT_ENDPOINT_ATTRIBUTE]
        return attribute?.value ?: resolveEndpoint(element.parentElement)
    }

    private fun resolveName(element: Element): String? {
        val attribute = element.attributes[OUTPUT_NAME_ATTRIBUTE]
        return attribute?.value
    }

    internal val renderers by lazy { context.gather<ElementVisionRenderer>(ElementVisionRenderer.TYPE).values }

    private fun findRendererFor(vision: Vision): ElementVisionRenderer? = renderers.mapNotNull {
        val rating = it.rateVision(vision)
        if (rating > 0) {
            rating to it
        } else {
            null
        }
    }.maxByOrNull { it.first }?.second

    private fun Element.getEmbeddedData(className: String): String? = getElementsByClassName(className)[0]?.innerHTML

    private fun Element.getFlag(attribute: String): Boolean = attributes[attribute]?.value != null

    private val mutex = Mutex()


    private val rootChangeCollector = VisionChangeCollector()

    /**
     * Communicate vision property changed from rendering engine to model
     */
    override fun notifyPropertyChanged(visionName: Name, propertyName: Name, item: Meta?) {
        context.launch {
            mutex.withLock {
                rootChangeCollector.getOrCreateChange(visionName).propertyChanged(propertyName, item)
            }
        }
    }

    private val eventCollector = MutableSharedFlow<Pair<Name, VisionEvent>>(meta["feedback.eventCache"].int ?: 100)

    /**
     * Send a custom feedback event
     */
    override suspend fun sendEvent(targetName: Name, event: VisionEvent) {
        eventCollector.emit(targetName to event)
    }

    private fun startVisionUpdate(element: Element, visionName: Name, vision: Vision, outputMeta: Meta) {
        element.attributes[OUTPUT_CONNECT_ATTRIBUTE]?.let { attr ->
            val wsUrl = if (attr.value.isBlank() || attr.value == VisionTagConsumer.AUTO_DATA_ATTRIBUTE) {
                val endpoint = resolveEndpoint(element)
                logger.info { "Vision server is resolved to $endpoint" }
                URL(endpoint).apply {
                    pathname += "/ws"
                }
            } else {
                URL(attr.value)
            }.apply {
                protocol = "ws"
                searchParams.append("name", visionName.toString())
            }


            logger.info { "Updating vision data from $wsUrl" }

            //Individual websocket for this vision
            WebSocket(wsUrl.toString()).apply {
                onmessage = { messageEvent ->
                    val stringData: String? = messageEvent.data as? String
                    if (stringData != null) {
                        val event: VisionEvent = visionManager.jsonFormat.decodeFromString(stringData)

                        // If change contains root vision replacement, do it
                        if (event is VisionChange) {
                            event.vision?.let { vision ->
                                renderVision(element, visionName, vision, outputMeta)
                            }
                        }

                        logger.debug { "Got $event for output with name $visionName" }
                        context.launch {
                            vision.receiveEvent(event)
                        }
                    } else {
                        logger.error { "WebSocket message data is not a string" }
                    }
                }

                //Backward change propagation
                var feedbackJob: Job? = null

                //Feedback changes aggregation time in milliseconds
                val feedbackAggregationTime = meta["feedback.aggregationTime"]?.int ?: 300

                onopen = {

                    feedbackJob = visionManager.context.launch {
                        //launch a separate coroutine to send events to the backend
                        eventCollector.filter { it.first == visionName }.onEach {
                            send(visionManager.jsonFormat.encodeToString(it.second))
                        }.launchIn(this)

                        //aggregate atomic changes
                        while (isActive) {
                            delay(feedbackAggregationTime.milliseconds)
                            val visionChangeCollector = rootChangeCollector[visionName]
                            if (visionChangeCollector?.isEmpty() == false) {
                                mutex.withLock {
                                    eventCollector.emit(visionName to visionChangeCollector.collect(visionManager))
                                    rootChangeCollector.reset()
                                }
                            }
                        }
                    }
                    logger.info { "WebSocket feedback channel established for output '$visionName'" }
                }

                onclose = {
                    feedbackJob?.cancel()
                    logger.info { "WebSocket feedback channel closed for output '$visionName'" }
                }

                onerror = {
                    feedbackJob?.cancel()
                    logger.error { "WebSocket feedback channel error for output '$visionName'" }
                }
            }
        }
    }


    private fun renderVision(element: Element, name: Name, vision: Vision, outputMeta: Meta) {
        vision.setAsRoot(visionManager)
        val renderer: ElementVisionRenderer = findRendererFor(vision)
            ?: error("Could not find renderer for ${vision::class}")
        //render vision
        renderer.render(element, name, vision, outputMeta)
        //start vision update from a backend model
        startVisionUpdate(element, name, vision, outputMeta)
        //subscribe to backwards events propagation for control visions
        if (vision is ControlVision) {
            vision.eventFlow.onEach {
                sendEvent(name, it)
            }.launchIn(context)
        }

    }

    /**
     * Fetch from server and render a vision, described in a given with [VisionTagConsumer.OUTPUT_CLASS] class.
     */
    public fun renderVisionIn(element: Element) {
        if (!element.classList.contains(VisionTagConsumer.OUTPUT_CLASS)) error("The element $element is not an output element")
        val name = resolveName(element)?.parseAsName() ?: error("The element is not a vision output")

        if (element.attributes[OUTPUT_RENDERED]?.value == "true") {
            logger.info { "VF output in element $element is already rendered" }
            return
        } else {
            logger.info { "Rendering VF output with name $name" }
        }

        val outputMeta = element.getEmbeddedData(VisionTagConsumer.OUTPUT_META_CLASS)?.let {
            VisionManager.defaultJson.decodeFromString(MetaSerializer, it).also {
                logger.info { "Output meta for $name: $it" }
            }
        } ?: Meta.EMPTY

        when {
            // fetch data if the path is provided
            element.attributes[OUTPUT_FETCH_ATTRIBUTE] != null -> {
                val attr = element.attributes[OUTPUT_FETCH_ATTRIBUTE]!!

                val fetchUrl = if (attr.value.isBlank() || attr.value == VisionTagConsumer.AUTO_DATA_ATTRIBUTE) {
                    val endpoint = resolveEndpoint(element)
                    logger.info { "Vision server is resolved to $endpoint" }
                    URL(endpoint).apply {
                        pathname += "/data"
                    }
                } else {
                    URL(attr.value)
                }.apply {
                    searchParams.append("name", name.toString())
                }

                logger.info { "Fetching vision data from $fetchUrl" }
                window.fetch(fetchUrl).then { response ->
                    if (response.ok) {
                        response.text().then { text ->
                            val vision = visionManager.decodeFromString(text)
                            renderVision(element, name, vision, outputMeta)
                        }
                    } else {
                        logger.error { "Failed to fetch initial vision state from $fetchUrl" }
                    }
                }
            }

            // use embedded data if it is available
            element.getElementsByClassName(VisionTagConsumer.OUTPUT_DATA_CLASS).length > 0 -> {
                //Getting embedded vision data
                val embeddedVision = element.getEmbeddedData(VisionTagConsumer.OUTPUT_DATA_CLASS)!!.let {
                    visionManager.decodeFromString(it)
                }
                logger.info { "Found embedded vision data with name $name" }
                renderVision(element, name, embeddedVision, outputMeta)
            }

            //Try to load vision via websocket
//            element.attributes[OUTPUT_CONNECT_ATTRIBUTE] != null -> {
//                startVisionUpdate(element, name, null, outputMeta)
//            }

            else -> error("No embedded vision data / fetch url for $name")
        }
        element.setAttribute(OUTPUT_RENDERED, "true")
    }

    override fun content(target: String): Map<Name, Any> = if (target == ElementVisionRenderer.TYPE) {
        listOf(
            htmlVisionRenderer,
            inputVisionRenderer,
            checkboxVisionRenderer,
            numberVisionRenderer,
            textVisionRenderer,
            rangeVisionRenderer,
            formVisionRenderer,
            buttonVisionRenderer
        ).associateBy { it.toString().asName() }
    } else super<AbstractPlugin>.content(target)

    public companion object : PluginFactory<JsVisionClient> {
        override fun build(context: Context, meta: Meta): JsVisionClient = JsVisionClient()

        override val tag: PluginTag = PluginTag(name = "vision.client.js", group = PluginTag.DATAFORGE_GROUP)
    }
}

private fun whenDocumentLoaded(block: Document.() -> Unit): Unit {
    if (document.body != null) {
        block(document)
    } else {
        document.addEventListener("DOMContentLoaded", { block(document) })
    }
}

/**
 * Fetch and render visions for all elements with [VisionTagConsumer.OUTPUT_CLASS] class inside given [element].
 */
public fun JsVisionClient.renderAllVisionsIn(element: Element) {
    val elements = element.getElementsByClassName(VisionTagConsumer.OUTPUT_CLASS)
    logger.info { "Finished search for outputs. Found ${elements.length} items" }
    elements.asList().forEach { child ->
        renderVisionIn(child)
    }
}

/**
 * Render all visions in an element with a given [id]
 */
public fun JsVisionClient.renderAllVisionsById(document: Document, id: String): Unit {
    val element = document.getElementById(id)
    if (element != null) {
        renderAllVisionsIn(element)
    } else {
        logger.warn { "Element with id $id not found" }
    }
}


/**
 * Fetch visions from the server for all elements with [VisionTagConsumer.OUTPUT_CLASS] class in the document body
 */
public fun JsVisionClient.renderAllVisions(): Unit = whenDocumentLoaded {
    val element = body ?: error("Document does not have a body")
    renderAllVisionsIn(element)
}

/**
 * Create a vision client context and render all visions on the page.
 */
public fun runVisionClient(contextBuilder: ContextBuilder.() -> Unit) {
    Global.logger.info { "Starting VisionForge context" }

    val context = Context("VisionForge") {
        plugin(JsVisionClient)
        contextBuilder()
    }

    val client = context.request(JsVisionClient)

    startApplication {
        context.logger.info {
            "Starting VisionClient with renderers: ${
                client.renderers.joinToString(
                    prefix = "\n\t",
                    separator = "\n\t"
                ) { it.toString() }
            }"
        }
        val element = document.body ?: error("Document does not have a body")
        client.renderAllVisionsIn(element)
    }
}