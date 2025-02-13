package org.intelehealth.common.triagingrule.model.rules

import com.google.gson.annotations.SerializedName
import org.intelehealth.app.triagingengine.model.rules.TriageRuleSet

/**
 * Created by Lincon Pradhan on  03-02-2025.
 **/
data class TriagingReferralRule(
    @SerializedName("ruleset") var ruleset: TriageRuleSet
)