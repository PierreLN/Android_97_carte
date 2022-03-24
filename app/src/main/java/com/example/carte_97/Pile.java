package com.example.carte_97;

public class Pile {

    Carte carte;
    public Pile(Carte carte) {
        this.carte = carte;
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }
}
