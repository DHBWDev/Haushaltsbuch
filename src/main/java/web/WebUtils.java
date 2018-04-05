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
package web;

import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

/**
 * Statische Hilfsmethoden
 */
public class WebUtils {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    /**
     * Stellt sicher, dass einer URL der Kontextpfad der Anwendung vorangestellt
     * ist. Denn sonst ruft man aus Versehen Seiten auf, die nicht zur eigenen
     * Webanwendung gehören.
     *
     * @param request HttpRequest-Objekt
     * @param url Die aufzurufende URL
     * @return Die vollständige URL
     */
    public static String appUrl(HttpServletRequest request, String url) {
        return request.getContextPath() + url;
    }

    /**
     * Formatiert ein Datum für die Ausgabe, z.B. 31.12.9999
     *
     * @param date Datum
     * @return String für die Ausgabe
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * Formatiert eine Uhrzeit für die Ausgabe, z.B. 11:50:00
     *
     * @param time Uhrzeit
     * @return String für die Ausgabe
     */
    public static String formatTime(Time time) {
        return TIME_FORMAT.format(time);
    }

    /**
     * Erzeugt ein Datumsobjekt aus dem übergebenen String, z.B. 03.06.1986
     *
     * @param input Eingegebener String
     * @return Datumsobjekt oder null bei einem Fehler
     */
    public static Date parseDate(String input) {
        try {
            java.util.Date date = DATE_FORMAT.parse(input);
            return new Date(date.getTime());
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * Erzeugt ein Uhrzeitobjekt aus dem übergebenen String, z.B. 09:20:00
     *
     * @param input Eingegebener String
     * @return Uhrzeitobjekt oder null bei einem Fehler
     */
    public static Time parseTime(String input) {
        try {
            java.util.Date date = TIME_FORMAT.parse(input);
            return new Time(date.getTime());
        } catch (ParseException ex) {
            return null;
        }
    }

    public String formatDouble(Double d) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.GERMANY));
        return df.format(d);
    }
}
