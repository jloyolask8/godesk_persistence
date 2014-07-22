/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.itcs.helpdesk.persistence.entities.Caso;
import com.itcs.helpdesk.persistence.entities.Prioridad;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.PreexistingEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.eclipse.persistence.config.EntityManagerProperties;

/**
 *
 * @author jonathan
 */
public class PrioridadJpaController extends AbstractJPAController implements Serializable {

    public PrioridadJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class PrioridadJpaController implements Serializable {
//
//    public PrioridadJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(Prioridad prioridad) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (prioridad.getCasoList() == null) {
            prioridad.setCasoList(new ArrayList<Caso>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            List<Caso> attachedCasoList = new ArrayList<Caso>();
            for (Caso casoListCasoToAttach : prioridad.getCasoList()) {
                casoListCasoToAttach = em.getReference(casoListCasoToAttach.getClass(), casoListCasoToAttach.getIdCaso());
                attachedCasoList.add(casoListCasoToAttach);
            }
            prioridad.setCasoList(attachedCasoList);
            em.persist(prioridad);
            for (Caso casoListCaso : prioridad.getCasoList()) {
                Prioridad oldIdPrioridadOfCasoListCaso = casoListCaso.getIdPrioridad();
                casoListCaso.setIdPrioridad(prioridad);
                casoListCaso = em.merge(casoListCaso);
                if (oldIdPrioridadOfCasoListCaso != null) {
                    oldIdPrioridadOfCasoListCaso.getCasoList().remove(casoListCaso);
                    oldIdPrioridadOfCasoListCaso = em.merge(oldIdPrioridadOfCasoListCaso);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPrioridad(prioridad.getIdPrioridad()) != null) {
                throw new PreexistingEntityException("Prioridad " + prioridad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Prioridad prioridad) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Prioridad persistentPrioridad = em.find(Prioridad.class, prioridad.getIdPrioridad());
            List<Caso> casoListOld = persistentPrioridad.getCasoList();
            List<Caso> casoListNew = prioridad.getCasoList();
            List<Caso> attachedCasoListNew = new ArrayList<Caso>();
            for (Caso casoListNewCasoToAttach : casoListNew) {
                casoListNewCasoToAttach = em.getReference(casoListNewCasoToAttach.getClass(), casoListNewCasoToAttach.getIdCaso());
                attachedCasoListNew.add(casoListNewCasoToAttach);
            }
            casoListNew = attachedCasoListNew;
            prioridad.setCasoList(casoListNew);
            prioridad = em.merge(prioridad);
            for (Caso casoListOldCaso : casoListOld) {
                if (!casoListNew.contains(casoListOldCaso)) {
                    casoListOldCaso.setIdPrioridad(null);
                    casoListOldCaso = em.merge(casoListOldCaso);
                }
            }
            for (Caso casoListNewCaso : casoListNew) {
                if (!casoListOld.contains(casoListNewCaso)) {
                    Prioridad oldIdPrioridadOfCasoListNewCaso = casoListNewCaso.getIdPrioridad();
                    casoListNewCaso.setIdPrioridad(prioridad);
                    casoListNewCaso = em.merge(casoListNewCaso);
                    if (oldIdPrioridadOfCasoListNewCaso != null && !oldIdPrioridadOfCasoListNewCaso.equals(prioridad)) {
                        oldIdPrioridadOfCasoListNewCaso.getCasoList().remove(casoListNewCaso);
                        oldIdPrioridadOfCasoListNewCaso = em.merge(oldIdPrioridadOfCasoListNewCaso);
                    }
                }
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
                String id = prioridad.getIdPrioridad();
                if (findPrioridad(id) == null) {
                    throw new NonexistentEntityException("The prioridad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Prioridad prioridad;
            try {
                prioridad = em.getReference(Prioridad.class, id);
                prioridad.getIdPrioridad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prioridad with id " + id + " no longer exists.", enfe);
            }
            List<Caso> casoList = prioridad.getCasoList();
            for (Caso casoListCaso : casoList) {
                casoListCaso.setIdPrioridad(null);
                casoListCaso = em.merge(casoListCaso);
            }
            em.remove(prioridad);
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

    public List<Prioridad> findPrioridadEntities() {
        return findPrioridadEntities(true, -1, -1);
    }

    public List<Prioridad> findPrioridadEntities(int maxResults, int firstResult) {
        return findPrioridadEntities(false, maxResults, firstResult);
    }

    private List<Prioridad> findPrioridadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prioridad.class));
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

    public Prioridad findPrioridad(String id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Prioridad.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrioridadCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prioridad> rt = cq.from(Prioridad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
