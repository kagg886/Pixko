package top.kagg886.pixko.module.novel.parser

import top.kagg886.pixko.module.novel.parser.Type.*


private val JUMP_URI_REGEX = "\\[\\[jumpuri:(.*)>(.*)]]".toRegex()
private val NOTATION_REGEX = "\\[\\[rb:(.*)>(.*)]]".toRegex()
private val UPLOAD_IMAGE_REGEX = "\\[uploadedimage:(.*)]".toRegex()
private val PIXIV_IMAGE_REGEX = "\\[pixivimage:(.*)]".toRegex()
private val NEW_PAGE_REGEX = "\\[newpage]".toRegex()
private val CHAPTER_REGEX = "\\[chapter:(.*)]".toRegex()
private val JUMP_PAGE_REGEX = "\\[jump:(.*)]".toRegex()
fun createNovelData(str: String): List<TextNode> {
    val result = mutableListOf<TextNode>()

    str.findAllJumpUri().forEach { i ->
        result.add(TextNode(JUMP_URI, i.groupValues[1], i.groupValues[2], i.range))
    }
    str.findAllNotation().forEach { i ->
        result.add(TextNode(NOTATION, i.groupValues[1], i.groupValues[2], i.range))
    }
    str.findAllUploadImage().forEach { i ->
        result.add(TextNode(UPLOAD_IMAGE, i.groupValues[1], null, i.range))
    }
    str.findAllPixivImage().forEach { i ->
        result.add(TextNode(PIXIV_IMAGE, i.groupValues[1], null, i.range))
    }
    str.findAllNewPage().forEach { i ->
        result.add(TextNode(NEW_PAGE, null, null, i.range))
    }
    str.findAllChapter().forEach { i ->
        result.add(TextNode(TITLE, i.groupValues[1], null, i.range))
    }
    str.findAllJumpPage().forEach { i ->
        result.add(TextNode(JUMP_PAGE, i.groupValues[1], null, i.range))
    }
    result.sortBy {
        it.position.first
    }
    if (result.isEmpty()) {
        return listOf(
            TextNode(PLAIN, str, null, 0..str.length)
        )
    }
    var index = -1
    for (i in 0..<result.count()) {
        val ele = result[i]
        result.add(
            TextNode(PLAIN, str.substring(index + 1, ele.position.first), null, index..ele.position.first)
        )
        index = ele.position.last
    }
    if (index < str.length) {
        result.add(
            TextNode(PLAIN, str.substring(index + 1, str.length), null, index..str.length)
        )
    }
    result.sortBy {
        it.position.first
    }
    return result
}

fun List<TextNode>.toOriginalString(): String {
    return buildString {
        this@toOriginalString.forEach { v ->
            append(
                when (v.type) {
                    PLAIN -> v.value!!
                    JUMP_URI -> "[[jumpuri:${v.value}>${v.metadata}]]"
                    NOTATION -> "[[rb:${v.value}>${v.metadata}]]"
                    UPLOAD_IMAGE -> "[uploadedimage:${v.value}]"
                    PIXIV_IMAGE -> "[pixivimage:${v.value}]"
                    NEW_PAGE -> "[newpage]"
                    TITLE -> "[chapter:${v.value}]"
                    JUMP_PAGE -> "[jump:${v.value}]"
                }
            )
        }
    }
}

private fun String.findAllJumpPage(): List<MatchResult> {
    val result = JUMP_PAGE_REGEX.findAll(this)
    return result.toList()
}

private fun String.findAllChapter(): List<MatchResult> {
    val result = CHAPTER_REGEX.findAll(this)
    return result.toList()
}


private fun String.findAllNewPage(): List<MatchResult> {
    val result = NEW_PAGE_REGEX.findAll(this)
    return result.toList()
}

private fun String.findAllPixivImage(): List<MatchResult> {
    val result = PIXIV_IMAGE_REGEX.findAll(this)
    return result.toList()
}

private fun String.findAllUploadImage(): List<MatchResult> {
    val result = UPLOAD_IMAGE_REGEX.findAll(this)
    return result.toList()
}

private fun String.findAllNotation(): List<MatchResult> {
    val result = NOTATION_REGEX.findAll(this)
    return result.toList()
}


private fun String.findAllJumpUri(): List<MatchResult> {
    val result = JUMP_URI_REGEX.findAll(this)
    return result.toList()
}