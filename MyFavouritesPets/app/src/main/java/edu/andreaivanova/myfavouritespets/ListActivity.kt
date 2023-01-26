package edu.andreaivanova.myfavouritespets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.andreaivanova.myfavouritespets.FormActivity
import edu.andreaivanova.myfavouritespets.adapters.RVListAdapter
import edu.andreaivanova.myfavouritespets.databinding.ActivityFormBinding
import edu.andreaivanova.myfavouritespets.databinding.ActivityListBinding
import edu.andreaivanova.myfavouritespets.model.Clase
import edu.andreaivanova.myfavouritespets.model.Pelaje
import edu.andreaivanova.myfavouritespets.utils.MyUtils

class ListActivity : AppCompatActivity(), RVListAdapter.ItemLongClickListener {
    private lateinit var binding: ActivityListBinding
    private lateinit var  listaP:MutableList<Pelaje>
    private lateinit var  listaC:MutableList<Clase>
    private lateinit var myUtils: MyUtils
    private lateinit var myAdapter:RVListAdapter
    private lateinit var myRecycler: RecyclerView
    private lateinit var  lista:MutableList<Object>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myUtils=MyUtils()

        var nombre=intent.getStringExtra(FormActivity.EXTRA_NAME)
        if(nombre.equals("clase")){
            listaC= myUtils.getClases(this)
        }else if(nombre.equals("pelaje")){
            listaP = myUtils.getPelaje(this)
        }

        myAdapter = RVListAdapter(listaC)
        myAdapter.setLongClickListener(this)
        myRecycler = binding.recyclerView
        myRecycler.setHasFixedSize(true)
        myRecycler.layoutManager = LinearLayoutManager(this)
        myRecycler.adapter = myAdapter

        //En caso de que no existen los items en la lista, aviso con un textView
        if (myRecycler.adapter?.itemCount == 0) {
            binding.textView3.text = getString(R.string.noItems)
        } else {
            binding.textView3.text = ""
        }
    }

    //implemento el método de la interface, le paso el item y su posición en la lista
    override fun onItemLongClick(view: View, position: Int) {
        val myUtils = MyUtils()
        //obtengo el registro de la lista correspondiente a la posición
        val clase = listaC.get(position)

        //elimino el objeto de la lista, luego de la BD y actualizo el adapter
        lista.removeAt(position)
        val num = myUtils.deleteClase(this,clase.id)
        myAdapter.notifyItemRemoved(position)

        //si ya se han eliminado todos los Items de la vista, aviso con un textView
        if(lista.size == 0){
            binding.textView3.text = getString(R.string.noItems)
        } else {
            binding.textView3.text = ""
        }
        //si no se ha eliminado el objeto, aviso con un Snackbar
        if( num == 0){
            Snackbar.make( view, binding.root.resources.getString(R.string.txt_noDelete),
                Snackbar.LENGTH_LONG).show()
        }else{
            /*si se ha eliminado el registro, doy la opción de deshacer la eliminación
                en cual caso la inserto en la BD con su propio Id, lo añado a la lista
                y actualizo el adapter
             */
            Snackbar.make( binding.root, binding.root.resources.getString(R.string.borrado, clase.nombre),
                Snackbar.LENGTH_LONG).setAction(binding.root.resources.getString(R.string.deshacer)) {
                listaC.add(position,clase)
                myUtils.saveClase(this,clase)
                myAdapter.notifyItemInserted(position)
            }.show()
        }
    }
}