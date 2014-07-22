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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "area")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Area.findAll", query = "SELECT a FROM Area a"),
    @NamedQuery(name = "Area.findByIdArea", query = "SELECT a FROM Area a WHERE a.idArea = :idArea"),
    @NamedQuery(name = "Area.findByNombre", query = "SELECT a FROM Area a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Area.findByEditable", query = "SELECT a FROM Area a WHERE a.editable = :editable")})
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "id_area", nullable = false, length = 40)
    private String idArea;
    @Size(max = 40)
    @Column(name = "nombre", length = 40)
    private String nombre;
    @Size(max = 2147483647)
    @Column(name = "descripcion", length = 2147483647)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "editable", nullable = false)
    private Boolean editable;
    @Size(max = 64)
    @Column(name = "mail_smtp_host", length = 64)
    private String mailSmtpHost;
    @Column(name = "mail_smtp_port")
    private Short mailSmtpPort;
    @Size(max = 64)
    @Column(name = "mail_smtp_user", length = 64)
    private String mailSmtpUser;
    @Size(max = 2147483647)
    @Column(name = "mail_smtp_password", length = 2147483647)
    private String mailSmtpPassword;
    @Size(max = 64)
    @Column(name = "mail_smtp_from", length = 64)
    private String mailSmtpFrom;
    @Size(max = 64)
    @Column(name = "mail_smtp_fromname", length = 64)
    private String mailSmtpFromname;
    @Column(name = "mail_smtp_auth")
    private Boolean mailSmtpAuth;
    @Column(name = "mail_smtp_socket_factory_port")
    private Short mailSmtpSocketFactoryPort;
    @Column(name = "mail_smtp_connectiontimeout")
    private Integer mailSmtpConnectiontimeout = 0;
    @Column(name = "mail_smtp_timeout")
    private Integer mailSmtpTimeout = 0;
    @Size(max = 64)
    @Column(name = "mail_smtp_encr_type", length = 64)
    private String mailSmtpEncriptionType = "none" ;    
    @Size(max = 64)
    @Column(name = "mail_server_type", length = 64)
    private String mailServerType = "popimap";
    @Size(max = 64)
    @Column(name = "mail_transport_protocol", length = 64)
    private String mailTransportProtocol;
    @Size(max = 64)
    @Column(name = "mail_store_protocol", length = 64)
    private String mailStoreProtocol;
    @Column(name = "mail_transport_tls")
    private Boolean mailTransportTls;
    @Column(name = "mail_use_jndi")
    private Boolean mailUseJndi;
    @Size(max = 64)
    @Column(name = "mail_session_jndiname", length = 64)
    private String mailSessionJndiname;
    @Size(max = 64)
    @Column(name = "mail_inbound_host", length = 64)
    private String mailInboundHost;
    @Column(name = "mail_inbound_port")
    private Short mailInboundPort;
    @Size(max = 64)
    @Column(name = "mail_inbound_user", length = 64)
    private String mailInboundUser;
    @Size(max = 2147483647)
    @Column(name = "mail_inbound_password", length = 2147483647)
    private String mailInboundPassword;
    @Column(name = "mail_inbound_ssl_enabled")
    private Boolean mailInboundSslEnabled;
    @Column(name = "email_enabled")
    private Boolean emailEnabled;
    @Column(name = "email_frecuencia")
    private Integer emailFrecuencia = 60;
    @Column(name = "email_acusederecibo")
    private Boolean emailAcusederecibo;
    @Size(max = 2147483647)
    @Column(name = "subject_resp_automatica", length = 2147483647)
    private String subjectRespAutomatica;
    @Size(max = 2147483647)
    @Column(name = "texto_resp_automatica", length = 2147483647)
    private String textoRespAutomatica;
    @Size(max = 2147483647)
    @Column(name = "texto_resp_caso", length = 2147483647)
    private String textoRespCaso;
    @Column(name = "mail_debug")
    private Boolean mailDebug;
    @Size(max = 64)
    @Column(name = "dominio_exchange_salida", length = 64)
    private String dominioExchangeSalida;
    @Size(max = 64)
    @Column(name = "dominio_exchange_inbound", length = 64)
    private String dominioExchangeInbound;
    @Basic(optional = false)
    @Size(min = 1, max = 64)
    @Column(name = "mail_server_type_salida", length = 64)
    private String mailServerTypeSalida;
    @Column(name = "mail_smtp_ssl_enable")
    private Boolean mailSmtpSslEnable;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "idArea")
    private List<Grupo> grupoList;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "idArea")
    private List<Categoria> categoriaList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idArea")
//    private List<ReglaTrigger> reglaTriggerList;
    @OneToMany(mappedBy = "idArea")
    private List<Vista> vistaList;
    @OneToMany(mappedBy = "idArea")
    private List<Clipping> clippingList;
    //...   
    @ManyToMany(mappedBy = "areaList", fetch = FetchType.LAZY)
    private List<ReglaTrigger> reglaTriggerList;

    public Area() {
    }

    public Area(String idArea) {
        this.idArea = idArea;
    }

    public Area(String idArea, String nombre, String descripcion, boolean editable) {
        this.idArea = idArea;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.editable = editable;
    }

    public Area(String idArea, boolean editable, String mailServerTypeSalida) {
        this.idArea = idArea;
        this.editable = editable;
        this.mailServerTypeSalida = mailServerTypeSalida;
    }

    public String getIdArea() {
        return idArea;
    }

    public void setIdArea(String idArea) {
        this.idArea = idArea;
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

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getMailSmtpHost() {
        return mailSmtpHost;
    }

    public void setMailSmtpHost(String mailSmtpHost) {
        this.mailSmtpHost = mailSmtpHost;
    }

    public Short getMailSmtpPort() {
        return mailSmtpPort;
    }

    public void setMailSmtpPort(Short mailSmtpPort) {
        this.mailSmtpPort = mailSmtpPort;
    }

    public String getMailSmtpUser() {
        return mailSmtpUser;
    }

    public void setMailSmtpUser(String mailSmtpUser) {
        this.mailSmtpUser = mailSmtpUser;
    }

    public String getMailSmtpPassword() {
        return mailSmtpPassword;
    }

    public void setMailSmtpPassword(String mailSmtpPassword) {
        this.mailSmtpPassword = mailSmtpPassword;
    }

    public String getMailSmtpFrom() {
        return mailSmtpFrom;
    }

    public void setMailSmtpFrom(String mailSmtpFrom) {
        this.mailSmtpFrom = mailSmtpFrom;
    }

    public String getMailSmtpFromname() {
        return mailSmtpFromname;
    }

    public void setMailSmtpFromname(String mailSmtpFromname) {
        this.mailSmtpFromname = mailSmtpFromname;
    }

    public Boolean getMailSmtpAuth() {
        return mailSmtpAuth;
    }

    public void setMailSmtpAuth(Boolean mailSmtpAuth) {
        this.mailSmtpAuth = mailSmtpAuth;
    }

    public Short getMailSmtpSocketFactoryPort() {
        return mailSmtpSocketFactoryPort;
    }

    public void setMailSmtpSocketFactoryPort(Short mailSmtpSocketFactoryPort) {
        this.mailSmtpSocketFactoryPort = mailSmtpSocketFactoryPort;
    }

    public Integer getMailSmtpConnectiontimeout() {
        return mailSmtpConnectiontimeout;
    }

    public void setMailSmtpConnectiontimeout(Integer mailSmtpConnectiontimeout) {
        this.mailSmtpConnectiontimeout = mailSmtpConnectiontimeout;
    }

    public Integer getMailSmtpTimeout() {
        return mailSmtpTimeout;
    }

    public void setMailSmtpTimeout(Integer mailSmtpTimeout) {
        this.mailSmtpTimeout = mailSmtpTimeout;
    }

    public String getMailServerType() {
        return mailServerType;
    }

    public void setMailServerType(String mailServerType) {
        this.mailServerType = mailServerType;
    }

    public String getMailTransportProtocol() {
        return mailTransportProtocol;
    }

    public void setMailTransportProtocol(String mailTransportProtocol) {
        this.mailTransportProtocol = mailTransportProtocol;
    }

    public String getMailStoreProtocol() {
        return mailStoreProtocol;
    }

    public void setMailStoreProtocol(String mailStoreProtocol) {
        this.mailStoreProtocol = mailStoreProtocol;
    }

    public Boolean getMailTransportTls() {
        return mailTransportTls;
    }

    public void setMailTransportTls(Boolean mailTransportTls) {
        this.mailTransportTls = mailTransportTls;
    }

    public Boolean getMailUseJndi() {
        return mailUseJndi;
    }

    public void setMailUseJndi(Boolean mailUseJndi) {
        this.mailUseJndi = mailUseJndi;
    }

    public String getMailSessionJndiname() {
        return mailSessionJndiname;
    }

    public void setMailSessionJndiname(String mailSessionJndiname) {
        this.mailSessionJndiname = mailSessionJndiname;
    }

    public String getMailInboundHost() {
        return mailInboundHost;
    }

    public void setMailInboundHost(String mailInboundHost) {
        this.mailInboundHost = mailInboundHost;
    }

    public Short getMailInboundPort() {
        return mailInboundPort;
    }

    public void setMailInboundPort(Short mailInboundPort) {
        this.mailInboundPort = mailInboundPort;
    }

    public String getMailInboundUser() {
        return mailInboundUser;
    }

    public void setMailInboundUser(String mailInboundUser) {
        this.mailInboundUser = mailInboundUser;
    }

    public String getMailInboundPassword() {
        return mailInboundPassword;
    }

    public void setMailInboundPassword(String mailInboundPassword) {
        this.mailInboundPassword = mailInboundPassword;
    }

    public Boolean getMailInboundSslEnabled() {
        return mailInboundSslEnabled;
    }

    public void setMailInboundSslEnabled(Boolean mailInboundSslEnabled) {
        this.mailInboundSslEnabled = mailInboundSslEnabled;
    }

    public Boolean getEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(Boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public Integer getEmailFrecuencia() {
        return emailFrecuencia;
    }

    public void setEmailFrecuencia(Integer emailFrecuencia) {
        this.emailFrecuencia = emailFrecuencia;
    }

    public Boolean getEmailAcusederecibo() {
        return emailAcusederecibo;
    }

    public void setEmailAcusederecibo(Boolean emailAcusederecibo) {
        this.emailAcusederecibo = emailAcusederecibo;
    }

    public String getSubjectRespAutomatica() {
        return subjectRespAutomatica;
    }

    public void setSubjectRespAutomatica(String subjectRespAutomatica) {
        this.subjectRespAutomatica = subjectRespAutomatica;
    }

    public String getTextoRespAutomatica() {
        return textoRespAutomatica;
    }

    public void setTextoRespAutomatica(String textoRespAutomatica) {
        this.textoRespAutomatica = textoRespAutomatica;
    }

    public String getTextoRespCaso() {
        return textoRespCaso;
    }

    public void setTextoRespCaso(String textoRespCaso) {
        this.textoRespCaso = textoRespCaso;
    }

    public Boolean getMailDebug() {
        return mailDebug;
    }

    public void setMailDebug(Boolean mailDebug) {
        this.mailDebug = mailDebug;
    }

    public String getDominioExchangeSalida() {
        return dominioExchangeSalida;
    }

    public void setDominioExchangeSalida(String dominioExchangeSalida) {
        this.dominioExchangeSalida = dominioExchangeSalida;
    }

    public String getDominioExchangeInbound() {
        return dominioExchangeInbound;
    }

    public void setDominioExchangeInbound(String dominioExchangeInbound) {
        this.dominioExchangeInbound = dominioExchangeInbound;
    }

    public String getMailServerTypeSalida() {
        return mailServerTypeSalida;
    }

    public void setMailServerTypeSalida(String mailServerTypeSalida) {
        this.mailServerTypeSalida = mailServerTypeSalida;
    }

    public Boolean getMailSmtpSslEnable() {
        return mailSmtpSslEnable;
    }

    public void setMailSmtpSslEnable(Boolean mailSmtpSslEnable) {
        this.mailSmtpSslEnable = mailSmtpSslEnable;
    }

    @XmlTransient
    public List<Grupo> getGrupoList() {
        return grupoList;
    }

    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
    }

    @XmlTransient
    public List<Vista> getVistaList() {
        return vistaList;
    }

    public void setVistaList(List<Vista> vistaList) {
        this.vistaList = vistaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArea != null ? idArea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Area)) {
            return false;
        }
        Area other = (Area) object;
        if ((this.idArea == null && other.idArea != null) || (this.idArea != null && !this.idArea.equals(other.idArea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " (" + idArea + ")";
    }

    /**
     * @return the categoriaList
     */
    public List<Categoria> getCategoriaList() {
        return categoriaList;
    }

    /**
     * @param categoriaList the categoriaList to set
     */
    public void setCategoriaList(List<Categoria> categoriaList) {
        this.categoriaList = categoriaList;
    }

    /**
     * @return the reglaTriggerList
     */
    public List<ReglaTrigger> getReglaTriggerList() {
        return reglaTriggerList;
    }

    /**
     * @param reglaTriggerList the reglaTriggerList to set
     */
    public void setReglaTriggerList(List<ReglaTrigger> reglaTriggerList) {
        this.reglaTriggerList = reglaTriggerList;
    }

    @XmlTransient
    public List<Clipping> getClippingList() {
        return clippingList;
    }

    public void setClippingList(List<Clipping> clippingList) {
        this.clippingList = clippingList;
    }

    /**
     * @return the mailSmtpEncriptionType
     */
    public String getMailSmtpEncriptionType() {
        return mailSmtpEncriptionType;
    }

    /**
     * @param mailSmtpEncriptionType the mailSmtpEncriptionType to set
     */
    public void setMailSmtpEncriptionType(String mailSmtpEncriptionType) {
        this.mailSmtpEncriptionType = mailSmtpEncriptionType;
    }
}
