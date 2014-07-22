/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jonathan
 */
@Embeddable
public class CustomFieldPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "field_key")
    private String fieldKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    private String entity;

    public CustomFieldPK() {
    }

    public CustomFieldPK(String fieldKey, String entity) {
        this.fieldKey = fieldKey;
        this.entity = entity;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fieldKey != null ? fieldKey.hashCode() : 0);
        hash += (entity != null ? entity.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomFieldPK)) {
            return false;
        }
        CustomFieldPK other = (CustomFieldPK) object;
        if ((this.fieldKey == null && other.fieldKey != null) || (this.fieldKey != null && !this.fieldKey.equals(other.fieldKey))) {
            return false;
        }
        if ((this.entity == null && other.entity != null) || (this.entity != null && !this.entity.equals(other.entity))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistence.CustomFieldPK[ fieldKey=" + fieldKey + ", entity=" + entity + " ]";
    }
    
}
