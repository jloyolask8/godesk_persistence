/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
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
@Table(name = "cliente")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findByIdCliente", query = "SELECT c FROM Cliente c WHERE c.idCliente = :idCliente"),
    @NamedQuery(name = "Cliente.findByRut", query = "SELECT c FROM Cliente c WHERE c.rut = :rut"),
    @NamedQuery(name = "Cliente.findByNombres", query = "SELECT c FROM Cliente c WHERE c.nombres = :nombres"),
    @NamedQuery(name = "Cliente.findByApellidos", query = "SELECT c FROM Cliente c WHERE c.apellidos = :apellidos")
})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE")
    private Integer idCliente;
    @Size(max = 40)
    private String rut;
    @Size(max = 80)
    private String nombres;
    @Size(max = 80)
    private String apellidos;
    @Size(max = 40)
    private String sexo;
    @Size(max = 40)
    private String fono1;
    @Size(max = 40)
    private String fono2;
    @Size(max = 400)
    @Column(name = "DIR_PARTICULAR")
    private String dirParticular;
    @Size(max = 400)
    @Column(name = "DIR_COMERCIAL")
    private String dirComercial;
    @OneToMany(mappedBy = "cliente", fetch= FetchType.EAGER)
    private List<EmailCliente> emailClienteList;
    private String theme;
    @OneToMany(cascade = CascadeType.ALL, fetch= FetchType.LAZY, mappedBy = "cliente")
    private List<ProductoContratado> productoContratadoList;

    public Cliente() {
    }

    public Cliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
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

    public String getFono1() {
        return fono1;
    }

    public void setFono1(String fono1) {
        this.fono1 = fono1;
    }

    public String getFono2() {
        return fono2;
    }

    public void setFono2(String fono2) {
        this.fono2 = fono2;
    }

    public String getDirParticular() {
        return dirParticular;
    }

    public void setDirParticular(String dirParticular) {
        this.dirParticular = dirParticular;
    }

    public String getDirComercial() {
        return dirComercial;
    }

    public void setDirComercial(String dirComercial) {
        this.dirComercial = dirComercial;
    }

    public List<EmailCliente> getEmailClienteList() {
        return emailClienteList;
    }

    public void setEmailClienteList(List<EmailCliente> emailClienteList) {
        this.emailClienteList = emailClienteList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.rut != null ? this.rut.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cliente other = (Cliente) obj;
        if ((this.rut == null) ? (other.rut != null) : !this.rut.equals(other.rut)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Cliente [");
        builder.append("idCliente=").append(idCliente);
        builder.append(", rut=").append(rut);
        builder.append(", nombres=").append(nombres);
        builder.append(", apellidos=").append(apellidos);
        builder.append(", dirComercial=").append(dirComercial);
        builder.append(", dirParticular=").append(dirParticular);
        builder.append(", sexo=").append(sexo);
        builder.append("]");
        return builder.toString();
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
     * @return the sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @XmlTransient
    public List<ProductoContratado> getProductoContratadoList() {
        return productoContratadoList;
    }

    public void setProductoContratadoList(List<ProductoContratado> productoContratadoList) {
        this.productoContratadoList = productoContratadoList;
    }

    public String getCapitalName() {
        String capitalName = "";
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
        return capitalName;
    }
}
