package top.kagg886.pixko

import kotlinx.datetime.Instant
import top.kagg886.pixko.anno.ExperimentalNovelParserAPI
import top.kagg886.pixko.module.novel.parser.createNovelData
import top.kagg886.pixko.module.novel.parser.toOriginalString
import top.kagg886.pixko.module.novel.parser.v2.createNovelDataV2
import top.kagg886.pixko.module.novel.parser.v2.toOriginalString
import top.kagg886.pixko.module.profile.CountryCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OtherTest {
    @Test
    fun testLocalDateTime() {
        println(Instant.parse("2022-07-17T13:16:49+09:00"))
    }

    @Test
    fun testNovelParserNoAnyElement() {
        val novel = """
            123456
        """.trimIndent()
        val data = createNovelData(str = novel)
        assertTrue(data.isNotEmpty())
        data.forEach(::println)
    }

    @OptIn(ExperimentalNovelParserAPI::class)
    @Test
    fun testNovelParserV2() {
        val novel = """
              [chapter:1. 引子]
              如你所见，该小说用于测试文本解析器性能
              请前往[[jumpuri:https://github.com/kagg886/Pixko > https://github.com/kagg886/Pixko]]查看项目
              [newpage]

              [chapter:2. 图片测试]
              上传图片[uploadedimage:18743458]
              p站站内图片[pixivimage:108181682]
              连续的两张图片[pixivimage:108181682][pixivimage:108181682-2]
              [newpage]

              [chapter:3. [[rb:注音>zhu yin]]测试]
              这是一个[[rb:注音 > zhu yin 1]]注音
              [newpage]

              [chapter:4. 非法标签测试]
              注音不可以嵌套注音，就像[[rb:注音 > [[rb:套注音 > taozhuyin]]]]
              注音无大于号将不会被解析[[rb:注音]]，但是有的话会被解析[[rb:注音>qwq]]
              非法站内图片会解析成纯文本：[pixivimage:]
              非法链接会解析成纯文本：[[jumpuri:https://github.com/kagg886/Pixko]]
              非法链接会解析成纯文本：[[jumpuri:>qwq]]
              非法链接会解析成纯文本：[[jumpuri:awa>qwq]]
              [newpage]

              [chapter:5. 跳转测试1]
              点击右侧文本前往[jump:6]
              [newpage]

              [chapter:6. 跳转测试2]
              点击右侧文本前往[jump:5]
        """.trimIndent()
        val data = createNovelDataV2(str = novel)

        data.forEach(::println)
        println(data.toOriginalString())

        assertEquals(novel, data.toOriginalString())
    }


    @Test
    fun testNovelParser() {
        val novel = """
              [chapter:1. 引子]
              如你所见，该小说用于测试文本解析器性能
              请前往[[jumpuri:https://github.com/kagg886/Pixko > https://github.com/kagg886/Pixko]]查看项目
              [newpage]

              [chapter:2. 图片测试]
              上传图片[uploadedimage:18743458]
              p站站内图片[pixivimage:108181682]
              连续的两张图片[pixivimage:108181682][pixivimage:108181682-2]
              [newpage]

              [chapter:3. [[rb:注音>zhu yin]]测试]
              这是一个[[rb:注音 > zhu yin 1]]注音
              [newpage]

              [chapter:4. 非法标签测试]
              注音不可以嵌套注音，就像[[rb:注音 > [[rb:套注音 > taozhuyin]]]]
              注音无大于号将不会被解析[[rb:注音]]，但是有的话会被解析[[rb:注音>qwq]]
              非法站内图片会解析成纯文本：[pixivimage:]
              非法链接会解析成纯文本：[[jumpuri:https://github.com/kagg886/Pixko]]
              非法链接会解析成纯文本：[[jumpuri:>qwq]]
              非法链接会解析成纯文本：[[jumpuri:awa>qwq]]
              [newpage]

              [chapter:5. 跳转测试1]
              点击右侧文本前往[jump:6]
              [newpage]

              [chapter:6. 跳转测试2]
              点击右侧文本前往[jump:5]
        """.trimIndent()
        val data = createNovelData(str = novel)

        data.forEach(::println)
        println(data.toOriginalString())

        assertEquals(novel, data.toOriginalString())
    }

    @Test
    fun testUnknownCountry() {
        val a = """
                            IS: "冰岛",
                            IE: "爱尔兰",
                            AZ: "阿塞拜疆",
                            AF: "阿富汗",
                            US: "美国",
                            VI: "美属维尔京群岛",
                            AS: "美属萨摩亚",
                            UM: "美国本土外小岛屿",
                            AE: "阿拉伯联合酋长国",
                            DZ: "阿尔及利亚",
                            AR: "阿根廷",
                            AW: "阿鲁巴岛",
                            AL: "阿尔巴尼亚",
                            AM: "亚美尼亚",
                            AI: "安圭拉",
                            AO: "安哥拉",
                            AG: "安提瓜和巴布达",
                            AD: "安道尔",
                            YE: "也门",
                            GB: "英国",
                            IO: "英属印度洋领地",
                            VG: "英属维尔京群岛",
                            IL: "以色列",
                            IT: "意大利",
                            IQ: "伊拉克共和国",
                            IR: "伊朗",
                            IN: "印度",
                            ID: "印度尼西亚",
                            UG: "乌干达",
                            UA: "乌克兰",
                            UZ: "乌兹别克斯坦",
                            UY: "乌拉圭",
                            EC: "厄瓜多尔",
                            EG: "埃及",
                            EE: "爱沙尼亚",
                            ET: "埃塞俄比亚",
                            ER: "厄立特里亚",
                            SV: "萨尔瓦多",
                            AU: "澳大利亚",
                            AT: "奥地利",
                            AX: "奥兰群岛",
                            OM: "阿曼",
                            NL: "荷兰",
                            GH: "加纳",
                            GG: "根西岛",
                            GY: "圭亚那",
                            KZ: "哈萨克斯坦",
                            QA: "卡塔尔",
                            CA: "加拿大",
                            CV: "佛得角",
                            GA: "加蓬",
                            CM: "喀麦隆",
                            GM: "冈比亚共和国",
                            KH: "柬埔寨",
                            MP: "北马里亚纳群岛",
                            GN: "几内亚",
                            GW: "几内亚比绍",
                            CY: "塞浦路斯",
                            CU: "古巴共和国",
                            GR: "希腊",
                            KI: "基里巴斯",
                            KG: "吉尔吉斯斯坦",
                            GT: "危地马拉",
                            GP: "瓜德罗普",
                            GU: "关岛",
                            KW: "科威特",
                            CK: "库克群岛",
                            GL: "格陵兰",
                            CX: "圣诞岛",
                            GE: "格鲁吉亚",
                            GD: "格林纳达",
                            HR: "克罗地亚",
                            KY: "开曼群岛",
                            KE: "肯尼亚",
                            CI: "科特迪瓦",
                            CC: "科科斯群岛",
                            CR: "哥斯达黎加",
                            KM: "科摩罗",
                            CO: "哥伦比亚",
                            CG: "刚果共和国",
                            CD: "刚果民主共和国",
                            SA: "沙特阿拉伯",
                            WS: "萨摩亚",
                            ST: "圣多美普林西比",
                            BL: "圣巴泰勒米",
                            ZM: "赞比亚",
                            PM: "圣皮埃尔和密克隆群岛",
                            SM: "圣马力诺共和国",
                            MF: "圣马丁岛",
                            SL: "塞拉利昂",
                            DJ: "吉布提",
                            GI: "直布罗陀",
                            JE: "泽西岛",
                            JM: "牙买加",
                            SY: "叙利亚",
                            SG: "新加坡",
                            ZW: "津巴布韦",
                            CH: "瑞士",
                            SE: "瑞典",
                            SD: "苏丹共和国",
                            SJ: "斯瓦尔巴和扬马延",
                            ES: "西班牙",
                            SR: "苏里南",
                            LK: "斯里兰卡",
                            SK: "斯洛伐克",
                            SI: "斯洛文尼亚",
                            SZ: "斯威士兰",
                            SC: "塞舌尔",
                            GQ: "赤道几内亚",
                            SN: "塞内加尔",
                            RS: "塞尔维亚",
                            KN: "圣基茨和尼维斯",
                            VC: "圣文森特和格林纳丁斯",
                            SH: "圣赫勒拿",
                            LC: "圣卢西亚",
                            SO: "索马里",
                            SB: "所罗门群岛",
                            TC: "特克斯和凯科斯群岛",
                            TH: "泰国",
                            KR: "大韩民国（韩国）",
                            TW: "台湾",
                            TJ: "塔吉克斯坦",
                            TZ: "坦桑尼亚",
                            CZ: "捷克",
                            TD: "乍得",
                            CF: "中非",
                            CN: "中华人民共和国（中国）",
                            TN: "突尼斯",
                            KP: "朝鲜民主主义人民共和国（北朝鲜）",
                            CL: "智利",
                            TV: "图瓦卢",
                            DK: "丹麦",
                            DE: "德国",
                            TG: "多哥",
                            TK: "托克劳群岛",
                            DO: "多明尼加共和国",
                            DM: "多米尼加",
                            TT: "特立尼达和多巴哥",
                            TM: "土库曼斯坦",
                            TR: "土耳其",
                            TO: "汤加",
                            NG: "尼日利亚",
                            NR: "瑙鲁",
                            NA: "纳米比亚",
                            AQ: "南极",
                            NU: "纽埃",
                            NI: "尼加拉瓜",
                            NE: "尼日尔",
                            JP: "日本",
                            EH: "西撒哈拉",
                            NC: "新喀里多尼亚",
                            NZ: "新西兰",
                            NP: "尼泊尔",
                            NF: "诺福克岛",
                            NO: "挪威",
                            BH: "巴林",
                            HT: "海地",
                            PK: "巴基斯坦",
                            VA: "梵蒂冈",
                            PA: "巴拿马",
                            VU: "瓦努阿图",
                            BS: "巴哈马",
                            PG: "巴布亚新几内亚",
                            BM: "百慕大",
                            PW: "帕劳",
                            PY: "巴拉圭",
                            BB: "巴巴多斯",
                            PS: "巴勒斯坦",
                            HU: "匈牙利",
                            BD: "孟加拉国",
                            TL: "东帝汶",
                            PN: "皮特凯恩",
                            FJ: "斐济",
                            PH: "菲律宾",
                            FI: "芬兰",
                            BT: "不丹",
                            BV: "布维岛",
                            PR: "波多黎各",
                            FO: "法罗群岛",
                            FK: "福克兰群岛",
                            BR: "巴西",
                            FR: "法国",
                            GF: "法属圭亚那",
                            PF: "法属波利尼西亚",
                            TF: "法属南部和南极领地",
                            BG: "保加利亚",
                            BF: "布基纳法索",
                            BN: "文莱",
                            BI: "布隆迪",
                            HM: "赫德岛和麦克唐纳群岛",
                            VN: "越南",
                            BJ: "贝宁",
                            VE: "委内瑞拉",
                            BY: "白俄罗斯",
                            BZ: "伯利兹",
                            PE: "秘鲁",
                            BE: "比利时",
                            PL: "波兰",
                            BA: "波斯尼亚和黑塞哥维那",
                            BW: "博茨瓦纳",
                            BO: "玻利维亚",
                            PT: "葡萄牙",
                            HK: "香港",
                            HN: "洪都拉斯",
                            MH: "马绍尔群岛",
                            MO: "澳门",
                            MK: "北马其顿",
                            MG: "马达加斯加",
                            YT: "马约特岛",
                            MW: "马拉维",
                            ML: "马里",
                            MT: "马耳他",
                            MQ: "马提尼克",
                            MY: "马来西亚",
                            IM: "马恩岛",
                            FM: "密克罗尼西亚联邦",
                            ZA: "南非共和国",
                            GS: "南乔治亚岛和南桑威奇群岛",
                            MM: "缅甸",
                            MX: "墨西哥",
                            MU: "毛里求斯",
                            MR: "毛里塔尼亚",
                            MZ: "莫桑比克",
                            MC: "摩纳哥",
                            MV: "马尔代夫",
                            MD: "摩尔多瓦",
                            MA: "摩洛哥",
                            MN: "蒙古",
                            ME: "黑山",
                            MS: "蒙特塞拉特",
                            JO: "约旦",
                            LA: "老挝",
                            LV: "拉脱维亚",
                            LT: "立陶宛",
                            LY: "利比亚",
                            LI: "列支敦士登",
                            LR: "利比里亚",
                            RO: "罗马尼亚",
                            LU: "卢森堡",
                            RW: "卢旺达",
                            LS: "莱索托",
                            LB: "黎巴嫩",
                            RE: "留尼汪",
                            RU: "俄罗斯",
                            WF: "瓦利斯富图纳",
                            BQ: "博内尔、圣尤斯特歇斯和萨巴",
                            CW: "库拉索",
                            KV: "科索沃"
        """.trimIndent()

        val b = a.split("\n").map(String::trim).associate { with(it.split(": ")) { this[0] to this[1] } }

        b.forEach { (t, u) ->
            val code = t.replace("\"", "")
            val name = u.replace("\"", "").replace(",", "")
            runCatching {
                CountryCode.fromCode(code)
            }.onFailure {
                println("(\"$code\",\"$name\")")
            }
        }
    }
}
