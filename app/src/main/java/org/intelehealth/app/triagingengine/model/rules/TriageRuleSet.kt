package org.intelehealth.app.triagingengine.model.rules

import com.google.gson.annotations.SerializedName

/**
 * Created by Lincon Pradhan on  03-02-2025.
 **/
data class TriageRuleSet(
    @SerializedName("age_risk_score") val ageRiskScore: List<AgeRiskScore>,
    @SerializedName("fields") val fields: List<Field>,
    @SerializedName("formulas") val formulas: List<Formula>,
    @SerializedName("referral_rules") val referralRules: List<ReferralRule>,
    @SerializedName("popup_result") val popupResult: List<PopupResult>,
    @SerializedName("symptom_duration_risk_score") val symptomDurationRiskScore: List<SymptomDurationRiskScore>,
    @SerializedName("symptom_duration") val symptomDuration: List<SymptomDuration>,
    @SerializedName("symptom_duration_finding_questions") val symptomDurationFindingQuestions: List<String>,
    @SerializedName("symptom_risk_score") val symptomRiskScore: List<SymptomRiskScore>
)