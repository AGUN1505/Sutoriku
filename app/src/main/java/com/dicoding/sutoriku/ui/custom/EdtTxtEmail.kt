package com.dicoding.sutoriku.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.*
import android.util.AttributeSet
import android.view.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.sutoriku.R

class EdtTxtEmail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var removeButtonImg: Drawable

    init {
        removeButtonImg =
            ContextCompat.getDrawable(context, R.drawable.baseline_delete_forever_24) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) showClearButton() else hideClearButton()

                if (isEmailValid(p0.toString())) {
                    error = null
                } else {
                    setError(context.getString(R.string.invalid_email), null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = Regex("^\\S+@\\S+\\.\\S+\$")
        return emailPattern.matches(email)
    }

    private fun hideClearButton() {
        buttonDraw()
    }

    private fun showClearButton() {
        buttonDraw(endOfTheText = removeButtonImg)
    }

    private fun buttonDraw(endOfTheText: Drawable? = null) {
        setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            endOfTheText,
            null
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Email"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTouch(p0: View?, p1: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val removeBtnStart: Float
            val removeBtnEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                removeBtnEnd = (removeButtonImg.intrinsicWidth + paddingStart).toFloat()
                when {
                    p1.x < removeBtnEnd -> isClearButtonClicked = true
                }
            } else {
                removeBtnStart = (width - paddingEnd - removeButtonImg.intrinsicWidth).toFloat()
                when {
                    p1.x > removeBtnStart -> isClearButtonClicked = true
                }
            }

            if (isClearButtonClicked) {
                when (p1.action) {
                    MotionEvent.ACTION_DOWN -> {
                        removeButtonImg = ContextCompat.getDrawable(
                            context,
                            R.drawable.baseline_delete_forever_24
                        ) as Drawable
                        showClearButton()
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        removeButtonImg = ContextCompat.getDrawable(
                            context,
                            R.drawable.baseline_delete_forever_24
                        ) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }

                    else -> return false
                }
            } else return false
        }
        return false
    }
}