package edu.andreaivanova.myfavouritespets.utils

import android.content.Context
import edu.andreaivanova.myfavouritespets.adapters.DBAdapter
import edu.andreaivanova.myfavouritespets.model.Pet

class MyUtils {
    fun getPets(context: Context):MutableList<Pet>{
        var dbHelper= DBAdapter(context, null)
        return dbHelper.allPets()
    }
}