package org.intelehealth.app.utilities.extensions

import org.intelehealth.app.ui.baseline_survey.model.Baseline

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

fun String.storeReasonIfAnswerIsPositive(answer: String): String {
    return if (answer == "Yes") {
        this
    } else {
        ""
    }
}

fun String.storeHyphenOrRelation(relation: String): String =
    if (this == "Yes") {
        "-"
    } else {
        relation
    }

fun String.getHyphenOrRelation(baseline: Baseline) {
    if (this == "-") {
        baseline.headOfHousehold = "Yes"
        baseline.relationWithHousehold = ""
    } else {
        baseline.headOfHousehold = "No"
        baseline.relationWithHousehold = this
    }
}

fun String.storeCultivableLandValue(landValue: String): String = "$landValue $this"

fun String.getCultivableLandValue(): String = this.split(" ")[0]

// introduced try-catch since it created parsing error
fun String.getCultivableLandUnit(): String {
    var result = this
    try {
        result = this.split(" ")[1]
    } catch (e: Exception){
        println(">>>>>>>>>> error while parsing - getCultivableLandUnit()")
    }
    return result
}