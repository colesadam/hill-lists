package uk.colessoft.android.hilllist.ui.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    private val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0x00001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this, vmFactory)[NearbyHillListViewModel::class.java]

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) run {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)


        }else observeLocation()



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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    observeLocation()

                } else {
                    finish()
                }

            }
        }
    }

    private fun observeLocation() {
        locationRepository.getLocation().observe(this, Observer {

            loc: Location? ->
            run {
                vm.location.value = loc
                Log.i(this.toString(), "Location: ${loc!!.latitude} ${loc!!.longitude}")
            }

        })
    }

}
