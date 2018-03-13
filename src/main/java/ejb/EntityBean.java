package ejb;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Abstrakte Basisklasse für EJBs, die einfach nur Standardmethoden zum Lesen
 * und Schreiben eines Entity-Typs bietet.
 *
 * @param <Entity> Basisklasse der Entität
 * @param <EntityId> Datentyp oder Klasse für die Schlüsselwerte
 */
public abstract class EntityBean<Entity, EntityId> {

    @PersistenceContext
    EntityManager em;

    private final Class<Entity> entityClass;
    
    /**
     * Dieser Konstruktor muss von der erbenden Klasse aufgerufen werden, um
     * das Klassenobjekt der Entity zu setzen. Sonst lässt sich die Methode
     * findById() aufgrund einer Einschränkung der Java Generics hier nicht
     * typsicher definieren.
     * 
     * @param entityClass Klasse der zugrunde liegenden Entity
     */
    public EntityBean(Class<Entity> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Auslesen eines eindeutigen Datensatzes anhand seiner ID bzw. seines
     * Primary Key.
     * 
     * @param id Schlüsselwert
     * @return Gefundener Datensatz oder null
     */
    public Entity findeMitId(EntityId id) {
        if (id == null) {
            return null;
        }
        return em.find(entityClass, id);
    }

    /**
     * Auslesen aller Datensätze (Reihenfolge undefiniert)
     * @return Liste mit allen Datensätzen
     */
    public List<Entity> findeAlle() {
        String select = "SELECT e FROM $E e".replace("$E", this.entityClass.getName());
        return em.createQuery(select).getResultList();
    }

    /**
     * Speichern eines neuen Datensatzes.
     * @param entity Zu speichernder Datensatz
     * @return Gespeicherter Datensatz
     */
    public Entity speichernNeu(Entity entity) {
        em.persist(entity);
        return em.merge(entity);
    }

    /**
     * Änderungen an einem vorhandenen Datensatz speichern
     * @param entity Zu speichernder Datensatz
     * @return Gespeicherter Datensatz
     */
    public Entity aktualisieren(Entity entity) {
        return em.merge(entity);
    }

    /**
     * Vorhandenen Dantensatz löschen
     * @param entity Zu löschender Datensatz
     */
    public void löschen(Entity entity) {
        em.remove(em.merge(entity));
    }
}
