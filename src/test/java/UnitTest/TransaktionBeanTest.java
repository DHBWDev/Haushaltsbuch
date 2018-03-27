package UnitTest;

import javax.ejb.EJB;
import org.junit.Test;
import ejb.TransaktionBean;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import jpa.TransaktionsArten;
import org.junit.Assert;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fabio Kraemer
 */
public class TransaktionBeanTest {
    public TransaktionBeanTest(){
        super();
    }
    
    @EJB
    TransaktionBean transaktionBean;
    
    @Test
    public void getStatistikLastYearPerMonthTest (){
       /*GregorianCalendar cal = new GregorianCalendar();
        Integer fromMonth, toMonth, fromYear, toYear;
        for (int i = 0; i<= 11; i++){
            cal.set(2016, i, 12);
            Date [][] intervalle = transaktionBean.getMonthAndYearFromTo(cal.getTime(), 2);
            
            for (Date[] d : intervalle){
                cal.setTime(d[0]);
                fromMonth = cal.get(Calendar.MONTH);
                fromYear = cal.get(Calendar.YEAR);
                
                cal.
                //Das Ende Datum liegt nach dem Anfangsdatum
                Assert.assertThat(cal.set);
            }
        }*/
        
    }
    
}
