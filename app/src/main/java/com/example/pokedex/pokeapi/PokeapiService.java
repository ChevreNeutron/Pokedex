package com.example.pokedex.pokeapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.pokedex.models.PokeapiResponse;

public interface PokeapiService {

    @GET("pokemon")
    Call<PokeapiResponse> getPokemonList(@Query("limit") int limit, @Query("offset") int offset);
}
