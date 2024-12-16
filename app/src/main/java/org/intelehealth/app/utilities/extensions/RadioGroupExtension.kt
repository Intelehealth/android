package org.intelehealth.app.utilities.extensions

import android.widget.RadioButton
import android.widget.RadioGroup

fun RadioGroup.validate(): Boolean {
    return checkedRadioButtonId != -1
}

fun RadioGroup.getSelectedData(): String {
    return if (checkedRadioButtonId != -1) {
        val checkedRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
        checkedRadioButton.text.toString()
    } else ""
}

fun RadioGroup.setCheckedRadioButtonByText(targetText: String) {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is RadioButton && child.text.toString() == targetText) {
            child.isChecked = true
            break
        }
    }
}