package org.intelehealth.app.ui.baseline_survey.model

import org.intelehealth.config.room.entity.PatientRegistrationFields

data class Baseline(

    // General
    var occupation: String = "",
    var caste: String = "",
    var education: String = "",
    var ayushmanCard: String = "",
    var mgnregaCard: String = "",
    var bankAccount: String = "",
    var phoneOwnership: String = "",
    var familyWhatsApp: String = "",
    var martialStatus: String = "",

    // Medical
    var hbCheck: String = "",
    var bpCheck: String = "",
    var sugarCheck: String = "",
    var bpValue: String = "",
    var diabetesValue: String = "",
    var arthritisValue: String = "",
    var anemiaValue: String = "",
    var surgeryValue: String = "",
    var surgeryReason: String = "",
    var smokingHistory: String = "",
    var smokingRate: String = "",
    var smokingDuration: String = "",
    var smokingFrequency: String = "",
    var chewTobacco: String = "",
    var alcoholHistory: String = "",
    var alcoholRate: String = "",
    var alcoholDuration: String = "",
    var alcoholFrequency: String = "",

    // Other
    var headOfHousehold: String = "",
    var rationCardCheck: String = "",
    var economicStatus: String = "",
    var religion: String = "",
    var totalHouseholdMembers: String = "",
    var usualHouseholdMembers: String = "",
    var numberOfSmartphones: String = "",
    var numberOfFeaturePhones: String = "",
    var numberOfEarningMembers: String = "",
    var electricityCheck: String = "",
    var waterCheck: String = "",
    val loadSheddingHours: String = "",
    val loadSheddingDays: String = "",
    var sourceOfWater: String = "",
    var safeguardWater: String = "",
    var distanceFromWater: String = "",
    var toiletFacility: String = "",
    var houseStructure: String = "",
    var cultivableLand: String = "",
    var averageIncome: String = "",
    var fuelType: String = "",
    var sourceOfLight: String = "",
    var handWashPractices: String = "",
    var ekalServiceCheck: String = "",
    var relationWithHousehold: String = "",
)
