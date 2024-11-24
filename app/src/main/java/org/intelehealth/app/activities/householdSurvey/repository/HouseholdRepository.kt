package org.intelehealth.app.activities.householdSurvey.repository

import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.github.ajalt.timberkt.Timber
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.database.dao.ImagesDAO
import org.intelehealth.app.database.dao.ImagesPushDAO
import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.database.dao.SyncDAO
import org.intelehealth.app.models.HouseholdSurveyModel
import org.intelehealth.app.models.dto.PatientAttributesDTO
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.app.ui.patient.data.PatientQueryBuilder
import org.intelehealth.app.utilities.NetworkConnection
import org.intelehealth.config.presenter.fields.data.RegFieldRepository
import org.intelehealth.config.room.dao.PatientRegFieldDao
import java.util.UUID

class HouseholdRepository(
    private val patientsDao: PatientsDAO,
    private val sqlHelper: SQLiteOpenHelper,
) {
    fun addHouseholdPatientAttributes(
        patient: PatientDTO,
        householdSurveyModel: HouseholdSurveyModel
    ): Boolean {
        Log.d("devKZchk", "addHouseholdPatientAttributes: patient uuid : "+patient.uuid)
        bindPatientAttributes(patient, householdSurveyModel).let {
            val flag = patientsDao.updatePatientSurveyInDb(it.uuid, it.patientAttributesDTOList)
            syncOnServer()
            return flag
        }
    }

    fun updateHouseholdPatientAttributes(
        patient: PatientDTO,
        householdSurveyModel: HouseholdSurveyModel
    ): Boolean {
        return bindPatientAttributes(patient, householdSurveyModel).let {
            val flag = patientsDao.updatePatientSurveyInDb(it.uuid, it.patientAttributesDTOList)
            syncOnServer()
            return flag
        }
    }

    private fun bindPatientAttributes(
        patient: PatientDTO,
        householdSurveyModel: HouseholdSurveyModel
    ) = patient.apply {
        patientAttributesDTOList =
            createPatientAttributes(patient, householdSurveyModel, patient.uuid)
        syncd = false
    }

    fun fetchPatient(uuid: String): HouseholdSurveyModel {
        Timber.d { "uuid => $uuid" }
        PatientQueryBuilder().buildPatientSurveyAttributesDetailsQuery(uuid).apply {
            Timber.d { "Query => $this" }
            val cursor = sqlHelper.readableDatabase.rawQuery(this, null)
            return patientsDao.retrievePatientHouseholdSurveyAttributes(cursor)
        }
    }


    private fun createPatientAttributes(
        patient: PatientDTO,
        householdSurveyModel: HouseholdSurveyModel,
        patientUuid: String
    ) = arrayListOf<PatientAttributesDTO>()
        .apply {
            add(
                createPatientAttribute(
                    patientUuid,
                    PatientAttributesDTO.Column.REPORT_DATE_OF_SURVEY_STARTED.value,
                    householdSurveyModel.reportDateOfSurveyStarted
                )
            )
            add(
                createPatientAttribute(
                    patientUuid,
                    PatientAttributesDTO.Column.HOUSE_STRUCTURE.value,
                    householdSurveyModel.houseStructure
                )
            )
            add(
                createPatientAttribute(
                    patientUuid,
                    PatientAttributesDTO.Column.RESULT_OF_VISIT.value,
                    householdSurveyModel.resultOfVisit
                )
            )
            add(
                createPatientAttribute(
                    patientUuid,
                    PatientAttributesDTO.Column.NAME_OF_PRIMARY_RESPONDENT.value,
                    householdSurveyModel.namePrimaryRespondent
                )
            )
            add(
                createPatientAttribute(
                    patientUuid,
                    PatientAttributesDTO.Column.HOUSEHOLD_NUMBER_OF_SURVEY.value,
                    householdSurveyModel.householdNumberOfSurvey
                )
            )
        }

    private fun createPatientAttribute(
        patientId: String,
        attrName: String,
        value: String?
    ) = PatientAttributesDTO().apply {
        uuid = UUID.randomUUID().toString()
        patientuuid = patientId
        personAttributeTypeUuid = patientsDao.getUuidForAttribute(attrName)
        this.value = value
    }

    private fun updatePatientAttribute(
        patientId: String,
        attrName: String,
        value: String?
    ) = PatientAttributesDTO().apply {
        uuid = UUID.randomUUID().toString()
        patientuuid = patientId
        personAttributeTypeUuid = patientsDao.getUuidForAttribute(attrName)
        this.value = value
    }

    fun syncOnServer() {
        if (NetworkConnection.isOnline(IntelehealthApplication.getAppContext())) {
            val syncDAO = SyncDAO()
            val imagesPushDAO = ImagesPushDAO()
            syncDAO.pushDataApi()
            imagesPushDAO.patientProfileImagesPush()
        }
    }
}