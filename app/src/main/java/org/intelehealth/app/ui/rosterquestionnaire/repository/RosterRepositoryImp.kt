package org.intelehealth.app.ui.rosterquestionnaire.repository

import android.content.Context
import org.intelehealth.app.R
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.ui.rosterquestionnaire.model.PregnancyOutComeViewQuestion
import javax.inject.Inject

class RosterRepositoryImp @Inject constructor() : RosterRepository {
    private var context: Context = IntelehealthApplication.getAppContext()

    fun getOutcomeQuestionList(): ArrayList<PregnancyOutComeViewQuestion> {
        val list = ArrayList<PregnancyOutComeViewQuestion>()
        list.apply {
            add(
                PregnancyOutComeViewQuestion(
                    layoutId = R.layout.item_spinner_view,
                    question = "What was the outcome of your pregnancy?",
                    spinnerItem = context.resources.getStringArray(R.array.outcomes).toList()
                )
            )
            add(
                PregnancyOutComeViewQuestion(
                    layoutId = R.layout.item_spinner_view,
                    question = "What was the year of pregnancy outcome? ((a) for pregnant ladies, mention the date when conceived (b) in case of other options mention the date of delivery/miscarriage)",
                    spinnerItem = context.resources.getStringArray(R.array.outcomes).toList()
                )
            )
            add(
                PregnancyOutComeViewQuestion(
                    layoutId = R.layout.item_date_picker_view,
                    question = "What was the year of pregnancy outcome? ((a) for pregnant ladies, mention the date when conceived (b) in case of other options mention the date of delivery/miscarriage)",

                    )
            )
            add(
                PregnancyOutComeViewQuestion(
                    layoutId = R.layout.item_spinner_view,
                    question = "How many months did this pregnancy last?",
                    spinnerItem = context.resources.getStringArray(R.array.outcomes).toList()
                )
            )
        }
        return list
    }
}
