package uk.colessoft.android.hilllist.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.lifecycle.ViewModelProviders

import uk.colessoft.android.hilllist.R
import uk.colessoft.android.hilllist.ui.activity.dialogs.BaseActivity
import uk.colessoft.android.hilllist.ui.fragment.HillListFragment
import uk.colessoft.android.hilllist.ui.fragment.HillDetailFragment
import uk.colessoft.android.hilllist.ui.viewmodel.HillListViewModel
import uk.colessoft.android.hilllist.ui.viewmodel.ViewModelFactory
import javax.inject.Inject


class HillListActivity : BaseActivity(),HillListFragment.OnHillSelectedListener {

    var rowId:Long? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hill_lists_menu_p,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.menu_show_map_p) {
            val mapIntent = Intent(this,
                    HillListMapActivity::class.java)
            mapIntent.putExtras(intent.extras)
            mapIntent.putExtra("moreWhen", vm.searchString)
            mapIntent.putExtra("selectedHill",rowId)
            startActivity(mapIntent)
            return true
        }
        else return false
    }

    @Inject
    lateinit var vmFactory: ViewModelFactory<HillListViewModel>

    lateinit var vm: HillListViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this, vmFactory)[HillListViewModel::class.java]
        setContentView(R.layout.hill_list_fragment)

    }


    override fun onHillSelected(hillId: Long) {
        rowId = hillId
        val fragment = supportFragmentManager
                .findFragmentById(R.id.hill_detail_fragment) as HillDetailFragment?

        if (fragment == null || !fragment.isInLayout) {
            val intent = Intent(this, HillDetailActivity::class.java)
            intent.putExtra("rowid", hillId)
            startActivity(intent)
        } else {

            vm.select(hillId)
        }

    }


}