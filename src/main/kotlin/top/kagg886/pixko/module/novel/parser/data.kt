package top.kagg886.pixko.module.novel.parser

//@Deprecated("")
//enum class NovelContentBlockType(val blocking: Boolean, val double: Boolean = true) {
//    PLAIN(false, false),
//    JUMP_URI(false, false), //[[jumpuri:详情>链接]]
//    NOTATION(blocking = false, false),//[[rb:文本>注音]]
//
//    UPLOAD_IMAGE(blocking = true), //[[uploadedimage:id]]
//    PIXIV_IMAGE(blocking = true),//[[pixivimage:id]]
//
//    NEW_PAGE(blocking = true), //[newpage]
//    TITLE(blocking = true), //[chapter:标题]
//    JUMP_PAGE(blocking = true),//[jump:页码]
//}
//
//@Deprecated("")
//data class TextNode(
//    val novelContentBlockType: NovelContentBlockType,
//    val value: String? = null,
//    val metadata: String? = null,
//    val position: IntRange
//)


sealed interface NovelNode {
    val blocking: Boolean
    val position: IntRange
}


data class PlainTextNode(val text: String, override val position: IntRange) : NovelNode {
    override val blocking = false
}

data class JumpUriNode(val text: String, val uri: String, override val position: IntRange) : NovelNode {
    override val blocking = false
}

data class NotationNode(val text: String, val notation: String, override val position: IntRange) : NovelNode {
    override val blocking = false
}

data class UploadImageNode(val url: String, override val position: IntRange) : NovelNode {
    override val blocking = true
}

data class PixivImageNode(val id: Int, override val position: IntRange) : NovelNode {
    override val blocking = true
}

data class NewPageNode(override val position: IntRange) : NovelNode {
    override val blocking = true
}

data class TitleNode(val text: String, override val position: IntRange) : NovelNode {
    override val blocking = true
}

data class JumpPageNode(val page: Int, override val position: IntRange) : NovelNode {
    override val blocking = true
}