package edu.andreaivanova.myfavouritespets

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import edu.andreaivanova.myfavouritespets.databinding.ActivityFormBinding
import edu.andreaivanova.myfavouritespets.databinding.DialogLayoutBinding
import edu.andreaivanova.myfavouritespets.model.Clase
import edu.andreaivanova.myfavouritespets.model.Pelaje
import edu.andreaivanova.myfavouritespets.model.Pet
import edu.andreaivanova.myfavouritespets.utils.MyUtils
import java.io.File
import java.security.AccessController.getContext

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var  listaP:MutableList<Pelaje>
    private lateinit var  listaC:MutableList<Clase>
    private lateinit var myUtils:MyUtils
    private var photoFile: File? = null


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
        listaP= myUtils.getPelaje(this)

        binding.btnClase.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Elige clase")
            var list= arrayOf("uno","dos","tres")
            builder.setItems(list){id, posicion ->
                Toast.makeText(this)
            }


        }
            binding.btnImagen.setOnClickListener() {
                // Se crea el fichero donde se guardará la imagen.
                photoFile = myUtils.createImageFile(this)
                val fileProvider =
                    FileProvider.getUriForFile( // En base al provider creado en el Manifest.
                        this,
                        "edu.andreaivanova.captureandsave",
                        photoFile!!
                    )

                //Se crea el intent y se le pasa el contenedor del fichero a recuperar.
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(
                        MediaStore.EXTRA_OUTPUT, fileProvider
                    )
                }
                var resultTakePicture =
                    registerForActivityResult(ActivityResultContracts.StartActivityForResult())
                    { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            //binding.imageView.setImageResource(photoFile!!))
                            photoFile = null
                        }
                        //resultTakePicture.launch(intent)

//                val intent = Intent(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
//                if (intent.resolveActivity(packageManager) != null)
//                    resultCaptura.launch(intent)
                    }
            }
                binding.btnGuardar.setOnClickListener() {
                    var nombre=""
                    var latNombre=""
                    var rating=0.0f
                    if (binding.tilNombre.text.isNullOrEmpty()) {
                        binding.tilName.error = getString(R.string.requerido)
                    } else {
                        nombre = binding.tilNombre.text.toString()
                    }
                    if (binding.txtLatName.text.isNullOrBlank()) {
                        binding.tilLatName.error = getString(R.string.requerido)
                    } else {
                        latNombre = binding.txtLatName.text.toString()
                    }
                    if (binding.ratingBar.rating == 0.0f || binding.ratingBar.rating == null) {
                        binding.tvNivelRating.text = getString(R.string.ratingError)
                        binding.tvNivelRating.setTextColor(getResources().getColor(R.color.red))
                    } else {
                        rating = binding.ratingBar.rating
                    }
//                    var clase = binding.spClase.selectedItem as Clase
//                    var pelaje = binding.spPelaje.selectedItem as Pelaje
                    var fav=0
                    //var pet = Pet(0,nombre,latNombre,photoFile, clase ,pelaje,rating,fav)

                }
                binding.ibAddClase.setOnClickListener {
                    AlertDialog.Builder(this).apply {
                        // Se infla el layout personalizado del diálogo.
                        val bindingCustom = DialogLayoutBinding.inflate(layoutInflater)

                        setView(bindingCustom.root)

                        setPositiveButton(android.R.string.ok) { _, _ ->
                            var lastId= listaC.last().id
                            var clase = Clase(lastId+1,bindingCustom.tvNuevoReg.text.toString())
                            listaC.add(clase)
                            myUtils.saveClase(this@FormActivity,clase)
                            Toast.makeText(
                                context,
                                "Registro insertado: ${bindingCustom.tvNuevoReg.text}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        setNegativeButton(android.R.string.cancel) { dialog, _ ->
                            Toast.makeText(
                                context,
                                android.R.string.cancel,
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                        }
                    }.show()
                }
                binding.ibAddPelaje.setOnClickListener {

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