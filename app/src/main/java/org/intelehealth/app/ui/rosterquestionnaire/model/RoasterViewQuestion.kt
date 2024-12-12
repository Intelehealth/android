package org.intelehealth.app.ui.rosterquestionnaire.model

data class RoasterViewQuestion(
    val layoutId: Int,
    val data: Any? = null,
    val question: String,
    val spinnerItem: List<String>?= null,
    var spinnerPosition: Int?= null,
    var answer: String?= null,
    val errorMessage: String= "",
)