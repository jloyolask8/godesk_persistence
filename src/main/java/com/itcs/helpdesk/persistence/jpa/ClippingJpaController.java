/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import com.itcs.helpdesk.persistence.entities.Area;
import com.itcs.helpdesk.persistence.entities.Clipping;
import com.itcs.helpdesk.persistence.entities.FiltroVista;
import com.itcs.helpdesk.persistence.entities.Grupo;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.PreexistingEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.eclipse.persistence.config.EntityManagerProperties;

/**
 *
 * @author jonathan
 */
public class ClippingJpaController extends AbstractJPAController implements Serializable {

    public ClippingJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }

//    public ClippingJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }
    public void create(Clipping clipping) throws PreexistingEntityException, Exception {
        if (clipping.getClippingList() == null) {
            clipping.setClippingList(new ArrayList<Clipping>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());

            Grupo idGrupo = clipping.getIdGrupo();
            if (idGrupo != null) {
                idGrupo = em.getReference(idGrupo.getClass(), idGrupo.getIdGrupo());
                clipping.setIdGrupo(idGrupo);
            }
            Clipping idClippingPadre = clipping.getIdClippingPadre();
            if (idClippingPadre != null) {
                idClippingPadre = em.getReference(idClippingPadre.getClass(), idClippingPadre.getIdClipping());
                clipping.setIdClippingPadre(idClippingPadre);
            }
            Area idArea = clipping.getIdArea();
            if (idArea != null) {
                idArea = em.getReference(idArea.getClass(), idArea.getIdArea());
                clipping.setIdArea(idArea);
            }
            List<Clipping> attachedClippingList = new ArrayList<Clipping>();
            for (Clipping clippingListClippingToAttach : clipping.getClippingList()) {
                clippingListClippingToAttach = em.getReference(clippingListClippingToAttach.getClass(), clippingListClippingToAttach.getIdClipping());
                attachedClippingList.add(clippingListClippingToAttach);
            }
            clipping.setClippingList(attachedClippingList);
            em.persist(clipping);
            if (idGrupo != null) {
                idGrupo.getClippingList().add(clipping);
                idGrupo = em.merge(idGrupo);
            }
            if (idClippingPadre != null) {
                idClippingPadre.getClippingList().add(clipping);
                idClippingPadre = em.merge(idClippingPadre);
            }
            if (idArea != null) {
                idArea.getClippingList().add(clipping);
                idArea = em.merge(idArea);
            }
            for (Clipping clippingListClipping : clipping.getClippingList()) {
                Clipping oldIdClippingPadreOfClippingListClipping = clippingListClipping.getIdClippingPadre();
                clippingListClipping.setIdClippingPadre(clipping);
                clippingListClipping = em.merge(clippingListClipping);
                if (oldIdClippingPadreOfClippingListClipping != null) {
                    oldIdClippingPadreOfClippingListClipping.getClippingList().remove(clippingListClipping);
                    oldIdClippingPadreOfClippingListClipping = em.merge(oldIdClippingPadreOfClippingListClipping);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            if (findClipping(clipping.getIdClipping()) != null) {
                throw new PreexistingEntityException("AppSetting " + clipping + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clipping clipping) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Clipping persistentClipping = em.find(Clipping.class, clipping.getIdClipping());
            Grupo idGrupoOld = persistentClipping.getIdGrupo();
            Grupo idGrupoNew = clipping.getIdGrupo();
            Clipping idClippingPadreOld = persistentClipping.getIdClippingPadre();
            Clipping idClippingPadreNew = clipping.getIdClippingPadre();
            Area idAreaOld = persistentClipping.getIdArea();
            Area idAreaNew = clipping.getIdArea();
            List<Clipping> clippingListOld = persistentClipping.getClippingList();
            List<Clipping> clippingListNew = clipping.getClippingList();
            if (idGrupoNew != null) {
                idGrupoNew = em.getReference(idGrupoNew.getClass(), idGrupoNew.getIdGrupo());
                clipping.setIdGrupo(idGrupoNew);
            }
            if (idClippingPadreNew != null) {
                idClippingPadreNew = em.getReference(idClippingPadreNew.getClass(), idClippingPadreNew.getIdClipping());
                clipping.setIdClippingPadre(idClippingPadreNew);
            }
            if (idAreaNew != null) {
                idAreaNew = em.getReference(idAreaNew.getClass(), idAreaNew.getIdArea());
                clipping.setIdArea(idAreaNew);
            }
            List<Clipping> attachedClippingListNew = new ArrayList<Clipping>();
            for (Clipping clippingListNewClippingToAttach : clippingListNew) {
                clippingListNewClippingToAttach = em.getReference(clippingListNewClippingToAttach.getClass(), clippingListNewClippingToAttach.getIdClipping());
                attachedClippingListNew.add(clippingListNewClippingToAttach);
            }
            clippingListNew = attachedClippingListNew;
            clipping.setClippingList(clippingListNew);
            clipping = em.merge(clipping);
            if (idGrupoOld != null && !idGrupoOld.equals(idGrupoNew)) {
                idGrupoOld.getClippingList().remove(clipping);
                idGrupoOld = em.merge(idGrupoOld);
            }
            if (idGrupoNew != null && !idGrupoNew.equals(idGrupoOld)) {
                idGrupoNew.getClippingList().add(clipping);
                idGrupoNew = em.merge(idGrupoNew);
            }
            if (idClippingPadreOld != null && !idClippingPadreOld.equals(idClippingPadreNew)) {
                idClippingPadreOld.getClippingList().remove(clipping);
                idClippingPadreOld = em.merge(idClippingPadreOld);
            }
            if (idClippingPadreNew != null && !idClippingPadreNew.equals(idClippingPadreOld)) {
                idClippingPadreNew.getClippingList().add(clipping);
                idClippingPadreNew = em.merge(idClippingPadreNew);
            }
            if (idAreaOld != null && !idAreaOld.equals(idAreaNew)) {
                idAreaOld.getClippingList().remove(clipping);
                idAreaOld = em.merge(idAreaOld);
            }
            if (idAreaNew != null && !idAreaNew.equals(idAreaOld)) {
                idAreaNew.getClippingList().add(clipping);
                idAreaNew = em.merge(idAreaNew);
            }
            for (Clipping clippingListOldClipping : clippingListOld) {
                if (!clippingListNew.contains(clippingListOldClipping)) {
                    clippingListOldClipping.setIdClippingPadre(null);
                    clippingListOldClipping = em.merge(clippingListOldClipping);
                }
            }
            for (Clipping clippingListNewClipping : clippingListNew) {
                if (!clippingListOld.contains(clippingListNewClipping)) {
                    Clipping oldIdClippingPadreOfClippingListNewClipping = clippingListNewClipping.getIdClippingPadre();
                    clippingListNewClipping.setIdClippingPadre(clipping);
                    clippingListNewClipping = em.merge(clippingListNewClipping);
                    if (oldIdClippingPadreOfClippingListNewClipping != null && !oldIdClippingPadreOfClippingListNewClipping.equals(clipping)) {
                        oldIdClippingPadreOfClippingListNewClipping.getClippingList().remove(clippingListNewClipping);
                        oldIdClippingPadreOfClippingListNewClipping = em.merge(oldIdClippingPadreOfClippingListNewClipping);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clipping.getIdClipping();
                if (findClipping(id) == null) {
                    throw new NonexistentEntityException("The clipping with id " + id + " no longer exists.");
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
            Clipping clipping;
            try {
                clipping = em.getReference(Clipping.class, id);
                clipping.getIdClipping();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clipping with id " + id + " no longer exists.", enfe);
            }
            Grupo idGrupo = clipping.getIdGrupo();
            if (idGrupo != null) {
                idGrupo.getClippingList().remove(clipping);
                idGrupo = em.merge(idGrupo);
            }
            Clipping idClippingPadre = clipping.getIdClippingPadre();
            if (idClippingPadre != null) {
                idClippingPadre.getClippingList().remove(clipping);
                idClippingPadre = em.merge(idClippingPadre);
            }
            Area idArea = clipping.getIdArea();
            if (idArea != null) {
                idArea.getClippingList().remove(clipping);
                idArea = em.merge(idArea);
            }
            List<Clipping> clippingList = clipping.getClippingList();
            for (Clipping clippingListClipping : clippingList) {
                clippingListClipping.setIdClippingPadre(null);
                clippingListClipping = em.merge(clippingListClipping);
            }
            em.remove(clipping);
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

    public List<Clipping> findClippingEntities() {
        return findClippingEntities(true, -1, -1);
    }

    public List<Clipping> findClippingEntities(int maxResults, int firstResult) {
        return findClippingEntities(false, maxResults, firstResult);
    }

    /**
     * we should implement a generic jpa controller!
     *
     * @param predicate
     * @param all
     * @param maxResults
     * @param firstResult
     * @return
     */
    public List<Clipping> selectRootClippings() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Clipping> criteriaQuery = criteriaBuilder.createQuery(Clipping.class);
            Root<Clipping> root = criteriaQuery.from(Clipping.class);

            Predicate predicate = criteriaBuilder.isNull(root.get("idClippingPadre"));

            criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.asc(root.get("orden")));

            Query q = em.createQuery(criteriaQuery.where(predicate));

            return q.getResultList();
        } finally {
            em.close();
        }
    }

    private List<Clipping> findClippingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clipping.class));
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

//////    public List<Clipping> findClippingEntities(Vista vista, int maxResults, int firstResult) throws NotSupportedException, ClassNotFoundException {
//////        return findClippingEntities(vista, true, -1, -1);
//////    }
//////
//////    private List<Clipping> findClippingEntities(Vista vista, boolean all, int maxResults, int firstResult) throws NotSupportedException, ClassNotFoundException {
//////        EntityManager em = getEntityManager(); em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, schema);
//////        try {
//////            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//////            CriteriaQuery<Clipping> criteriaQuery = criteriaBuilder.createQuery(Clipping.class);
//////            Root<Clipping> root = criteriaQuery.from(Clipping.class);
//////
////////            CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery();
////////            criteriaQuery.select(criteriaQuery.from(Clipping.class));
////////           
//////
//////            Predicate predicate = createPredicate(em, criteriaBuilder, root, vista, null);
//////
//////
//////            if (predicate != null) {
//////                //return criteriaQuery.orderBy(criteriaBuilder.desc(root.get("fechaCreacion"))).where(predicate);
//////                criteriaQuery.where(predicate);
//////            }
//////            Query q = em.createQuery(criteriaQuery);
//////            if (!all) {
//////                q.setMaxResults(maxResults);
//////                q.setFirstResult(firstResult);
//////            }
//////            return q.getResultList();
//////
//////        } finally {
//////            em.close();
//////        }
//////    }
    public Clipping findClipping(Integer id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Clipping.class, id);
        } finally {
            em.close();
        }
    }

    public int getClippingCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clipping> rt = cq.from(Clipping.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @Override
    protected Predicate createSpecialPredicate(FiltroVista filtro) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected boolean isThereSpecialFiltering(FiltroVista filtro) {
        return false;//nothing special yet
    }
}
