package com.example.carte_97;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    LinearLayout rootLayout;
    LinearLayout boardGameLayout;
    LinearLayout deckLayout;

    LinearLayout posUp1;
    LinearLayout posUp2;
    LinearLayout posDown1;
    LinearLayout posDown2;

    int carteRestant = 98;
    int score = 0;
    double temps = 0;

    TextView carteRestantText;
    TextView tempsText;
    TextView scoreText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.rootLayout);
        boardGameLayout = findViewById(R.id.boardGameLayout);
        deckLayout = findViewById(R.id.deckLayout);

        posUp1 = findViewById(R.id.posUp1);
        posUp2 = findViewById(R.id.posUp2);
        posDown1 = findViewById(R.id.posDown1);
        posDown2 = findViewById(R.id.posDown2);

        carteRestantText = findViewById(R.id.carteRestantText);
        tempsText = findViewById(R.id.tempsText);
        scoreText = findViewById(R.id.scoreText);

        carteRestantText.setText(String.valueOf(carteRestant));
        tempsText.setText(String.valueOf(temps));
        scoreText.setText(String.valueOf(score));


        Ecouteur ec = new Ecouteur();

        posUp1.setOnDragListener(ec);
        posUp2.setOnDragListener(ec);
        posDown1.setOnDragListener(ec);
        posDown2.setOnDragListener(ec);

        for (int i = 0; i < deckLayout.getChildCount(); i++){
            LinearLayout enf = (LinearLayout)deckLayout.getChildAt(i);
            for (int j = 0; j < enf.getChildCount(); j++) {
                LinearLayout enf2 = (LinearLayout)enf.getChildAt(j);
                for (int k = 0; k < enf.getChildCount(); k++) {
                    LinearLayout enf3 = (LinearLayout)enf.getChildAt(k);
                    enf3.setOnDragListener(ec);
                    enf3.getChildAt(0).setOnTouchListener(ec);
                }
            }
        }


    }

    public class Ecouteur implements View.OnTouchListener, View.OnDragListener {

        Drawable normal = getResources().getDrawable(R.drawable.background_carte);
        Drawable select = getResources().getDrawable(R.drawable.background_carte_selection);

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
//          Creation de l'ombre
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            int sdkVersion = Build.VERSION.SDK_INT;
            //Demarre le processus de drag&drop
            if (sdkVersion <= 24) {
                view.startDrag(null, shadowBuilder, view, 0); // deprecated API24
            } else {
                view.startDragAndDrop(null,shadowBuilder,view,0); // API@$++
            }
            // mettre la source invisible le temps du deplacement
            view.setVisibility(View.INVISIBLE);
            return true;
        }

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {

            View carte = null;

            switch (dragEvent.getAction()) {

                case DragEvent.ACTION_DRAG_ENTERED:
                    view.setBackground(select);
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    view.setBackground(normal);
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    view.setBackground(normal);
                    break;

                case DragEvent.ACTION_DROP:
                    carte = (View) dragEvent.getLocalState();
                    LinearLayout deckJeu = (LinearLayout) carte.getParent();
                    deckJeu.removeView(carte);
                    LinearLayout board = (LinearLayout) view;
                    board.addView(carte);
                    carte.setVisibility(View.VISIBLE);
                    break;
            default:
                break;
            }
            return true;
        }




    }

}