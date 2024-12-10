package org.intelehealth.app.utilities

/**
 * Created by Tanvir Hasan on 29-04-2024 : 16-31.
 * Email: mhasan@intelehealth.org
 *
 * Patient register config keys
 */
class PatientRegConfigKeys {
    companion object{
        //PERSONAL
        const val PROFILE_PHOTO = "p_profile_photo"
        const val FIRST_NAME = "p_first_name"
        const val MIDDLE_NAME = "p_middle_name"
        const val LAST_NAME = "p_last_name"
        const val GENDER = "p_gender"
        const val DOB = "p_date_of_birth"
        const val AGE = "p_age"
        const val PHONE_NUM = "p_phone_number"
        const val GUARDIAN_NAME = "p_guardian_name"
        const val GUARDIAN_TYPE = "p_guardian_type"
        const val EM_CONTACT_NAME = "p_emergency_contact_name"
        const val EM_CONTACT_NUMBER = "p_emergency_contact_number"
        const val EM_CONTACT_TYPE = "p_contact_type"

        //ADDRESS
        const val POSTAL_CODE = "a_postal_address"
        const val COUNTRY = "a_country"
        const val STATE = "a_state"
        const val DISTRICT = "a_district"
        const val VILLAGE_TOWN_CITY = "a_village_town_city"
        const val ADDRESS_1 = "a_corresponding_address_1"
        const val ADDRESS_2 = "a_corresponding_address_2"

        //OTHERS
        const val NATIONAL_ID = "o_national_id"
        const val OCCUPATION = "o_occupation"
        const val SOCIAL_CATEGORY = "o_social_category"
        const val EDUCATION = "o_education"
        const val ECONOMIC_CATEGORY = "o_economic_category"

        // GENERAL
        const val AYUSHMAN_CARD = "general_ayushman_card"
        const val MGNREGA_CARD = "general_mgnrega_card"
        const val BANK_ACCOUNT = "general_bank_account"
        const val PHONE_OWNERSHIP = "general_phone_ownership"
        const val FAMILY_WHATSAPP = "general_family_whatsapp"
        const val MARITAL_STATUS = "general_marital_status"

        // MEDICAL
        const val HB_CHECK = "hb_check"
        const val BP_CHECK = "bp_check"
        const val SUGAR_CHECK = "sugar_check"
        const val BP_VALUE = "bp_value"
        const val DIABETES_VALUE = "diabetes_value"
        const val ARTHRITIS_VALUE = "arthritis_value"
        const val ANEMIA_VALUE = "anemia_value"
        const val SUGAR_VALUE = "sugar_value"
        const val SUGAR_REASON = "sugar_reason"
        const val SMOKING_HISTORY = "smoking_history"
        const val SMOKING_RATE = "smoking_rate"
        const val SMOKING_DURATION = "smoking_duration"
        const val SMOKING_FREQUENCY = "smoking_frequency"
        const val CHEW_TOBACCO = "chew_tobacco"
        const val ALCOHOL_HISTORY = "alcohol_history"
        const val ALCOHOL_RATE = "alcohol_rate"
        const val ALCOHOL_DURATION= "alcohol_duration"
        const val ALCOHOL_FREQUENCY = "alcohol_frequency"
    }
}