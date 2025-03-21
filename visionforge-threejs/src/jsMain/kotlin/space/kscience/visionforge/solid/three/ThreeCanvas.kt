package space.kscience.visionforge.solid.three

import kotlinx.browser.window
import kotlinx.coroutines.launch
import org.w3c.dom.Element
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Node
import org.w3c.dom.events.MouseEvent
import space.kscience.dataforge.context.info
import space.kscience.dataforge.context.logger
import space.kscience.dataforge.meta.get
import space.kscience.dataforge.meta.isEmpty
import space.kscience.dataforge.meta.string
import space.kscience.dataforge.meta.useProperty
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.asName
import space.kscience.dataforge.names.plus
import space.kscience.visionforge.Colors
import space.kscience.visionforge.solid.Solid
import space.kscience.visionforge.solid.specifications.*
import three.WebGLRenderer
import three.cameras.PerspectiveCamera
import three.core.Object3D
import three.core.Raycaster
import three.external.controls.OrbitControls
import three.external.controls.TrackballControls
import three.geometries.EdgesGeometry
import three.materials.Material
import three.math.Box3
import three.math.Plane
import three.math.Vector2
import three.math.Vector3
import three.meshline.MeshLine
import three.meshline.MeshLineMaterial
import three.meshline.isMeshLineMaterial
import three.objects.LineSegments
import three.objects.Mesh
import three.scenes.Scene
import kotlin.math.cos
import kotlin.math.sin

/**
 * A canvas for three-js rendering
 */
public class ThreeCanvas(
    public val three: ThreePlugin,
    public val element: Element,
    public val options: Canvas3DOptions = Canvas3DOptions(),
) {
    private var boundingBox: Box3? = null
    private var root: Object3D? = null
        set(value) {
            field = value
            if (value != null) boundingBox = Box3().setFromObject(value)
        }

    private val raycaster = Raycaster()
    private val mousePosition: Vector2 = Vector2()

    private val scene: Scene = Scene()


    private fun buildCamera(spec: CameraScheme) = PerspectiveCamera(
        spec.fov,
        1.0,
        spec.nearClip,
        spec.farClip
    ).apply {
        translateX(spec.distance * sin(spec.zenith) * sin(spec.azimuth))
        translateY(spec.distance * cos(spec.zenith))
        translateZ(spec.distance * sin(spec.zenith) * cos(spec.azimuth))
        options.useProperty(Canvas3DOptions::layers, this) { selectedLayers ->
            (0..31).forEach {
                if (it in selectedLayers) {
                    this@apply.layers.enable(it)
                } else {
                    this@apply.layers.disable(it)
                }
            }
        }
    }

    public val camera: PerspectiveCamera = buildCamera(options.camera)

    private var picked: Object3D? = null

    private val renderer = WebGLRenderer {
        antialias = true
    }.apply {
        setClearColor(Colors.skyblue, 1)
    }

    private val canvas = (renderer.domElement as HTMLCanvasElement).apply {
        className += "three-canvas"
        style.apply {
            width = "100%"
            height = "100%"
            display = "block"
        }
    }

    /**
     * Force camera aspect ratio and renderer size recalculation
     */
    private fun updateSize() {
        val width = element.clientWidth
        val height = element.clientHeight
        canvas.width = width
        canvas.height = height
        renderer.setSize(width, height, false)
        camera.aspect = width.toDouble() / height.toDouble()
        camera.updateProjectionMatrix()
    }

    init {
        check(element.getElementsByClassName("three-canvas").length == 0) {
            "Three canvas already created in this element"
        }
        element.appendChild(canvas)
        updateSize()
        canvas.addEventListener("pointerdown", {
            val newPicked = pick()
            options.onSelect?.invoke(newPicked?.fullName())
        }, false)

        //Attach listener to track mouse changes
        canvas.addEventListener("mousemove", { event ->
            (event as? MouseEvent)?.run {
                val rect = canvas.getBoundingClientRect()
                mousePosition.x = ((event.clientX - rect.left) / canvas.clientWidth) * 2 - 1
                mousePosition.y = -((event.clientY - rect.top) / canvas.clientHeight) * 2 + 1
            }
        }, false)

        window.onresize = {
            updateSize()
        }

        addControls(canvas, options.controls)

        renderer.setAnimationLoop {
            val newPicked = pick()

            if (newPicked != null && picked !== newPicked) {
                picked?.toggleHighlight(false, HIGHLIGHT_NAME, HIGHLIGHT_MATERIAL)
                newPicked.toggleHighlight(true, HIGHLIGHT_NAME, HIGHLIGHT_MATERIAL)
                picked = newPicked
            }

            renderer.render(scene, camera)
        }

        //Clipping planes
        options.useProperty(Canvas3DOptions::clipping) { clipping: PointScheme ->
            if (clipping.meta.isEmpty()) {
                renderer.clippingPlanes = emptyArray()
            } else {
                boundingBox?.let { boundingBox ->
                    val xClippingPlane = clipping.x?.let {
                        val absoluteValue = boundingBox.min.x + (boundingBox.max.x - boundingBox.min.x) * it
                        Plane(Vector3(-1.0, 0.0, 0.0), absoluteValue)
                    }
                    val yClippingPlane = clipping.y?.let {
                        val absoluteValue = boundingBox.min.y + (boundingBox.max.y - boundingBox.min.y) * it
                        Plane(Vector3(0.0, -1.0, 0.0), absoluteValue)
                    }

                    val zClippingPlane = clipping.z?.let {
                        val absoluteValue = boundingBox.min.z + (boundingBox.max.z - boundingBox.min.z) * it
                        Plane(Vector3(0.0, 0.0, -1.0), absoluteValue)
                    }
                    renderer.clippingPlanes =
                        listOfNotNull(xClippingPlane, yClippingPlane, zClippingPlane).toTypedArray()
                }
            }
        }

        options.useProperty(Canvas3DOptions::size) {
            canvas.style.apply {
                minWidth = "${options.size.minWith.toInt()}px"
                maxWidth = "${options.size.maxWith.toInt()}px"
                minHeight = "${options.size.minHeight.toInt()}px"
                maxHeight = "${options.size.maxHeight.toInt()}px"
            }
        }

    }

    /**
     * Resolve the full name of the object relative to the global root
     */
    private fun Object3D.fullName(): Name {
        if (root == null) error("Can't resolve element name without the root")
        return if (parent == root) {
            Name.parse(name)
        } else {
            (parent?.fullName() ?: Name.EMPTY) + Name.parse(name)
        }
    }

    //find the first non-static parent in this object ancestry
    private tailrec fun Object3D.upTrace(): Object3D? = if (!name.startsWith("@")) this else parent?.upTrace()

    private fun pick(): Object3D? {
        // update the picking ray with the camera and mouse position
        raycaster.setFromCamera(mousePosition, camera)

        // calculate objects intersecting the picking ray
        return root?.let { root ->
            val intersects = raycaster.intersectObject(root, true)
            //skip invisible objects
            val obj: Object3D? = intersects.map { it.`object` }.firstOrNull { it.visible }
            obj?.upTrace()
        }
    }

//    private fun buildLight(spec: AmbientLightScheme?): info.laht.threekt.lights.Light = when (spec?.type) {
//        AmbientLightScheme.Type.POINT -> PointLight().apply {
//            position.x = spec.position.x ?: 0.0
//            position.y = spec.position.y ?: 0.0
//            position.z = spec.position.z ?: 0.0
//        }
//        else -> AmbientLight().apply {
//
//        }
//    }.apply {
//        this.color = spec?.color?.threeColor() ?: Color(0x404040)
//
//        spec?.intensity?.coerceIn(0.0, 1.0)?.let {
//            this.intensity = it
//        }
//    }

    private fun addControls(element: Node, controls: Canvas3DUIScheme) {
        when (controls.meta["type"].string) {
            "trackball" -> TrackballControls(camera, element)
            else -> OrbitControls(camera, element)
        }
    }

    public fun clear() {
        scene.children.find { it.name == "@root" }?.let {
            scene.remove(it)
        }
    }

    public fun render(vision: Solid) {
        if (root != null) {
            three.logger.info { "Replacing root node in three canvas" }
            scene.findChild("@root".asName())?.let { scene.remove(it) }
            root?.dispose()
        }
        three.context.launch {
            val object3D = three.buildObject3D(vision)

            object3D.name = "@root"
            scene.add(object3D)
            root = object3D
        }
    }

    private var selected: Object3D? = null

    /**
     * Toggle highlight for the given [Mesh] object
     */
    private fun Object3D.toggleHighlight(
        highlight: Boolean,
        edgesName: String,
        material: Material,
    ) {

        if (userData[DO_NOT_HIGHLIGHT_TAG] == true) {
            return
        }

        if (isMesh(this)) {
            val highlightMesh = getObjectByName(edgesName) ?: if (isMeshLineMaterial(material)) {
                Mesh(
                    MeshLine(EdgesGeometry(geometry)),
                    material
                )
            } else {
                LineSegments(EdgesGeometry(geometry), material)
            }.also {
                it.name = edgesName
                add(it)
            }
            highlightMesh.visible = highlight
        } else {
            //ignore service objects if they are not statics
            children.filter { it.name.startsWith("@static") || !it.name.startsWith("@") }.forEach {
                it.toggleHighlight(highlight, edgesName, material)
            }
        }
    }

    /**
     * Toggle highlight for element with given name
     */
    public fun select(name: Name?) {
        if (name == null) {
            selected?.toggleHighlight(false, SELECT_NAME, SELECTED_MATERIAL)
            selected = null
            return
        }
        val obj = root?.findChild(name)
        if (obj != null && selected != obj) {
            selected?.toggleHighlight(false, SELECT_NAME, SELECTED_MATERIAL)
            obj.toggleHighlight(true, SELECT_NAME, SELECTED_MATERIAL)
            selected = obj
        }
    }

    public companion object {
        public val SELECTED_MATERIAL: MeshLineMaterial = MeshLineMaterial().apply {
            color.set(Colors.ivory)
            lineWidth = 2f
            cached = true
        }

        public val HIGHLIGHT_MATERIAL: MeshLineMaterial = MeshLineMaterial().apply {
            color.set(Colors.blue)
            lineWidth = 1f
            cached = true
        }
//
//        public val SELECTED_MATERIAL: LineBasicMaterial = LineBasicMaterial().apply {
//            color.set(Colors.ivory)
//            cached = true
//        }
//
//        public val HIGHLIGHT_MATERIAL: LineBasicMaterial = LineBasicMaterial().apply {
//            color.set(Colors.blue)
//            cached = true
//        }


        public const val DO_NOT_HIGHLIGHT_TAG: String = "doNotHighlight"
        private const val HIGHLIGHT_NAME = "@highlight"
        private const val SELECT_NAME = "@select"
        private const val LIGHT_NAME = "@light"
        private const val AXES_NAME = "@axes"
        //private const val CLIP_HELPER_NAME = "@clipping"
    }
}

//public fun ThreeCanvas.configure(newOptions: Canvas3DOptions) {
//    this.options.update(newOptions.toMeta())
//    options.onChange(this) { name, _, newItem ->
//        this.options[name] = newItem
//    }
//}