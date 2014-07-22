/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import com.itcs.helpdesk.persistence.entities.FiltroVista;
import com.itcs.helpdesk.persistence.entities.Vista;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
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
public class FiltroVistaJpaController extends AbstractJPAController implements Serializable {

    public FiltroVistaJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class FiltroVistaJpaController implements Serializable {
//
//    public FiltroVistaJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(FiltroVista filtroVista) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Vista idVista = filtroVista.getIdVista();
            if (idVista != null) {
                idVista = em.getReference(idVista.getClass(), idVista.getIdVista());
                filtroVista.setIdVista(idVista);
            }
            em.persist(filtroVista);
            if (idVista != null) {
                idVista.getFiltrosVistaList().add(filtroVista);
                idVista = em.merge(idVista);
            }
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

    public void edit(FiltroVista filtroVista) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            FiltroVista persistentFiltroVista = em.find(FiltroVista.class, filtroVista.getIdFiltro());
            Vista idVistaOld = persistentFiltroVista.getIdVista();
            Vista idVistaNew = filtroVista.getIdVista();
            if (idVistaNew != null) {
                idVistaNew = em.getReference(idVistaNew.getClass(), idVistaNew.getIdVista());
                filtroVista.setIdVista(idVistaNew);
            }
            filtroVista = em.merge(filtroVista);
            if (idVistaOld != null && !idVistaOld.equals(idVistaNew)) {
                idVistaOld.getFiltrosVistaList().remove(filtroVista);
                idVistaOld = em.merge(idVistaOld);
            }
            if (idVistaNew != null && !idVistaNew.equals(idVistaOld)) {
                idVistaNew.getFiltrosVistaList().add(filtroVista);
                idVistaNew = em.merge(idVistaNew);
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
                Integer id = filtroVista.getIdFiltro();
                if (findFiltroVista(id) == null) {
                    throw new NonexistentEntityException("The filtroVista with id " + id + " no longer exists.");
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
            FiltroVista filtroVista;
            try {
                filtroVista = em.getReference(FiltroVista.class, id);
                filtroVista.getIdFiltro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The filtroVista with id " + id + " no longer exists.", enfe);
            }
            Vista idVista = filtroVista.getIdVista();
            if (idVista != null) {
                idVista.getFiltrosVistaList().remove(filtroVista);
                idVista = em.merge(idVista);
            }
            em.remove(filtroVista);
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

    public List<FiltroVista> findFiltroVistaEntities() {
        return findFiltroVistaEntities(true, -1, -1);
    }

    public List<FiltroVista> findFiltroVistaEntities(int maxResults, int firstResult) {
        return findFiltroVistaEntities(false, maxResults, firstResult);
    }

    private List<FiltroVista> findFiltroVistaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FiltroVista.class));
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

    public FiltroVista findFiltroVista(Integer id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(FiltroVista.class, id);
        } finally {
            em.close();
        }
    }

    public int getFiltroVistaCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FiltroVista> rt = cq.from(FiltroVista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
