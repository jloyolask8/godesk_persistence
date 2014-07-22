/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.utils.vo;

import com.itcs.helpdesk.persistence.entities.Usuario;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author danilomoya
 */
public class AuditLogVO implements Serializable{
    private Long idLog;
    private String idLogStr;
    private String tabla;
    private String campo;
    private String oldValue;
    private String newValue;
    private Date fecha;
    private Usuario idUser;
    private Usuario idOwner;
    private boolean alertLevel;
    
    private String idCasoStr;
    private Long idCaso;

    private Date fechaInicio;
    private Date fechaFin;

    public AuditLogVO(){}

    public Long getIdCaso() {
        return idCaso;
    }

    public void setIdCaso(Long idCaso) {
        this.idCaso = idCaso;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getIdLog() {
        return idLog;
    }

    public void setIdLog(Long idLog) {
        this.idLog = idLog;
    }

    public Usuario getIdUser() {
        return idUser;
    }

    public void setIdUser(Usuario idUser) {
        this.idUser = idUser;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the idLogStr
     */
    public String getIdLogStr() {
        return idLogStr;
    }

    /**
     * @param idLogStr the idLogStr to set
     */
    public void setIdLogStr(String idLogStr) {
        this.idLogStr = idLogStr;
    }

    /**
     * @return the idCasoStr
     */
    public String getIdCasoStr() {
        return idCasoStr;
    }

    /**
     * @param idCasoStr the idCasoStr to set
     */
    public void setIdCasoStr(String idCasoStr) {
        this.idCasoStr = idCasoStr;
    }

    /**
     * @return the alertLevel
     */
    public boolean isAlertLevel() {
        return alertLevel;
    }

    /**
     * @param alertLevel the alertLevel to set
     */
    public void setAlertLevel(boolean alertLevel) {
        this.alertLevel = alertLevel;
    }

    /**
     * @return the idOwner
     */
    public Usuario getIdOwner() {
        return idOwner;
    }

    /**
     * @param idOwner the idOwner to set
     */
    public void setIdOwner(Usuario idOwner) {
        this.idOwner = idOwner;
    }
}
