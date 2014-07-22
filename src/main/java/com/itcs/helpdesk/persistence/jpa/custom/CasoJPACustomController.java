/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa.custom;

import com.itcs.helpdesk.persistence.entities.Caso;
import com.itcs.helpdesk.persistence.entities.EstadoCaso;
import com.itcs.helpdesk.persistence.entities.FiltroVista;
import com.itcs.helpdesk.persistence.entities.Producto;
import com.itcs.helpdesk.persistence.entities.TipoAlerta;
import com.itcs.helpdesk.persistence.entities.TipoCaso;
import com.itcs.helpdesk.persistence.entities.Usuario;
import com.itcs.helpdesk.persistence.entityenums.EnumEstadoCaso;
import com.itcs.helpdesk.persistence.jpa.CasoJpaController;
import com.itcs.jpautils.EasyCriteriaQuery;
import java.util.List;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.eclipse.persistence.config.EntityManagerProperties;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author jonathan
 */
public class CasoJPACustomController extends CasoJpaController {

    public CasoJPACustomController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }

    public List<Caso> findByRutCliente(String rut) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.createNamedQuery("Caso.findByRutCliente").setParameter("rutCliente", rut).getResultList();
        } finally {
            em.close();
        }

    }

    public List<Caso> findByEmailCliente(String emailCliente) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.createNamedQuery("Caso.findByEmailCliente").setParameter("emailCliente", emailCliente).getResultList();
        } finally {
            em.close();
        }

    }

    /*
     * @deprecated 
     * please improve this code. 
     */
    public List<Caso> getCasoFindByEstadoAndAlerta(EstadoCaso estado, TipoAlerta tipoAlerta) {
    
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.createNamedQuery("Caso.findByEstadoAndTipoAlerta")
                    .setParameter("idEstado", estado.getIdEstado())
                    .setParameter("estadoAlerta", tipoAlerta.getIdalerta())
                    .getResultList();
        } finally {
            em.close();
        }
    }

   

    public int countCasos(Predicate predicate, EntityManager em, CriteriaBuilder cb, CriteriaQuery cq, Root<Caso> rt) {
        if (predicate == null) {
            cq.select(cb.count(rt));
        } else {
            cq.select(cb.count(rt)).where(predicate);
        }
        Query q = em.createQuery(cq);
        int retorno = ((Long) q.getSingleResult()).intValue();
        return retorno;
    }

    public Caso findCasoBy(String emailCliente, Producto idProducto, EstadoCaso idEstado, TipoCaso tipoCaso) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            TypedQuery<Caso> query
                    = em.createQuery("SELECT c FROM Caso c WHERE c.idEstado = :idEstado AND c.emailCliente.emailCliente = :emailCliente AND c.tipoCaso = :tipoCaso AND c.idProducto = :idProducto", Caso.class);
            query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);
            query.setParameter("idEstado", idEstado);
            query.setParameter("emailCliente", emailCliente);
            query.setParameter("tipoCaso", tipoCaso);
            query.setParameter("idProducto", idProducto);

            return query.getSingleResult();

        } catch (NoResultException no) {
            return null;
        } catch (NonUniqueResultException noU) {
            noU.printStackTrace();
            return null;
        } catch (IllegalStateException ill) {//- if called for a Java Persistence query language UPDATE or DELETE statement
            ill.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public Caso findCasoByIdEmailCliente(String email, Long idCaso) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            TypedQuery<Caso> query
                    = em.createQuery("SELECT c FROM Caso c WHERE c.idCaso = :idCaso AND c.emailCliente.emailCliente = :email", Caso.class);
            query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);
            query.setParameter("idCaso", idCaso);
            query.setParameter("email", email);

            return query.getSingleResult();

        } catch (NoResultException no) {
            return null;
        } catch (NonUniqueResultException noU) {
            noU.printStackTrace();
            return null;
        } catch (IllegalStateException ill) {//- if called for a Java Persistence query language UPDATE or DELETE statement
            ill.printStackTrace();
            return null;
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
        return false;
    }
}
