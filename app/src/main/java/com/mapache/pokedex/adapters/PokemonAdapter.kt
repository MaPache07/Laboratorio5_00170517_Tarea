package com.mapache.pokedex.adapters

import android.content.res.Configuration
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mapache.pokedex.R
import com.mapache.pokedex.pojos.Pokemon
import com.mapache.pokedex.utilities.AppConstants
import kotlinx.android.synthetic.main.list_item_pokemon.view.*

class PokemonAdapter(var pokemons: List<Pokemon>, val clickListener: (Pokemon) -> Unit) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>(), Filterable {

    lateinit var image : String
    var pokeList : ArrayList<Pokemon>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = pokeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(pokeList[position], clickListener)
    }

    init {
        pokeList = pokemons as ArrayList<Pokemon>
    }

    fun bindImage(url : String){
        var i = 2
        var num : String = ""
        do {
            num = url.substring(url.length-i, url.length-1)
            i++
        }while (url.substring(url.length-i, (url.length-i+1)) != "/")
        image = AppConstants.POKE_IMAGE_1 + num + ".png"
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: Pokemon, clickListener: (Pokemon) -> Unit) = with(itemView){
            bindImage(item.url)
            Glide.with(itemView.context).load(image).into(pokemon_image)
            if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                pokemon_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            }
            var name = item.name.substring(0, item.name.length)
            name = name.substring(0,1).toUpperCase() + name.substring(1)
            pokemon_name.text = name

            this.setOnClickListener{clickListener(item)}
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filteredList = ArrayList<Pokemon>()
                if(constraint == null || constraint.length == 0){
                    filteredList.addAll(pokemons)
                } else {
                    var filter = constraint.toString().toLowerCase().trim()
                    for(pokemon : Pokemon in pokemons){
                        if(pokemon.name.toLowerCase().startsWith(filter)){
                            filteredList.add(pokemon)
                        }
                    }
                }
                var results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                pokeList.clear()
                if (results != null) {
                    pokeList.addAll(results.values as ArrayList<Pokemon>)
                }
                notifyDataSetChanged()
            }

        }
    }
}