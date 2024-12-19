package org.intelehealth.app.ui.baseline_survey.data

import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.models.dto.PatientAttributesDTO.Column
import org.intelehealth.app.models.pushRequestApiCall.Attribute
import org.intelehealth.app.ui.baseline_survey.model.Baseline
import org.intelehealth.app.utilities.extensions.returnEmptyIfHyphen

class PatientAttributeToBaseline(private val patientsDAO: PatientsDAO) {

    fun getBaselineData(patientAttributeList: List<Attribute>): Baseline {
        return Baseline().apply {
            getGeneralData(list = patientAttributeList, baseline = this)
        }
    }

    private fun getGeneralData(
        baseline: Baseline,
        list: List<Attribute>,
        patientsDAO: PatientsDAO = this.patientsDAO
    ) {
        list.forEach {
            val personTypeAttributeName = patientsDAO.getAttributesName(it.attributeType)
            when (personTypeAttributeName) {
                Column.CASTE.value -> baseline.caste = it.value.returnEmptyIfHyphen()
                Column.EDUCATION.value -> baseline.education = it.value.returnEmptyIfHyphen()
                Column.OCCUPATION.value -> baseline.occupation = it.value.returnEmptyIfHyphen()
                Column.BANK_ACCOUNT.value -> baseline.bankAccount = it.value.returnEmptyIfHyphen()
                Column.USE_WHATSAPP.value -> baseline.familyWhatsApp =
                    it.value.returnEmptyIfHyphen()

                Column.MARTIAL_STATUS.value -> baseline.martialStatus =
                    it.value.returnEmptyIfHyphen()

                Column.MGNREGA_CARD_STATUS.value -> baseline.mgnregaCard =
                    it.value.returnEmptyIfHyphen()

                Column.MOBILE_PHONE_TYPE.value -> baseline.phoneOwnership =
                    it.value.returnEmptyIfHyphen()

                Column.AYUSHMAN_CARD_STATUS.value -> baseline.ayushmanCard =
                    it.value.returnEmptyIfHyphen()
            }
        }
    }
}