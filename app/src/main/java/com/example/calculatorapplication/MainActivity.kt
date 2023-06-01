package com.example.calculatorapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.calculatorapplication.databinding.ActivityMainBinding
//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var canAddOperation = false
    private var canAddDecimal = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun equalsAction(view: View) = binding.run {
        resultsTV.text = calculateResults()
    }
    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""

        val timeDivision = timeDivisionCalculate(digitsOperators)
        if(timeDivision.isEmpty()) return ""

        val result = addSubtractionCalculate(timeDivision)
        return result.toString()
    }

    private fun addSubtractionCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timeDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('X') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size
        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float
                when(operator)
                {
                    'X' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i+1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i+1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }

                }
            }
            if(i > restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }


    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in binding.workingsTV.text)
        {
            if(character.isDigit() || character == '.')
            {
                currentDigit += character
            }
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if(currentDigit != "")
            list.add(currentDigit.toFloat())
        return list
    }
    fun allClear(view: View) {
        binding.workingsTV.text = ""
        binding.resultsTV.text = ""
    }
    fun backSpace(view: View) {
        val length = binding.workingsTV.length()
        if(length>0)
        {
            binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length-1)
        }
    }
    fun numberAction(view: View) {
        if(view is Button)
        {
            if(view.text == ".")
            {
                if(canAddDecimal)
                {
                    binding.workingsTV.append(view.text)
                }
                canAddDecimal = false
            }
            else
            {
                binding.workingsTV.append(view.text)
            }

            canAddOperation = true
        }
    }
    fun operationAction(view: View) {
        if(view is Button && canAddOperation)
        {
            binding.workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }
}