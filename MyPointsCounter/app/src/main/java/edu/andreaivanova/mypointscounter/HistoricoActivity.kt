package edu.andreaivanova.mypointscounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.andreaivanova.mypointscounter.adapters.HistoricoRVAdapter
import edu.andreaivanova.mypointscounter.databinding.ActivityHistoricoBinding
import edu.andreaivanova.mypointscounter.model.MyPoints
import edu.andreaivanova.mypointscounter.utils.MyUtils

class HistoricoActivity : AppCompatActivity(),HistoricoRVAdapter.ItemLongClickListener {
    private lateinit var binding:ActivityHistoricoBinding
    private lateinit var myAdapter:HistoricoRVAdapter
    private lateinit var myRecycler: RecyclerView
    private lateinit var myUtils :MyUtils
    private lateinit var lista :MutableList<MyPoints>

    //inicializo las variables
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myUtils= MyUtils()
        //obtengo la lista
        lista= myUtils.getPoints(this)
        setUpRecyclerView()
    }
    //paso la lista al adapter y lo asigno al recyclerView
    private fun setUpRecyclerView() {
        myAdapter = HistoricoRVAdapter(lista)
        myAdapter.setLongClickListener(this)
        myRecycler = binding.recyclerView
        myRecycler.setHasFixedSize(true)
        myRecycler.layoutManager = LinearLayoutManager(this)
        myRecycler.adapter = myAdapter

        //En caso de que no existen los items en la lista, aviso con un textView
        if (myRecycler.adapter?.itemCount == 0) {
            binding.tvNoItem.text = getString(R.string.txt_sinItems)
        } else {
            binding.tvNoItem.text = ""
        }
    }
    //implemento el método de la interface, le paso el item y su posición en la lista
    override fun onItemLongClick(view: View, position: Int) {
        val myUtils = MyUtils()
        //obtengo el registro de la lista correspondiente a la posición
        val point = lista.get(position)

        //elimino el objeto de la lista, luego de la BD y actualizo el adapter
        lista.removeAt(position)
        val num = myUtils.deletePointDB(point,this)
        myAdapter.notifyItemRemoved(position)

        //si ya se han eliminado todos los Items de la vista, aviso con un textView
        if(lista.size == 0){
            binding.tvNoItem.text = getString(R.string.txt_sinItems)
        } else {
            binding.tvNoItem.text = ""
        }
        //si no se ha eliminado el objeto, aviso con un Snackbar
        if( num == 0){
            Snackbar.make( view, binding.root.resources.getString(R.string.txt_errorDelete),
                Snackbar.LENGTH_LONG).show()
        }else{
            /*si se ha eliminado el registro, doy la opción de deshacer la eliminación
                en cual caso la inserto en la BD con su propio Id, lo añado a la lista
                y actualizo el adapter
             */
            Snackbar.make( binding.root, binding.root.resources.getString(R.string.txt_borrado),
                Snackbar.LENGTH_LONG).setAction(binding.root.resources.getString(R.string.txt_deshacer)) {
                lista.add(position,point)
                myUtils.savePointsWithId(this,point)
                myAdapter.notifyItemInserted(position)
            }.show()
        }
    }

}