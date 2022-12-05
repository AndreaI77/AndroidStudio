package edu.andreaivanova.ej0403

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.andreaivanova.ej0403.databinding.RvItemBinding


class AdapterLibro(listaLibros: MutableList<String>) : RecyclerView.Adapter<AdapterLibro.ViewHolder>(){
   private var libros: MutableList<String> = ArrayList()
   init {
       this.libros = listaLibros
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            RvItemBinding.inflate(layoutInflater, parent, false).root
        )
    }
    override fun onBindViewHolder(holder:ViewHolder, position:Int) {
        val item = libros.get(position)
        holder.bind(item)
    }

    override fun getItemCount():Int{
            return libros.size
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Se usa View Binding para localizar los elementos en la vista.
        private val binding = RvItemBinding.bind(view)

        fun bind(libro: String) {
            binding.textView.text = libro

            itemView.setOnClickListener {
                Snackbar.make(binding.root, libro, Snackbar.LENGTH_SHORT).show()

                }
             }
    }
}