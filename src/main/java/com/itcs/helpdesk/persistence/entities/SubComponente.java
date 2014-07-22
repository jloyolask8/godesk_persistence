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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "SUB_COMPONENTE")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubComponente.findAll", query = "SELECT s FROM SubComponente s"),
    @NamedQuery(name = "SubComponente.findByIdSubComponente", query = "SELECT s FROM SubComponente s WHERE s.idSubComponente = :idSubComponente"),
    @NamedQuery(name = "SubComponente.findByNombre", query = "SELECT s FROM SubComponente s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SubComponente.findByDescripcion", query = "SELECT s FROM SubComponente s WHERE s.descripcion = :descripcion")})
public class SubComponente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "ID_SUB_COMPONENTE")
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "ID", fieldIdFull = "idSubComponente", fieldTypeFull = String.class)
    private String idSubComponente;
    @Size(max = 200)
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Nombre", fieldIdFull = "nombre", fieldTypeFull = String.class)
    private String nombre;
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Descripcion", fieldIdFull = "descripcion", fieldTypeFull = String.class)
    private String descripcion;
    @OneToMany(mappedBy = "idSubComponente")
    private List<Caso> casoList;
    @JoinColumn(name = "ID_COMPONENTE", referencedColumnName = "ID_COMPONENTE")
    @ManyToOne(optional = false)
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Componente", fieldIdFull = "idComponente.idComponente", fieldTypeFull = String.class)
    private Componente idComponente;
    @JoinColumns({
        @JoinColumn(name = "id_modelo", referencedColumnName = "id_modelo"),
        @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")})
    @ManyToOne
    private ModeloProducto modelo;

    public SubComponente() {
    }

    public SubComponente(String idSubComponente) {
        this.idSubComponente = idSubComponente;
    }

    public String getIdSubComponente() {
        return idSubComponente;
    }

    public void setIdSubComponente(String idSubComponente) {
        this.idSubComponente = idSubComponente;
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

    public Componente getIdComponente() {
        return idComponente;
    }

    public void setIdComponente(Componente idComponente) {
        this.idComponente = idComponente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSubComponente != null ? idSubComponente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubComponente)) {
            return false;
        }
        SubComponente other = (SubComponente) object;
        if ((this.idSubComponente == null && other.idSubComponente != null) || (this.idSubComponente != null && !this.idSubComponente.equals(other.idSubComponente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.cnsv.referidos.persistence.entities.SubComponente[ idSubComponente=" + idSubComponente + " ]";
    }

    /**
     * @return the modelo
     */
    public ModeloProducto getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(ModeloProducto modelo) {
        this.modelo = modelo;
    }
}
