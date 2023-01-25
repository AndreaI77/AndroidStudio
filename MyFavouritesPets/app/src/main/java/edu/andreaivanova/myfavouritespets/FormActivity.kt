package edu.andreaivanova.myfavouritespets

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import edu.andreaivanova.myfavouritespets.databinding.ActivityFormBinding
import edu.andreaivanova.myfavouritespets.model.Clase
import edu.andreaivanova.myfavouritespets.model.Pelaje
import edu.andreaivanova.myfavouritespets.utils.MyUtils

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var  listaP:MutableList<Pelaje>
    private lateinit var  listaC:MutableList<Clase>
    private lateinit var myUtils:MyUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
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
        myUtils= MyUtils()

        //obtengo las listas
        listaC=myUtils.getClases(this)

        val list= arrayOf("uno","dos","tres")
        val adapterC= ArrayAdapter(this,android.R.layout.simple_spinner_item,list)
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spClase.adapter=adapterC

        listaP=myUtils.getPelaje(this)
        val adapterP= ArrayAdapter(this,android.R.layout.simple_spinner_item,listaP)
        adapterP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spPelaje.adapter=adapterP

        with(binding){
            btnImagen.setOnClickListener(){
                val intent = Intent(Intent(MediaStore.ACTION_IMAGE_CAPTURE))

                if (intent.resolveActivity(packageManager) != null)
                    resultCaptura.launch(intent)
            }
            btnGuardar.setOnClickListener(){
                if(tilNombre.text.isNullOrEmpty()){
                    tilName.error = "Campo requerido"
                }
                if(txtLatName.text.isNullOrEmpty()){
                    tilLatName.error = "Campo requerido"
                }
            }
            ibAddClase.setOnClickListener{
                
            }
            ibAddPelaje.setOnClickListener{

            }

        }

    }

    private var resultCaptura = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data: Intent? = result.data

        if (result.resultCode == RESULT_OK) {
            val thumbnail: Bitmap = data?.getParcelableExtra("data")!!
            binding.imageView.setImageBitmap(thumbnail)
        }
    }
}