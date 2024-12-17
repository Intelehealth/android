package org.intelehealth.app.utilities.extensions

import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray

fun LinearLayout.validateCheckboxes(): Boolean {
    var isSelected = false
    for (i in 0 until childCount) {
        val currentView: View = getChildAt(i)
        if (currentView is CheckBox && currentView.isChecked) {
            isSelected = true
            break
        }
    }

    return isSelected
}

fun LinearLayout.getTextIfVisible(editText: TextInputEditText): String = if (this.isVisible) {
    editText.text.toString()
} else {
    ""
}

fun LinearLayout.getSelectedCheckboxes(): String {
    val result = JSONArray()

    for (i in 0 until this.childCount) {
        val child = this.getChildAt(i)
        if (child is CheckBox && child.isChecked) {
            val text = child.text.toString()
            result.put(text)
        }
    }

    return result.toString()
}

fun LinearLayout.setSelectedCheckboxes(data: String) {
    val normalizedData = data.replace("\\/", "/")
    for (i in 0 until this.childCount) {
        val child = this.getChildAt(i)
        if (child is CheckBox) {
            child.isChecked = normalizedData.contains(child.text.toString())
        }
    }
}