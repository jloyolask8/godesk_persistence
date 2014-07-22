/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import com.itcs.helpdesk.persistence.entities.Area;
import com.itcs.helpdesk.persistence.entities.FiltroVista;
import com.itcs.helpdesk.persistence.entities.Grupo;
import com.itcs.helpdesk.persistence.entities.Usuario;
import com.itcs.helpdesk.persistence.entities.Vista;
import com.itcs.helpdesk.persistence.jpa.custom.CriteriaQueryHelper;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
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
public class VistaJpaController extends AbstractJPAController implements Serializable {

    public VistaJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class VistaJpaController implements Serializable {
//
//    public VistaJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(Vista vista) throws RollbackFailureException, Exception {
        if (vista.getFiltrosVistaList() == null) {
            vista.setFiltrosVistaList(new ArrayList<FiltroVista>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Usuario idUsuarioCreadaPor = vista.getIdUsuarioCreadaPor();
            if (idUsuarioCreadaPor != null) {
                idUsuarioCreadaPor = em.getReference(idUsuarioCreadaPor.getClass(), idUsuarioCreadaPor.getIdUsuario());
                vista.setIdUsuarioCreadaPor(idUsuarioCreadaPor);
            }
            Grupo idGrupo = vista.getIdGrupo();
            if (idGrupo != null) {
                idGrupo = em.getReference(idGrupo.getClass(), idGrupo.getIdGrupo());
                vista.setIdGrupo(idGrupo);
            }
            Area idArea = vista.getIdArea();
            if (idArea != null) {
                idArea = em.getReference(idArea.getClass(), idArea.getIdArea());
                vista.setIdArea(idArea);
            }

            List<FiltroVista> attachedFiltrosVistaList = new ArrayList<FiltroVista>();
            for (FiltroVista filtrosVistaListFiltroVistaToAttach : vista.getFiltrosVistaList()) {
                if (filtrosVistaListFiltroVistaToAttach.getIdFiltro() == null) {
                    em.persist(filtrosVistaListFiltroVistaToAttach);
                    //filtrosVistaListFiltroVistaToAttach = em.getReference(filtrosVistaListFiltroVistaToAttach.getClass(), filtrosVistaListFiltroVistaToAttach.getIdFiltro());
                } //else {
//                }

                attachedFiltrosVistaList.add(filtrosVistaListFiltroVistaToAttach);
            }
            vista.setFiltrosVistaList(attachedFiltrosVistaList);
            em.persist(vista);
            if (idUsuarioCreadaPor != null) {
                idUsuarioCreadaPor.getVistaList().add(vista);
                idUsuarioCreadaPor = em.merge(idUsuarioCreadaPor);
            }
            if (idGrupo != null) {
                idGrupo.getVistaList().add(vista);
                idGrupo = em.merge(idGrupo);
            }
            if (idArea != null) {
                idArea.getVistaList().add(vista);
                idArea = em.merge(idArea);
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

    public void edit(Vista vista) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Vista persistentVista = em.find(Vista.class, vista.getIdVista());
            Usuario idUsuarioCreadaPorOld = persistentVista.getIdUsuarioCreadaPor();
            Usuario idUsuarioCreadaPorNew = vista.getIdUsuarioCreadaPor();
            Grupo idGrupoOld = persistentVista.getIdGrupo();
            Grupo idGrupoNew = vista.getIdGrupo();
            Area idAreaOld = persistentVista.getIdArea();
            Area idAreaNew = vista.getIdArea();
            List<FiltroVista> filtrosVistaListOld = persistentVista.getFiltrosVistaList();
            List<FiltroVista> filtrosVistaListNew = vista.getFiltrosVistaList();
            if (idUsuarioCreadaPorNew != null) {
                idUsuarioCreadaPorNew = em.getReference(idUsuarioCreadaPorNew.getClass(), idUsuarioCreadaPorNew.getIdUsuario());
                vista.setIdUsuarioCreadaPor(idUsuarioCreadaPorNew);
            }
            if (idGrupoNew != null) {
                idGrupoNew = em.getReference(idGrupoNew.getClass(), idGrupoNew.getIdGrupo());
                vista.setIdGrupo(idGrupoNew);
            }
            if (idAreaNew != null) {
                idAreaNew = em.getReference(idAreaNew.getClass(), idAreaNew.getIdArea());
                vista.setIdArea(idAreaNew);
            }
            List<FiltroVista> attachedFiltrosVistaListNew = new ArrayList<FiltroVista>();
            for (FiltroVista filtrosVistaListNewFiltroVistaToAttach : filtrosVistaListNew) {
                if (filtrosVistaListNewFiltroVistaToAttach.getIdFiltro() == null) {
                    em.persist(filtrosVistaListNewFiltroVistaToAttach);

                }
//                else {
//                    filtrosVistaListNewFiltroVistaToAttach = em.getReference(filtrosVistaListNewFiltroVistaToAttach.getClass(), filtrosVistaListNewFiltroVistaToAttach.getIdFiltro());
//                }

                attachedFiltrosVistaListNew.add(filtrosVistaListNewFiltroVistaToAttach);
            }
            filtrosVistaListNew = attachedFiltrosVistaListNew;
            vista.setFiltrosVistaList(filtrosVistaListNew);

            vista = em.merge(vista);
            if (idUsuarioCreadaPorOld != null && !idUsuarioCreadaPorOld.equals(idUsuarioCreadaPorNew)) {
                idUsuarioCreadaPorOld.getVistaList().remove(vista);
                idUsuarioCreadaPorOld = em.merge(idUsuarioCreadaPorOld);
            }
            if (idUsuarioCreadaPorNew != null && !idUsuarioCreadaPorNew.equals(idUsuarioCreadaPorOld)) {
                idUsuarioCreadaPorNew.getVistaList().add(vista);
                idUsuarioCreadaPorNew = em.merge(idUsuarioCreadaPorNew);
            }
            if (idGrupoOld != null && !idGrupoOld.equals(idGrupoNew)) {
                idGrupoOld.getVistaList().remove(vista);
                idGrupoOld = em.merge(idGrupoOld);
            }
            if (idGrupoNew != null && !idGrupoNew.equals(idGrupoOld)) {
                idGrupoNew.getVistaList().add(vista);
                idGrupoNew = em.merge(idGrupoNew);
            }
            if (idAreaOld != null && !idAreaOld.equals(idAreaNew)) {
                idAreaOld.getVistaList().remove(vista);
                idAreaOld = em.merge(idAreaOld);
            }
            if (idAreaNew != null && !idAreaNew.equals(idAreaOld)) {
                idAreaNew.getVistaList().add(vista);
                idAreaNew = em.merge(idAreaNew);
            }
            for (FiltroVista filtrosVistaListOldFiltroVista : filtrosVistaListOld) {
                if (!filtrosVistaListNew.contains(filtrosVistaListOldFiltroVista)) {
                    filtrosVistaListOldFiltroVista.setIdVista(null);
                    filtrosVistaListOldFiltroVista = em.merge(filtrosVistaListOldFiltroVista);
                }
            }
            for (FiltroVista filtrosVistaListNewFiltroVista : filtrosVistaListNew) {
                if (!filtrosVistaListOld.contains(filtrosVistaListNewFiltroVista)) {
                    Vista oldIdVistaOfFiltrosVistaListNewFiltroVista = filtrosVistaListNewFiltroVista.getIdVista();
                    filtrosVistaListNewFiltroVista.setIdVista(vista);
                    filtrosVistaListNewFiltroVista = em.merge(filtrosVistaListNewFiltroVista);
                    if (oldIdVistaOfFiltrosVistaListNewFiltroVista != null && !oldIdVistaOfFiltrosVistaListNewFiltroVista.equals(vista)) {
                        oldIdVistaOfFiltrosVistaListNewFiltroVista.getFiltrosVistaList().remove(filtrosVistaListNewFiltroVista);
                        oldIdVistaOfFiltrosVistaListNewFiltroVista = em.merge(oldIdVistaOfFiltrosVistaListNewFiltroVista);
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
                Integer id = vista.getIdVista();
                if (findVista(id) == null) {
                    throw new NonexistentEntityException("The vista with id " + id + " no longer exists.");
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
            Vista vista;
            try {
                vista = em.getReference(Vista.class, id);
                vista.getIdVista();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vista with id " + id + " no longer exists.", enfe);
            }
            Usuario idUsuarioCreadaPor = vista.getIdUsuarioCreadaPor();
            if (idUsuarioCreadaPor != null) {
                idUsuarioCreadaPor.getVistaList().remove(vista);
                idUsuarioCreadaPor = em.merge(idUsuarioCreadaPor);
            }
            Grupo idGrupo = vista.getIdGrupo();
            if (idGrupo != null) {
                idGrupo.getVistaList().remove(vista);
                idGrupo = em.merge(idGrupo);
            }
            Area idArea = vista.getIdArea();
            if (idArea != null) {
                idArea.getVistaList().remove(vista);
                idArea = em.merge(idArea);
            }
            List<FiltroVista> filtrosVistaList = vista.getFiltrosVistaList();
            for (FiltroVista filtrosVistaListFiltroVista : filtrosVistaList) {
                filtrosVistaListFiltroVista.setIdVista(null);
                filtrosVistaListFiltroVista = em.merge(filtrosVistaListFiltroVista);
            }
            em.remove(vista);
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

    public List<Vista> findVistaEntities() {
        return findVistaEntities(true, -1, -1);
    }

    public List<Vista> findVistaEntities(int maxResults, int firstResult) {
        return findVistaEntities(false, maxResults, firstResult);
    }

    private List<Vista> findVistaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vista.class));
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

    

    public Vista findVista(Integer id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Vista.class, id);
        } finally {
            em.close();
        }
    }

    public int getVistaCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vista> rt = cq.from(Vista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
