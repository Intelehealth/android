package org.intelehealth.videolibrary.listing.activity

import android.app.Activity
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


fun ProgressBar?.checkAndHideProgressBar() {
    if (this?.visibility == View.VISIBLE) {
        this.visibility = View.GONE
    }
}

fun SwipeRefreshLayout?.checkAndHideProgressBar() {
    if (this?.isRefreshing == true) {
        this.isRefreshing = false
    }
}

val emojiFilter = InputFilter { source, start, end, dest, dstart, dend ->
    var keepOriginal = true
    val sb = StringBuilder(end - start)

    for (i in start until end) {
        val c = source[i]
        if (isCharAllowed(c) || c in listOf(
                '.',
                '&',
                '(',
                ')',
                '\'',
                '-',
                '#',
                '@',
                '%',
                '/',
                ':',
                ','
            )
        ) {
            sb.append(c)
        } else {
            keepOriginal = false
        }
    }

    if (keepOriginal) {
        null
    } else {
        if (source is Spanned) {
            val sp = SpannableString(sb)
            TextUtils.copySpansFrom(source, start, sb.length, null, sp, 0)
            sp
        } else {
            sb
        }
    }
}

private fun isCharAllowed(c: Char): Boolean {
    return c.isLetter() || c.isWhitespace() || c.isDigit()
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
