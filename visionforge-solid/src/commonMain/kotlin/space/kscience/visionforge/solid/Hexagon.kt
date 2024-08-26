package space.kscience.visionforge.solid

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import space.kscience.visionforge.MutableVisionContainer
import space.kscience.visionforge.VisionBuilder

public interface Hexagon : GeometrySolid {
    public val node1: Float32Vector3D
    public val node2: Float32Vector3D
    public val node3: Float32Vector3D
    public val node4: Float32Vector3D
    public val node5: Float32Vector3D
    public val node6: Float32Vector3D
    public val node7: Float32Vector3D
    public val node8: Float32Vector3D

    override fun <T : Any> toGeometry(geometryBuilder: GeometryBuilder<T>) {
        geometryBuilder.face4(node1, node4, node3, node2)
        geometryBuilder.face4(node1, node2, node6, node5)
        geometryBuilder.face4(node2, node3, node7, node6)
        geometryBuilder.face4(node4, node8, node7, node3)
        geometryBuilder.face4(node1, node5, node8, node4)
        geometryBuilder.face4(node8, node5, node6, node7)
    }
}

/**
 * A separate class is created to optimize native rendering
 */
@Serializable
@SerialName("solid.box")
public class Box(
    public val xSize: Float,
    public val ySize: Float,
    public val zSize: Float,
) : SolidBase<Box>(), Hexagon {

    private inline val dx get() = xSize / 2
    private inline val dy get() = ySize / 2
    private inline val dz get() = zSize / 2

    override val node1: Float32Vector3D get() = Float32Vector3D(-dx, -dy, -dz)
    override val node2: Float32Vector3D get() = Float32Vector3D(dx, -dy, -dz)
    override val node3: Float32Vector3D get() = Float32Vector3D(dx, dy, -dz)
    override val node4: Float32Vector3D get() = Float32Vector3D(-dx, dy, -dz)
    override val node5: Float32Vector3D get() = Float32Vector3D(-dx, -dy, dz)
    override val node6: Float32Vector3D get() = Float32Vector3D(dx, -dy, dz)
    override val node7: Float32Vector3D get() = Float32Vector3D(dx, dy, dz)
    override val node8: Float32Vector3D get() = Float32Vector3D(-dx, dy, dz)
}

@VisionBuilder
public inline fun MutableVisionContainer<Solid>.box(
    xSize: Number,
    ySize: Number,
    zSize: Number,
    name: String? = null,
    block: Box.() -> Unit = {},
): Box = Box(xSize.toFloat(), ySize.toFloat(), zSize.toFloat()).apply(block).also { setSolid(name, it) }

@Serializable
@SerialName("solid.hexagon")
public class GenericHexagon(
    override val node1: Float32Vector3D,
    override val node2: Float32Vector3D,
    override val node3: Float32Vector3D,
    override val node4: Float32Vector3D,
    override val node5: Float32Vector3D,
    override val node6: Float32Vector3D,
    override val node7: Float32Vector3D,
    override val node8: Float32Vector3D,
) : SolidBase<GenericHexagon>(), Hexagon

@VisionBuilder
public inline fun MutableVisionContainer<Solid>.hexagon(
    node1: Float32Vector3D,
    node2: Float32Vector3D,
    node3: Float32Vector3D,
    node4: Float32Vector3D,
    node5: Float32Vector3D,
    node6: Float32Vector3D,
    node7: Float32Vector3D,
    node8: Float32Vector3D,
    name: String? = null,
    action: Hexagon.() -> Unit = {},
): Hexagon = GenericHexagon(node1, node2, node3, node4, node5, node6, node7, node8).apply(action).also { setSolid(name, it) }