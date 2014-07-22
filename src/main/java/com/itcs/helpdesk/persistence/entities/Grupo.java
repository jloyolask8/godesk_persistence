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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
@Table(name = "grupo")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type = TenantTableDiscriminatorType.SCHEMA, contextProperty = "eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g"),
    @NamedQuery(name = "Grupo.findByIdGrupo", query = "SELECT g FROM Grupo g WHERE g.idGrupo = :idGrupo"),
    @NamedQuery(name = "Grupo.findByNombre", query = "SELECT g FROM Grupo g WHERE g.nombre = :nombre")})
public class Grupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "id_grupo")
    private String idGrupo;
    @Size(max = 40)
    private String nombre;
    @Lob
    @Size(max = 2147483647)
    private String descripcion;
    
     @JoinTable(name = "CATEGORIA_GRUPO", joinColumns = {@JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo", table = "grupo")
        }, inverseJoinColumns = {@JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria", table = "categoria")
        })
    @ManyToMany
    private List<Categoria> categoriaList;
    @JoinColumn(name = "id_area", referencedColumnName = "id_area")
    @ManyToOne(optional = true)
    private Area idArea;
    
    
    @ManyToMany
    @JoinTable(name = "usuario_grupo",
            joinColumns = {@JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo")},
            inverseJoinColumns = {@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")})
    
    private List<Usuario> usuarioList;
    @NotNull
    private boolean editable;
    @OneToMany(mappedBy = "idGrupo")
    private List<Vista> vistaList;
    @OneToMany(mappedBy = "idGrupo")
    private List<Clipping> clippingList;
    @ManyToMany
    @JoinTable(name = "grupo_producto", joinColumns = {
        @JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo", table = "grupo")}, inverseJoinColumns = {
        @JoinColumn(name = "id_producto", referencedColumnName = "id_producto", table = "producto")})
    private List<Producto> productoList;

    public Grupo() {
    }

    public Grupo(String idGrupo, String nombre, String descripcion, Area idArea/*, List<Usuario> usuarioList*/, boolean editable) {
        this.idGrupo = idGrupo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idArea = idArea;
        //this.usuarioList = usuarioList;
        this.editable = editable;
    }

    public Grupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
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
    public List<Categoria> getCategoriaList() {
        return categoriaList;
    }

    public void setCategoriaList(List<Categoria> categoriaList) {
        this.categoriaList = categoriaList;
    }

    public Area getIdArea() {
        return idArea;
    }

    public void setIdArea(Area idArea) {
        this.idArea = idArea;
    }

    @XmlTransient
    public List<Vista> getVistaList() {
        return vistaList;
    }

    public void setVistaList(List<Vista> vistaList) {
        this.vistaList = vistaList;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGrupo != null ? idGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.idGrupo == null && other.idGrupo != null) || (this.idGrupo != null && !this.idGrupo.equals(other.idGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
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

    @XmlTransient
    public List<Clipping> getClippingList() {
        return clippingList;
    }

    public void setClippingList(List<Clipping> clippingList) {
        this.clippingList = clippingList;
    }

    /**
     * @return the productoList
     */
    public List<Producto> getProductoList() {
        return productoList;
    }

    /**
     * @param productoList the productoList to set
     */
    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }
}
