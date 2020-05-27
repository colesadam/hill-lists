package uk.colessoft.android.hilllist.ui.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.DatePicker
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders

import uk.colessoft.android.hilllist.R
import uk.colessoft.android.hilllist.ui.activity.dialogs.BaseActivity
import uk.colessoft.android.hilllist.ui.fragment.HillDetailFragment
import uk.colessoft.android.hilllist.domain.HillDetail
import uk.colessoft.android.hilllist.ui.viewmodel.HillDetailViewModel
import uk.colessoft.android.hilllist.ui.viewmodel.HillListViewModel
import uk.colessoft.android.hilllist.ui.viewmodel.ViewModelFactory
import java.util.*
import javax.inject.Inject

class HillDetailActivity : BaseActivity() {

    private val osLink: String? = null

    private val hillDetail: HillDetail? = null
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private val mDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        mYear = year
        mMonth = monthOfYear
        mDay = dayOfMonth
        updateDisplay()
    }

    private val dateClimbed: TextView? = null

    private val realDate = Date()

    private val calendar = GregorianCalendar()

    private val hillnameView: TextView? = null

    private val hillheight: TextView? = null

    @Inject
    lateinit var vmFactory: ViewModelFactory<HillDetailViewModel>

    lateinit var vm: HillDetailViewModel


    override fun onCreateDialog(id: Int): Dialog? {
        calendar.setTime(realDate)
        when (id) {
            DATE_DIALOG_ID -> {
                mYear = calendar.get(Calendar.YEAR)
                mMonth = calendar.get(Calendar.MONTH)
                mDay = calendar.get(Calendar.DAY_OF_MONTH)

                return DatePickerDialog(this, mDateSetListener,
                        mYear, mMonth, mDay)
            }
            MARK_HILL_CLIMBED_DIALOG -> {

            }
        }
        return null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProviders.of(this, vmFactory)[HillDetailViewModel::class.java]
        updateFromPreferences()
        setContentView(R.layout.hill_detail_fragment)

        val fragment = supportFragmentManager
                .findFragmentById(R.id.hill_detail_fragment) as HillDetailFragment?


    }

    private fun updateDisplay() {
        dateClimbed!!.text = StringBuilder().append(mDay).append("/")
                .append(mMonth + 1).append("/").append(mYear)
    }

    private fun updateFromPreferences() {
        val context = applicationContext
        val prefs = PreferenceManager
                .getDefaultSharedPreferences(context)

        val useMetricHeights = prefs.getBoolean(
                PreferencesActivity.PREF_METRIC_HEIGHTS, false)
    }

    companion object {
        private val DATE_DIALOG_ID = 0
        private val MARK_HILL_CLIMBED_DIALOG = 1


    }
}
