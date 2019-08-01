package uk.colessoft.android.hilllist.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import uk.colessoft.android.hilllist.R
import uk.colessoft.android.hilllist.activity.dialogs.BaseActivity
import uk.colessoft.android.hilllist.dao.HillsOrder
import uk.colessoft.android.hilllist.fragment.DisplayHillListFragment
import uk.colessoft.android.hilllist.fragment.HillDetailFragment
import uk.colessoft.android.hilllist.viewmodel.HillDetailViewModel
import uk.colessoft.android.hilllist.component.viewModel
import uk.colessoft.android.hilllist.viewmodel.ViewModelFactory
import javax.inject.Inject


//import uk.colessoft.android.hilllist.viewmodel.HillDetailViewModelFactory;

class DisplayHillListFragmentActivity : BaseActivity(), DisplayHillListFragment.OnHillSelectedListener {
    /** Called when the activity is first created.  */

    internal var useMetricHeights: Boolean = false


    @Inject
    lateinit var vmFactory: ViewModelFactory<HillDetailViewModel>

    lateinit var vm: HillDetailViewModel

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
        vm = ViewModelProviders.of(this, vmFactory)[HillDetailViewModel::class.java]

        vm.getSelectedHills(null, hilltype, null,
                null)
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