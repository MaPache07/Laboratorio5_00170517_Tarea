package com.mapache.pokedex.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.mapache.pokedex.R
import com.mapache.pokedex.pojos.Pokemon
import com.mapache.pokedex.utilities.AppConstants
import com.mapache.pokedex.utilities.NetworkUtil
import kotlinx.android.synthetic.main.cont_pokemon.*
import kotlinx.android.synthetic.main.cont_pokemon.view.*

class ContentFragment : Fragment() {

    var poke = Pokemon()
    lateinit var viewF : View
    var idP = 0
    var name = ""
    var type = ""
    var weight = ""
    var height = ""
    var base_experience = ""
    var image = ArrayList<String>()
    var index = 0

    companion object {
        fun newInstance(poke: Pokemon): ContentFragment{
            val newFragment = ContentFragment()
            newFragment.poke = poke
            return newFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.cont_pokemon, container, false)
        viewF = view
        if(savedInstanceState != null){
            poke = savedInstanceState?.getParcelable(AppConstants.MAIN_CONTENT_KEY)!!
        }
        FetchInfoPoke().execute()
        return view
    }

    inner class FetchInfoPoke : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            var pokeLink = NetworkUtil().buildUrlSingle(poke.url)
            var pokeJson = NetworkUtil().getResponseFromHttpUrl(pokeLink)!!
            pokeJson = "[" + pokeJson + "]"
            var pokeArray = JsonParser().parse(pokeJson).asJsonArray
            for(pokeElement : JsonElement in pokeArray){
                var pokeObject = pokeElement.asJsonObject
                name = pokeObject.get("name").asString.substring(0,1).toUpperCase()+pokeObject.get("name").asString.substring(1)
                if(name.length > 12){
                    var i = 0
                    while(i < name.length){
                        if(name.get(i) == '-'){
                            name = name.substring(0, name.indexOf(name.get(i))+1) + '\n' + name.substring(name.indexOf(name.get(i))+1)
                            break
                        }
                        i++
                    }
                }
                idP = pokeObject.get("id").asInt
                var types = ""
                for(typeElement : JsonElement in pokeObject.getAsJsonArray("types")){
                    if (types != ""){
                        types = types + " / "
                    }
                    var ty : String = typeElement.asJsonObject.get("type").asJsonObject.get("name").asString
                    types = types + ty.substring(0,1).toUpperCase()+ty.substring(1)
                }
                type = types
                height = (pokeObject.get("height").asDouble/10).toString()
                weight = (pokeObject.get("weight").asDouble/10).toString()
                base_experience = (pokeObject.get("base_experience").asInt).toString()
                image.add(AppConstants.POKE_IMAGE_1 + idP.toString() + ".png")
                if(!pokeObject.get("sprites").asJsonObject.get("front_female").isJsonNull) image.add(pokeObject.get("sprites").asJsonObject.get("front_female").asString)
                if(!pokeObject.get("sprites").asJsonObject.get("front_shiny").isJsonNull) image.add(pokeObject.get("sprites").asJsonObject.get("front_shiny").asString)
                if(!pokeObject.get("sprites").asJsonObject.get("front_shiny_female").isJsonNull) image.add(pokeObject.get("sprites").asJsonObject.get("front_shiny_female").asString)
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            bindData(viewF)
        }

    }

    fun bindData(view : View){
        view.namePoke.text = name
        view.idPoke.text = idP.toString()
        view.typePoke.text = type
        view.heightPoke.text = height
        view.weightPoke.text = height
        view.baseExperiencePoke.text = base_experience
        Glide.with(view).load(image.get(0)).into(imagePoke)
        imagePoke.setOnClickListener {
            index++
            if(index == image.size){
                index = 0
            }
            Glide.with(view).load(image.get(index)).into(imagePoke)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(AppConstants.MAIN_CONTENT_KEY, poke)
        super.onSaveInstanceState(outState)
    }
}