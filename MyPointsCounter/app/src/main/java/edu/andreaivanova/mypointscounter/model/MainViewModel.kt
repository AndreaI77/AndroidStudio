package edu.andreaivanova.mypointscounter.model

import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private var _id= 0
    private var _contador = 0
    private var _savedInfo = ""

    val id:Int
        get()=_id

    val contador:Int
        get()=_contador

    val savedInfo:String
        get()=_savedInfo

    fun updateId(id:Int){
        _id=id
    }
    fun updateContador(cont:Int){
        _contador=cont
    }
    fun updateInfo(info:String){
        _savedInfo=info
    }
}