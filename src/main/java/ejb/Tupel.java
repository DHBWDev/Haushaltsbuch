/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

/**
 *
 * @author Fabio Kraemer
 */
public class Tupel {
    private String label;
    private Double wert;
    
   public Tupel(String label, double wert){
       this.wert = wert;
       this.label = label;
   }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getWert() {
        return wert;
    }

    public void setWert(Double wert) {
        this.wert = wert;
    }
    
    
}
