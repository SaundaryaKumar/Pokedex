package com.dromeus.delta.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class PokemonActivity extends AppCompatActivity {

    private ImageView pokemonImg;
    private TextView nameTextView;
    private TextView type1TextView;
    private TextView type2TextView;
    private String url;
    private RequestQueue requestQueue;
    private TextView numberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        url = getIntent().getStringExtra("url");
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        nameTextView = findViewById(R.id.pokemon_name);
        type1TextView = findViewById(R.id.pokemon_type_1);
        type2TextView = findViewById(R.id.pokemon_type_2);
        numberTextView = findViewById(R.id.pokemon_number);
        pokemonImg = findViewById(R.id.pokemon_img);
        loadPokemon();}
    public void loadPokemon (){
        type2TextView.setText("");
        type1TextView.setText("");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                        nameTextView.setText(response.getString("name"));
                        numberTextView.setText(String.format("#%03d",response.getInt("id")));
                        JSONArray typeEntries = response.getJSONArray("types");

                        for(int i=0; i<typeEntries.length(); i++){
                            JSONObject typeEntry = typeEntries.getJSONObject(i);
                            int slot = typeEntry.getInt("slot");
                            String type = typeEntry.getJSONObject("type").getString("name");

                            if(slot==1)
                                type1TextView.setText(type);
                            else
                                type2TextView.setText(type);
                        }

                }
                catch (JSONException e) {
                    Log.e("pokemon", "Pokemon Json error", e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            public void onErrorResponse (VolleyError error){
                Log.e("pokemon", "Pokemon details error");
            }
        });

        requestQueue.add(request);
    }
}
