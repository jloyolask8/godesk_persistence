/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import com.itcs.helpdesk.persistence.entityenums.EnumFieldType;
import com.itcs.helpdesk.persistence.utils.FilterField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "schedule_event")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type = TenantTableDiscriminatorType.SCHEMA, contextProperty = "eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScheduleEvent.findByEventId", query = "SELECT s FROM ScheduleEvent s WHERE s.eventId = :eventId"),
    @NamedQuery(name = "ScheduleEvent.findByTitle", query = "SELECT s FROM ScheduleEvent s WHERE s.title = :title"),
    @NamedQuery(name = "ScheduleEvent.findByPublicEvent", query = "SELECT s FROM ScheduleEvent s WHERE s.publicEvent = :publicEvent")})
public class ScheduleEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "event_id")
    private Integer eventId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "title")
    private String title;
    @FilterField(fieldTypeId = EnumFieldType.CALENDAR, label = "Start Date", fieldIdFull = "startDate", fieldTypeFull = Date.class)
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @FilterField(fieldTypeId = EnumFieldType.CALENDAR, label = "End Date", fieldIdFull = "endDate", fieldTypeFull = Date.class)
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "all_day")
    private Boolean allDay;
    @FilterField(fieldTypeId = EnumFieldType.CALENDAR, label = "Creado", fieldIdFull = "fechaCreacion", fieldTypeFull = Date.class)
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Size(max = 40)
    @Column(name = "color_style_class")
    private String colorStyleClass;
    @Size(max = 2147483647)
    @Column(name = "email_invitados_csv")
    private String emailInvitadosCsv;
    @Column(name = "public_event")
    private Boolean publicEvent = Boolean.TRUE;//default
    @Size(max = 2000)
    @Column(name = "lugar")
    private String lugar;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Agente (Propietario)", fieldIdFull = "idUsuario.idUsuario", fieldTypeFull = String.class)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuario;

    @Column(name = "execute_action")
    private Boolean executeAction;

    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Tipo Accion", fieldIdFull = "idTipoAccion.idTipoAccion", fieldTypeFull = String.class)
    @JoinColumn(name = "id_tipo_accion", referencedColumnName = "id_tipo_accion")
    @ManyToOne
    private TipoAccion idTipoAccion;

    @Size(max = 2147483647)
    @Column(name = "parametros_accion")
    private String parametrosAccion;

    @Size(max = 200)
    @Column(name = "quartz_job_id")
    private String quartzJobId;

    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Caso", fieldIdFull = "idCaso.idCaso", fieldTypeFull = Long.class)
    @JoinColumn(name = "id_caso", referencedColumnName = "id_caso")
    @ManyToOne
    private Caso idCaso;

    @OneToMany(mappedBy = "eventId", cascade = CascadeType.ALL)
    private List<ScheduleEventReminder> scheduleEventReminderList;

    @FilterField(fieldTypeId = EnumFieldType.SELECT_MANY_ENTITIES, label = "resourceList", fieldIdFull = "resourceList", fieldTypeFull = List.class, listGenericTypeFieldId = "idResource")
    @JoinTable(name = "schedule_event_resource", joinColumns = {
        @JoinColumn(name = "event_id", referencedColumnName = "event_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "id_resource", referencedColumnName = "id_resource")
            })
    @ManyToMany(cascade = CascadeType.MERGE)
//     @ManyToMany(mappedBy = "scheduleEventList")
    private List<Resource> resourceList;

    @FilterField(fieldTypeId = EnumFieldType.SELECT_MANY_ENTITIES, label = "usuariosInvitedList", fieldIdFull = "usuariosInvitedList", fieldTypeFull = List.class, listGenericTypeFieldId = "idUsuario")
//       @ManyToMany(mappedBy = "scheduleEventList")
    @JoinTable(name = "schedule_event_usuario", joinColumns = {
        @JoinColumn(name = "event_id", referencedColumnName = "event_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
            })
    @ManyToMany
    private List<Usuario> usuariosInvitedList;

    public ScheduleEvent() {

    }

    public ScheduleEvent(Integer eventId) {
        this.eventId = eventId;
    }

    public ScheduleEvent(Integer eventId, String title, Date startDate, Date fechaCreacion) {
        this.eventId = eventId;
        this.title = title;
        this.startDate = startDate;
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @return the valoresList
     */
    public List<String> getEmailInvitadosCsvList() {
        final List<String> result = new ArrayList<String>();
        if (emailInvitadosCsv != null) {
            for (String value : emailInvitadosCsv.split(",", -1)) {
                final String trimmedValue = value.trim();
                result.add(trimmedValue);
            }
        }
        return result;
    }

    /**
     * @param valoresList the valoresList to set
     */
    public void setEmailInvitadosCsvList(List<String> valoresList) {
        emailInvitadosCsv = "";
        boolean first = true;
        for (String string : valoresList) {
            if (first) {
                first = false;
                emailInvitadosCsv += string;
            } else {
                emailInvitadosCsv += "," + string;
            }
        }
    }

    public final void addNewScheduleEventReminder() {
        if (scheduleEventReminderList == null) {
            scheduleEventReminderList = new LinkedList<ScheduleEventReminder>();
        }

        Random randomGenerator = new Random();
        int n = randomGenerator.nextInt();
        if (n > 0) {
            n = n * (-1);
        }
        final ScheduleEventReminder scheduleEventReminder = new ScheduleEventReminder(n);
        scheduleEventReminder.setEventId(this);
        scheduleEventReminderList.add(scheduleEventReminder);
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getParametrosAccion() {
        return parametrosAccion;
    }

    public void setParametrosAccion(String parametrosAccion) {
        this.parametrosAccion = parametrosAccion;
    }

    public String getColorStyleClass() {
        return colorStyleClass;
    }

    public void setColorStyleClass(String colorStyleClass) {
        this.colorStyleClass = colorStyleClass;
    }

    public String getEmailInvitadosCsv() {
        return emailInvitadosCsv;
    }

    public void setEmailInvitadosCsv(String emailInvitadosCsv) {
        this.emailInvitadosCsv = emailInvitadosCsv;
    }

    public Boolean getPublicEvent() {
        return publicEvent;
    }

    public void setPublicEvent(Boolean publicEvent) {
        this.publicEvent = publicEvent;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public TipoAccion getIdTipoAccion() {
        return idTipoAccion;
    }

    public void setIdTipoAccion(TipoAccion idTipoAccion) {
        this.idTipoAccion = idTipoAccion;
    }

    public Caso getIdCaso() {
        return idCaso;
    }

    public void setIdCaso(Caso idCaso) {
        this.idCaso = idCaso;
    }

    @XmlTransient
    public List<ScheduleEventReminder> getScheduleEventReminderList() {
        return scheduleEventReminderList;
    }

    public void setScheduleEventReminderList(List<ScheduleEventReminder> scheduleEventReminderList) {
        this.scheduleEventReminderList = scheduleEventReminderList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventId != null ? eventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ScheduleEvent)) {
            return false;
        }
        ScheduleEvent other = (ScheduleEvent) object;
        if ((this.eventId == null && other.eventId != null) || (this.eventId != null && !this.eventId.equals(other.eventId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ScheduleEvent{" + "eventId=" + eventId + ", title=" + title + ", startDate=" + startDate + ", endDate=" + endDate + ", allDay=" + allDay + ", fechaCreacion=" + fechaCreacion + ", colorStyleClass=" + colorStyleClass + ", emailInvitadosCsv=" + emailInvitadosCsv + ", publicEvent=" + publicEvent + ", lugar=" + lugar + ", descripcion=" + descripcion + ", idUsuario=" + idUsuario + ", executeAction=" + executeAction + ", idTipoAccion=" + idTipoAccion + ", parametrosAccion=" + parametrosAccion + ", quartzJobId=" + quartzJobId + ", idCaso=" + idCaso + '}';
    }

    /**
     * @return the executeAction
     */
    public Boolean getExecuteAction() {
        return executeAction;
    }

    /**
     * @param executeAction the executeAction to set
     */
    public void setExecuteAction(Boolean executeAction) {
        this.executeAction = executeAction;
    }

    /**
     * @return the quartzJobId
     */
    public String getQuartzJobId() {
        return quartzJobId;
    }

    /**
     * @param quartzJobId the quartzJobId to set
     */
    public void setQuartzJobId(String quartzJobId) {
        this.quartzJobId = quartzJobId;
    }

    /**
     * @return the resourceList
     */
    public List<Resource> getResourceList() {
        return resourceList;
    }

    /**
     * @param resourceList the resourceList to set
     */
    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    /**
     * @return the usuariosInvitedList
     */
    public List<Usuario> getUsuariosInvitedList() {
        return usuariosInvitedList;
    }

    /**
     * @param usuariosInvitedList the usuariosInvitedList to set
     */
    public void setUsuariosInvitedList(List<Usuario> usuariosInvitedList) {
        this.usuariosInvitedList = usuariosInvitedList;
    }

}
