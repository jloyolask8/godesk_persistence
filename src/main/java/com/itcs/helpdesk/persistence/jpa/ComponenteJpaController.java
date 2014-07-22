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
import com.itcs.helpdesk.persistence.entities.Producto;
import com.itcs.helpdesk.persistence.entities.Caso;
import com.itcs.helpdesk.persistence.entities.Componente;
import java.util.ArrayList;
import java.util.List;
import com.itcs.helpdesk.persistence.entities.SubComponente;
import com.itcs.helpdesk.persistence.jpa.exceptions.IllegalOrphanException;
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
public class ComponenteJpaController extends AbstractJPAController implements Serializable {

    public ComponenteJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class ComponenteJpaController implements Serializable {
//
//    public ComponenteJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(Componente componente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (componente.getCasoList() == null) {
            componente.setCasoList(new ArrayList<Caso>());
        }
        if (componente.getSubComponenteList() == null) {
            componente.setSubComponenteList(new ArrayList<SubComponente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Producto idProducto = componente.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                componente.setIdProducto(idProducto);
            }
            List<Caso> attachedCasoList = new ArrayList<Caso>();
            for (Caso casoListCasoToAttach : componente.getCasoList()) {
                casoListCasoToAttach = em.getReference(casoListCasoToAttach.getClass(), casoListCasoToAttach.getIdCaso());
                attachedCasoList.add(casoListCasoToAttach);
            }
            componente.setCasoList(attachedCasoList);
            List<SubComponente> attachedSubComponenteList = new ArrayList<SubComponente>();
            for (SubComponente subComponenteListSubComponenteToAttach : componente.getSubComponenteList()) {
                subComponenteListSubComponenteToAttach = em.getReference(subComponenteListSubComponenteToAttach.getClass(), subComponenteListSubComponenteToAttach.getIdSubComponente());
                attachedSubComponenteList.add(subComponenteListSubComponenteToAttach);
            }
            componente.setSubComponenteList(attachedSubComponenteList);
            em.persist(componente);
            if (idProducto != null) {
                idProducto.getComponenteList().add(componente);
                idProducto = em.merge(idProducto);
            }
            for (Caso casoListCaso : componente.getCasoList()) {
                Componente oldIdComponenteOfCasoListCaso = casoListCaso.getIdComponente();
                casoListCaso.setIdComponente(componente);
                casoListCaso = em.merge(casoListCaso);
                if (oldIdComponenteOfCasoListCaso != null) {
                    oldIdComponenteOfCasoListCaso.getCasoList().remove(casoListCaso);
                    oldIdComponenteOfCasoListCaso = em.merge(oldIdComponenteOfCasoListCaso);
                }
            }
            for (SubComponente subComponenteListSubComponente : componente.getSubComponenteList()) {
                Componente oldIdComponenteOfSubComponenteListSubComponente = subComponenteListSubComponente.getIdComponente();
                subComponenteListSubComponente.setIdComponente(componente);
                subComponenteListSubComponente = em.merge(subComponenteListSubComponente);
                if (oldIdComponenteOfSubComponenteListSubComponente != null) {
                    oldIdComponenteOfSubComponenteListSubComponente.getSubComponenteList().remove(subComponenteListSubComponente);
                    oldIdComponenteOfSubComponenteListSubComponente = em.merge(oldIdComponenteOfSubComponenteListSubComponente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findComponente(componente.getIdComponente()) != null) {
                throw new PreexistingEntityException("Componente " + componente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Componente componente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Componente persistentComponente = em.find(Componente.class, componente.getIdComponente());
            Producto idProductoOld = persistentComponente.getIdProducto();
            Producto idProductoNew = componente.getIdProducto();
            List<Caso> casoListOld = persistentComponente.getCasoList();
            List<Caso> casoListNew = componente.getCasoList();
            List<SubComponente> subComponenteListOld = persistentComponente.getSubComponenteList();
            List<SubComponente> subComponenteListNew = componente.getSubComponenteList();
            List<String> illegalOrphanMessages = null;
            for (SubComponente subComponenteListOldSubComponente : subComponenteListOld) {
                if (!subComponenteListNew.contains(subComponenteListOldSubComponente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SubComponente " + subComponenteListOldSubComponente + " since its idComponente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                componente.setIdProducto(idProductoNew);
            }
            List<Caso> attachedCasoListNew = new ArrayList<Caso>();
            for (Caso casoListNewCasoToAttach : casoListNew) {
                casoListNewCasoToAttach = em.getReference(casoListNewCasoToAttach.getClass(), casoListNewCasoToAttach.getIdCaso());
                attachedCasoListNew.add(casoListNewCasoToAttach);
            }
            casoListNew = attachedCasoListNew;
            componente.setCasoList(casoListNew);
            List<SubComponente> attachedSubComponenteListNew = new ArrayList<SubComponente>();
            for (SubComponente subComponenteListNewSubComponenteToAttach : subComponenteListNew) {
                subComponenteListNewSubComponenteToAttach = em.getReference(subComponenteListNewSubComponenteToAttach.getClass(), subComponenteListNewSubComponenteToAttach.getIdSubComponente());
                attachedSubComponenteListNew.add(subComponenteListNewSubComponenteToAttach);
            }
            subComponenteListNew = attachedSubComponenteListNew;
            componente.setSubComponenteList(subComponenteListNew);
            componente = em.merge(componente);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getComponenteList().remove(componente);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getComponenteList().add(componente);
                idProductoNew = em.merge(idProductoNew);
            }
            for (Caso casoListOldCaso : casoListOld) {
                if (!casoListNew.contains(casoListOldCaso)) {
                    casoListOldCaso.setIdComponente(null);
                    casoListOldCaso = em.merge(casoListOldCaso);
                }
            }
            for (Caso casoListNewCaso : casoListNew) {
                if (!casoListOld.contains(casoListNewCaso)) {
                    Componente oldIdComponenteOfCasoListNewCaso = casoListNewCaso.getIdComponente();
                    casoListNewCaso.setIdComponente(componente);
                    casoListNewCaso = em.merge(casoListNewCaso);
                    if (oldIdComponenteOfCasoListNewCaso != null && !oldIdComponenteOfCasoListNewCaso.equals(componente)) {
                        oldIdComponenteOfCasoListNewCaso.getCasoList().remove(casoListNewCaso);
                        oldIdComponenteOfCasoListNewCaso = em.merge(oldIdComponenteOfCasoListNewCaso);
                    }
                }
            }
            for (SubComponente subComponenteListNewSubComponente : subComponenteListNew) {
                if (!subComponenteListOld.contains(subComponenteListNewSubComponente)) {
                    Componente oldIdComponenteOfSubComponenteListNewSubComponente = subComponenteListNewSubComponente.getIdComponente();
                    subComponenteListNewSubComponente.setIdComponente(componente);
                    subComponenteListNewSubComponente = em.merge(subComponenteListNewSubComponente);
                    if (oldIdComponenteOfSubComponenteListNewSubComponente != null && !oldIdComponenteOfSubComponenteListNewSubComponente.equals(componente)) {
                        oldIdComponenteOfSubComponenteListNewSubComponente.getSubComponenteList().remove(subComponenteListNewSubComponente);
                        oldIdComponenteOfSubComponenteListNewSubComponente = em.merge(oldIdComponenteOfSubComponenteListNewSubComponente);
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
                String id = componente.getIdComponente();
                if (findComponente(id) == null) {
                    throw new NonexistentEntityException("The componente with id " + id + " no longer exists.");
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
            Componente componente;
            try {
                componente = em.getReference(Componente.class, id);
                componente.getIdComponente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The componente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SubComponente> subComponenteListOrphanCheck = componente.getSubComponenteList();
            for (SubComponente subComponenteListOrphanCheckSubComponente : subComponenteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Componente (" + componente + ") cannot be destroyed since the SubComponente " + subComponenteListOrphanCheckSubComponente + " in its subComponenteList field has a non-nullable idComponente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Producto idProducto = componente.getIdProducto();
            if (idProducto != null) {
                idProducto.getComponenteList().remove(componente);
                idProducto = em.merge(idProducto);
            }
            List<Caso> casoList = componente.getCasoList();
            for (Caso casoListCaso : casoList) {
                casoListCaso.setIdComponente(null);
                casoListCaso = em.merge(casoListCaso);
            }
            em.remove(componente);
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

    public List<Componente> findComponenteEntities() {
        return findComponenteEntities(true, -1, -1);
    }

    public List<Componente> findComponenteEntities(int maxResults, int firstResult) {
        return findComponenteEntities(false, maxResults, firstResult);
    }

    private List<Componente> findComponenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Componente.class));
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

    public Componente findComponente(String id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Componente.class, id);
        } finally {
            em.close();
        }
    }

    public int getComponenteCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Componente> rt = cq.from(Componente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
