package com.itcs.helpdesk.persistence.jpa.service;

import com.itcs.helpdesk.persistence.entities.Archivo;
import com.itcs.helpdesk.persistence.entities.Area;
import com.itcs.helpdesk.persistence.entities.Attachment;
import com.itcs.helpdesk.persistence.entities.AuditLog;
import com.itcs.helpdesk.persistence.entities.Canal;
import com.itcs.helpdesk.persistence.entities.Caso;
import com.itcs.helpdesk.persistence.entities.Categoria;
import com.itcs.helpdesk.persistence.entities.Cliente;
import com.itcs.helpdesk.persistence.entities.Componente;
import com.itcs.helpdesk.persistence.entities.Condicion;
import com.itcs.helpdesk.persistence.entities.CustomField;
import com.itcs.helpdesk.persistence.entities.EmailCliente;
import com.itcs.helpdesk.persistence.entities.EstadoCaso;
import com.itcs.helpdesk.persistence.entities.Etiqueta;
import com.itcs.helpdesk.persistence.entities.FieldType;
import com.itcs.helpdesk.persistence.entities.FiltroVista;
import com.itcs.helpdesk.persistence.entities.Grupo;
import com.itcs.helpdesk.persistence.entities.Nota;
import com.itcs.helpdesk.persistence.entities.Prioridad;
import com.itcs.helpdesk.persistence.entities.Producto;
import com.itcs.helpdesk.persistence.entities.ReglaTrigger;
import com.itcs.helpdesk.persistence.entities.Resource;
import com.itcs.helpdesk.persistence.entities.Rol;
import com.itcs.helpdesk.persistence.entities.SubComponente;
import com.itcs.helpdesk.persistence.entities.SubEstadoCaso;
import com.itcs.helpdesk.persistence.entities.TipoAlerta;
import com.itcs.helpdesk.persistence.entities.TipoCaso;
import com.itcs.helpdesk.persistence.entities.TipoComparacion;
import com.itcs.helpdesk.persistence.entities.Usuario;
import com.itcs.helpdesk.persistence.entities.Vista;
import com.itcs.helpdesk.persistence.entityenums.EnumTipoComparacion;
import com.itcs.helpdesk.persistence.jpa.AbstractJPAController;
import com.itcs.helpdesk.persistence.jpa.custom.CriteriaQueryHelper;
import com.itcs.helpdesk.persistence.jpa.exceptions.IllegalOrphanException;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.PreexistingEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.RollbackFailureException;
import com.itcs.helpdesk.persistence.utils.CasoChangeListener;
import com.itcs.helpdesk.persistence.utils.OrderBy;
import com.itcs.jpautils.EasyCriteriaQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.resource.NotSupportedException;
import javax.transaction.UserTransaction;
import org.apache.commons.lang.WordUtils;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.sessions.Session;

public class JPAServiceFacade extends AbstractJPAController {

    public static final String CASE_CUSTOM_FIELD = "case";
    public static final String CLIENT_CUSTOM_FIELD = "client";
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
    //--------------specific controllers wrapped by this service------------------------
//    private CasoJPACustomController casoJpa;
//    private AuditLogJpaCustomController auditLogJpa;
//    private UsuarioJpaController usuarioJpaController;
//    private UsuarioJpaCustomController usuarioJpaCustomController;
//    private RolJpaController rolJpaController;
//    private NotaJpaController notaJpaController;
//    private EmailClienteJpaCustomController emailClienteJpaController;
//    private ClienteJpaController clienteJpaController;
//    private PrioridadJpaController prioridadJpaController;
//    private GrupoJpaController grupoJpaController;
//    private CategoriaJpaController categoriaJpaController;
//    private AreaJpaController areaJpaController;
//    private CanalJpaController canalJpaController;
//    private TipoNotaJpaController tipoNotaJpaController;
//    private AccionJpaController accionJpaController;
//    private ArchivoJpaController archivoJpaController;
//    private AttachmentJpaController attachmentJpaController;
//    private ComponenteJpaController componenteJpaController;
//    private CondicionJpaController condicionJpaController;
//    private EstadoCasoJpaController estadoCasoJpaController;
//    private FuncionJpaController funcionJpaController;
//    private TipoAccionJpaController tipoAccionJpaController;
//    private ProductoJpaController productoJpaController;
//    private ReglaTriggerJpaController reglaTriggerJpaController;
//    private SubComponenteJpaController subComponenteJpaController;
//    private SubEstadoCasoJpaController subEstadoCasoJpaController;
//    private TipoAlertaJpaController tipoAlertaJpaController;
//    private VistaJpaController vistaJpaController;
//    private FiltroVistaJpaController filtroVistaJpaController;
//    private AppSettingJpaController appSettingJpaController;
//    private TipoComparacionJpaController tipoComparacionJpaController;
//    private ClippingJpaController clippingJpaController;
    //--------------end of specific controllers wrapped by this service------------------------
    private CasoChangeListener casoChangeListener;

    public JPAServiceFacade(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }

//    public JPAServiceFacade(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    @Override
//    protected EntityManager getEntityManager() {
//        EntityManager em = emf.createEntityManager();
//        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, "public");//Make schema dynamic
//        return em;
//    }
    /**
     * Returns the id of the entity. A generated id is not guaranteed to be
     * available until after the database insert has occurred. Returns null if
     * the entity does not yet have an id
     *
     * @param entity
     * @return id of the entity
     * @throws IllegalStateException if the entity is found not to be an entity.
     */
    public Object getIdentifier(Object entity) {
        EntityManager em = createEntityManager();

        try {
            AbstractSession session = (AbstractSession) em.unwrap(Session.class);
            ClassDescriptor descriptor = session.getDescriptor(entity);
            if (descriptor.getPrimaryKeyFields().size() != 1) {
                throw new NotSupportedException("Composite PK is not supported yet!");
            }
            String methodName = null;
            for (DatabaseField databaseField : descriptor.getPrimaryKeyFields()) {
                methodName = createGetIdentifierMethodName(databaseField.getName());
            }

            java.beans.Expression expresion;
            expresion = new java.beans.Expression(entity, methodName, new Object[0]);

            expresion.execute();
            return expresion.getValue();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }

        return null;
    }

    public Object getIdentifier(EntityManager em, Object entity) {
        try {
            AbstractSession session = (AbstractSession) em.unwrap(Session.class);
            ClassDescriptor descriptor = session.getDescriptor(entity);
            if (descriptor.getPrimaryKeyFields().size() != 1) {
                throw new NotSupportedException("Composite PK is not supported yet!");
            }
            String methodName = null;
            for (DatabaseField databaseField : descriptor.getPrimaryKeyFields()) {
                methodName = createGetIdentifierMethodName(databaseField.getName());
            }

            java.beans.Expression expresion;
            expresion = new java.beans.Expression(entity, methodName, new Object[0]);

            expresion.execute();
            return expresion.getValue();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String createGetIdentifierMethodName(String name) { // "MY_COLUMN"
        String name0 = name.replace("_", " "); // to "MY COLUMN"
        name0 = WordUtils.capitalizeFully(name0); // to "My Column"
        name0 = name0.replace(" ", ""); // to "MyColumn"
//        name0 = WordUtils.uncapitalize(name0); // to "myColumn"
        return "get" + name0;
    }

    //find code, not in a transaction but still setting tenant schema
    public <T extends Object> T find(Class<T> entityClass, Object id) {
        EntityManager em = createEntityManager();

        T entity = createEntityManager().find(entityClass, id);
        em.close();
        return entity;
    }

    public <T extends Object> T getReference(Class<T> entityClass, Object id) {
        EntityManager em = createEntityManager();

        T entity = createEntityManager().getReference(entityClass, id);
        em.close();
        return entity;
    }

    public void refresh(Object o) {
        createEntityManager().refresh(o);
        EntityManager em = createEntityManager();

        em.refresh(o);
        em.close();
    }

    public void persist(Object o) throws Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.persist(o);
            utx.commit();
        } catch (Exception ex) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    public void remove(Object o) throws Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();

            em.remove(em.getReference(o.getClass(), getIdentifier(em, o)));
            utx.commit();
        } catch (Exception ex) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    public void remove(Class clazz, Object pk) throws Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();

            em.remove(em.getReference(clazz, pk));
            utx.commit();
        } catch (Exception ex) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    public void merge(Object o) throws Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.merge(o);
            utx.commit();
        } catch (Exception ex) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    /**
     * @deprecated
     *
     */
    public Long count(Class entityClass) {
        EasyCriteriaQuery q = new EasyCriteriaQuery(emf, entityClass);
        return q.count();
    }

    public Long countEntities(Vista vista) throws ClassNotFoundException {
        return countEntities(vista, null);
    }

    public Long countEntities(Vista vista, Usuario who) throws ClassNotFoundException {
        EntityManager em = createEntityManager();
        em.setProperty("javax.persistence.cache.storeMode", javax.persistence.CacheRetrieveMode.USE);

        try {
            final Class<?> clazz = Class.forName(vista.getBaseEntityType());
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery();
            Root root = criteriaQuery.from(clazz);
            Predicate predicate = createPredicate(em, criteriaBuilder, root, vista, who);

            if (predicate != null) {
                criteriaQuery.select(criteriaBuilder.count(root)).where(predicate).distinct(true);
            } else {
                criteriaQuery.select(criteriaBuilder.count(root));
            }
            Query q = em.createQuery(criteriaQuery);
            q.setHint("eclipselink.query-results-cache", true);
            return ((Long) q.getSingleResult());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, "countEntities by view " + vista, ex);
            throw ex;
        } catch (Exception e) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, "countEntities by view " + vista, e);
            return 0L;
        } finally {
            em.close();
        }
    }

    public List<?> findAllEntities(Class entityClass, Vista vista, OrderBy orderBy, Usuario who) throws NotSupportedException, ClassNotFoundException {
        return findEntities(entityClass, vista, true, -1, -1, orderBy, who);
    }

    public List<?> findEntities(Class entityClass, Vista vista, int maxResults, int firstResult, OrderBy orderBy, Usuario who) throws NotSupportedException, ClassNotFoundException {
        return findEntities(entityClass, vista, false, maxResults, firstResult, orderBy, who);
    }

//    public List<?> findEntities(Class entityClass, Vista vista, OrderBy orderBy, Usuario who) throws NotSupportedException, ClassNotFoundException {
//        return findEntities(entityClass, vista, true, -1, -1, orderBy, who);
//    }

    private List<?> findEntities(Class entityClass, Vista vista, boolean all, int maxResults, int firstResult, OrderBy orderBy, Usuario who) throws IllegalStateException, ClassNotFoundException {
        EntityManager em = createEntityManager();

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root root = criteriaQuery.from(entityClass);

            Predicate predicate = createPredicate(em, criteriaBuilder, root, vista, who);

            if (predicate != null) {
                criteriaQuery.where(predicate).distinct(true);
            }
            if (orderBy != null && orderBy.getFieldName() != null) {
                if (orderBy.getOrderType().equals(OrderBy.OrderType.ASC)) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(orderBy.getFieldName())));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderBy.getFieldName())));
                }

            }
            Query q = em.createQuery(criteriaQuery);

            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param rut
     * @return
     */
    public Cliente findClienteByRut(String rut) {
        try {
            if (rut != null && !rut.isEmpty()) {
                return (Cliente) createEntityManager().createNamedQuery("Cliente.findByRut").setParameter("rut", rut).getSingleResult();
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param email
     * @param idCaso
     * @return
     */
    public Caso findCasoByIdEmailCliente(String email, Long idCaso) {
        EntityManager em = createEntityManager();

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

    /**
     *
     * @param etiqueta
     * @return
     */
    public Long countCasosByEtiqueta(Etiqueta etiqueta) {
        EntityManager em = createEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Caso> root = cq.from(Caso.class);
            Expression<List<Etiqueta>> exp = root.get("etiquetaList");
            Predicate predicate = cb.isMember(etiqueta, exp);

            if (predicate != null) {
                cq.select(cb.count(root)).where(predicate);
                Query q = em.createQuery(cq);
                return (Long) q.getSingleResult();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            em.close();
        }
        return 0L;
    }

    /**
     *
     * @param idUsuario
     * @return
     */
    public List<Etiqueta> findEtiquetasByUsuario(String idUsuario) {

        EntityManager em = createEntityManager();

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Etiqueta> criteriaQuery = criteriaBuilder.createQuery(Etiqueta.class);
            Root<Etiqueta> root = criteriaQuery.from(Etiqueta.class);
            Expression<String> exp = root.get("owner").get("idUsuario");

            criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.desc(root.get("tagId")));
            Predicate predicate = criteriaBuilder.equal(exp, idUsuario);
            criteriaQuery.where(predicate);
            Query q = em.createQuery(criteriaQuery);
            return q.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;

        } finally {
            em.close();
        }
    }

    public List<Etiqueta> findEtiquetasLike(String etiquetaPattern, String idUsuario) {

        EntityManager em = createEntityManager();

        try {
            return em.createNamedQuery("Etiqueta.findByTagIdAndIdUsuario")
                    .setParameter("tagId", etiquetaPattern + "%")
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        } finally {
            em.close();
        }
    }

    public List queryByRange(Class entityClazz, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClazz));
//            Root root = cq.from(entityClazz);
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClazz);
            Query q = em.createQuery(criteriaQuery);

            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);

            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /*
     * Caso helper Methods
     */
    public List<Caso> findCasoEntities(Vista view, Usuario userWhoIsApplying, int maxResults, int firstResult, OrderBy orderBy) throws javax.resource.NotSupportedException, ClassNotFoundException {
        return findCasoEntities(view, userWhoIsApplying, false, maxResults, firstResult, orderBy);
    }

    public List<Caso> findCasoEntities(Vista view, Usuario userWhoIsApplying, OrderBy orderBy) throws IllegalStateException, ClassNotFoundException {
        return findCasoEntities(view, userWhoIsApplying, true, 0, 0, orderBy);
    }

    private List<Caso> findCasoEntities(Vista view, Usuario userWhoIsApplying, boolean all, int maxResults, int firstResult, OrderBy orderBy) throws IllegalStateException, ClassNotFoundException {
        return (List<Caso>) findEntities(Caso.class, view, all, maxResults, firstResult, orderBy, userWhoIsApplying);

    }

    /**
     *
     * @param caso
     * @param changeList
     * @return
     * @throws PreexistingEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public Caso persistCaso(Caso caso, List<AuditLog> changeList) throws PreexistingEntityException, RollbackFailureException, Exception {
        persist(caso);
        if (changeList != null) {
            for (AuditLog auditLog : changeList) {
                auditLog.setIdCaso(caso.getIdCaso());
                persist(auditLog);
            }
        }

        if (getCasoChangeListener() != null) {
            getCasoChangeListener().casoCreated(caso);
        }

        return caso;

    }

    public Caso mergeCaso(Caso caso, AuditLog log) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        List<AuditLog> changeList = new LinkedList<AuditLog>();
        changeList.add(log);
        return mergeCaso(caso, changeList);
    }

    public Caso mergeCaso(Caso caso, List<AuditLog> changeList) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        caso.setFechaModif(new Date());
        merge(caso);
        if (changeList != null) {
            for (AuditLog auditLog : changeList) {
                persist(auditLog);
            }
        }
        if (getCasoChangeListener() != null) {
            getCasoChangeListener().casoChanged(caso, changeList);
        } else {
            System.out.println("****** NO getCasoChangeListener configured!");
        }

        return (Caso) createEntityManager().getReference(Caso.class, caso.getIdCaso());
    }

    public Caso mergeCasoWithoutNotify(Caso caso) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        merge(caso);
        return (Caso) createEntityManager().getReference(Caso.class, caso.getIdCaso());
    }

//    public void removeCaso(Caso caso) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
//        getCasoJpa().destroy(caso.getIdCaso());
//    }
//
//    /**
//     * <
//     * code>SELECT c FROM Caso c</code>
//     */
//    public List<Caso> getCasoFindAll() {
////        throw new UnsupportedOperationException("Not implemented... You shoud use a paginated or parcial query instead of quering all the tuples in a table.");
//        return getCasoJpa().findCasoEntities();
//    }
//
//    /**
//     * <
//     * code>SELECT c FROM Caso c WHERE c.idCaso = :idCaso</code>
//     */
//    public Caso getCasoFindByIdCaso(Long idCaso) {
//        return getCasoJpa().findCaso(idCaso);
//    }
//
//    /**
//     * <
//     * code>SELECT c FROM Caso c WHERE c.emailCliente = :emailCliente</code>
//     */
//    public List<Caso> getCasoFindByEmailCliente(String emailCliente) {
//        return getCasoJpa().findByEmailCliente(emailCliente);
//    }
//
//    /**
//     * <
//     * code>SELECT c FROM Caso c WHERE c.rutCliente = :rutCliente</code>
//     */
//    public List<Caso> getCasoFindByRutCliente(String rutCliente) {
//        return getCasoJpa().findByRutCliente(rutCliente);
//    }
//
//    /*
//     * Audit Log Methods
//     */
//    public int getAuditLogCount(AuditLogVO alert, boolean log) {
//        alert.setAlertLevel(!log);
//        return getAuditLogJpaCustomController().countByFilterForAudit(alert);
//    }
//
//    /*
//     * Audit Log Methods
//     */
//    public List<AuditLog> findAuditLogEntities(AuditLogVO alert) {
//        return getAuditLogJpaCustomController().findAuditLogEntities(true, -1, -1, alert, true);
//    }
//
//    /*
//     * Audit Log Methods
//     */
//    public List<AuditLog> findAuditLogEntities(int maxResults, int firstResult, AuditLogVO alert, boolean log) {
//        return getAuditLogJpaCustomController().findAuditLogEntities(false, maxResults, firstResult, alert, log);
//    }
//    /**
//     * <
//     * code>SELECT c FROM CampoCompCaso c WHERE c.tipo = :tipo</code>
//     */
//    public List<CampoCompCaso> getCampoCompCasoFindByTipo(String tipo) {
//        return getEntityManager().createNamedQuery("CampoCompCaso.findByTipo").setParameter("tipo", tipo).getResultList();
//    }
//    /**
//     * <
//     * code>SELECT c FROM CampoCompCaso c WHERE c.nombreTablaValores =
//     * :nombreTablaValores</code>
//     */
//    public List<CampoCompCaso> getCampoCompCasoFindByNombreTablaValores(String nombreTablaValores) {
//        return getEntityManager().createNamedQuery("CampoCompCaso.findByNombreTablaValores").setParameter("nombreTablaValores", nombreTablaValores).getResultList();
//    }
//    /**
//     * <
//     * code>SELECT c FROM CampoCompCaso c WHERE c.nombreColValor =
//     * :nombreColValor</code>
//     */
//    public List<CampoCompCaso> getCampoCompCasoFindByNombreColValor(String nombreColValor) {
//        return getEntityManager().createNamedQuery("CampoCompCaso.findByNombreColValor").setParameter("nombreColValor", nombreColValor).getResultList();
//    }
//    public void persistUsuario(Usuario usuario) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getUsuarioJpaController().create(usuario);
//    }
//
//    public void mergeUsuarioFull(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
//        getUsuarioJpaController().edit(usuario);
//    }
    public void mergeUsuarioLight(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();

            usuario = em.merge(usuario);

            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getIdUsuario();

                if (em.find(Usuario.class, id) == null) {
                    throw new NonexistentEntityException(
                            "The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

//    public void removeUsuario(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
//        getUsuarioJpaController().destroy(usuario.getIdUsuario());
//    }
//
//    /**
//     * <
//     * code>SELECT u FROM Usuario u</code>
//     */
//    public List<Usuario> getUsuarioFindAll() {
//        return getUsuarioJpaController().findUsuarioEntities();
//
//    }
    /**
     * <
     * code>SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario</code>
     */
//    public Usuario getUsuarioFindByIdUsuario(String idUsuario) {
//        return getUsuarioJpaController().findUsuario(idUsuario);
//    }
    /**
     * <
     * code>SELECT u FROM Usuario u WHERE u.nombres = :nombres</code>
     */
    public List<Usuario> getUsuarioFindByNombres(String nombres) {
        return createEntityManager().createNamedQuery("Usuario.findByNombres").setParameter("nombres", nombres).getResultList();
    }

    /**
     * <
     * code>SELECT u FROM Usuario u WHERE u.apellidos = :apellidos</code>
     */
    public List<Usuario> getUsuarioFindByApellidos(String apellidos) {
        return createEntityManager().createNamedQuery("Usuario.findByApellidos").setParameter("apellidos", apellidos).getResultList();
    }

    /**
     * <
     * code>SELECT u FROM Usuario u WHERE u.email = :email</code>
     */
    public List<Usuario> getUsuarioFindByEmail(String email) {
        return createEntityManager().createNamedQuery("Usuario.findByEmail").setParameter("email", email).getResultList();
    }

    /**
     * <
     * code>SELECT u FROM Usuario u WHERE u.rut = :rut</code>
     */
    public List<Usuario> getUsuarioFindByRut(String rut) {
        return (List<Usuario>) createEntityManager().createNamedQuery("Usuario.findByRut").setParameter("rut", rut).getResultList();
    }

    public List<Usuario> findUsuariosEntitiesLike(String searchPart, boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
            Root<Usuario> from = criteriaQuery.from(Usuario.class);
            Expression<String> exp1 = from.get("email");
            Expression<String> exp2 = from.get("nombres");
            Expression<String> exp3 = from.get("apellidos");
            Expression<String> exp4 = from.get("idUsuario");

            Expression<String> literal = criteriaBuilder.upper(criteriaBuilder.literal("%" + (String) searchPart + "%"));
            Predicate predicate1 = criteriaBuilder.like(criteriaBuilder.upper(exp1), literal);
            Predicate predicate2 = criteriaBuilder.like(criteriaBuilder.upper(exp2), literal);
            Predicate predicate3 = criteriaBuilder.like(criteriaBuilder.upper(exp3), literal);
            Predicate predicate4 = criteriaBuilder.like(criteriaBuilder.upper(exp4), literal);

            criteriaQuery.where(criteriaBuilder.or(predicate1, predicate2, predicate3, predicate4));

            TypedQuery<Usuario> typedQuery = em.createQuery(criteriaQuery);
            if (!all) {
                typedQuery.setMaxResults(maxResults);
                typedQuery.setFirstResult(firstResult);
            }
            return typedQuery.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Resource> findResourcesEntitiesLike(String searchPart, boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Resource> criteriaQuery = criteriaBuilder.createQuery(Resource.class);
            Root<Resource> from = criteriaQuery.from(Resource.class);
            Expression<String> exp1 = from.get("nombre");

            Expression<String> literal = criteriaBuilder.upper(criteriaBuilder.literal("%" + (String) searchPart + "%"));
            Predicate predicate1 = criteriaBuilder.like(criteriaBuilder.upper(exp1), literal);
           

            criteriaQuery.where(criteriaBuilder.or(predicate1));

            TypedQuery<Resource> typedQuery = em.createQuery(criteriaQuery);
            if (!all) {
                typedQuery.setMaxResults(maxResults);
                typedQuery.setFirstResult(firstResult);
            }
            return typedQuery.getResultList();
        } finally {
            em.close();
        }
    }

//    public void persistGrupo(Grupo grupo) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getGrupoJpaController().create(grupo);
//    }
//
//    public void mergeGrupo(Grupo grupo) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getGrupoJpaController().edit(grupo);
//    }
//
//    public void removeGrupo(Grupo grupo) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getGrupoJpaController().destroy(grupo.getIdGrupo());
//    }
//
//    /**
//     * <
//     * code>SELECT g FROM Grupo g</code>
//     */
//    public List<Grupo> getGrupoFindAll() {
////        throw new UnsupportedOperationException("Not implemented... You shoud use a paginated or parcial query instead of quering all the tuples in a table.");
//        return getGrupoJpaController().findGrupoEntities();
//    }
    /**
     * <
     * code>SELECT g FROM Grupo g WHERE g.idGrupo = :idGrupo</code>
     */
//    public Grupo getGrupoFindByIdGrupo(String idGrupo) {
//        return getGrupoJpaController().findGrupo(idGrupo);
//    }
    /**
     * <
     * code>SELECT g FROM Grupo g WHERE g.nombre = :nombre</code>
     */
    public List<Grupo> getGrupoFindByNombre(String nombre) {
        return createEntityManager().createNamedQuery("Grupo.findByNombre").setParameter("nombre", nombre).getResultList();
    }

//    public void persistCanal(Canal canal) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getCanalJpaController().create(canal);
//    }
//
//    public void mergeCanal(Canal canal) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getCanalJpaController().edit(canal);
//    }
//
//    public void removeCanal(Canal canal) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getCanalJpaController().destroy(canal.getIdCanal());
//    }
//
//    /**
//     * <
//     * code>SELECT c FROM Canal c</code>
//     */
//    public List<Canal> getCanalFindAll() {
////        throw new UnsupportedOperationException("Not implemented... You shoud use a paginated or parcial query instead of quering all the tuples in a table.");
//        return getCanalJpaController().findCanalEntities();
//    }
    /**
     * <
     * code>SELECT c FROM Canal c WHERE c.idCanal = :idCanal</code>
     */
//    public Canal getCanalFindByIdCanal(String idCanal) {
//        return getCanalJpaController().findCanal(idCanal);
//    }
    /**
     * <
     * code>SELECT c FROM Canal c WHERE c.nombre = :nombre</code>
     */
    public List<Canal> getCanalFindByNombre(String nombre) {
        return createEntityManager().createNamedQuery("Canal.findByNombre").setParameter("nombre", nombre).getResultList();
    }

//    /**
//     * @deprecated @param nota
//     * @throws PreexistingEntityException
//     * @throws RollbackFailureException
//     * @throws Exception
//     */
//    public void persistNota(Nota nota) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getNotaJpaController().create(nota);
//    }
//
//    public void mergeNota(Nota nota) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getNotaJpaController().edit(nota);
//    }
//
////    public void removeNota(Nota nota) throws NonexistentEntityException, RollbackFailureException, Exception {
////        getNotaJpaController().destroy(nota.getIdNota());
////    }
//    /**
//     * <
//     * code>SELECT n FROM Nota n WHERE n.idNota = :idNota</code>
//     */
//    public Nota getNotaFindByIdNota(Integer idNota) {
//        return getNotaJpaController().findNota(idNota);
//    }
    public List<Nota> getNotaFindByIdCaso(Long idCaso) {
        return createEntityManager().createNamedQuery("Nota.findByIdCaso").setParameter("idCaso", idCaso).getResultList();
    }

    public List<Nota> getNotasPublicasFindByIdCaso(Long idCaso) {
        return createEntityManager().createNamedQuery("Nota.findByIdCasoPublic").setParameter("idCaso", idCaso)
                .setParameter("visible", Boolean.TRUE).getResultList();
    }

    /**
     * <
     * code>SELECT n FROM Nota n WHERE n.tipoNota = :tipoNota</code>
     */
    public List<Nota> getNotaFindByTipoNota(String tipoNota) {
        return createEntityManager().createNamedQuery("Nota.findByTipoNota").setParameter("tipoNota", tipoNota).getResultList();
    }

//    public void persistComponente(Componente componente) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getComponenteJpaController().create(componente);
//    }
//
//    public void mergeComponente(Componente componente) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getComponenteJpaController().edit(componente);
//    }
//
//    public void removeComponente(Componente componente) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getComponenteJpaController().destroy(componente.getIdComponente());
//    }
//
//    /**
//     * <
//     * code>SELECT c FROM Componente c</code>
//     */
//    public List<Componente> getComponenteFindAll() {
////        throw new UnsupportedOperationException("Not implemented... You shoud use a paginated or parcial query instead of quering all the tuples in a table.");
//        return getComponenteJpaController().findComponenteEntities();
//    }
//
//    /**
//     * <
//     * code>SELECT c FROM Componente c WHERE c.idComponente =
//     * :idComponente</code>
//     */
//    public Componente getComponenteFindByIdComponente(String idComponente) {
//        return getComponenteJpaController().findComponente(idComponente);
//    }
//
//    public void persistTipoAccion(TipoAccion tipoAccion) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getTipoAccionJpaController().create(tipoAccion);
//    }
//
//    public void mergeTipoAccion(TipoAccion tipoAccion) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getTipoAccionJpaController().edit(tipoAccion);
//    }
//
//    public void removeTipoAccion(TipoAccion tipoAccion) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getTipoAccionJpaController().destroy(tipoAccion.getIdTipoAccion());
//    }
//
//    /**
//     * <
//     * code>SELECT n FROM TipoAccion n</code>
//     */
//    public List<TipoAccion> getTipoAccionFindAll() {
////        throw new UnsupportedOperationException("Not implemented... You shoud use a paginated or parcial query instead of quering all the tuples in a table.");
//        return getTipoAccionJpaController().findTipoAccionEntities();
//    }
//
//    /**
//     * <
//     * code>SELECT n FROM TipoAccion n WHERE n.idTipoAccion =
//     * :idTipoAccion</code>
//     */
////    public TipoAccion getTipoAccionFindByIdTipoAccion(String idTipoAccion) {
////        return getTipoAccionJpaController().findTipoAccion(idTipoAccion);
////    }
//    public void persistAttachment(Attachment attachment) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getAttachmentJpaController().create(attachment);
//    }
//
//    public void mergeAttachment(Attachment attachment) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getAttachmentJpaController().edit(attachment);
//    }
//
//    public void removeAttachment(Attachment attachment) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getAttachmentJpaController().destroy(attachment.getIdAttachment());
//    }
//
//    /**
//     * <
//     * code>SELECT a FROM Attachment a</code>
//     */
//    public List<Attachment> getAttachmentFindAll() {
////        throw new UnsupportedOperationException("Not implemented... You shoud use a paginated or parcial query instead of quering all the tuples in a table.");
//        return getAttachmentJpaController().findAttachmentEntities();
//    }
//
//    /**
//     * <
//     * code>SELECT a FROM Attachment a WHERE a.idAttachment =
//     * :idAttachment</code>
//     */
//    public Attachment getAttachmentFindByIdAttachment(Long idAttachment) {
//        return getAttachmentJpaController().findAttachment(idAttachment);
//    }
//
//    public List<Attachment> getAttachmentWOContentId(Caso caso) {
//        return getAttachmentJpaController().findAttachmentsWOContentId(caso);
//    }
//
//    public Long countAttachmentWOContentId(Caso caso) {
//        return getAttachmentJpaController().countAttachmentsWOContentId(caso);
//    }
//
//    public Long countAttachmentWContentId(Caso caso) {
//        return getAttachmentJpaController().countAttachmentsWContentId(caso);
//    }
//
//    public Attachment getAttachmentFindByContentId(String contentId, Caso caso) {
//        return getAttachmentJpaController().findByContentId(contentId, caso);
//    }
//
//    public void persistFuncion(Funcion funcion) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getFuncionJpaController().create(funcion);
//    }
//
//    public void mergeFuncion(Funcion funcion) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getFuncionJpaController().edit(funcion);
//    }
//
//    public void removeFuncion(Funcion funcion) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getFuncionJpaController().destroy(funcion.getIdFuncion());
//    }
//
//    /**
//     * <
//     * code>SELECT f FROM Funcion f</code>
//     */
//    public List<Funcion> getFuncionFindAll() {
////        throw new UnsupportedOperationException("Not implemented... You shoud use a paginated or parcial query instead of quering all the tuples in a table.");
//        return getFuncionJpaController().findFuncionEntities();
//    }
//    /**
//     * <
//     * code>SELECT f FROM Funcion f WHERE f.idFuncion = :idFuncion</code>
//     */
////    public Funcion getFuncionFindByIdFuncion(Integer idFuncion) {
////        return getFuncionJpaController().findFuncion(idFuncion);
////    }
//    public void persistTipoAlerta(TipoAlerta tipoAlerta) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getTipoAlertaJpaController().create(tipoAlerta);
//    }
//
//    public void mergeTipoAlerta(TipoAlerta tipoAlerta) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getTipoAlertaJpaController().edit(tipoAlerta);
//    }
//
//    public void removeTipoAlerta(TipoAlerta tipoAlerta) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getTipoAlertaJpaController().destroy(tipoAlerta.getIdalerta());
//    }
//
//    /**
//     * <
//     * code>SELECT t FROM TipoAlerta t</code>
//     */
//    public List<TipoAlerta> getTipoAlertaFindAll() {
////        throw new UnsupportedOperationException("Not implemented... You shoud use a paginated or parcial query instead of quering all the tuples in a table.");
//        return getTipoAlertaJpaController().findTipoAlertaEntities();
//    }
//    /**
//     * <
//     * code>SELECT t FROM TipoAlerta t WHERE t.idalerta = :idalerta</code>
//     */
////    public TipoAlerta getTipoAlertaFindByIdTipoAlerta(Integer idalerta) {
////        return getTipoAlertaJpaController().findTipoAlerta(idalerta);
////    }
//    public void persistArchivo(Archivo archivo) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getArchivoJpaController().create(archivo);
//    }
//
//    public void mergeArchivo(Archivo archivo) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getArchivoJpaController().edit(archivo);
//    }
//
//    public void removeArchivo(Archivo archivo) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getArchivoJpaController().destroy(archivo.getIdAttachment());
//    }
//
//    /**
//     * <
//     * code>SELECT a FROM Archivo a WHERE a.idAttachment = :idAttachment</code>
//     */
//    public Archivo getArchivoFindByIdAttachment(Long idAttachment) {
//        return getArchivoJpaController().findArchivo(idAttachment);
//    }
//
//    public void persistEstadoCaso(EstadoCaso estadoCaso) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getEstadoCasoJpaController().create(estadoCaso);
//    }
//
//    public void mergeEstadoCaso(EstadoCaso estadoCaso) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getEstadoCasoJpaController().edit(estadoCaso);
//    }
//
//    public void removeEstadoCaso(EstadoCaso estadoCaso) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getEstadoCasoJpaController().destroy(estadoCaso.getIdEstado());
//    }
//
//    /**
//     * <
//     * code>SELECT e FROM EstadoCaso e</code>
//     */
//    public List<EstadoCaso> getEstadoCasoFindAll() {
//        return getEstadoCasoJpaController().findEstadoCasoEntities();
//    }
    /**
     * <
     * code>SELECT e FROM EstadoCaso e WHERE e.idEstado = :idEstado</code>
     */
//    public EstadoCaso getEstadoCasoFindByIdEstado(String idEstado) {
//        return getEstadoCasoJpaController().findEstadoCaso(idEstado);
//    }
    /**
     * <
     * code>SELECT e FROM EstadoCaso e WHERE e.nombre = :nombre</code>
     */
    public List<EstadoCaso> getEstadoCasoFindByNombre(String nombre) {
        return createEntityManager().createNamedQuery("EstadoCaso.findByNombre").setParameter("nombre", nombre).getResultList();
    }

//    public void persistSubEstadoCaso(SubEstadoCaso subEstadoCaso) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getSubEstadoCasoJpaController().create(subEstadoCaso);
//    }
//
//    public void mergeSubEstadoCaso(SubEstadoCaso subEstadoCaso) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getSubEstadoCasoJpaController().edit(subEstadoCaso);
//    }
//
//    public void removeSubEstadoCaso(SubEstadoCaso subEstadoCaso) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getSubEstadoCasoJpaController().destroy(subEstadoCaso.getIdSubEstado());
//    }
//    /**
//     * <
//     * code>SELECT s FROM SubEstadoCaso s</code>
//     */
//    public List<SubEstadoCaso> getSubEstadoCasoFindAll() {
//        return getEntityManager().createNamedQuery("SubEstadoCaso.findAll").getResultList();
//    }
    /**
     * <
     * code>SELECT s FROM SubEstadoCaso s WHERE s.idSubEstado =
     * :idSubEstado</code>
     */
//    public SubEstadoCaso getSubEstadoCasoFindByIdSubEstadoCaso(String idSubEstado) {
//        return (SubEstadoCaso) getEntityManager().createNamedQuery("SubEstadoCaso.findByIdSubEstado").setParameter("idSubEstado", idSubEstado).getSingleResult();
//    }
    /**
     * <
     * code>SELECT s FROM SubEstadoCaso s WHERE s.nombre = :nombre</code>
     */
    public List<SubEstadoCaso> getSubEstadoCasoFindByNombre(String nombre) {
        return createEntityManager().createNamedQuery("SubEstadoCaso.findByNombre").setParameter("nombre", nombre).getResultList();
    }

    /**
     * <
     * code>SELECT s FROM SubEstadoCaso s WHERE s.nombre = :nombre</code>
     */
    public List<SubEstadoCaso> getSubEstadoCasofindByIdEstado(String idEstado) {
        return createEntityManager().createNamedQuery("SubEstadoCaso.findByIdEstado").setParameter("idEstado", idEstado).getResultList();
    }

    public List<SubEstadoCaso> getSubEstadoCasofindByIdEstadoAndTipoCaso(EstadoCaso idEstado, TipoCaso tipo) {
        return createEntityManager().createNamedQuery("SubEstadoCaso.findByIdEstadoTipoCaso")
                .setParameter("idEstado", idEstado.getIdEstado()).setParameter("tipoCaso", tipo.getIdTipoCaso()).getResultList();
    }

//    public List<Prioridad> findPrioridadByTipoCaso(TipoCaso tipo) {
//        return getEntityManager().createNamedQuery("Prioridad.findByTipoCaso").setParameter("tipoCaso", tipo).getResultList();
//    }
//    public void persistProducto(Producto producto) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getProductoJpaController().create(producto);
//    }
//
//    public void mergeProducto(Producto producto) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getProductoJpaController().edit(producto);
//    }
//
//    public void removeProducto(Producto producto) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getProductoJpaController().destroy(producto.getIdProducto());
//    }
//    /**
//     * <
//     * code>SELECT p FROM Producto p</code>
//     */
//    public List<Producto> getProductoFindAll() {
//        return getEntityManager().createNamedQuery("Producto.findAll").getResultList();
//    }
//    /**
//     * <
//     * code>SELECT p FROM Producto p WHERE p.idProducto = :idProducto</code>
//     */
//    public Producto getProductoFindByIdProducto(String idProducto) {
//        return getProductoJpaController().findProducto(idProducto);
//    }
    /**
     * <
     * code>SELECT p FROM Producto p WHERE p.nombre = :nombre</code>
     */
    public Producto getProductoFindByNombre(String nombre) {
        return (Producto) createEntityManager().createNamedQuery("Producto.findByNombre").setParameter("nombre", nombre).getSingleResult();
    }

//    public void persistAuditLog(AuditLog auditLog) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getAuditLogJpaCustomController().create(auditLog);
//    }
//
//    public void mergeAuditLog(AuditLog auditLog) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getAuditLogJpaCustomController().edit(auditLog);
//    }
//
//    public void removeAuditLog(AuditLog auditLog) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getAuditLogJpaCustomController().destroy(auditLog.getIdLog());
//    }
//    /**
//     * <
//     * code>SELECT a FROM AuditLog a</code>
//     */
//    public List<AuditLog> getAuditLogFindAll() {
//        return getEntityManager().createNamedQuery("AuditLog.findAll").getResultList();
//    }
    /**
     * <
     * code>SELECT a FROM AuditLog a WHERE a.idLog = :idLog</code>
     */
    public List<AuditLog> getAuditLogFindByIdLog(Long idLog) {
        return createEntityManager().createNamedQuery("AuditLog.findByIdLog").setParameter("idLog", idLog).getResultList();
    }

    /**
     * <
     * code>SELECT a FROM AuditLog a WHERE a.tabla = :tabla</code>
     */
    public List<AuditLog> getAuditLogFindByTabla(String tabla) {
        return createEntityManager().createNamedQuery("AuditLog.findByTabla").setParameter("tabla", tabla).getResultList();
    }

    /**
     * <
     * code>SELECT a FROM AuditLog a WHERE a.campo = :campo</code>
     */
    public List<AuditLog> getAuditLogFindByCampo(String campo) {
        return createEntityManager().createNamedQuery("AuditLog.findByCampo").setParameter("campo", campo).getResultList();
    }

    /**
     * <
     * code>SELECT a FROM AuditLog a WHERE a.fecha = :fecha</code>
     */
    public List<AuditLog> getAuditLogFindByFecha(Date fecha) {
        return createEntityManager().createNamedQuery("AuditLog.findByFecha").setParameter("fecha", fecha).getResultList();
    }

//    public void persistPrioridad(Prioridad prioridad) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getPrioridadJpaController().create(prioridad);
////        return (Prioridad) throws PreexistingEntityException, RollbackFailureException, Exception(prioridad);
//    }
//
//    public void mergePrioridad(Prioridad prioridad) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getPrioridadJpaController().edit(prioridad);
//    }
//
//    public void removePrioridad(Prioridad prioridad) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getPrioridadJpaController().destroy(prioridad.getIdPrioridad());
//    }
//    /**
//     * <
//     * code>SELECT p FROM Prioridad p</code>
//     */
//    public List<Prioridad> getPrioridadFindAll() {
//        return getEntityManager().createNamedQuery("Prioridad.findAll").getResultList();
//    }
//    /**
//     * <
//     * code>SELECT p FROM Prioridad p WHERE p.idPrioridad = :idPrioridad</code>
//     */
//    public Prioridad getPrioridadFindByIdPrioridad(String idPrioridad) {
//        return getPrioridadJpaController().findPrioridad(idPrioridad);
//    }
    /**
     * <
     * code>SELECT p FROM Prioridad p WHERE p.nombre = :nombre</code>
     */
    public List<Prioridad> getPrioridadFindByNombre(String nombre) {
        return createEntityManager().createNamedQuery("Prioridad.findByNombre").setParameter("nombre", nombre).getResultList();
    }

//    public void persistCondicion(Condicion condicion) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getCondicionJpaController().create(condicion);
//    }
//
//    public void mergeCondicion(Condicion condicion) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getCondicionJpaController().edit(condicion);
//    }
//
//    public void removeCondicion(Condicion condicion) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getCondicionJpaController().destroy(condicion.getIdCondicion());
//    }
//    /**
//     * <
//     * code>SELECT c FROM Condicion c</code>
//     */
//    public List<Condicion> getCondicionFindAll() {
//        return getEntityManager().createNamedQuery("Condicion.findAll").getResultList();
//    }
    /**
     * <
     * code>SELECT c FROM Condicion c WHERE c.idCondicion = :idCondicion</code>
     */
    public List<Condicion> getCondicionFindByIdCondicion(Integer idCondicion) {
        return createEntityManager().createNamedQuery("Condicion.findByIdCondicion").setParameter("idCondicion", idCondicion).getResultList();
    }

//    public void persistReglaTrigger(ReglaTrigger reglaTrigger) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getReglaTriggerJpaController().create(reglaTrigger);
//    }
//
//    public void mergeReglaTrigger(ReglaTrigger reglaTrigger) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getReglaTriggerJpaController().edit(reglaTrigger);
//    }
//
//    public void removeReglaTrigger(ReglaTrigger reglaTrigger) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getReglaTriggerJpaController().destroy(reglaTrigger.getIdTrigger());
//    }
//    /**
//     * <
//     * code>SELECT r FROM ReglaTrigger r</code>
//     */
//    public List<ReglaTrigger> getReglaTriggerFindAll() {
//        return getEntityManager().createNamedQuery("ReglaTrigger.findAll").getResultList();
//    }
    /**
     * <
     * code>SELECT r FROM ReglaTrigger r WHERE r.idTrigger = :idTrigger</code>
     */
    public ReglaTrigger getReglaTriggerFindByIdTrigger(String idTrigger) {
        return (ReglaTrigger) createEntityManager().createNamedQuery("ReglaTrigger.findByIdTrigger").setParameter("idTrigger", idTrigger).getSingleResult();
    }

    public List<ReglaTrigger> getReglaTriggerFindByEvento(String event) {
        return createEntityManager().createNamedQuery("ReglaTrigger.findByEvento").setParameter("evento", event).getResultList();
    }

//    public List<ReglaTrigger> getReglaTriggerFindByEvento(String event, Area idArea) {
//        return getEntityManager().createNamedQuery("ReglaTrigger.findByEventoArea").setParameter("evento", event).setParameter("idArea", idArea).getResultList();
//    }
    public List<ReglaTrigger> findReglasToExecute(String idArea, String eventType) throws NotSupportedException, ClassNotFoundException {

        Vista vista = new Vista(ReglaTrigger.class);
        if (idArea != null) {
            FiltroVista filtro1 = new FiltroVista(0);
            filtro1.setIdCampo("areaList");
            filtro1.setIdComparador(EnumTipoComparacion.SC.getTipoComparacion());
            filtro1.setValor(idArea);
            vista.addFiltroVista(filtro1);
        }

        FiltroVista filtro2 = new FiltroVista(0);
        filtro2.setIdCampo("evento");
        filtro2.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
        filtro2.setValor(eventType);

        vista.addFiltroVista(filtro2);

        FiltroVista filtro3 = new FiltroVista(0);
        filtro3.setIdCampo("reglaActiva");
        filtro3.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
        filtro3.setValor(Boolean.TRUE.toString());

        vista.addFiltroVista(filtro3);

        return (List<ReglaTrigger>) findAllEntities(ReglaTrigger.class, vista, new OrderBy("orden", OrderBy.OrderType.ASC), null);

    }

//    public void persistTipoComparacion(TipoComparacion tipoComparacion) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getTipoComparacionJpaController().create(tipoComparacion);
//    }
//
//    public void mergeTipoComparacion(TipoComparacion tipoComparacion) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getTipoComparacionJpaController().edit(tipoComparacion);
//    }
//
//    public void removeTipoComparacion(TipoComparacion tipoComparacion) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getTipoComparacionJpaController().destroy(tipoComparacion.getIdComparador());
//    }
//
//    /**
//     * <
//     * code>SELECT t FROM TipoComparacion t</code>
//     */
//    public List<TipoComparacion> getTipoComparacionFindAll() {
//        return getTipoComparacionJpaController().findTipoComparacionEntities();
//    }
    /**
     * <
     * code>SELECT t FROM TipoComparacion t WHERE t.idComparador =
     * :idComparador</code>
     */
//    public TipoComparacion getTipoComparacionFindByIdComparador(String idComparador) {
//        return getTipoComparacionJpaController().findTipoComparacion(idComparador);
//    }
    /**
     * <
     * code>SELECT t FROM TipoComparacion t WHERE t.simbolo = :simbolo</code>
     */
    public List<TipoComparacion> getTipoComparacionFindBySimbolo(String simbolo) {
        return createEntityManager().createNamedQuery("TipoComparacion.findBySimbolo").setParameter("simbolo", simbolo).getResultList();
    }

//    public void persistSubComponente(SubComponente subComponente) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getSubComponenteJpaController().create(subComponente);
//    }
//
//    public void mergeSubComponente(SubComponente subComponente) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getSubComponenteJpaController().edit(subComponente);
//    }
//
//    public void removeSubComponente(SubComponente subComponente) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getSubComponenteJpaController().destroy(subComponente.getIdSubComponente());
//    }
//
//    /**
//     * <
//     * code>SELECT s FROM SubComponente s</code>
//     */
//    public List<SubComponente> getSubComponenteFindAll() {
//        return getEntityManager().createNamedQuery("SubComponente.findAll").getResultList();
//    }
//    /**
//     * <
//     * code>SELECT s FROM SubComponente s WHERE s.idSubComponente =
//     * :idSubComponente</code>
//     */
//    public SubComponente getSubComponenteFindByIdSubComponente(String idSubComponente) {
//        return getSubComponenteJpaController().findSubComponente(idSubComponente);
//    }
    /**
     * <
     * code>SELECT s FROM SubComponente s WHERE s.nombre = :nombre</code>
     */
    public List<SubComponente> getSubComponenteFindByNombre(String nombre) {
        return createEntityManager().createNamedQuery("SubComponente.findByNombre").setParameter("nombre", nombre).getResultList();
    }

//    public void persistAccion(Accion accion) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getAccionJpaController().create(accion);
//    }
//
//    public void mergeAccion(Accion accion) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getAccionJpaController().edit(accion);
//    }
//
//    public void removeAccion(Accion accion) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getAccionJpaController().destroy(accion.getIdAccion());
//    }
//
//    /**
//     * <
//     * code>SELECT a FROM Accion a</code>
//     */
//    public List<Accion> getAccionFindAll() {
//        return getAccionJpaController().findAccionEntities();
//    }
//
//    /**
//     * <
//     * code>SELECT a FROM Accion a WHERE a.idAccion = :idAccion</code>
//     */
//    public Accion getAccionFindById(Integer idAccion) {
//        return getAccionJpaController().findAccion(idAccion);
//    }
//
//    public void persistArea(Area area) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getAreaJpaController().create(area);
////        return (Area) throws PreexistingEntityException, RollbackFailureException, Exception(area);
//    }
//
//    public void persistSetting(AppSetting s) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getAppSettingJpaController().create(s);
////        return (Area) throws PreexistingEntityException, RollbackFailureException, Exception(area);
//    }
//
//    public void mergeArea(Area area) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
//        getAreaJpaController().edit(area);
////        return (Area) mergeEntity(area);
//    }
//
//    public void removeArea(Area area) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
//        getAreaJpaController().destroy(area.getIdArea());
////        area = getEntityManager().find(Area.class, area.getIdArea());
////        removeEntity(area);
//    }
//
//    /**
//     * <
//     * code>SELECT a FROM Area a</code>
//     */
//    public List<Area> getAreaFindAll() {
//        return getAreaJpaController().findAreaEntities();
//    }
    /**
     * <
     * code>SELECT a FROM Area a WHERE a.idArea = :idArea</code>
     */
//    public Area getAreaFindByIdArea(String idArea) {
//        return getAreaJpaController().findArea(idArea);
//    }
    /**
     * <
     * code>SELECT a FROM Area a WHERE a.nombre = :nombre</code>
     */
    public List<Area> getAreaFindByNombre(String nombre) {
        return createEntityManager().createNamedQuery("Area.findByNombre").setParameter("nombre", nombre).getResultList();
    }

//    public List<Caso> getCasoFindByEstadoAndAlerta(EstadoCaso estado, TipoAlerta tipoAlerta) {
//        return getCasoJpa().getCasoFindByEstadoAndAlerta(estado, tipoAlerta);
//    }
//
//    public void persistRol(Rol rol) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getRolJpaController().create(rol);
//    }
//
//    public void mergeRol(Rol rol) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getRolJpaController().edit(rol);
//    }
//
//    public void removeRol(Rol rol) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getRolJpaController().destroy(rol.getIdRol());
//    }
//
//    /**
//     * <
//     * code>SELECT r FROM Rol r</code>
//     */
//    public List<Rol> getRolFindAll() {
//        return getRolJpaController().findRolEntities();
//    }
    /**
     * <
     * code>SELECT r FROM Rol r WHERE r.idRol = :idRol</code>
     */
//    public Rol getRolFindByIdRol(String idRol) {
//        return getRolJpaController().findRol(idRol);
//    }
    /**
     * <
     * code>SELECT r FROM Rol r WHERE r.nombre = :nombre</code>
     */
    public List<Rol> getRolFindByNombre(String nombre) {
        return createEntityManager().createNamedQuery("Rol.findByNombre").setParameter("nombre", nombre).getResultList();
    }

//    public void persistCategoria(Categoria current) throws PreexistingEntityException, RollbackFailureException, Exception {
//        current.setIdCategoria(current.getOrden().intValue());
//        getCategoriaJpaController().create(current);
//    }
//
//    public void mergeCategoria(Categoria current) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getCategoriaJpaController().edit(current);
//    }
//
//    public void removeCategoria(Categoria current) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getCategoriaJpaController().destroy(current.getIdCategoria());
//    }
    public List<Categoria> getCategoriaFindByNombreLike(String nombre) {
        return createEntityManager().createNamedQuery("Categoria.findByNombreLike").setParameter("nombre", "%" + nombre + "%").getResultList();
    }

//    /**
//     * <
//     * code>SELECT c FROM Categoria c</code>
//     */
//    public List<Categoria> getCategoriaFindAll() {
//        return getCategoriaJpaController().findCategoriaEntities();
//    }
//
//    /**
//     * <
//     * code>SELECT c FROM Categoria c WHERE c.idCategoria = :idCategoria</code>
//     */
//    public Categoria getCategoriaFindByIdCategoria(Integer idCategoria) {
//        return getCategoriaJpaController().findCategoria(idCategoria);
////        return getEntityManager().createNamedQuery("Categoria.findByIdCategoria").setParameter("idCategoria", idCategoria).getResultList();
//    }
    public Categoria getCategoriaFindByName(String nombreCategoria) {
        return (Categoria) createEntityManager().createNamedQuery("Categoria.findByNombre").setParameter("nombre", nombreCategoria).getSingleResult();
//        return getEntityManager().createNamedQuery("Categoria.findByIdCategoria").setParameter("idCategoria", idCategoria).getResultList();
    }

//    public Object findById(Class aClass, Object key) {
//    }
//    public void persistCliente(Cliente current) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getClienteJpaController().create(current);
//    }
//
//    public Cliente findCliente(Integer idCliente) {
//        return getClienteJpaController().findCliente(idCliente);
//    }
//
//    public void mergeCliente(Cliente current) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getClienteJpaController().edit(current);
//    }
//
//    public void removeCliente(Cliente current) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getClienteJpaController().destroy(current.getIdCliente());
//    }
//
//    public List<Cliente> getClienteFindAll() {
//        return getClienteJpaController().findClienteEntities();
//    }
//
//    public void persistEmailCliente(EmailCliente current) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getEmailClienteJpaController().create(current);
//    }
//
//    public void mergeEmailCliente(EmailCliente current) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getEmailClienteJpaController().edit(current);
//    }
//
//    public void removeEmailCliente(EmailCliente current) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getEmailClienteJpaController().destroy(current.getEmailCliente());
//    }
//
//    public List<EmailCliente> getEmailClienteFindAll() {
//        return getEmailClienteJpaController().findEmailClienteEntities();
//    }
//
//    public EmailCliente getEmailClienteFindByEmail(String id) throws NoResultException {
//        return getEmailClienteJpaController().findEmailCliente(id);
//    }
//
//
//    public void persistTipoNota(TipoNota current) throws PreexistingEntityException, RollbackFailureException, Exception {
//        getTipoNotaJpaController().create(current);
//    }
//
//    public void mergeTipoNota(TipoNota current) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getTipoNotaJpaController().edit(current);
//    }
//
//    public void removeTipoNota(TipoNota current) throws NonexistentEntityException, RollbackFailureException, Exception {
//        getTipoNotaJpaController().destroy(current.getIdTipoNota());
//    }
//
//    public List<TipoNota> getTipoNotaFindAll() {
////        throw new UnsupportedOperationException("Not implemented... You shoud use a paginated or parcial query instead of quering all the tuples in a table.");
//        return getTipoNotaJpaController().findTipoNotaEntities();
//    }
//    public TipoNota getTipoNotaFindById(Integer idTipoNota) {
//        return getTipoNotaJpaController().findTipoNota(idTipoNota);
//    }
    public List<FieldType> getCustomFieldTypes() {
        return createEntityManager().createNamedQuery("FieldType.findByIsCustomField").setParameter("isCustomField", Boolean.TRUE).getResultList();
    }

    public List<CustomField> getCustomFieldsForCaso() {
        return createEntityManager().createNamedQuery("CustomField.findByEntity").setParameter("entity", CASE_CUSTOM_FIELD).getResultList();
    }

    public List<CustomField> getClientsCustomFieldsForCaso() {
        return createEntityManager().createNamedQuery("CustomField.findByEntityForCustomers").setParameter("entity", CASE_CUSTOM_FIELD).getResultList();
    }

    public List<CustomField> getAgentsCustomFieldsForClient() {
        return createEntityManager().createNamedQuery("CustomField.findByEntityForAgents").setParameter("entity", CLIENT_CUSTOM_FIELD).getResultList();
    }

    public List<CustomField> getClientsCustomFieldsForClient() {
        return createEntityManager().createNamedQuery("CustomField.findByEntityForCustomers").setParameter("entity", CLIENT_CUSTOM_FIELD).getResultList();
    }

//    /**
//     * @return the usuarioJpaController
//     */
//    public UsuarioJpaController getUsuarioJpaController() {
//        if (usuarioJpaController == null) {
//            usuarioJpaController = new UsuarioJpaController(utx, emf, getSchema());
//        }
//        return usuarioJpaController;
//    }
//
//    /**
//     * @return the rolJpaController
//     */
//    public RolJpaController getRolJpaController() {
//        if (rolJpaController == null) {
//            rolJpaController = new RolJpaController(utx, emf, getSchema());
//        }
//        return rolJpaController;
//    }
//
//    /**
//     * @return the notaJpaController
//     */
//    public NotaJpaController getNotaJpaController() {
//        if (notaJpaController == null) {
//            notaJpaController = new NotaJpaController(utx, emf, getSchema());
//        }
//        return notaJpaController;
//    }
//
//    /**
//     * @return the emailClienteJpaController
//     */
//    public EmailClienteJpaCustomController getEmailClienteJpaController() {
//        if (emailClienteJpaController == null) {
//            emailClienteJpaController = new EmailClienteJpaCustomController(utx, emf, getSchema());
//        }
//        return emailClienteJpaController;
//    }
//
//    /**
//     * @return the clienteJpaController
//     */
//    public ClienteJpaController getClienteJpaController() {
//        if (clienteJpaController == null) {
//            clienteJpaController = new ClienteJpaController(utx, emf, getSchema());
//        }
//        return clienteJpaController;
//    }
//
//    /**
//     * @return the prioridadJpaController
//     */
//    public PrioridadJpaController getPrioridadJpaController() {
//        if (prioridadJpaController == null) {
//            prioridadJpaController = new PrioridadJpaController(utx, emf, getSchema());
//        }
//        return prioridadJpaController;
//    }
//
//    /**
//     * @return the grupoJpaController
//     */
//    public GrupoJpaController getGrupoJpaController() {
//        if (grupoJpaController == null) {
//            grupoJpaController = new GrupoJpaController(utx, emf, getSchema());
//        }
//        return grupoJpaController;
//    }
//
//    /**
//     * @return the categoriaJpaController
//     */
//    public CategoriaJpaController getCategoriaJpaController() {
//        if (categoriaJpaController == null) {
//            categoriaJpaController = new CategoriaJpaController(utx, emf, getSchema());
//        }
//        return categoriaJpaController;
//    }
//
//    /**
//     * @return the areaJpaController
//     */
//    public AreaJpaController getAreaJpaController() {
//        if (areaJpaController == null) {
//            areaJpaController = new AreaJpaController(utx, emf, getSchema());
//        }
//        return areaJpaController;
//    }
//
//    /**
//     * @return the casoJpa
//     */
//    public CasoJPACustomController getCasoJpa() {
//        if (casoJpa == null) {
//            casoJpa = new CasoJPACustomController(utx, emf, getSchema());
//        }
//        return casoJpa;
//    }
//
//    /**
//     * @return the casoJpa
//     */
//    public AuditLogJpaCustomController getAuditLogJpaCustomController() {
//        if (auditLogJpa == null) {
//            auditLogJpa = new AuditLogJpaCustomController(utx, emf, getSchema());
//        }
//        return auditLogJpa;
//    }
//
//    public CanalJpaController getCanalJpaController() {
//        if (canalJpaController == null) {
//            canalJpaController = new CanalJpaController(utx, emf, getSchema());
//        }
//        return canalJpaController;
//    }
//
//    /**
//     * @return the tipoNotaJpaController
//     */
//    public TipoNotaJpaController getTipoNotaJpaController() {
//        if (tipoNotaJpaController == null) {
//            tipoNotaJpaController = new TipoNotaJpaController(utx, emf, getSchema());
//        }
//        return tipoNotaJpaController;
//    }
//
//    /**
//     * @return the accionJpaController
//     */
//    public AccionJpaController getAccionJpaController() {
//        if (accionJpaController == null) {
//            accionJpaController = new AccionJpaController(utx, emf, getSchema());
//        }
//        return accionJpaController;
//    }
//
//    /**
//     * @return the archivoJpaController
//     */
//    public ArchivoJpaController getArchivoJpaController() {
//        if (archivoJpaController == null) {
//            archivoJpaController = new ArchivoJpaController(utx, emf, getSchema());
//        }
//        return archivoJpaController;
//    }
//
//    /**
//     * @return the attachmentJpaController
//     */
//    public AttachmentJpaController getAttachmentJpaController() {
//        if (attachmentJpaController == null) {
//            attachmentJpaController = new AttachmentJpaController(utx, emf, getSchema());
//        }
//        return attachmentJpaController;
//    }
//
//    /**
//     * @return the componenteJpaController
//     */
//    public ComponenteJpaController getComponenteJpaController() {
//        if (componenteJpaController == null) {
//            componenteJpaController = new ComponenteJpaController(utx, emf, getSchema());
//        }
//        return componenteJpaController;
//    }
//
//    /**
//     * @return the condicionJpaController
//     */
//    public CondicionJpaController getCondicionJpaController() {
//        if (condicionJpaController == null) {
//            condicionJpaController = new CondicionJpaController(utx, emf, getSchema());
//        }
//        return condicionJpaController;
//    }
//
//    /**
//     * @return the estadoCasoJpaController
//     */
//    public EstadoCasoJpaController getEstadoCasoJpaController() {
//        if (estadoCasoJpaController == null) {
//            estadoCasoJpaController = new EstadoCasoJpaController(utx, emf, getSchema());
//        }
//        return estadoCasoJpaController;
//    }
//
//    /**
//     * @return the funcionJpaController
//     */
//    public FuncionJpaController getFuncionJpaController() {
//        if (funcionJpaController == null) {
//            funcionJpaController = new FuncionJpaController(utx, emf, getSchema());
//        }
//        return funcionJpaController;
//    }
//
//    /**
//     * @return the tipoAccionJpaController
//     */
//    public TipoAccionJpaController getTipoAccionJpaController() {
//        if (tipoAccionJpaController == null) {
//            tipoAccionJpaController = new TipoAccionJpaController(utx, emf, getSchema());
//        }
//        return tipoAccionJpaController;
//    }
//
//    /**
//     * @return the productoJpaController
//     */
//    public ProductoJpaController getProductoJpaController() {
//        if (productoJpaController == null) {
//            productoJpaController = new ProductoJpaController(utx, emf, getSchema());
//        }
//        return productoJpaController;
//    }
//
//    /**
//     * @return the reglaTriggerJpaController
//     */
//    public ReglaTriggerJpaController getReglaTriggerJpaController() {
//        if (reglaTriggerJpaController == null) {
//            reglaTriggerJpaController = new ReglaTriggerJpaController(utx, emf, getSchema());
//        }
//        return reglaTriggerJpaController;
//    }
//
//    /**
//     * @return the subComponenteJpaController
//     */
//    public SubComponenteJpaController getSubComponenteJpaController() {
//        if (subComponenteJpaController == null) {
//            subComponenteJpaController = new SubComponenteJpaController(utx, emf, getSchema());
//        }
//        return subComponenteJpaController;
//    }
//
//    /**
//     * @return the subEstadoCasoJpaController
//     */
//    public SubEstadoCasoJpaController getSubEstadoCasoJpaController() {
//        if (subEstadoCasoJpaController == null) {
//            subEstadoCasoJpaController = new SubEstadoCasoJpaController(utx, emf, getSchema());
//        }
//        return subEstadoCasoJpaController;
//    }
//
//    /**
//     * @return the tipoAlertaJpaController
//     */
//    public TipoAlertaJpaController getTipoAlertaJpaController() {
//        if (tipoAlertaJpaController == null) {
//            tipoAlertaJpaController = new TipoAlertaJpaController(utx, emf, getSchema());
//        }
//        return tipoAlertaJpaController;
//    }
//
//    /**
//     * @return the tipoComparacionJpaController
//     */
//    public TipoComparacionJpaController getTipoComparacionJpaController() {
//        if (tipoComparacionJpaController == null) {
//            tipoComparacionJpaController = new TipoComparacionJpaController(utx, emf, getSchema());
//        }
//        return tipoComparacionJpaController;
//    }
//
// 
//
//    /**
//     * @return the vistaJpaController
//     */
//    public VistaJpaController getVistaJpaController() {
//        if (vistaJpaController == null) {
//            vistaJpaController = new VistaJpaController(utx, emf, getSchema());
//        }
//        return vistaJpaController;
//    }
//
//    /**
//     * @return the filtroVistaJpaController
//     */
//    public FiltroVistaJpaController getFiltroVistaJpaController() {
//        if (filtroVistaJpaController == null) {
//            filtroVistaJpaController = new FiltroVistaJpaController(utx, emf, getSchema());
//        }
//        return filtroVistaJpaController;
//    }
//
//    /**
//     * @return the appSettingJpaController
//     */
//    public AppSettingJpaController getAppSettingJpaController() {
//        if (appSettingJpaController == null) {
//            appSettingJpaController = new AppSettingJpaController(utx, emf, getSchema());
//        }
//        return appSettingJpaController;
//    }
//
//    /**
//     * @return the clippingJpaController
//     */
//    public ClippingJpaController getClippingJpaController() {
//        if (clippingJpaController == null) {
//            clippingJpaController = new ClippingJpaController(utx, emf, getSchema());
//        }
//        return clippingJpaController;
//    }
//
//    public UsuarioJpaCustomController getUsuarioJpaCustomController() {
//        if (usuarioJpaCustomController == null) {
//            usuarioJpaCustomController = new UsuarioJpaCustomController(utx, emf, getSchema());
//        }
//        return usuarioJpaCustomController;
//    }
    public void persistArchivoAdjunto(byte[] bytearray, String contentId, Caso caso, String nombre, String mimeType, Long size, AuditLog log)
            throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();

            String fileName = nombre.trim().replace(" ", "_");
            Archivo archivo = new Archivo();
            archivo.setArchivo(bytearray);
            archivo.setContentType(mimeType);
            archivo.setFileName(fileName);
            try {
                archivo.setFormat(fileName.substring(fileName.lastIndexOf(".") + 1));
            } catch (Exception e) {
            }

            List col = caso.getAttachmentList();

            Attachment attach = new Attachment();
            attach.setIdCaso(caso);
            attach.setNombreArchivo(fileName);
            attach.setFileSize(size);
            attach.setMimeType(mimeType);
            attach.setContentId(contentId);

            persist(attach);
            col.add(attach);
            caso.setAttachmentList(col);
            archivo.setIdAttachment(attach.getIdAttachment());
            persist(archivo);
            persist(log);

            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                //just ignore if rollback fails
                // throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /*
     * @deprecated 
     * please improve this code. 
     */
    public List<Caso> getCasoFindByEstadoAndAlerta(EstadoCaso estado, TipoAlerta tipoAlerta) {
        EntityManager em = createEntityManager();
        try {
            return em.createNamedQuery("Caso.findByEstadoAndTipoAlerta")
                    .setParameter("idEstado", estado.getIdEstado())
                    .setParameter("estadoAlerta", tipoAlerta.getIdalerta())
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * @deprecated shit code is this!!!!!!
     * @param emailCliente
     * @param idProducto
     * @param idEstado
     * @param tipoCaso
     * @return
     */
    public Caso findCasoBy(String emailCliente, Producto idProducto, EstadoCaso idEstado, TipoCaso tipoCaso) {
        EntityManager em = createEntityManager();

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

    public List<Vista> findVistaEntitiesVisibleForAll() {
        EntityManager em = createEntityManager();

        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Vista> criteriaQuery = criteriaBuilder.createQuery(Vista.class);
            Root<Vista> root = criteriaQuery.from(Vista.class);
            //Add Criteria here
            Predicate predicate = criteriaBuilder.equal(root.get("visibleToAll"), Boolean.TRUE);
            criteriaQuery.where(predicate);
            //Get Results
            Query q = em.createQuery(criteriaQuery);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Vista> findVistasOwnedBy(Usuario user, boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();

        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Vista> criteriaQuery = criteriaBuilder.createQuery(Vista.class);
            Root<Vista> root = criteriaQuery.from(Vista.class);
            //Add Criteria here
            Predicate predicate = criteriaBuilder.equal(root.get("idUsuarioCreadaPor"), user);
            criteriaQuery.where(predicate);
            //Get Results

            Query q = em.createQuery(criteriaQuery);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Vista> findVistaEntitiesCreatedByUser(Usuario user, boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();

        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Vista> criteriaQuery = criteriaBuilder.createQuery(Vista.class);
            Root<Vista> root = criteriaQuery.from(Vista.class);
            //Add Criteria here
            Predicate predicate = criteriaBuilder.equal(root.get("idUsuarioCreadaPor"), user);
            Predicate predicate2 = criteriaBuilder.equal(root.get("visibleToAll"), Boolean.FALSE);
            predicate = CriteriaQueryHelper.addPredicate(predicate, predicate2, criteriaBuilder);
            criteriaQuery.where(predicate);
//            Expression<Date> expresion = root.get("visibleToAll");
            //Get Results

            Query q = em.createQuery(criteriaQuery);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Vista> findVistaEntitiesVisibleForGroupsOfUser(Usuario user) {
        EntityManager em = createEntityManager();

        try {
            if (user != null && user.getGrupoList() != null) {
                List<String> gruposUser = new ArrayList<String>();
                for (Grupo g : user.getGrupoList()) {
                    if (!gruposUser.contains(g.getIdGrupo())) {
                        gruposUser.add(g.getIdGrupo());
                    }
                }
                if (!gruposUser.isEmpty()) {
                    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
                    CriteriaQuery<Vista> criteriaQuery = criteriaBuilder.createQuery(Vista.class);
                    Root<Vista> root = criteriaQuery.from(Vista.class);
                    //Add Criteria here
                    Predicate predicate = criteriaBuilder.equal(root.get("idGrupo").get("idGrupo").in(gruposUser), Boolean.TRUE);
                    criteriaQuery.where(predicate);
//            Expression<Date> expresion = root.get("visibleToAll");
                    //Get Results
                    Query q = em.createQuery(criteriaQuery);
                    return q.getResultList();
                }
            }
        } finally {
            em.close();
        }
        return null;
    }

    public List<Vista> findVistaEntitiesVisibleForAreasOfUser(Usuario user) {
        EntityManager em = createEntityManager();

        try {
            List<String> areasUser = new ArrayList<String>();
            for (Grupo g : user.getGrupoList()) {
                if (!areasUser.contains(g.getIdArea().getIdArea())) {
                    areasUser.add(g.getIdArea().getIdArea());
                }
            }
            if (!areasUser.isEmpty()) {
                CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
                CriteriaQuery<Vista> criteriaQuery = criteriaBuilder.createQuery(Vista.class);
                Root<Vista> root = criteriaQuery.from(Vista.class);
                //Add Criteria here
                Predicate predicate = criteriaBuilder.equal(root.get("idArea").get("idArea").in(areasUser), Boolean.TRUE);
                criteriaQuery.where(predicate);
//            Expression<Date> expresion = root.get("visibleToAll");
                //Get Results
                Query q = em.createQuery(criteriaQuery);
                return q.getResultList();
            } else {
                return null;
            }

        } finally {
            em.close();
        }
    }

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

            if (find(Producto.class, producto.getIdProducto()) != null) {
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

    public int getCasoCountByEstado(Usuario usuario, EstadoCaso estadoCaso) {
        EntityManager em = createEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();

            Root<Caso> rt = cq.from(Caso.class);
//            ParameterExpression<TipoAlerta> pAlerta = cb.parameter(TipoAlerta.class);
            cq.select(cb.count(rt)).where(
                    cb.equal(rt.get("owner"), usuario),
                    cb.equal(rt.get("idEstado"), estadoCaso));
            Query q = em.createQuery(cq);
            int retorno = ((Long) q.getSingleResult()).intValue();
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            em.close();
        }
        return 0;
    }

    public List<EmailCliente> getEmailClienteFindByEmailLike(String id, int maxResults) {
        return findEmailClienteEntitiesLike(id, maxResults, 0);
    }

    public List<EmailCliente> findEmailClienteEntitiesLike(String searchPart) {
        return findEmailClienteEntitiesLike(searchPart, true, -1, -1);
    }

    public List<EmailCliente> findEmailClienteEntitiesLike(String searchPart, int maxResults, int firstResult) {
        return findEmailClienteEntitiesLike(searchPart, false, maxResults, firstResult);
    }

    private List<EmailCliente> findEmailClienteEntitiesLike(String searchPart, boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<EmailCliente> criteriaQuery = criteriaBuilder.createQuery(EmailCliente.class);
            Root<EmailCliente> from = criteriaQuery.from(EmailCliente.class);
            Expression<String> expemailCliente = from.get("emailCliente");
            CriteriaQuery<EmailCliente> select = criteriaQuery.select(from);

            Expression<String> literal = criteriaBuilder.upper(criteriaBuilder.literal((String) searchPart + "%"));
            Predicate predicate = criteriaBuilder.like(criteriaBuilder.upper(expemailCliente), literal);

            criteriaQuery.where(predicate);

            TypedQuery<EmailCliente> typedQuery = em.createQuery(select);
            if (!all) {
                typedQuery.setMaxResults(maxResults);
                typedQuery.setFirstResult(firstResult);
            }
            return typedQuery.getResultList();
        } finally {
            em.close();
        }
    }

    public Long countSearchClientes(String searchPattern) throws ClassNotFoundException {
        EntityManager em = createEntityManager();
        em.setProperty("javax.persistence.cache.storeMode", javax.persistence.CacheRetrieveMode.USE);

        try {
//            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//            CriteriaQuery<EmailCliente> criteriaQuery = criteriaBuilder.createQuery(EmailCliente.class);
//            Root<EmailCliente> root = criteriaQuery.from(EmailCliente.class);

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery();
//            Root root = criteriaBuilder.from(EmailCliente.class);
            Root<EmailCliente> root = criteriaQuery.from(EmailCliente.class);

            Join<EmailCliente, Cliente> cliente = root.join("cliente");

            Expression<String> expresionNombre = root.get("cliente").get("nombres");
            Expression<String> expresionApellido = root.get("cliente").get("apellidos");
            Expression<String> expresionRut = root.get("cliente").get("rut");
            Expression<String> expresionEmail = root.get("emailCliente");
            Expression<String> expresionDireccionM = cliente.joinList("productoContratadoList").get("subComponente").get("nombre");

            Predicate predicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.upper(expresionNombre), searchPattern.toUpperCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.upper(expresionApellido), searchPattern.toUpperCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.upper(expresionRut), searchPattern.toUpperCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.upper(expresionEmail), searchPattern.toUpperCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.upper(expresionDireccionM), "%" + searchPattern.toUpperCase() + "%"));

            if (predicate != null) {
                criteriaQuery.select(criteriaBuilder.count(root)).where(predicate).distinct(true);
            } else {
                criteriaQuery.select(criteriaBuilder.count(root));
            }
            Query q = em.createQuery(criteriaQuery);
            q.setHint("eclipselink.query-results-cache", true);
            return ((Long) q.getSingleResult());
        } catch (Exception e) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, "countSearchEntities " + searchPattern, e);
            return 0L;
        } finally {
            em.close();
        }
    }

    public List<EmailCliente> searchClientes(String searchPattern, boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<EmailCliente> criteriaQuery = criteriaBuilder.createQuery(EmailCliente.class);
            Root<EmailCliente> from = criteriaQuery.from(EmailCliente.class);
            Join<EmailCliente, Cliente> cliente = from.join("cliente");

            Expression<String> expresionNombre = from.get("cliente").get("nombres");
            Expression<String> expresionApellido = from.get("cliente").get("apellidos");
            Expression<String> expresionRut = from.get("cliente").get("rut");
            Expression<String> expresionEmail = from.get("emailCliente");
            Expression<String> expresionDireccionM = cliente.joinList("productoContratadoList").get("subComponente").get("nombre");

            Predicate predicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.upper(expresionNombre), searchPattern.toUpperCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.upper(expresionApellido), searchPattern.toUpperCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.upper(expresionRut), searchPattern.toUpperCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.upper(expresionEmail), searchPattern.toUpperCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.upper(expresionDireccionM), "%" + searchPattern.toUpperCase() + "%"));

            criteriaQuery.where(predicate);

            Query q = em.createQuery(criteriaQuery);

            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }

    }

    /**
     * I think one common way to do this is with transactions. If you begin a
     * new transaction and then persist a large number of objects, they won't
     * actually be inserted into the DB until you commit the transaction. This
     * can gain you some efficiencies if you have a large number of items to
     * commit.
     *
     */
    public int persistManyClients(List<Cliente> list) throws Exception {
        int persistedClients = 0;
        if (list != null && !list.isEmpty()) {

            EntityManager em = null;
            try {
                utx.begin();
                em = createEntityManager();

                int count = 0;
                for (Cliente cliente : list) {
                    try {
                        if (cliente != null) {
                            if (cliente.getIdCliente() != null) {
                                cliente = em.getReference(cliente.getClass(), cliente.getIdCliente());
                            } else {
                                List<Cliente> clientesRut = em.createNamedQuery("Cliente.findByRut").setParameter("rut", cliente.getRut()).getResultList();
                                if (clientesRut != null && !clientesRut.isEmpty()) {
                                    cliente = clientesRut.get(0);
                                } else {
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
                                    persistedClients++;
                                }
                            }
                        }
//                        em.flush();
                    } catch (Exception e) {
                        System.out.println("ERORR en " + cliente + " -- " + cliente.getEmailClienteList());
                        e.printStackTrace();
//                        break;
                    }
                    System.out.println(count++);
                }
                utx.commit();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (em != null) {
                    em.close();
                }
            }

        } else {
            throw new Exception("Nothing to persist dude.");
        }

        return persistedClients;
    }

//    public List<Attachment> findAttachmentsWOContentId(Caso caso) {
//        EasyCriteriaQuery<Attachment> easyCriteriaQuery = new EasyCriteriaQuery<Attachment>(emf, Attachment.class);
//        easyCriteriaQuery.addEqualPredicate(Attachment_.contentId.getName(), (Attachment) null);
//        easyCriteriaQuery.addEqualPredicate(Attachment_.idCaso.getName(), caso);
//        return easyCriteriaQuery.getAllResultList();
//    }
//    public Long countAttachmentsWOContentId(Long idCaso) {
////        EasyCriteriaQuery<Attachment> easyCriteriaQuery = new EasyCriteriaQuery<Attachment>(emf, Attachment.class);
////        easyCriteriaQuery.addEqualPredicate(Attachment_.contentId.getName(), (Attachment) null);
////        easyCriteriaQuery.addEqualPredicate(Attachment_.idCaso.getName(), caso);
////        return easyCriteriaQuery.count();s
//                
//        EntityManager em = getEntityManager();
//        
//        try {
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//
//            Root<Attachment> rt = cq.from(Attachment.class);
////            ParameterExpression<TipoAlerta> pAlerta = cb.parameter(TipoAlerta.class);
//            cq.select(cb.count(rt)).where(
//                    cb.equal(rt.get(Attachment_.contentId.getName()), usuario),
//                    cb.equal(rt.get(Attachment_.idCaso.getName()), estadoCaso));
//            Query q = em.createQuery(cq);
//            return ((Long) q.getSingleResult());
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//            em.close();
//        }
//        return 0L;
//    }
    public Long countAttachmentsWContentId(Caso caso) {
        Vista v = new Vista(Attachment.class);
        FiltroVista f1 = new FiltroVista(1);
        f1.setIdCampo("contentId");
        f1.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
        f1.setValor(PLACE_HOLDER_NULL);

        v.addFiltroVista(f1);

        FiltroVista f2 = new FiltroVista(1);
        f2.setIdCampo("idCaso");
        f2.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
        f2.setValor(caso.getIdCaso().toString());

        v.addFiltroVista(f2);

        try {
            return countEntities(v);

//        EasyCriteriaQuery<Attachment> easyCriteriaQuery = new EasyCriteriaQuery<Attachment>(emf, Attachment.class);
//        easyCriteriaQuery.addDistinctPredicate(Attachment_.contentId.getName(), (Attachment) null);
//        easyCriteriaQuery.addEqualPredicate(Attachment_.idCaso.getName(), caso);
//        return easyCriteriaQuery.count();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, null, ex);
            return 0L;
        }
    }

    public Attachment findByContentId(String contentId, Caso caso) {
//        EasyCriteriaQuery<Attachment> easyCriteriaQuery = new EasyCriteriaQuery<Attachment>(emf, Attachment.class);
//        easyCriteriaQuery.addEqualPredicate("contentId", contentId);
//        easyCriteriaQuery.addEqualPredicate("idCaso", caso);
//        System.out.println("buscando contentId: " + contentId);
//        List<Attachment> res = easyCriteriaQuery.getAllResultList();
//        if (res.size() > 0) {
//            return res.get(0);
//        }
        Vista v = new Vista(Attachment.class);
        FiltroVista f1 = new FiltroVista(1);
        f1.setIdCampo("contentId");
        f1.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
        f1.setValor(contentId);

        v.addFiltroVista(f1);

        FiltroVista f2 = new FiltroVista(1);
        f2.setIdCampo("idCaso");
        f2.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
        f2.setValor(caso.getIdCaso().toString());

        v.addFiltroVista(f2);

        try {
            List<Attachment> res = (List<Attachment>) findAllEntities(Attachment.class, v, null, null);
            if (res.size() > 0) {
                return res.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, "error at findByContentId", ex);
        }
        return null;
    }

    public void setCasoChangeListener(CasoChangeListener casoChangeListener) {
        this.casoChangeListener = casoChangeListener;
    }

    public CasoChangeListener getCasoChangeListener() {
        return casoChangeListener;
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
