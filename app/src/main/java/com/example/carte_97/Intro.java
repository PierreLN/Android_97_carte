package com.example.carte_97;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Intro extends AppCompatActivity {

    TextView meilleurScroreText;
    Button btnCommencer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        meilleurScroreText = findViewById(R.id.meilleurScroreText);
        meilleurScroreText.setText("0000"); // Changer le score avec bd ici


        btnCommencer = (Button) findViewById(R.id.btnCommencer);
        btnCommencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pageMain = new Intent(Intro.this, MainActivity.class);
                pageMain.putExtra("Fenêtre de jeu", "Jeux de cartes");
                startActivity(pageMain);
            }
        });
    }
}