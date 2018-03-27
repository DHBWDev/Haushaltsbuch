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
        
        if ("rottoene".equals(this.farbe)) {
            String[] rottoene = new String[]{"#FF0000", "#FF7256", "#CD0000", "#EE4000", "#FF4040", "#FF3030", "#FF6A6A", "#8B2323"};
            dataset.put("backgroundColor", rottoene);
        }
        
        if ("gruentoene".equals(this.farbe)) {
            String[] gruentoene = new String[]{"#7CFC00", "#C0FF3E", "#00FF7F", "#00EE00", "#008B45", "#BCEE68", "#7CCD7C", "#00CD00"};
            dataset.put("backgroundColor", gruentoene);
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
