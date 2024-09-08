package space.kscience.plotly

import space.kscience.plotly.models.Shape
import space.kscience.plotly.models.Text


public fun Plot.text(block: Text.() -> Unit) {
    layout.annotation(block)
}

public fun Plot.shape(block: Shape.() -> Unit) {
    layout.figure(block)
}
