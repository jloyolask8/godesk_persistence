/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import com.itcs.helpdesk.persistence.entityenums.EnumFieldType;
import com.itcs.helpdesk.persistence.utils.FilterField;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "prioridad")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prioridad.findAll", query = "SELECT p FROM Prioridad p"),
    @NamedQuery(name = "Prioridad.findByIdPrioridad", query = "SELECT p FROM Prioridad p WHERE p.idPrioridad = :idPrioridad"),
//    @NamedQuery(name = "Prioridad.findByTipoCaso", query = "SELECT p FROM Prioridad p WHERE p.tipoCaso = :tipoCaso"),
    @NamedQuery(name = "Prioridad.findByNombre", query = "SELECT p FROM Prioridad p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Prioridad.findBySlaHoras", query = "SELECT p FROM Prioridad p WHERE p.slaHoras = :slaHoras")})
public class Prioridad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "id_prioridad")
    private String idPrioridad;
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Nombre", fieldIdFull = "nombre", fieldTypeFull = String.class)
    @Size(max = 40)
    private String nombre;
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Descripción", fieldIdFull = "descripcion", fieldTypeFull = String.class)
    @Lob
    @Size(max = 2147483647)
    private String descripcion;
    @Column(name = "sla_horas")
    private Integer slaHoras;
    @OneToMany(mappedBy = "idPrioridad")
    private List<Caso> casoList;
//    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Tipo", fieldIdFull = "tipoCaso.idTipoCaso")
//    @JoinColumn(name = "id_tipo_caso", referencedColumnName = "id_tipo_caso")
//    @ManyToOne
//    private TipoCaso tipoCaso;

    public Prioridad() {
    }

    public Prioridad(String idPrioridad) {
        this.idPrioridad = idPrioridad;
    }

    public Prioridad(String idPrioridad, String nombre, String descripcion, Integer slaHoras) {
//        this.tipoCaso = tipoCaso;
        this.idPrioridad = idPrioridad;
        this.slaHoras = slaHoras;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getIdPrioridad() {
        return idPrioridad;
    }

    public void setIdPrioridad(String idPrioridad) {
        this.idPrioridad = idPrioridad;
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

    public Integer getSlaHoras() {
        return slaHoras;
    }

    public void setSlaHoras(Integer slaHoras) {
        this.slaHoras = slaHoras;
    }

    @XmlTransient
    public List<Caso> getCasoList() {
        return casoList;
    }

    public void setCasoList(List<Caso> casoList) {
        this.casoList = casoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPrioridad != null ? idPrioridad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prioridad)) {
            return false;
        }
        Prioridad other = (Prioridad) object;
        if ((this.idPrioridad == null && other.idPrioridad != null) || (this.idPrioridad != null && !this.idPrioridad.equals(other.idPrioridad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
