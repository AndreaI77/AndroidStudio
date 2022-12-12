package edu.andreaivanova.ej0502

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.andreaivanova.ej0502.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
           if(binding.myInput.text!!.isBlank()){
                //binding.myInput.error = "Error!!!" //error de editText
               binding.myInputLabel.error = "Error!!" //error de label- lo  pone rojo.
           }

        }

    }
}