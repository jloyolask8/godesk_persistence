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
import java.util.ArrayList;
import java.util.List;
import com.itcs.helpdesk.persistence.entities.Componente;
import com.itcs.helpdesk.persistence.entities.Producto;
import com.itcs.helpdesk.persistence.entities.SubComponente;
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
public class ProductoJpaController extends AbstractJPAController implements Serializable {

    public ProductoJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class ProductoJpaController implements Serializable {
//
//    public ProductoJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void createOrMerge(Producto producto) throws RollbackFailureException, Exception {
        if (producto.getCasoList() == null) {
            producto.setCasoList(new ArrayList<Caso>());
        }
        if (producto.getComponenteList() == null) {
            producto.setComponenteList(new ArrayList<Componente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());

            List<Componente> attachedComponenteList = new ArrayList<Componente>();
            for (Componente componente : producto.getComponenteList()) {
                Componente persistentComponent = em.find(componente.getClass(), componente.getIdComponente());
                if (persistentComponent == null) {
                    //component do not exist!
                    em.persist(componente);
                    List<SubComponente> subComponentList = new ArrayList<SubComponente>();
                    for (SubComponente subComponent : componente.getSubComponenteList()) {
                        SubComponente persistentSubComponent = em.find(SubComponente.class, subComponent.getIdSubComponente());
                        if (persistentSubComponent == null) {
                            //sub component do not exist!
                            System.out.println("sub component do not exist!:" + subComponent);
                            em.persist(subComponent);
                        }
                        subComponentList.add(subComponent);
                    }
                    componente.setSubComponenteList(subComponentList);
                    componente = em.merge(componente);
                }
                attachedComponenteList.add(componente);
            }
            producto.setComponenteList(attachedComponenteList);

            if (findProducto(producto.getIdProducto()) != null) {
                //exists
                em.merge(producto);
            } else {
                em.persist(producto);
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

    public void create(Producto producto) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (producto.getCasoList() == null) {
            producto.setCasoList(new ArrayList<Caso>());
        }
        if (producto.getComponenteList() == null) {
            producto.setComponenteList(new ArrayList<Componente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            List<Caso> attachedCasoList = new ArrayList<Caso>();
            for (Caso casoListCasoToAttach : producto.getCasoList()) {
                casoListCasoToAttach = em.getReference(casoListCasoToAttach.getClass(), casoListCasoToAttach.getIdCaso());
                attachedCasoList.add(casoListCasoToAttach);
            }
            producto.setCasoList(attachedCasoList);
            List<Componente> attachedComponenteList = new ArrayList<Componente>();
            for (Componente componenteListComponenteToAttach : producto.getComponenteList()) {
                componenteListComponenteToAttach = em.getReference(componenteListComponenteToAttach.getClass(), componenteListComponenteToAttach.getIdComponente());
                attachedComponenteList.add(componenteListComponenteToAttach);
            }
            producto.setComponenteList(attachedComponenteList);
            em.persist(producto);
            for (Caso casoListCaso : producto.getCasoList()) {
                Producto oldIdProductoOfCasoListCaso = casoListCaso.getIdProducto();
                casoListCaso.setIdProducto(producto);
                casoListCaso = em.merge(casoListCaso);
                if (oldIdProductoOfCasoListCaso != null) {
                    oldIdProductoOfCasoListCaso.getCasoList().remove(casoListCaso);
                    oldIdProductoOfCasoListCaso = em.merge(oldIdProductoOfCasoListCaso);
                }
            }
            for (Componente componenteListComponente : producto.getComponenteList()) {
                Producto oldIdProductoOfComponenteListComponente = componenteListComponente.getIdProducto();
                componenteListComponente.setIdProducto(producto);
                componenteListComponente = em.merge(componenteListComponente);
                if (oldIdProductoOfComponenteListComponente != null) {
                    oldIdProductoOfComponenteListComponente.getComponenteList().remove(componenteListComponente);
                    oldIdProductoOfComponenteListComponente = em.merge(oldIdProductoOfComponenteListComponente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProducto(producto.getIdProducto()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Producto persistentProducto = em.find(Producto.class, producto.getIdProducto());
            List<Caso> casoListOld = persistentProducto.getCasoList();
            List<Caso> casoListNew = producto.getCasoList();
            List<Componente> componenteListOld = persistentProducto.getComponenteList();
            List<Componente> componenteListNew = producto.getComponenteList();
            List<Caso> attachedCasoListNew = new ArrayList<Caso>();
            for (Caso casoListNewCasoToAttach : casoListNew) {
                casoListNewCasoToAttach = em.getReference(casoListNewCasoToAttach.getClass(), casoListNewCasoToAttach.getIdCaso());
                attachedCasoListNew.add(casoListNewCasoToAttach);
            }
            casoListNew = attachedCasoListNew;
            producto.setCasoList(casoListNew);
            List<Componente> attachedComponenteListNew = new ArrayList<Componente>();
            for (Componente componenteListNewComponenteToAttach : componenteListNew) {
                componenteListNewComponenteToAttach = em.getReference(componenteListNewComponenteToAttach.getClass(), componenteListNewComponenteToAttach.getIdComponente());
                attachedComponenteListNew.add(componenteListNewComponenteToAttach);
            }
            componenteListNew = attachedComponenteListNew;
            producto.setComponenteList(componenteListNew);
            producto = em.merge(producto);
            for (Caso casoListOldCaso : casoListOld) {
                if (!casoListNew.contains(casoListOldCaso)) {
                    casoListOldCaso.setIdProducto(null);
                    casoListOldCaso = em.merge(casoListOldCaso);
                }
            }
            for (Caso casoListNewCaso : casoListNew) {
                if (!casoListOld.contains(casoListNewCaso)) {
                    Producto oldIdProductoOfCasoListNewCaso = casoListNewCaso.getIdProducto();
                    casoListNewCaso.setIdProducto(producto);
                    casoListNewCaso = em.merge(casoListNewCaso);
                    if (oldIdProductoOfCasoListNewCaso != null && !oldIdProductoOfCasoListNewCaso.equals(producto)) {
                        oldIdProductoOfCasoListNewCaso.getCasoList().remove(casoListNewCaso);
                        oldIdProductoOfCasoListNewCaso = em.merge(oldIdProductoOfCasoListNewCaso);
                    }
                }
            }
            for (Componente componenteListOldComponente : componenteListOld) {
                if (!componenteListNew.contains(componenteListOldComponente)) {
                    componenteListOldComponente.setIdProducto(null);
                    componenteListOldComponente = em.merge(componenteListOldComponente);
                }
            }
            for (Componente componenteListNewComponente : componenteListNew) {
                if (!componenteListOld.contains(componenteListNewComponente)) {
                    Producto oldIdProductoOfComponenteListNewComponente = componenteListNewComponente.getIdProducto();
                    componenteListNewComponente.setIdProducto(producto);
                    componenteListNewComponente = em.merge(componenteListNewComponente);
                    if (oldIdProductoOfComponenteListNewComponente != null && !oldIdProductoOfComponenteListNewComponente.equals(producto)) {
                        oldIdProductoOfComponenteListNewComponente.getComponenteList().remove(componenteListNewComponente);
                        oldIdProductoOfComponenteListNewComponente = em.merge(oldIdProductoOfComponenteListNewComponente);
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
                String id = producto.getIdProducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<Caso> casoList = producto.getCasoList();
            for (Caso casoListCaso : casoList) {
                casoListCaso.setIdProducto(null);
                casoListCaso = em.merge(casoListCaso);
            }
            List<Componente> componenteList = producto.getComponenteList();
            for (Componente componenteListComponente : componenteList) {
                componenteListComponente.setIdProducto(null);
                componenteListComponente = em.merge(componenteListComponente);
            }
            em.remove(producto);
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

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(String id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
