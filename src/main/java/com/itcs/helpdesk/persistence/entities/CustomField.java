/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
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
@Table(name = "custom_field")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@NamedQueries({
    @NamedQuery(name = "CustomField.findAll", query = "SELECT c FROM CustomField c"),
    @NamedQuery(name = "CustomField.findByEntity", query = "SELECT c FROM CustomField c WHERE c.customFieldPK.entity = :entity"),
    @NamedQuery(name = "CustomField.findByEntityForCustomers", query = "SELECT c FROM CustomField c WHERE c.customFieldPK.entity = :entity and c.visibleToCustomers = TRUE")})
public class CustomField implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CustomFieldPK customFieldPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    private String label;
    @Basic(optional = false)
    @NotNull
    private boolean required;
    @Basic(optional = false)
    @NotNull
    @Column(name = "visible_to_customers")
    private boolean visibleToCustomers;
    //@BatchFetch(value = BatchFetchType.EXISTS)
    @JoinColumn(name = "field_type_id", referencedColumnName = "field_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private FieldType fieldTypeId;
    @Size(max = 2147483647)
    @Column(name = "list_options")
    private String listOptions;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customField")
    private List<CasoCustomField> casoCustomFieldList;
    @JoinTable(name = "custom_field_tipo_caso" , joinColumns = {
        @JoinColumn(name = "field_key", referencedColumnName = "field_key", table = "custom_field"),
        @JoinColumn(name = "entity", referencedColumnName = "entity", table = "custom_field")}, inverseJoinColumns = {
        @JoinColumn(name = "id_tipo_caso", referencedColumnName = "id_tipo_caso", table = "tipo_caso")})
    @ManyToMany(fetch = FetchType.EAGER)
    private List<TipoCaso> tipoCasoList;
    @JoinTable(name = "custom_field_tipo_accion", joinColumns = {
        @JoinColumn(name = "field_key", referencedColumnName = "field_key", table = "custom_field"),
        @JoinColumn(name = "entity", referencedColumnName = "entity", table = "custom_field")}, inverseJoinColumns = {
        @JoinColumn(name = "id_tipo_accion", referencedColumnName = "id_tipo_accion", table = "tipo_accion")})
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<TipoAccion> tipoAccionList;

    public CustomField() {
    }
    
    @Transient
    public boolean isCasoCustomField(){
        return this.customFieldPK.getEntity().equalsIgnoreCase("case");
    }

    public CustomField(CustomFieldPK customFieldPK) {
        this.customFieldPK = customFieldPK;
    }

    public CustomField(CustomFieldPK customFieldPK, String label, boolean required, boolean visibleToCustomers) {
        this.customFieldPK = customFieldPK;
        this.label = label;
        this.required = required;
        this.visibleToCustomers = visibleToCustomers;
    }

    public CustomField(String fieldKey, String entity) {
        this.customFieldPK = new CustomFieldPK(fieldKey, entity);
    }

    public CustomFieldPK getCustomFieldPK() {
        return customFieldPK;
    }

    public void setCustomFieldPK(CustomFieldPK customFieldPK) {
        this.customFieldPK = customFieldPK;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean getVisibleToCustomers() {
        return visibleToCustomers;
    }

    public void setVisibleToCustomers(boolean visibleToCustomers) {
        this.visibleToCustomers = visibleToCustomers;
    }

    public List<CasoCustomField> getCasoCustomFieldList() {
        return casoCustomFieldList;
    }

    public void setCasoCustomFieldList(List<CasoCustomField> casoCustomFieldList) {
        this.casoCustomFieldList = casoCustomFieldList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customFieldPK != null ? customFieldPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomField)) {
            return false;
        }
        CustomField other = (CustomField) object;
        if ((this.customFieldPK == null && other.customFieldPK != null) || (this.customFieldPK != null && !this.customFieldPK.equals(other.customFieldPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CustomField [");
        builder.append("customFieldPK=");
        builder.append(customFieldPK);

        builder.append(", fieldTypeId=");
        builder.append(fieldTypeId);
        builder.append(", label=");
        builder.append(label);
        builder.append(", required=");
        builder.append(required);
        builder.append(", tipoCasoList=");
        builder.append(tipoCasoList);
        builder.append(", visibleToCustomers=");
        builder.append(visibleToCustomers);
        builder.append("]");
        return builder.toString();
    }

    /**
     * @return the fieldTypeId
     */
    public FieldType getFieldTypeId() {
        return fieldTypeId;
    }

    /**
     * @param fieldTypeId the fieldTypeId to set
     */
    public void setFieldTypeId(FieldType fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }

    public List<TipoCaso> getTipoCasoList() {
        return tipoCasoList;
    }

    public void setTipoCasoList(List<TipoCaso> tipoCasoList) {
        this.tipoCasoList = tipoCasoList;
    }

    /**
     * @return the listOptions
     */
    public String getListOptions() {
        return listOptions;
    }

    /**
     * @param listOptions the listOptions to set
     */
    public void setListOptions(String listOptions) {
        this.listOptions = listOptions;
    }
    
      /**
     * @return the valoresList
     */
    public List<String> getFieldOptionsList() {
        final List<String> result = new ArrayList<String>();
        if (listOptions != null) {
            for (String value : listOptions.split(",", -1)) {
                final String trimmedValue = value.trim();
                result.add(trimmedValue);
            }
        }
        return result;
    }

    /**
     * @param valoresList the valoresList to set
     */
    public void setFieldOptionsList(List<String> valoresList) {
        listOptions = "";
        boolean first = true;
        for (String string : valoresList) {
            if (first) {
                first = false;
                listOptions += string;
            } else {
                listOptions += "," + string;
            }
        }
    }

    /**
     * @return the tipoAccionList
     */
    public List<TipoAccion> getTipoAccionList() {
        return tipoAccionList;
    }

    /**
     * @param tipoAccionList the tipoAccionList to set
     */
    public void setTipoAccionList(List<TipoAccion> tipoAccionList) {
        this.tipoAccionList = tipoAccionList;
    }
}
