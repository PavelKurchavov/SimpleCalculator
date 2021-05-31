package com.example.calculator

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.MotionEvent
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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val buttons = listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnAC,btnBackspace, btnPoint, btnPlus, btnMinus, btnMultiply, btnDivision, btnEqual, btnPercent)
        buttons.forEach { it.setOnClickListener(this) }
    }

    override fun onClick(v: View?) = with(expressionTextView) {
        when ((v as Button).id) {
            R.id.btnAC -> text = ""
            R.id.btnBackspace -> if (text.isNotEmpty()) text = text.substring(0, text.length - 1)
            R.id.btnEqual, R.id.btnPercent -> {
                text = interpreter.calculateResult(v, text.toString())
                archivedExpressionsTextView.append("${v.text} ${text}\n")
            }
            else -> append(v.text)
        }
    }

    private fun Interpreter.calculateResult(button: Button, expression: String): String {
        val validExpression = expression.replace("×", "*").replace("÷", "/")
        return try {
            if (button.id == R.id.btnPercent) eval("(${validExpression})/100.0").toString() else eval("1.0 * $validExpression").toString()
        } catch (e: Exception) {
            incorrectExpressionHandle()
            ""
        }
    }

    private fun incorrectExpressionHandle() {
        val toast = Toast.makeText(this, "Incorrect expression!", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 96)
        toast.show()
        expressionTextView.text = ""
    }
}


