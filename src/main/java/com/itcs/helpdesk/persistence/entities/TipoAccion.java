/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantTableDiscriminator;
import org.eclipse.persistence.annotations.TenantTableDiscriminatorType;

/**
 *
 * @author jonathan
 */
@Entity
@Table(name = "TIPO_ACCION")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type = TenantTableDiscriminatorType.SCHEMA, contextProperty = "eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoAccion.findAll", query = "SELECT n FROM TipoAccion n"),
    @NamedQuery(name = "TipoAccion.findByIdTipoAccion", query = "SELECT n FROM TipoAccion n WHERE n.idTipoAccion = :idTipoAccion"),
    @NamedQuery(name = "TipoAccion.findByNombre", query = "SELECT n FROM TipoAccion n WHERE n.nombre = :nombre")})
public class TipoAccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "id_tipo_accion")
    private String idTipoAccion;
    @Size(max = 64)
    private String nombre;
    @Size(max = 1000)
    @Column(name = "implementation_class_name")
    private String implementationClassName;
    @Lob
    @Size(max = 2147483647)
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoAccion")
    private List<Accion> accionList;
    @ManyToMany(mappedBy = "tipoAccionList", fetch = FetchType.EAGER)
    private List<CustomField> customFieldList;

    public TipoAccion() {
    }

    public TipoAccion(String idTipoAccion) {
        this.idTipoAccion = idTipoAccion;
    }

    public TipoAccion(String idTipoAccion, String nombre, String descripcion, String implementationClassName) {
        this.idTipoAccion = idTipoAccion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.implementationClassName = implementationClassName;
    }

    public String getIdTipoAccion() {
        return idTipoAccion;
    }

    public void setIdTipoAccion(String idTipoAccion) {
        this.idTipoAccion = idTipoAccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Accion> getAccionList() {
        return accionList;
    }

    public void setAccionList(List<Accion> accionList) {
        this.accionList = accionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoAccion != null ? idTipoAccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoAccion)) {
            return false;
        }
        TipoAccion other = (TipoAccion) object;
        if ((this.idTipoAccion == null && other.idTipoAccion != null) || (this.idTipoAccion != null && !this.idTipoAccion.equals(other.idTipoAccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

    /**
     * @return the customFieldList
     */
    public List<CustomField> getCustomFieldList() {
        return customFieldList;
    }

    /**
     * @param customFieldList the customFieldList to set
     */
    public void setCustomFieldList(List<CustomField> customFieldList) {
        this.customFieldList = customFieldList;
    }

    /**
     * @return the implementationClassName
     */
    public String getImplementationClassName() {
        return implementationClassName;
    }

    /**
     * @param implementationClassName the implementationClassName to set
     */
    public void setImplementationClassName(String implementationClassName) {
        this.implementationClassName = implementationClassName;
    }

}
