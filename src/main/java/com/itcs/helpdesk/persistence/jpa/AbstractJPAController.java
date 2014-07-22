/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import com.itcs.helpdesk.persistence.entities.Categoria;
import com.itcs.helpdesk.persistence.entities.FieldType;
import com.itcs.helpdesk.persistence.entities.FiltroVista;
import com.itcs.helpdesk.persistence.entities.Grupo;
import com.itcs.helpdesk.persistence.entities.TipoComparacion;
import com.itcs.helpdesk.persistence.entities.Usuario;
import com.itcs.helpdesk.persistence.entities.Vista;
import com.itcs.helpdesk.persistence.entityenums.EnumFieldType;
import com.itcs.helpdesk.persistence.entityenums.EnumTipoComparacion;
import com.itcs.helpdesk.persistence.entityenums.EnumUsuariosBase;
import com.itcs.helpdesk.persistence.jpa.custom.CriteriaQueryHelper;
import com.itcs.helpdesk.persistence.utils.ComparableField;
import com.itcs.helpdesk.persistence.utils.FilterField;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.eclipse.persistence.config.EntityManagerProperties;

/**
 *
 * @author jonathan
 */
public class AbstractJPAController implements Serializable {

    protected UserTransaction utx = null;
    protected EntityManagerFactory emf = null;
//    protected EntityManagerFactory nonSharedEmf = null;
    private final String schema;//default is public in case not passed from the user session.

    public static final String PLACE_HOLDER_CURRENT_USER = "{CURRENT_USER}";
    public static final String PLACE_HOLDER_NULL = "{NULL}";
    public static final String PLACE_HOLDER_ANY = "{ANY_BUT_NULL}";

    public AbstractJPAController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        this.utx = utx;
        this.emf = emf;
        this.schema = schema;
    }

    protected EntityManager createEntityManager() {

//        if (nonSharedEmf == null) {
//            // Non shared EMF
//            Map pu = emf.getProperties();
//
//            HashMap properties = new HashMap();
//            for (Object key : pu.keySet()) {
//                System.out.println("key:" + key + ",value:" + pu.get(key));
//                properties.put(key, pu.get(key));
//            }
//            properties.put(PersistenceUnitProperties.MULTITENANT_SHARED_EMF, "false");
//            properties.put(PersistenceUnitProperties.SESSION_NAME, "non-shared-emf-for-" + schema);
//            properties.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, schema);
//            nonSharedEmf = Persistence.createEntityManagerFactory("helpdeskPU", properties);
//        }
//        EntityManager em = nonSharedEmf.createEntityManager();
        EntityManager em = emf.createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        return em;
    }

    public List<?> findAll(Class entityClass) {
        return findEntities(entityClass, true, 0, 0);
    }

    public List<?> findAll(Class entityClass, String... orderBy) {
        return findEntities(entityClass, true, 0, 0, orderBy);
    }

    protected List<?> findEntities(Class entityClass, boolean all, int maxResults, int firstResult) {
        return findEntities(entityClass, all, maxResults, firstResult, (String[]) null);
    }

    protected List<?> findEntities(Class entityClass, boolean all, int maxResults, int firstResult, String... orderBy) {
        EntityManager em = createEntityManager();

        try {
            final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery cq = criteriaBuilder.createQuery();
            javax.persistence.criteria.Root rt = cq.from(entityClass);
            cq.select(rt);

            if (orderBy != null && orderBy.length > 0) {
                for (String o : orderBy) {
                    cq.orderBy(criteriaBuilder.asc(rt.get(o)));
                }
            }

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

    protected Expression<?> createExpression(Root<?> root, String field) {
        Path<?> path = null;
        String[] fields = field.split("\\.");

        for (String string : fields) {
            if (null == path) {
                path = root.get(string);
            } else {
                path = path.get(string);
            }
        }

        return path;
    }

    /**
     * ALTER TABLE vista ADD COLUMN base_entity_type character varying(1000) NOT
     * NULL DEFAULT 'class';
     *
     * @param em
     * @param criteriaBuilder
     * @param root
     * @param vista
     * @param whoIsApplyingView
     * @return
     * @throws IllegalStateException
     * @throws ClassNotFoundException
     */
    protected Predicate createPredicate(EntityManager em, CriteriaBuilder criteriaBuilder, Root<?> root, Vista vista, Usuario whoIsApplyingView) throws IllegalStateException, ClassNotFoundException {
        Predicate predicate = null;
        if (vista == null) {
            throw new IllegalStateException("La vista no puede ser null!");
        }
        Class baseClass = Class.forName(vista.getBaseEntityType());
        Map<String, ComparableField> annotatedFields = getAnnotatedComparableFieldsMap(baseClass);

        for (FiltroVista filtro : vista.getFiltrosVistaList()) {

            final ComparableField comparableField = annotatedFields.get(filtro.getIdCampo());

            if (comparableField == null) {
                throw new IllegalStateException("Los valores de los filtros de la vista no cumplen con los requisitos minimos!. Debe especificar un campo comparable. atraves de annotaciones en el bean. " + vista.toString());
            }

            TipoComparacion operador = filtro.getIdComparador();

            String valorAttributo = filtro.getValor();

            if (operador == null || valorAttributo == null || filtro == null) {
                throw new IllegalStateException("Los valores de los filtros de la vista no cumplen con los requisitos minimos. " + vista.toString());
            }

            final FieldType fieldType = comparableField.getFieldTypeId();

            if (fieldType == null) {
                throw new IllegalStateException("Los valores de los filtros de la vista no cumplen con los requisitos minimos!. Debe especificar un tipo de campo, ejemplo TEXT. " + vista.toString());
            }

            Predicate localPredicate = null;

            if (fieldType.equals(EnumFieldType.TEXT.getFieldType()) || fieldType.equals(EnumFieldType.TEXTAREA.getFieldType())) {
                //El valor es de tipo String, usarlo tal como esta pero case insensitive

                Expression<String> expresion = root.get(filtro.getIdCampo());

                if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                    localPredicate = criteriaBuilder.equal(criteriaBuilder.lower(expresion), valorAttributo.trim().toLowerCase());
                } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                    localPredicate = criteriaBuilder.notEqual(criteriaBuilder.lower(expresion), valorAttributo.trim().toLowerCase());
                } else if (operador.equals(EnumTipoComparacion.CO.getTipoComparacion())) {
                    if (!valorAttributo.contains("*")) {
                        localPredicate = criteriaBuilder.like(criteriaBuilder.lower(expresion), "%" + valorAttributo.trim().toLowerCase() + "%");
                    } else {
                        localPredicate = criteriaBuilder.like(criteriaBuilder.lower(expresion), valorAttributo.trim().toLowerCase().replace("*", "%"));
                    }

                } else {
                    throw new IllegalStateException("Comparador " + operador.getIdComparador() + " is not supported!!");
                }
            } else if (fieldType.equals(EnumFieldType.NUMBER.getFieldType())) {

                if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                    localPredicate = criteriaBuilder.equal(root.get(filtro.getIdCampo()), valorAttributo.trim());
                } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                    localPredicate = criteriaBuilder.notEqual(root.get(filtro.getIdCampo()), valorAttributo.trim());
                } else if (operador.equals(EnumTipoComparacion.CO.getTipoComparacion())) {
                    Expression<String> expresion = root.get(filtro.getIdCampo());
                    if (!valorAttributo.contains("*")) {
                        localPredicate = criteriaBuilder.like(expresion, "%" + valorAttributo.trim().toLowerCase() + "%");
                    } else {
                        localPredicate = criteriaBuilder.like(expresion, valorAttributo.trim().toLowerCase().replace("*", "%"));
                    }

                } else {
                    throw new IllegalStateException("Comparador " + operador.getIdComparador() + " is not supported!!");
                }

            } else if (fieldType.equals(EnumFieldType.CALENDAR.getFieldType())) {
                //El valor es de tipo Fecha, usar el String parseado a una fecha
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    Date fecha1 = sdf.parse(valorAttributo);
                    if (fecha1 != null) {
                        Expression<Date> expresion = root.get(filtro.getIdCampo());
                        if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.equal(expresion, fecha1);
                        } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.notEqual(expresion, fecha1);
                        } else if (operador.equals(EnumTipoComparacion.LE.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.lessThanOrEqualTo(expresion, fecha1);
                        } else if (operador.equals(EnumTipoComparacion.GE.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.greaterThanOrEqualTo(expresion, fecha1);
                        } else if (operador.equals(EnumTipoComparacion.LT.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.lessThan(expresion, fecha1);
                        } else if (operador.equals(EnumTipoComparacion.GT.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.greaterThan(expresion, fecha1);
                        } else if (operador.equals(EnumTipoComparacion.BW.getTipoComparacion())) {
                            Date fecha2 = sdf.parse(filtro.getValor2());
                            localPredicate = criteriaBuilder.between(expresion, fecha1, fecha2);
                        } else {
                            throw new IllegalStateException("Comparador " + operador.getIdComparador() + " is not supported!!");
                        }
                    } else {
                        Logger.getLogger(AbstractJPAController.class.getName()).log(Level.SEVERE, "filtro descartado [CALENDAR] la fecha {0} es null.", valorAttributo);
                        continue;
                    }
                } catch (ParseException ex) {
                    //ignore and do not add this filter to the query
                    Logger.getLogger(AbstractJPAController.class.getName()).log(Level.SEVERE, "filtro descartado [CALENDAR] No se puede parsear la fecha {0}", valorAttributo);
                    continue;
                }

            } else if (fieldType.equals(EnumFieldType.SELECTONE_ENTITY.getFieldType())) {
                //El valor es el id de un entity, que tipo de Entity?= comparableField.tipo
                //One or more values??

                if (operador.equals(EnumTipoComparacion.SC.getTipoComparacion())) {

                    //One or more values, as list select many.
                    final List<String> valores = filtro.getValoresList();

                    if (comparableField.getTipoCampoFullPath().equals(List.class)) {

                        localPredicate = root.joinList(filtro.getIdCampo()).get(comparableField.getListParameterFieldId()).in(valores);

                    } else {

                        boolean addCurrentUserFilter = false;
                        boolean addPlaceHolderNullFilter = false;

                        if (valores.contains(PLACE_HOLDER_CURRENT_USER)) {
//                            valores.remove(PLACE_HOLDER_CURRENT_USER);
                            addCurrentUserFilter = true;
                        }

                        if (valores.contains(PLACE_HOLDER_NULL)) {
//                            valores.remove(PLACE_HOLDER_NULL);
                            addPlaceHolderNullFilter = true;
                        }

                        if (valores.contains(PLACE_HOLDER_ANY)) {
//                            valores.remove(PLACE_HOLDER_ANY);
                            //nothig, just dont send this string to the "in" criteria Predicate.
                        }

                        Expression expresion2 = createExpression(root, comparableField.getIdCampoFullPath()).as(comparableField.getTipoCampoFullPath());
                        localPredicate = criteriaBuilder.equal(expresion2.in(valores), Boolean.TRUE);

                        //handle PLACEHOLDERS! in valores
                        System.out.println("//remove this, PLACEHOLDERS! valores:" + valores);
                        if (addCurrentUserFilter) {

                            if (comparableField.getTipo().equals(Usuario.class) && (whoIsApplyingView != null)) {
                                Expression expresion = root.get(filtro.getIdCampo()).as(comparableField.getTipo());
                                localPredicate = CriteriaQueryHelper.addOrPredicate(localPredicate, criteriaBuilder.equal(expresion, em.find(comparableField.getTipo(), whoIsApplyingView.getIdUsuario())), criteriaBuilder);
                            }
                        }

                        if (addPlaceHolderNullFilter) {
                            Expression expresion = root.get(filtro.getIdCampo()).as(comparableField.getTipo());
                            localPredicate = CriteriaQueryHelper.addOrPredicate(localPredicate, criteriaBuilder.isNull(expresion), criteriaBuilder);
                        }
                    }

                } else {

                    Expression expresion = root.get(filtro.getIdCampo()).as(comparableField.getTipo());

                    //some entities may have special place holder that others entities do not have, so here we should call the special method
                    if (isThereSpecialFiltering(filtro)) {
                        localPredicate = createSpecialPredicate(filtro);
                    } else {
                        //Owner has a special place holder that others entities do not have
                        if (comparableField.getTipo().equals(Usuario.class)
                                //                        if (filtro.getIdCampo().equals(EnumCampoCompCaso.OWNER.getCampoCompCaso().getIdCampo())
                                && PLACE_HOLDER_CURRENT_USER.equalsIgnoreCase(valorAttributo)) {
                            if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                                localPredicate = criteriaBuilder.equal(expresion, em.find(comparableField.getTipo(), whoIsApplyingView.getIdUsuario()));
                            } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                                localPredicate = criteriaBuilder.notEqual(expresion, em.find(comparableField.getTipo(), whoIsApplyingView.getIdUsuario()));
                            } else {
                                throw new IllegalStateException("Tipo comparacion " + operador.getIdComparador() + " is not supported here!!");
                            }

                        } else if (PLACE_HOLDER_ANY.equalsIgnoreCase(valorAttributo)) {
                            if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                                localPredicate = criteriaBuilder.isNotNull(expresion);
                            } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                                localPredicate = criteriaBuilder.isNull(expresion);
                            } else {
                                throw new IllegalStateException("Tipo comparacion " + operador.getIdComparador() + " is not supported here!!");
                            }
                        } else if (PLACE_HOLDER_NULL.equalsIgnoreCase(valorAttributo)) {
                            if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                                localPredicate = criteriaBuilder.isNull(expresion);
                            } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                                localPredicate = criteriaBuilder.isNotNull(expresion);
                            } else {
                                throw new IllegalStateException("Tipo comparacion " + operador.getIdComparador() + " is not supported here!!");
                            }
                        } else {
                            if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {

                                try {
                                    Constructor actionConstructor = Class.forName(comparableField.getTipoCampoFullPath().getName()).getConstructor(String.class);
                                    localPredicate = criteriaBuilder.equal(expresion, em.find(comparableField.getTipo(), actionConstructor.newInstance(valorAttributo)));
//                                } catch (java.lang.IllegalArgumentException e) {
                                } catch (Exception e) {
                                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "abstractJPAController error", e);
//                                    localPredicate = criteriaBuilder.equal(expresion, em.find(comparableField.getTipo(), Integer.valueOf(valorAttributo)));
                                }
                            } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                                try {
                                    Constructor actionConstructor = Class.forName(comparableField.getTipoCampoFullPath().getName()).getConstructor(String.class);
                                    localPredicate = criteriaBuilder.notEqual(expresion, em.find(comparableField.getTipo(), actionConstructor.newInstance(valorAttributo)));
//                                } catch (java.lang.IllegalArgumentException e) {
                                } catch (Exception e) {
                                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "abstractJPAController error", e);
//                                    localPredicate = criteriaBuilder.notEqual(expresion, em.find(comparableField.getTipo(), Integer.valueOf(valorAttributo)));
                                }

                            } else {
                                throw new IllegalStateException("Tipo comparacion " + operador.getIdComparador() + " is not supported here!!");
                            }
                        }
                    }
                }

            } else if (fieldType.equals(EnumFieldType.SELECTONE_PLACE_HOLDER.getFieldType())) {

                Expression expresion = root.get(filtro.getIdCampo()).as(comparableField.getTipo());

                //some entities may have special place holder that others entities do not have, so here we should call the special method
                if (isThereSpecialFiltering(filtro)) {
                    localPredicate = createSpecialPredicate(filtro);
                } else {
                    if (PLACE_HOLDER_ANY.equalsIgnoreCase(valorAttributo)) {
                        if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.isNotNull(expresion);
                        } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.isNull(expresion);
                        } else {
                            throw new IllegalStateException("Tipo comparacion " + operador.getIdComparador() + " is not supported here!!");
                        }
                    } else if (PLACE_HOLDER_NULL.equalsIgnoreCase(valorAttributo)) {
                        if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.isNull(expresion);
                        } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.isNotNull(expresion);
                        } else {
                            throw new IllegalStateException("Tipo comparacion " + operador.getIdComparador() + " is not supported here!!");
                        }
                    } else {
                        throw new IllegalStateException("valor Attributo " + valorAttributo + " is not supported here!!");
                    }
                }
            } else if (fieldType.equals(EnumFieldType.SELECT_MANY_ENTITIES.getFieldType())) {

                //One or more values, as list select many.
                final List<String> valores = filtro.getValoresList();

                if (operador.equals(EnumTipoComparacion.IM.getTipoComparacion())) {
                    if (comparableField.getTipoCampoFullPath().equals(List.class)) {

                        localPredicate = root.joinList(filtro.getIdCampo()).get(comparableField.getListParameterFieldId()).in(valores);

                    } else {
                        throw new IllegalStateException("Field type " + comparableField.getTipoCampoFullPath() + " is not supported in " + EnumFieldType.SELECT_MANY_ENTITIES + ". Expected List.class");
                    }
                } else {
                    throw new IllegalStateException("Comparador " + operador.getIdComparador() + " is not supported here!!" + EnumFieldType.SELECT_MANY_ENTITIES);
                }

            } else if (fieldType.equals(EnumFieldType.CHECKBOX.getFieldType())) {
                //Boolean comparation
                //El valor es de tipo boolean, usar el String parseado a un boolean
                Expression<Boolean> expresion = root.get(filtro.getIdCampo());
                boolean boolValue = Boolean.valueOf(valorAttributo);

                if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                    localPredicate = criteriaBuilder.equal(expresion, boolValue);
                } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                    localPredicate = criteriaBuilder.notEqual(expresion, boolValue);
                } else {
                    throw new IllegalStateException("Comparador " + operador.getIdComparador() + " is not supported!!");
                }

            } else if (fieldType.equals(EnumFieldType.COMMA_SEPARATED_VALUELIST.getFieldType())) {
                if (operador.equals(EnumTipoComparacion.IM.getTipoComparacion())) {
//                    Expression<List> expresion = root.get(filtro.getIdCampo());
                    final List<String> valoresList = filtro.getValoresList();
//                    ListJoin<Caso, Etiqueta> tags = root.joinList(filtro.getIdCampo());
//                    localPredicate = root.joinList(filtro.getIdCampo()).get("tagId").in(valoresList);
                    localPredicate = root.joinList(filtro.getIdCampo()).get("tagId").in(valoresList);

//                      Expression expresion2 = createExpression(root, comparableField.getIdCampoFullPath()).as(comparableField.getTipoCampoFullPath());
//                    localPredicate = criteriaBuilder.isTrue(expresion2.in(valoresList));
                } else {
                    throw new IllegalStateException("Comparador " + operador.getIdComparador() + " is not supported!!");
                }

            } else {
                throw new IllegalStateException("fieldType " + fieldType.getFieldTypeId() + " is not supported yet!!");
            }

            predicate = CriteriaQueryHelper.addPredicate(predicate, localPredicate, criteriaBuilder);
        }

        return predicate;
    }

    public Map<String, ComparableField> getAnnotatedComparableFieldsMap(Class<?> baseClazz) {
        Map<String, ComparableField> fields = new HashMap<String, ComparableField>();
        for (ComparableField comparableField : getAnnotatedComparableFieldsByClass(baseClazz)) {
            fields.put(comparableField.getIdCampo(), comparableField);
        }
        return fields;
    }

    public Map<String, ComparableField> getAnnotatedComparableFieldsMap(List<ComparableField> comparableFields) {
        Map<String, ComparableField> fields = new HashMap<String, ComparableField>();
        for (ComparableField comparableField : comparableFields) {
            fields.put(comparableField.getIdCampo(), comparableField);
        }
        return fields;
    }

    public List<ComparableField> getAnnotatedComparableFieldsByClass(Class<?> baseClazz) {
        List<ComparableField> filters = new ArrayList<ComparableField>();
        Field[] fields = baseClazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(FilterField.class)) {

                FilterField filterField = field.getAnnotation(FilterField.class);
                ComparableField comparableField = new ComparableField();
                comparableField.setBaseClass(baseClazz);
                comparableField.setIdCampo(field.getName());
                comparableField.setLabel(filterField.label());
                comparableField.setTipo(field.getType());
                comparableField.setFieldTypeId(filterField.fieldTypeId().getFieldType());
                comparableField.setIdCampoFullPath(filterField.fieldIdFull());
                comparableField.setTipoCampoFullPath(filterField.fieldTypeFull());

                if (field.getType().equals(List.class)) {

//                        Field listGenericField = baseClazz.getDeclaredField(filterField.fieldIdFull());
                    ParameterizedType listGenericType = (ParameterizedType) field.getGenericType();
                    Class<?> listGenericClass = (Class<?>) listGenericType.getActualTypeArguments()[0];
                    comparableField.setListGenericType(listGenericClass);
                    comparableField.setListParameterFieldId(filterField.listGenericTypeFieldId());

                }

                filters.add(comparableField);
            }
        }
        return filters;
    }

    public List<?> findPosibleDBOptionsFor(ComparableField comparableField, Usuario userWhoIsAsking) {
        FieldType fieldType = comparableField.getFieldTypeId();

        if (fieldType == null) {
            return null;
        }

        List<?> entities;

        //need to filter what categories you can see
        if (comparableField.getTipo().equals(Categoria.class)) {
            List<Categoria> lista = new LinkedList<Categoria>();
            for (Grupo grupo : userWhoIsAsking.getGrupoList()) {
                for (Categoria cat : grupo.getCategoriaList()) {
                    if (!lista.contains(cat)) {
                        lista.add(cat);
                    }
                }
            }
            Collections.sort(lista, new Comparator<Categoria>() {
                @Override
                public int compare(Categoria o1, Categoria o2) {
                    return o1.getOrden() - o2.getOrden();
                }
            });
            entities = lista;
        } else if (comparableField.getTipo().equals(List.class)) {
            entities = findAll(comparableField.getListGenericType());
            if (comparableField.getListGenericType().equals(Usuario.class)) {
                entities.remove(EnumUsuariosBase.SISTEMA.getUsuario());
            }
        } else {
            //Que tipo de Entity? -> comparableField.tipo

            entities = findAll(comparableField.getTipo());

            //Usuario entity has a special place holder that others entities do not have
            if (comparableField.getTipo().equals(Usuario.class)) {
                entities.remove(EnumUsuariosBase.SISTEMA.getUsuario());
            }
        }
        return entities;

    }

    protected /**
             * abstract*
             */
            Predicate createSpecialPredicate(FiltroVista filtro) {
        return null;
    }

    protected /**
             * abstract*
             */
            boolean isThereSpecialFiltering(FiltroVista filtro) {
        return false;
    }

    /**
     * @return the schema
     */
    public String getSchema() {
        return schema;
    }
}
