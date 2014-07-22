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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Table(name = "EMAIL_CLIENTE")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type = TenantTableDiscriminatorType.SCHEMA, contextProperty = "eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmailCliente.findAll", query = "SELECT e FROM EmailCliente e"),
    @NamedQuery(name = "EmailCliente.findByEmailCliente", query = "SELECT e FROM EmailCliente e WHERE e.emailCliente = :emailCliente")})
public class EmailCliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Pattern(regexp = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?", message = "Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(min = 1, max = 80)
    @Column(name = "EMAIL_CLIENTE")
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Email Cliente", fieldIdFull = "emailCliente", fieldTypeFull = String.class)
    private String emailCliente;
    @OneToMany(mappedBy = "emailCliente", fetch = FetchType.EAGER)
    private List<Caso> casoList;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID_CLIENTE")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Cliente cliente;
    /*
     * Tipos:
     Trabajo
     Casa
     Movil
     Otro
     * TODO: hacer enumerado con los posibles valores.
     */
    @Basic(optional = false)
    @Size(min = 1, max = 80)
    @Column(name = "tipo_email")
    private String tipoEmail;

    public EmailCliente() {
    }

    public EmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente.toLowerCase();
    }

    @XmlTransient
    public List<Caso> getCasoList() {
        return casoList;
    }

    public void setCasoList(List<Caso> casoList) {
        this.casoList = casoList;
    }

    public Cliente getCliente() {
        if (cliente == null) {
            cliente = new Cliente();
        }
        return cliente;
    }

    public void setCliente(Cliente idCliente) {
        this.cliente = idCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (emailCliente != null ? emailCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmailCliente)) {
            return false;
        }
        EmailCliente other = (EmailCliente) object;
        if ((this.emailCliente == null && other.emailCliente != null) || (this.emailCliente != null && !this.emailCliente.equals(other.emailCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return emailCliente;
    }

    /**
     * @return the tipoEmail
     */
    public String getTipoEmail() {
        return tipoEmail;
    }

    /**
     * @param tipoEmail the tipoEmail to set
     */
    public void setTipoEmail(String tipoEmail) {
        this.tipoEmail = tipoEmail;
    }

    @Transient
    public String getNombreOEmailCliente() {
        if (this.getCliente() != null) {
            if (this.getCliente().getNombres() != null && !this.getCliente().getNombres().isEmpty()) {
                return this.getCliente().getCapitalName();
            } else {
                return this.getEmailCliente();
            }
        }
        return "unknown client email";
    }
}
