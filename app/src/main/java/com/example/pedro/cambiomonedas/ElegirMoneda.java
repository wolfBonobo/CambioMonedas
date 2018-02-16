package com.example.pedro.cambiomonedas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ElegirMoneda extends AppCompatActivity {

    ListView lvMonedas;
    ArrayList<String> monedas;
    ArrayAdapter<String> adapter;
    Intent e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_moneda);
        lvMonedas=findViewById(R.id.lvMonedas);
        monedas=new ArrayList();

        monedas=(ArrayList<String>)getIntent().getSerializableExtra("listaMonedas");

        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, monedas);
        lvMonedas.setAdapter(adapter);
        e = new Intent(this,MainActivity.class);

        lvMonedas.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView vistaAdaptador, View view, int  i, long k){
                //Envio a la nueva actividad  la url del Objeto Enum Pais seleccionado en la ListView

                e.putExtra("moneda",monedas.get(i));
                startActivity(e);
            }
        });

    }
}
