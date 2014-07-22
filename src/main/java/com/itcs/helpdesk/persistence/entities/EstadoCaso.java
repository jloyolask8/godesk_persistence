/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "ESTADO_CASO")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoCaso.findAll", query = "SELECT e FROM EstadoCaso e"),
    @NamedQuery(name = "EstadoCaso.findByIdEstado", query = "SELECT e FROM EstadoCaso e WHERE e.idEstado = :idEstado"),
    @NamedQuery(name = "EstadoCaso.findByNombre", query = "SELECT e FROM EstadoCaso e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EstadoCaso.findByDescripcion", query = "SELECT e FROM EstadoCaso e WHERE e.descripcion = :descripcion")})
public class EstadoCaso implements Serializable, Comparable<EstadoCaso> {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "id_estado")
    private String idEstado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    private String nombre;
    @Size(max = 200)
    private String descripcion;
    @OneToMany(mappedBy = "idEstado")
    private List<Caso> casoList;
    @OneToMany(mappedBy = "idEstado")
    private List<SubEstadoCaso> subEstadoCasoList;

    public EstadoCaso() {
    }

    public EstadoCaso(String idEstado) {
        this.idEstado = idEstado;
    }

    public EstadoCaso(String idEstado, String nombre) {
        this.idEstado = idEstado;
        this.nombre = nombre;
    }
    
     public EstadoCaso(String idEstado, String nombre, String descripcion) {
        this.idEstado = idEstado;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
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
    public List<Caso> getCasoList() {
        return casoList;
    }

    public void setCasoList(List<Caso> casoList) {
        this.casoList = casoList;
    }

    @XmlTransient
    public List<SubEstadoCaso> getSubEstadoCasoList() {
        return subEstadoCasoList;
    }

    public void setSubEstadoCasoList(List<SubEstadoCaso> subEstadoCasoList) {
        this.subEstadoCasoList = subEstadoCasoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstado != null ? idEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoCaso)) {
            return false;
        }
        EstadoCaso other = (EstadoCaso) object;
        if ((this.idEstado == null && other.idEstado != null) || (this.idEstado != null && !this.idEstado.equals(other.idEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public int compareTo(EstadoCaso o)
    {
        return this.getNombre().compareTo(o.getNombre());
    }
    
    
    
}
