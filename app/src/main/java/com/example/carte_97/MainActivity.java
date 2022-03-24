package com.example.carte_97;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.os.IResultReceiver;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collection;
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
    Chronometer chronometer;
    TextView carteRestantText;
//    TextView tempsText;
    TextView scoreText;

    Deck deck;
    Vector<Carte> v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Les layouts principaux
        rootLayout = findViewById(R.id.rootLayout);
        boardGameLayout = findViewById(R.id.boardGameLayout);
        deckLayout = findViewById(R.id.deckLayout);

//      Les 4 piles de carte
        posUp1 = findViewById(R.id.posUp1);
        posUp2 = findViewById(R.id.posUp2);
        posDown1 = findViewById(R.id.posDown1);
        posDown2 = findViewById(R.id.posDown2);

//      Les informations sur le jeux
        carteRestantText = findViewById(R.id.carteRestantText);
        scoreText = findViewById(R.id.scoreText);

        carteRestantText.setText(String.valueOf(carteRestant));
        scoreText.setText(String.valueOf(score));

//      Le chronometre
        long elapsedRealtime = SystemClock.elapsedRealtime();
        chronometer = findViewById(R.id.chronometerID);
        chronometer.setBase(elapsedRealtime);
        chronometer.start();

        Ecouteur ec = new Ecouteur();

        posUp1.setOnDragListener(ec);
        posUp2.setOnDragListener(ec);
        posDown1.setOnDragListener(ec);
        posDown2.setOnDragListener(ec);

//      Generation d'un deck
        v = new Vector<Carte>();
        deck = new Deck(v);
        deck.creerDeck();

//      Assignation de l'ecouteur et generation des cartes
        for (int i = 0; i < deckLayout.getChildCount(); i++){
            LinearLayout enf = (LinearLayout)deckLayout.getChildAt(i);
            for (int j = 0; j < enf.getChildCount(); j++) {
                LinearLayout enf2 = (LinearLayout)enf.getChildAt(j);
                for (int k = 0; k < enf.getChildCount(); k++) {
                    LinearLayout enf3 = (LinearLayout)enf.getChildAt(k);
                    enf3.setOnDragListener(ec);

//                  installation des cartes du deck dans les textView
                    if (enf3.getChildAt(0) == null) {
                            TextView imageCarte = new TextView(MainActivity.this);
                            ViewGroup.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            imageCarte.setGravity(5);
                            imageCarte.setPadding(0,15,25,0);
                            imageCarte.setBackgroundResource(R.drawable.carte);
                            imageCarte.setCursorVisible(true);
                            imageCarte.setTextSize(25);

//                          Retirer la carte du deck
                            imageCarte.setText(String.valueOf(deck.getV().elementAt(0).getNumero()));
                            deck.getV().removeElementAt(0);
                            enf3.addView(imageCarte, layoutParam);
                        }
                    enf3.getChildAt(0).setOnTouchListener(ec);
                }
            }
        }
    }

    public class Ecouteur implements View.OnTouchListener, View.OnDragListener {

        Drawable normal = getResources().getDrawable(R.drawable.background_carte);
        Drawable select = getResources().getDrawable(R.drawable.background_carte_selection);

//      Installer les nouvelles cartes ici

//                    for (int i = 0; i < deckLayout.getChildCount(); i++) {
//                        LinearLayout enf = (LinearLayout) deckLayout.getChildAt(i);
//                        for (int j = 0; j < enf.getChildCount(); j++) {
//                            LinearLayout enf2 = (LinearLayout) enf.getChildAt(j);
//                            for (int k = 0; k < enf.getChildCount(); k++) {
//                                LinearLayout enf3 = (LinearLayout) enf.getChildAt(k);
//                                if (enf3.getChildAt(0) == null) {
//                                    TextView imageCarte = new TextView(MainActivity.this);
//                                    ViewGroup.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                                    imageCarte.setText("###");
//                                    imageCarte.setBackgroundColor(R.drawable.carte);
//                                    enf3.addView(imageCarte, layoutParam);
//                                }
//                            }
//                        }
//                    }

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

//              revoir le view en textView
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