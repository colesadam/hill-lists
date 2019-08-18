package uk.colessoft.android.hilllist.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

import uk.colessoft.android.hilllist.R
import uk.colessoft.android.hilllist.domain.HillDetail
import uk.colessoft.android.hilllist.ui.activity.dialogs.BaseActivity
import uk.colessoft.android.hilllist.ui.fragment.HillListFragment
import uk.colessoft.android.hilllist.ui.fragment.HillDetailFragment
import uk.colessoft.android.hilllist.ui.fragment.ListHillsMapFragment
import uk.colessoft.android.hilllist.ui.viewmodel.HillListViewModel
import uk.colessoft.android.hilllist.ui.viewmodel.ViewModelFactory
import javax.inject.Inject

class ListHillsMapFragmentActivity : BaseActivity(), HillListFragment.OnHillSelectedListener {

    @Inject
    lateinit var vmFactory: ViewModelFactory<HillListViewModel>

    lateinit var vm: HillListViewModel

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        vm = ViewModelProviders.of(this, vmFactory)[HillListViewModel::class.java]

        setContentView(R.layout.hills_map_fragment)
    }

    override fun onHillSelected(hillId: Long) {

        val fragment = supportFragmentManager
                .findFragmentById(R.id.hill_detail_fragment) as HillDetailFragment?

        if (fragment == null || !fragment.isInLayout) {
            val intent = Intent(this, HillDetailFragmentActivity::class.java)
            intent.putExtra("rowid", hillId)
            startActivity(intent)
        } else {

            vm.select(hillId)
        }

    }

}
