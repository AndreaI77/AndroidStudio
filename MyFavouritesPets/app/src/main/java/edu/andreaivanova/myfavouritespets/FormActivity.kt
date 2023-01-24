package edu.andreaivanova.myfavouritespets

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import edu.andreaivanova.myfavouritespets.databinding.ActivityFormBinding

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnImagen.setOnClickListener(){
            val intent = Intent(Intent(MediaStore.ACTION_IMAGE_CAPTURE))

            if (intent.resolveActivity(packageManager) != null)
                resultCaptura.launch(intent)
        }
    }

    private var resultCaptura = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data: Intent? = result.data

        if (result.resultCode == RESULT_OK) {
            val thumbnail: Bitmap = data?.getParcelableExtra("data")!!
            binding.imageView.setImageBitmap(thumbnail)
        }
    }
}