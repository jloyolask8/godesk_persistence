/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import com.itcs.helpdesk.persistence.entities.Cliente;
import com.itcs.helpdesk.persistence.entities.EmailCliente;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.PreexistingEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.eclipse.persistence.config.EntityManagerProperties;

/**
 *
 * @author jonathan
 */
public class ClienteJpaController extends AbstractJPAController implements Serializable {

    public ClienteJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class ClienteJpaController implements Serializable {
//
//    public ClienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

//    public List<Cliente> findByRut(String rut) {
//        return (List<Cliente>) getEntityManager().createNamedQuery("Cliente.findByRut").setParameter("rut", rut).getResultList();
//    }
   

   

    public void create(Cliente cliente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (cliente.getEmailClienteList() == null) {
            cliente.setEmailClienteList(new ArrayList<EmailCliente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            List<EmailCliente> attachedEmailClienteList = new ArrayList<EmailCliente>();
            for (EmailCliente emailClienteListEmailClienteToAttach : cliente.getEmailClienteList()) {
                if (em.find(EmailCliente.class, emailClienteListEmailClienteToAttach.getEmailCliente()) == null) {
                    em.persist(emailClienteListEmailClienteToAttach);
                    //emailClienteListEmailClienteToAttach = em.getReference(emailClienteListEmailClienteToAttach.getClass(), emailClienteListEmailClienteToAttach.getEmailCliente());
                }

                attachedEmailClienteList.add(emailClienteListEmailClienteToAttach);
            }
            cliente.setEmailClienteList(attachedEmailClienteList);
            em.persist(cliente);
            for (EmailCliente emailClienteListEmailCliente : cliente.getEmailClienteList()) {
                Cliente oldIdClienteOfEmailClienteListEmailCliente = emailClienteListEmailCliente.getCliente();
                emailClienteListEmailCliente.setCliente(cliente);
                emailClienteListEmailCliente = em.merge(emailClienteListEmailCliente);
                if (oldIdClienteOfEmailClienteListEmailCliente != null) {
                    oldIdClienteOfEmailClienteListEmailCliente.getEmailClienteList().remove(emailClienteListEmailCliente);
                    oldIdClienteOfEmailClienteListEmailCliente = em.merge(oldIdClienteOfEmailClienteListEmailCliente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCliente(cliente.getIdCliente()) != null) {
                throw new PreexistingEntityException("Cliente " + cliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdCliente());
            List<EmailCliente> emailClienteListOld = persistentCliente.getEmailClienteList();
            List<EmailCliente> emailClienteListNew = cliente.getEmailClienteList();

            Cliente existentCliente = null;
            try {
                existentCliente = (Cliente) em.createNamedQuery("Cliente.findByRut").setParameter("rut", cliente.getRut()).getSingleResult();
            } catch (NoResultException not) {
                existentCliente = null;
            }
            if (existentCliente != null && existentCliente.getIdCliente() != cliente.getIdCliente()) {
                //There is another client using this RUT.
                throw new Exception("El Rut ya está asociado a otro cliente: " + existentCliente.getCapitalName());
            }

            List<EmailCliente> attachedEmailClienteListNew = new ArrayList<EmailCliente>();
            for (EmailCliente emailClienteListNewEmailClienteToAttach : emailClienteListNew) {
                emailClienteListNewEmailClienteToAttach = em.getReference(emailClienteListNewEmailClienteToAttach.getClass(), emailClienteListNewEmailClienteToAttach.getEmailCliente());
                attachedEmailClienteListNew.add(emailClienteListNewEmailClienteToAttach);
            }
            emailClienteListNew = attachedEmailClienteListNew;
            cliente.setEmailClienteList(emailClienteListNew);
            cliente = em.merge(cliente);
            for (EmailCliente emailClienteListOldEmailCliente : emailClienteListOld) {
                if (!emailClienteListNew.contains(emailClienteListOldEmailCliente)) {
                    emailClienteListOldEmailCliente.setCliente(null);
                    emailClienteListOldEmailCliente = em.merge(emailClienteListOldEmailCliente);
                }
            }
            for (EmailCliente emailClienteListNewEmailCliente : emailClienteListNew) {
                if (!emailClienteListOld.contains(emailClienteListNewEmailCliente)) {
                    Cliente oldIdClienteOfEmailClienteListNewEmailCliente = emailClienteListNewEmailCliente.getCliente();
                    emailClienteListNewEmailCliente.setCliente(cliente);
                    emailClienteListNewEmailCliente = em.merge(emailClienteListNewEmailCliente);
                    if (oldIdClienteOfEmailClienteListNewEmailCliente != null && !oldIdClienteOfEmailClienteListNewEmailCliente.equals(cliente)) {
                        oldIdClienteOfEmailClienteListNewEmailCliente.getEmailClienteList().remove(emailClienteListNewEmailCliente);
                        oldIdClienteOfEmailClienteListNewEmailCliente = em.merge(oldIdClienteOfEmailClienteListNewEmailCliente);
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
                Integer id = cliente.getIdCliente();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<EmailCliente> emailClienteList = cliente.getEmailClienteList();
            for (EmailCliente emailClienteListEmailCliente : emailClienteList) {
                emailClienteListEmailCliente.setCliente(null);
                emailClienteListEmailCliente = em.merge(emailClienteListEmailCliente);
            }
            em.remove(cliente);
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

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
