/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

/**
 *
 * Arten  von Transaktionen
 */
public enum TransaktionsArten {
    Einnahme, Ausgabe;

    /**
     * Bezeichnung ermitteln
     *
     * @return Bezeichnung
     */
    public String getLabel() {
        switch (this) {
            case Einnahme:
                return "Einnahme";
            case Ausgabe:
                return "Ausgabe";       
            default:
                return this.toString();
        }
    }
}

