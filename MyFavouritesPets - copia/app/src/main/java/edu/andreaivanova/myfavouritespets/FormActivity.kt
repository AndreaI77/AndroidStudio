package edu.andreaivanova.myfavouritespets

import edu.andreaivanova.myfavouritespets.ListActivity
import edu.andreaivanova.myfavouritespets.R

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var listaP: MutableList<Pelaje>
    private lateinit var listaC: MutableList<Clase>
    private lateinit var lista: MutableList<Pet>
    private lateinit var myUtils: MyUtils
    private var photoFile: File? = null
    private lateinit var clase: Clase
    private lateinit var pelaje: Pelaje
    private lateinit var formViewModel: FormViewModel
    private var modificar = false
    private var fav = 0
    private var id: String? = null
    private var enlaceFoto = ""
    private var position = 0


    companion object {
        const val TAG_APP = "MyFavouritesPets"
        const val EXTRA_NAME = "id"
    }
    //resultado de obtener imagen de la cámara y lo cargo en el imageView
//    private var resultCaptura = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            result ->
//        val data: Intent? = result.data
//
//        if (result.resultCode == RESULT_OK) {
//            val thumbnail: Bitmap = data?.getParcelableExtra("data")!!
//            binding.imageView.setImageBitmap(thumbnail)
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //relleno los datos desde viewModel
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
        if(formViewModel.imagen != ""){
            enlaceFoto= formViewModel.imagen
            BitmapFactory.decodeFile(enlaceFoto)
            binding.imageView.setImageBitmap(BitmapFactory.decodeFile(enlaceFoto))
        }
        if(formViewModel.id != null){
            id = formViewModel.id
        }

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
        lista=myUtils.getPets(this)

        //esto es el id del item pasado por intent para modificar.
        // LLena los datos del formulario
        id=intent.getStringExtra(MainActivity.EXTRA_ID)
        if(id != null ){

            for (item in lista) {
                if (item.id == id!!.toInt()) {
                    clase = item.clase
                    pelaje = item.pelo
                    binding.tvClase.text = item.clase.nombre
                    binding.tvPelo.text = item.pelo.nombre
                    binding.ratingBar.rating = item.rating
                    binding.tilNombre.setText(item.nombre)
                    binding.txtLatName.setText(item.latName)
                    fav = item.favorite
                    enlaceFoto = item.image
                    binding.imageView.setImageBitmap(BitmapFactory.decodeFile(item.image))
                    modificar = true
                }
            }
        }

        //registro el resultado del intent lanzado por el botón de la clase
        var resultadoActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // Se recupera la información adicional.
                val data: Intent? = result.data

                if (result.resultCode == Activity.RESULT_OK) {
                    val valor = data?.getIntExtra("claseId", 0)
                    for (item in listaC) {
                        if (item.id == valor) {
                            clase = item
                            formViewModel.clase = item
                            binding.tvClase.text = item.nombre
                            binding.tvClase.setTextColor(getResources().getColor(R.color.black))
                        }
                    }
                }
            }
        // lanzo un intent para obtener clase
        binding.btnClase.setOnClickListener {
            
            val myIntent = Intent(this, ListActivity::class.java).apply {
                putExtra(EXTRA_NAME, "clase")
            }
            resultadoActivity.launch(myIntent)
        }
        //registro el resultado del intent lanzado por el botón del pelaje
        var resultadoActivity2 =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    // Se recupera la información adicional.
                    val data: Intent? = result.data

                    if (result.resultCode == Activity.RESULT_OK) {
                        val valor = data?.getIntExtra("pelajeId", 0)
                        for (item in listaP) {
                            if (item.id == valor) {
                                pelaje = item
                                formViewModel.pelaje=item
                                binding.tvPelo.text = item.nombre
                                binding.tvPelo.setTextColor(getResources().getColor(R.color.black))
                            }
                        }
                    }
                }
        //lanzo un intent para obtener pelaje
        binding.btnPelaje.setOnClickListener {
            val myIntent = Intent(this, PelajeActivity::class.java).apply {
                putExtra(EXTRA_NAME, "pelaje")
            }
            resultadoActivity2.launch(myIntent)
        }

        var resultTakePicture =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    enlaceFoto=Uri.fromFile(photoFile).toString()
                    formViewModel.imagen=enlaceFoto
                    val bMap = BitmapFactory.decodeFile(enlaceFoto)
                    if(bMap == null){
                        Toast.makeText(
                            this,
                            getString(R.string.no_foto),
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        binding.imageView.setImageBitmap(bMap)
                        photoFile = null
                    }
                }
            }

        binding.btnImagen.setOnClickListener() {
                // Se crea el fichero donde se guardará la imagen.

                  photoFile = myUtils.createImageFile(this)
                val fileProvider =
                    FileProvider.getUriForFile( // En base al provider creado en el Manifest.
                        this,
                        "edu.andreaivanova.myfavouritespets",
                        photoFile!!
                    )

                //Se crea el intent y se le pasa el contenedor del fichero a recuperar.
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(
                        MediaStore.EXTRA_OUTPUT, fileProvider
                    )
                }
            resultTakePicture.launch(intent)

                //intent para cargar foto desde la cámara en el imageView
//               val intent = Intent(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
//                if (intent.resolveActivity(packageManager) != null){
//                    resultCaptura.launch(intent)
//                }
            }

        binding.btnGuardar.setOnClickListener() {
            var nombre = ""
            var latNombre = ""
            var rating = 0.0f
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
            if (binding.tvClase.text.isNullOrBlank()){
                binding.tvClase.text = getString(R.string.btn_clase)
                binding.tvClase.setTextColor(getResources().getColor(R.color.red))
                result = false
            }
            if (binding.tvPelo.text.isNullOrBlank()){
                binding.tvPelo.text = getString(R.string.btn_pelo)
                binding.tvPelo.setTextColor(getResources().getColor(R.color.red))
                result = false
            }

            if (binding.ratingBar.rating == 0.0f ) {
                binding.tvNivelRating.text = getString(R.string.ratingError)
                binding.tvNivelRating.setTextColor(getResources().getColor(R.color.red))
                result = false
            } else {
                rating = binding.ratingBar.rating
                binding.tvNivelRating.text = getString(R.string.rating)
                binding.tvNivelRating.setTextColor(getResources().getColor(R.color.black))
            }

            if(result){
                if(modificar){

                    var valores = mutableMapOf<String,String>()
                    valores["nombre"]= nombre
                    valores["latNombre"] = latNombre
                    valores["enlace"] = enlaceFoto
                    valores["id_clase"]=clase.id.toString()
                    valores["id_pelaje"]=pelaje.id.toString()
                    valores["rating"]=rating.toString()
                    valores["favorite"]=fav.toString()
                    myUtils.updatePet(this,id!!.toInt(), valores)


                    val myIntent = Intent(this, MainActivity::class.java)
                    startActivity(myIntent)
                }else{
                    fav=0
                    var pet = Pet(0,nombre ,latNombre ,enlaceFoto, clase ,pelaje, rating, fav)
                    if(myUtils.savePet(this,pet)){
                        Toast.makeText(
                            this,
                            "Registro insertado: ${ pet.nombre}",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.tilNombre.setText("")
                        binding.txtLatName.setText("")
                        binding.tvClase.text=""
                        binding.tvPelo.text=""
                        binding.ratingBar.rating = 0.0f
                        binding.imageView.setImageBitmap(null)
                        enlaceFoto=""
                    }else{
                        Toast.makeText(
                            this,
                            getString(R.string.no_insert),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        binding.ibAddClase.setOnClickListener {
            //lanzo un alertDialog para añadir nuevo registro
            AlertDialog.Builder(this).apply {
                // Se infla el layout personalizado del diálogo.

                val bindingCustom = DialogLayoutBinding.inflate(layoutInflater)

                setView(bindingCustom.root)
                bindingCustom.tilRegistro.hint = getString(R.string.insertar_clase)
                setPositiveButton(android.R.string.ok) { _, _ ->
                    if(bindingCustom.tvNuevoReg.text.isNullOrBlank()){
                        bindingCustom.tvNuevoReg.error = getString(R.string.requerido)
                    }else{
                        var name=bindingCustom.tvNuevoReg.text.toString()

                        var lastId= 0
                        if(listaC.size >0){
                            lastId=listaC.last().id
                        }

                        clase = Clase(lastId+1,name)
                        formViewModel.clase=clase
                        listaC.add(clase)
                        myUtils.saveClase(this@FormActivity,clase)

                        binding.tvClase!!.text=name
                        binding.tvClase.setTextColor(getResources().getColor(R.color.black))
                        Toast.makeText(
                            context,
                            "Registro insertado: ${bindingCustom.tvNuevoReg.text}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

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
                    if(bindingCustom.tvNuevoReg.text.isNullOrBlank()){
                        bindingCustom.tvNuevoReg.error = getString(R.string.requerido)
                    }else{
                        var name=bindingCustom.tvNuevoReg.text.toString()
                        var lastId=0
                        if(listaP.size>0){
                            lastId=listaP.last().id
                        }
                        pelaje = Pelaje(lastId+1,name)
                        formViewModel.pelaje = pelaje
                        listaP.add(pelaje)
                        myUtils.savePelaje(this@FormActivity,pelaje)
                        binding.tvPelo!!.text=name
                        binding.tvPelo.setTextColor(getResources().getColor(R.color.black))
                        Toast.makeText(
                            context,
                            "Registro insertado: ${bindingCustom.tvNuevoReg.text}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
    private fun recuperarDatos(){
        //relleno los datos desde viewModel
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
        if(formViewModel.imagen != ""){
            enlaceFoto= formViewModel.imagen
            BitmapFactory.decodeFile(enlaceFoto)
            binding.imageView.setImageBitmap(BitmapFactory.decodeFile(enlaceFoto))
        }

    }

}