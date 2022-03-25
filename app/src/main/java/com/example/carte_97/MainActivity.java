package com.example.carte_97;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.os.IResultReceiver;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    long elapsedRealtime;
    long bonusTemps;
    TextView carteRestantText;
    TextView scoreText;

    Deck deck;
    Vector<Carte> v;

    Pile pileUp1, pileUp2, piledown1, piledown2;

    Button buttonFin;

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
        elapsedRealtime = SystemClock.elapsedRealtime();
        chronometer = findViewById(R.id.chronometerID);
        chronometer.setBase(elapsedRealtime);
        chronometer.start();

        Ecouteur ec = new Ecouteur();

//      Position des emplacement du terrain de jeu
        posUp1.setOnDragListener(ec);
        posUp2.setOnDragListener(ec);
        posDown1.setOnDragListener(ec);
        posDown2.setOnDragListener(ec);

//      Generation d'un deck
        v = new Vector<Carte>();
        deck = new Deck(v);
        deck.creerDeck();
        TextView imageCarte;

//      Generation des 4 piles du jeu
        pileUp1 = new Pile(1);
        pileUp2 = new Pile(1);
        piledown1 = new Pile(99);
        piledown2 = new Pile(99);

        buttonFin = findViewById(R.id.buttonFin);

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
                            imageCarte = new TextView(MainActivity.this);
                            ViewGroup.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            configurationImageCarte(imageCarte);
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

        String numeroCarte = "";


        Drawable normal = getResources().getDrawable(R.drawable.background_carte);
        Drawable select = getResources().getDrawable(R.drawable.background_carte_selection);

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            verificationVersion(view,shadowBuilder).setVisibility(View.INVISIBLE);
            return true;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            TextView carte = null;
            TextView carteJeu = null;
            int compteurEmplacementVide = 0;
            LinearLayout enf, enf2, enf3;
            int numeroCarteChoisi = 0;
            int numeroCarteDuJeu = 0;
            carte = (TextView) dragEvent.getLocalState();

            switch (dragEvent.getAction()) {

                case DragEvent.ACTION_DRAG_ENTERED:
                    view.setBackground(select);
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    view.setBackground(normal);
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    view.setBackground(normal);
                    carte.setVisibility(View.VISIBLE);
                    carteRestantText.setText(String.valueOf(carteRestant));

//                  degenerescence des point bonus sur une periode de 10 mins. Aucun point bonus apres 10 mins
                    if (bonusTemps >= 0) {
                        bonusTemps = 600 - (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
                    } else {
                        bonusTemps = 0;
                    }
                    scoreText.setText(String.valueOf(score));

                    break;

                case DragEvent.ACTION_DROP:
                    LinearLayout deckJeu = (LinearLayout) carte.getParent();
//                  Va Prendre en note la valeur de la carte a jouer
                        numeroCarteChoisi = Integer.parseInt(carte.getText().toString());

                        LinearLayout board = (LinearLayout) view;

//                  Restriction pour placer les bonne cartes sur le jeu
//                  Une fois mis sur le jeu,
//                  Enlever l'ecouteur et enregistre la valeur de la carte pour la pile
                    if (board == posDown1) {
                            if (numeroCarteChoisi < piledown1.getNumeroCarte() || numeroCarteChoisi == piledown1.getNumeroCarte() + 10) {
                                deckJeu.removeView(carte);
                                board.removeAllViewsInLayout();
                                board.addView(carte);
                                carte.setVisibility(TextView.VISIBLE);
                                piledown1.setNumeroCarte(numeroCarteChoisi);
                                carte.setOnTouchListener(null);
                                carteRestant--;
                                if (numeroCarteChoisi == piledown1.getNumeroCarte() + 10) {
                                    score += 2400 + bonusTemps;
                                }
                                else {
                                    score += 800 + bonusTemps;
                                }
                            }
                        }
                        else if (board == posDown2) {
                            if (numeroCarteChoisi < piledown2.getNumeroCarte() || numeroCarteChoisi == piledown2.getNumeroCarte() + 10){
                                deckJeu.removeView(carte);
                                board.removeAllViewsInLayout();
                                board.addView(carte);
                                carte.setVisibility(TextView.VISIBLE);
                                piledown2.setNumeroCarte(numeroCarteChoisi);
                                carte.setOnTouchListener(null);
                                carteRestant--;
                                if (numeroCarteChoisi == piledown1.getNumeroCarte() + 10) {
                                    score += 2400 + bonusTemps;
                                }
                                else {
                                    score += 800 + bonusTemps;
                                }
                            }
                        }
                        else if (board == posUp1) {
                            if (numeroCarteChoisi > pileUp1.getNumeroCarte() || numeroCarteChoisi == pileUp1.getNumeroCarte() - 10){
                                deckJeu.removeView(carte);
                                board.removeAllViewsInLayout();
                                board.addView(carte);
                                carte.setVisibility(TextView.VISIBLE);
                                pileUp1.setNumeroCarte(numeroCarteChoisi);
                                carte.setOnTouchListener(null);
                                carteRestant--;
                                if (numeroCarteChoisi == piledown1.getNumeroCarte() + 10) {
                                    score += 2400 + bonusTemps;
                                }
                                else {
                                    score += 800 + bonusTemps;
                                }
                            }
                        }
                        else if (board == posUp2) {
                            if (numeroCarteChoisi > pileUp2.getNumeroCarte() || numeroCarteChoisi == pileUp2.getNumeroCarte() - 10){
                                deckJeu.removeView(carte);
                                board.removeAllViewsInLayout();
                                board.addView(carte);
                                carte.setVisibility(TextView.VISIBLE);
                                pileUp2.setNumeroCarte(numeroCarteChoisi);
                                carte.setOnTouchListener(null);
                                carteRestant--;
                                if (numeroCarteChoisi == piledown1.getNumeroCarte() + 10) {
                                    score += 2400 + bonusTemps;
                                }
                                else {
                                    score += 800 + bonusTemps;
                                }
                            }
                        }
                         else {
                            carte.setVisibility(TextView.VISIBLE);
                            return false;
                        }

//                  Generation et installation des nouvelles cartes s'il y a 2 emplacements libres
                    for (int i = 0; i < deckLayout.getChildCount(); i++) {
                        enf = (LinearLayout) deckLayout.getChildAt(i);
                        for (int j = 0; j < enf.getChildCount(); j++) {
                            enf2 = (LinearLayout) enf.getChildAt(j);
                            for (int k = 0; k < enf.getChildCount(); k++) {
                                enf3 = (LinearLayout) enf.getChildAt(k);
                                if (enf3.getChildAt(0) == null) {
                                    compteurEmplacementVide++;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < deckLayout.getChildCount(); i++) {
                        enf = (LinearLayout) deckLayout.getChildAt(i);
                        for (int j = 0; j < enf.getChildCount(); j++) {
                            enf2 = (LinearLayout) enf.getChildAt(j);
                            for (int l = 0; l < enf.getChildCount(); l++) {
                                enf3 = (LinearLayout) enf.getChildAt(l);
                                if (enf3.getChildAt(0) == null) {
                                    if (compteurEmplacementVide % 4 == 0) {
                                        TextView imageCarte = new TextView(MainActivity.this);
                                        ViewGroup.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                        configurationImageCarte(imageCarte);
//                                      Retirer la carte du deck
                                        imageCarte.setText(String.valueOf(deck.getV().elementAt(0).getNumero()));
                                        deck.getV().removeElementAt(0);
                                        enf3.addView(imageCarte, layoutParam);

                                        enf3.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                                                verificationVersion(view, shadowBuilder).setVisibility(View.INVISIBLE);
                                                return true;
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                    break;
                default:
                break;
            }

            return true;
        }
    }

    public void configurationImageCarte(TextView imageCarte){
        imageCarte.setGravity(5);
        imageCarte.setPadding(0,15,25,0);
        imageCarte.setBackgroundResource(R.drawable.carte);
        imageCarte.setCursorVisible(true);
        imageCarte.setTextSize(25);
    }

    public View verificationVersion(View view, View.DragShadowBuilder shadowBuilder){
        int sdkVersion = Build.VERSION.SDK_INT;
        //Demarre le processus de drag&drop
        if (sdkVersion <= 24) {
            view.startDrag(null, shadowBuilder, view, 0); // deprecated API24
        }
        else {
            view.startDragAndDrop(null,shadowBuilder,view,0); // API@$++
        }
        return view;
    }

    public void finDePartie(View view) {
        Intent pageFinDePartie = new Intent(MainActivity.this, FinDePartie.class);
        startActivity(pageFinDePartie);
    }

}