package edu.andreaivanova.ej0501


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.andreaivanova.ej0501.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

            binding.btn1.setOnClickListener {
                val miIntent = Intent(this, SegundaActivity::class.java)
                startActivity(miIntent)
            }
    }
}