package ejb;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
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

   
    public void registrieren(String benutzerName, String passwort) throws UserAlreadyExistsException {
        if (em.find(Benutzer.class, benutzerName) != null) {
            throw new UserAlreadyExistsException("Der Benutzername $B ist bereits vergeben.".replace("$B", benutzerName));
        }

        Benutzer benutzer = new Benutzer(benutzerName, passwort);
        //user.addToGroup("maximarkt-app-user");
        em.persist(benutzer);
    }

    @RolesAllowed("")
    public void aenderePasswort(Benutzer benutzer, String altesPasswort, String neuesPasswort) throws InvalidCredentialsException {
        if (benutzer == null || !benutzer.testePasswort(altesPasswort)) {
            throw new InvalidCredentialsException("Benutzername oder Passwort sind falsch.");
        }

        benutzer.setPasswort(neuesPasswort);
    }

    @RolesAllowed("")
    public void löschen(Benutzer benutzer) {
        this.em.remove(benutzer);
    }

  
    @RolesAllowed("")
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
