/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import com.itcs.helpdesk.persistence.entities.Condicion;
import com.itcs.helpdesk.persistence.entities.ReglaTrigger;
import com.itcs.helpdesk.persistence.entities.TipoComparacion;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.PreexistingEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.eclipse.persistence.config.EntityManagerProperties;

/**
 *
 * @author jonathan
 */
public class CondicionJpaController extends AbstractJPAController implements Serializable {

    public CondicionJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }

//public class CondicionJpaController implements Serializable {
//
//    public CondicionJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }
    public void create(Condicion condicion) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            TipoComparacion idComparador = condicion.getIdComparador();
            if (idComparador != null) {
                idComparador = em.getReference(idComparador.getClass(), idComparador.getIdComparador());
                condicion.setIdComparador(idComparador);
            }
            ReglaTrigger idTrigger = condicion.getIdTrigger();
            if (idTrigger != null) {
                idTrigger = em.getReference(idTrigger.getClass(), idTrigger.getIdTrigger());
                condicion.setIdTrigger(idTrigger);
            }

            em.persist(condicion);

            if (idTrigger != null) {
                idTrigger.getCondicionList().add(condicion);
                idTrigger = em.merge(idTrigger);
            }

            utx.commit();
        } catch (Exception ex) {
//            Log.createLogger(CondicionJpaController.class.getName()).log(Level.SEVERE, null, ex);
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCondicion(condicion.getIdCondicion()) != null) {
                throw new PreexistingEntityException("Condicion " + condicion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Condicion condicion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Condicion persistentCondicion = em.find(Condicion.class, condicion.getIdCondicion());
            TipoComparacion idComparadorOld = persistentCondicion.getIdComparador();
            TipoComparacion idComparadorNew = condicion.getIdComparador();
            ReglaTrigger idTriggerOld = persistentCondicion.getIdTrigger();
            ReglaTrigger idTriggerNew = condicion.getIdTrigger();
            if (idComparadorNew != null) {
                idComparadorNew = em.getReference(idComparadorNew.getClass(), idComparadorNew.getIdComparador());
                condicion.setIdComparador(idComparadorNew);
            }
            if (idTriggerNew != null) {
                idTriggerNew = em.getReference(idTriggerNew.getClass(), idTriggerNew.getIdTrigger());
                condicion.setIdTrigger(idTriggerNew);
            }

            condicion = em.merge(condicion);

            if (idTriggerOld != null && !idTriggerOld.equals(idTriggerNew)) {
                idTriggerOld.getCondicionList().remove(condicion);
                idTriggerOld = em.merge(idTriggerOld);
            }
            if (idTriggerNew != null && !idTriggerNew.equals(idTriggerOld)) {
                idTriggerNew.getCondicionList().add(condicion);
                idTriggerNew = em.merge(idTriggerNew);
            }

            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = condicion.getIdCondicion();
                if (findCondicion(id) == null) {
                    throw new NonexistentEntityException("The condicion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Condicion condicion;
            try {
                condicion = em.getReference(Condicion.class, id);
                condicion.getIdCondicion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The condicion with id " + id + " no longer exists.", enfe);
            }
            TipoComparacion idComparador = condicion.getIdComparador();

            ReglaTrigger idTrigger = condicion.getIdTrigger();
            if (idTrigger != null) {
                idTrigger.getCondicionList().remove(condicion);
                idTrigger = em.merge(idTrigger);
            }

            em.remove(condicion);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Condicion> findCondicionEntities() {
        return findCondicionEntities(true, -1, -1);
    }

    public List<Condicion> findCondicionEntities(int maxResults, int firstResult) {
        return findCondicionEntities(false, maxResults, firstResult);
    }

    private List<Condicion> findCondicionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Condicion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Condicion findCondicion(Integer id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Condicion.class, id);
        } finally {
            em.close();
        }
    }

    public int getCondicionCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Condicion> rt = cq.from(Condicion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
