package org.intelehealth.app.ui.rosterquestionnaire.repository

import android.content.Context
import android.text.InputType
import org.intelehealth.app.R
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.utilities.RoasterQuestionView
import javax.inject.Inject

class RosterRepositoryImp @Inject constructor() : RosterRepository {
    private var context: Context = IntelehealthApplication.getAppContext()
    override fun getOutcomeQuestionList(): ArrayList<RoasterViewQuestion> {
        val list = ArrayList<RoasterViewQuestion>()
        list.apply {
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = "What was the outcome of your pregnancy?*",
                    spinnerItem = context.resources.getStringArray(R.array.outcome_pregnancy_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )

            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.DATE_PICKER,
                    question = "What was the year of pregnancy outcome? ((a) for pregnant ladies, mention the date when conceived (b) in case of other options mention the date of delivery/miscarriage)",
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = "How many months did this pregnancy last?",
                    spinnerItem = context.resources.getStringArray(R.array.outcome_how_many_pregnancy_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = "What is the Place of Delivery?",
                    spinnerItem = context.resources.getStringArray(R.array.place_delivery_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = "What is the type of Delivery?",
                    spinnerItem = context.resources.getStringArray(R.array.delivery_type_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = "Was the pregnancy planned?",
                    spinnerItem = context.resources.getStringArray(R.array.pregnancy_planned_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = "Where you identified as a High-Risk Pregnancy case?",
                    spinnerItem = context.resources.getStringArray(R.array.high_risk_pregnancy_en)
                        .toList(),
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
                    layoutId = RoasterQuestionView.SPINNER,
                    question = "Health Issue Reported",
                    spinnerItem = context.resources.getStringArray(R.array.health_issue_reported_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.EDIT_TEXT,
                    question = "No. Of Episodes in The Last Years",
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    inputType = InputType.TYPE_CLASS_NUMBER
                )
            )

            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = "Generally, which is the primary health provider you interacted with for health issues?",
                    spinnerItem = context.resources.getStringArray(R.array.primary_health_provider_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = "First location Of Visit / interaction",
                    spinnerItem = context.resources.getStringArray(R.array.place_delivery_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = "Referred To",
                    spinnerItem = context.resources.getStringArray(R.array.referred_to_en).toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = "Mode Of Transportation Used To Reach facility",
                    spinnerItem = context.resources.getStringArray(R.array.mode_transport_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.EDIT_TEXT,
                    question = "Average cost incurred on travel and stay per episode",
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    inputType = InputType.TYPE_CLASS_NUMBER,
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.EDIT_TEXT,
                    question = "Average cost incurred on consultation fee per episode",
                    inputType = InputType.TYPE_CLASS_NUMBER,
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.EDIT_TEXT,
                    question = "Average cost incurred on medicines per episode",
                    inputType = InputType.TYPE_CLASS_NUMBER,
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = "Score for experience of treatment",
                    spinnerItem = context.resources.getStringArray(R.array.score_experience_en).toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
        }
        return list
    }


}
