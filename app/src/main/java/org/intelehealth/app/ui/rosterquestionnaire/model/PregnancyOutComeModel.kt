package org.intelehealth.app.ui.rosterquestionnaire.model

data class PregnancyOutComeModel(
    val title: String,
    val roasterViewQuestion: List<RoasterViewQuestion>,
    var isOpen: Boolean = false,
    var noOfTimesPregnant: String = "0",
    var noOfPregnantPastTwoYears: String = "0",
    var noPregnancyOutcomeTwoYears: String = "0",
)