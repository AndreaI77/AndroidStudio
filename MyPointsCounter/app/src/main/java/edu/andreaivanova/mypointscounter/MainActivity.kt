package edu.andreaivanova.mypointscounter

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import edu.andreaivanova.mypointscounter.databinding.ActivityMainBinding
import edu.andreaivanova.mypointscounter.model.MyPoints
import edu.andreaivanova.mypointscounter.model.MainViewModel
import edu.andreaivanova.mypointscounter.utils.MyUtils
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var datos:MyPoints
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //obtengo las variables del viewModelProvider
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        var id= mainViewModel.id
        binding.tvSaved.text=mainViewModel.savedInfo
        var contador = mainViewModel.contador

        // obtengo la rotación del teléfono
        val estado = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.display?.rotation
        }else {
            @Suppress("DEPRECATION")
             this.windowManager.defaultDisplay.rotation
             }
        // Log.i("DISPLAY", estado.toString())

        //escondo  actionBar si tel está en la posición horizontal
        if(estado == 1 || estado == 3){
            supportActionBar!!.hide()
        }
        //creo un objeto de MyUtils y actualizo el marcador
        val myUtils = MyUtils()
        myUtils.updateMarcador(this, binding.marcador, contador)

        //al hacer click en botón reset borro el texto de tvSaved
        binding.btnReset.setOnClickListener {
            binding.tvSaved.text=""
            mainViewModel.updateInfo(binding.tvSaved.text.toString())
            if (contador == 0) {
                //si el contador ya está a 0, aviso mediante snackbar
                Snackbar.make(binding.root, getString(R.string.txt_info_reset), Snackbar.LENGTH_LONG).show()
            } else {
                //pongo contador a 0, actualizo viewModel y el marcador y hago vibrar el dispositivo
                contador = 0
                /*mainViewModel.contador = contador*/
                mainViewModel.updateContador(contador)
                myUtils.updateMarcador(this, binding.marcador, contador)
                myUtils.vibrate(this)
            }

        }
        //en el botón resta ofrezco la posibilidad de guardar la puntuación,
        // resto 1 punto al contador, actualizo el marcador y el viewModel

        binding.btnResta.setOnClickListener {
            binding.tvSaved.text=getString(R.string.txt_save)
            contador --
            myUtils.updateMarcador(this, binding.marcador, contador)
            // hago vibrar el dispositivo
            myUtils.vibrate(this)

            mainViewModel.updateContador(contador)
            mainViewModel.updateInfo(binding.tvSaved.text.toString())
        }
        //en el botón suma ofrezco la posibilidad de guardar la puntuación,
        // sumo 1 punto al contador, actualizo el marcador y el viewModel
        binding.btnSuma.setOnClickListener {
            binding.tvSaved.text=getString(R.string.txt_save)
            contador ++
            myUtils.updateMarcador(this, binding.marcador, contador)
            // hago vibrar el dispositivo
            myUtils.vibrate(this)

            mainViewModel.updateContador(contador)
            mainViewModel.updateInfo(binding.tvSaved.text.toString())
        }

        binding.ibSave.setOnClickListener {

            //obtengo las variables necesarias para crear un objerto MyPoints
            var points = Integer.parseInt(binding.marcador.text.toString())

            //ya que no me salía la hora correcta, establezco el TimeZone
            TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"))

            //obtengo fecha y hora en el formato requerido y creo el objeto
            var fecha= SimpleDateFormat("dd/MM/yyyy").format(Date())
            var hora = SimpleDateFormat("HH:mm:ss").format(Date())
            datos = MyPoints(id, points, fecha.toString(), hora.toString())
            id++
            //actualizo id en viewModel
            mainViewModel.updateId(id)

            //si la función de guardado me devuelve true, hago vibrar el dispositivo

            if(myUtils.savePoints(this, datos)){
                myUtils.vibrate(this)

                //cambio el texto de tvSaved y actualizo el viewModel
                binding.tvSaved.text = getString(R.string.txt_saved_info,("$fecha - $hora"))
                mainViewModel.updateInfo (binding.tvSaved.text.toString())

            }else{
                //si el guardado devuelve false, cambio el texto de tvSaved y actualizo el viewModel
                binding.tvSaved.text=resources.getString(R.string.error)
                mainViewModel.updateInfo(binding.tvSaved.text.toString())
            }
        }
    }
}