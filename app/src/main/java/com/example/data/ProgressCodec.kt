package com.example.data

import com.example.model.AnswerKind
import com.example.model.AttemptRecord
import com.example.model.CircuitProblem
import com.example.model.CircuitTopology
import com.example.model.ComplexNum
import com.example.model.Difficulty
import com.example.model.ProblemCategory

internal object ProgressCodec {

    private const val MAX_ATTEMPTS = 30

    fun encodeAttempts(attempts: List<AttemptRecord>): String {
        val sb = StringBuilder()
        sb.append('[')
        val bounded = attempts.take(MAX_ATTEMPTS)
        bounded.forEachIndexed { index, record ->
            if (index > 0) sb.append(',')
            appendAttemptJson(sb, record)
        }
        sb.append(']')
        return sb.toString()
    }

    fun decodeAttempts(raw: String): List<AttemptRecord> {
        if (raw.isBlank()) return emptyList()
        val parsed = runCatching { JsonReader(raw).parseValue() }.getOrNull() as? List<*> ?: return emptyList()
        return parsed.mapNotNull { item ->
            @Suppress("UNCHECKED_CAST")
            (item as? Map<String, Any?>)?.let(::mapToAttempt)
        }.take(MAX_ATTEMPTS)
    }

    private fun appendAttemptJson(sb: StringBuilder, r: AttemptRecord) {
        sb.append('{')
        appendKeyString(sb, "id", r.problemId); sb.append(',')
        appendKeyString(sb, "code", r.problemCode); sb.append(',')
        appendKeyString(sb, "title", r.problemTitle); sb.append(',')
        appendKeyString(sb, "sub", r.submittedAnswer); sb.append(',')
        sb.append("\"ok\":").append(r.isCorrect).append(',')
        sb.append("\"xp\":").append(r.xpEarned).append(',')
        sb.append("\"ts\":").append(r.timestampMillis)
        r.drillSnapshot?.let { snap ->
            sb.append(",\"snap\":")
            appendProblemJson(sb, snap)
        }
        sb.append('}')
    }

    private fun appendProblemJson(sb: StringBuilder, p: CircuitProblem) {
        sb.append('{')
        appendKeyString(sb, "id", p.id); sb.append(',')
        appendKeyString(sb, "code", p.code); sb.append(',')
        appendKeyString(sb, "title", p.title); sb.append(',')
        appendKeyString(sb, "cat", p.category.name); sb.append(',')
        appendKeyString(sb, "diff", p.difficulty.name); sb.append(',')
        appendKeyString(sb, "topo", p.topology.name); sb.append(',')
        appendKeyString(sb, "prompt", p.prompt); sb.append(',')
        appendKeyString(sb, "p1", p.parametersLine1); sb.append(',')
        appendKeyString(sb, "p2", p.parametersLine2); sb.append(',')
        appendKeyString(sb, "mt", p.methodTitle); sb.append(',')
        appendKeyString(sb, "mn", p.methodNote); sb.append(',')
        appendKeyString(sb, "ih", p.inputHint); sb.append(',')
        appendKeyString(sb, "ak", p.answerKind.name); sb.append(',')
        sb.append("\"tol\":").append(p.tolerance).append(',')
        appendKeyString(sb, "unit", p.unit)
        sb.appendIfPresent(p.expectedComplex, p.expectedReal)
        sb.append(",\"proc\":").append(p.isProcedural)
        p.sourceProblemId?.let { srcId ->
            sb.append(',')
            appendKeyString(sb, "srcId", srcId)
        }
        p.sourceProblemCode?.let { srcCode ->
            sb.append(',')
            appendKeyString(sb, "srcCode", srcCode)
        }
        sb.append(",\"hints\":")
        appendStringList(sb, p.hints)
        sb.append(",\"steps\":")
        appendStringList(sb, p.workedSteps)
        sb.append('}')
    }

    private fun StringBuilder.appendIfPresent(complex: ComplexNum?, real: Double?): StringBuilder {
        if (complex != null) {
            append(",\"re\":").append(complex.re)
            append(",\"im\":").append(complex.im)
        }
        if (real != null) {
            append(",\"val\":").append(real)
        }
        return this
    }

    private fun appendStringList(sb: StringBuilder, list: List<String>) {
        sb.append('[')
        list.forEachIndexed { idx, s ->
            if (idx > 0) sb.append(',')
            appendJsonString(sb, s)
        }
        sb.append(']')
    }

    private fun appendKeyString(sb: StringBuilder, key: String, value: String) {
        appendJsonString(sb, key)
        sb.append(':')
        appendJsonString(sb, value)
    }

    private fun appendJsonString(sb: StringBuilder, value: String) {
        sb.append('"')
        for (ch in value) {
            when (ch) {
                '\\' -> sb.append("\\\\")
                '"' -> sb.append("\\\"")
                '\n' -> sb.append("\\n")
                '\r' -> sb.append("\\r")
                '\t' -> sb.append("\\t")
                '\b' -> sb.append("\\b")
                '\u000C' -> sb.append("\\f")
                else -> {
                    if (ch.code < 0x20) {
                        sb.append(String.format(java.util.Locale.US, "\\u%04x", ch.code))
                    } else {
                        sb.append(ch)
                    }
                }
            }
        }
        sb.append('"')
    }

    private fun mapToAttempt(obj: Map<String, Any?>): AttemptRecord? {
        val id = obj["id"] as? String ?: return null
        val code = obj["code"] as? String ?: ""
        val title = obj["title"] as? String ?: ""
        val sub = obj["sub"] as? String ?: ""
        val ok = obj["ok"] as? Boolean ?: false
        val xp = (obj["xp"] as? Number)?.toInt() ?: 0
        val ts = (obj["ts"] as? Number)?.toLong() ?: 0L
        @Suppress("UNCHECKED_CAST")
        val snap = (obj["snap"] as? Map<String, Any?>)?.let(::mapToProblem)
        return AttemptRecord(
            problemId = id,
            problemCode = code,
            problemTitle = title,
            submittedAnswer = sub,
            isCorrect = ok,
            xpEarned = xp,
            timestampMillis = ts,
            drillSnapshot = snap
        )
    }

    private fun mapToProblem(obj: Map<String, Any?>): CircuitProblem? {
        val id = obj["id"] as? String ?: return null
        val code = obj["code"] as? String ?: return null
        val title = obj["title"] as? String ?: return null
        val cat = enumValueOrNull<ProblemCategory>(obj["cat"] as? String) ?: return null
        val diff = enumValueOrNull<Difficulty>(obj["diff"] as? String) ?: return null
        val topo = enumValueOrNull<CircuitTopology>(obj["topo"] as? String) ?: return null
        val ak = enumValueOrNull<AnswerKind>(obj["ak"] as? String) ?: return null
        val re = (obj["re"] as? Number)?.toDouble()
        val im = (obj["im"] as? Number)?.toDouble()
        val expectedComplex = if (re != null && im != null) ComplexNum(re, im) else null
        val expectedReal = (obj["val"] as? Number)?.toDouble()
        val hints = (obj["hints"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
        val steps = (obj["steps"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

        return CircuitProblem(
            id = id,
            code = code,
            title = title,
            category = cat,
            difficulty = diff,
            topology = topo,
            prompt = obj["prompt"] as? String ?: "",
            parametersLine1 = obj["p1"] as? String ?: "",
            parametersLine2 = obj["p2"] as? String ?: "",
            methodTitle = obj["mt"] as? String ?: "",
            methodNote = obj["mn"] as? String ?: "",
            inputHint = obj["ih"] as? String ?: "",
            answerKind = ak,
            expectedComplex = expectedComplex,
            expectedReal = expectedReal,
            tolerance = (obj["tol"] as? Number)?.toDouble() ?: 0.03,
            unit = obj["unit"] as? String ?: "",
            hints = hints,
            workedSteps = steps,
            isProcedural = obj["proc"] as? Boolean ?: true,
            sourceProblemId = obj["srcId"] as? String,
            sourceProblemCode = obj["srcCode"] as? String
        )
    }

    private inline fun <reified T : Enum<T>> enumValueOrNull(name: String?): T? {
        if (name == null) return null
        return enumValues<T>().firstOrNull { it.name == name }
    }

    private class JsonReader(private val input: String) {
        private var pos = 0

        fun parseValue(): Any? {
            skipWhitespace()
            if (pos >= input.length) return null
            return when (val ch = input[pos]) {
                '{' -> parseObject()
                '[' -> parseArray()
                '"' -> parseString()
                't', 'f' -> parseBoolean()
                'n' -> parseNull()
                '-', in '0'..'9' -> parseNumber()
                else -> throw IllegalArgumentException("Unexpected char $ch at $pos")
            }
        }

        private fun parseObject(): Map<String, Any?> {
            pos++ // '{'
            val map = linkedMapOf<String, Any?>()
            skipWhitespace()
            if (pos < input.length && input[pos] == '}') {
                pos++
                return map
            }
            while (pos < input.length) {
                skipWhitespace()
                val key = parseString()
                skipWhitespace()
                require(pos < input.length && input[pos] == ':')
                pos++
                val value = parseValue()
                map[key] = value
                skipWhitespace()
                if (pos < input.length && input[pos] == ',') {
                    pos++
                } else if (pos < input.length && input[pos] == '}') {
                    pos++
                    return map
                } else {
                    throw IllegalArgumentException("Malformed JSON object at $pos")
                }
            }
            return map
        }

        private fun parseArray(): List<Any?> {
            pos++ // '['
            val list = mutableListOf<Any?>()
            skipWhitespace()
            if (pos < input.length && input[pos] == ']') {
                pos++
                return list
            }
            while (pos < input.length) {
                list.add(parseValue())
                skipWhitespace()
                if (pos < input.length && input[pos] == ',') {
                    pos++
                } else if (pos < input.length && input[pos] == ']') {
                    pos++
                    return list
                } else {
                    throw IllegalArgumentException("Malformed JSON array at $pos")
                }
            }
            return list
        }

        private fun parseString(): String {
            require(pos < input.length && input[pos] == '"')
            pos++
            val sb = StringBuilder()
            while (pos < input.length) {
                val ch = input[pos++]
                when (ch) {
                    '"' -> return sb.toString()
                    '\\' -> {
                        val esc = input[pos++]
                        when (esc) {
                            '"' -> sb.append('"')
                            '\\' -> sb.append('\\')
                            '/' -> sb.append('/')
                            'b' -> sb.append('\b')
                            'f' -> sb.append('\u000C')
                            'n' -> sb.append('\n')
                            'r' -> sb.append('\r')
                            't' -> sb.append('\t')
                            'u' -> {
                                val hex = input.substring(pos, pos + 4)
                                sb.append(hex.toInt(16).toChar())
                                pos += 4
                            }
                            else -> sb.append(esc)
                        }
                    }
                    else -> sb.append(ch)
                }
            }
            throw IllegalArgumentException("Unterminated JSON string")
        }

        private fun parseNumber(): Number {
            val start = pos
            while (pos < input.length) {
                val c = input[pos]
                if (c in '0'..'9' || c == '-' || c == '+' || c == '.' || c == 'e' || c == 'E') {
                    pos++
                } else {
                    break
                }
            }
            val token = input.substring(start, pos)
            return if (token.contains('.') || token.contains('e', ignoreCase = true)) {
                token.toDouble()
            } else {
                token.toLongOrNull() ?: token.toDouble()
            }
        }

        private fun parseBoolean(): Boolean {
            return if (input.startsWith("true", pos)) {
                pos += 4
                true
            } else if (input.startsWith("false", pos)) {
                pos += 5
                false
            } else {
                throw IllegalArgumentException("Invalid boolean at $pos")
            }
        }

        private fun parseNull(): Any? {
            require(input.startsWith("null", pos))
            pos += 4
            return null
        }

        private fun skipWhitespace() {
            while (pos < input.length && input[pos].isWhitespace()) pos++
        }
    }
}
