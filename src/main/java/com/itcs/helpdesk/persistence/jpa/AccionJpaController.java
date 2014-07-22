/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import com.itcs.helpdesk.persistence.entities.Accion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.itcs.helpdesk.persistence.entities.ReglaTrigger;
import com.itcs.helpdesk.persistence.entities.TipoAccion;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.PreexistingEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.RollbackFailureException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.eclipse.persistence.config.EntityManagerProperties;

/**
 *
 * @author jonathan
 */
public class AccionJpaController extends AbstractJPAController implements Serializable {

    public AccionJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class AccionJpaController implements Serializable {
//
//    public AccionJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(Accion accion) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            ReglaTrigger idTrigger = accion.getIdTrigger();
            if (idTrigger != null) {
                idTrigger = em.getReference(idTrigger.getClass(), idTrigger.getIdTrigger());
                accion.setIdTrigger(idTrigger);
            }
            TipoAccion idTipoAccion = accion.getIdTipoAccion();
            if (idTipoAccion != null) {
                idTipoAccion = em.getReference(idTipoAccion.getClass(), idTipoAccion.getIdTipoAccion());
                accion.setIdTipoAccion(idTipoAccion);
            }
            em.persist(accion);
            if (idTrigger != null) {
                idTrigger.getAccionList().add(accion);
                idTrigger = em.merge(idTrigger);
            }
            if (idTipoAccion != null) {
                idTipoAccion.getAccionList().add(accion);
                idTipoAccion = em.merge(idTipoAccion);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAccion(accion.getIdAccion()) != null) {
                throw new PreexistingEntityException("Accion " + accion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Accion accion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Accion persistentAccion = em.find(Accion.class, accion.getIdAccion());
            ReglaTrigger idTriggerOld = persistentAccion.getIdTrigger();
            ReglaTrigger idTriggerNew = accion.getIdTrigger();
            TipoAccion idTipoAccionOld = persistentAccion.getIdTipoAccion();
            TipoAccion idTipoAccionNew = accion.getIdTipoAccion();
            if (idTriggerNew != null) {
                idTriggerNew = em.getReference(idTriggerNew.getClass(), idTriggerNew.getIdTrigger());
                accion.setIdTrigger(idTriggerNew);
            }
            if (idTipoAccionNew != null) {
                idTipoAccionNew = em.getReference(idTipoAccionNew.getClass(), idTipoAccionNew.getIdTipoAccion());
                accion.setIdTipoAccion(idTipoAccionNew);
            }
            accion = em.merge(accion);
            if (idTriggerOld != null && !idTriggerOld.equals(idTriggerNew)) {
                idTriggerOld.getAccionList().remove(accion);
                idTriggerOld = em.merge(idTriggerOld);
            }
            if (idTriggerNew != null && !idTriggerNew.equals(idTriggerOld)) {
                idTriggerNew.getAccionList().add(accion);
                idTriggerNew = em.merge(idTriggerNew);
            }
            if (idTipoAccionOld != null && !idTipoAccionOld.equals(idTipoAccionNew)) {
                idTipoAccionOld.getAccionList().remove(accion);
                idTipoAccionOld = em.merge(idTipoAccionOld);
            }
            if (idTipoAccionNew != null && !idTipoAccionNew.equals(idTipoAccionOld)) {
                idTipoAccionNew.getAccionList().add(accion);
                idTipoAccionNew = em.merge(idTipoAccionNew);
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
                Integer id = accion.getIdAccion();
                if (findAccion(id) == null) {
                    throw new NonexistentEntityException("The accion with id " + id + " no longer exists.");
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
            Accion accion;
            try {
                accion = em.getReference(Accion.class, id);
                accion.getIdAccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The accion with id " + id + " no longer exists.", enfe);
            }
            ReglaTrigger idTrigger = accion.getIdTrigger();
            if (idTrigger != null) {
                idTrigger.getAccionList().remove(accion);
                idTrigger = em.merge(idTrigger);
            }
            TipoAccion idTipoAccion = accion.getIdTipoAccion();
            if (idTipoAccion != null) {
                idTipoAccion.getAccionList().remove(accion);
                idTipoAccion = em.merge(idTipoAccion);
            }
            em.remove(accion);
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

    public List<Accion> findAccionEntities() {
        return findAccionEntities(true, -1, -1);
    }

    public List<Accion> findAccionEntities(int maxResults, int firstResult) {
        return findAccionEntities(false, maxResults, firstResult);
    }

    private List<Accion> findAccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Accion.class));
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

    public Accion findAccion(Integer id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Accion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccionCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Accion> rt = cq.from(Accion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
