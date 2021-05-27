package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import bsh.Interpreter
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val interpreter = Interpreter()

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        archivedExpressionsTextView.movementMethod = ScrollingMovementMethod()
        val buttons = listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnAC,btnBackspace, btnPoint, btnPlus, btnMinus, btnMultiply, btnDivision, btnEqual, btnPercent)
        buttons.forEach { it.setOnClickListener(this) }
    }

    override fun onClick(v: View?) = with(expressionTextView) {
        when ((v as Button).id) {
            R.id.btnAC -> text = ""
            R.id.btnBackspace -> if (text.isNotEmpty()) text = text.substring(0, text.length - 1)
            R.id.btnEqual, R.id.btnPercent -> {
                text = interpreter.calculateResult(v, text.toString()).toString()
                archivedExpressionsTextView.append("${v.text} ${text}\n")
            }
            else -> append(v.text)
        }
    }

    private fun Interpreter.calculateResult(button: Button, expression: String): Double {
        val validExpression = expression.replace("×", "*").replace("÷", "/")
        var result = 0.0
        try {
            result = when (button.id) {
                R.id.btnPercent -> eval("(${validExpression})/100.0").toString().toDouble()
                else -> eval(validExpression).toString().toDouble()
            }
        } catch (e: Exception) {
            incorrectExpressionHandle()
        }
        return result
    }

    private fun incorrectExpressionHandle() {
        val toast = Toast.makeText(this, "Incorrect expression!", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 96)
        toast.show()
        expressionTextView.text = ""
    }
}


