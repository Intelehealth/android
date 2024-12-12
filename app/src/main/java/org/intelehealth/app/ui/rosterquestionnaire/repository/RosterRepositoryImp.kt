package org.intelehealth.app.ui.rosterquestionnaire.repository

import android.content.Context
import org.intelehealth.app.R
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import javax.inject.Inject

class RosterRepositoryImp @Inject constructor() : RosterRepository {
    private var context: Context = IntelehealthApplication.getAppContext()
    override fun getOutcomeQuestionList(): ArrayList<RoasterViewQuestion> {
        val list = ArrayList<RoasterViewQuestion>()
        list.apply {
            add(
                RoasterViewQuestion(
                    layoutId = R.layout.item_spinner_view,
                    question = "What was the outcome of your pregnancy?",
                    spinnerItem = context.resources.getStringArray(R.array.outcomes).toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = R.layout.item_spinner_view,
                    question = "What was the year of pregnancy outcome? ((a) for pregnant ladies, mention the date when conceived (b) in case of other options mention the date of delivery/miscarriage)",
                    spinnerItem = context.resources.getStringArray(R.array.outcomes).toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = R.layout.item_date_picker_view,
                    question = "What was the year of pregnancy outcome? ((a) for pregnant ladies, mention the date when conceived (b) in case of other options mention the date of delivery/miscarriage)",
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = R.layout.item_spinner_view,
                    question = "How many months did this pregnancy last?",
                    spinnerItem = context.resources.getStringArray(R.array.outcomes).toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
        }
        return list
    }

    override fun getHealthServiceQuestionList(): ArrayList<RoasterViewQuestion> {
        val list = ArrayList<RoasterViewQuestion>()
        list.apply {
            add(
                RoasterViewQuestion(
                    layoutId = R.layout.item_spinner_view,
                    question = "What was the outcome of your pregnancy?",
                    spinnerItem = context.resources.getStringArray(R.array.outcomes).toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = R.layout.item_spinner_view,
                    question = "What was the year of pregnancy outcome? ((a) for pregnant ladies, mention the date when conceived (b) in case of other options mention the date of delivery/miscarriage)",
                    spinnerItem = context.resources.getStringArray(R.array.outcomes).toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )

            add(
                RoasterViewQuestion(
                    layoutId = R.layout.item_spinner_view,
                    question = "How many months did this pregnancy last?",
                    spinnerItem = context.resources.getStringArray(R.array.outcomes).toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
        }
        return list
    }


}
