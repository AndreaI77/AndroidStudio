package edu.andreaivanova.myfavouritespets

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
    private var selectedPosition = -1


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

        // creo un Dialog  para obtener clase
        binding.btnClase.setOnClickListener {
            var result= false
            //si la lista está vacía, aviso con alertdialog
            if(listaC.size == 0){
                androidx.appcompat.app.AlertDialog.Builder(this).apply {
                    setMessage(R.string.noItems)
                    setPositiveButton(android.R.string.ok, null)
                }.show() // Se muestra el AlertDialog.
            }else {
                //creo un array de los nombres de las clases
                var listaN: MutableList<String> = ArrayList()
                for (item in listaC) {
                    listaN.add(item.nombre)
                }
                var miArray: Array<String> = listaN.toTypedArray()

                //creo el Alertdialog de selección simple
                AlertDialog.Builder(this).apply {
                    selectedPosition = -1
                    setTitle(R.string.btn_clase)
                    setSingleChoiceItems(miArray, -1) { _, which ->
                        selectedPosition = which
                    }
                    //al seleccionar ok, asigno la clase,
                    // actualizo el viewModel e inserto texto en los
                    setPositiveButton(android.R.string.ok) { dialog, _ ->
                        if (selectedPosition != -1) {
                            clase = listaC.get(selectedPosition)
                            formViewModel.clase = clase
                            binding.tvClase.text = clase.nombre
                            binding.tvClase.setTextColor(getResources().getColor(R.color.black))
                        }
                    }
                    // si decide eliminar, obtengo la clase de la lista y pido confirmación
                    setNegativeButton("eliminar") { dialog, _ ->

                        if (selectedPosition != -1) {
                            var cls = listaC.get(selectedPosition)
                            val builder = androidx.appcompat.app.AlertDialog.Builder(this.context)
                            builder.setTitle(R.string.att)
                            builder.setMessage(R.string.txt_msg)
                            builder.setPositiveButton("Sí") { dialog, which ->

                                // en caso de confirmación,
                                // compruebo si alguno de los pets contiene esta clase
                                for (item in lista) {
                                    if (item.clase.id == cls.id) {
                                        result = true
                                    }
                                }
                                //si no la contiene
                                if (!result) {
                                    //elimino el objeto de la lista, luego de la BD y actualizo el adapter
                                    listaC.removeAt(selectedPosition)
                                    val num = myUtils.delItem(this.context, cls.id, "clase")
                                    if (binding.tvClase.text.equals(cls.nombre)) {
                                        binding.tvClase.text = ""
                                    }
                                    //si ya se han eliminado todos los Items de la vista, aviso con un textView
                                    if (listaC.size == 0) {
                                        binding.tvClase.text = getString(R.string.noItems)
                                    } else {
                                        binding.tvClase.text = ""
                                    }
                                    //si no se ha eliminado el objeto, aviso con un toast
                                    if (num == 0) {
                                        Toast.makeText(
                                            this.context,
                                            binding.root.resources.getString(R.string.txt_noDelete),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    // si alguno de los pets contiene la clase, informo de la situación y no elimino el registro.
                                    androidx.appcompat.app.AlertDialog.Builder(this.context).apply {
                                        // Se asigna un título.
                                        setTitle(R.string.att)
                                        setMessage(R.string.txt_noDelete)
                                        setPositiveButton(android.R.string.ok, null)
                                    }.show() // Se muestra el AlertDialog.
                                }
                            }
                            builder.setNegativeButton("No", null)
                            builder.show()
                        }
                    }
                    //botón para cancelar
                    setNeutralButton(android.R.string.cancel, null)
                }.show()
            }
        }

        //botón para obtener el tipo de pelo
        binding.btnPelaje.setOnClickListener {
            var result= false
            //si la lista está vacía, aviso con un dialog
            if(listaP.size == 0){
                androidx.appcompat.app.AlertDialog.Builder(this).apply {
                    setMessage(R.string.noItems)
                    setPositiveButton(android.R.string.ok, null)
                }.show()
            }else {
                //creo un array con los nombres de pelaje
                var listaN: MutableList<String> = ArrayList()
                for (item in listaP) {
                    listaN.add(item.nombre)
                }
                var miArray: Array<String> = listaN.toTypedArray()
                //lanzo un AlertDialgo con la lista de opciones
                AlertDialog.Builder(this).apply {
                    selectedPosition = -1
                    setTitle(R.string.btn_pelo)
                    setSingleChoiceItems(miArray, -1) { _, which ->
                        selectedPosition = which
                    }
                    // en caso de ok asigno el pelaje, actualizo el viewModel y los textview
                    setPositiveButton(android.R.string.ok) { dialog, _ ->
                        if (selectedPosition != -1) {
                            pelaje = listaP.get(selectedPosition)
                            formViewModel.pelaje = pelaje
                            binding.tvPelo.text = pelaje.nombre
                            binding.tvPelo.setTextColor(getResources().getColor(R.color.grey))
                        }
                    }
                    //si decide eliminar, obtengo el pelaje y pido confirmación
                    setNegativeButton("eliminar") { dialog, _ ->
                        if (selectedPosition != -1) {
                            var cls = listaP.get(selectedPosition)
                            val builder = androidx.appcompat.app.AlertDialog.Builder(this.context)
                            builder.setTitle(R.string.att)
                            builder.setMessage(R.string.txt_msg)

                            //si confirma la eliminación compruebo si exite en la lista de los pets
                            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                                for (item in lista) {
                                    if (item.pelo.id == cls.id) {
                                        result = true
                                    }
                                }
                                //si no existe
                                if (!result) {
                                    //elimino el objeto de la lista, luego de la BD y actualizo el adapter
                                    listaP.removeAt(selectedPosition)
                                    val num = myUtils.delItem(this.context, cls.id, "pelaje")
                                    if (binding.tvPelo.text.equals(cls.nombre)) {
                                        binding.tvPelo.text = ""
                                    }
                                    //si no se ha eliminado el objeto, aviso con un toast
                                    if (num == 0) {
                                        Toast.makeText(
                                            this.context,
                                            binding.root.resources.getString(R.string.txt_noDelete),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    //aviso si no se puede eliminar el registro
                                    androidx.appcompat.app.AlertDialog.Builder(this.context).apply {
                                        // Se asigna un título.
                                        setTitle(R.string.att)
                                        // Se asigna el cuerpo del mensaje.
                                        setMessage(R.string.txt_noDelete)
                                        // Se define el comportamiento de los botones.
                                        setPositiveButton(android.R.string.ok, null)
                                    }.show() // Se muestra el AlertDialog.
                                }
                            }
                            builder.setNegativeButton("No", null)
                            builder.show()
                        }
                    }
                    setNeutralButton(android.R.string.cancel, null)
                }.show()
            }
        }

        //registro para el resultado de la camara
        var resultTakePicture =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    //obtengo el enlace, lo guardo en una variable y actualizo el viewModel
                    enlaceFoto=Uri.fromFile(photoFile).toString()
                    formViewModel.imagen=enlaceFoto
                    //obtengo el código de la imagen y si está vacío, aviso con un toast
                    val bMap = BitmapFactory.decodeFile(enlaceFoto)
                    if(bMap == null){
                        Toast.makeText(
                            this,
                            getString(R.string.no_foto),
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        //cargo la imagen en el imageview
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

            //lanzo el intent
            resultTakePicture.launch(intent)

                //intent para cargar foto desde la cámara en el imageView
//               val intent = Intent(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
//                if (intent.resolveActivity(packageManager) != null){
//                    resultCaptura.launch(intent)
//                }
            }

        binding.btnGuardar.setOnClickListener() {
            //compruebo todos los campos obligatorios
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
                //si es para modificar un registro existente, creo el map y lo actualizo
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

                    //vuelvo a la pantalla principal
                    val myIntent = Intent(this, MainActivity::class.java)
                    startActivity(myIntent)
                }else{
                    //si no es para modificar, creo pet y lo guardo en la BD
                    fav=0
                    var pet = Pet(0,nombre ,latNombre ,enlaceFoto, clase ,pelaje, rating, fav)
                    if(myUtils.savePet(this,pet)){
                        //confirmo la inserción y borro el formulario
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
                //establezco la vista y asigno el hint al campo
                setView(bindingCustom.root)
                bindingCustom.tilRegistro.hint = getString(R.string.insertar_clase)

                setPositiveButton(android.R.string.ok) { _, _ ->
                    //compruebo el campo
                    if(bindingCustom.tvNuevoReg.text.isNullOrBlank()){
                        bindingCustom.tvNuevoReg.error = getString(R.string.requerido)
                    }else{
                        //inserto el texto en el campo
                        var name=bindingCustom.tvNuevoReg.text.toString()

                        //busco el último id
                        var lastId= 0
                        if(listaC.size >0){
                            lastId=listaC.last().id
                        }
                        //creo nueva clase, la añado a la lista,
                        // actualizo viewModel e inserto la clase ne la BD
                        clase = Clase(lastId+1,name)
                        formViewModel.clase=clase
                        listaC.add(clase)
                        myUtils.saveClase(this@FormActivity,clase)

                        //inserto texto
                        binding.tvClase!!.text=name
                        binding.tvClase.setTextColor(getResources().getColor(R.color.grey))
                        Toast.makeText(
                            context,
                            "Registro insertado: ${bindingCustom.tvNuevoReg.text}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    //aviso de que no se ha insertado nada
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
            //lanzo un alert dialog para insertar el campo.
            AlertDialog.Builder(this).apply {
                // Se infla el layout personalizado del diálogo.

                val bindingCustom = DialogLayoutBinding.inflate(layoutInflater)

                setView(bindingCustom.root)
                bindingCustom.tilRegistro.hint = getString(R.string.insertar_pelaje)

                setPositiveButton(android.R.string.ok) { _, _ ->
                    //compruebo el campo
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
                    //aviso de que no se ha insertado nada
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