/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import com.itcs.helpdesk.persistence.entityenums.EnumFieldType;
import com.itcs.helpdesk.persistence.utils.FilterField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantTableDiscriminator;
import org.eclipse.persistence.annotations.TenantTableDiscriminatorType;

/**
 *
 * @author jonathan
 */
@Entity
@Table(name = "vista")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@NamedQueries({
    @NamedQuery(name = "Vista.findAll", query = "SELECT v FROM Vista v"),
    @NamedQuery(name = "Vista.findByIdVista", query = "SELECT v FROM Vista v WHERE v.idVista = :idVista"),
    @NamedQuery(name = "Vista.findByNombre", query = "SELECT v FROM Vista v WHERE v.nombre = :nombre"),
    @NamedQuery(name = "Vista.findByDescripcion", query = "SELECT v FROM Vista v WHERE v.descripcion = :descripcion")
})
public class Vista implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_vista", nullable = false)
    private Integer idVista;
     @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Nombre", fieldIdFull = "nombre", fieldTypeFull = String.class)
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nombre", nullable = false, length = 40)
    private String nombre;
     @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Descripción", fieldIdFull = "descripcion", fieldTypeFull = String.class)
    @Size(max = 40)
    @Column(name = "descripcion", length = 40)
    private String descripcion;
    @FilterField(fieldTypeId = EnumFieldType.CHECKBOX, label = "Visible a todos", fieldIdFull = "visibleToAll", fieldTypeFull = Boolean.class)
    @Basic(optional = false)
    @NotNull
    @Column(name = "visible_to_all", nullable = false)
    private boolean visibleToAll;
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Creada por", fieldIdFull = "idUsuarioCreadaPor.capitalName",  fieldTypeFull = String.class)
    @JoinColumn(name = "id_usuario_creada_por", referencedColumnName = "id_usuario", nullable = false)
    @ManyToOne(optional = false)
    private Usuario idUsuarioCreadaPor;
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Grupo", fieldIdFull = "idGrupo.idGrupo",  fieldTypeFull = String.class)
    @JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo")
    @ManyToOne
    private Grupo idGrupo;
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Area", fieldIdFull = "idArea.idArea",  fieldTypeFull = String.class)
    @JoinColumn(name = "id_area", referencedColumnName = "id_area")
    @ManyToOne
    private Area idArea;
    @OneToMany(mappedBy = "idVista", cascade = CascadeType.ALL)
    private List<FiltroVista> filtrosVistaList;
    @Size(max = 1000)
    @Column(name = "base_entity_type", nullable = false, length = 1000)
    private String baseEntityType;

    public Vista() {
    }

    public Vista(Class clazz) {
        this.baseEntityType = clazz.getName();
        filtrosVistaList = new ArrayList<FiltroVista>();
    }

    public Vista(Integer idVista) {
        this.idVista = idVista;
    }

    public Vista(Integer idVista, String nombre, boolean visibleToAll) {
        this.idVista = idVista;
        this.nombre = nombre;
        this.visibleToAll = visibleToAll;
    }

    public Integer getIdVista() {
        return idVista;
    }

    public void setIdVista(Integer idVista) {
        this.idVista = idVista;
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

    public boolean getVisibleToAll() {
        return visibleToAll;
    }

    public void setVisibleToAll(boolean visibleToAll) {
        this.visibleToAll = visibleToAll;
    }

    public Usuario getIdUsuarioCreadaPor() {
        return idUsuarioCreadaPor;
    }

    public void setIdUsuarioCreadaPor(Usuario idUsuarioCreadaPor) {
        this.idUsuarioCreadaPor = idUsuarioCreadaPor;
    }

    public Grupo getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Grupo idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Area getIdArea() {
        return idArea;
    }

    public void setIdArea(Area idArea) {
        this.idArea = idArea;
    }

    public List<FiltroVista> getFiltrosVistaList() {
        return filtrosVistaList;
    }

    public void setFiltrosVistaList(List<FiltroVista> filtrosVistaList) {
        this.filtrosVistaList = filtrosVistaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVista != null ? idVista.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vista)) {
            return false;
        }
        Vista other = (Vista) object;
        if ((this.idVista == null && other.idVista != null) || (this.idVista != null && !this.idVista.equals(other.idVista))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Vista [" + "Criterios: " + filtrosVistaList + (idArea != null ? " Area: " + idArea : "") + (idArea != null ? " Grupo: " + idGrupo : "") + " visibleToAll: " + visibleToAll + " creadaPor:" + this.idUsuarioCreadaPor + "]";
    }

    public void addFiltroVista(FiltroVista filtro) {                      
        if (this.getFiltrosVistaList() == null || this.getFiltrosVistaList().isEmpty()) {
            this.setFiltrosVistaList(new ArrayList<FiltroVista>());
        }
        this.getFiltrosVistaList().add(filtro);
        filtro.setIdVista(this);
    }
    
    public void addNewFiltroVista() {
        FiltroVista filtro = new FiltroVista();
        Random randomGenerator = new Random();
        int n = randomGenerator.nextInt();
        if (n > 0) {
            n = n * (-1);
        }
        filtro.setIdFiltro(n);//Ugly patch to solve identifier unknown when new items are added to the datatable.
        if (this.getFiltrosVistaList() == null || this.getFiltrosVistaList().isEmpty()) {
            this.setFiltrosVistaList(new ArrayList<FiltroVista>());
        }
        this.getFiltrosVistaList().add(filtro);
        filtro.setIdVista(this);
    }

    public void removeFiltroFromVista(FiltroVista filtro) {
        if (this.getFiltrosVistaList() != null) {
            if (this.getFiltrosVistaList().contains(filtro)) {
                this.getFiltrosVistaList().remove(filtro);
            }
            filtro.setIdVista(null);
        }
    }

    /**
     * @return the baseEntityType
     */
    public String getBaseEntityType() {
        return baseEntityType;
    }

    /**
     * @param baseEntityType the baseEntityType to set
     */
    public void setBaseEntityType(String baseEntityType) {
        this.baseEntityType = baseEntityType;
    }
}
