package com.example.mystoryapp.view.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.example.mystoryapp.R
import com.google.android.material.textfield.TextInputEditText
class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do nothing.
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do nothing.
            }

            override fun afterTextChanged(p0: Editable?) {
                error = if (p0!!.contains("@")) null else context.getString(R.string.alert_invalid_email)

            }
        })
    }
}