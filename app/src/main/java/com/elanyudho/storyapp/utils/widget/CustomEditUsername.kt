package com.elanyudho.storyapp.utils.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.elanyudho.storyapp.R
import com.elanyudho.storyapp.utils.extensions.dp

class CustomEditUsername : AppCompatEditText {

    private lateinit var iconStartDrawable: Drawable


    constructor(context: Context) : super(context) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        iconStartDrawable =
            ContextCompat.getDrawable(context, R.drawable.custom_username_icon) as Drawable

        settingHint()
        showDefaultDrawable()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when {
                    s.isEmpty() -> {
                        error = context.getString(R.string.input_username)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.

            }
        })
    }

    private fun showDefaultDrawable() {
        setButtonDrawables(startOfTheText = iconStartDrawable)
    }


    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )

        compoundDrawablePadding = 12.dp
    }

    private fun settingHint() {
        hint = context.getString(R.string.input_username)
    }
}