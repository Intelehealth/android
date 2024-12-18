package org.intelehealth.app.ui.baseline_survey.data

import android.database.sqlite.SQLiteOpenHelper
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.database.dao.ImagesPushDAO
import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.database.dao.SyncDAO
import org.intelehealth.app.ui.baseline_survey.model.Baseline
import org.intelehealth.app.utilities.NetworkConnection

class BaselineRepository(
    private val patientsDAO: PatientsDAO,
    private val sqlHelper: SQLiteOpenHelper
) {
    fun createPatientAttributes(baseline: Baseline, patientId: String): Boolean {
        bindGeneralBaselinePatientAttributes(baseline, patientId, patientsDAO).let {
            val flag = patientsDAO.patientAttributes(it)
            syncOnServer()
            return flag
        }
    }

    fun getPatientAttributes(patientId: String): Baseline {
        return patientsDAO.getPatientAttributesForBaseline(patientId).let {
            PatientAttributeToBaseline(patientsDAO).getBaselineData(it)
        }
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