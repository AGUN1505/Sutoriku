package com.dicoding.sutoriku.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.*
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.sutoriku.R

class EdtTxtPass @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {
    private var showPassword: Drawable =
        ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24) as Drawable
    private var hidePassword: Drawable =
        ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24) as Drawable
    private var isPasswordVisible = false

    init {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isPasswordVisible) {
                    if (p0.toString().isNotEmpty()) {
                        showButton()
                    } else {
                        hideButton()
                    }
                } else {
                    hideButtonHide()
                }

                if (p0.toString().isEmpty()) {
                    hideButton()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length < 8) {
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                } else {
                    error = null
                }
            }
        })
    }

    private fun hideButtonHide() {
        setButtonDrawables(endOfTheText = showPassword)
    }

    private fun hideButton() {
        setButtonDrawables()
    }

    private fun showButton() {
        setButtonDrawables(endOfTheText = hidePassword)
    }

    private fun setButtonDrawables(endOfTheText: Drawable? = null) {
        setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            endOfTheText,
            null
        )
    }

    private fun passwordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            showButton()
            transformationMethod = null
        } else {
            hideButtonHide()
            transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val hideButtonStart: Float
            val hideButtonEnd: Float
            var isHideButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                hideButtonEnd = (hidePassword.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < hideButtonEnd -> isHideButtonClicked = true
                }
            } else {
                hideButtonStart = (width - paddingEnd - hidePassword.intrinsicWidth).toFloat()
                when {
                    event.x > hideButtonStart -> isHideButtonClicked = true
                }
            }
            if (isHideButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        passwordVisibility()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }
}




