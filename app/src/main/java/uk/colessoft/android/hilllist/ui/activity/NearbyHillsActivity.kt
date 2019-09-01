package uk.colessoft.android.hilllist.ui.activity

import android.app.ProgressDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import uk.colessoft.android.hilllist.R
import uk.colessoft.android.hilllist.ui.activity.dialogs.BaseActivity
import uk.colessoft.android.hilllist.ui.fragment.HillDetailFragment
import uk.colessoft.android.hilllist.ui.fragment.NearbyHillsFragment.OnLocationFoundListener
import uk.colessoft.android.hilllist.ui.viewmodel.HillListViewModel
import uk.colessoft.android.hilllist.ui.viewmodel.NearbyHillListViewModel
import uk.colessoft.android.hilllist.ui.viewmodel.ViewModelFactory
import uk.colessoft.android.hilllist.utility.LocationRepository
import javax.inject.Inject

class NearbyHillsActivity : BaseActivity(), OnLocationFoundListener, uk.colessoft.android.hilllist.ui.fragment.NearbyHillsFragment.OnHillSelectedListener {
    private var dialog: ProgressDialog? = null

    @Inject
    lateinit var vmFactory: ViewModelFactory<NearbyHillListViewModel>
    lateinit var vm: NearbyHillListViewModel

    @Inject
    lateinit var locationRepository: LocationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this, vmFactory)[NearbyHillListViewModel::class.java]

        locationRepository.getLocation().observe(this, Observer { loc: Location? -> Log.i(this.toString(),"Location: ${loc!!.latitude} ${loc!!.longitude}") })

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
