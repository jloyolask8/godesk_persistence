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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
@Table(name = "usuario")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type = TenantTableDiscriminatorType.SCHEMA, contextProperty = "eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByIdUsuario", query = "SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario"),
    @NamedQuery(name = "Usuario.findByNombres", query = "SELECT u FROM Usuario u WHERE u.nombres = :nombres"),
    @NamedQuery(name = "Usuario.findByApellidos", query = "SELECT u FROM Usuario u WHERE u.apellidos = :apellidos"),
    @NamedQuery(name = "Usuario.findByEmail", query = "SELECT u FROM Usuario u WHERE u.email = :email"),
    @NamedQuery(name = "Usuario.findByTelFijo", query = "SELECT u FROM Usuario u WHERE u.telFijo = :telFijo"),
    @NamedQuery(name = "Usuario.findByTelMovil", query = "SELECT u FROM Usuario u WHERE u.telMovil = :telMovil"),
    @NamedQuery(name = "Usuario.findByActivo", query = "SELECT u FROM Usuario u WHERE u.activo = :activo"),
    @NamedQuery(name = "Usuario.findByPass", query = "SELECT u FROM Usuario u WHERE u.pass = :pass"),
    @NamedQuery(name = "Usuario.findByRut", query = "SELECT u FROM Usuario u WHERE u.rut = :rut")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "id_usuario")
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Nombre de Usuario", fieldIdFull = "idUsuario", fieldTypeFull = String.class)
    private String idUsuario;
    @Size(max = 40)
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Nombres", fieldIdFull = "nombres", fieldTypeFull = String.class)
    private String nombres;
    @Size(max = 40)
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Apellidos", fieldIdFull = "apellidos", fieldTypeFull = String.class)
    private String apellidos;
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 40)
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Email", fieldIdFull = "email", fieldTypeFull = String.class)
    private String email;
    @Size(max = 40)
    @Column(name = "tel_fijo")
    private String telFijo;
    @Size(max = 40)
    @Column(name = "tel_movil")
    private String telMovil;
    @Basic(optional = false)
    @NotNull
    @FilterField(fieldTypeId = EnumFieldType.CHECKBOX, label = "Activo", fieldIdFull = "activo", fieldTypeFull = Boolean.class)
    private boolean activo;
    @Basic(optional = false)
    @NotNull
    private boolean editable;
    @Size(max = 32)
    private String pass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 14)
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Rut", fieldIdFull = "rut", fieldTypeFull = String.class)
    private String rut;

    @OneToMany(mappedBy = "owner")
    private List<Caso> casoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy")
    private List<Documento> documentoList;
    @OneToMany(mappedBy = "supervisor")
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Supervisor de", fieldIdFull = "usuarioList", fieldTypeFull = List.class, listGenericTypeFieldId = "idUsuario")
    private List<Usuario> usuarioList;
    @JoinColumn(name = "supervisor", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario supervisor;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creadaPor")
    private List<Nota> notaList;
    //---
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Rol(es)", fieldIdFull = "rolList", fieldTypeFull = List.class, listGenericTypeFieldId = "idRol")
    @ManyToMany
    @JoinTable(name = "USER_ROL", joinColumns = {
        @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", table = "usuario")}, inverseJoinColumns = {
        @JoinColumn(name = "id_rol", referencedColumnName = "id_rol", table = "rol")})
    private List<Rol> rolList;
    //---
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Grupo(s)", fieldIdFull = "grupoList", fieldTypeFull = List.class, listGenericTypeFieldId = "idGrupo")
    @ManyToMany(mappedBy = "usuarioList", cascade = CascadeType.ALL)
    private List<Grupo> grupoList;
    //--
    @Column(name = "theme")
    @Size(max = 40)
    private String theme;
    @Column(name = "template_theme")
    @Size(max = 40)
    private String templateTheme;
     @Column(name = "main_menu_right")
    private Boolean mainMenuRight;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioCreadaPor")
    private List<Vista> vistaList;
    @Column(name = "page_layout_state")
    @Size(max = 2147483647)
    private String pageLayoutState;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioCreadaPor")
    private List<Clipping> clippingList;

    //owner, created by me events
    @OneToMany(mappedBy = "idUsuario")
    private List<ScheduleEvent> scheduleEventList;
    
    //invited or asociated events, not necessesarily created by me
//      @JoinTable(name = "schedule_event_usuario", joinColumns = {
//        @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")}, inverseJoinColumns = {
//        @JoinColumn(name = "event_id", referencedColumnName = "event_id")})
//    @ManyToMany
    @ManyToMany(mappedBy = "usuariosInvitedList")
    private List<ScheduleEvent> scheduleEventInvitedList;
      
    //notifications
    @Column(name = "notify_when_ticket_assigned")
    private Boolean notifyWhenTicketAssigned;
    @Column(name = "notify_when_new_ticket_in_group")
    private Boolean notifyWhenNewTicketInGroup;
    @Column(name = "notify_when_ticket_alert")
    private Boolean notifyWhenTicketAlert;
    @Column(name = "notify_when_ticket_is_updated")
    private Boolean notifyWhenTicketIsUpdated;
    @Column(name = "email_notifications_enabled")
    private Boolean emailNotificationsEnabled;

    public Usuario() {
    }

    public Usuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(String idUsuario, String nombres, String apellidos) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public Usuario(String idUsuario, boolean activo, String pass, String rut, String email, boolean editable, String nombres, String apellidos) {
        this.idUsuario = idUsuario;
        this.activo = activo;
        this.pass = pass;
        this.rut = rut;
        this.email = email;
        this.editable = editable;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public Usuario(String idUsuario, boolean activo, String rut) {
        this.idUsuario = idUsuario;
        this.activo = activo;
        this.rut = rut;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelFijo() {
        return telFijo;
    }

    public void setTelFijo(String telFijo) {
        this.telFijo = telFijo;
    }

    public String getTelMovil() {
        return telMovil;
    }

    public void setTelMovil(String telMovil) {
        this.telMovil = telMovil;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public List<Rol> getRolList() {
        return rolList;
    }

    public void setRolList(List<Rol> rolList) {
        this.rolList = rolList;
    }

    public List<Caso> getCasoList() {
        return casoList;
    }

    public void setCasoList(List<Caso> casoList) {
        this.casoList = casoList;
    }

    @XmlTransient
    public List<Documento> getDocumentoList() {
        return documentoList;
    }

    public void setDocumentoList(List<Documento> documentoList) {
        this.documentoList = documentoList;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public Usuario getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Usuario supervisor) {
        this.supervisor = supervisor;
    }

    @XmlTransient
    public List<Nota> getNotaList() {
        return notaList;
    }

    public void setNotaList(List<Nota> notaList) {
        this.notaList = notaList;
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
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    public String getCapitalName() {
        String capitalName = "";
        try {
            String[] names = new String[]{nombres == null ? "" : nombres.split(" ")[0], apellidos == null ? "" : apellidos.split(" ")[0]};
            for (int i = 0; i < names.length; i++) {
                if (!(names[i].trim().isEmpty())) {
                    if (i > 0) {
                        capitalName += " ";
                    }
                    if (!names[i].isEmpty()) {
                        capitalName += names[i].substring(0, 1).toUpperCase() + names[i].substring(1).toLowerCase();
                    }
                }
            }
        } catch (Exception e) {
            capitalName = nombres + " " + apellidos;
        }
        return capitalName;
    }

    @Override
    public String toString() {
        //return "cl.cnsv.referidos.persistence.entities.Usuario[ idUsuario=" + idUsuario + " ]";
        if (((nombres == null) || (nombres.isEmpty())) && ((apellidos == null) || (apellidos.isEmpty()))) {
            return idUsuario;
        }
        return nombres + " " + apellidos + "(" + idUsuario + ")";
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
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * @param theme the theme to set
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * @return the grupoList
     */
    public List<Grupo> getGrupoList() {
        return grupoList;
    }

    /**
     * @param grupoList the grupoList to set
     */
    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
    }

    /**
     * @return the pageLayoutState
     */
    public String getPageLayoutState() {
        return pageLayoutState;
    }

    /**
     * @param pageLayoutState the pageLayoutState to set
     */
    public void setPageLayoutState(String pageLayoutState) {
        this.pageLayoutState = pageLayoutState;
        System.out.println("setPageLayoutState:" + pageLayoutState);
    }

    /**
     * @return the clippingList
     */
    public List<Clipping> getClippingList() {
        return clippingList;
    }

    /**
     * @param clippingList the clippingList to set
     */
    public void setClippingList(List<Clipping> clippingList) {
        this.clippingList = clippingList;
    }

    public List<ScheduleEvent> getScheduleEventList() {
        return scheduleEventList;
    }

    public void setScheduleEventList(List<ScheduleEvent> scheduleEventList) {
        this.scheduleEventList = scheduleEventList;
    }

    /**
     * @return the notifyWhenTicketAssigned
     */
    public Boolean getNotifyWhenTicketAssigned() {
        return notifyWhenTicketAssigned;
    }

    /**
     * @param notifyWhenTicketAssigned the notifyWhenTicketAssigned to set
     */
    public void setNotifyWhenTicketAssigned(Boolean notifyWhenTicketAssigned) {
        this.notifyWhenTicketAssigned = notifyWhenTicketAssigned;
    }

    /**
     * @return the notifyWhenNewTicketInGroup
     */
    public Boolean getNotifyWhenNewTicketInGroup() {
        return notifyWhenNewTicketInGroup;
    }

    /**
     * @param notifyWhenNewTicketInGroup the notifyWhenNewTicketInGroup to set
     */
    public void setNotifyWhenNewTicketInGroup(Boolean notifyWhenNewTicketInGroup) {
        this.notifyWhenNewTicketInGroup = notifyWhenNewTicketInGroup;
    }

    /**
     * @return the notifyWhenTicketAlert
     */
    public Boolean getNotifyWhenTicketAlert() {
        return notifyWhenTicketAlert;
    }

    /**
     * @param notifyWhenTicketAlert the notifyWhenTicketAlert to set
     */
    public void setNotifyWhenTicketAlert(Boolean notifyWhenTicketAlert) {
        this.notifyWhenTicketAlert = notifyWhenTicketAlert;
    }

    /**
     * @return the notifyWhenTicketIsUpdated
     */
    public Boolean getNotifyWhenTicketIsUpdated() {
        return notifyWhenTicketIsUpdated;
    }

    /**
     * @param notifyWhenTicketIsUpdated the notifyWhenTicketIsUpdated to set
     */
    public void setNotifyWhenTicketIsUpdated(Boolean notifyWhenTicketIsUpdated) {
        this.notifyWhenTicketIsUpdated = notifyWhenTicketIsUpdated;
    }

    /**
     * @return the emailNotificationsEnabled
     */
    public Boolean getEmailNotificationsEnabled() {
        return emailNotificationsEnabled;
    }

    /**
     * @param emailNotificationsEnabled the emailNotificationsEnabled to set
     */
    public void setEmailNotificationsEnabled(Boolean emailNotificationsEnabled) {
        this.emailNotificationsEnabled = emailNotificationsEnabled;
    }

    /**
     * @return the templateTheme
     */
    public String getTemplateTheme() {
        return templateTheme;
    }

    /**
     * @param templateTheme the templateTheme to set
     */
    public void setTemplateTheme(String templateTheme) {
        this.templateTheme = templateTheme;
    }

    /**
     * @return the mainMenuRight
     */
    public Boolean getMainMenuRight() {
        return mainMenuRight;
    }

    /**
     * @param mainMenuRight the mainMenuRight to set
     */
    public void setMainMenuRight(Boolean mainMenuRight) {
        this.mainMenuRight = mainMenuRight;
    }

    /**
     * @return the scheduleEventInvitedList
     */
    public List<ScheduleEvent> getScheduleEventInvitedList() {
        return scheduleEventInvitedList;
    }

    /**
     * @param scheduleEventInvitedList the scheduleEventInvitedList to set
     */
    public void setScheduleEventInvitedList(List<ScheduleEvent> scheduleEventInvitedList) {
        this.scheduleEventInvitedList = scheduleEventInvitedList;
    }
}
