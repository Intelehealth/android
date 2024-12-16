package org.intelehealth.app.ui.rosterquestionnaire.model

import org.intelehealth.app.ui.rosterquestionnaire.utilities.RoasterQuestionView

data class RoasterViewQuestion(
    val layoutId: RoasterQuestionView,
    val data: Any? = null,
    val question: String,
    val spinnerItem: List<String>?= null,
    var spinnerPosition: Int?= null,
    var answer: String?= null,
    val errorMessage: String= "",
)