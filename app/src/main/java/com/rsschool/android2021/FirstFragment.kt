package com.rsschool.android2021

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class FirstFragment : Fragment() {

    private var generateButton: Button? = null
    private var generateButtonAction: OnTransferMinMax? = null
    private var previousResult: TextView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        generateButtonAction = context as OnTransferMinMax
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previousResult = view.findViewById(R.id.previous_result)
        generateButton = view.findViewById(R.id.generate)

        val result = arguments?.getInt(PREVIOUS_RESULT_KEY)
        previousResult?.text = "Previous result: ${result.toString()}"

        val minValueEditText = view.findViewById<EditText>(R.id.min_value).text
        val maxValueEditText = view.findViewById<EditText>(R.id.max_value).text

        generateButton?.setOnClickListener {
            checkAllFields(minValueEditText.toString(), maxValueEditText.toString())
        }
    }

    private fun checkAllFields(minValueText: String, maxValueText: String) {
        var toastMassage = checkAllStringFields(minValueText, maxValueText)
        if (toastMassage.isEmpty()) {
            toastMassage = checkAllValueFields(minValueText, maxValueText)
        } else {
            activity?.let {
                Toast.makeText(it, toastMassage, Toast.LENGTH_SHORT).show()
            }
        }
        if (toastMassage.isEmpty()) {
            toastMassage = checkAllValueFields(minValueText, maxValueText)
        } else {
            activity?.let {
                Toast.makeText(it, toastMassage, Toast.LENGTH_SHORT).show()
            }
        }
        if (toastMassage.isEmpty()) {
            (activity as MainActivity).onTransferMinMax(minValueText.toInt(), maxValueText.toInt())
        }
    }

    private fun checkAllStringFields(minValueText: String, maxValueText: String): String {
        return when {
            minValueText.isEmpty() && maxValueText.isEmpty() -> "Invalid data: Min and Max values are empty"
            minValueText.isEmpty() -> "Invalid data: Min value is empty"
            maxValueText.isEmpty() -> "Invalid data: Max value is empty"
            else -> ""
        }
    }

    //По условию генерируется результат в диапазоне [min; max],поэтому если min == max -> генерится число = min или max
    private fun checkAllValueFields(minValueText: String, maxValueText: String): String {
        val minValueInt = minValueText.toBigInteger()
        val maxValueInt = maxValueText.toBigInteger()
        return when {
            minValueInt > Int.MAX_VALUE.toBigInteger() -> "Invalid data: Min value is too large"
            maxValueInt > Int.MAX_VALUE.toBigInteger() -> "Invalid data: Max value is too large"
            minValueInt > maxValueInt -> "Invalid data: Min more Max"
            else -> ""
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(previousResult: Int): FirstFragment {
            val fragment = FirstFragment()
            val args = Bundle()
            args.putInt(PREVIOUS_RESULT_KEY, previousResult)
            fragment.arguments = args
            return fragment
        }

        private const val PREVIOUS_RESULT_KEY = "PREVIOUS_RESULT"
    }
}

