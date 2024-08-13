package top.kagg886.pixko.module.novel.parser


enum class Type(val blocking: Boolean, val double: Boolean = true) {
    PLAIN(false, false),
    JUMP_URI(false, false), //[[jumpuri:详情>链接]]
    NOTATION(blocking = false, false),//[[rb:文本>注音]]

    UPLOAD_IMAGE(blocking = true), //[[uploadedimage:id]]
    PIXIV_IMAGE(blocking = true),//[[pixivimage:id]]

    NEW_PAGE(blocking = true), //[newpage]
    TITLE(blocking = true), //[chapter:标题]
    JUMP_PAGE(blocking = true),//[jump:页码]
}

data class TextNode(
    val type: Type,
    val value: String? = null,
    val metadata: String? = null,
    val position: IntRange
)

