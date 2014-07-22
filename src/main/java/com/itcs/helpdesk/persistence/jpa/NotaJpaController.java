/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import com.itcs.helpdesk.persistence.entities.Caso;
import com.itcs.helpdesk.persistence.entities.Nota;
import com.itcs.helpdesk.persistence.entities.TipoNota;
import com.itcs.helpdesk.persistence.entities.Usuario;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.PreexistingEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.eclipse.persistence.config.EntityManagerProperties;

/**
 *
 * @author jonathan
 */
public class NotaJpaController extends AbstractJPAController implements Serializable {

    public NotaJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class NotaJpaController implements Serializable {
//
//    public NotaJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(Nota nota) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Usuario creadaPor = nota.getCreadaPor();
            if (creadaPor != null) {
                creadaPor = em.getReference(creadaPor.getClass(), creadaPor.getIdUsuario());
                nota.setCreadaPor(creadaPor);
            }
            TipoNota idTipoNota = nota.getTipoNota();
            if (idTipoNota != null) {
                idTipoNota = em.getReference(idTipoNota.getClass(), idTipoNota.getIdTipoNota());
                nota.setTipoNota(idTipoNota);
            }
            Caso idCaso = nota.getIdCaso();
            if (idCaso != null) {
                idCaso = em.getReference(idCaso.getClass(), idCaso.getIdCaso());
                nota.setIdCaso(idCaso);
            }
            em.persist(nota);
            if (creadaPor != null) {
                creadaPor.getNotaList().add(nota);
                creadaPor = em.merge(creadaPor);
            }
            if (idTipoNota != null) {
                idTipoNota.getNotaList().add(nota);
                idTipoNota = em.merge(idTipoNota);
            }
//            if (idCaso != null) {
//                idCaso.getNotaList().add(nota);
//                idCaso = em.merge(idCaso);
//            }
            utx.commit();
        } catch (Exception ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                Set<ConstraintViolation<?>> set = ((ConstraintViolationException) ex.getCause()).getConstraintViolations();
                for (ConstraintViolation<?> constraintViolation : set) {
                    System.out.println("getPropertyPath: " + constraintViolation.getPropertyPath().toString());
                    System.out.println("getInvalidValue: " + constraintViolation.getInvalidValue());
                    System.out.println("getConstraintDescriptor: " + constraintViolation.getConstraintDescriptor());
                    System.out.println("getMessageTemplate: " + constraintViolation.getMessageTemplate());
                    System.out.println("getMessage: " + constraintViolation.getMessage());
                    System.out.println("leafBean class: " + constraintViolation.getLeafBean().getClass());
                    System.out.println("leafBean toString: " + constraintViolation.getLeafBean().toString());
                    System.out.println("anotacion: " + constraintViolation.getConstraintDescriptor().getAnnotation().toString() + " value:" + constraintViolation.getInvalidValue());
                    System.out.println("getPayload: " + constraintViolation.getConstraintDescriptor().getPayload() + " value:" + constraintViolation.getConstraintDescriptor().getPayload().getClass());
                }
            }
            if (ex instanceof ConstraintViolationException) {
                Set<ConstraintViolation<?>> set = ((ConstraintViolationException) ex).getConstraintViolations();
                for (ConstraintViolation<?> constraintViolation : set) {
                    System.out.println("getPropertyPath: " + constraintViolation.getPropertyPath().toString());
                    System.out.println("getInvalidValue: " + constraintViolation.getInvalidValue());
                    System.out.println("getConstraintDescriptor: " + constraintViolation.getConstraintDescriptor());
                    System.out.println("getMessageTemplate: " + constraintViolation.getMessageTemplate());
                    System.out.println("getMessage: " + constraintViolation.getMessage());
                    System.out.println("leafBean class: " + constraintViolation.getLeafBean().getClass());
                    System.out.println("leafBean toString: " + constraintViolation.getLeafBean().toString());
                    System.out.println("anotacion: " + constraintViolation.getConstraintDescriptor().getAnnotation().toString() + " value:" + constraintViolation.getInvalidValue());
                    System.out.println("getPayload: " + constraintViolation.getConstraintDescriptor().getPayload() + " value:" + constraintViolation.getConstraintDescriptor().getPayload().getClass());
                }
            }
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findNota(nota.getIdNota()) != null) {
                throw new PreexistingEntityException("Nota " + nota + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nota nota) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Nota persistentNota = em.find(Nota.class, nota.getIdNota());
            Usuario creadaPorOld = persistentNota.getCreadaPor();
            Usuario creadaPorNew = nota.getCreadaPor();
            TipoNota idTipoNotaOld = persistentNota.getTipoNota();
            TipoNota idTipoNotaNew = nota.getTipoNota();
            Caso idCasoOld = persistentNota.getIdCaso();
            Caso idCasoNew = nota.getIdCaso();
            if (creadaPorNew != null) {
                creadaPorNew = em.getReference(creadaPorNew.getClass(), creadaPorNew.getIdUsuario());
                nota.setCreadaPor(creadaPorNew);
            }
            if (idTipoNotaNew != null) {
                idTipoNotaNew = em.getReference(idTipoNotaNew.getClass(), idTipoNotaNew.getIdTipoNota());
                nota.setTipoNota(idTipoNotaNew);
            }
            if (idCasoNew != null) {
                idCasoNew = em.getReference(idCasoNew.getClass(), idCasoNew.getIdCaso());
                nota.setIdCaso(idCasoNew);
            }
            nota = em.merge(nota);
            if (creadaPorOld != null && !creadaPorOld.equals(creadaPorNew)) {
                creadaPorOld.getNotaList().remove(nota);
                creadaPorOld = em.merge(creadaPorOld);
            }
            if (creadaPorNew != null && !creadaPorNew.equals(creadaPorOld)) {
                creadaPorNew.getNotaList().add(nota);
                creadaPorNew = em.merge(creadaPorNew);
            }
            if (idTipoNotaOld != null && !idTipoNotaOld.equals(idTipoNotaNew)) {
                idTipoNotaOld.getNotaList().remove(nota);
                idTipoNotaOld = em.merge(idTipoNotaOld);
            }
            if (idTipoNotaNew != null && !idTipoNotaNew.equals(idTipoNotaOld)) {
                idTipoNotaNew.getNotaList().add(nota);
                idTipoNotaNew = em.merge(idTipoNotaNew);
            }
            if (idCasoOld != null && !idCasoOld.equals(idCasoNew)) {
                idCasoOld.getNotaList().remove(nota);
                idCasoOld = em.merge(idCasoOld);
            }
//            if (idCasoNew != null && !idCasoNew.equals(idCasoOld)) {
//                idCasoNew.getNotaList().add(nota);
//                idCasoNew = em.merge(idCasoNew);
//            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nota.getIdNota();
                if (findNota(id) == null) {
                    throw new NonexistentEntityException("The nota with id " + id + " no longer exists.");
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
            Nota nota;
            try {
                nota = em.getReference(Nota.class, id);
                nota.getIdNota();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nota with id " + id + " no longer exists.", enfe);
            }
            Usuario creadaPor = nota.getCreadaPor();
            if (creadaPor != null) {
                creadaPor.getNotaList().remove(nota);
                creadaPor = em.merge(creadaPor);
            }
            TipoNota idTipoNota = nota.getTipoNota();
            if (idTipoNota != null) {
                idTipoNota.getNotaList().remove(nota);
                idTipoNota = em.merge(idTipoNota);
            }
            Caso idCaso = nota.getIdCaso();
            if (idCaso != null) {
                idCaso.getNotaList().remove(nota);
                idCaso = em.merge(idCaso);
            }
            em.remove(nota);
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

    public List<Nota> findNotaEntities() {
        return findNotaEntities(true, -1, -1);
    }

    public List<Nota> findNotaEntities(int maxResults, int firstResult) {
        return findNotaEntities(false, maxResults, firstResult);
    }

    private List<Nota> findNotaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nota.class));
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

    public Nota findNota(Integer id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Nota.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotaCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nota> rt = cq.from(Nota.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
