package org.intelehealth.app.utilities

object LocationValidationUtils {

    fun areLocationFieldsValid(sessionManager: SessionManager): Boolean {
        if (!isStateNameValid(sessionManager.stateName)) {
            return false
        }

        if (!isDistrictNameValid(sessionManager.districtName)) {
            return false
        }

        if (!isSanchNameValid(sessionManager.sanchName)) {
            return false
        }

        if (!isCurrentLocationNameValid(sessionManager.currentLocationName)) {
            return false
        }

        if (!isSecondaryLocationNameValid(sessionManager.secondaryLocationName)) {
            return false
        }

        if (!isSubCentreValid(sessionManager.subCentreDistance)) {
            return false
        }

        if (!isPrimaryHealthCentreValid(sessionManager.primaryHealthCentreDistance)) {
            return false
        }

        if (!isCommunityHealthCentreValid(sessionManager.communityHealthCentreDistance)) {
            return false
        }

        if (!isDistrictHospitalDistanceValid(sessionManager.districtHospitalDistance)) {
            return false
        }

        if (!isMedicalStoreDistanceValid(sessionManager.medicalStoreDistance)) {
            return false
        }

        if (!isPathologicalLabDistanceValid(sessionManager.pathologicalLabDistance)) {
            return false
        }

        if (!isPrivateClinicWithMbbsDoctorDistanceValid(sessionManager.privateClinicWithMbbsDoctorDistance)) {
            return false
        }

        if (!isPrivateClinicWithAlternateDoctorValid(sessionManager.privateClinicWithAlternateDoctorDistance)) {
            return false
        }

        if (!isJalJeevanYojanaSchemeValid(sessionManager.jalJeevanYojanaScheme)) {
            return false
        }

        return true
    }

    fun isStateNameValid(stateName: String) = isValueNotNullOrEmpty(stateName)

    fun isDistrictNameValid(districtName: String) = isValueNotNullOrEmpty(districtName)

    fun isSanchNameValid(sanchName: String) = isValueNotNullOrEmpty(sanchName)

    fun isCurrentLocationNameValid(currentLocation: String) =
        isValueNotNullOrEmpty(currentLocation)

    fun isSecondaryLocationNameValid(secondaryLocation: String) =
        isValueNotNullOrEmpty(secondaryLocation)

    fun isSubCentreValid(subCentre: String) = isValueNotNullOrEmpty(subCentre)

    fun isPrimaryHealthCentreValid(primaryHealthCentre: String) =
        isValueNotNullOrEmpty(primaryHealthCentre)

    fun isCommunityHealthCentreValid(communityHealthCentre: String) =
        isValueNotNullOrEmpty(communityHealthCentre)

    fun isDistrictHospitalDistanceValid(districtHospitalDistance: String) =
        isValueNotNullOrEmpty(districtHospitalDistance)

    fun isMedicalStoreDistanceValid(medicalStoreDistance: String) =
        isValueNotNullOrEmpty(medicalStoreDistance)

    fun isPathologicalLabDistanceValid(pathologicalLabDistance: String) =
        isValueNotNullOrEmpty(pathologicalLabDistance)

    fun isPrivateClinicWithMbbsDoctorDistanceValid(privateClinicWithMbbsDoctor: String) =
        isValueNotNullOrEmpty(privateClinicWithMbbsDoctor)

    fun isPrivateClinicWithAlternateDoctorValid(privateClinicWithAlternateDoctor: String) =
        isValueNotNullOrEmpty(privateClinicWithAlternateDoctor)

    fun isJalJeevanYojanaSchemeValid(jalJeevanYojana: String) =
        isValueNotNullOrEmpty(jalJeevanYojana)

    private fun isValueNotNullOrEmpty(value: String?): Boolean {
        return !value.isNullOrEmpty()
    }
}