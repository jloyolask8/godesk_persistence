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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
@Table(name = "sub_estado_caso")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type = TenantTableDiscriminatorType.SCHEMA, contextProperty = "eclipselink.tenant-id")
@NamedQueries({
    @NamedQuery(name = "SubEstadoCaso.findAll", query = "SELECT s FROM SubEstadoCaso s"),
    @NamedQuery(name = "SubEstadoCaso.findByIdSubEstado", query = "SELECT s FROM SubEstadoCaso s WHERE s.idSubEstado = :idSubEstado"),
    @NamedQuery(name = "SubEstadoCaso.findByIdEstado", query = "SELECT s FROM SubEstadoCaso s WHERE s.idEstado.idEstado = :idEstado"),
    @NamedQuery(name = "SubEstadoCaso.findByIdEstadoTipoCaso", query = "SELECT s FROM SubEstadoCaso s WHERE s.idEstado.idEstado = :idEstado AND s.tipoCaso.idTipoCaso = :tipoCaso"),
    @NamedQuery(name = "SubEstadoCaso.findByNombre", query = "SELECT s FROM SubEstadoCaso s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SubEstadoCaso.findByDescripcion", query = "SELECT s FROM SubEstadoCaso s WHERE s.descripcion = :descripcion")
})
public class SubEstadoCaso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "id_sub_estado")
    private String idSubEstado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    private String nombre;
    private String descripcion;
    @OneToMany(mappedBy = "idSubEstado")
    private List<Caso> casoList;
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Estado", fieldIdFull = "idEstado.idEstado", fieldTypeFull = String.class)
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne
    private EstadoCaso idEstado;
    @NotNull
    private boolean editable;
    @Basic(optional = false)
    @NotNull
    @Size(min = 3, max = 6)
    @Column(name = "font_color")
    private String fontColor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 3, max = 6)
    @Column(name = "background_color")
    private String backgroundColor;
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Tipo", fieldIdFull = "tipoCaso.idTipoCaso", fieldTypeFull = String.class)
    @JoinColumn(name = "id_tipo_caso", referencedColumnName = "id_tipo_caso")
    @ManyToOne
    private TipoCaso tipoCaso;

    @NotNull
    private boolean first;

    public SubEstadoCaso() {
    }

    public SubEstadoCaso(String idSubEstado) {
        this.idSubEstado = idSubEstado;
    }

    public SubEstadoCaso(TipoCaso tipoCaso, String idSubEstado, String nombre, EstadoCaso idEstado, String descripcion, boolean editable, String fontColor, String backgroundColor, boolean first) {
        this.tipoCaso = tipoCaso;
        this.idSubEstado = idSubEstado;
        this.nombre = nombre;
        this.idEstado = idEstado;
        this.descripcion = descripcion;
        this.editable = editable;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        this.first = first;
    }

    public SubEstadoCaso(String idSubEstado, String nombre) {
        this.idSubEstado = idSubEstado;
        this.nombre = nombre;
    }

    public String getIdSubEstado() {
        return idSubEstado;
    }

    public void setIdSubEstado(String idSubEstado) {
        this.idSubEstado = idSubEstado;
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

//    @XmlTransient
//    public List<Categoria> getCategoriaList() {
//        return categoriaList;
//    }
//
//    public void setCategoriaList(List<Categoria> categoriaList) {
//        this.categoriaList = categoriaList;
//    }
    @XmlTransient
    public List<Caso> getCasoList() {
        return casoList;
    }

    public void setCasoList(List<Caso> casoList) {
        this.casoList = casoList;
    }

    public EstadoCaso getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(EstadoCaso idEstado) {
        this.idEstado = idEstado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSubEstado != null ? idSubEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubEstadoCaso)) {
            return false;
        }
        SubEstadoCaso other = (SubEstadoCaso) object;
        if ((this.idSubEstado == null && other.idSubEstado != null) || (this.idSubEstado != null && !this.idSubEstado.equals(other.idSubEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getTipoCaso().getNombre() + " > " + nombre;
    }

    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * @return the fontColor
     */
    public String getFontColor() {
        return fontColor;
    }

    /**
     * @param fontColor the fontColor to set
     */
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * @return the backgroundColor
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return the tipoCaso
     */
    public TipoCaso getTipoCaso() {
        return tipoCaso;
    }

    /**
     * @param tipoCaso the tipoCaso to set
     */
    public void setTipoCaso(TipoCaso tipoCaso) {
        this.tipoCaso = tipoCaso;
    }

    /**
     * @return the first
     */
    public boolean isFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(boolean first) {
        this.first = first;
    }
}
