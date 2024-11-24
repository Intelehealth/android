package org.intelehealth.app.activities.householdSurvey.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import org.intelehealth.app.R
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.databinding.FragmentOneHouseholdSurveyBinding
import org.intelehealth.app.models.HouseholdSurveyModel
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.app.utilities.DateAndTimeUtils
import org.intelehealth.app.utilities.HouseholdSurveyStage
import org.intelehealth.app.utilities.exception.DAOException
import org.intelehealth.core.registry.PermissionRegistry
import java.util.Calendar

class FirstFragment : BaseHouseholdSurveyFragment(R.layout.fragment_one_household_survey) {
    private val TAG = "FirstFragment"
    private lateinit var binding: FragmentOneHouseholdSurveyBinding
    var selectedDate = Calendar.getInstance().timeInMillis
    var patientUuid: String? = null
    var mHouseStructure: String? = null
    var mResultVisit: String? = null
    private val permissionRegistry by lazy {
        PermissionRegistry(requireContext(), requireActivity().activityResultRegistry)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOneHouseholdSurveyBinding.bind(view)
        houseHoldViewModel.updatePatientStage(HouseholdSurveyStage.FIRST_SCREEN)

        initViews()
        setClickListener()
        radioButtonsClickListener();
    }

    private fun radioButtonsClickListener() {
        // House Structure
        binding.rgStructureType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbKucha -> {
                    mHouseStructure = "Kucha"
                }

                R.id.rbPucca -> {
                    mHouseStructure = "Pucca"
                }
            }
            Log.d(TAG, "radioButtonsClickListener: mHouseStructure : " + mHouseStructure)
        }

        //Result of Visit
        binding.rgResultOfVisit.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbAvailableAndAccepted -> {
                    mResultVisit = "available and accepted"
                }

                R.id.rbAvailableAndDeferred -> {
                    mResultVisit = "available and deferred"
                }

                R.id.rbNotAvailableForSurvey -> {
                    mResultVisit = "Not available on Survey"
                }

                R.id.rbNotAvailableForSecondVisit -> {
                    mResultVisit = "Not available on second visit"
                }

                R.id.rbNotAvailableForThirdVisit -> {
                    mResultVisit = "Not available on third visit"
                }

                R.id.rbRefusedForVisit -> {
                    mResultVisit = "Refused to Participate"
                }
            }
            Log.d(TAG, "radioButtonsClickListener: mResultVisit : " + mResultVisit)
        }
    }

    private fun initViews() {
        val intent = requireActivity().intent
        if (intent != null) {
            patientUuid = intent.getStringExtra("patientUuid")
        }
        getPatientUuidsForHouseholdValue(patientUuid!!)

    }

    private fun setClickListener() {
        binding.btnFirstFragNext.setOnClickListener {
            /* FirstFragmentDirections.actionOneToTwo().apply {
                 findNavController().navigate(this)
             }*/
            savePatient()
        }
    }

    private fun savePatient() {
        householdSurveyModel.apply {
            houseStructure = mHouseStructure
            resultOfVisit = mResultVisit
            namePrimaryRespondent = binding.textInputNameOfPrimaryRespondent.text?.toString()
            reportDateOfSurveyStarted = DateAndTimeUtils.currentDateTimeFormat()
            householdNumberOfSurvey = binding.textInputHouseholdNumber.text?.toString()


            houseHoldViewModel.updatedPatient(this)
            val patient = PatientDTO()
            patient.uuid = patientUuid
            Log.d("devKZchk", "savePatient: survey patientUuid  " + patientUuid)

            if (houseHoldViewModel.isEditMode) {
                saveAndNavigateToDetails(patient, householdSurveyModel)
                Log.d("devKZchk", "savePatient: editmode survey patientUuid  " + patientUuid)
            } else {
                saveAndNavigateToDetails(patient, householdSurveyModel)
            }
        }
    }

    private fun saveAndNavigateToDetails(
        patient: PatientDTO,
        householdSurveyModel: HouseholdSurveyModel
    ) {
        houseHoldViewModel.savePatient(patient, householdSurveyModel).observe(viewLifecycleOwner) {
            it
                ?: return@observe houseHoldViewModel.handleResponse(it) { result -> if (result) navigateToDetails() }
        }
    }

    private fun navigateToDetails() {
        FirstFragmentDirections.actionOneToTwo().apply {
            findNavController().navigate(this)
        }
    }

    private fun getPatientUuidsForHouseholdValue(patientUuid: String) {
        val patientsDAO = PatientsDAO()
        // Getting the household value and then getting all the Patient UUIDs listed to it so that we
        // can insert all of this data into each of them.
        var houseHoldValue = ""
        try {
            houseHoldValue = patientsDAO.getHouseHoldValue(patientUuid)
        } catch (e: DAOException) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }

        if (houseHoldValue.isNotEmpty()) {
            // Fetch all patient UUIDs from houseHoldValue
            try {
                val patientUUIDs = patientsDAO.getPatientUUIDs(houseHoldValue).toList()
                Log.e("patientUUIDss", patientUUIDs.toString())
                patientUUIDs.forEach { uuid ->
                    setData(uuid)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setData(patientUuid: String) {
        val patientsDAO = PatientsDAO()

        Log.d(TAG, "setData: kkk attr survey")
        val db = IntelehealthApplication.inteleHealthDatabaseHelper.writableDatabase

        val patientSelection1 = "patientuuid = ?"
        val patientArgs1 = arrayOf(patientUuid)
        val patientColumns1 = arrayOf("value", "person_attribute_type_uuid")
        val idCursor1 = db.query(
            "tbl_patient_attribute",
            patientColumns1,
            patientSelection1,
            patientArgs1,
            null,
            null,
            null
        )

        if (idCursor1.moveToFirst()) {
            do {
                val name = try {
                    patientsDAO.getAttributesName(
                        idCursor1.getString(idCursor1.getColumnIndexOrThrow("person_attribute_type_uuid"))
                    )
                } catch (e: DAOException) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                    ""
                }

                when {
                    name.equals("NamePrimaryRespondent", ignoreCase = true) -> {
                        val value1 = idCursor1.getString(idCursor1.getColumnIndexOrThrow("value"))
                        if (!value1.isNullOrEmpty() && !value1.equals("-", ignoreCase = true)) {
                            //namePerson.text = value1
                        }
                    }

                    name.equals("HouseholdNumber", ignoreCase = true) -> {
                        val value1 = idCursor1.getString(idCursor1.getColumnIndexOrThrow("value"))
                        if (!value1.isNullOrEmpty() && !value1.equals("-", ignoreCase = true)) {
                            // householdNumber.text = value1
                        }
                    }

                    /* name.equals("HouseStructure", ignoreCase = true) -> {
                         val value1 = idCursor1.getString(idCursor1.getColumnIndexOrThrow("value"))
                        // mhouseStructure = value1
                         when {
                             value1.equals("Pucca", ignoreCase = true) -> puccaRadioButton.isChecked = true
                             value1.equals("Kucha", ignoreCase = true) -> kuchaRadioButton.isChecked = true
                         }
                     }*/

                    /*
                                        name.equals("ResultOfVisit", ignoreCase = true) -> {
                                            val result = idCursor1.getString(idCursor1.getColumnIndexOrThrow("value"))
                                            mresultVisit = result
                                            when {
                                                result.equals("available and accepted", ignoreCase = true) -> availableAccepted.isChecked = true
                                                result.equals("available and deferred", ignoreCase = true) -> availableDeferred.isChecked = true
                                                result.equals("Not available on Survey", ignoreCase = true) -> notavailableSurvey.isChecked = true
                                                result.equals("Not available on second visit", ignoreCase = true) -> notavailableSecondVisit.isChecked = true
                                                result.equals("Not available on third visit", ignoreCase = true) -> notavailableThirdVisit.isChecked = true
                                                result.equals("Refused to Participate", ignoreCase = true) -> refusedParticipate.isChecked = true
                                            }
                                        }
                    */
                }
            } while (idCursor1.moveToNext())
        }
        idCursor1.close()
    }

    override fun onPatientDataLoaded(householdSurveyModel: HouseholdSurveyModel) {
        super.onPatientDataLoaded(householdSurveyModel)
        Timber.d { "onPatientDataLoaded" }
        Timber.d { Gson().toJson(householdSurveyModel) }
        //fetchPersonalInfoConfig()
        Log.d(
            TAG,
            "onPatientDataLoaded: householdSurveyModel : " + Gson().toJson(householdSurveyModel)
        )
        binding.patientSurveyAttributes = householdSurveyModel
        binding.isEditMode = houseHoldViewModel.isEditMode
    }


}