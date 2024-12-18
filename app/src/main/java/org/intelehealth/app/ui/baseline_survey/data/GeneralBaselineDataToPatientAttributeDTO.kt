package org.intelehealth.app.ui.baseline_survey.data

import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.models.dto.PatientAttributesDTO
import org.intelehealth.app.ui.baseline_survey.model.Baseline
import java.util.UUID

fun bindGeneralBaselinePatientAttributes(
    baseline: Baseline,
    patientId: String,
    patientsDAO: PatientsDAO
): List<PatientAttributesDTO> {
    return ArrayList<PatientAttributesDTO>().apply {
        add(
            createPatientAttribute(
                patientId,
                PatientAttributesDTO.Column.OCCUPATION.value,
                baseline.occupation,
                patientsDAO
            )
        )

        add(
            createPatientAttribute(
                patientId,
                PatientAttributesDTO.Column.CASTE.value,
                baseline.caste,
                patientsDAO
            )
        )

        add(
            createPatientAttribute(
                patientId,
                PatientAttributesDTO.Column.EDUCATION.value,
                baseline.education,
                patientsDAO
            )
        )

        add(
            createPatientAttribute(
                patientId,
                PatientAttributesDTO.Column.AYUSHMAN_CARD_STATUS.value,
                baseline.ayushmanCard,
                patientsDAO
            )
        )

        add(
            createPatientAttribute(
                patientId,
                PatientAttributesDTO.Column.MGNREGA_CARD_STATUS.value,
                baseline.mgnregaCard,
                patientsDAO
            )
        )

        add(
            createPatientAttribute(
                patientId,
                PatientAttributesDTO.Column.BANK_ACCOUNT.value,
                baseline.bankAccount,
                patientsDAO
            )
        )

        add(
            createPatientAttribute(
                patientId,
                PatientAttributesDTO.Column.MOBILE_PHONE_TYPE.value,
                baseline.phoneOwnership,
                patientsDAO
            )
        )

        add(
            createPatientAttribute(
                patientId,
                PatientAttributesDTO.Column.USE_WHATSAPP.value,
                baseline.familyWhatsApp,
                patientsDAO
            )
        )

        add(
            createPatientAttribute(
                patientId, PatientAttributesDTO.Column.MARTIAL_STATUS.value,
                baseline.martialStatus,
                patientsDAO
            )
        )
    }
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