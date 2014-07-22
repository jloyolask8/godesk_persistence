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
import com.itcs.helpdesk.persistence.entities.TipoAlerta;
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
public class TipoAlertaJpaController extends AbstractJPAController implements Serializable {

    public TipoAlertaJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class TipoAlertaJpaController implements Serializable {
//
//    public TipoAlertaJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(TipoAlerta tipoAlerta) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tipoAlerta.getCasoList() == null) {
            tipoAlerta.setCasoList(new ArrayList<Caso>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            List<Caso> attachedCasoList = new ArrayList<Caso>();
            for (Caso casoListCasoToAttach : tipoAlerta.getCasoList()) {
                casoListCasoToAttach = em.getReference(casoListCasoToAttach.getClass(), casoListCasoToAttach.getIdCaso());
                attachedCasoList.add(casoListCasoToAttach);
            }
            tipoAlerta.setCasoList(attachedCasoList);
            em.persist(tipoAlerta);
            for (Caso casoListCaso : tipoAlerta.getCasoList()) {
                TipoAlerta oldEstadoAlertaOfCasoListCaso = casoListCaso.getEstadoAlerta();
                casoListCaso.setEstadoAlerta(tipoAlerta);
                casoListCaso = em.merge(casoListCaso);
                if (oldEstadoAlertaOfCasoListCaso != null) {
                    oldEstadoAlertaOfCasoListCaso.getCasoList().remove(casoListCaso);
                    oldEstadoAlertaOfCasoListCaso = em.merge(oldEstadoAlertaOfCasoListCaso);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipoAlerta(tipoAlerta.getIdalerta()) != null) {
                throw new PreexistingEntityException("TipoAlerta " + tipoAlerta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoAlerta tipoAlerta) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            TipoAlerta persistentTipoAlerta = em.find(TipoAlerta.class, tipoAlerta.getIdalerta());
            List<Caso> casoListOld = persistentTipoAlerta.getCasoList();
            List<Caso> casoListNew = tipoAlerta.getCasoList();
            List<Caso> attachedCasoListNew = new ArrayList<Caso>();
            for (Caso casoListNewCasoToAttach : casoListNew) {
                casoListNewCasoToAttach = em.getReference(casoListNewCasoToAttach.getClass(), casoListNewCasoToAttach.getIdCaso());
                attachedCasoListNew.add(casoListNewCasoToAttach);
            }
            casoListNew = attachedCasoListNew;
            tipoAlerta.setCasoList(casoListNew);
            tipoAlerta = em.merge(tipoAlerta);
            for (Caso casoListOldCaso : casoListOld) {
                if (!casoListNew.contains(casoListOldCaso)) {
                    casoListOldCaso.setEstadoAlerta(null);
                    casoListOldCaso = em.merge(casoListOldCaso);
                }
            }
            for (Caso casoListNewCaso : casoListNew) {
                if (!casoListOld.contains(casoListNewCaso)) {
                    TipoAlerta oldEstadoAlertaOfCasoListNewCaso = casoListNewCaso.getEstadoAlerta();
                    casoListNewCaso.setEstadoAlerta(tipoAlerta);
                    casoListNewCaso = em.merge(casoListNewCaso);
                    if (oldEstadoAlertaOfCasoListNewCaso != null && !oldEstadoAlertaOfCasoListNewCaso.equals(tipoAlerta)) {
                        oldEstadoAlertaOfCasoListNewCaso.getCasoList().remove(casoListNewCaso);
                        oldEstadoAlertaOfCasoListNewCaso = em.merge(oldEstadoAlertaOfCasoListNewCaso);
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
                Integer id = tipoAlerta.getIdalerta();
                if (findTipoAlerta(id) == null) {
                    throw new NonexistentEntityException("The tipoAlerta with id " + id + " no longer exists.");
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
            TipoAlerta tipoAlerta;
            try {
                tipoAlerta = em.getReference(TipoAlerta.class, id);
                tipoAlerta.getIdalerta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoAlerta with id " + id + " no longer exists.", enfe);
            }
            List<Caso> casoList = tipoAlerta.getCasoList();
            for (Caso casoListCaso : casoList) {
                casoListCaso.setEstadoAlerta(null);
                casoListCaso = em.merge(casoListCaso);
            }
            em.remove(tipoAlerta);
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

    public List<TipoAlerta> findTipoAlertaEntities() {
        return findTipoAlertaEntities(true, -1, -1);
    }

    public List<TipoAlerta> findTipoAlertaEntities(int maxResults, int firstResult) {
        return findTipoAlertaEntities(false, maxResults, firstResult);
    }

    private List<TipoAlerta> findTipoAlertaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoAlerta.class));
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

    public TipoAlerta findTipoAlerta(Integer id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(TipoAlerta.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoAlertaCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoAlerta> rt = cq.from(TipoAlerta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
