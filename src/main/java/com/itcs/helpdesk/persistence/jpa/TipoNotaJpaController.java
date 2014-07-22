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
import com.itcs.helpdesk.persistence.entities.Nota;
import com.itcs.helpdesk.persistence.entities.TipoNota;
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
public class TipoNotaJpaController extends AbstractJPAController implements Serializable {

    public TipoNotaJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class TipoNotaJpaController implements Serializable {
//
//    public TipoNotaJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(TipoNota tipoNota) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tipoNota.getNotaList() == null) {
            tipoNota.setNotaList(new ArrayList<Nota>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            List<Nota> attachedNotaList = new ArrayList<Nota>();
            for (Nota notaListNotaToAttach : tipoNota.getNotaList()) {
                notaListNotaToAttach = em.getReference(notaListNotaToAttach.getClass(), notaListNotaToAttach.getIdNota());
                attachedNotaList.add(notaListNotaToAttach);
            }
            tipoNota.setNotaList(attachedNotaList);
            em.persist(tipoNota);
            for (Nota notaListNota : tipoNota.getNotaList()) {
                TipoNota oldIdTipoNotaOfNotaListNota = notaListNota.getTipoNota();
                notaListNota.setTipoNota(tipoNota);
                notaListNota = em.merge(notaListNota);
                if (oldIdTipoNotaOfNotaListNota != null) {
                    oldIdTipoNotaOfNotaListNota.getNotaList().remove(notaListNota);
                    oldIdTipoNotaOfNotaListNota = em.merge(oldIdTipoNotaOfNotaListNota);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipoNota(tipoNota.getIdTipoNota()) != null) {
                throw new PreexistingEntityException("TipoNota " + tipoNota + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoNota tipoNota) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            TipoNota persistentTipoNota = em.find(TipoNota.class, tipoNota.getIdTipoNota());
            List<Nota> notaListOld = persistentTipoNota.getNotaList();
            List<Nota> notaListNew = tipoNota.getNotaList();
            List<Nota> attachedNotaListNew = new ArrayList<Nota>();
            if (notaListNew != null) {
                for (Nota notaListNewNotaToAttach : notaListNew) {
                    notaListNewNotaToAttach = em.getReference(notaListNewNotaToAttach.getClass(), notaListNewNotaToAttach.getIdNota());
                    attachedNotaListNew.add(notaListNewNotaToAttach);
                }
                notaListNew = attachedNotaListNew;
                tipoNota.setNotaList(notaListNew);
            }

            tipoNota = em.merge(tipoNota);

            if (notaListOld != null && notaListNew != null) {
                for (Nota notaListOldNota : notaListOld) {
                    if (!notaListNew.contains(notaListOldNota)) {
                        notaListOldNota.setTipoNota(null);
                        notaListOldNota = em.merge(notaListOldNota);
                    }
                }
                for (Nota notaListNewNota : notaListNew) {
                    if (!notaListOld.contains(notaListNewNota)) {
                        TipoNota oldIdTipoNotaOfNotaListNewNota = notaListNewNota.getTipoNota();
                        notaListNewNota.setTipoNota(tipoNota);
                        notaListNewNota = em.merge(notaListNewNota);
                        if (oldIdTipoNotaOfNotaListNewNota != null && !oldIdTipoNotaOfNotaListNewNota.equals(tipoNota)) {
                            oldIdTipoNotaOfNotaListNewNota.getNotaList().remove(notaListNewNota);
                            oldIdTipoNotaOfNotaListNewNota = em.merge(oldIdTipoNotaOfNotaListNewNota);
                        }
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
                Integer id = tipoNota.getIdTipoNota();
                if (findTipoNota(id) == null) {
                    throw new NonexistentEntityException("The tipoNota with id " + id + " no longer exists.");
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
            TipoNota tipoNota;
            try {
                tipoNota = em.getReference(TipoNota.class, id);
                tipoNota.getIdTipoNota();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoNota with id " + id + " no longer exists.", enfe);
            }
            List<Nota> notaList = tipoNota.getNotaList();
            for (Nota notaListNota : notaList) {
                notaListNota.setTipoNota(null);
                notaListNota = em.merge(notaListNota);
            }
            em.remove(tipoNota);
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

    public List<TipoNota> findTipoNotaEntities() {
        return findTipoNotaEntities(true, -1, -1);
    }

    public List<TipoNota> findTipoNotaEntities(int maxResults, int firstResult) {
        return findTipoNotaEntities(false, maxResults, firstResult);
    }

    private List<TipoNota> findTipoNotaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoNota.class));
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

    public TipoNota findTipoNota(Integer id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(TipoNota.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoNotaCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoNota> rt = cq.from(TipoNota.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
