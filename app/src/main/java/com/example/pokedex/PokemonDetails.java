package com.example.pokedex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PokemonDetails extends AppCompatActivity {

    private TextView pokemon_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        System.out.println(key);

        TextView textView = (TextView) findViewById(R.id.pokemon_name);
        textView.setText(key.toUpperCase());

    }
}
