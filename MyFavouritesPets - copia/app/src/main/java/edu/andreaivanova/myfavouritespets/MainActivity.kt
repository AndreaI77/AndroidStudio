package edu.andreaivanova.myfavouritespets

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.andreaivanova.myfavouritespets.FormActivity
import edu.andreaivanova.myfavouritespets.adapters.RVAdapter
import edu.andreaivanova.myfavouritespets.databinding.ActivityMainBinding
import edu.andreaivanova.myfavouritespets.model.Pet
import edu.andreaivanova.myfavouritespets.utils.MyUtils


class MainActivity : AppCompatActivity(),RVAdapter.ItemLongClickListener, RVAdapter.ItemClickListener  {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: RVAdapter
    private lateinit var myRecycler: RecyclerView
    private lateinit var myUtils : MyUtils
    private lateinit var lista :MutableList<Pet>
    private var pos =0
    companion object{
        const val EXTRA_ID = "itemId"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myUtils= MyUtils()
        //obtengo la lista
       lista=myUtils.getPets(this)

        setUpRecyclerView()
        binding.fbtnAdd.setOnClickListener(){
            startActivity(
                Intent(this,
                    FormActivity::class.java).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            )
        }
    }
    private fun setUpRecyclerView() {
        myAdapter = RVAdapter(lista)
        myAdapter.setLongClickListener(this)
        myAdapter.setClickListener(this)
        myRecycler = binding.rvPets
        myRecycler.setHasFixedSize(true)
        myRecycler.layoutManager = LinearLayoutManager(this)
        myRecycler.adapter = myAdapter

        //En caso de que no existen los items en la lista, aviso con un textView
        if (myRecycler.adapter?.itemCount == 0) {
            binding.tvNoItem.text = getString(R.string.noItems)
        } else {
            binding.tvNoItem.text = ""
        }
    }

    override fun onItemClick(view:View, position:Int){
        pos=position
        var id = lista.get(position).id


        val myIntent = Intent(this, FormActivity::class.java).apply {
            // Se añade la información a pasar por clave-valor.
         putExtra( EXTRA_ID, id.toString())
        }
        // Se lanza la nueva activity con el Intent.
        startActivity(myIntent)
    }

    //implemento el método de la interface, le paso el item y su posición en la lista
    override fun onItemLongClick(view: View, position: Int) {
        //obtengo el registro de la lista correspondiente a la posición

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.att)
        builder.setMessage(R.string.txt_msg)
        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            //obtengo el registro de la lista correspondiente a la posición
            val pet = lista.get(position)

            //elimino el objeto de la lista, luego de la BD y actualizo el adapter
            lista.removeAt(position)
            val num = myUtils.delPet(this, pet.id)
            myAdapter.notifyItemRemoved(position)

            //si ya se han eliminado todos los Items de la vista, aviso con un textView
            if (lista.size == 0) {
                binding.tvNoItem.text = getString(R.string.noItems)
            } else {
                binding.tvNoItem.text = ""
            }
            //si no se ha eliminado el objeto, aviso con un Snackbar
            if (num == 0) {
                Snackbar.make(
                    view, binding.root.resources.getString(R.string.sinBorrado),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                /*si se ha eliminado el registro, doy la opción de deshacer la eliminación
                    en cual caso la inserto en la BD con su propio Id, lo añado a la lista
                    y actualizo el adapter
                 */
                Snackbar.make(
                    binding.root, binding.root.resources.getString(R.string.borrado, pet.nombre),
                    Snackbar.LENGTH_LONG
                ).setAction(binding.root.resources.getString(R.string.deshacer)) {
                    lista.add(position, pet)
                    myUtils.savePet(this, pet)
                    myAdapter.notifyItemInserted(position)
                }.show()
            }
        }
        builder.setNegativeButton("No", null)
        builder.show()

    }
    override fun onCreateOptionsMenu(menu: Menu?):Boolean{
        val inflate = menuInflater
        inflate.inflate(R.menu.menu,menu)
        return true
    }
   override fun onOptionsItemSelected(item: MenuItem):Boolean{
        return when (item.itemId) {
            R.id.mi_ordenar-> {
              lista.sortBy { it.nombre.toLowerCase().first() }
                myAdapter.notifyDataSetChanged()
                true
            }
            R.id.mi_amor->{
                lista.sortByDescending{ it.rating }
                myAdapter.notifyDataSetChanged()
                true
            }
            R.id.mi_fav ->{
                //lista.filter{it.favorite > 0} as MutableList<Pet>
                lista = myUtils.getFavPets(this)
                myAdapter.notifyDataSetChanged()
                true
            }
            R.id.mi_todos ->{
                lista= myUtils.getPets(this)
                myAdapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
       }
    }

}