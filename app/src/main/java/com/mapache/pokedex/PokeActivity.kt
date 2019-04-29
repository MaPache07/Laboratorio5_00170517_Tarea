package com.mapache.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mapache.pokedex.fragments.ContentFragment
import com.mapache.pokedex.pojos.Pokemon
import com.mapache.pokedex.utilities.AppConstants

class PokeActivity : AppCompatActivity() {

    private lateinit var mainFragment: ContentFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke)
        val pokemon: Pokemon = intent?.extras?.getParcelable(AppConstants.POKE_KEY) ?: Pokemon()
        mainFragment = ContentFragment.newInstance(pokemon)
        changeFragment(R.id.main_cont_fragment, mainFragment)
    }

    private fun changeFragment(id: Int, frag: Fragment){ supportFragmentManager.beginTransaction().replace(id, frag).commit() }
}
