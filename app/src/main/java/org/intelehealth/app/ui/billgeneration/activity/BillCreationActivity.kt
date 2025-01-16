package org.intelehealth.app.ui.billgeneration.activity

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.intelehealth.app.R
import org.intelehealth.app.activities.homeActivity.HomeScreenActivity_New
import org.intelehealth.app.databinding.ActivityBillCreationBinding
import org.intelehealth.app.ui.billgeneration.models.BillDetails
import org.intelehealth.app.ui.billgeneration.viewmodel.BillGenerationViewModel
import org.intelehealth.app.utilities.DialogUtils
import org.intelehealth.app.utilities.SessionManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale

class BillCreationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBillCreationBinding
    private val viewModel: BillGenerationViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private lateinit var billDetails: BillDetails
    private var paidOrUnpaid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        setupActionBar()
        setupUI()
        observeViewModel()

        // Fetch initial data
        intent?.let { billDetails = getBillDetailsFromIntent(it)!! }
        showAddedBillDetails()
    }

    private fun showAddedBillDetails() {
        binding.contentGenerateBill.patientDetailsTV.text = viewModel.setPatientDetails(billDetails)
        manageCardView()
        viewModel.manageTestsData(binding,billDetails.selectedTestsList, billDetails)
        setupPaymentStatus()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupUI() {
        setupReasonTextWatcher()
        setupPaymentButtons()
        setupBillActionButtons()
    }

    private fun setupReasonTextWatcher() {
        binding.contentGenerateBill.reasonET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handleReasonTextChanged(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun handleReasonTextChanged(s: CharSequence?) {
        with(binding.contentGenerateBill) {
            if (!s.isNullOrEmpty()) {
                val isEmpty = reasonET.text.isNullOrEmpty()
                tvReasonErrorNotPay.visibility = if (isEmpty) View.VISIBLE else View.GONE
                reasonET.background = ContextCompat.getDrawable(
                    this@BillCreationActivity,
                    if (isEmpty) R.drawable.input_field_error_bg_ui2 else R.drawable.bg_input_fieldnew
                )
            }
        }
    }

    private fun setupPaymentButtons() {
        binding.contentGenerateBill.yesPayBill.setOnClickListener {
            toggleReasonVisibility(false)
            paidOrUnpaid = "Paid"
        }
        binding.contentGenerateBill.noPayBill.setOnClickListener {
            toggleReasonVisibility(true)
            paidOrUnpaid = "Unpaid"
        }
    }

    private fun toggleReasonVisibility(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        with(binding.contentGenerateBill) {
            reasonET.visibility = visibility
            reasonTIL.visibility = visibility
        }
    }

    private fun setupBillActionButtons() {
        with(binding.contentGenerateBill) {
            buttonConfirmBill.setOnClickListener { handleBillConfirmation() }
            buttonPrint.setOnClickListener { viewModel.printBill() }
            buttonDownload.setOnClickListener { viewModel.generatePdf(binding) }
            buttonShare.setOnClickListener { viewModel.shareFile() }
        }
    }

    private fun handleBillConfirmation() {
        if (isValidBill()) {
            lifecycleScope.launch {
                val result = viewModel.confirmBill(billDetails)
                if (result) {
                    updatePaymentStatus(viewModel.paymentStatus.value.toString())
                    onBillCreated()
                    viewModel.syncOnServer()
                }
            }
        }
    }

    private fun updatePaymentStatus(paymentStatus: String) {
        with(binding.contentGenerateBill.paymentStatus) {
            visibility = View.VISIBLE
            text = if (paymentStatus == "Paid") getString(R.string.paid)
            else getString(R.string.unpaid)
        }
    }

    private fun manageViewsVisibility(optionButtonsVisibility: Boolean) {
        val visibility = if (optionButtonsVisibility) View.VISIBLE else View.GONE
        with(binding.contentGenerateBill) {
            buttonPrint.visibility = visibility
            buttonDownload.visibility = visibility
            buttonShare.visibility = visibility
        }
    }

    private fun onBillCreated() {
        Toast.makeText(this, getString(R.string.bill_generated_success), Toast.LENGTH_LONG).show()
        with(binding.contentGenerateBill) {
            llBillNotPayingReason.visibility = View.GONE
            buttonConfirmBill.visibility = View.GONE
        }
        manageViewsVisibility(true)
    }

    private fun manageCardView() {
        val selectedTests = billDetails.selectedTestsList
        val viewMap = mapOf(
            R.string.blood_glucose_non_fasting to binding.contentGenerateBill.glucoseNfChargesCV,
            R.string.blood_glucose_fasting to binding.contentGenerateBill.glucoseFChargesCV,
            R.string.blood_glucose_post_prandial to binding.contentGenerateBill.glucosePpnChargesCV,
            R.string.blood_glucose_random to binding.contentGenerateBill.glucoseRanChargesCV,
            R.string.uric_acid to binding.contentGenerateBill.uricAcidChargesCV,
            R.string.total_cholestrol to binding.contentGenerateBill.cholestrolChargesCV,
            R.string.haemoglobin to binding.contentGenerateBill.haemeoChargesCV,
            R.string.visit_summary_bp to binding.contentGenerateBill.bpChargesCV
        )

        viewMap.forEach { (stringRes, view) ->
            if (selectedTests.contains(getString(stringRes))) {
                view.visibility = View.VISIBLE
            }
        }


    }

    private fun isValidBill(): Boolean {
        with(binding.contentGenerateBill) {
            if (!yesPayBill.isChecked && !noPayBill.isChecked) {
                DialogUtils().showCommonDialog(
                    this@BillCreationActivity, 0,
                    getString(R.string.error),
                    getString(R.string.select_payment_information),
                    true, getString(R.string.ok), getString(R.string.cancel), {}
                )
                return false
            }

            if (noPayBill.isChecked && reasonTIL.visibility == View.VISIBLE) {
                val reason = reasonET.text.toString()
                if (reason.isEmpty()) {
                    tvReasonErrorNotPay.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.enter_reason_toast)
                    }
                    return false
                } else {
                    viewModel.updatePaymentStatus(if (yesPayBill.isChecked) "Paid" else "Unpaid - $reason")
                }
            }
            return true
        }
    }
    private fun observeViewModel() {
        viewModel.apply {
            patientDetails.observe(this@BillCreationActivity) {
                binding.contentGenerateBill.patientDetailsTV.text = it
            }

            paymentStatus.observe(this@BillCreationActivity) {
                binding.contentGenerateBill.paymentStatus.apply {
                    visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                    text = it
                }
            }

            totalAmount.observe(this@BillCreationActivity) {
                binding.contentGenerateBill.totalChargesTV.text = it.toString()
            }
        }
    }
    private fun getBillDetailsFromIntent(intent: Intent): BillDetails? {
        val bundle = intent.getBundleExtra("BUNDLE")
        billDetails = bundle?.getSerializable("billDetails") as? BillDetails
            ?: run {
                return billDetails
            }
        return billDetails
    }

    private fun setupPaymentStatus() {
        if (billDetails.billType != "NA") {
            when {
                billDetails.billType == "Paid" -> {
                    binding.contentGenerateBill.paymentStatus.visibility = View.VISIBLE
                    binding.contentGenerateBill.paymentStatus.text = getString(R.string.paid)
                    // paymentStatusTV.setBackgroundColor(Color.GREEN)
                }
                billDetails.billType.contains("Unpaid", ignoreCase = true) -> {
                    binding.contentGenerateBill.paymentStatus.visibility = View.VISIBLE
                    binding.contentGenerateBill.paymentStatus.text = getString(R.string.unpaid)
                    // paymentStatusTV.setBackgroundColor(Color.RED)
                }
            }
            binding.contentGenerateBill.llBillNotPayingReason.visibility = View.GONE
            binding.contentGenerateBill.buttonConfirmBill.visibility = View.GONE
            manageViewsVisibility(true)

        }
    }

    private fun loadBitmap(view: View, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home_menu -> {
                val intent = Intent(
                    this@BillCreationActivity,
                    HomeScreenActivity_New::class.java
                )
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
