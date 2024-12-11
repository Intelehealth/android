package org.intelehealth.app.ui.rosterquestionnaire.model

import androidx.annotation.ArrayRes

data class PregnancyOutComeModel(
    val title: String,
    val pregnancyOutComeViewQuestion: List<PregnancyOutComeViewQuestion>,
    val isOpen: Boolean = false,
)