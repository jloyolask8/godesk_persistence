/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantTableDiscriminator;
import org.eclipse.persistence.annotations.TenantTableDiscriminatorType;

/**
 *
 * @author jonathan
 */
@Entity
@Table(name = "field_type")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FieldType.findAll", query = "SELECT f FROM FieldType f"),
    @NamedQuery(name = "FieldType.findByFieldTypeId", query = "SELECT f FROM FieldType f WHERE f.fieldTypeId = :fieldTypeId"),
    @NamedQuery(name = "FieldType.findByIsCustomField", query = "SELECT f FROM FieldType f WHERE f.isCustomField = :isCustomField")
   })
public class FieldType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "field_type_id", nullable = false, length = 40)
    private String fieldTypeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "name", nullable = false, length = 40)
    private String name;
    @Size(max = 2147483647)
    @Column(name = "description", length = 2147483647)
    private String description;
    @Column(name = "is_custom_field")
    private Boolean isCustomField;
    @Size(max = 40)
    @Column(name = "java_type", length = 40)
    private String javaType;

    public FieldType() {
    }

    public FieldType(String fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }

    public FieldType(String fieldTypeId, String name, Boolean isCustomField) {
        this.fieldTypeId = fieldTypeId;
        this.name = name;
        this.isCustomField = isCustomField;
    }

    public String getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId(String fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fieldTypeId != null ? fieldTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FieldType)) {
            return false;
        }
        FieldType other = (FieldType) object;
        if ((this.fieldTypeId == null && other.fieldTypeId != null) || (this.fieldTypeId != null && !this.fieldTypeId.equals(other.fieldTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EnumFieldType." + fieldTypeId;
    }

    /**
     * @return the isCustomField
     */
    public Boolean getIsCustomField() {
        return isCustomField;
    }

    /**
     * @param isCustomField the isCustomField to set
     */
    public void setIsCustomField(Boolean isCustomField) {
        this.isCustomField = isCustomField;
    }
}
