package org.intelehealth.app.utilities.extensions

import android.annotation.SuppressLint
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.github.ajalt.timberkt.Timber
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Created by Vaghela Mithun R. on 12-07-2024 - 11:05.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

fun TextInputLayout.showError(@StringRes resId: Int) {
    Timber.d { "showError" }
    error = context.getString(resId)
}

fun TextInputLayout.hideError() {
    Timber.d { "hideError" }
    isErrorEnabled = false
}

fun TextInputLayout.showError() {
    Timber.d { "showError" }
    isErrorEnabled = false
}

fun TextInputLayout.hideErrorOnTextChang(input: TextInputEditText) {
    input.doOnTextChanged { text, _, _, count ->
        Timber.d { "hideErrorOnTextChang" }
        if (text?.length!! > 0) hideError()
    }
}

fun TextInputLayout.hideDigitErrorOnTextChang(input: TextInputEditText, digit: Int) {
    input.doOnTextChanged { text, _, _, count ->
        Timber.d { "phone validation $count == $digit" }
        if (text?.length == digit) hideError() else showError()
    }
}

fun TextInputLayout.validate(input: TextInputEditText, @StringRes resId: Int): Boolean {
    return if (input.text.isNullOrEmpty()) {
        showError(resId)
        false
    } else true
}

fun TextInputLayout.validateDropDowb(input: AutoCompleteTextView, @StringRes resId: Int): Boolean {
    return if (input.text.isNullOrEmpty()) {
        showError(resId)
        false
    } else true
}

fun TextInputLayout.validateDigit(
    input: TextInputEditText,
    @StringRes resId: Int,
    minDigit: Int
): Boolean {
    return if (input.text.isNullOrEmpty() || input.text?.length!! < minDigit) {
        showError(resId)
        false
    } else true
}

fun EditText.addFilter(filter: InputFilter) {
    this.filters = this.filters + filter
}

fun TextInputLayout.validateNumberOfUsualMembers(
    usualMembersInput: TextInputEditText,
    totalMembersInput: TextInputEditText,
    @StringRes resId: Int
): Boolean {
    val usualMembers: String = usualMembersInput.text.toString()
    val totalMembers: String = totalMembersInput.text.toString()
    return if (usualMembers.isNotEmpty() || totalMembers.isNotEmpty()) {
        if (usualMembers.toInt() > totalMembers.toInt()) {
            showError(resId)
            false
        } else true
    } else false
}

fun TextInputLayout.validateIntegerDataLimits(
    valueEditText: TextInputEditText,
    startLimit: Int,
    endLimit: Int,
    @StringRes resId: Int
): Boolean {
    val input = valueEditText.text.toString()
    return if (input.isNotEmpty()) {
        val enteredValue: Int = input.toInt()
        if (enteredValue in startLimit..endLimit) {
            true
        } else {
            showError(resId)
            false
        }
    } else false
}