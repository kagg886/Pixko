package top.kagg886.pixko.module.novel.parser

// 或在将来被使用
// private val JUMP_URI_REGEX = "\\[\\[jumpuri:(.*)>(.*)]]".toRegex()
private val NOTATION_REGEX = "\\[\\[rb:(.*)>(.*)]]".toRegex()

private val TAG_REGEX = """\[{1,2}(\w+)(?::([^\[\]]+))?]{1,2}""".toRegex()

private fun tagToNode(
    name: String,
    rawValue: String,
    position: IntRange,
): NovelNode =
    when (name) {
        "jump" -> JumpPageNode(rawValue.toInt(), position)
        "newpage" -> NewPageNode(position)
        "uploadedimage" -> UploadImageNode(rawValue, position)
        "pixivimage" -> {
            val split = rawValue.split("-").map { it.toInt() }
            val id = split.first()
            val pageIndex = split.getOrElse(1) { 1 } - 1
            PixivImageNode(id, pageIndex, position)
        }

        "jumpuri" -> {
            val (text, url) = rawValue.split(">", limit = 2).also {
                check(it.size == 2) { "Cannot find separator(>)" }
            }
            check(url.startsWith("http")) {
                "Invalid url:$url"
            }
            JumpUriNode(text, url, position)
        }

        "rb" -> {
            val (notation, text) = rawValue.split(">", limit = 2).also {
                check(it.size == 2) { "Cannot find separator(>)" }
            }
            TextNode(text.toNotatedText(notation).asSingle(), position)
        }

        "chapter" -> {
            val textNodes = mutableListOf<CombinedTextNode>()
            var lastIndex = 0
            NOTATION_REGEX.findAll(rawValue).forEach { result ->
                val position = result.range
                if (position.first > lastIndex) {
                    val plain = rawValue.substring(lastIndex, position.first).toPlainText()
                    textNodes.add(plain)
                }
                val (notation, text) = result.destructured
                val notated = text.toNotatedText(notation)
                textNodes.add(notated)
                lastIndex = position.last + 1
            }
            if (lastIndex < rawValue.length) {
                val plain = rawValue.substring(lastIndex).toPlainText()
                textNodes.add(plain)
            }
            TitleNode(textNodes.asCombinedText(), position)
        }

        else -> error("Unknown tag name:$name")
    }

fun createNovelData(str: String): List<NovelNode> {
    val nodes = mutableListOf<NovelNode>()
    var lastIndex = 0

    TAG_REGEX.findAll(str).forEach { result ->
        val position = result.range

        if (position.first > lastIndex) {
            val plainText = str.substring(lastIndex, position.first).toPlainText().asSingle()
            nodes.add(TextNode(plainText, lastIndex..position.first))
        }

        val (name, rawValue) = result.destructured
        try {
            val node = tagToNode(name, rawValue, position)
            nodes.add(node)
        } catch (_: Exception) { // 处理未知tag
            nodes.add(TextNode(result.value.toPlainText().asSingle(), position))
        }

        lastIndex = position.last + 1
    }

    if (lastIndex < str.length) {
        val plainText = str.substring(lastIndex).toPlainText().asSingle()
        nodes.add(TextNode(plainText, lastIndex..(str.length)))
    }

    return nodes
}

fun List<NovelNode>.toOriginalString(): String {
    fun CombinedText.toOriginalString() = joinToString("") {
        when (it) {
            is NotatedText -> "[[rb:${it.notation}>${it.text}]]"
            is PlainText -> it.text
        }
    }

    return buildString {
        this@toOriginalString.forEach { v ->
            append(
                when (v) {
                    is TextNode -> v.text.toOriginalString()
                    is JumpUriNode -> "[[jumpuri:${v.text}>${v.uri}]]"
                    is UploadImageNode -> "[uploadedimage:${v.url}]"
                    is PixivImageNode -> "[pixivimage:${v.id}${if (v.index != 0) "-${v.index + 1}" else ""}]"
                    is NewPageNode -> "[newpage]"
                    is TitleNode -> "[chapter:${v.text.toOriginalString()}]"
                    is JumpPageNode -> "[jump:${v.page}]"
                }
            )
        }
    }
}
