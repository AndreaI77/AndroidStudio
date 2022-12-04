package edu.andreaivanova.ejercicio0401

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    //private lateinit var binding: ActivityMainBinding
    private lateinit var nombre:EditText
    private lateinit var apellidos: EditText
    private lateinit var rdHombre: RadioButton
    private lateinit var rdMujer: RadioButton
    private lateinit var checkbox: CheckBox
    private lateinit var spinner: Spinner
    private lateinit var boton: Button


    val datos = arrayOf("Selecciona un elemento","Enero", "Febrero", "Marzo", "Abril", "Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       //binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        nombre = findViewById(R.id.et_nombre)
        apellidos = findViewById(R.id.et_apellidos)
        rdHombre = findViewById(R.id.rd_hombre)
        rdMujer = findViewById(R.id.rd_mujer)
        spinner = findViewById(R.id.spinner)
        boton = findViewById(R.id.button)
        checkbox = findViewById(R.id.checkBox)

    //no tengo nada claro donde tengo que poner las siguientes líneas para quitar el teclado:
      //  val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
       // imm.hideSoftInputFromWindow(currentFocus!!.windowToken,0)

        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item,datos)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador
        var mes =""
        spinner.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {
                mes = spinner.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //eso no funciona
               Toast.makeText( applicationContext,"Selecciona el mes de nacimiento", Toast.LENGTH_SHORT).show()

            }
        }

        boton.setOnClickListener{
            var sexo = ""
            var carnet = ""
            if(nombre.text.isEmpty()){
                nombre.error= "El nombre es obligatorio"
            }
            if(apellidos.text.isEmpty()){
                apellidos.error= "Los apellidos son obligatorios"
            }
            if(rdHombre.isChecked ){
                sexo = "Hombre"
            }else if(rdMujer.isChecked){
                sexo = "Mujer"
            }
            if(checkbox.isChecked){
                carnet = "tiene carnet de conducir"
            }else{
                carnet = "no tiene carnet de conducir"
            }
            //compruebo los campos
            if(sexo.equals("") || mes.equals("Selecciona un elemento") || mes.equals("") || nombre.text.isEmpty() || apellidos.text.isEmpty()){
                Toast.makeText( applicationContext,"Rellena los campos requeridos", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText( applicationContext,"Nombre: ${nombre.text}, Apellidos: ${apellidos.text},\nSexo: ${sexo}, ${carnet}, \nMes de nacimeinto: ${mes}", Toast.LENGTH_LONG).show()
                //no me reconoce el binding
                /*Snackbar.make(
                   //binding.root,

                    "Nombre: ${nombre.text}, Apellidos: ${apellidos.text},\nSexo: ${sexo}, ${carnet}, \nMes de nacimeinto: ${mes}",
                    Snackbar.LENGTH_LONG
                ).show()*/
            }


        }


    }
}