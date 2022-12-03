package edu.andreaivanova.ejercicio0401

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    private lateinit var nombre:EditText;
    private lateinit var apellidos: EditText;
    private lateinit var rdHombre: RadioButton
    private lateinit var rdMujer: RadioButton
    private lateinit var checkbox: CheckBox
    private lateinit var spinner: Spinner
    private lateinit var boton: Button
    private lateinit var textoMes: TextView

    val datos = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre");

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nombre = findViewById(R.id.et_nombre)
        apellidos = findViewById(R.id.et_apellidos)
        rdHombre = findViewById(R.id.rd_hombre)
        rdMujer = findViewById(R.id.rd_mujer)
        spinner = findViewById(R.id.spinner)
        boton = findViewById(R.id.button)
        checkbox = findViewById(R.id.checkBox)


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
                textoMes.setTextColor(Color.BLACK)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                textoMes.setTextColor(Color.RED)

            }
        }

        boton.setOnClickListener{
            var sexo = "";
            var carnet = "";
            if(nombre.text.isBlank()){
                nombre.error= "El nombre es obligatorio"
            }
            if(apellidos.text.isBlank()){
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
        }

    }
}