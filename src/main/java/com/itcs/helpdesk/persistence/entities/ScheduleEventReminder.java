/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "schedule_event_reminder")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type = TenantTableDiscriminatorType.SCHEMA, contextProperty = "eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScheduleEventReminder.findByReminderType", query = "SELECT s FROM ScheduleEventReminder s WHERE s.reminderType = :reminderType")
})
public class ScheduleEventReminder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_reminder")
    private Integer idReminder;
    /**
     * types: POPUP, EMAIL.
     */
    @Size(max = 100)
    @Column(name = "reminder_type")
    private String reminderType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity_time")
    private int quantityTime = 10;
    /**
     * minutos:=1 horas:=60 dias:60*24=1440 semanas:60*24*7=10080
     */
    @Column(name = "unit_of_time_in_minutes")
    private BigInteger unitOfTimeInMinutes;
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    @ManyToOne
    private ScheduleEvent eventId;

    @Size(max = 200)
    @Column(name = "quartz_job_id")
    private String quartzJobId;

    @Column(name = "notified_ok")
    private Boolean notifiedOk = Boolean.FALSE;//default

    public ScheduleEventReminder() {
    }

    public ScheduleEventReminder(Integer idReminder) {
        this.idReminder = idReminder;
    }

    public ScheduleEventReminder(Integer idReminder, int quantityTime) {
        this.idReminder = idReminder;
        this.quantityTime = quantityTime;
    }

    public Integer getIdReminder() {
        return idReminder;
    }

    public void setIdReminder(Integer idReminder) {
        this.idReminder = idReminder;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public int getQuantityTime() {
        return quantityTime;
    }

    public void setQuantityTime(int quantityTime) {
        this.quantityTime = quantityTime;
    }

    public BigInteger getUnitOfTimeInMinutes() {
        return unitOfTimeInMinutes;
    }

    public void setUnitOfTimeInMinutes(BigInteger unitOfTimeInMinutes) {
        this.unitOfTimeInMinutes = unitOfTimeInMinutes;
    }

    public ScheduleEvent getEventId() {
        return eventId;
    }

    public void setEventId(ScheduleEvent eventId) {
        this.eventId = eventId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReminder != null ? idReminder.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ScheduleEventReminder)) {
            return false;
        }
        ScheduleEventReminder other = (ScheduleEventReminder) object;
        if ((this.idReminder == null && other.idReminder != null) || (this.idReminder != null && !this.idReminder.equals(other.idReminder))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ScheduleEventReminder{" + "idReminder=" + idReminder + ", reminderType=" + reminderType + ", quantityTime=" + quantityTime + ", unitOfTimeInMinutes=" + unitOfTimeInMinutes + ", eventId=" + eventId + ", quartzJobId=" + quartzJobId + ", notifiedOk=" + notifiedOk + '}';
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
     * @return the notifiedOk
     */
    public Boolean getNotifiedOk() {
        return notifiedOk;
    }

    /**
     * @param notifiedOk the notifiedOk to set
     */
    public void setNotifiedOk(Boolean notifiedOk) {
        this.notifiedOk = notifiedOk;
    }

}
