/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
@Table(name = "resource")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type = TenantTableDiscriminatorType.SCHEMA, contextProperty = "eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resource.findAll", query = "SELECT r FROM Resource r"),
    @NamedQuery(name = "Resource.findByIdResource", query = "SELECT r FROM Resource r WHERE r.idResource = :idResource"),
    @NamedQuery(name = "Resource.findByNombre", query = "SELECT r FROM Resource r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Resource.findByDescripcion", query = "SELECT r FROM Resource r WHERE r.descripcion = :descripcion"),
    @NamedQuery(name = "Resource.findByTipo", query = "SELECT r FROM Resource r WHERE r.tipo = :tipo")})
public class Resource implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_resource")
    private Integer idResource;
    @Size(max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 100)
    @Column(name = "tipo")
    private String tipo;
    
    @ManyToMany(mappedBy = "resourceList", cascade = CascadeType.MERGE)
//     @JoinTable(name = "schedule_event_resource", joinColumns = {
//        @JoinColumn(name = "id_resource", referencedColumnName = "id_resource")}, inverseJoinColumns = {
//        @JoinColumn(name = "event_id", referencedColumnName = "event_id")})
//    @ManyToMany
    private List<ScheduleEvent> scheduleEventList;

    public Resource() {
    }

    public Resource(Integer idResource) {
        this.idResource = idResource;
    }

    public Integer getIdResource() {
        return idResource;
    }

    public void setIdResource(Integer idResource) {
        this.idResource = idResource;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public List<ScheduleEvent> getScheduleEventList() {
        return scheduleEventList;
    }

    public void setScheduleEventList(List<ScheduleEvent> scheduleEventList) {
        this.scheduleEventList = scheduleEventList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idResource != null ? idResource.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Resource)) {
            return false;
        }
        Resource other = (Resource) object;
        if ((this.idResource == null && other.idResource != null) || (this.idResource != null && !this.idResource.equals(other.idResource))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Resource{" + "idResource=" + idResource + ", nombre=" + nombre + ", descripcion=" + descripcion + ", tipo=" + tipo + '}';
    }

   
    
}
