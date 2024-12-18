package org.intelehealth.app.ui.baseline_survey.data

import android.database.sqlite.SQLiteOpenHelper
import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.models.Patient
import org.intelehealth.app.models.dto.PatientAttributesDTO
import org.intelehealth.app.ui.baseline_survey.model.Baseline
import java.util.UUID

class BaselineRepository(
    private val patientsDAO: PatientsDAO,
    private val sqlHelper: SQLiteOpenHelper
) {
    fun updateBaselinePatientAttributes(baseline: Baseline, patientId: String) {

    }

    private fun createPatientAttributes(baseline: Baseline, patientId: String) {

    }




}