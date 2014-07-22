package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
@Table(name = "TIPO_COMPARACION")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoComparacion.findAll", query = "SELECT t FROM TipoComparacion t"),
    @NamedQuery(name = "TipoComparacion.findByIdComparador", query = "SELECT t FROM TipoComparacion t WHERE t.idComparador = :idComparador"),
    @NamedQuery(name = "TipoComparacion.findBySimbolo", query = "SELECT t FROM TipoComparacion t WHERE t.simbolo = :simbolo"),
    @NamedQuery(name = "TipoComparacion.findByDescripcion", query = "SELECT t FROM TipoComparacion t WHERE t.descripcion = :descripcion")})
public class TipoComparacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 2)
    @Column(name = "ID_COMPARADOR")
    private String idComparador;
    @Size(max = 2)
    private String simbolo;
    @Size(max = 64)
    private String nombre;
    @Size(max = 200)
    private String descripcion;
    

    public TipoComparacion() {
    }

    public TipoComparacion(String idComparador) {
        this.idComparador = idComparador;
    }

    public TipoComparacion(String nombre, String idComparador, String simbolo, String descripcion)
    {
        this.idComparador = idComparador;
        this.simbolo = simbolo;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }
    
    

    public String getIdComparador() {
        return idComparador;
    }

    public void setIdComparador(String idComparador) {
        this.idComparador = idComparador;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComparador != null ? idComparador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoComparacion)) {
            return false;
        }
        TipoComparacion other = (TipoComparacion) object;
        if ((this.idComparador == null && other.idComparador != null) || (this.idComparador != null && !this.idComparador.equals(other.idComparador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
