package org.intelehealth.app.utilities

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.annotation.ArrayRes
import com.google.gson.Gson
import org.intelehealth.app.activities.identificationActivity.model.Block
import org.intelehealth.app.activities.identificationActivity.model.DistData
import org.intelehealth.app.activities.identificationActivity.model.GramPanchayat
import org.intelehealth.app.activities.identificationActivity.model.StateData
import org.intelehealth.app.activities.identificationActivity.model.StateDistMaster
import org.intelehealth.app.activities.identificationActivity.model.Village
import org.intelehealth.app.activities.visitSummaryActivity.model.ReferralFacility
import org.intelehealth.app.activities.visitSummaryActivity.model.ReferralFacilityData
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.enums.ReferralFacilityDataFormatType
import java.util.Locale

/**
 * Created by Vaghela Mithun R. on 26-06-2024 - 20:19.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
object LanguageUtils {
    private const val STATE_DISTRICT_JSON = "state_district_tehsil.json"

    //    private const val FACILITY_ALL_DATA_JSON = "facility-all-data.json"
    private const val FACILITY_ALL_DATA_JSON = "facility-all-data-for-qa.json"


    @JvmStatic
    fun getLocalLang(): String {
        val context = IntelehealthApplication.getAppContext()
        return SessionManager(context).appLanguage
    }

    @JvmStatic
    fun getState(state: String): StateData? {
        val context = IntelehealthApplication.getAppContext()
        val jsonObject = FileUtils.encodeJSON(context, STATE_DISTRICT_JSON)
        val stateDistMaster = Gson().fromJson(
            jsonObject.toString(),
            StateDistMaster::class.java
        )

        return stateDistMaster.stateDataList.find { it.state == state }
    }

    @JvmStatic
    fun getDistrict(state: StateData?, district: String): DistData? {
        return state?.distDataList?.find { it.name == district }
    }

    @JvmStatic
    fun getBlock(district: DistData?, block: String?): Block? {
        return block?.let { return@let district?.blocks?.find { it.name == block } }
    }

    @JvmStatic
    fun getGramPanchayat(block: Block?, gramPanchayat: String?): GramPanchayat? {
        return gramPanchayat?.let { return@let block?.gramPanchayats?.find { it.name == gramPanchayat } }
    }

    @JvmStatic
    fun getVillage(gramPanchayat: GramPanchayat?, village: String?): Village? {
        return village?.let { return@let gramPanchayat?.villages?.find { it.name == village } }
    }

    @JvmStatic
    fun getStateLocal(state: StateData): String {
        if (getLocalLang().equals("hi")) return state.stateHindi
        return state.state
    }

    @JvmStatic
    fun getDistrictLocal(district: DistData): String {
        if (getLocalLang().equals("hi")) return district.nameHindi
        return district.name
    }

    @JvmStatic
    fun getBlockLocal(block: Block): String {
        if (getLocalLang().equals("hi")) return block.nameHindi ?: block.name ?: ""
        return block.name ?: ""
    }

    @JvmStatic
    fun getGramPanchayatLocal(gramPanchayat: GramPanchayat): String {
        if (getLocalLang().equals("hi")) return gramPanchayat.nameHindi ?: gramPanchayat.name ?: ""
        return gramPanchayat.name ?: ""
    }

    @JvmStatic
    fun getVillageLocal(village: Village): String {
        if (getLocalLang().equals("hi")) return village.nameHindi ?: village.name ?: ""
        return village.name ?: ""
    }

    @JvmStatic
    fun getSpecificLocalResource(context: Context, locale: String): Resources {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale(locale))
        return context.createConfigurationContext(configuration).resources
    }

    @JvmStatic
    fun getLocalValueFromArray(
        context: Context,
        dbString: String,
        @ArrayRes arrayResId: Int
    ): String {
        return if (SessionManager(context).appLanguage.equals("en").not()) {
            val array = context.resources.getStringArray(arrayResId)
            val index = getSpecificLocalResource(context, "en")
                .getStringArray(arrayResId).indexOf(dbString)
            return if (index > 0) array[index]
            else ""
        } else dbString
    }

    @JvmStatic
    fun getReferralFacilityByCategory(category: String): List<ReferralFacilityData?> {
        val context = IntelehealthApplication.getAppContext()
        val jsonObject = FileUtils.encodeJSON(context, FACILITY_ALL_DATA_JSON)
        val referralFacilityData: ReferralFacility = Gson().fromJson(
            jsonObject.toString(),
            ReferralFacility::class.java
        )

        return referralFacilityData.data.filter { it.category.contains(category) }
    }

    @JvmStatic
    fun getReferralFacilityByCategoryAndLocation(
        category: String,
        blockName: String,
        districtName: String
    ): List<ReferralFacilityData?> {
        val context = IntelehealthApplication.getAppContext()
        val jsonObject = FileUtils.encodeJSON(context, FACILITY_ALL_DATA_JSON)
        val referralFacilityData: ReferralFacility = Gson().fromJson(
            jsonObject.toString(),
            ReferralFacility::class.java
        )

        return referralFacilityData.data.filter {
            it.category.contains(category) && it.block.contains(
                blockName
            ) && it.district.contains(districtName)
        }
    }


    @JvmStatic
    fun getReferralFacilityByName(facilityName: String): ReferralFacilityData? {
        val context = IntelehealthApplication.getAppContext()
        val jsonObject = FileUtils.encodeJSON(context, FACILITY_ALL_DATA_JSON)
        val referralFacilityData: ReferralFacility = Gson().fromJson(
            jsonObject.toString(),
            ReferralFacility::class.java
        )

        return referralFacilityData.data.find { it.facilityName == facilityName }
    }

    @JvmStatic
    fun getReferralFacilityDataByLanguage(
        referralFacilityData: ReferralFacilityData,
        type: ReferralFacilityDataFormatType
    ): String {
        if (type == ReferralFacilityDataFormatType.VIEW) {
            if (referralFacilityData.id == 0L) {
                if (getLocalLang().equals("hi")) return referralFacilityData.facilityNameHi
                return referralFacilityData.facilityName
            } else {
                if (getLocalLang().equals("hi")) return "${referralFacilityData.facilityNameHi} (${referralFacilityData.blockHi})"
                return "${referralFacilityData.facilityName} (${referralFacilityData.block})"
            }

        } else {
            return "${referralFacilityData.facilityName} (${referralFacilityData.category}, ${referralFacilityData.block}) Incharge - ${referralFacilityData.nameOfMoicIncharge} (${referralFacilityData.contactNumber})"
        }
    }
}