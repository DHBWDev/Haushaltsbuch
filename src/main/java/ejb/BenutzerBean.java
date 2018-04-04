package ejb;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Benutzer;

@Stateless
public class BenutzerBean {

    @PersistenceContext
    EntityManager em;

    @Resource
    EJBContext ctx;


    public Benutzer gibAktuellenBenutzer() {
        return this.em.find(Benutzer.class, this.ctx.getCallerPrincipal().getName());
    }

    public void löschen(Benutzer benutzer) {
        this.em.remove(benutzer);
    }

  
    public Benutzer aktualisieren(Benutzer benutzer) {
        return em.merge(benutzer);
    }

    /**
     * Fehler: Der Benutzername ist bereits vergeben
     */
    public class UserAlreadyExistsException extends Exception {

        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    /**
     * Fehler: Das übergebene Passwort stimmt nicht mit dem des Benutzers
     * überein
     */
    public class InvalidCredentialsException extends Exception {

        public InvalidCredentialsException(String message) {
            super(message);
        }
    }
}
