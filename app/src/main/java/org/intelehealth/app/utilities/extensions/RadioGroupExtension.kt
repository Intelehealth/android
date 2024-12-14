package org.intelehealth.app.utilities.extensions

import android.widget.RadioGroup

fun RadioGroup.validate(): Boolean {
    return checkedRadioButtonId != -1
}