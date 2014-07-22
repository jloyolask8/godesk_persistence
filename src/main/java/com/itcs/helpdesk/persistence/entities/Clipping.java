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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "clipping")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clipping.findAll", query = "SELECT c FROM Clipping c"),
    @NamedQuery(name = "Clipping.findByIdClipping", query = "SELECT c FROM Clipping c WHERE c.idClipping = :idClipping"),
    @NamedQuery(name = "Clipping.findByNombre", query = "SELECT c FROM Clipping c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Clipping.findByVisibleToAll", query = "SELECT c FROM Clipping c WHERE c.visibleToAll = :visibleToAll")})
public class Clipping implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_clipping", nullable = false)
    private Integer idClipping;
    @Basic(optional = false)
    @NotNull
    @Size(min = 6, max = 64)
    @Column(name = "nombre", nullable = false, length = 64)
    private String nombre;
    @Lob
    @Size(max = 2147483647)
    private String texto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "visible_to_all", nullable = false)
    private boolean visibleToAll;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo")
    @ManyToOne
    private Grupo idGrupo;
    @OneToMany(mappedBy = "idClippingPadre")
    private List<Clipping> clippingList;
    @JoinColumn(name = "id_clipping_padre", referencedColumnName = "id_clipping")
    @ManyToOne
    private Clipping idClippingPadre;
    @JoinColumn(name = "id_area", referencedColumnName = "id_area")
    @ManyToOne
    private Area idArea;
//    private Integer orden;
//    private boolean folder;
    @JoinColumn(name = "creada_por", referencedColumnName = "id_usuario", nullable = false)
    @ManyToOne(optional = false)
    private Usuario idUsuarioCreadaPor;

    public Clipping() {
    }

    public Clipping(Integer idClipping) {
        this.idClipping = idClipping;
    }

    public Clipping(Integer idClipping, String nombre, String texto, boolean visibleToAll) {
        this.idClipping = idClipping;
        this.nombre = nombre;
        this.texto = texto;
        this.visibleToAll = visibleToAll;
    }

    public Integer getIdClipping() {
        return idClipping;
    }

    public void setIdClipping(Integer idClipping) {
        this.idClipping = idClipping;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean getVisibleToAll() {
        return visibleToAll;
    }

    public void setVisibleToAll(boolean visibleToAll) {
        this.visibleToAll = visibleToAll;
    }

    public Grupo getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Grupo idGrupo) {
        this.idGrupo = idGrupo;
    }

    @XmlTransient
    public List<Clipping> getClippingList() {
        return clippingList;
    }

    public void setClippingList(List<Clipping> clippingList) {
        this.clippingList = clippingList;
    }

    public Clipping getIdClippingPadre() {
        return idClippingPadre;
    }

    public void setIdClippingPadre(Clipping idClippingPadre) {
        this.idClippingPadre = idClippingPadre;
    }

    public Area getIdArea() {
        return idArea;
    }

    public void setIdArea(Area idArea) {
        this.idArea = idArea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idClipping != null ? idClipping.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clipping)) {
            return false;
        }
        Clipping other = (Clipping) object;
        if ((this.idClipping == null && other.idClipping != null) || (this.idClipping != null && !this.idClipping.equals(other.idClipping))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + nombre;
    }



    /**
     * @return the idUsuarioCreadaPor
     */
    public Usuario getIdUsuarioCreadaPor() {
        return idUsuarioCreadaPor;
    }

    /**
     * @param idUsuarioCreadaPor the idUsuarioCreadaPor to set
     */
    public void setIdUsuarioCreadaPor(Usuario idUsuarioCreadaPor) {
        this.idUsuarioCreadaPor = idUsuarioCreadaPor;
    }

  
}
