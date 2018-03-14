package com.example.bruno.debarrio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegistroActivity extends AppCompatActivity {

    TextView textview_regresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        textview_regresar = findViewById(R.id.textview_regresar);
        textview_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //vuelve al activity anterior
                //Intent intentRegistro = new Intent(RegistroActivity.this, LoginActivity.class);
                //RegistroActivity.this.startActivity(intentRegistro);
            }
        });
    }
}
