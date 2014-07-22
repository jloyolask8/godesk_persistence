/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import com.itcs.helpdesk.persistence.entities.Attachment;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.itcs.helpdesk.persistence.entities.Caso;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.PreexistingEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.RollbackFailureException;
import com.itcs.jpautils.EasyCriteriaQuery;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.eclipse.persistence.config.EntityManagerProperties;

/**
 *
 * @author jonathan
 */
public class AttachmentJpaController extends AbstractJPAController implements Serializable {

    public AttachmentJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//public class AttachmentJpaController implements Serializable {
//
//    public AttachmentJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(Attachment attachment) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
//            Caso idCaso = attachment.getIdCaso();
//            if (idCaso != null) {
//                idCaso = em.getReference(idCaso.getClass(), idCaso.getIdCaso());
////                System.out.println("getReference:"+idCaso);
//                attachment.setIdCaso(idCaso);
//            }
            em.persist(attachment);
//            if (idCaso != null) {
//                idCaso.getAttachmentList().add(attachment);
//                idCaso = em.merge(idCaso);
//            }
            utx.commit();
        } catch (Exception ex) {

//            System.out.println(attachment.toString());
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAttachment(attachment.getIdAttachment()) != null) {
                throw new PreexistingEntityException("Attachment " + attachment + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Attachment attachment) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Attachment persistentAttachment = em.find(Attachment.class, attachment.getIdAttachment());
            Caso idCasoOld = persistentAttachment.getIdCaso();
            Caso idCasoNew = attachment.getIdCaso();
            if (idCasoNew != null) {
                idCasoNew = em.getReference(idCasoNew.getClass(), idCasoNew.getIdCaso());
                attachment.setIdCaso(idCasoNew);
            }
            attachment = em.merge(attachment);
            if (idCasoOld != null && !idCasoOld.equals(idCasoNew)) {
                idCasoOld.getAttachmentList().remove(attachment);
                idCasoOld = em.merge(idCasoOld);
            }
            if (idCasoNew != null && !idCasoNew.equals(idCasoOld)) {
                idCasoNew.getAttachmentList().add(attachment);
                idCasoNew = em.merge(idCasoNew);
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
                Long id = attachment.getIdAttachment();
                if (findAttachment(id) == null) {
                    throw new NonexistentEntityException("The attachment with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Attachment attachment;
            try {
                attachment = em.getReference(Attachment.class, id);
                attachment.getIdAttachment();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The attachment with id " + id + " no longer exists.", enfe);
            }
            Caso idCaso = attachment.getIdCaso();
            if (idCaso != null) {
                idCaso.getAttachmentList().remove(attachment);
                idCaso = em.merge(idCaso);
            }
            em.remove(attachment);
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

    public List<Attachment> findAttachmentEntities() {
        return findAttachmentEntities(true, -1, -1);
    }

    public List<Attachment> findAttachmentEntities(int maxResults, int firstResult) {
        return findAttachmentEntities(false, maxResults, firstResult);
    }

    private List<Attachment> findAttachmentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Attachment.class));
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

    public Attachment findByContentId(String contentId, Caso caso) {
        EasyCriteriaQuery<Attachment> easyCriteriaQuery = new EasyCriteriaQuery<Attachment>(emf, Attachment.class);
        easyCriteriaQuery.addEqualPredicate("contentId", contentId);
        easyCriteriaQuery.addEqualPredicate("idCaso", caso);
        System.out.println("buscando contentId: " + contentId);
        List<Attachment> res = easyCriteriaQuery.getAllResultList();
        if (res.size() > 0) {
            return res.get(0);
        }
        return null;
    }

    public Attachment findAttachment(Long id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Attachment.class, id);
        } finally {
            em.close();
        }
    }

//    public List<Attachment> findAttachmentsWOContentId(Caso caso) {
//        EasyCriteriaQuery<Attachment> easyCriteriaQuery = new EasyCriteriaQuery<Attachment>(emf, Attachment.class);
//        easyCriteriaQuery.addEqualPredicate(Attachment_.contentId.getName(), (Attachment) null);
//        easyCriteriaQuery.addEqualPredicate(Attachment_.idCaso.getName(), caso);
//        return easyCriteriaQuery.getAllResultList();
//    }
//
//    public Long countAttachmentsWOContentId(Caso caso) {
//        EasyCriteriaQuery<Attachment> easyCriteriaQuery = new EasyCriteriaQuery<Attachment>(emf, Attachment.class);
//        easyCriteriaQuery.addEqualPredicate(Attachment_.contentId.getName(), (Attachment) null);
//        easyCriteriaQuery.addEqualPredicate(Attachment_.idCaso.getName(), caso);
//        return easyCriteriaQuery.count();
//    }
//
//    public Long countAttachmentsWContentId(Caso caso) {
//        EasyCriteriaQuery<Attachment> easyCriteriaQuery = new EasyCriteriaQuery<Attachment>(emf, Attachment.class);
//        easyCriteriaQuery.addDistinctPredicate(Attachment_.contentId.getName(), (Attachment) null);
//        easyCriteriaQuery.addEqualPredicate(Attachment_.idCaso.getName(), caso);
//        return easyCriteriaQuery.count();
//    }

    public int getAttachmentCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Attachment> rt = cq.from(Attachment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
