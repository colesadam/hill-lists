package uk.colessoft.android.hilllist.utility

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient

class LocationRepository(val fusedLocationProviderClient: FusedLocationProviderClient) {

    private var location : MutableLiveData<Location> = MutableLiveData()

    fun getLocation() : LiveData<Location> {
        fusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener {loc: Location? ->
                    location.value = loc

                }

        return location
    }
}