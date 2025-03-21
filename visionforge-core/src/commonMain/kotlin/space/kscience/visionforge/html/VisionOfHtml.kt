package space.kscience.visionforge.html

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.div
import kotlinx.html.stream.createHTML
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.asName
import space.kscience.visionforge.*


public interface VisionOfHtml : MutableVision {

    /**
     * Html class strings for this instance. Does not use vision inheritance, but uses styles
     */
    public var classes: Set<String>
        get() = readProperty(::classes.name, false, true).stringList?.toSet() ?: emptySet()
        set(value) {
            properties[::classes.name] = value.map { it.asValue() }
        }

    /**
     * A custom style string
     */
    public var styleString: String?
        get() = readProperty(::styleString.name,false,true).string
        set(value){
            properties[::styleString.name] = value?.asValue()
        }
}

@Serializable
@SerialName("html.plain")
public class VisionOfPlainHtml : AbstractVision(), VisionOfHtml {
    public var content: String? by properties.string()
}

public fun VisionOfPlainHtml.content(block: DIV.() -> Unit) {
    content = createHTML().apply {
        div(block = block)
    }.finalize()
}

@Suppress("UnusedReceiverParameter")
public inline fun VisionOutput.html(
    block: VisionOfPlainHtml.() -> Unit,
): VisionOfPlainHtml = VisionOfPlainHtml().apply(block)

@Serializable
public enum class InputFeedbackMode {
    /**
     * Fire feedback event on `onchange` event
     */
    ONCHANGE,

    /**
     * Fire feedback event on `oninput` event
     */
    ONINPUT,

    /**
     * provide only manual feedback
     */
    NONE
}

@Serializable
@SerialName("html.input")
public open class VisionOfHtmlInput(
    public val inputType: String,
) : AbstractControlVision(), VisionOfHtml {
    public var value: Value? by properties.value()
    public var disabled: Boolean by properties.boolean { false }
    public var fieldName: String? by properties.string()
}

/**
 * Trigger [callback] on each value change
 */
public fun VisionOfHtmlInput.onValueChange(
    scope: CoroutineScope = manager?.context ?: error("Coroutine context is not resolved for $this"),
    callback: suspend ControlValueChangeEvent.() -> Unit,
): Job = eventFlow.filterIsInstance<ControlValueChangeEvent>().onEach(callback).launchIn(scope)

public fun VisionOfHtmlInput.onInput(
    scope: CoroutineScope = manager?.context ?: error("Coroutine context is not resolved for $this"),
    callback: suspend ControlInputEvent.() -> Unit,
): Job = eventFlow.filterIsInstance<ControlInputEvent>().onEach(callback).launchIn(scope)

@Suppress("UnusedReceiverParameter")
public inline fun VisionOutput.htmlInput(
    inputType: String,
    block: VisionOfHtmlInput.() -> Unit = {},
): VisionOfHtmlInput = VisionOfHtmlInput(inputType).apply(block)

@Serializable
@SerialName("html.text")
public class VisionOfTextField : VisionOfHtmlInput(InputType.text.realValue) {
    public var text: String? by properties.string(key = VisionOfHtmlInput::value.name.asName())
}

@Suppress("UnusedReceiverParameter")
public inline fun VisionOutput.htmlTextField(
    block: VisionOfTextField.() -> Unit = {},
): VisionOfTextField = VisionOfTextField().apply(block)


@Serializable
@SerialName("html.checkbox")
public class VisionOfCheckbox : VisionOfHtmlInput(InputType.checkBox.realValue) {
    public var checked: Boolean? by properties.boolean(key = VisionOfHtmlInput::value.name.asName())
}

@Suppress("UnusedReceiverParameter")
public inline fun VisionOutput.htmlCheckBox(
    block: VisionOfCheckbox.() -> Unit = {},
): VisionOfCheckbox = VisionOfCheckbox().apply(block)

@Serializable
@SerialName("html.number")
public class VisionOfNumberField : VisionOfHtmlInput(InputType.number.realValue) {
    public var numberValue: Number? by properties.number(key = VisionOfHtmlInput::value.name.asName())
}

@Suppress("UnusedReceiverParameter")
public inline fun VisionOutput.htmlNumberField(
    block: VisionOfNumberField.() -> Unit = {},
): VisionOfNumberField = VisionOfNumberField().apply(block)

@Serializable
@SerialName("html.range")
public class VisionOfRangeField(
    public val min: Double,
    public val max: Double,
    public val step: Double = 1.0,
) : VisionOfHtmlInput(InputType.range.realValue) {
    public var numberValue: Number? by properties.number(key = VisionOfHtmlInput::value.name.asName())
}

@Suppress("UnusedReceiverParameter")
public inline fun VisionOutput.htmlRangeField(
    min: Number,
    max: Number,
    step: Number = 1.0,
    block: VisionOfRangeField.() -> Unit = {},
): VisionOfRangeField = VisionOfRangeField(min.toDouble(), max.toDouble(), step.toDouble()).apply(block)

