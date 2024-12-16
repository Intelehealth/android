package org.intelehealth.app.utilities.extensions

import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout

fun LinearLayout.validateCheckboxes(): Boolean {
    var isSelected: Boolean = false
    for (i in 0 until childCount) {
        val currentView: View = getChildAt(i)
        if (currentView is CheckBox && currentView.isChecked) {
            isSelected = true
            break
        }
    }

    return isSelected
}