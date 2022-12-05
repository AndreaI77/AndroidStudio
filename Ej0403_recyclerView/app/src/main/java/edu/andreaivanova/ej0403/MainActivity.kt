package edu.andreaivanova.ej0403

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.andreaivanova.ej0403.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: AdapterLibro
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
    }
    private fun setUpRecyclerView() {
        // Esta opción a TRUE significa que el RV tendrá
        // hijos del mismo tamaño, optimiza su creación.
        binding.misLibros.setHasFixedSize(true)
        // Se indica el contexto para RV en forma de lista.
        binding.misLibros.layoutManager = LinearLayoutManager(this)
        // Se genera el adapter.
        myAdapter = AdapterLibro(getLibros())
         // Se asigna el adapter al RV.
        binding.misLibros.adapter = myAdapter
    }
    private fun getLibros(): MutableList<String>{
        var libros: MutableList<String> = arrayListOf()
        val datos = resources.getStringArray(R.array.libros)

        for(item in datos){
            libros.add(item)
            Toast.makeText(applicationContext, item, Toast.LENGTH_SHORT).show()
        }
        return libros
    }
}