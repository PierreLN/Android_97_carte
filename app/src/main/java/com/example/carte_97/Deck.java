package com.example.carte_97;

import java.util.Vector;

public class Deck {
    private Carte carte;
    Vector<Carte> v;

    public Deck () {

    }



    public Vector<Carte> remplirDeck() {
        for (int i = 1; i < 99; i++) {
            v.add(new Carte(i));
        }
        return v;
    }
}
