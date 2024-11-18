package org.intelehealth.app.activities.visit.staticEnabledFields

import org.intelehealth.config.room.entity.PatientVital
import org.intelehealth.config.utility.PatientVitalConfigKeys

object VitalsEnabledFieldsHelper {

    fun getStaticVitalsEnabledFields(): List<PatientVital> {
        val fields: MutableList<PatientVital> = mutableListOf()

        // Height
        var currentField = PatientVital(
            name = "",
            vitalKey = PatientVitalConfigKeys.HEIGHT,
            uuid = "",
            isMandatory = true
        )
        fields.add(currentField)

        // Weight
        currentField = PatientVital(
            name = "",
            vitalKey = PatientVitalConfigKeys.WEIGHT,
            uuid = "",
            isMandatory = true
        )
        fields.add(currentField)

        // BMI
        currentField = PatientVital(
            name = "",
            vitalKey = PatientVitalConfigKeys.BMI,
            uuid = "",
            isMandatory = true
        )
        fields.add(currentField)

        // SBP
        currentField = PatientVital(
            name = "",
            vitalKey = PatientVitalConfigKeys.SBP,
            uuid = "",
            isMandatory = true
        )
        fields.add(currentField)

        // DBP
        currentField = PatientVital(
            name = "",
            vitalKey = PatientVitalConfigKeys.DBP,
            uuid = "",
            isMandatory = true
        )
        fields.add(currentField)

        // Pulse
        currentField = PatientVital(
            name = "",
            vitalKey = PatientVitalConfigKeys.PULSE,
            uuid = "",
            isMandatory = true
        )
        fields.add(currentField)

        // Temperature
        currentField = PatientVital(
            name = "",
            vitalKey = PatientVitalConfigKeys.TEMPERATURE,
            uuid = "",
            isMandatory = true
        )
        fields.add(currentField)

        // SPO2
        currentField = PatientVital(
            name = "",
            vitalKey = PatientVitalConfigKeys.SPO2,
            uuid = "",
            isMandatory = true
        )
        fields.add(currentField)

        // Respiratory Rate
        currentField = PatientVital(
            name = "",
            vitalKey = PatientVitalConfigKeys.RESPIRATORY_RATE,
            uuid = "",
            isMandatory = true
        )
        fields.add(currentField)

        // Blood Type
        currentField = PatientVital(
            name = "",
            vitalKey = PatientVitalConfigKeys.BLOOD_TYPE,
            uuid = "",
            isMandatory = true
        )
        fields.add(currentField)

        return fields
    }
}