/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Fabio Kraemer
 */
public class StatistikDaten {
    public List<Tupel> tupeln; 
    private String titel;
    private String farbe;
    
    public StatistikDaten (){
        this.tupeln = new ArrayList<>();
    }
    
    public StatistikDaten (String farbe, String titel){
        this.farbe = farbe;
        this.titel = titel;
        this.tupeln = new ArrayList<>();
    }
    
    public String erzeugeJson (){
        JSONObject data1 = new JSONObject();

        JSONObject dataset = new JSONObject();
        dataset.put("label", this.titel);
        
        if ("rot".equals(this.farbe)) {
            dataset.put("backgroundColor", "rgb(255, 0, 0)");
            dataset.put("borderColor", "rgb(255, 0, 0)");
        }

        if ("gruen".equals(this.farbe)) {
            dataset.put("backgroundColor", "rgb(0, 255, 0)");
            dataset.put("borderColor", "rgb(0, 255, 0)");
        }

        dataset.put("data", this.getArrayWithWerte());

        JSONArray array = new JSONArray();

        array.put(dataset);

        data1.put("datasets", array);
        data1.put("labels", this.getArrayWithLabels());

        return data1.toString();
    }

    public String getFarbe() {
        return farbe;
    }

    public void setFarbe(String farbe) {
        this.farbe = farbe;
    }
    
    public String getTitel() {
        return titel;
    }

    public void setTitel(String farbe) {
        this.titel = farbe;
    }
    
    public String [] getArrayWithLabels(){
        String [] labels = new String[this.tupeln.size()];
        int i = 0;
        for (Tupel t : this.tupeln){
            labels[i] = t.getLabel();
            i = i +1;
        }
        
        return labels;
    }
    
    public Double [] getArrayWithWerte(){
        Double [] betraege = new Double[this.tupeln.size()];
        int i = 0;
        for (Tupel t : this.tupeln){
            betraege[i] = t.getWert();
            i = i +1;
        }
        
        return betraege;
    }
    
    public void setWert(Double betrag, String label){
        this.tupeln.add(new Tupel(label, betrag));
    }
    
    
}
