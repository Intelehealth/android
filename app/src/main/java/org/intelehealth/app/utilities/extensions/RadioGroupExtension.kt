package org.intelehealth.app.utilities.extensions

import android.widget.RadioButton
import android.widget.RadioGroup

fun RadioGroup.validate(): Boolean {
    return checkedRadioButtonId != -1
}

fun RadioGroup.getSelectedData(): String {
    val checkedRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
    return checkedRadioButton.text.toString()
}