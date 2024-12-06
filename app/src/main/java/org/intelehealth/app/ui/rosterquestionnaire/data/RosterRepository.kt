package org.intelehealth.app.ui.rosterquestionnaire.data

import android.database.sqlite.SQLiteOpenHelper
import org.intelehealth.app.database.dao.PatientsDAO

class RosterRepository (
    private val patientsDao: PatientsDAO,
    private val sqlHelper: SQLiteOpenHelper,
) {}