package com.example.carte_97;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Vector;

public class FinDePartie extends AppCompatActivity {

    GestionBD instance;
    ListView listeScore;
    Vector<String> vector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_de_partie);

        vector = new Vector();
        instance = GestionBD.getInstance(this);
        instance.ouvrirBD();
        vector = instance.retournerScore();
        instance.fermerBD();
        listeScore = findViewById(R.id.listeScore);
        ArrayAdapter arrayAdapter =new ArrayAdapter(this, android.R.layout.simple_list_item_1, vector);
        listeScore.setAdapter(arrayAdapter);

    }
}