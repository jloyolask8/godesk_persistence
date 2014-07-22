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
import com.itcs.helpdesk.persistence.entities.Accion;
import com.itcs.helpdesk.persistence.entities.TipoAccion;
import com.itcs.helpdesk.persistence.jpa.exceptions.IllegalOrphanException;
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
public class TipoAccionJpaController extends AbstractJPAController implements Serializable {

    public TipoAccionJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class TipoAccionJpaController implements Serializable {
//
//    public TipoAccionJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(TipoAccion tipoAccion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tipoAccion.getAccionList() == null) {
            tipoAccion.setAccionList(new ArrayList<Accion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            List<Accion> attachedAccionList = new ArrayList<Accion>();
            for (Accion accionListAccionToAttach : tipoAccion.getAccionList()) {
                accionListAccionToAttach = em.getReference(accionListAccionToAttach.getClass(), accionListAccionToAttach.getIdAccion());
                attachedAccionList.add(accionListAccionToAttach);
            }
            tipoAccion.setAccionList(attachedAccionList);
            em.persist(tipoAccion);
            for (Accion accionListAccion : tipoAccion.getAccionList()) {
                TipoAccion oldIdTipoAccionOfAccionListAccion = accionListAccion.getIdTipoAccion();
                accionListAccion.setIdTipoAccion(tipoAccion);
                accionListAccion = em.merge(accionListAccion);
                if (oldIdTipoAccionOfAccionListAccion != null) {
                    oldIdTipoAccionOfAccionListAccion.getAccionList().remove(accionListAccion);
                    oldIdTipoAccionOfAccionListAccion = em.merge(oldIdTipoAccionOfAccionListAccion);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipoAccion(tipoAccion.getIdTipoAccion()) != null) {
                throw new PreexistingEntityException("TipoAccion " + tipoAccion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoAccion tipoAccion) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            TipoAccion persistentTipoAccion = em.find(TipoAccion.class, tipoAccion.getIdTipoAccion());
            List<Accion> accionListOld = persistentTipoAccion.getAccionList();
            List<Accion> accionListNew = tipoAccion.getAccionList();
            List<String> illegalOrphanMessages = null;
            for (Accion accionListOldAccion : accionListOld) {
                if (!accionListNew.contains(accionListOldAccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Accion " + accionListOldAccion + " since its idTipoAccion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Accion> attachedAccionListNew = new ArrayList<Accion>();
            for (Accion accionListNewAccionToAttach : accionListNew) {
                accionListNewAccionToAttach = em.getReference(accionListNewAccionToAttach.getClass(), accionListNewAccionToAttach.getIdAccion());
                attachedAccionListNew.add(accionListNewAccionToAttach);
            }
            accionListNew = attachedAccionListNew;
            tipoAccion.setAccionList(accionListNew);
            tipoAccion = em.merge(tipoAccion);
            for (Accion accionListNewAccion : accionListNew) {
                if (!accionListOld.contains(accionListNewAccion)) {
                    TipoAccion oldIdTipoAccionOfAccionListNewAccion = accionListNewAccion.getIdTipoAccion();
                    accionListNewAccion.setIdTipoAccion(tipoAccion);
                    accionListNewAccion = em.merge(accionListNewAccion);
                    if (oldIdTipoAccionOfAccionListNewAccion != null && !oldIdTipoAccionOfAccionListNewAccion.equals(tipoAccion)) {
                        oldIdTipoAccionOfAccionListNewAccion.getAccionList().remove(accionListNewAccion);
                        oldIdTipoAccionOfAccionListNewAccion = em.merge(oldIdTipoAccionOfAccionListNewAccion);
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
                String id = tipoAccion.getIdTipoAccion();
                if (findTipoAccion(id) == null) {
                    throw new NonexistentEntityException("The tipoAccion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            TipoAccion tipoAccion;
            try {
                tipoAccion = em.getReference(TipoAccion.class, id);
                tipoAccion.getIdTipoAccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoAccion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Accion> accionListOrphanCheck = tipoAccion.getAccionList();
            for (Accion accionListOrphanCheckAccion : accionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoAccion (" + tipoAccion + ") cannot be destroyed since the Accion " + accionListOrphanCheckAccion + " in its accionList field has a non-nullable idTipoAccion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoAccion);
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

    public List<TipoAccion> findTipoAccionEntities() {
        return findTipoAccionEntities(true, -1, -1);
    }

    public List<TipoAccion> findTipoAccionEntities(int maxResults, int firstResult) {
        return findTipoAccionEntities(false, maxResults, firstResult);
    }

    private List<TipoAccion> findTipoAccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoAccion.class));
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

    public TipoAccion findTipoAccion(String id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(TipoAccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoAccionCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoAccion> rt = cq.from(TipoAccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
