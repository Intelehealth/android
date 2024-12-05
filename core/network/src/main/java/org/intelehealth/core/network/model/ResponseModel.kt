package org.intelehealth.core.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.intelehealth.coreroomdb.entity.*

/**
 * Created by - Prajwal W. on 14/10/24.
 * Email: prajwalwaingankar@gmail.com
 * Mobile: +917304154312
 **/
data class PullResponse(

    @SerializedName("patientlist") @Expose var patientlist: List<Patient>,

    @SerializedName("pullexecutedtime") @Expose var pullexecutedtime: String,

    @SerializedName("patientAttributeTypeListMaster") @Expose var patientAttributeTypeListMaster: List<PatientAttributeTypeMaster>,

    @SerializedName("patientAttributesList") @Expose var patientAttributesList: List<PatientAttribute>,

    @SerializedName("visitlist") @Expose var visitlist: List<Visit>,

    @SerializedName("encounterlist") @Expose var encounterlist: List<Encounter>,

    @SerializedName("obslist") @Expose var obslist: List<Observation>,

    @SerializedName("locationlist") @Expose var locationlist: List<PatientLocation>,

    @SerializedName("providerlist") @Expose var providerlist: List<Provider>,

    @SerializedName("providerAttributeTypeList") @Expose var providerAttributeTypeList: List<ProviderAttribute>,

    @SerializedName("providerAttributeList") @Expose var providerAttributeList: List<ProviderAttribute>,

    @SerializedName("visitAttributeTypeList") @Expose var visitAttributeTypeList: List<VisitAttribute>,

    @SerializedName("visitAttributeList") @Expose var visitAttributeList: List<VisitAttribute>,

    @SerializedName("pageNo") @Expose var pageNo: Int,

    @SerializedName("totalCount") @Expose var totalCount: Int,

    /*@SerializedName("propertyContents") @Expose
     var propertyContents: ConfigResponse*/
)