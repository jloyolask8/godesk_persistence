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
@Table(name = "TIPO_NOTA")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoNota.findAll", query = "SELECT t FROM TipoNota t"),
    @NamedQuery(name = "TipoNota.findByIdTipoNota", query = "SELECT t FROM TipoNota t WHERE t.idTipoNota = :idTipoNota"),
    @NamedQuery(name = "TipoNota.findByNombre", query = "SELECT t FROM TipoNota t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoNota.findByDescripcion", query = "SELECT t FROM TipoNota t WHERE t.descripcion = :descripcion")})
public class TipoNota implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPO_NOTA")
    private Integer idTipoNota;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String nombre;
    @Size(max = 200)
    private String descripcion;
    @OneToMany(mappedBy = "idTipoNota")
    private List<Nota> notaList;

    public TipoNota() {
    }

    public TipoNota(Integer idTipoNota) {
        this.idTipoNota = idTipoNota;
    }

    public TipoNota(Integer idTipoNota, String nombre) {
        this.idTipoNota = idTipoNota;
        this.nombre = nombre;
    }

    public Integer getIdTipoNota() {
        return idTipoNota;
    }

    public void setIdTipoNota(Integer idTipoNota) {
        this.idTipoNota = idTipoNota;
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
    public List<Nota> getNotaList() {
        return notaList;
    }

    public void setNotaList(List<Nota> notaList) {
        this.notaList = notaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoNota != null ? idTipoNota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoNota)) {
            return false;
        }
        TipoNota other = (TipoNota) object;
        if ((this.idTipoNota == null && other.idTipoNota != null) || (this.idTipoNota != null && !this.idTipoNota.equals(other.idTipoNota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + "[ id=" + idTipoNota + " ]";
    }
    
}
