package uk.colessoft.android.hilllist.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

import javax.inject.Inject

import uk.colessoft.android.hilllist.R
import uk.colessoft.android.hilllist.component.injector
import uk.colessoft.android.hilllist.dao.CountryClause
import uk.colessoft.android.hilllist.dao.HillsOrder
import uk.colessoft.android.hilllist.database.BritishHillsDatasource
import uk.colessoft.android.hilllist.fragment.DisplayHillListFragment
import uk.colessoft.android.hilllist.fragment.HillDetailFragment
import uk.colessoft.android.hilllist.model.HillDetail
import uk.colessoft.android.hilllist.viewmodel.HillDetailViewModel
import uk.colessoft.android.hilllist.viewmodel.ViewModelFactory
import uk.colessoft.android.hilllist.component.viewModel


//import uk.colessoft.android.hilllist.viewmodel.HillDetailViewModelFactory;

class DisplayHillListFragmentActivity : AppCompatActivity(), DisplayHillListFragment.OnHillSelectedListener {
    /** Called when the activity is first created.  */

    internal var useMetricHeights: Boolean = false


    private val viewModel by viewModel(this) { injector.viewModel }


    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val hilltype = intent.extras!!.getString("hilltype")
        val hilllistType = intent.extras!!
                .getString("hilllistType")
        if ("" != hilllistType) {
            title = hilllistType
        } else
            title = "Results"
        val country = intent.extras!!.getInt("country")
        val where = intent.extras!!.getString("search")
        (viewModel as HillDetailViewModel).getHills(null, hilltype, null, null, null, HillsOrder.NAME_ASC).observe(this, Observer { s -> Log.d("LIVEDATA", "onChanged: #############" + s[0].hill.hillname!!) })
        setContentView(R.layout.hill_list_fragment)

    }


    override fun onHillSelected(rowid: Int) {

        val fragment = supportFragmentManager
                .findFragmentById(R.id.hill_detail_fragment) as HillDetailFragment?

        if (fragment == null || !fragment.isInLayout) {
            val intent = Intent(this, HillDetailFragmentActivity::class.java)
            intent.putExtra("rowid", rowid)
            startActivity(intent)
        } else {

            fragment.updateHill(rowid)
        }

    }

}