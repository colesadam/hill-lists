package uk.colessoft.android.hilllist.ui.activity

import android.content.Intent
import android.os.Bundle

import androidx.lifecycle.ViewModelProviders

import uk.colessoft.android.hilllist.R
import uk.colessoft.android.hilllist.ui.activity.dialogs.BaseActivity
import uk.colessoft.android.hilllist.ui.fragment.DisplayHillListFragment
import uk.colessoft.android.hilllist.ui.fragment.HillDetailFragment
import uk.colessoft.android.hilllist.ui.viewmodel.HillDetailViewModel
import uk.colessoft.android.hilllist.ui.viewmodel.ViewModelFactory
import javax.inject.Inject


class DisplayHillListFragmentActivity : BaseActivity(), DisplayHillListFragment.OnHillSelectedListener {

    internal var useMetricHeights: Boolean = false


    @Inject
    lateinit var vmFactory: ViewModelFactory<HillDetailViewModel>

    lateinit var vm: HillDetailViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        vm = ViewModelProviders.of(this, vmFactory)[HillDetailViewModel::class.java]

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