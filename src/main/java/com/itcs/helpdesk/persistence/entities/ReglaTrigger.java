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
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
@Table(name = "REGLA_TRIGGER")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type = TenantTableDiscriminatorType.SCHEMA, contextProperty = "eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReglaTrigger.findAll", query = "SELECT r FROM ReglaTrigger r"),
//    @NamedQuery(name = "ReglaTrigger.findByEventoArea", query = "SELECT r FROM ReglaTrigger r WHERE r.evento = :evento and r.idArea = :idArea ORDER BY r.orden ASC"),
    @NamedQuery(name = "ReglaTrigger.findByEvento", query = "SELECT r FROM ReglaTrigger r WHERE r.evento = :evento ORDER BY r.orden ASC"),
    @NamedQuery(name = "ReglaTrigger.findByIdTrigger", query = "SELECT r FROM ReglaTrigger r WHERE r.idTrigger = :idTrigger")})
public class ReglaTrigger implements Serializable, Comparable<ReglaTrigger> {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "id_trigger")
    private String idTrigger;
    @Lob
    @Size(max = 2147483647)
    private String desccripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTrigger")
    private List<Condicion> condicionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTrigger")
    private List<Accion> accionList;
//    @JoinColumn(name = "id_area", referencedColumnName = "id_area")
//    @ManyToOne(optional = false)
//    private Area idArea;
    @FilterField(fieldTypeId = EnumFieldType.CHECKBOX, label = "reglaActiva", fieldIdFull = "reglaActiva", fieldTypeFull = Boolean.class)
    @Column(name = "regla_activa")
    private Boolean reglaActiva = true;
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Tipo de Evento", fieldIdFull = "evento", fieldTypeFull = String.class)
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "evento")
    private String evento;
    //ALTER TABLE regla_trigger ADD COLUMN "orden" integer NOT NULL DEFAULT 0;
    @Basic(optional = false)
    @NotNull
    @OrderBy(value = "orden ASC")
    private Integer orden;
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Area", fieldIdFull = "areaList", fieldTypeFull = List.class, listGenericTypeFieldId = "idArea")
    @JoinTable(name = "regla_trigger_area", joinColumns = {
        @JoinColumn(name = "id_trigger", referencedColumnName = "id_trigger", table = "REGLA_TRIGGER")
    }, inverseJoinColumns = {
        @JoinColumn(name = "id_area", referencedColumnName = "id_area", table = "area")})
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Area> areaList;

    public ReglaTrigger() {
        condicionList = new ArrayList<Condicion>();
    }

    public ReglaTrigger(String idTrigger) {
        this.idTrigger = idTrigger;
    }

    public String getIdTrigger() {
        return idTrigger;
    }

    public void setIdTrigger(String idTrigger) {
        this.idTrigger = idTrigger;
    }

    public String getDesccripcion() {
        return desccripcion;
    }

    public void setDesccripcion(String desccripcion) {
        this.desccripcion = desccripcion;
    }

    public List<Condicion> getCondicionList() {
        return condicionList;
    }

    public void setCondicionList(List<Condicion> condicionList) {
        this.condicionList = condicionList;
    }

    public List<Accion> getAccionList() {
        return accionList;
    }

    public void setAccionList(List<Accion> accionList) {
        this.accionList = accionList;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTrigger != null ? idTrigger.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReglaTrigger)) {
            return false;
        }
        ReglaTrigger other = (ReglaTrigger) object;
        if ((this.idTrigger == null && other.idTrigger != null) || (this.idTrigger != null && !this.idTrigger.equals(other.idTrigger))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReglaTrigger [" + "idTrigger " + idTrigger + " " + "orden = " + orden + "]";
    }

    /**
     * @return the reglaActiva
     */
    public Boolean getReglaActiva() {
        return reglaActiva;
    }

    /**
     * @param reglaActiva the reglaActiva to set
     */
    public void setReglaActiva(Boolean reglaActiva) {
        this.reglaActiva = reglaActiva;
    }

    /**
     * @return the evento
     */
    public String getEvento() {
        return evento;
    }

    /**
     * @param evento the evento to set
     */
    public void setEvento(String evento) {
        this.evento = evento;
    }

    /**
     * @return the orden
     */
    public Integer getOrden() {
        return orden;
    }

    /**
     * @param orden the orden to set
     */
    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public int compareTo(ReglaTrigger o) {
        //ascending order
        return this.orden - o.orden;
    }
}
