package com.ibrahimf.coffeebean.util

import android.text.TextUtils
import androidx.core.widget.TextViewCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ibrahimf.coffeebean.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


fun convertMilliSecondsToDate(dateMilliseconds: Long): String{
    val format = SimpleDateFormat("d MMM HH:mm", Locale.getDefault())
    return format.format(Date(dateMilliseconds))
}



enum class InputTypes {NAME, CITY}

fun TextInputLayout.isValid(textInput: TextInputEditText, validationTypes: InputTypes): Boolean {
    val text = textInput.text.toString()
    when(validationTypes){
        InputTypes.NAME -> {
            if (TextUtils.isEmpty(text)){
                this.error = this.context.getString(R.string.not_empty_field)
                return false
            }
        }
        InputTypes.CITY -> {
            if (TextUtils.isEmpty(text)){
                this.error = this.context.getString(R.string.not_empty_field)
                return false
            }

        }
    }

    this.error = null
    return true

}



