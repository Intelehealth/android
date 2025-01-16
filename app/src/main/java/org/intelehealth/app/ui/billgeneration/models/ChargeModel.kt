package org.intelehealth.app.ui.billgeneration.models

data class ChargeModel(  val consultationChargesVisible: Boolean,
                         val followUpChargesVisible: Boolean,
                         val glucoseRanChargesVisible: Boolean,
                         val glucoseFChargesVisible: Boolean,
                         val glucosePpnChargesVisible: Boolean,
                         val uricAcidChargesVisible: Boolean,
                         val haemeoChargesVisible: Boolean,
                         val cholestrolChargesVisible: Boolean,
                         val bpChargesVisible: Boolean)
