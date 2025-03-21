package space.kscience.visionforge.html

import kotlinx.dom.clear
import kotlinx.html.InputType
import kotlinx.html.div
import kotlinx.html.js.input
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import space.kscience.dataforge.meta.asValue
import space.kscience.dataforge.meta.double
import space.kscience.dataforge.meta.string
import space.kscience.visionforge.ControlInputEvent
import space.kscience.visionforge.ControlValueChangeEvent
import space.kscience.visionforge.asyncControlEvent
import space.kscience.visionforge.useProperty

/**
 * Subscribes the HTML element to a given vision.
 *
 * @param vision The vision to subscribe to.
 */
internal fun HTMLElement.subscribeToVision(vision: VisionOfHtml) {
    vision.useProperty(VisionOfHtml::classes) {
        classList.value = classes.joinToString(separator = " ")
    }

    vision.useProperty(VisionOfHtml::styleString) {
        style.cssText = it ?: ""
    }
}

/**
 * Subscribes the HTML input element to a given vision.
 *
 * @param inputVision The input vision to subscribe to.
 */
private fun HTMLInputElement.subscribeToInput(inputVision: VisionOfHtmlInput) {
    subscribeToVision(inputVision)
    inputVision.useProperty(VisionOfHtmlInput::disabled) {
        disabled = it
    }
}

internal val htmlVisionRenderer: ElementVisionRenderer = ElementVisionRenderer<VisionOfPlainHtml> { _, vision, _ ->
    div().also { div ->
        div.subscribeToVision(vision)
        vision.useProperty(VisionOfPlainHtml::content) {
            div.clear()
            if (it != null) div.innerHTML = it
        }
    }
}

internal val inputVisionRenderer: ElementVisionRenderer = ElementVisionRenderer<VisionOfHtmlInput>(
    acceptRating = ElementVisionRenderer.DEFAULT_RATING - 1
) { name, vision, _ ->

    input {
        type = InputType.text
    }.also { htmlInputElement ->

        htmlInputElement.onchange = {
            vision.asyncControlEvent(ControlValueChangeEvent(htmlInputElement.value.asValue(), name))
        }

        htmlInputElement.oninput = {
            vision.asyncControlEvent(ControlInputEvent(htmlInputElement.value.asValue(), name))
        }

        htmlInputElement.subscribeToInput(vision)
        vision.useProperty(VisionOfHtmlInput::value) {
            htmlInputElement.value = it?.string ?: ""
        }
    }
}

internal val checkboxVisionRenderer: ElementVisionRenderer =
    ElementVisionRenderer<VisionOfCheckbox> { name, vision, _ ->
        input {
            type = InputType.checkBox
        }.also { htmlInputElement ->

            htmlInputElement.onchange = {
                vision.asyncControlEvent(ControlValueChangeEvent(htmlInputElement.value.asValue(), name))
            }

            htmlInputElement.oninput = {
                vision.asyncControlEvent(ControlInputEvent(htmlInputElement.value.asValue(), name))
            }


            htmlInputElement.subscribeToInput(vision)
            vision.useProperty(VisionOfCheckbox::checked) {
                htmlInputElement.checked = it ?: false
            }
        }
    }

internal val textVisionRenderer: ElementVisionRenderer =
    ElementVisionRenderer<VisionOfTextField> { name, vision, _ ->
        input {
            type = InputType.text
        }.also { htmlInputElement ->

            htmlInputElement.onchange = {
                vision.asyncControlEvent(ControlValueChangeEvent(htmlInputElement.value.asValue(), name))
            }

            htmlInputElement.oninput = {
                vision.asyncControlEvent(ControlInputEvent(htmlInputElement.value.asValue(), name))
            }

            htmlInputElement.subscribeToInput(vision)
            vision.useProperty(VisionOfTextField::text) {
                htmlInputElement.value = it ?: ""
            }
        }
    }

internal val numberVisionRenderer: ElementVisionRenderer =
    ElementVisionRenderer<VisionOfNumberField> { name, vision, _ ->
        input {
            type = InputType.number
        }.also { htmlInputElement ->

            htmlInputElement.onchange = {
                htmlInputElement.value.toDoubleOrNull()?.let {
                    vision.asyncControlEvent(ControlValueChangeEvent(it.asValue(), name))
                }
            }

            htmlInputElement.oninput = {
                htmlInputElement.value.toDoubleOrNull()?.let {
                    vision.asyncControlEvent(ControlInputEvent(it.asValue(), name))
                }
            }


            htmlInputElement.subscribeToInput(vision)
            vision.useProperty(VisionOfNumberField::value) {
                htmlInputElement.valueAsNumber = it?.double ?: 0.0
            }
        }
    }

internal val rangeVisionRenderer: ElementVisionRenderer =
    ElementVisionRenderer<VisionOfRangeField> { name, vision, _ ->
        input {
            type = InputType.range
            min = vision.min.toString()
            max = vision.max.toString()
            step = vision.step.toString()
        }.also { htmlInputElement ->

            htmlInputElement.onchange = {
                htmlInputElement.value.toDoubleOrNull()?.let {
                    vision.asyncControlEvent(ControlValueChangeEvent(it.asValue(), name))
                }
            }

            htmlInputElement.oninput = {
                htmlInputElement.value.toDoubleOrNull()?.let {
                    vision.asyncControlEvent(ControlInputEvent(it.asValue(), name))
                }
            }

            htmlInputElement.subscribeToInput(vision)
            vision.useProperty(VisionOfRangeField::value) {
                htmlInputElement.valueAsNumber = it?.double ?: 0.0
            }
        }
    }
