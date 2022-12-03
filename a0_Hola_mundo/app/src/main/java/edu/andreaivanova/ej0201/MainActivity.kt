package edu.andreaivanova.ej0201

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import edu.andreaivanova.ej0101.R

class MainActivity : AppCompatActivity() {

    private lateinit var tvSaludo: TextView
    private lateinit var etSaludo: EditText
    private lateinit var btnSaludar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // se inicializan las variables
        tvSaludo = findViewById(R.id.tv_saludo)
        etSaludo = findViewById(R.id.et_saludo)
        btnSaludar = findViewById(R.id.btn_saludar)

        btnSaludar.setOnClickListener { btn ->
            // si no se inserta el texto
            if (etSaludo.text.isBlank()) {
                etSaludo.error = resources.getString(R.string.texto_error) //este es el mensaje al lado del campo
                //crea un toast con el texto de error indicado esto es el mensaje abajo
                Toast.makeText(this, R.string.texto_error, Toast.LENGTH_SHORT).show()
                btn.setBackgroundColor(Color.RED)
            } else {
                //si se inserta texto, aparece en el textView
                etSaludo.error = null
                tvSaludo.text = resources.getString(R.string.texto_saludo, etSaludo.text.trim())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    btn.setBackgroundColor(this.getColor(R.color.purple_500))
                } else {
                    btn.setBackgroundColor(resources.getColor(R.color.purple_500))
                }
            }
        }
    }
}