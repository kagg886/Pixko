package top.kagg886.pixko.module.novel.parser

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

data class PixivImageNode(val id: Int, val index: Int = 0, override val position: IntRange) : NovelNode {
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