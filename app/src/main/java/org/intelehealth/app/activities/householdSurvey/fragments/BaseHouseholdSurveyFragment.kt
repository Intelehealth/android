package org.intelehealth.app.activities.householdSurvey.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.intelehealth.app.activities.householdSurvey.factory.HouseHoldViewModelFactory
import org.intelehealth.app.models.HouseholdSurveyModel
import org.intelehealth.app.models.dto.PatientAttributesDTO
import org.intelehealth.app.models.dto.PatientDTO

abstract class BaseHouseholdSurveyFragment (@LayoutRes layoutResId: Int) : Fragment(layoutResId) {
    protected var householdSurveyModel: HouseholdSurveyModel = HouseholdSurveyModel()
    protected val houseHoldViewModel by lazy {
        return@lazy HouseHoldViewModelFactory.create(requireActivity(), requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        houseHoldViewModel.patientSurveyAttributesData.observe(viewLifecycleOwner) {
            onPatientDataLoaded(it)
        }
    }

    open fun onPatientDataLoaded(householdSurveyModel: HouseholdSurveyModel) {
        this.householdSurveyModel = householdSurveyModel
    }
}