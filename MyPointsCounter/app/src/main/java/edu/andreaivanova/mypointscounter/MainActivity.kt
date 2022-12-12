package edu.andreaivanova.mypointscounter

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import edu.andreaivanova.mypointscounter.databinding.ActivityMainBinding
import edu.andreaivanova.mypointscounter.model.MyPoints
import edu.andreaivanova.mypointscounter.utils.MyUtils


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var datos:MyPoints

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val estado = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.display?.rotation
        }else {
            @Suppress("DEPRECATION")
             this.windowManager.defaultDisplay.rotation
             }
         Log.i("DISPLAY", estado.toString())
        if(estado == 1 || estado == 3){
            supportActionBar!!.hide()
        }
        var contador = 0
        val myUtils = MyUtils()
        myUtils.updateMarcador(this, binding.marcador, contador)

        binding.btnReset.setOnClickListener {
            if (contador == 0) {
                Snackbar.make(binding.root, getString(R.string.txt_info_reset), Snackbar.LENGTH_LONG).show()
            } else {
                contador = 0
                myUtils.updateMarcador(this, binding.marcador, contador)
                myUtils.vibrate(this)
            }
        }

        binding.btnResta.setOnClickListener {
            contador --
            myUtils.updateMarcador(this, binding.marcador, contador)
            myUtils.vibrate(this)
        }
        binding.btnSuma.setOnClickListener {
            contador ++
            myUtils.updateMarcador(this, binding.marcador, contador)
            myUtils.vibrate(this)
        }
    }
}