package edu.andreaivanova.mypointscounter.utils

import android.content.Context
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import edu.andreaivanova.mypointscounter.R
import edu.andreaivanova.mypointscounter.model.MyPoints


class MyUtils() {
    fun vibrate(context:Context){
       var vibrator:Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibrator = vibratorManager.defaultVibrator
        } else {
            // backward compatibility for Android API < 31,
            // VibratorManager was only added on API level 31 release.

            vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        if (!vibrator.hasVibrator()) {
            Toast.makeText(context, "No tienes vibrador!!", Toast.LENGTH_SHORT).show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 val vibrationEffect =
                     VibrationEffect.createOneShot(
                             1000,
                 VibrationEffect.DEFAULT_AMPLITUDE)
                 vibrator.vibrate(vibrationEffect)
                 } else {
                 vibrator.vibrate(1000) // Deprecated API 26
                // Toast.makeText(context, "Deprecated version", Toast.LENGTH_SHORT).show()
                 }
             }
    }
    fun savePoints(context:Context, datos:MyPoints):Boolean{

        return true
    }
    fun updateMarcador(context:Context,marcador:TextView, contador:Int){
        marcador.text= contador.toString()
        if(contador <0){
            marcador.setTextColor(ContextCompat.getColor(context, R.color.reset))
        }else{
            marcador.setTextColor(ContextCompat.getColor(context, R.color.black))
        }


    }
}