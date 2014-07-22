/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import com.itcs.helpdesk.persistence.entities.Canal;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.itcs.helpdesk.persistence.entities.Caso;
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
//public class CanalJpaController implements Serializable {
public class CanalJpaController extends AbstractJPAController implements Serializable {

    public CanalJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }

//    public CanalJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }
    public void create(Canal canal) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (canal.getCasoList() == null) {
            canal.setCasoList(new ArrayList<Caso>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            List<Caso> attachedCasoList = new ArrayList<Caso>();
            for (Caso casoListCasoToAttach : canal.getCasoList()) {
                casoListCasoToAttach = em.getReference(casoListCasoToAttach.getClass(), casoListCasoToAttach.getIdCaso());
                attachedCasoList.add(casoListCasoToAttach);
            }
            canal.setCasoList(attachedCasoList);
            em.persist(canal);
            for (Caso casoListCaso : canal.getCasoList()) {
                Canal oldIdCanalOfCasoListCaso = casoListCaso.getIdCanal();
                casoListCaso.setIdCanal(canal);
                casoListCaso = em.merge(casoListCaso);
                if (oldIdCanalOfCasoListCaso != null) {
                    oldIdCanalOfCasoListCaso.getCasoList().remove(casoListCaso);
                    oldIdCanalOfCasoListCaso = em.merge(oldIdCanalOfCasoListCaso);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCanal(canal.getIdCanal()) != null) {
                throw new PreexistingEntityException("Canal " + canal + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Canal canal) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Canal persistentCanal = em.find(Canal.class, canal.getIdCanal());
            List<Caso> casoListOld = persistentCanal.getCasoList();
            List<Caso> casoListNew = canal.getCasoList();
            List<Caso> attachedCasoListNew = new ArrayList<Caso>();
            for (Caso casoListNewCasoToAttach : casoListNew) {
                casoListNewCasoToAttach = em.getReference(casoListNewCasoToAttach.getClass(), casoListNewCasoToAttach.getIdCaso());
                attachedCasoListNew.add(casoListNewCasoToAttach);
            }
            casoListNew = attachedCasoListNew;
            canal.setCasoList(casoListNew);
            canal = em.merge(canal);
            for (Caso casoListOldCaso : casoListOld) {
                if (!casoListNew.contains(casoListOldCaso)) {
                    casoListOldCaso.setIdCanal(null);
                    casoListOldCaso = em.merge(casoListOldCaso);
                }
            }
            for (Caso casoListNewCaso : casoListNew) {
                if (!casoListOld.contains(casoListNewCaso)) {
                    Canal oldIdCanalOfCasoListNewCaso = casoListNewCaso.getIdCanal();
                    casoListNewCaso.setIdCanal(canal);
                    casoListNewCaso = em.merge(casoListNewCaso);
                    if (oldIdCanalOfCasoListNewCaso != null && !oldIdCanalOfCasoListNewCaso.equals(canal)) {
                        oldIdCanalOfCasoListNewCaso.getCasoList().remove(casoListNewCaso);
                        oldIdCanalOfCasoListNewCaso = em.merge(oldIdCanalOfCasoListNewCaso);
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
                String id = canal.getIdCanal();
                if (findCanal(id) == null) {
                    throw new NonexistentEntityException("The canal with id " + id + " no longer exists.");
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
            Canal canal;
            try {
                canal = em.getReference(Canal.class, id);
                canal.getIdCanal();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The canal with id " + id + " no longer exists.", enfe);
            }
            List<Caso> casoList = canal.getCasoList();
            for (Caso casoListCaso : casoList) {
                casoListCaso.setIdCanal(null);
                casoListCaso = em.merge(casoListCaso);
            }
            em.remove(canal);
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

    public List<Canal> findCanalEntities() {
        return findCanalEntities(true, -1, -1);
    }

    public List<Canal> findCanalEntities(int maxResults, int firstResult) {
        return findCanalEntities(false, maxResults, firstResult);
    }

    private List<Canal> findCanalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Canal.class));
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

    public Canal findCanal(String id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Canal.class, id);
        } finally {
            em.close();
        }
    }

    public int getCanalCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Canal> rt = cq.from(Canal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
