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
import com.itcs.helpdesk.persistence.entities.Componente;
import com.itcs.helpdesk.persistence.entities.Caso;
import com.itcs.helpdesk.persistence.entities.SubComponente;
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
public class SubComponenteJpaController extends AbstractJPAController implements Serializable {

    public SubComponenteJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class SubComponenteJpaController implements Serializable {
//
//    public SubComponenteJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(SubComponente subComponente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (subComponente.getCasoList() == null) {
            subComponente.setCasoList(new ArrayList<Caso>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Componente idComponente = subComponente.getIdComponente();
            if (idComponente != null) {
                idComponente = em.getReference(idComponente.getClass(), idComponente.getIdComponente());
                subComponente.setIdComponente(idComponente);
            }
            List<Caso> attachedCasoList = new ArrayList<Caso>();
            for (Caso casoListCasoToAttach : subComponente.getCasoList()) {
                casoListCasoToAttach = em.getReference(casoListCasoToAttach.getClass(), casoListCasoToAttach.getIdCaso());
                attachedCasoList.add(casoListCasoToAttach);
            }
            subComponente.setCasoList(attachedCasoList);
            em.persist(subComponente);
            if (idComponente != null) {
                idComponente.getSubComponenteList().add(subComponente);
                idComponente = em.merge(idComponente);
            }
            for (Caso casoListCaso : subComponente.getCasoList()) {
                SubComponente oldIdSubComponenteOfCasoListCaso = casoListCaso.getIdSubComponente();
                casoListCaso.setIdSubComponente(subComponente);
                casoListCaso = em.merge(casoListCaso);
                if (oldIdSubComponenteOfCasoListCaso != null) {
                    oldIdSubComponenteOfCasoListCaso.getCasoList().remove(casoListCaso);
                    oldIdSubComponenteOfCasoListCaso = em.merge(oldIdSubComponenteOfCasoListCaso);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSubComponente(subComponente.getIdSubComponente()) != null) {
                throw new PreexistingEntityException("SubComponente " + subComponente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SubComponente subComponente) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            SubComponente persistentSubComponente = em.find(SubComponente.class, subComponente.getIdSubComponente());
            Componente idComponenteOld = persistentSubComponente.getIdComponente();
            Componente idComponenteNew = subComponente.getIdComponente();
            List<Caso> casoListOld = persistentSubComponente.getCasoList();
            List<Caso> casoListNew = subComponente.getCasoList();
            if (idComponenteNew != null) {
                idComponenteNew = em.getReference(idComponenteNew.getClass(), idComponenteNew.getIdComponente());
                subComponente.setIdComponente(idComponenteNew);
            }
            List<Caso> attachedCasoListNew = new ArrayList<Caso>();
            for (Caso casoListNewCasoToAttach : casoListNew) {
                casoListNewCasoToAttach = em.getReference(casoListNewCasoToAttach.getClass(), casoListNewCasoToAttach.getIdCaso());
                attachedCasoListNew.add(casoListNewCasoToAttach);
            }
            casoListNew = attachedCasoListNew;
            subComponente.setCasoList(casoListNew);
            subComponente = em.merge(subComponente);
            if (idComponenteOld != null && !idComponenteOld.equals(idComponenteNew)) {
                idComponenteOld.getSubComponenteList().remove(subComponente);
                idComponenteOld = em.merge(idComponenteOld);
            }
            if (idComponenteNew != null && !idComponenteNew.equals(idComponenteOld)) {
                idComponenteNew.getSubComponenteList().add(subComponente);
                idComponenteNew = em.merge(idComponenteNew);
            }
            for (Caso casoListOldCaso : casoListOld) {
                if (!casoListNew.contains(casoListOldCaso)) {
                    casoListOldCaso.setIdSubComponente(null);
                    casoListOldCaso = em.merge(casoListOldCaso);
                }
            }
            for (Caso casoListNewCaso : casoListNew) {
                if (!casoListOld.contains(casoListNewCaso)) {
                    SubComponente oldIdSubComponenteOfCasoListNewCaso = casoListNewCaso.getIdSubComponente();
                    casoListNewCaso.setIdSubComponente(subComponente);
                    casoListNewCaso = em.merge(casoListNewCaso);
                    if (oldIdSubComponenteOfCasoListNewCaso != null && !oldIdSubComponenteOfCasoListNewCaso.equals(subComponente)) {
                        oldIdSubComponenteOfCasoListNewCaso.getCasoList().remove(casoListNewCaso);
                        oldIdSubComponenteOfCasoListNewCaso = em.merge(oldIdSubComponenteOfCasoListNewCaso);
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
                String id = subComponente.getIdSubComponente();
                if (findSubComponente(id) == null) {
                    throw new NonexistentEntityException("The subComponente with id " + id + " no longer exists.");
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
            SubComponente subComponente;
            try {
                subComponente = em.getReference(SubComponente.class, id);
                subComponente.getIdSubComponente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subComponente with id " + id + " no longer exists.", enfe);
            }
            Componente idComponente = subComponente.getIdComponente();
            if (idComponente != null) {
                idComponente.getSubComponenteList().remove(subComponente);
                idComponente = em.merge(idComponente);
            }
            List<Caso> casoList = subComponente.getCasoList();
            for (Caso casoListCaso : casoList) {
                casoListCaso.setIdSubComponente(null);
                casoListCaso = em.merge(casoListCaso);
            }
            em.remove(subComponente);
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

    public List<SubComponente> findSubComponenteEntities() {
        return findSubComponenteEntities(true, -1, -1);
    }

    public List<SubComponente> findSubComponenteEntities(int maxResults, int firstResult) {
        return findSubComponenteEntities(false, maxResults, firstResult);
    }

    private List<SubComponente> findSubComponenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SubComponente.class));
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

    public SubComponente findSubComponente(String id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(SubComponente.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubComponenteCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SubComponente> rt = cq.from(SubComponente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
