/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantTableDiscriminator;
import org.eclipse.persistence.annotations.TenantTableDiscriminatorType;

/**
 *
 * @author jonathan
 */
@Entity
@Table(name = "caso_custom_field")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@IdClass(CasoCustomFieldPK.class)
@NamedQueries({
    @NamedQuery(name = "CasoCustomField.findAll", query = "SELECT c FROM CasoCustomField c")})
public class CasoCustomField implements Serializable {

    private static final long serialVersionUID = 1L;
//    @EmbeddedId
//    protected CasoCustomFieldPK casoCustomFieldPK;
    @Id
    @Size(min = 1, max = 40)
    @Column(name = "field_key")
    private String fieldKey;
    @Id
    @Size(min = 1, max = 40)
    private String entity;
    @Id
    @JoinColumn(name = "id_caso", referencedColumnName = "id_caso")
    @ManyToOne(optional = false)
    private Caso idCaso;
    @Size(max = 2147483647)
    private String valor;
    @Size(max = 2147483647)
    private String valor2;
    @JoinColumns({
        @JoinColumn(name = "field_key", referencedColumnName = "field_key", insertable = false, updatable = false),
        @JoinColumn(name = "entity", referencedColumnName = "entity", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private CustomField customField;

    public CasoCustomField() {
    }

    public CasoCustomField(String fieldKey, String entity, Caso caso) {
        this.fieldKey = fieldKey;
        this.entity = entity;
        this.idCaso = caso;
        this.valor = "";
    }
    
    

//    public CasoCustomField(CasoCustomFieldPK casoCustomFieldPK) {
//        this.casoCustomFieldPK = casoCustomFieldPK;
//    }

//    public CasoCustomField(Long idCaso, String fieldKey, String entity) {
//        this.casoCustomFieldPK = new CasoCustomFieldPK(idCaso, fieldKey, entity);
//    }
//
//    public CasoCustomField(Caso idCaso, CustomFieldPK customFieldPK) {
//        this.casoCustomFieldPK = new CasoCustomFieldPK(idCaso.getIdCaso(), customFieldPK.getFieldKey(), customFieldPK.getEntity());
//        this.caso = idCaso;
//    }

//    public CasoCustomFieldPK getCasoCustomFieldPK() {
//        return casoCustomFieldPK;
//    }
//
//    public void setCasoCustomFieldPK(CasoCustomFieldPK casoCustomFieldPK) {
//        this.casoCustomFieldPK = casoCustomFieldPK;
//    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValor2() {
        return valor2;
    }

    public void setValor2(String valor2) {
        this.valor2 = valor2;
    }

    public CustomField getCustomField() {
        return customField;
    }

    public void setCustomField(CustomField customField) {
        this.customField = customField;
    }

    public Caso getIdCaso() {
        return idCaso;
    }

    public void setIdCaso(Caso caso) {
        this.idCaso = caso;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.fieldKey != null ? this.fieldKey.hashCode() : 0);
        hash = 29 * hash + (this.entity != null ? this.entity.hashCode() : 0);
        hash = 29 * hash + (this.idCaso != null ? this.idCaso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CasoCustomField other = (CasoCustomField) obj;
        if ((this.fieldKey == null) ? (other.fieldKey != null) : !this.fieldKey.equals(other.fieldKey)) {
            return false;
        }
        if ((this.entity == null) ? (other.entity != null) : !this.entity.equals(other.entity)) {
            return false;
        }
        if (this.idCaso != other.idCaso && (this.idCaso == null || !this.idCaso.equals(other.idCaso))) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CasoCustomField [");
        builder.append("caso=");
        builder.append(idCaso);
        builder.append(", entity=");
        builder.append(entity);
        builder.append(", fieldKey=");
        builder.append(fieldKey);
        builder.append("]");
        return builder.toString();
    }



    /**
     * @return the fieldKey
     */
    public String getFieldKey() {
        return fieldKey;
    }

    /**
     * @param fieldKey the fieldKey to set
     */
    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    /**
     * @return the entity
     */
    public String getEntity() {
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }
}
