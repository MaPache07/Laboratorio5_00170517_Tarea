package com.mapache.pokedex

import android.content.Intent
import android.content.res.Configuration
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.mapache.pokedex.adapters.PokemonAdapter
import com.mapache.pokedex.fragments.ContentFragment
import com.mapache.pokedex.fragments.ListFragment
import com.mapache.pokedex.pojos.Pokemon
import com.mapache.pokedex.pojos.PokemonList
import com.mapache.pokedex.utilities.AppConstants
import com.mapache.pokedex.utilities.NetworkUtil

class MainActivity : AppCompatActivity(), ListFragment.SetOnClickPokeListener {

    private lateinit var contentFragment: ContentFragment
    private lateinit var listFragment: ListFragment
    private var pokeList = ArrayList<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FetchPokemon().execute(AppConstants.POKE_LINK)
    }

    fun initFragments(){
        listFragment = ListFragment.newInstance(pokeList)
        val resource = if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            R.id.main_fragment
        else {
            R.id.land_main_fragment
        }
        changeFragment(resource, listFragment)
    }

    private fun changeFragment(id: Int, frag: Fragment){ supportFragmentManager.beginTransaction().replace(id, frag).commit() }

    override fun portraitItemClick(pokemon: Pokemon) {
        var bundle = Bundle()
        bundle.putParcelable(AppConstants.POKE_KEY, pokemon)
        startActivity(Intent(this, PokeActivity::class.java).putExtras(bundle))
    }

    override fun landscapeItemClick(pokemon: Pokemon) {
        contentFragment = ContentFragment.newInstance(pokemon)
        changeFragment(R.id.land_main_cont_fragment,contentFragment)
    }

    inner class FetchPokemon : AsyncTask<String, Void, Void>(){

        override fun doInBackground(vararg params: String?): Void? {
            if(params.size == 0) return null
            var pokeApi = NetworkUtil().buildUrlSingle(params[0]!!)
            var pokeJson = NetworkUtil().getResponseFromHttpUrl(pokeApi)!!
            var listPoke = Gson().fromJson(pokeJson, PokemonList::class.java)
            pokeList = listPoke.results
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            initFragments()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.search_meno, menu)

        var searchItem = menu!!.findItem(R.id.action_search)
        var searchView : SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listFragment.pokemonAdapter.filter.filter(newText)
                return false
            }

        })
        return true
    }

}
