package com.mapache.pokedex.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapache.pokedex.R
import com.mapache.pokedex.adapters.PokemonAdapter
import com.mapache.pokedex.pojos.Pokemon
import com.mapache.pokedex.utilities.AppConstants
import kotlinx.android.synthetic.main.pokemon_list_fragment.view.*

class ListFragment : Fragment(){

    private lateinit var  pokemons : ArrayList<Pokemon>
    lateinit var pokemonAdapter: PokemonAdapter
    var click : SetOnClickPokeListener? = null

    companion object {
        fun newInstance(dataset : ArrayList<Pokemon>): ListFragment{
            val newFragment = ListFragment()
            newFragment.pokemons = dataset
            return newFragment
        }
    }

    interface SetOnClickPokeListener{
        fun portraitItemClick(pokemon: Pokemon)

        fun landscapeItemClick(pokemon: Pokemon)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.pokemon_list_fragment, container, false)

        if(savedInstanceState != null) pokemons = savedInstanceState.getParcelableArrayList<Pokemon>(AppConstants.MAIN_LIST_KEY)!!
        initRecyclerView(resources.configuration.orientation, view)
        return view
    }

    fun initRecyclerView(orientation : Int, container: View){
        val linearLayoutManager = LinearLayoutManager(this.context)
        if(orientation == Configuration.ORIENTATION_PORTRAIT) pokemonAdapter = PokemonAdapter(pokemons, {pokemon : Pokemon -> click?.portraitItemClick(pokemon)})
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) pokemonAdapter = PokemonAdapter(pokemons, {pokemon : Pokemon -> click?.landscapeItemClick(pokemon)})
        container.pokemon_list_rv.adapter = pokemonAdapter as PokemonAdapter
        container.pokemon_list_rv.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SetOnClickPokeListener) {
            click = context
        } else {
            throw RuntimeException("Se necesita una implementación de  la interfaz")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.MAIN_LIST_KEY, pokemons)
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        click = null
    }
}