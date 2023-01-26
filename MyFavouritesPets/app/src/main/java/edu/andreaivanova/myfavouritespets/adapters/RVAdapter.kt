package edu.andreaivanova.myfavouritespets.adapters

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.andreaivanova.myfavouritespets.R
import edu.andreaivanova.myfavouritespets.databinding.ItemPetBinding
import edu.andreaivanova.myfavouritespets.model.Pet

class RVAdapter (lista:MutableList<Pet>): RecyclerView.Adapter<RVAdapter.ViewHolder>() {
    var lista:MutableList<Pet> =ArrayList()
    //inicializo la lista
    init{
        this.lista = lista
    }
    //Se instancia la interface para el LongClick
    private lateinit var mLongClickListener : ItemLongClickListener

    //interface que debe implementar la clase que use el adaptador
    interface ItemLongClickListener{
        fun onItemLongClick(view: View, position:Int)
    }
    fun setLongClickListener(itemLongClickListener: ItemLongClickListener?) {
        mLongClickListener = itemLongClickListener !!
    }
    //inflo la vista de los items y devuelvo el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPetBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,false).root)
    }
    // método encargado de pasar los objetos al viewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= lista[position]
        holder.bind(item)
    }
    //devuelve la cantidad de los objetos en la lista
    override fun getItemCount(): Int {
        return lista.size
    }
    //la clase que rellena las vistas que se inflarán para los elementos de RecyclerView
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //creo el binding para la vista del item
        private val binding = ItemPetBinding.bind(itemView)

        //asigno los componentes a las variables
        val nombre: TextView = binding.tvName
        val latNom : TextView =binding.tvLatName
        val clase: TextView = binding.tvClass
        val pelo: TextView = binding.tvPelaje
        val enlace: TextView = binding.tvEnlace
        val stars: RatingBar = binding.rbRating
        val imagen: ImageView = binding.ivImagen
        val fav:ImageView=binding.imageButton

        //junto las variables con los datos del objeto de la lista
        fun bind(pet : Pet){
            nombre.text = pet.nombre.toString()
            latNom.text = pet.latName
            clase.text=pet.clase.nombre
            pelo.text= pet.pelo.nombre

            enlace.text = binding.root.resources.getString(R.string.enlace,pet.latName.replace(' ','_',true))
            stars.rating=pet.rating.toFloat()
           // imagen.setImageResource()
            if(pet.favorite == 0){
               // fav.imageTintMode= binding.root.resources.getColor(R.color.red)
                fav.setBackgroundColor(binding.root.resources.getColor(R.color.red))
            }else{
                fav.setBackgroundColor(binding.root.resources.getColor(R.color.grey))
            }
            fav.setOnClickListener(){
                if(pet.favorite ==0 ){
                    pet.favorite =1
                    fav.setBackgroundColor(binding.root.resources.getColor(R.color.red))
                }else{
                    pet.favorite = 0
                    fav.setBackgroundColor(binding.root.resources.getColor(R.color.grey))
                }
            }
            enlace.setOnClickListener{
                var texto=binding.root.resources.getString(R.string.enlace,pet.latName.replace(' ','_',true))
                    val miIntent = Intent( Intent.ACTION_VIEW, Uri.parse(texto))
            }

            //añado un listener a cada elemento
            itemView.setOnClickListener{
                Snackbar.make( itemView, binding.root.resources.getString(R.string.clase, pet.id, pet.nombre),
                    Snackbar.LENGTH_LONG).show()
            }
            //añado otro tipo de listener a cada elemento
            itemView.setOnLongClickListener{
                //binding.root.setBackgroundColor(Color.RED)
                mLongClickListener.onItemLongClick(it, adapterPosition)
                true
            }
        }
    }
}