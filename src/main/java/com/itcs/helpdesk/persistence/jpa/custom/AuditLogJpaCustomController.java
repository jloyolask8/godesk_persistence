package com.itcs.helpdesk.persistence.jpa.custom;

import com.itcs.helpdesk.persistence.entities.AuditLog;
import com.itcs.helpdesk.persistence.jpa.AuditLogJpaController;
import com.itcs.helpdesk.persistence.utils.vo.AuditLogVO;
import com.itcs.jpautils.EasyCriteriaQuery;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author jonathan
 * 
 */
public class AuditLogJpaCustomController extends AuditLogJpaController {

//    private EntityManagerFactory localEmf;

    public AuditLogJpaCustomController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
//        this.localEmf = emf;
    }

    public int countByFilterForAudit(AuditLogVO filter) {
        
        EasyCriteriaQuery<AuditLog> criteriaQueryCasos = new EasyCriteriaQuery<AuditLog>(emf, AuditLog.class);
        createPredicate(filter, criteriaQueryCasos);
        return criteriaQueryCasos.count().intValue();
        
    }

    public List<AuditLog> findAuditLogEntities(boolean all, int maxResults, int firstResult, AuditLogVO filter, boolean log) {

        EasyCriteriaQuery<AuditLog> criteriaQueryCasos = new EasyCriteriaQuery<AuditLog>(emf, AuditLog.class);
        filter.setAlertLevel(!log);
        createPredicate(filter, criteriaQueryCasos);
        criteriaQueryCasos.orderBy("fecha", false);

        if (!all) {
            criteriaQueryCasos.setFirstResult(firstResult);
            criteriaQueryCasos.setMaxResults(maxResults);
            return criteriaQueryCasos.next();
        }

        return criteriaQueryCasos.getAllResultList();
    }

    private void createPredicate(AuditLogVO filter, EasyCriteriaQuery<AuditLog> criteriaQueryCasos) {
        if (filter.isAlertLevel()) {
            criteriaQueryCasos.addEqualPredicate("campo", "EstadoAlerta");
        }

        if (filter.getIdCaso() != null) {
            criteriaQueryCasos.addEqualPredicate("idCaso", filter.getIdCaso());
        }

        if (filter.getFechaFin() != null && filter.getFechaInicio() != null) {
//            criteriaQueryCasos.addBetweenPredicate(AuditLog_.fecha, filter.getFechaInicio(), filter.getFechaFin());
        }
        if (filter.getIdOwner() != null) {
            criteriaQueryCasos.addEqualPredicate("owner", filter.getIdOwner().getIdUsuario());
        }
        if (filter.getIdUser() != null) {
            criteriaQueryCasos.addEqualPredicate("idUser", filter.getIdUser().getIdUsuario());
        }
    }
}
