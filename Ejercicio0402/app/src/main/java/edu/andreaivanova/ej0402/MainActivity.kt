package edu.andreaivanova.ej0402

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import edu.andreaivanova.ej0402.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var datos = arrayOf("uno","dos")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

       /* with(binding){
            button.setOnClickListener{

            }
        }*/
        val adaptador = ArrayAdapter(
            this,
            R.layout.listview_item,
            datos
        )
        binding.miLista.adapter= adaptador
        binding.miLista.onItemClickListener = object: AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(
                    applicationContext,
                    "${binding.miLista.getItemAtPosition(p2)}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}