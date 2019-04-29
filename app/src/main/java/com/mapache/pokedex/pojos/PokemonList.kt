package com.mapache.pokedex.pojos

data class PokemonList(
    val count: Int = 0,
    val next: String = "",
    val previous: String = "",
    val results: ArrayList<Pokemon>
)