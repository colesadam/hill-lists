package uk.colessoft.android.hilllist.ui.activity

import android.content.Intent
import android.os.Bundle

import androidx.lifecycle.ViewModelProviders

import uk.colessoft.android.hilllist.R
import uk.colessoft.android.hilllist.domain.HillDetail
import uk.colessoft.android.hilllist.ui.activity.dialogs.BaseActivity
import uk.colessoft.android.hilllist.ui.fragment.HillListFragment
import uk.colessoft.android.hilllist.ui.fragment.HillDetailFragment
import uk.colessoft.android.hilllist.ui.viewmodel.HillListViewModel
import uk.colessoft.android.hilllist.ui.viewmodel.ViewModelFactory
import javax.inject.Inject


class HillListFragmentActivity : BaseActivity(),HillListFragment.OnHillSelectedListener {

    internal var useMetricHeights: Boolean = false


    @Inject
    lateinit var vmFactory: ViewModelFactory<HillListViewModel>

    lateinit var vm: HillListViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        vm = ViewModelProviders.of(this, vmFactory)[HillListViewModel::class.java]

        setContentView(R.layout.hill_list_fragment)

    }


    override fun onHillSelected(hillDetail: HillDetail) {

        val fragment = supportFragmentManager
                .findFragmentById(R.id.hill_detail_fragment) as HillDetailFragment?

        if (fragment == null || !fragment.isInLayout) {
            val intent = Intent(this, HillDetailFragmentActivity::class.java)
            intent.putExtra("rowid", hillDetail.hill.h_id)
            startActivity(intent)
        } else {

            vm.select(hillDetail)
        }

    }

}