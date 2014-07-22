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
import com.itcs.helpdesk.persistence.entities.Categoria;
import com.itcs.helpdesk.persistence.entities.Area;
import java.util.ArrayList;
import java.util.List;
import com.itcs.helpdesk.persistence.entities.Grupo;
import com.itcs.helpdesk.persistence.entities.Caso;
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
public class CategoriaJpaController extends AbstractJPAController implements Serializable {

    public CategoriaJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class CategoriaJpaController implements Serializable {
//
//    public CategoriaJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(Categoria categoria) throws PreexistingEntityException, RollbackFailureException, Exception {
//        if (categoria.getSubEstadoCasoList() == null) {
//            categoria.setSubEstadoCasoList(new ArrayList<SubEstadoCaso>());
//        }
        if (categoria.getGrupoList() == null) {
            categoria.setGrupoList(new ArrayList<Grupo>());
        }
        if (categoria.getCasoList() == null) {
            categoria.setCasoList(new ArrayList<Caso>());
        }
        if (categoria.getCategoriaList() == null) {
            categoria.setCategoriaList(new ArrayList<Categoria>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Categoria idCategoriaPadre = categoria.getIdCategoriaPadre();
            if (idCategoriaPadre != null) {
                idCategoriaPadre = em.getReference(idCategoriaPadre.getClass(), idCategoriaPadre.getIdCategoria());
                categoria.setIdCategoriaPadre(idCategoriaPadre);
            }
            Area idArea = categoria.getIdArea();
            if (idArea != null) {
                idArea = em.getReference(idArea.getClass(), idArea.getIdArea());
                categoria.setIdArea(idArea);
            }
//            List<SubEstadoCaso> attachedSubEstadoCasoList = new ArrayList<SubEstadoCaso>();
//            for (SubEstadoCaso subEstadoCasoListSubEstadoCasoToAttach : categoria.getSubEstadoCasoList()) {
//                subEstadoCasoListSubEstadoCasoToAttach = em.getReference(subEstadoCasoListSubEstadoCasoToAttach.getClass(), subEstadoCasoListSubEstadoCasoToAttach.getIdSubEstado());
//                attachedSubEstadoCasoList.add(subEstadoCasoListSubEstadoCasoToAttach);
//            }
//            categoria.setSubEstadoCasoList(attachedSubEstadoCasoList);
            List<Grupo> attachedGrupoList = new ArrayList<Grupo>();
            for (Grupo grupoListGrupoToAttach : categoria.getGrupoList()) {
                grupoListGrupoToAttach = em.getReference(grupoListGrupoToAttach.getClass(), grupoListGrupoToAttach.getIdGrupo());
                attachedGrupoList.add(grupoListGrupoToAttach);
            }
            categoria.setGrupoList(attachedGrupoList);
            List<Caso> attachedCasoList = new ArrayList<Caso>();
            for (Caso casoListCasoToAttach : categoria.getCasoList()) {
                casoListCasoToAttach = em.getReference(casoListCasoToAttach.getClass(), casoListCasoToAttach.getIdCaso());
                attachedCasoList.add(casoListCasoToAttach);
            }
            categoria.setCasoList(attachedCasoList);
            List<Categoria> attachedCategoriaList = new ArrayList<Categoria>();
            for (Categoria categoriaListCategoriaToAttach : categoria.getCategoriaList()) {
                categoriaListCategoriaToAttach = em.getReference(categoriaListCategoriaToAttach.getClass(), categoriaListCategoriaToAttach.getIdCategoria());
                attachedCategoriaList.add(categoriaListCategoriaToAttach);
            }
            categoria.setCategoriaList(attachedCategoriaList);
            em.persist(categoria);
            if (idCategoriaPadre != null) {
                idCategoriaPadre.getCategoriaList().add(categoria);
                idCategoriaPadre = em.merge(idCategoriaPadre);
            }
            if (idArea != null) {
                idArea.getCategoriaList().add(categoria);
                idArea = em.merge(idArea);
            }
//            for (SubEstadoCaso subEstadoCasoListSubEstadoCaso : categoria.getSubEstadoCasoList()) {
//                subEstadoCasoListSubEstadoCaso.getCategoriaList().add(categoria);
//                subEstadoCasoListSubEstadoCaso = em.merge(subEstadoCasoListSubEstadoCaso);
//            }
            for (Grupo grupoListGrupo : categoria.getGrupoList()) {
                grupoListGrupo.getCategoriaList().add(categoria);
                grupoListGrupo = em.merge(grupoListGrupo);
            }
            for (Caso casoListCaso : categoria.getCasoList()) {
                Categoria oldIdCategoriaOfCasoListCaso = casoListCaso.getIdCategoria();
                casoListCaso.setIdCategoria(categoria);
                casoListCaso = em.merge(casoListCaso);
                if (oldIdCategoriaOfCasoListCaso != null) {
                    oldIdCategoriaOfCasoListCaso.getCasoList().remove(casoListCaso);
                    oldIdCategoriaOfCasoListCaso = em.merge(oldIdCategoriaOfCasoListCaso);
                }
            }
            for (Categoria categoriaListCategoria : categoria.getCategoriaList()) {
                Categoria oldIdCategoriaPadreOfCategoriaListCategoria = categoriaListCategoria.getIdCategoriaPadre();
                categoriaListCategoria.setIdCategoriaPadre(categoria);
                categoriaListCategoria = em.merge(categoriaListCategoria);
                if (oldIdCategoriaPadreOfCategoriaListCategoria != null) {
                    oldIdCategoriaPadreOfCategoriaListCategoria.getCategoriaList().remove(categoriaListCategoria);
                    oldIdCategoriaPadreOfCategoriaListCategoria = em.merge(oldIdCategoriaPadreOfCategoriaListCategoria);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (categoria != null && categoria.getIdCategoria() != null) {
                if (findCategoria(categoria.getIdCategoria()) != null) {
                    throw new PreexistingEntityException("Categoria " + categoria + " already exists.", ex);
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categoria categoria) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Categoria persistentCategoria = em.find(Categoria.class, categoria.getIdCategoria());
            Categoria idCategoriaPadreOld = persistentCategoria.getIdCategoriaPadre();
            Categoria idCategoriaPadreNew = categoria.getIdCategoriaPadre();
            Area idAreaOld = persistentCategoria.getIdArea();
            Area idAreaNew = categoria.getIdArea();
//            List<SubEstadoCaso> subEstadoCasoListOld = persistentCategoria.getSubEstadoCasoList();
//            List<SubEstadoCaso> subEstadoCasoListNew = categoria.getSubEstadoCasoList();
            List<Grupo> grupoListOld = persistentCategoria.getGrupoList();
            List<Grupo> grupoListNew = categoria.getGrupoList();
            List<Caso> casoListOld = persistentCategoria.getCasoList();
            List<Caso> casoListNew = categoria.getCasoList();
            List<Categoria> categoriaListOld = persistentCategoria.getCategoriaList();
            List<Categoria> categoriaListNew = categoria.getCategoriaList();
            if (idCategoriaPadreNew != null) {
                idCategoriaPadreNew = em.getReference(idCategoriaPadreNew.getClass(), idCategoriaPadreNew.getIdCategoria());
                categoria.setIdCategoriaPadre(idCategoriaPadreNew);
            }
            if (idAreaNew != null) {
                idAreaNew = em.getReference(idAreaNew.getClass(), idAreaNew.getIdArea());
                categoria.setIdArea(idAreaNew);
            }
//            List<SubEstadoCaso> attachedSubEstadoCasoListNew = new ArrayList<SubEstadoCaso>();
//            for (SubEstadoCaso subEstadoCasoListNewSubEstadoCasoToAttach : subEstadoCasoListNew) {
//                subEstadoCasoListNewSubEstadoCasoToAttach = em.getReference(subEstadoCasoListNewSubEstadoCasoToAttach.getClass(), subEstadoCasoListNewSubEstadoCasoToAttach.getIdSubEstado());
//                attachedSubEstadoCasoListNew.add(subEstadoCasoListNewSubEstadoCasoToAttach);
//            }
//            subEstadoCasoListNew = attachedSubEstadoCasoListNew;
//            categoria.setSubEstadoCasoList(subEstadoCasoListNew);
            List<Grupo> attachedGrupoListNew = new ArrayList<Grupo>();
            for (Grupo grupoListNewGrupoToAttach : grupoListNew) {
                grupoListNewGrupoToAttach = em.getReference(grupoListNewGrupoToAttach.getClass(), grupoListNewGrupoToAttach.getIdGrupo());
                attachedGrupoListNew.add(grupoListNewGrupoToAttach);
            }
            grupoListNew = attachedGrupoListNew;
            categoria.setGrupoList(grupoListNew);
            List<Caso> attachedCasoListNew = new ArrayList<Caso>();
            for (Caso casoListNewCasoToAttach : casoListNew) {
                casoListNewCasoToAttach = em.getReference(casoListNewCasoToAttach.getClass(), casoListNewCasoToAttach.getIdCaso());
                attachedCasoListNew.add(casoListNewCasoToAttach);
            }
            casoListNew = attachedCasoListNew;
            categoria.setCasoList(casoListNew);
            List<Categoria> attachedCategoriaListNew = new ArrayList<Categoria>();
            for (Categoria categoriaListNewCategoriaToAttach : categoriaListNew) {
                categoriaListNewCategoriaToAttach = em.getReference(categoriaListNewCategoriaToAttach.getClass(), categoriaListNewCategoriaToAttach.getIdCategoria());
                attachedCategoriaListNew.add(categoriaListNewCategoriaToAttach);
            }
            categoriaListNew = attachedCategoriaListNew;
            categoria.setCategoriaList(categoriaListNew);
            categoria = em.merge(categoria);
            if (idCategoriaPadreOld != null && !idCategoriaPadreOld.equals(idCategoriaPadreNew)) {
                idCategoriaPadreOld.getCategoriaList().remove(categoria);
                idCategoriaPadreOld = em.merge(idCategoriaPadreOld);
            }
            if (idCategoriaPadreNew != null && !idCategoriaPadreNew.equals(idCategoriaPadreOld)) {
                idCategoriaPadreNew.getCategoriaList().add(categoria);
                idCategoriaPadreNew = em.merge(idCategoriaPadreNew);
            }
            if (idAreaOld != null && !idAreaOld.equals(idAreaNew)) {
                idAreaOld.getCategoriaList().remove(categoria);
                idAreaOld = em.merge(idAreaOld);
            }
            if (idAreaNew != null && !idAreaNew.equals(idAreaOld)) {
                idAreaNew.getCategoriaList().add(categoria);
                idAreaNew = em.merge(idAreaNew);
            }
//            for (SubEstadoCaso subEstadoCasoListOldSubEstadoCaso : subEstadoCasoListOld) {
//                if (!subEstadoCasoListNew.contains(subEstadoCasoListOldSubEstadoCaso)) {
//                    subEstadoCasoListOldSubEstadoCaso.getCategoriaList().remove(categoria);
//                    subEstadoCasoListOldSubEstadoCaso = em.merge(subEstadoCasoListOldSubEstadoCaso);
//                }
//            }
//            for (SubEstadoCaso subEstadoCasoListNewSubEstadoCaso : subEstadoCasoListNew) {
//                if (!subEstadoCasoListOld.contains(subEstadoCasoListNewSubEstadoCaso)) {
//                    subEstadoCasoListNewSubEstadoCaso.getCategoriaList().add(categoria);
//                    subEstadoCasoListNewSubEstadoCaso = em.merge(subEstadoCasoListNewSubEstadoCaso);
//                }
//            }
            for (Grupo grupoListOldGrupo : grupoListOld) {
                if (!grupoListNew.contains(grupoListOldGrupo)) {
                    grupoListOldGrupo.getCategoriaList().remove(categoria);
                    grupoListOldGrupo = em.merge(grupoListOldGrupo);
                }
            }
            for (Grupo grupoListNewGrupo : grupoListNew) {
                if (!grupoListOld.contains(grupoListNewGrupo)) {
                    grupoListNewGrupo.getCategoriaList().add(categoria);
                    grupoListNewGrupo = em.merge(grupoListNewGrupo);
                }
            }
            for (Caso casoListOldCaso : casoListOld) {
                if (!casoListNew.contains(casoListOldCaso)) {
                    casoListOldCaso.setIdCategoria(null);
                    casoListOldCaso = em.merge(casoListOldCaso);
                }
            }
            for (Caso casoListNewCaso : casoListNew) {
                if (!casoListOld.contains(casoListNewCaso)) {
                    Categoria oldIdCategoriaOfCasoListNewCaso = casoListNewCaso.getIdCategoria();
                    casoListNewCaso.setIdCategoria(categoria);
                    casoListNewCaso = em.merge(casoListNewCaso);
                    if (oldIdCategoriaOfCasoListNewCaso != null && !oldIdCategoriaOfCasoListNewCaso.equals(categoria)) {
                        oldIdCategoriaOfCasoListNewCaso.getCasoList().remove(casoListNewCaso);
                        oldIdCategoriaOfCasoListNewCaso = em.merge(oldIdCategoriaOfCasoListNewCaso);
                    }
                }
            }
            for (Categoria categoriaListOldCategoria : categoriaListOld) {
                if (!categoriaListNew.contains(categoriaListOldCategoria)) {
                    categoriaListOldCategoria.setIdCategoriaPadre(null);
                    categoriaListOldCategoria = em.merge(categoriaListOldCategoria);
                }
            }
            for (Categoria categoriaListNewCategoria : categoriaListNew) {
                if (!categoriaListOld.contains(categoriaListNewCategoria)) {
                    Categoria oldIdCategoriaPadreOfCategoriaListNewCategoria = categoriaListNewCategoria.getIdCategoriaPadre();
                    categoriaListNewCategoria.setIdCategoriaPadre(categoria);
                    categoriaListNewCategoria = em.merge(categoriaListNewCategoria);
                    if (oldIdCategoriaPadreOfCategoriaListNewCategoria != null && !oldIdCategoriaPadreOfCategoriaListNewCategoria.equals(categoria)) {
                        oldIdCategoriaPadreOfCategoriaListNewCategoria.getCategoriaList().remove(categoriaListNewCategoria);
                        oldIdCategoriaPadreOfCategoriaListNewCategoria = em.merge(oldIdCategoriaPadreOfCategoriaListNewCategoria);
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
                Integer id = categoria.getIdCategoria();
                if (findCategoria(id) == null) {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
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
            Categoria categoria;
            try {
                categoria = em.getReference(Categoria.class, id);
                categoria.getIdCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            Categoria idCategoriaPadre = categoria.getIdCategoriaPadre();
            if (idCategoriaPadre != null) {
                idCategoriaPadre.getCategoriaList().remove(categoria);
                idCategoriaPadre = em.merge(idCategoriaPadre);
            }
            Area idArea = categoria.getIdArea();
            if (idArea != null) {
                idArea.getCategoriaList().remove(categoria);
                idArea = em.merge(idArea);
            }
//            List<SubEstadoCaso> subEstadoCasoList = categoria.getSubEstadoCasoList();
//            for (SubEstadoCaso subEstadoCasoListSubEstadoCaso : subEstadoCasoList) {
//                subEstadoCasoListSubEstadoCaso.getCategoriaList().remove(categoria);
//                subEstadoCasoListSubEstadoCaso = em.merge(subEstadoCasoListSubEstadoCaso);
//            }
            List<Grupo> grupoList = categoria.getGrupoList();
            for (Grupo grupoListGrupo : grupoList) {
                grupoListGrupo.getCategoriaList().remove(categoria);
                grupoListGrupo = em.merge(grupoListGrupo);
            }
            List<Caso> casoList = categoria.getCasoList();
            for (Caso casoListCaso : casoList) {
                casoListCaso.setIdCategoria(null);
                casoListCaso = em.merge(casoListCaso);
            }
            List<Categoria> categoriaList = categoria.getCategoriaList();
            for (Categoria categoriaListCategoria : categoriaList) {
                categoriaListCategoria.setIdCategoriaPadre(null);
                categoriaListCategoria = em.merge(categoriaListCategoria);
            }
            em.remove(categoria);
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

    public List<Categoria> findCategoriaEntities() {
        return findCategoriaEntities(true, -1, -1);
    }

    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return findCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
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

    public Categoria findCategoria(Integer id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
