package top.kagg886.pixko.module.novel.parser.v2

import top.kagg886.pixko.anno.ExperimentalNovelParserAPI
import top.kagg886.pixko.module.novel.NovelData

@ExperimentalNovelParserAPI
val NovelData.content
    get() = lazy {
        createNovelDataV2(this.text)
    }