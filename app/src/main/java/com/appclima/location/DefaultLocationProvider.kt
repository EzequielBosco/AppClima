package com.appclima.location

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class DefaultLocationProvider(
    private val context: Context
) : LocationProvider {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Pair<Double, Double>? = suspendCancellableCoroutine { cont ->
        Log.d("LocationProvider", "Requesting current location...")

        fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    Log.d("LocationProvider", "Location received: lat=$lat, lon=$lon")
                    cont.resume(lat to lon)
                } else {
                    Log.w("LocationProvider", "Location is null!")
                    cont.resume(null)
                }
            }
            .addOnFailureListener { error ->
                Log.e("LocationProvider", "Failed to get location: ${error.message}", error)
                cont.resume(null)
            }
    }
}
