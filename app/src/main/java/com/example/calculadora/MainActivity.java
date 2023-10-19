package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUF;
    private Button btnGuardar;
    private ListView listView;
    private ArrayList<Double> ufValues;
    private ArrayAdapter<Double> adapter;
    private RequestQueue requestQueue;

    private TextView fechita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUF = findViewById(R.id.editTextUF);
        btnGuardar = findViewById(R.id.btnGuardar);
        listView = findViewById(R.id.listView);
        ufValues = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ufValues);
        listView.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(this);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerValorUF();
            }
        });

        // Configurar la eliminación de registros cuando se hace clic en ellos
        listView.setOnItemClickListener((parent, view, position, id) -> {
            ufValues.remove(position);
            adapter.notifyDataSetChanged();
        });
        Date fechaa = new Date();
        //fecha completa
        fechita = (TextView) findViewById(R.id.fechita);
        SimpleDateFormat fechitac = new SimpleDateFormat("d,MMM 'del', yyy");
        String sfecha = fechitac.format(fechaa);
        fechita.setText(sfecha);
    }

    private void obtenerValorUF() {
        String url = "https://mindicador.cl/api/uf";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        double ufValue = response.getDouble("valor");
                        // Agrega el valor UF a la lista de datos
                        ufValues.add(ufValue);
                        adapter.notifyDataSetChanged();
                        String resultado = String.format(Locale.getDefault(), "%.2f CLP", ufValue);
                        editTextUF.setText(resultado);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> editTextUF.setText("Error al obtener valor UF"));

        // Agrega la solicitud a la cola de solicitudes
        requestQueue.add(request);
    }

}