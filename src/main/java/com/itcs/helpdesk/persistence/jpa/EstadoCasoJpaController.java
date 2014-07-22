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
import com.itcs.helpdesk.persistence.entities.EstadoCaso;
import java.util.ArrayList;
import java.util.List;
import com.itcs.helpdesk.persistence.entities.SubEstadoCaso;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.PreexistingEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.RollbackFailureException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.eclipse.persistence.config.EntityManagerProperties;

/**
 *
 * @author jonathan
 */
public class EstadoCasoJpaController extends AbstractJPAController implements Serializable {

    public EstadoCasoJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class EstadoCasoJpaController implements Serializable {
//
//    public EstadoCasoJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(EstadoCaso estadoCaso) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (estadoCaso.getCasoList() == null) {
            estadoCaso.setCasoList(new ArrayList<Caso>());
        }
        if (estadoCaso.getSubEstadoCasoList() == null) {
            estadoCaso.setSubEstadoCasoList(new ArrayList<SubEstadoCaso>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            List<Caso> attachedCasoList = new ArrayList<Caso>();
            for (Caso casoListCasoToAttach : estadoCaso.getCasoList()) {
                casoListCasoToAttach = em.getReference(casoListCasoToAttach.getClass(), casoListCasoToAttach.getIdCaso());
                attachedCasoList.add(casoListCasoToAttach);
            }
            estadoCaso.setCasoList(attachedCasoList);
            List<SubEstadoCaso> attachedSubEstadoCasoList = new ArrayList<SubEstadoCaso>();
            for (SubEstadoCaso subEstadoCasoListSubEstadoCasoToAttach : estadoCaso.getSubEstadoCasoList()) {
                subEstadoCasoListSubEstadoCasoToAttach = em.getReference(subEstadoCasoListSubEstadoCasoToAttach.getClass(), subEstadoCasoListSubEstadoCasoToAttach.getIdSubEstado());
                attachedSubEstadoCasoList.add(subEstadoCasoListSubEstadoCasoToAttach);
            }
            estadoCaso.setSubEstadoCasoList(attachedSubEstadoCasoList);
            em.persist(estadoCaso);
            for (Caso casoListCaso : estadoCaso.getCasoList()) {
                EstadoCaso oldIdEstadoOfCasoListCaso = casoListCaso.getIdEstado();
                casoListCaso.setIdEstado(estadoCaso);
                casoListCaso = em.merge(casoListCaso);
                if (oldIdEstadoOfCasoListCaso != null) {
                    oldIdEstadoOfCasoListCaso.getCasoList().remove(casoListCaso);
                    oldIdEstadoOfCasoListCaso = em.merge(oldIdEstadoOfCasoListCaso);
                }
            }
            for (SubEstadoCaso subEstadoCasoListSubEstadoCaso : estadoCaso.getSubEstadoCasoList()) {
                EstadoCaso oldIdEstadoOfSubEstadoCasoListSubEstadoCaso = subEstadoCasoListSubEstadoCaso.getIdEstado();
                subEstadoCasoListSubEstadoCaso.setIdEstado(estadoCaso);
                subEstadoCasoListSubEstadoCaso = em.merge(subEstadoCasoListSubEstadoCaso);
                if (oldIdEstadoOfSubEstadoCasoListSubEstadoCaso != null) {
                    oldIdEstadoOfSubEstadoCasoListSubEstadoCaso.getSubEstadoCasoList().remove(subEstadoCasoListSubEstadoCaso);
                    oldIdEstadoOfSubEstadoCasoListSubEstadoCaso = em.merge(oldIdEstadoOfSubEstadoCasoListSubEstadoCaso);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEstadoCaso(estadoCaso.getIdEstado()) != null) {
                throw new PreexistingEntityException("EstadoCaso " + estadoCaso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoCaso estadoCaso) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            EstadoCaso persistentEstadoCaso = em.find(EstadoCaso.class, estadoCaso.getIdEstado());
            List<Caso> casoListOld = persistentEstadoCaso.getCasoList();
            List<Caso> casoListNew = estadoCaso.getCasoList();
            List<SubEstadoCaso> subEstadoCasoListOld = persistentEstadoCaso.getSubEstadoCasoList();
            List<SubEstadoCaso> subEstadoCasoListNew = estadoCaso.getSubEstadoCasoList();
            List<Caso> attachedCasoListNew = new ArrayList<Caso>();
            for (Caso casoListNewCasoToAttach : casoListNew) {
                casoListNewCasoToAttach = em.getReference(casoListNewCasoToAttach.getClass(), casoListNewCasoToAttach.getIdCaso());
                attachedCasoListNew.add(casoListNewCasoToAttach);
            }
            casoListNew = attachedCasoListNew;
            estadoCaso.setCasoList(casoListNew);
            List<SubEstadoCaso> attachedSubEstadoCasoListNew = new ArrayList<SubEstadoCaso>();
            for (SubEstadoCaso subEstadoCasoListNewSubEstadoCasoToAttach : subEstadoCasoListNew) {
                subEstadoCasoListNewSubEstadoCasoToAttach = em.getReference(subEstadoCasoListNewSubEstadoCasoToAttach.getClass(), subEstadoCasoListNewSubEstadoCasoToAttach.getIdSubEstado());
                attachedSubEstadoCasoListNew.add(subEstadoCasoListNewSubEstadoCasoToAttach);
            }
            subEstadoCasoListNew = attachedSubEstadoCasoListNew;
            estadoCaso.setSubEstadoCasoList(subEstadoCasoListNew);
            estadoCaso = em.merge(estadoCaso);
            for (Caso casoListOldCaso : casoListOld) {
                if (!casoListNew.contains(casoListOldCaso)) {
                    casoListOldCaso.setIdEstado(null);
                    casoListOldCaso = em.merge(casoListOldCaso);
                }
            }
            for (Caso casoListNewCaso : casoListNew) {
                if (!casoListOld.contains(casoListNewCaso)) {
                    EstadoCaso oldIdEstadoOfCasoListNewCaso = casoListNewCaso.getIdEstado();
                    casoListNewCaso.setIdEstado(estadoCaso);
                    casoListNewCaso = em.merge(casoListNewCaso);
                    if (oldIdEstadoOfCasoListNewCaso != null && !oldIdEstadoOfCasoListNewCaso.equals(estadoCaso)) {
                        oldIdEstadoOfCasoListNewCaso.getCasoList().remove(casoListNewCaso);
                        oldIdEstadoOfCasoListNewCaso = em.merge(oldIdEstadoOfCasoListNewCaso);
                    }
                }
            }
            for (SubEstadoCaso subEstadoCasoListOldSubEstadoCaso : subEstadoCasoListOld) {
                if (!subEstadoCasoListNew.contains(subEstadoCasoListOldSubEstadoCaso)) {
                    subEstadoCasoListOldSubEstadoCaso.setIdEstado(null);
                    subEstadoCasoListOldSubEstadoCaso = em.merge(subEstadoCasoListOldSubEstadoCaso);
                }
            }
            for (SubEstadoCaso subEstadoCasoListNewSubEstadoCaso : subEstadoCasoListNew) {
                if (!subEstadoCasoListOld.contains(subEstadoCasoListNewSubEstadoCaso)) {
                    EstadoCaso oldIdEstadoOfSubEstadoCasoListNewSubEstadoCaso = subEstadoCasoListNewSubEstadoCaso.getIdEstado();
                    subEstadoCasoListNewSubEstadoCaso.setIdEstado(estadoCaso);
                    subEstadoCasoListNewSubEstadoCaso = em.merge(subEstadoCasoListNewSubEstadoCaso);
                    if (oldIdEstadoOfSubEstadoCasoListNewSubEstadoCaso != null && !oldIdEstadoOfSubEstadoCasoListNewSubEstadoCaso.equals(estadoCaso)) {
                        oldIdEstadoOfSubEstadoCasoListNewSubEstadoCaso.getSubEstadoCasoList().remove(subEstadoCasoListNewSubEstadoCaso);
                        oldIdEstadoOfSubEstadoCasoListNewSubEstadoCaso = em.merge(oldIdEstadoOfSubEstadoCasoListNewSubEstadoCaso);
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
                String id = estadoCaso.getIdEstado();
                if (findEstadoCaso(id) == null) {
                    throw new NonexistentEntityException("The estadoCaso with id " + id + " no longer exists.");
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
            EstadoCaso estadoCaso;
            try {
                estadoCaso = em.getReference(EstadoCaso.class, id);
                estadoCaso.getIdEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoCaso with id " + id + " no longer exists.", enfe);
            }
            List<Caso> casoList = estadoCaso.getCasoList();
            for (Caso casoListCaso : casoList) {
                casoListCaso.setIdEstado(null);
                casoListCaso = em.merge(casoListCaso);
            }
            List<SubEstadoCaso> subEstadoCasoList = estadoCaso.getSubEstadoCasoList();
            for (SubEstadoCaso subEstadoCasoListSubEstadoCaso : subEstadoCasoList) {
                subEstadoCasoListSubEstadoCaso.setIdEstado(null);
                subEstadoCasoListSubEstadoCaso = em.merge(subEstadoCasoListSubEstadoCaso);
            }
            em.remove(estadoCaso);
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

    public List<EstadoCaso> findEstadoCasoEntities() {
        return findEstadoCasoEntities(true, -1, -1);
    }

    public List<EstadoCaso> findEstadoCasoEntities(int maxResults, int firstResult) {
        return findEstadoCasoEntities(false, maxResults, firstResult);
    }

    private List<EstadoCaso> findEstadoCasoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoCaso.class));
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

    public EstadoCaso findEstadoCaso(String id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(EstadoCaso.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoCasoCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoCaso> rt = cq.from(EstadoCaso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
