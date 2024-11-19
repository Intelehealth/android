package org.intelehealth.app.ui.filter

import android.text.InputFilter
import android.text.Spanned

// Filter to allow Characters, Digits, Special Characters, and Emojis
class CorrespondingAddressInputFilter : InputFilter {

    private val allowedChars = "[a-zA-Z0-9\\s!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?]"

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {
        val input = source.subSequence(start, end)
        val filteredInput = input.filter { it.toString().matches(allowedChars.toRegex()) }
        return filteredInput
    }

}