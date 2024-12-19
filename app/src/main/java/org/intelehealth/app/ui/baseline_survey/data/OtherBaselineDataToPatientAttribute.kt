package org.intelehealth.app.ui.baseline_survey.data

import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.models.dto.PatientAttributesDTO
import org.intelehealth.app.ui.baseline_survey.model.Baseline
import org.intelehealth.app.utilities.extensions.storeCultivableLandValue
import org.intelehealth.app.utilities.extensions.storeHyphenOrRelation
import org.intelehealth.app.utilities.extensions.storeHyphenIfEmpty
import java.util.UUID

fun bindOtherBaselinePatientAttributes(
    baseline: Baseline,
    patientId: String,
    patientsDAO: PatientsDAO
): List<PatientAttributesDTO> = ArrayList<PatientAttributesDTO>().apply {
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.HOH_RELATIONSHIP.value,
            baseline.headOfHousehold.storeHyphenOrRelation(baseline.relationWithHousehold),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.RATION_CARD.value,
            baseline.rationCardCheck.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.ECONOMIC_STATUS.value,
            baseline.economicStatus.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.RELIGION.value,
            baseline.religion.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.TOTAL_FAMILY_MEMBERS.value,
            baseline.totalHouseholdMembers.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.TOTAl_FAMILY_MEMBERS_STAYING.value,
            baseline.usualHouseholdMembers.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.NUMBER_OF_SMARTPHONES.value,
            baseline.numberOfSmartphones.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.NUMBER_OF_FEATURE_PHONES.value,
            baseline.numberOfFeaturePhones.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.NUMBER_OF_EARNING_MEMBERS.value,
            baseline.numberOfEarningMembers.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.ELECTRICITY_STATUS.value,
            baseline.electricityCheck.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.LOAD_SHEDDING_HOURS_PER_DAY.value,
            baseline.loadSheddingHours.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.LOAD_SHEDDING_DAYS_PER_WEEK.value,
            baseline.loadSheddingDays.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.RUNNING_WATER_AVAILABILITY.value,
            baseline.waterCheck.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.WATER_SUPPLY_AVAILABILITY_HOURS_PER_DAY.value,
            baseline.waterAvailabilityHours.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.WATER_SUPPLY_AVAILABILITY_DAYS_PER_WEEK.value,
            baseline.waterAvailabilityDays.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.DRINKING_WATER_SOURCE.value,
            baseline.sourceOfWater.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.SAFE_DRINKING_WATER.value,
            baseline.safeguardWater.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.TIME_DRINKING_WATER_SOURCE.value,
            baseline.distanceFromWater.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.TOILET_FACILITY.value,
            baseline.toiletFacility.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.TOILET_FACILITY.value,
            baseline.toiletFacility.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.HOUSE_STRUCTURE.value,
            baseline.houseStructure.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.FAMILY_CULTIVABLE_LAND.value,
            baseline.cultivableLand.storeCultivableLandValue(baseline.cultivableLandValue),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.AVERAGE_ANNUAL_HOUSEHOLD_INCOME.value,
            baseline.averageIncome.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.COOKING_FUEL.value,
            baseline.fuelType.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.HOUSEHOLD_LIGHTING.value,
            baseline.sourceOfLight.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.SOAP_HAND_WASHING_OCCASION.value,
            baseline.handWashPractices.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
    add(
        createPatientAttribute(
            patientId, PatientAttributesDTO.Column.TAKE_OUR_SERVICE.value,
            baseline.ekalServiceCheck.storeHyphenIfEmpty(),
            patientsDAO
        )
    )
}

private fun createPatientAttribute(
    patientId: String,
    attrName: String,
    value: String?,
    patientsDAO: PatientsDAO
): PatientAttributesDTO {
    return PatientAttributesDTO().apply {
        uuid = UUID.randomUUID().toString()
        patientuuid = patientId
        personAttributeTypeUuid = patientsDAO.getUuidForAttribute(attrName)
        this.value = value
    }
}