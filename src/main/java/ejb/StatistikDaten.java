/* 
 * Copyright (C) 2018 Fabio Krämer, Samuel Haag, Sebastian Greulich
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
