package com.example.carte_97;

import java.util.Collections;
import java.util.Vector;

public class Deck {
    Vector<Carte> v;

    public Deck (Vector<Carte> v) {
        this.v = v;
    }

    // Ajouter 98 cartes (1 a 98)
    public Vector<Carte> creerDeck() {
        for (int i = 1; i < 99; i++) {
            v.add(new Carte(i));
        }
        // Melanger les cartes dans le vector
        Collections.shuffle(v);
        return v;
    }

    public void setV(Vector<Carte> v) {
        this.v = v;
    }

    public Vector<Carte> getV() {
        return v;


    }
}
