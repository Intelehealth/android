package org.intelehealth.app.activities.visit.staticEnabledFields

import org.intelehealth.config.room.entity.Specialization

object SpecializationsEnabledFieldsHelper {
    fun getSpecializations() = mutableListOf(
        Specialization(
            sKey = "",
            name = "General Physician"
        )
    )
}