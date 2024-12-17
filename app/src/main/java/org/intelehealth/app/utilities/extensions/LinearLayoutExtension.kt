package org.intelehealth.app.utilities.extensions

import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText

fun LinearLayout.getTextIfVisible(editText: TextInputEditText): String = if (this.isVisible) {
    editText.text.toString()
} else {
    ""
}