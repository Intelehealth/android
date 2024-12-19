package org.intelehealth.app.ui.baseline_survey.model

data class MedicalHistory(
    val hypertension: String = "-",
    val diabetes: String = "-",
    val arthritis: String = "-",
    val anemia: String = "-",
    val anySurgeries: String = "-",
    val reasonForSurgery: String = "-"
)
