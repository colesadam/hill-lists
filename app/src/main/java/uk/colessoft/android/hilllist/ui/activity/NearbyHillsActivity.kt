package uk.colessoft.android.hilllist.ui.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders

import uk.colessoft.android.hilllist.R
import uk.colessoft.android.hilllist.ui.activity.dialogs.BaseActivity
import uk.colessoft.android.hilllist.ui.fragment.HillDetailFragment
import uk.colessoft.android.hilllist.ui.fragment.NearbyHillsFragment.OnLocationFoundListener
import uk.colessoft.android.hilllist.ui.viewmodel.HillListViewModel
import uk.colessoft.android.hilllist.ui.viewmodel.ViewModelFactory
import javax.inject.Inject

class NearbyHillsActivity : BaseActivity(), OnLocationFoundListener, uk.colessoft.android.hilllist.ui.fragment.NearbyHillsFragment.OnHillSelectedListener {
    private var dialog: ProgressDialog? = null

    @Inject
    lateinit var vmFactory: ViewModelFactory<HillListViewModel>
    lateinit var vm: HillListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this, vmFactory)[HillListViewModel::class.java]

        setContentView(R.layout.nearby_hills_fragment)

    }


    override fun locationFound() {
        dialog!!.dismiss()

    }

    override fun showDialog() {

        dialog = ProgressDialog(this)
        dialog!!.setTitle("Please wait...")
        dialog!!.setMessage("Acquiring Location ...")
        // dialog.setCancelable(true);
        dialog!!.setButton("Cancel") { dialog1, which ->
            // Use either finish() or return() to either close the activity
            // or just the dialog
            finish()
        }
        dialog!!.show()

    }

    override fun onHillSelected(rowid: Long) {
        val fragment = supportFragmentManager
                .findFragmentById(R.id.hill_detail_fragment) as HillDetailFragment?

        if (fragment == null || !fragment.isInLayout) {
            val intent = Intent(this, HillDetailActivity::class.java)
            intent.putExtra("rowid", rowid)
            startActivity(intent)
        } else {

            //fragment.updateHill(rowid);
        }

    }

}
