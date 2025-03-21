package space.kscience.visionforge

///**
// * A container for styles
// */
//@JvmInline
//public value class StyleSheet(private val owner: MutableVision) {
//
//    private val styleNode: Meta get() = owner.properties[STYLESHEET_KEY] ?: Meta.EMPTY
//
//    public val items: Map<NameToken, Meta> get() = styleNode.items
//
//    public operator fun get(key: String): Meta? = owner.getStyle(key)
//
//    /**
//     * Define a style without notifying owner
//     */
//    public fun define(key: String, style: Meta?) {
//        owner.properties[STYLESHEET_KEY + key] = style
//    }
//
//    /**
//     * Set or clear the style
//     */
//    public operator fun set(key: String, style: Meta?) {
//        val oldStyle = get(key)
//        define(key, style)
//        owner.styleChanged(key, oldStyle, style)
//    }
//
//    public inline operator fun invoke(block: StyleSheet.() -> Unit): Unit = this.block()
//
//    /**
//     * Create and set a style
//     */
//    public fun update(key: String, builder: MutableMeta.() -> Unit) {
//        val newStyle = get(key)?.toMutableMeta()?.apply(builder) ?: Meta(builder)
//        set(key, newStyle.seal())
//    }
//
//    public companion object {
//        public val STYLESHEET_KEY: Name = "@stylesheet".asName()
//    }
//}
//
//internal fun Vision.styleChanged(key: String, oldStyle: Meta?, newStyle: Meta?) {
//    if (styles.contains(key)) {
//        //TODO optimize set concatenation
//        val tokens: Collection<Name> =
//            ((oldStyle?.items?.keys ?: emptySet()) + (newStyle?.items?.keys ?: emptySet())).map { it.asName() }
//    }
//    if (this is VisionGroup<*>) {
//        children.forEach { (_, vision) ->
//            vision.styleChanged(key, oldStyle, newStyle)
//        }
//    }
//}


