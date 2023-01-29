package edu.andreaivanova.myfavouritepets.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.andreaivanova.myfavouritepets.databinding.ListLayoutBinding

import edu.andreaivanova.myfavouritepets.model.Clase

class RVListAdapter(lista:MutableList<Clase>): RecyclerView.Adapter<RVListAdapter.ViewHolder>() {
    var lista:MutableList<Clase> =ArrayList()
    //inicializo la lista
    init{
        this.lista = lista
    }
    //Se instancia la interface para el LongClick
    private lateinit var mLongClickListener : ItemLongClickListener
    private lateinit var mClickListener : ItemClickListener

    //interface que debe implementar la clase que use el adaptador
    interface ItemLongClickListener{
        fun onItemLongClick(view: View, position:Int)
    }
    fun setLongClickListener(itemLongClickListener: ItemLongClickListener?) {
        mLongClickListener = itemLongClickListener !!
    }
    //interface que debe implementar la clase que use el adaptador
    interface ItemClickListener{
        fun onItemClick(view: View, position:Int)
    }
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener !!
    }

    //inflo la vista de los items y devuelvo el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListLayoutBinding.inflate(
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
        private val binding = ListLayoutBinding.bind(itemView)

        //asigno los componentes a las variables
        val nombre: TextView = binding.tvTexto

        //junto las variables con los datos del objeto de la lista
        fun bind(item : Clase){
            nombre.text = item.nombre.toString()

            //añado un listener a cada elemento
            itemView.setOnClickListener{
                mClickListener.onItemClick(it, adapterPosition)
                true
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