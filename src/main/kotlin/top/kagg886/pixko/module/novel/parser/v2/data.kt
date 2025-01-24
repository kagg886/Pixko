package top.kagg886.pixko.module.novel.parser.v2

sealed interface NovelNode {
    val position: IntRange
}


data class TextNode(val text: CombinedText, override val position: IntRange) : NovelNode

data class JumpUriNode(val text: String, val uri: String, override val position: IntRange) : NovelNode

data class UploadImageNode(val url: String, override val position: IntRange) : NovelNode

data class PixivImageNode(val id: Int, val index: Int = 0, override val position: IntRange) : NovelNode

data class NewPageNode(override val position: IntRange) : NovelNode

data class TitleNode(val text: CombinedText, override val position: IntRange) : NovelNode

data class JumpPageNode(val page: Int, override val position: IntRange) : NovelNode


val NovelNode.isBlocking get() = this is JumpUriNode || this is TextNode || this is JumpPageNode


class CombinedText internal constructor(nodes: List<CombinedTextNode>) : List<CombinedTextNode> by nodes {
    override fun toString() = joinToString {
        when (it) {
            is NotatedText -> "${it.text}^{${it.notation}}"
            is PlainText -> it.text
        }
    }
}

fun List<CombinedTextNode>.asCombinedText() = CombinedText(this)

sealed interface CombinedTextNode {
    val text: String

    fun asSingle() = CombinedText(listOf(this))
}

data class PlainText(override val text: String) : CombinedTextNode
data class NotatedText(override val text: String, val notation: String) : CombinedTextNode

fun String.toPlainText() = PlainText(this)
fun String.toNotatedText(notation: String) = NotatedText(this, notation)