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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Table(name = "modelo_producto")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModeloProducto.findAll", query = "SELECT m FROM ModeloProducto m"),
    @NamedQuery(name = "ModeloProducto.findByIdModelo", query = "SELECT m FROM ModeloProducto m WHERE m.modeloProductoPK.idModelo = :idModelo"),
    @NamedQuery(name = "ModeloProducto.findByNombre", query = "SELECT m FROM ModeloProducto m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "ModeloProducto.findByIdProducto", query = "SELECT m FROM ModeloProducto m WHERE m.modeloProductoPK.idProducto = :idProducto")})
public class ModeloProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ModeloProductoPK modeloProductoPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Nombre", fieldIdFull = "nombre", fieldTypeFull = String.class)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 2147483647)
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Descripcion", fieldIdFull = "descripcion", fieldTypeFull = String.class)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "idModelo")
    private List<Caso> casoList;
    @OneToMany(mappedBy = "modelo")
    private List<SubComponente> subComponenteList;
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto;
    @JoinColumn(name = "id_componente", referencedColumnName = "id_componente")
    @ManyToOne
    private Componente idComponente;
    @Transient
    private String idModelo;

    public ModeloProducto() {
    }

    public ModeloProducto(ModeloProductoPK modeloProductoPK) {
        this.modeloProductoPK = modeloProductoPK;
    }

    public ModeloProducto(ModeloProductoPK modeloProductoPK, String nombre) {
        this.modeloProductoPK = modeloProductoPK;
        this.nombre = nombre;
    }

    public ModeloProducto(String idModelo, String idProducto) {
        this.modeloProductoPK = new ModeloProductoPK(idModelo, idProducto);
    }

    public ModeloProductoPK getModeloProductoPK() {
        return modeloProductoPK;
    }

    public void setModeloProductoPK(ModeloProductoPK modeloProductoPK) {
        this.modeloProductoPK = modeloProductoPK;
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
    public List<SubComponente> getSubComponenteList() {
        return subComponenteList;
    }

    public void setSubComponenteList(List<SubComponente> subComponenteList) {
        this.subComponenteList = subComponenteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modeloProductoPK != null ? modeloProductoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModeloProducto)) {
            return false;
        }
        ModeloProducto other = (ModeloProducto) object;
        if ((this.modeloProductoPK == null && other.modeloProductoPK != null) || (this.modeloProductoPK != null && !this.modeloProductoPK.equals(other.modeloProductoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ModeloProducto[ modeloProductoPK=" + modeloProductoPK + " ]";
    }

    /**
     * @return the producto
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * @param producto the producto to set
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * @return the idComponente
     */
    public Componente getIdComponente() {
        return idComponente;
    }

    /**
     * @param idComponente the idComponente to set
     */
    public void setIdComponente(Componente idComponente) {
        this.idComponente = idComponente;
    }

    /**
     * @return the idModelo
     */
    public String getIdModelo() {
        return idModelo;
    }

    /**
     * @param idModelo the idModelo to set
     */
    public void setIdModelo(String idModelo) {
        this.idModelo = idModelo;
    }
}
