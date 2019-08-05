package com.example.pokedex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.pokedex.models.ItemClickSupport;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.PokeapiResponse;
import com.example.pokedex.pokeapi.PokeapiService;

import com.example.pokedex.pokeapi.PokeapiService;

public class MainActivity extends AppCompatActivity {

    private String flag = "test tchoin";
    private String flag2 = "test poireau";

    private int offset;
    private boolean suitableForLoading;
    private static final String TAG = "POKEDEX";

    private Retrofit retrofit;

    private RecyclerView recyclerView;
    private PokemonAdapterList pokemonAdapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        pokemonAdapterList = new PokemonAdapterList(this);
        recyclerView.setAdapter(pokemonAdapterList);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (suitableForLoading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(TAG, "End.");

                            suitableForLoading = false;
                            offset += 40;
                            getData(offset);
                        }
                    }
                }
            }
        });


        retrofit = new Retrofit.Builder()
                .baseUrl("http://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        suitableForLoading = true;
        offset = 0;
        getData(offset);

        this.configureOnClickRecyclerView();
    }

    private void getData(int offset) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokeapiResponse> PokeapiResponseCall = service.getPokemonList(40, offset);

        PokeapiResponseCall.enqueue(new Callback<PokeapiResponse>() {
            @Override
            public void onResponse(Call<PokeapiResponse> call, Response<PokeapiResponse> response) {
                suitableForLoading = true;
                if (response.isSuccessful()) {

                    PokeapiResponse PokeapiResponse = response.body();
                    ArrayList<Pokemon> pokemonList = PokeapiResponse.getResults();

                    pokemonAdapterList.addPokemonList(pokemonList);

                } else {
                    Log.e(TAG, " onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokeapiResponse> call, Throwable t) {
                suitableForLoading = true;
                Log.e(TAG, " onFailure: " + t.getMessage());
            }
        });
    }


    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.activity_main)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Pokemon pokemon = pokemonAdapterList.getPokemon(position);
                        Toast.makeText(getBaseContext(), "You clicked on "+pokemon.getName(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "Position : "+position);
                        Intent details = new Intent(MainActivity.this, PokemonDetails.class);
                        startActivity(details);
                    }
                });
    }
}
