package org.intelehealth.app.ui.rosterquestionnaire.repository

import android.content.Context
import android.text.InputType
import org.intelehealth.app.R
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.models.dto.PatientAttributesDTO
import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.utilities.RoasterQuestionView
import javax.inject.Inject

class RosterRepositoryImp @Inject constructor() : RosterRepository {

    private var context: Context = IntelehealthApplication.getAppContext()

    private val patientDao = PatientsDAO()

    override fun getGeneralQuestionList(): ArrayList<RoasterViewQuestion> {
        val list = ArrayList<RoasterViewQuestion>()
        list.apply {
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.txt_what_is_your_relationship_with_head_of_household),
                    spinnerItem = context.resources.getStringArray(R.array.relationshipHoH)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    attribute = "RelationshipStatusHOH"
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.what_is_your_marital_status),
                    spinnerItem = context.resources.getStringArray(R.array.maritual)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    attribute = "MaritualStatus"
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.what_is_your_education_status),
                    spinnerItem = context.resources.getStringArray(R.array.education_nas)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    attribute = "Education Level"
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.nas_occupation),
                    spinnerItem = context.resources.getStringArray(R.array.occupation_identification)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    attribute = "occupation"
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.txt_phone_ownership),
                    spinnerItem = context.resources.getStringArray(R.array.phoneownership)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    attribute = "PhoneOwnership"
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.txt_when_was_the_last_time_you_got_your_bp_checked),
                    spinnerItem = context.resources.getStringArray(R.array.bp)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    attribute = "BPchecked"
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.txt_when_was_the_last_time_you_got_your_sugar_level_checked),
                    spinnerItem = context.resources.getStringArray(R.array.sugar)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    attribute = "Sugarchecked"
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.txt_when_was_the_last_time_you_got_your_hb_level_tested),
                    spinnerItem = context.resources.getStringArray(R.array.hb)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    attribute = "HBtest"
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.txt_bmi_height_and_weight_checked),
                    spinnerItem = context.resources.getStringArray(R.array.bmi)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    attribute = "BMI"
                )
            )
        }
        return list
    }

    override fun getOutcomeQuestionList(): ArrayList<RoasterViewQuestion> {
        val list = ArrayList<RoasterViewQuestion>()
        list.apply {
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.what_was_the_outcome_of_your_pregnancy),
                    spinnerItem = context.resources.getStringArray(R.array.outcome_pregnancy_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )

            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.DATE_PICKER,
                    question = context.getString(R.string.what_was_the_year_of_pregnancy_outcome_a_for_pregnant_ladies_mention_the_date_when_conceived_b_in_case_of_other_options_mention_the_date_of_delivery_miscarriage),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.how_many_months_did_this_pregnancy_last),
                    spinnerItem = context.resources.getStringArray(R.array.outcome_how_many_pregnancy_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.what_is_the_place_of_delivery),
                    spinnerItem = context.resources.getStringArray(R.array.place_delivery_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.what_is_the_type_of_delivery),
                    spinnerItem = context.resources.getStringArray(R.array.delivery_type_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.was_the_pregnancy_planned),
                    spinnerItem = context.resources.getStringArray(R.array.pregnancy_planned_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.where_you_identified_as_a_high_risk_pregnancy_case),
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
                    question = context.getString(R.string.health_issue_reported),
                    spinnerItem = context.resources.getStringArray(R.array.health_issue_reported_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.EDIT_TEXT,
                    question = context.getString(R.string.no_of_episodes_in_the_last_years),
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    inputType = InputType.TYPE_CLASS_NUMBER
                )
            )

            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.generally_which_is_the_primary_health_provider_you_interacted_with_for_health_issues),
                    spinnerItem = context.resources.getStringArray(R.array.primary_health_provider_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.first_location_of_visit_interaction),
                    spinnerItem = context.resources.getStringArray(R.array.place_delivery_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.referred_to),
                    spinnerItem = context.resources.getStringArray(R.array.referred_to_en).toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.mode_of_transportation_used_to_reach_facility),
                    spinnerItem = context.resources.getStringArray(R.array.mode_transport_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.EDIT_TEXT,
                    question = context.getString(R.string.average_cost_incurred_on_travel_and_stay_per_episode),
                    errorMessage = context.getString(R.string.this_field_is_mandatory),
                    inputType = InputType.TYPE_CLASS_NUMBER,
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.EDIT_TEXT,
                    question = context.getString(R.string.average_cost_incurred_on_consultation_fee_per_episode),
                    inputType = InputType.TYPE_CLASS_NUMBER,
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.EDIT_TEXT,
                    question = context.getString(R.string.average_cost_incurred_on_medicines_per_episode),
                    inputType = InputType.TYPE_CLASS_NUMBER,
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
            add(
                RoasterViewQuestion(
                    layoutId = RoasterQuestionView.SPINNER,
                    question = context.getString(R.string.score_for_experience_of_treatment),
                    spinnerItem = context.resources.getStringArray(R.array.score_experience_en)
                        .toList(),
                    errorMessage = context.getString(R.string.this_field_is_mandatory)
                )
            )
        }
        return list
    }

    override fun insertRoaster(attributeList: ArrayList<PatientAttributesDTO>) {
        patientDao.insertPatientAttributes(attributeList)
    }


}
