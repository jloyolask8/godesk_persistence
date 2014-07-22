/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import com.itcs.helpdesk.persistence.entityenums.EnumFieldType;
import com.itcs.helpdesk.persistence.utils.FilterField;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
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
@Table(name = "AUDIT_LOG")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuditLog.findAll", query = "SELECT a FROM AuditLog a"),
    @NamedQuery(name = "AuditLog.findByIdLog", query = "SELECT a FROM AuditLog a WHERE a.idLog = :idLog"),
    @NamedQuery(name = "AuditLog.findByTabla", query = "SELECT a FROM AuditLog a WHERE a.tabla = :tabla"),
    @NamedQuery(name = "AuditLog.findByCampo", query = "SELECT a FROM AuditLog a WHERE a.campo = :campo"),
    @NamedQuery(name = "AuditLog.findByFecha", query = "SELECT a FROM AuditLog a WHERE a.fecha = :fecha")})
public class AuditLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Long idLog;
    @Basic(optional = false)
    @Size(max = 100)
    private String tabla;
    @Basic(optional = false)
    @Size(max = 100)
    private String campo;
    @Basic(optional = false)
//    @Lob
    @Size(max = 2147483647)
    @Column(name = "old_value")
    private String oldValue;
    @Basic(optional = false)
    @NotNull
//    @Lob
    @Size(max = 2147483647)
    @Column(name = "new_value")
    private String newValue;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @FilterField(fieldTypeId = EnumFieldType.CALENDAR, label = "fecha", fieldIdFull = "fecha", fieldTypeFull = Date.class)
    private Date fecha;
    @NotNull
    @Column(name = "id_user")
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "idUser", fieldIdFull = "idUser", fieldTypeFull = String.class)
    private String idUser;
    @NotNull
    @Column(name = "id_caso")
    @FilterField(fieldTypeId = EnumFieldType.NUMBER, label = "idCaso", fieldIdFull = "idCaso", fieldTypeFull = Long.class)
    private Long idCaso;
    @Column(name = "owner")
     @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "owner", fieldIdFull = "owner", fieldTypeFull = String.class)
    private String owner;

    @Transient
    private String idCampo;
            
    public AuditLog() {
    }

    public AuditLog(Long idLog) {
        this.idLog = idLog;
    }

    public AuditLog(Long idLog, String tabla, String campo, String oldValue, String newValue, Date fecha) {
        this.idLog = idLog;
        this.tabla = tabla;
        this.campo = campo;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.fecha = fecha;
    }

    public Long getIdLog() {
        return idLog;
    }

    public void setIdLog(Long idLog) {
        this.idLog = idLog;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Long getIdCaso() {
        return idCaso;
    }

    public void setIdCaso(Long idCaso) {
        this.idCaso = idCaso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLog != null ? idLog.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuditLog)) {
            return false;
        }
        AuditLog other = (AuditLog) object;
        if ((this.idLog == null && other.idLog != null) || (this.idLog != null && !this.idLog.equals(other.idLog))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuditLog{" + "idLog=" + idLog + ", tabla=" + tabla + ", campo=" + campo + ", oldValue=" + oldValue + ", newValue=" + newValue + ", fecha=" + fecha + ", idUser=" + idUser + ", idCaso=" + idCaso + '}';
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the idCampo
     */
    public String getIdCampo() {
        return idCampo;
    }

    /**
     * @param idCampo the idCampo to set
     */
    public void setIdCampo(String idCampo) {
        this.idCampo = idCampo;
    }

    
}
