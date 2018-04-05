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

import java.util.List;
import javax.ejb.Stateless;
import jpa.Kategorie;
import jpa.TransaktionsArten;

@Stateless
public class KategorieBean extends EntityBean<Kategorie, String> {

    public KategorieBean() {
        super(Kategorie.class);
    }
 
    public List<Kategorie> findeAlle(TransaktionsArten art){
        String select = "SELECT k FROM Kategorie k WHERE k.art = :art";
        return em.createQuery(select).setParameter("art", art).getResultList();
    }
    public String Test(){
        return "test";
    }
}
