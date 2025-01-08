package org.intelehealth.app.ayu.visit.notification

data class LocalPrescriptionInfo(
    var visitUUID: String,
    var shareStatus: Boolean,
    var createdAt: Long
)