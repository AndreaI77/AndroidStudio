package edu.andreaivanova.myfavouritepets.model

import androidx.lifecycle.ViewModel

class FormViewModel : ViewModel() {
    var clase : Clase?= null
    var pelaje: Pelaje? = null
    var pet : Pet? = null

    var nombre = ""
    var latName = ""
    var rating = 0.0f
    var imagen = ""
    var id = ""

}