package org.intelehealth.app.ui.billgeneration.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.intelehealth.app.R
import org.intelehealth.app.databinding.ActivityBillCreationBinding
import org.intelehealth.app.ui.billgeneration.models.BillDetails
import org.intelehealth.app.ui.billgeneration.models.BillTestsChargeModel
import org.intelehealth.app.ui.billgeneration.repository.BillRepository
import org.intelehealth.app.ui.billgeneration.utils.BillRate
import org.intelehealth.app.ui.billgeneration.utils.VisitType
import org.intelehealth.app.utilities.SessionManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class BillGenerationViewModel(application: Application) : AndroidViewModel(application) {
    private val context: Context = application.applicationContext
    private val sessionManager = SessionManager(context)
    private val repository = BillRepository(sessionManager, context)

    private val _patientDetails = MutableLiveData<String>()
    val patientDetails: LiveData<String> get() = _patientDetails

    private val mutablePaymentStatus = MutableLiveData<String>()
    val paymentStatus: LiveData<String> get() = mutablePaymentStatus

    private val _totalAmount = MutableLiveData<Int>()
    val totalAmount: LiveData<Int> get() = _totalAmount

    var finalBillPath: String? = null


    suspend fun confirmBill(billDetails: BillDetails): Boolean {
        val result = withContext(Dispatchers.IO) {
            repository.confirmBill(billDetails, paymentStatus.value.toString())
        }
        return result
    }

    fun printBill() {

    }

    fun updatePaymentStatus(paymentStatusValue: String) {
        mutablePaymentStatus.postValue(paymentStatusValue)
    }

    suspend fun syncOnServer() {
        try {
            repository.syncOnServer()
        } catch (_: Exception) {
        }
    }

    fun setPatientDetails(billDetails: BillDetails): String {
        val patientDetails = buildString {
            append(context.getString(R.string.receipt_no)).append(billDetails.receiptNum)
                .append("\n")
            append(context.getString(R.string.client_name)).append(billDetails.patientName)
                .append("\n")
            append(context.getString(R.string.client_id)).append(billDetails.patientOpenID)
                .append("\n")
            append(context.getString(R.string.visit_id)).append(billDetails.patientHideVisitID)
                .append("\n")
            append(context.getString(R.string.contact_no)).append(billDetails.patientPhoneNum)
                .append("\n")
            append(context.getString(R.string.client_village_name)).append(billDetails.patientVillage)
                .append("\n")
            append(context.getString(R.string.date_bill)).append(billDetails.billDateString)
        }
        return patientDetails

    }

    fun manageTestsData(
        binding: ActivityBillCreationBinding,
        selectedTests: ArrayList<String?>,
        billDetails: BillDetails
    ) {
        val billTestsChargeModel = BillTestsChargeModel()
        var totalAmount = 0
        if (billDetails.visitType.equals(VisitType.CONSULTATION.value, true)) {
            val price = BillRate.CONSULTATION.value
            billTestsChargeModel.consultationChargeAmount = "₹$price/-"
            billTestsChargeModel.consultationChargesVisible = true
            totalAmount += price
        }
        if (billDetails.visitType.equals(VisitType.FOLLOW_UP.value, true)) {
            val price = BillRate.FOLLOW_UP.value
            billTestsChargeModel.followUpChargeAmount = "₹$price/-"
            totalAmount += price
        }
        if (selectedTests.contains(context.getString(R.string.blood_glucose_random))) {
            val price = BillRate.GLUCOSE_RANDOM.value
            billTestsChargeModel.glucoseRandomAmount = "₹$price/-"
            billTestsChargeModel.glucoseRandomVisible = true
            totalAmount += price
        }
        if (selectedTests.contains(context.getString(R.string.blood_glucose_fasting))) {
            val price = BillRate.GLUCOSE_FASTING.value
            billTestsChargeModel.glucoseFastingChargeAmount = "₹$price/-"
            billTestsChargeModel.glucoseFastingChargesVisible = true
            totalAmount += price
        }
        if (selectedTests.contains(context.getString(R.string.blood_glucose_post_prandial))) {
            val price = BillRate.GLUCOSE_POST_PRANDIAL.value
            billTestsChargeModel.glucosePostPrandialChargeAmount = "₹$price/-"
            billTestsChargeModel.glucosePostPrandialChargesVisible = true
            totalAmount += price
        }

        if (selectedTests.contains(context.getString(R.string.uric_acid))) {
            val price = BillRate.URIC_ACID.value
            billTestsChargeModel.uricAcidChargeAmount = "₹$price/-"
            billTestsChargeModel.uricAcidChargesVisible = true
            totalAmount += price
        }
        if (selectedTests.contains(context.getString(R.string.haemoglobin))) {
            val price = BillRate.HEMOGLOBIN.value
            billTestsChargeModel.hemoglobinChargeAmount = "₹$price/-"
            billTestsChargeModel.hemoglobinChargesVisible = true
            totalAmount += price
        }
        if (selectedTests.contains(context.getString(R.string.total_cholestrol))) {
            val price = BillRate.CHOLESTEROL.value
            billTestsChargeModel.cholestrolChargeAmount = "₹$price/-"
            billTestsChargeModel.cholestrolChargesVisible = true
            totalAmount += price
        }
        if (selectedTests.contains(context.getString(R.string.visit_summary_bp))) {
            val price = BillRate.BP.value
            billTestsChargeModel.bpChargeAmount = "₹$price/-"
            billTestsChargeModel.bpChargesVisible = true
            totalAmount += price
        }
        billTestsChargeModel.totalChargeAmount = "₹${totalAmount}/-"
        binding.contentGenerateBill.testsChargesModel = billTestsChargeModel
    }
     fun generatePdf(binding: ActivityBillCreationBinding) {
        val bitmap = loadBitmap(
            binding.contentGenerateBill.finalBillCV,
            binding.contentGenerateBill.finalBillCV.width,
            binding.contentGenerateBill.finalBillCV.height
        )
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        page.canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)

        val file = File(context.getExternalFilesDir("Bill"), "bill_${System.currentTimeMillis()}.pdf")
        try {
            FileOutputStream(file).use { pdfDocument.writeTo(it) }
            Toast.makeText(context, "successfully pdf created", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(context, "Something wrong: ", Toast.LENGTH_SHORT)
                .show()
        } finally {
            pdfDocument.close()
        }
    }
    private fun loadBitmap(view: View, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
     fun shareFile() {
        val file = File(finalBillPath ?: return)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        context.startActivity(Intent.createChooser(intent, "Share the file...."))
    }

}