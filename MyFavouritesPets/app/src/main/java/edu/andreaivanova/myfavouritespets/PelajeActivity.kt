package edu.andreaivanova.myfavouritespets

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.andreaivanova.myfavouritespets.adapters.RVListPAdapter
import edu.andreaivanova.myfavouritespets.databinding.ActivityPelajeBinding
import edu.andreaivanova.myfavouritespets.model.Pelaje
import edu.andreaivanova.myfavouritespets.utils.MyUtils

class PelajeActivity : AppCompatActivity(), RVListPAdapter.ItemLongClickListener, RVListPAdapter.ItemClickListener {

    private lateinit var binding: ActivityPelajeBinding
    private lateinit var lista:MutableList<Pelaje>
    private lateinit var myUtils: MyUtils
    private lateinit var myAdapter:RVListPAdapter
    private lateinit var myRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelaje)
        binding = ActivityPelajeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myUtils= MyUtils()

        var nombre=intent.getStringExtra(FormActivity.EXTRA_NAME)
        if(nombre.equals("clase")){

        }else if(nombre.equals("pelaje")){
        }

        lista= myUtils.getPelaje(this)
        myAdapter = RVListPAdapter(lista)
        myAdapter.setLongClickListener(this)
        myAdapter.setClickListener(this)
        myRecycler = binding.recyclerView3
        myRecycler.setHasFixedSize(true)
        myRecycler.layoutManager = LinearLayoutManager(this)
        myRecycler.adapter = myAdapter

        //En caso de que no existen los items en la lista, aviso con un textView
        if (myRecycler.adapter?.itemCount == 0) {
            binding.textView.text = getString(R.string.noItems)
        } else {
            binding.textView.text = ""
        }
    }
    override fun onItemClick(view: View, position:Int){
        val pelaje = lista.get(position)
        val intentResult: Intent = Intent().apply {
            // Se añade el id del pelaje.
            putExtra("pelajeId", pelaje.id)
        }
        setResult(Activity.RESULT_OK,intentResult)
        finish()
    }
    //implemento el método de la interface, le paso el item y su posición en la lista
    override fun onItemLongClick(view: View, position: Int) {

        //obtengo el registro de la lista correspondiente a la posición
        val pel = lista.get(position)
        if(binding.textView.text.equals(pel.nombre)){
            binding.textView.text = ""
        }
        //elimino el objeto de la lista, luego de la BD y actualizo el adapter
        lista.removeAt(position)
        val num = myUtils.deletePelaje(this,pel.id)

        myAdapter.notifyItemRemoved(position)

        //si ya se han eliminado todos los Items de la vista, aviso con un textView
        if(lista.size == 0){
            binding.textView.text = getString(R.string.noItems)
        } else {
            binding.textView.text = ""
        }
        //si no se ha eliminado el objeto, aviso con un Snackbar
        if( num == 0){
            Snackbar.make( view, binding.root.resources.getString(R.string.txt_noDelete),
                Snackbar.LENGTH_LONG).show()
        }
    }
}