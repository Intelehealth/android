package org.intelehealth.app.ui.rosterquestionnaire.ui.listeners

import android.view.View
import org.intelehealth.app.ui.rosterquestionnaire.model.PregnancyOutComeViewQuestion

interface PregnancyMultiViewListener {
    fun onItemClick(
        item: PregnancyOutComeViewQuestion,
        position: Int,
        view: View,
    )
}

