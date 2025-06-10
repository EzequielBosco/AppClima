package com.istea.appdelclima.presentacion.ciudades

import com.appclima.model.City

sealed class CitiesIntention {
    data class Search( val name:String ) : CitiesIntention()
    data class Select(val city: City) : CitiesIntention()
}
