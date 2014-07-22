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
import com.itcs.helpdesk.persistence.entities.Cliente;
import com.itcs.helpdesk.persistence.entities.Caso;
import com.itcs.helpdesk.persistence.entities.EmailCliente;
import com.itcs.helpdesk.persistence.entities.ProductoContratado;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.PreexistingEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import org.eclipse.persistence.config.EntityManagerProperties;

/**
 *
 * @author jonathan
 */
public class EmailClienteJpaController extends AbstractJPAController implements Serializable {

    public EmailClienteJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }

//public class EmailClienteJpaController implements Serializable {
//
//    public EmailClienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }
    public void create(EmailCliente emailCliente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (emailCliente.getCasoList() == null) {
            emailCliente.setCasoList(new ArrayList<Caso>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Cliente idCliente = emailCliente.getCliente();
            if (idCliente != null) {
                if (idCliente.getIdCliente() != null) {
                    idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                    emailCliente.setCliente(idCliente);
                } else {
                    emailCliente.setCliente(idCliente);
                    em.persist(idCliente);
                }
            }

            em.persist(emailCliente);
            if (idCliente != null) {
                if (idCliente.getEmailClienteList() == null) {
                    idCliente.setEmailClienteList(new ArrayList<EmailCliente>());
                }
                idCliente.getEmailClienteList().add(emailCliente);
                idCliente = em.merge(idCliente);
            }

            utx.commit();
        } catch (Exception ex) {
            if (ex instanceof ConstraintViolationException) {
                printOutContraintViolation((ConstraintViolationException) ex);
            }

            if (ex.getCause() instanceof ConstraintViolationException) {
                printOutContraintViolation((ConstraintViolationException) (ex.getCause()));
            }
            ex.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception re) {
                re.printStackTrace();
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEmailCliente(emailCliente.getEmailCliente()) != null) {
                throw new PreexistingEntityException("EmailCliente " + emailCliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EmailCliente emailCliente) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            EmailCliente persistentEmailCliente = em.find(EmailCliente.class, emailCliente.getEmailCliente());
            Cliente idClienteOld = persistentEmailCliente.getCliente();
            Cliente idClienteNew = emailCliente.getCliente();

            final Cliente existentCliente = (Cliente) em.createNamedQuery("Cliente.findByRut").setParameter("rut", idClienteNew.getRut()).getSingleResult();

            if (existentCliente != null && existentCliente.getIdCliente() != idClienteNew.getIdCliente()) {
                //There is another client using this RUT.
                throw new Exception("El Rut ya está asociado al cliente " + existentCliente.getNombres() + " " + existentCliente.getApellidos());
            }
            List<ProductoContratado> ProductoContratadoListOld = idClienteOld.getProductoContratadoList();
            List<ProductoContratado> ProductoContratadoListNew = idClienteNew.getProductoContratadoList();
//            if (idClienteNew != null) {
//                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
//                emailCliente.setCliente(idClienteNew);
//            }
            List<ProductoContratado> attachedProductoContratadoListNew = new ArrayList<ProductoContratado>();
            for (ProductoContratado productoContratadoListNewProductoContratadoToAttach : ProductoContratadoListNew) {
                ProductoContratado productoContratadoListNewProductoContratadoToAttach2 = em.find(ProductoContratado.class, productoContratadoListNewProductoContratadoToAttach.getProductoContratadoPK());

                if (productoContratadoListNewProductoContratadoToAttach2 == null) {
                    em.persist(productoContratadoListNewProductoContratadoToAttach);
                }
                attachedProductoContratadoListNew.add(productoContratadoListNewProductoContratadoToAttach);
            }
            ProductoContratadoListNew = attachedProductoContratadoListNew;
            emailCliente.getCliente().setProductoContratadoList(ProductoContratadoListNew);
            emailCliente = em.merge(emailCliente);
//            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
//                idClienteOld.getEmailClienteList().remove(emailCliente);
//                idClienteOld = em.merge(idClienteOld);
//            }
//            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
//                idClienteNew.getEmailClienteList().add(emailCliente);
//                idClienteNew = em.merge(idClienteNew);
//            }
            for (ProductoContratado ProductoContratadoListOldProductoContratado : ProductoContratadoListOld) {
                if (!ProductoContratadoListNew.contains(ProductoContratadoListOldProductoContratado)) {
                    em.remove(ProductoContratadoListOldProductoContratado);
                }
            }

//            for (ProductoContratado ProductoContratadoListNewProductoContratado : ProductoContratadoListNew) {
//                if (!ProductoContratadoListOld.contains(ProductoContratadoListNewProductoContratado)) {
//                    EmailCliente oldEmailClienteOfProductoContratadoListNewProductoContratado = ProductoContratadoListNewProductoContratado.getEmailCliente();
//                    ProductoContratadoListNewProductoContratado.setEmailCliente(emailCliente);
//                    ProductoContratadoListNewProductoContratado = em.merge(ProductoContratadoListNewProductoContratado);
//                    if (oldEmailClienteOfProductoContratadoListNewProductoContratado != null && !oldEmailClienteOfProductoContratadoListNewProductoContratado.equals(emailCliente)) {
//                        oldEmailClienteOfProductoContratadoListNewProductoContratado.getProductoContratadoList().remove(ProductoContratadoListNewProductoContratado);
//                        oldEmailClienteOfProductoContratadoListNewProductoContratado = em.merge(oldEmailClienteOfProductoContratadoListNewProductoContratado);
//                    }
//                }
//            }
            utx.commit();
        } catch (Exception ex) {
            if (ex instanceof ConstraintViolationException) {
                printOutContraintViolation((ConstraintViolationException) ex);
            }

            if (ex.getCause() instanceof ConstraintViolationException) {
                printOutContraintViolation((ConstraintViolationException) (ex.getCause()));
            }
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = emailCliente.getEmailCliente();
                if (findEmailCliente(id) == null) {
                    throw new NonexistentEntityException("The emailCliente with id " + id + " no longer exists.");
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
            EmailCliente emailCliente;
            try {
                emailCliente = em.getReference(EmailCliente.class, id);
                emailCliente.getEmailCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The emailCliente with id " + id + " no longer exists.", enfe);
            }
            Cliente idCliente = emailCliente.getCliente();
            if (idCliente != null) {
                idCliente.getEmailClienteList().remove(emailCliente);
                idCliente = em.merge(idCliente);
            }
            List<Caso> casoList = emailCliente.getCasoList();
            for (Caso casoListCaso : casoList) {
                casoListCaso.setEmailCliente(null);
                casoListCaso = em.merge(casoListCaso);
            }
            em.remove(emailCliente);
            utx.commit();
        } catch (Exception ex) {
            if (ex instanceof ConstraintViolationException) {
                printOutContraintViolation((ConstraintViolationException) ex);
            }

            if (ex.getCause() instanceof ConstraintViolationException) {
                printOutContraintViolation((ConstraintViolationException) (ex.getCause()));
            }
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

    public List<EmailCliente> findEmailClienteEntities() {
        return findEmailClienteEntities(true, -1, -1);
    }

    public List<EmailCliente> findEmailClienteEntities(int maxResults, int firstResult) {
        return findEmailClienteEntities(false, maxResults, firstResult);
    }

    private List<EmailCliente> findEmailClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EmailCliente.class));
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

    public EmailCliente findEmailCliente(String id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(EmailCliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmailClienteCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EmailCliente> rt = cq.from(EmailCliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    private void printOutContraintViolation(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> set = (ex).getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : set) {
            System.out.println("leafBean class: " + constraintViolation.getLeafBean().getClass());
            Iterator<Path.Node> iter = constraintViolation.getPropertyPath().iterator();
            System.out.println("constraintViolation.getPropertyPath(): ");
            while (iter.hasNext()) {
                System.out.print(iter.next().getName() + "/");
            }
            System.out.println("anotacion: " + constraintViolation.getConstraintDescriptor().getAnnotation().toString() + " value:" + constraintViolation.getInvalidValue());
        }
    }
}
