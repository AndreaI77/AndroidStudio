package edu.andreaivanova.mypointscounter.adapters


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.andreaivanova.mypointscounter.R
import edu.andreaivanova.mypointscounter.databinding.ItemMyPointsBinding
import edu.andreaivanova.mypointscounter.model.MyPoints

class HistoricoRVAdapter(lista:MutableList<MyPoints>):RecyclerView.Adapter<HistoricoRVAdapter.ViewHolder>(){
    var lista:MutableList<MyPoints> =ArrayList()
    //inicializo la lista
    init{
        this.lista = lista
    }
    //Se instancia la interface para el LongClick
     private lateinit var mLongClickListener : ItemLongClickListener

    //interface que debe implementar la clase que use el adaptador
    interface ItemLongClickListener{
        fun onItemLongClick(view:View, position:Int)
    }
    fun setLongClickListener(itemLongClickListener: ItemLongClickListener?) {
         mLongClickListener = itemLongClickListener !!
         }
    //inflo la vista de los items y devuelvo el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMyPointsBinding.inflate(LayoutInflater.from(parent.context)
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
        private val binding = ItemMyPointsBinding.bind(itemView)

        //asigno los componentes a las variables
        val id:TextView = binding.tvId
        val hora :TextView=binding.tvHora
        val fecha:TextView = binding.tvFecha
        val puntos:TextView = binding.tvPuntos

        //junto las variables con los datos del objeto de la lista
        fun bind(myPoint : MyPoints){
            puntos.text = myPoint.points.toString()
            fecha.text = myPoint.date
            hora.text=myPoint.hour
            id.text= myPoint.id.toString()

            //añado un listener a cada elemento
            itemView.setOnClickListener{
                Snackbar.make( itemView, binding.root.resources.getString(R.string.txt_puntuacion,myPoint.date, myPoint.hour, myPoint.points),
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