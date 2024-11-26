package top.kagg886.pixko.module.novel.parser


private val JUMP_URI_REGEX = "\\[\\[jumpuri:(.*)>(.*)]]".toRegex()
private val NOTATION_REGEX = "\\[\\[rb:(.*)>(.*)]]".toRegex()
private val UPLOAD_IMAGE_REGEX = "\\[uploadedimage:(.*)]".toRegex()
private val PIXIV_IMAGE_REGEX = "\\[pixivimage:(.*)]".toRegex()
private val NEW_PAGE_REGEX = "\\[newpage]".toRegex()
private val CHAPTER_REGEX = "\\[chapter:(.*)]".toRegex()
private val JUMP_PAGE_REGEX = "\\[jump:(.*)]".toRegex()
fun createNovelData(str: String): List<NovelNode> {
    val result = mutableListOf<NovelNode>()

    JUMP_URI_REGEX.findAll(str).forEach { i ->
        result.add(JumpUriNode(i.groupValues[1], i.groupValues[2], i.range))
    }
    NOTATION_REGEX.findAll(str).forEach { i ->
        result.add(NotationNode(i.groupValues[1], i.groupValues[2], i.range))
    }
    UPLOAD_IMAGE_REGEX.findAll(str).forEach { i ->
        result.add(UploadImageNode(i.groupValues[1], i.range))
    }
    PIXIV_IMAGE_REGEX.findAll(str).forEach { i ->
        result.add(PixivImageNode(i.groupValues[1].toInt(), i.range))
    }
    NEW_PAGE_REGEX.findAll(str).forEach { i ->
        result.add(NewPageNode(i.range))
    }
    CHAPTER_REGEX.findAll(str).forEach { i ->
        result.add(TitleNode(i.groupValues[1], i.range))
    }
    JUMP_PAGE_REGEX.findAll(str).forEach { i ->
        result.add(JumpPageNode(i.groupValues[1].toInt(), i.range))
    }
    result.sortBy {
        it.position.first
    }
    if (result.isEmpty()) {
        return listOf(
            PlainTextNode(str, 0..str.length)
        )
    }
    var index = -1
    for (i in 0..<result.count()) {
        val ele = result[i]
        result.add(
            PlainTextNode(str.substring(index + 1, ele.position.first), index..ele.position.first)
        )
        index = ele.position.last
    }
    if (index < str.length) {
        result.add(
            PlainTextNode(str.substring(index + 1, str.length), index..str.length)
        )
    }
    result.sortBy {
        it.position.first
    }
    return result
}

fun List<NovelNode>.toOriginalString(): String {
    return buildString {
        this@toOriginalString.forEach { v ->
            append(
                when (v) {
                    is PlainTextNode -> v.text
                    is JumpUriNode -> "[[jumpuri:${v.text}>${v.uri}]]"
                    is NotationNode -> "[[rb:${v.text}>${v.notation}]]"
                    is UploadImageNode -> "[uploadedimage:${v.url}]"
                    is PixivImageNode -> "[pixivimage:${v.id}]"
                    is NewPageNode -> "[newpage]"
                    is TitleNode -> "[chapter:${v.text}]"
                    is JumpPageNode -> "[jump:${v.page}]"
                }
            )
        }
    }
}