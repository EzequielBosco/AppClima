package com.appclima.location

interface LocationProvider {
    suspend fun getCurrentLocation(): Pair<Double, Double>?
}
