package org.example.app

/**
 * PUBLIC_INTERFACE
 * A simple sequential-evaluation calculator for basic arithmetic.
 * Use inputDigit, inputDot, applyOp, equals, clear, and backspace.
 */
class Calculator {
    var display: String = "0"
        private set
    var history: String = ""
        private set
    var isError: Boolean = false
        private set

    private var current: String = "0"
    private var acc: Double? = null
    private var op: Char? = null

    fun inputDigit(d: Char) {
        if (isError) return
        if (!d.isDigit()) return
        current = if (current == "0") d.toString() else current + d
        display = current
    }

    fun inputDot() {
        if (isError) return
        if (!current.contains(".")) {
            current += if (current.isEmpty()) "0." else "."
            display = current
        }
    }

    fun applyOp(next: Char) {
        if (isError) return
        val value = current.toDoubleOrNull()
        if (acc == null) {
            acc = value ?: 0.0
        } else if (value != null && op != null) {
            val res = compute(acc!!, value, op!!)
            if (res == null) {
                setError()
                return
            }
            acc = res
        }
        op = next
        current = "0"
        history = buildHistory()
        display = current
    }

    fun equals() {
        if (isError) return
        val value = current.toDoubleOrNull()
        if (acc != null && op != null && value != null) {
            val res = compute(acc!!, value, op!!)
            if (res == null) {
                setError()
                return
            }
            acc = null
            op = null
            current = stripTrailingZeros(res)
            display = current
            history = ""
        }
    }

    fun clear() {
        isError = false
        current = "0"
        acc = null
        op = null
        display = "0"
        history = ""
    }

    fun backspace() {
        if (isError) return
        current = if (current.length <= 1) "0" else current.dropLast(1)
        display = current
    }

    private fun buildHistory(): String {
        val left = acc?.toString()?.trimEnd('0')?.trimEnd('.') ?: ""
        val opr = op?.toString() ?: ""
        return if (left.isNotEmpty() && opr.isNotEmpty()) "$left $opr" else ""
    }

    private fun setError() {
        isError = true
        display = "Error"
        history = ""
        current = "0"
        acc = null
        op = null
    }

    private fun compute(a: Double, b: Double, op: Char): Double? {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '×' -> a * b
            '÷' -> if (b == 0.0) null else a / b
            else -> null
        }
    }

    private fun stripTrailingZeros(value: Double): String {
        val s = value.toString()
        return if (s.contains(".")) s.trimEnd('0').trimEnd('.') else s
    }
}
