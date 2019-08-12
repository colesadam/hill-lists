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

import java.util.Date
import java.util.HashMap

import uk.colessoft.android.hilllist.R
import uk.colessoft.android.hilllist.ui.activity.dialogs.BaseActivity
import uk.colessoft.android.hilllist.ui.fragment.HillDetailFragment
import uk.colessoft.android.hilllist.domain.HillDetail
import uk.colessoft.android.hilllist.ui.viewmodel.HillDetailViewModel
import uk.colessoft.android.hilllist.ui.viewmodel.HillListViewModel
import uk.colessoft.android.hilllist.ui.viewmodel.ViewModelFactory
import javax.inject.Inject

class HillDetailFragmentActivity : BaseActivity() {

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

    private val hillnameView: TextView? = null

    private val hillheight: TextView? = null

    @Inject
    lateinit var vmFactory: ViewModelFactory<HillDetailViewModel>

    lateinit var vm: HillDetailViewModel


    override fun onCreateDialog(id: Int): Dialog? {
        when (id) {
            DATE_DIALOG_ID -> {
                mYear = realDate.year + 1900
                mMonth = realDate.month
                mDay = realDate.date

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
        val rowid = intent.extras!!.getLong("rowid")

        val fragment = supportFragmentManager
                .findFragmentById(R.id.hill_detail_fragment) as HillDetailFragment?

        fragment!!.updateHill(rowid)

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

        val classesMap: HashMap<String, String> = object : HashMap<String, String>() {
            init {
                put("M", "Munro")
                put("MT", "Munro Top")
                put("Ma", "Marilyn")
                put("twinMa", "Marilyn twin-top")
                put("sMa", "Sub-Marilyn")
                put("Mur", "Murdo")
                put("sMur", "Sub-Murdo")
                put("ssMur", "double Sub-Murdo")
                put("C", "Corbett")
                put("CT", "Corbett Tops (all)")
                put("CTM", "Corbett Top of Munro")
                put("CTC", "Corbett Top of Corbett")
                put("G", "Graham")
                put("GT", "Graham Tops (all)")
                put("GTM", "Graham Top of Munro")
                put("GTC", "Graham Top of Corbett")
                put("GTG", "Graham Top of Graham")
                put("sG", "Sub-Graham")
                put("ssG", "double Sub-Graham")
                put("D", "Donald")
                put("DT", "Donald Top")
                put("N", "Nuttall")
                put("Hew", "Hewitt")
                put("sHew", "Sub-Hewitt")
                put("ssHew", "double Sub-Hewitt")
                put("W", "Wainwright")
                put("WO", "Wainwright Outlying Fell")
                put("Dewey", "Dewey")
                put("B", "Birkett")
                put("Hu", "HuMP")
                put("twinHu", "HuMP twin-top")
                put("CoH", "Historic County Top")
                put("CoU", "Current County/UA Top")
                put("CoA", "Administrative County Top")
                put("CoL", "London Borough Top")
                put("twinCoU", "Twin Current County/UA Top")
                put("twinCoA", "Twin Administrative County Top")
                put("twinCoL", "Twin London Borough Top")
                put("xMT", "Deleted Munro Top")
                put("xMa", "Deleted Marilyn")
                put("xsMa", "Deleted Sub-Marilyn")
                put("xC", "Deleted Corbett")
                put("xCT", "Deleted Corbett Top")
                put("xDT", "Deleted Donald Top")
                put("xN", "Deleted Nuttall")
                put("x5", "Deleted Dewey")
                put("xHu", "Deleted HuMP")
                put("xCoH", "Deleted County Top")
                put("BL", "Buxton & Lewis")
                put("Bg", "Bridge")
                put("T100", "Trail 100")

            }
        }
    }
}
