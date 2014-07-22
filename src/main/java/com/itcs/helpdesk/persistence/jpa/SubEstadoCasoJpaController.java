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
import com.itcs.helpdesk.persistence.entities.EstadoCaso;
import java.util.ArrayList;
import java.util.List;
import com.itcs.helpdesk.persistence.entities.Caso;
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
public class SubEstadoCasoJpaController extends AbstractJPAController implements Serializable {

    public SubEstadoCasoJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class SubEstadoCasoJpaController implements Serializable {
//
//    public SubEstadoCasoJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(SubEstadoCaso subEstadoCaso) throws PreexistingEntityException, RollbackFailureException, Exception {
//        if (subEstadoCaso.getCategoriaList() == null) {
//            subEstadoCaso.setCategoriaList(new ArrayList<Categoria>());
//        }
        if (subEstadoCaso.getCasoList() == null) {
            subEstadoCaso.setCasoList(new ArrayList<Caso>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            EstadoCaso idEstado = subEstadoCaso.getIdEstado();
            if (idEstado != null) {
                idEstado = em.getReference(idEstado.getClass(), idEstado.getIdEstado());
                subEstadoCaso.setIdEstado(idEstado);
            }
//            List<Categoria> attachedCategoriaList = new ArrayList<Categoria>();
//            for (Categoria categoriaListCategoriaToAttach : subEstadoCaso.getCategoriaList()) {
//                categoriaListCategoriaToAttach = em.getReference(categoriaListCategoriaToAttach.getClass(), categoriaListCategoriaToAttach.getIdCategoria());
//                attachedCategoriaList.add(categoriaListCategoriaToAttach);
//            }
//            subEstadoCaso.setCategoriaList(attachedCategoriaList);
            List<Caso> attachedCasoList = new ArrayList<Caso>();
            for (Caso casoListCasoToAttach : subEstadoCaso.getCasoList()) {
                casoListCasoToAttach = em.getReference(casoListCasoToAttach.getClass(), casoListCasoToAttach.getIdCaso());
                attachedCasoList.add(casoListCasoToAttach);
            }
            subEstadoCaso.setCasoList(attachedCasoList);
            em.persist(subEstadoCaso);
            if (idEstado != null) {
                idEstado.getSubEstadoCasoList().add(subEstadoCaso);
                idEstado = em.merge(idEstado);
            }
//            for (Categoria categoriaListCategoria : subEstadoCaso.getCategoriaList()) {
//                categoriaListCategoria.getSubEstadoCasoList().add(subEstadoCaso);
//                categoriaListCategoria = em.merge(categoriaListCategoria);
//            }
            for (Caso casoListCaso : subEstadoCaso.getCasoList()) {
                SubEstadoCaso oldIdSubEstadoOfCasoListCaso = casoListCaso.getIdSubEstado();
                casoListCaso.setIdSubEstado(subEstadoCaso);
                casoListCaso = em.merge(casoListCaso);
                if (oldIdSubEstadoOfCasoListCaso != null) {
                    oldIdSubEstadoOfCasoListCaso.getCasoList().remove(casoListCaso);
                    oldIdSubEstadoOfCasoListCaso = em.merge(oldIdSubEstadoOfCasoListCaso);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSubEstadoCaso(subEstadoCaso.getIdSubEstado()) != null) {
                throw new PreexistingEntityException("SubEstadoCaso " + subEstadoCaso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SubEstadoCaso subEstadoCaso) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            SubEstadoCaso persistentSubEstadoCaso = em.find(SubEstadoCaso.class, subEstadoCaso.getIdSubEstado());
            EstadoCaso idEstadoOld = persistentSubEstadoCaso.getIdEstado();
            EstadoCaso idEstadoNew = subEstadoCaso.getIdEstado();
//            List<Categoria> categoriaListOld = persistentSubEstadoCaso.getCategoriaList();
//            List<Categoria> categoriaListNew = subEstadoCaso.getCategoriaList();
            List<Caso> casoListOld = persistentSubEstadoCaso.getCasoList();
            List<Caso> casoListNew = subEstadoCaso.getCasoList();
            if (idEstadoNew != null) {
                idEstadoNew = em.getReference(idEstadoNew.getClass(), idEstadoNew.getIdEstado());
                subEstadoCaso.setIdEstado(idEstadoNew);
            }
//            List<Categoria> attachedCategoriaListNew = new ArrayList<Categoria>();
//            for (Categoria categoriaListNewCategoriaToAttach : categoriaListNew) {
//                categoriaListNewCategoriaToAttach = em.getReference(categoriaListNewCategoriaToAttach.getClass(), categoriaListNewCategoriaToAttach.getIdCategoria());
//                attachedCategoriaListNew.add(categoriaListNewCategoriaToAttach);
//            }
//            categoriaListNew = attachedCategoriaListNew;
//            subEstadoCaso.setCategoriaList(categoriaListNew);
//            List<Caso> attachedCasoListNew = new ArrayList<Caso>();
//            for (Caso casoListNewCasoToAttach : casoListNew) {
//                casoListNewCasoToAttach = em.getReference(casoListNewCasoToAttach.getClass(), casoListNewCasoToAttach.getIdCaso());
//                attachedCasoListNew.add(casoListNewCasoToAttach);
//            }
//            casoListNew = attachedCasoListNew;
            subEstadoCaso.setCasoList(casoListNew);
            subEstadoCaso = em.merge(subEstadoCaso);
            if (idEstadoOld != null && !idEstadoOld.equals(idEstadoNew)) {
                idEstadoOld.getSubEstadoCasoList().remove(subEstadoCaso);
                idEstadoOld = em.merge(idEstadoOld);
            }
            if (idEstadoNew != null && !idEstadoNew.equals(idEstadoOld)) {
                idEstadoNew.getSubEstadoCasoList().add(subEstadoCaso);
                idEstadoNew = em.merge(idEstadoNew);
            }
//            for (Categoria categoriaListOldCategoria : categoriaListOld) {
//                if (!categoriaListNew.contains(categoriaListOldCategoria)) {
//                    categoriaListOldCategoria.getSubEstadoCasoList().remove(subEstadoCaso);
//                    categoriaListOldCategoria = em.merge(categoriaListOldCategoria);
//                }
//            }
//            for (Categoria categoriaListNewCategoria : categoriaListNew) {
//                if (!categoriaListOld.contains(categoriaListNewCategoria)) {
//                    categoriaListNewCategoria.getSubEstadoCasoList().add(subEstadoCaso);
//                    categoriaListNewCategoria = em.merge(categoriaListNewCategoria);
//                }
//            }
            for (Caso casoListOldCaso : casoListOld) {
                if (!casoListNew.contains(casoListOldCaso)) {
                    casoListOldCaso.setIdSubEstado(null);
                    casoListOldCaso = em.merge(casoListOldCaso);
                }
            }
            for (Caso casoListNewCaso : casoListNew) {
                if (!casoListOld.contains(casoListNewCaso)) {
                    SubEstadoCaso oldIdSubEstadoOfCasoListNewCaso = casoListNewCaso.getIdSubEstado();
                    casoListNewCaso.setIdSubEstado(subEstadoCaso);
                    casoListNewCaso = em.merge(casoListNewCaso);
                    if (oldIdSubEstadoOfCasoListNewCaso != null && !oldIdSubEstadoOfCasoListNewCaso.equals(subEstadoCaso)) {
                        oldIdSubEstadoOfCasoListNewCaso.getCasoList().remove(casoListNewCaso);
                        oldIdSubEstadoOfCasoListNewCaso = em.merge(oldIdSubEstadoOfCasoListNewCaso);
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
                String id = subEstadoCaso.getIdSubEstado();
                if (findSubEstadoCaso(id) == null) {
                    throw new NonexistentEntityException("The subEstadoCaso with id " + id + " no longer exists.");
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
            SubEstadoCaso subEstadoCaso;
            try {
                subEstadoCaso = em.getReference(SubEstadoCaso.class, id);
                subEstadoCaso.getIdSubEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subEstadoCaso with id " + id + " no longer exists.", enfe);
            }
            EstadoCaso idEstado = subEstadoCaso.getIdEstado();
            if (idEstado != null) {
                idEstado.getSubEstadoCasoList().remove(subEstadoCaso);
                idEstado = em.merge(idEstado);
            }
//            List<Categoria> categoriaList = subEstadoCaso.getCategoriaList();
//            for (Categoria categoriaListCategoria : categoriaList) {
//                categoriaListCategoria.getSubEstadoCasoList().remove(subEstadoCaso);
//                categoriaListCategoria = em.merge(categoriaListCategoria);
//            }
            List<Caso> casoList = subEstadoCaso.getCasoList();
            for (Caso casoListCaso : casoList) {
                casoListCaso.setIdSubEstado(null);
                casoListCaso = em.merge(casoListCaso);
            }
            em.remove(subEstadoCaso);
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

    public List<SubEstadoCaso> findSubEstadoCasoEntities() {
        return findSubEstadoCasoEntities(true, -1, -1);
    }

    public List<SubEstadoCaso> findSubEstadoCasoEntities(int maxResults, int firstResult) {
        return findSubEstadoCasoEntities(false, maxResults, firstResult);
    }

    private List<SubEstadoCaso> findSubEstadoCasoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SubEstadoCaso.class));
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

    public SubEstadoCaso findSubEstadoCaso(String id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(SubEstadoCaso.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubEstadoCasoCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SubEstadoCaso> rt = cq.from(SubEstadoCaso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
