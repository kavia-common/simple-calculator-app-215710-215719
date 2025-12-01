package org.example.app

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

/**
 * MainActivity renders a simple calculator with Ocean Professional styling.
 * UI elements are defined in activity_main.xml and connected here for behavior.
 *
 * Behavior:
 * - Supports sequential binary operations (+, -, ×, ÷) with left-to-right evaluation.
 * - Handles decimals and prevents multiple decimal points in a number segment.
 * - Backspace deletes the last character in the current input.
 * - Clear resets the calculator state.
 * - Division by zero shows "Error" and locks until Clear is pressed.
 */
class MainActivity : Activity() {

    private lateinit var tvHistory: TextView
    private lateinit var tvDisplay: TextView

    // Calculator state
    private var currentInput: String = "0"
    private var accumulator: Double? = null
    private var pendingOp: Char? = null
    private var error: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHistory = findViewById(R.id.tvHistory)
        tvDisplay = findViewById(R.id.tvDisplay)

        // Ensure display starts correct
        updateDisplay()

        // Numeric buttons
        val digitButtons = listOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2",
            R.id.btn3 to "3", R.id.btn4 to "4", R.id.btn5 to "5",
            R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8",
            R.id.btn9 to "9"
        )
        digitButtons.forEach { (id, text) ->
            findViewById<Button>(id).setOnClickListener { onDigit(text) }
        }
        findViewById<Button>(R.id.btnDot).setOnClickListener { onDot() }

        // Operations
        findViewById<Button>(R.id.btnAdd).setOnClickListener { onOperator('+') }
        findViewById<Button>(R.id.btnSub).setOnClickListener { onOperator('-') }
        findViewById<Button>(R.id.btnMul).setOnClickListener { onOperator('×') }
        findViewById<Button>(R.id.btnDiv).setOnClickListener { onOperator('÷') }

        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEquals() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { onClear() }
        findViewById<ImageButton>(R.id.btnBackspace).setOnClickListener { onBackspace() }
    }

    // PUBLIC_INTERFACE
    fun updateDisplay() {
        /** Updates the display and history with current state using proper colors for error. */
        tvDisplay.text = currentInput
        if (error) {
            tvDisplay.setTextColor(getColor(R.color.errorColor))
        } else {
            tvDisplay.setTextColor(getColor(R.color.textColor))
        }
        val hist = buildHistory()
        tvHistory.text = hist
    }

    private fun buildHistory(): String {
        val left = accumulator?.toString()?.trimEnd('0')?.trimEnd('.') ?: ""
        val op = pendingOp?.toString() ?: ""
        return if (left.isNotEmpty() && op.isNotEmpty()) "$left $op" else ""
    }

    private fun onDigit(d: String) {
        if (error) return
        currentInput = if (currentInput == "0") d else currentInput + d
        updateDisplay()
    }

    private fun onDot() {
        if (error) return
        if (!currentInput.contains(".")) {
            currentInput += if (currentInput.isEmpty()) "0." else "."
            updateDisplay()
        }
    }

    private fun onOperator(op: Char) {
        if (error) return
        val value = currentInput.toDoubleOrNull()
        if (accumulator == null) {
            accumulator = value ?: 0.0
        } else if (value != null && pendingOp != null) {
            val res = compute(accumulator!!, value, pendingOp!!)
            if (res == null) {
                // Division by zero or error
                showError()
                return
            } else {
                accumulator = res
            }
        }
        pendingOp = op
        currentInput = "0"
        updateDisplay()
    }

    private fun onEquals() {
        if (error) return
        val value = currentInput.toDoubleOrNull()
        if (accumulator != null && pendingOp != null && value != null) {
            val res = compute(accumulator!!, value, pendingOp!!)
            if (res == null) {
                showError()
                return
            } else {
                currentInput = stripTrailingZeros(res)
                accumulator = null
                pendingOp = null
                updateDisplay()
            }
        }
    }

    private fun onClear() {
        currentInput = "0"
        accumulator = null
        pendingOp = null
        error = false
        updateDisplay()
    }

    private fun onBackspace() {
        if (error) return
        currentInput = if (currentInput.length <= 1) {
            "0"
        } else {
            currentInput.dropLast(1)
        }
        updateDisplay()
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

    private fun showError() {
        error = true
        currentInput = "Error"
        accumulator = null
        pendingOp = null
        updateDisplay()
    }
}
