package org.intelehealth.app.activities.digitalStethoscope

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.intelehealth.app.R
import org.intelehealth.app.utilities.SessionManager
import java.util.Locale

enum class AiStethoscopeOption {
    HEART,
    LUNG
}

class AiStethRecordActivity : AppCompatActivity() {
    var sessionManager: SessionManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_steth_record)
        setupAppLanguage()
        setupToolbar()

        val intent = intent
        val option = intent.getStringExtra("option")



    }


    private fun setupAppLanguage() {
        sessionManager = SessionManager(this)
        val language = sessionManager!!.appLanguage

        //In case of crash still the org should hold the current lang fix.
        if (!language.equals("", ignoreCase = true)) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }
        sessionManager!!.currentLang = resources.configuration.locale.toString()
    }

    private fun setupToolbar() {
        /*
         * Toolbar which displays back arrow on action bar
         * Add the below lines for every activity*/
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTheme)
        toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener { onBackPressed() }

    }
}