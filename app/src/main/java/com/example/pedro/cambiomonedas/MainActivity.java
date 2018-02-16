package com.example.pedro.cambiomonedas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    TextView fechaActualizada,monedaElegida;
    EditText etACambiar,etcambio;
    ArrayList<String> claves;
    ArrayList<Double> valores;
    String monedaCambio;



    private static final String URL="https://openexchangerates.org/api/latest.json?app_id=3b104210d52d43bc95d0bf35a9a818c7";
    //private static final String URL="http://localhost/cambioMonedas.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INSTANCIO ELEMENTOS
        monedaElegida=findViewById(R.id.tvMonedaelegida);
        fechaActualizada=findViewById(R.id.tvFechaActualizada);
        etACambiar =findViewById(R.id.etACambiar);
        etcambio =findViewById(R.id.etcambio);


        //INSTANCIO ARRAYLIST
        claves=new ArrayList();
        valores=new ArrayList();

        //COLA DE PETICON HTTP
        RequestQueue request = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {

                try {
                    //RECUPERAMOS TODO EL JSON DE LA PAGINA
                    JSONObject jsonObjectPrincipal=new JSONObject(response.toString(0));
                    //FECHA ACTUALIZACION
                    Date date=new Date(Long.parseLong(jsonObjectPrincipal.getString("timestamp"))*1000);
                    fechaActualizada.setText("Last Update: "+ date);
                  //recuperamos un objeto complejo compuesto por el nombre de monedas y valores
                  JSONObject objeto= jsonObjectPrincipal.getJSONObject("rates");

                  //Recuperamos todas las claves del objeto Json
                 Iterator<String> clavesIt= objeto.keys();
                    //Recorro Iterador para sacar clave/valor
                 while(clavesIt.hasNext()) {
                     String clave=clavesIt.next();
                     Double valor=objeto.getDouble(clave);
                     System.out.println("--------------------------------------------------------------");
                     System.out.println("CLAVE:  "+clave);
                     System.out.println("VALOR:  "+valor);
                     //METEMOS CLAVES Y VALORES EN DIFERENTES ARRAYLIST PERO MISMO INDEX CLAVE VALOR
                    claves.add(clave);
                    valores.add(valor);
                 }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.add(jsonObjectRequest);


        try {
            //GESTION DE MONEDA ELEGIDA
            //NECESARIO EL TRY CATCH PARA QUE NO ROMPA LA PRIMERA VEZ QUE ENTRA LA APLICACION
            Bundle bundle = getIntent().getExtras();
            monedaCambio = bundle.getString("moneda");

        }catch (Exception e){
            e.printStackTrace();
            monedaCambio = "EUR";
        }


        monedaElegida.setText("From "+monedaCambio);

    }//fin onCreate

    public void  elegir(View view) {
        //LANZO OTRA ACTIVITI PARA ELEGIR MONEDA/CLAVE PASANDO ARRAYLIST DE CLAVES

        Intent e = new Intent(this, ElegirMoneda.class );
        e.putExtra("listaMonedas",claves);//enviamos arraylist a CambiarMoneda
        startActivity(e);



    }
    public void  cambiar(View view) {
        //REALIZO REGLA DE TRES PARA CALCULAR CANTIDAD A $$$$$$
        //monedaCambio es la clave elegida en el ListView. Primera vez €
        double valor=valores.get(claves.indexOf(monedaCambio));
        Double aCambiar=Double.valueOf(etACambiar.getText().toString());
        etcambio.setText(""+aCambiar/valor);


    }

}
