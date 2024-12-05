package org.intelehealth.features.ondemand.mediator.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Vaghela Mithun R. on 15-11-2024 - 13:38.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Parcelize
data class ChatRoomConfig(
    val patientName: String = "",
    val patientId: String = "",
    val hwName: String = "",
    val visitId: String = "",
    val fromId: String = "",
    var toId: String = "",
    val openMrsId: String = ""
) : Parcelable