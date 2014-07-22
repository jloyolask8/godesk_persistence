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
@Table(name = "app_setting")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppSetting.findAll", query = "SELECT a FROM AppSetting a"),
    @NamedQuery(name = "AppSetting.findBySettingKey", query = "SELECT a FROM AppSetting a WHERE a.settingKey = :settingKey"),
    @NamedQuery(name = "AppSetting.findByGrupo", query = "SELECT a FROM AppSetting a WHERE a.grupo = :grupo")})
public class AppSetting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "setting_key", nullable = false, length = 64)
    private String settingKey;
    @Size(max = 2147483647)
    @Column(name = "setting_value", length = 2147483647)
    private String settingValue;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "grupo", nullable = false, length = 40)
    private String grupo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "label", nullable = false, length = 64)
    private String label;
    @Size(max = 2147483647)
    @Column(name = "descripcion", length = 2147483647)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "type", nullable = false, length = 64)
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "order_view", nullable = false)
    private int orderView;
    @Basic(optional = false)
    @NotNull
    @Column(name = "required", nullable = false)
    private boolean required;

    public AppSetting() {
    }

    public AppSetting(String settingKey) {
        this.settingKey = settingKey;
    }

    public AppSetting(String settingKey, String label, String value, String grupo, String type, int orderView, String descripcion, boolean req) {
        this.settingKey = settingKey;
        this.label = label;
        this.grupo = grupo;
        this.type = type;
        this.orderView = orderView;
        this.settingValue = value;
        this.descripcion = descripcion;
        this.required = req;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrderView() {
        return orderView;
    }

    public void setOrderView(int orderView) {
        this.orderView = orderView;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (settingKey != null ? settingKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppSetting)) {
            return false;
        }
        AppSetting other = (AppSetting) object;
        if ((this.settingKey == null && other.settingKey != null) || (this.settingKey != null && !this.settingKey.equals(other.settingKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AppSetting [");
        builder.append("getLabel=").append(getLabel());
        builder.append(", getSettingKey=").append(getSettingKey());
        builder.append(", getSettingValue=").append(getSettingValue());
        builder.append(", getType=").append(getType());
        builder.append("]");
        return builder.toString();
    }

  

    /**
     * @return the required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @param required the required to set
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
