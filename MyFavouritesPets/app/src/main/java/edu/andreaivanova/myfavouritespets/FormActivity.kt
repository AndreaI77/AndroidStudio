package edu.andreaivanova.myfavouritespets

import edu.andreaivanova.myfavouritespets.ListActivity
import edu.andreaivanova.myfavouritespets.R

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import edu.andreaivanova.myfavouritespets.databinding.ActivityFormBinding
import edu.andreaivanova.myfavouritespets.databinding.DialogLayoutBinding
import edu.andreaivanova.myfavouritespets.model.Clase
import edu.andreaivanova.myfavouritespets.model.FormViewModel
import edu.andreaivanova.myfavouritespets.model.Pelaje
import edu.andreaivanova.myfavouritespets.model.Pet
import edu.andreaivanova.myfavouritespets.utils.MyUtils
import java.io.File

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var  listaP:MutableList<Pelaje>
    private lateinit var  listaC:MutableList<Clase>
    private lateinit var myUtils:MyUtils
    private var photoFile: File? = null
    private var REQUEST_CODE=1234
    private lateinit var clase :Clase
    private lateinit var pelaje : Pelaje
    private lateinit var formViewModel : FormViewModel

    companion object{
        const val TAG_APP = "MyFavouritesPets"
        const val EXTRA_NAME = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        formViewModel = ViewModelProvider(this).get(FormViewModel::class.java)
        binding.tilNombre.setText(formViewModel.nombre)
        binding.txtLatName.setText(formViewModel.latName)
        binding.ratingBar.rating = formViewModel.rating

        if(formViewModel.clase != null){
            binding.tvClase.text= formViewModel.clase?.nombre
        }
        if(formViewModel.pelaje != null){
            binding.tvPelo.text= formViewModel.pelaje?.nombre
        }

//        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        // obtengo la rotación del teléfono
        val estado = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.display?.rotation
        }else {
            @Suppress("DEPRECATION")
            this.windowManager.defaultDisplay.rotation
        }
        //escondo  actionBar si tel está en la posición horizontal, ya que si no, no me entran los componentes
        if(estado == 1 || estado == 3){
            supportActionBar!!.hide()
        }
        myUtils= MyUtils()
        //obtengo las listas
        listaC=myUtils.getClases(this)
        listaP= myUtils.getPelaje(this)
        var resultadoActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Se recupera la información adicional.
            val data: Intent? = result.data

            if (result.resultCode == Activity.RESULT_OK) {
                val valor = data?.getIntExtra("claseId", 0)
                for (item in listaC) {
                    if (item.id == valor) {
                        clase = item
                        formViewModel.clase = item
                        binding.tvClase.text = item.nombre
                    }
                }
            }
        }
        binding.btnClase.setOnClickListener{

            val myIntent = Intent(this, ListActivity::class.java).apply {
                putExtra(EXTRA_NAME, "clase")
            }
          resultadoActivity.launch(myIntent)
        }
        var resultadoActivity2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Se recupera la información adicional.
            val data: Intent? = result.data

            if (result.resultCode == Activity.RESULT_OK) {
                val valor = data?.getIntExtra("pelajeId", 0)
                for (item in listaP) {
                    if (item.id == valor) {
                        pelaje = item
                        formViewModel.pelaje=item
                        binding.tvPelo.text = item.nombre
                    }
                }
            }
        }
        binding.btnPelaje.setOnClickListener {
            val myIntent = Intent(this, PelajeActivity::class.java).apply {
                putExtra(EXTRA_NAME, "pelaje")
            }
            resultadoActivity2.launch(myIntent)
        }
        var resultCaptura = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data

            if (result.resultCode == RESULT_OK) {
                val thumbnail: Bitmap = data?.getParcelableExtra("data")!!
                binding.imageView.setImageBitmap(thumbnail)
            }
        }
        binding.btnImagen.setOnClickListener() {
                // Se crea el fichero donde se guardará la imagen.
                photoFile = myUtils.createImageFile(this)
                val fileProvider =
                    FileProvider.getUriForFile( // En base al provider creado en el Manifest.
                        this,
                        "edu.andreaivanova.myfavoritespets",
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
                    }
                        //resultTakePicture.launch(intent)

               // val intent = Intent(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                if (intent.resolveActivity(packageManager) != null){
                    resultCaptura.launch(intent)
                    }
            }
        binding.btnGuardar.setOnClickListener() {
            var nombre=""
            var latNombre=""
            var rating=0.0f
            var image= "enlace"
            var result = true
            if (binding.tilNombre.text.isNullOrEmpty()) {
                binding.tilName.error = getString(R.string.requerido)
                result = false
            } else {
                nombre = binding.tilNombre.text.toString()
            }
            if (binding.txtLatName.text.isNullOrBlank()) {
                binding.tilLatName.error = getString(R.string.requerido)
                result = false
            } else {
                latNombre = binding.txtLatName.text.toString()
            }
            if (binding.ratingBar.rating == 0.0f ) {
                binding.tvNivelRating.text = getString(R.string.ratingError)
                binding.tvNivelRating.setTextColor(getResources().getColor(R.color.red))

                result = false
            } else {
                rating = binding.ratingBar.rating
                binding.tvNivelRating.text = getString(R.string.txt_rating)
                binding.tvNivelRating.setTextColor(getResources().getColor(R.color.black))
            }
            var fav=0
            if(result){
                var pet = Pet(0,nombre ,latNombre ,image, clase ,pelaje, rating, fav)
                if(myUtils.savePet(this,pet)){
                    Toast.makeText(
                        this,
                        "Registro insertado: ${ pet.nombre}",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    Toast.makeText(
                        this,
                        getString(R.string.no_insert),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }
        binding.ibAddClase.setOnClickListener {

            AlertDialog.Builder(this).apply {
                // Se infla el layout personalizado del diálogo.

                val bindingCustom = DialogLayoutBinding.inflate(layoutInflater)

                setView(bindingCustom.root)
                bindingCustom.tilRegistro.hint = getString(R.string.insertar_clase)
                setPositiveButton(android.R.string.ok) { _, _ ->
                    var name=bindingCustom.tvNuevoReg.text.toString()
                    var lastId= 0
                    if(listaC.size >0){
                        lastId=listaC.last().id
                    }
                    var textoClase =""
                    var clase = Clase(lastId+1,name)
                    formViewModel.clase=clase
                    listaC.add(clase)
                    myUtils.saveClase(this@FormActivity,clase)
                    textoClase = bindingCustom.tvNuevoReg.text.toString()
                    binding.tvClase!!.text=textoClase
                    Toast.makeText(
                        context,
                        "Registro insertado: ${bindingCustom.tvNuevoReg.text}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    Toast.makeText(
                        context,
                        getString(R.string.no_insert),
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                }
            }.show()
        }
        binding.ibAddPelaje.setOnClickListener {
            AlertDialog.Builder(this).apply {
                // Se infla el layout personalizado del diálogo.

                val bindingCustom = DialogLayoutBinding.inflate(layoutInflater)

                setView(bindingCustom.root)
                bindingCustom.tilRegistro.hint = getString(R.string.insertar_pelaje)
                setPositiveButton(android.R.string.ok) { _, _ ->
                    var name=bindingCustom.tvNuevoReg.text.toString()
                    var lastId=0
                    if(listaP.size>0){
                        listaP.last().id
                    }
                    var pelaje = Pelaje(lastId+1,name)
                    formViewModel.pelaje = pelaje
                    listaP.add(pelaje)
                    myUtils.savePelaje(this@FormActivity,pelaje)
                    var textoPelo = bindingCustom.tvNuevoReg.text.toString()
                    binding.tvPelo!!.text=textoPelo
                    Toast.makeText(
                        context,
                        "Registro insertado: ${bindingCustom.tvNuevoReg.text}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    Toast.makeText(
                        context,
                        getString(R.string.no_insert),
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                }
            }.show()
        }

    }


}