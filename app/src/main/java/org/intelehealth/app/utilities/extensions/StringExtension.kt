package org.intelehealth.app.utilities.extensions

fun String.storeHyphenIfEmpty(): String {
    return ifEmpty {
        "-"
    }
}

fun String.returnEmptyIfHyphen(): String {
    return if (this == "-") {
        ""
    } else {
        this
    }
}