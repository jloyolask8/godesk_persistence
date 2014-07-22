/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jonathan
 */
//@Embeddable
public class CasoCustomFieldPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_caso")
    private Long idCaso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "field_key")
    private String fieldKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    private String entity;

    public CasoCustomFieldPK() {
    }

    public CasoCustomFieldPK(Long idCaso, String fieldKey, String entity) {
        this.idCaso = idCaso;
        this.fieldKey = fieldKey;
        this.entity = entity;
    }

    public Long getIdCaso() {
        return idCaso;
    }

    public void setIdCaso(Long idCaso) {
        this.idCaso = idCaso;
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
        int hash = 7;
        hash = 37 * hash + (this.idCaso != null ? this.idCaso.hashCode() : 0);
        hash = 37 * hash + (this.fieldKey != null ? this.fieldKey.hashCode() : 0);
        hash = 37 * hash + (this.entity != null ? this.entity.hashCode() : 0);
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
        final CasoCustomFieldPK other = (CasoCustomFieldPK) obj;
        if (this.idCaso != other.idCaso && (this.idCaso == null || !this.idCaso.equals(other.idCaso))) {
            return false;
        }
        if ((this.fieldKey == null) ? (other.fieldKey != null) : !this.fieldKey.equals(other.fieldKey)) {
            return false;
        }
        if ((this.entity == null) ? (other.entity != null) : !this.entity.equals(other.entity)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CasoCustomFieldPK [");
        builder.append("entity=");
        builder.append(entity);
        builder.append(", fieldKey=");
        builder.append(fieldKey);
        builder.append(", idCaso=");
        builder.append(idCaso);
        builder.append("]");
        return builder.toString();
    }

   
    
}
