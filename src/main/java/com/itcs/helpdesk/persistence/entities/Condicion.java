/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "condicion")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA, contextProperty="eclipselink.tenant-id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Condicion.findAll", query = "SELECT c FROM Condicion c"),
    @NamedQuery(name = "Condicion.findByIdCondicion", query = "SELECT c FROM Condicion c WHERE c.idCondicion = :idCondicion"),
    @NamedQuery(name = "Condicion.findByValor", query = "SELECT c FROM Condicion c WHERE c.valor = :valor")})
public class Condicion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_condicion")
    private Integer idCondicion;
    @Size(max = 5000)
    @Column(name = "valor", length = 5000)
    private String valor;
    @Size(max = 5000)
    @Column(name = "valor2", length = 5000)
    private String valor2;
    @JoinColumn(name = "ID_COMPARADOR", referencedColumnName = "ID_COMPARADOR")
    @ManyToOne
    private TipoComparacion idComparador;
    @JoinColumn(name = "id_trigger", referencedColumnName = "id_trigger")
    @ManyToOne(optional = false)
    private ReglaTrigger idTrigger;
    @Size(max = 255)
    @Column(name = "id_campo", length = 255)
    private String idCampo;

    public Condicion() {
    }

    public Condicion(Integer idCondicion) {
        this.idCondicion = idCondicion;
    }

    public Integer getIdCondicion() {
        return idCondicion;
    }

    public void setIdCondicion(Integer idCondicion) {
        this.idCondicion = idCondicion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        System.out.println("Condicion valor: " + valor);
        this.valor = valor;
    }

    public TipoComparacion getIdComparador() {
        return idComparador;
    }

    public void setIdComparador(TipoComparacion idComparador) {
        this.idComparador = idComparador;
    }

    public ReglaTrigger getIdTrigger() {
        return idTrigger;
    }

    public void setIdTrigger(ReglaTrigger idTrigger) {
        this.idTrigger = idTrigger;
    }

    public String getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(String idCampo) {
        this.idCampo = idCampo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCondicion != null ? idCondicion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Condicion)) {
            return false;
        }
        Condicion other = (Condicion) object;
        if ((this.idCondicion == null && other.idCondicion != null) || (this.idCondicion != null && !this.idCondicion.equals(other.idCondicion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Condicion [" + "idCampo =" + idCampo + " " + "idComparador =" + idComparador + " " + "valor =" + valor +  "valor2 = " + valor + "]";
    }

    /**
     * @return the valor2
     */
    public String getValor2() {
        return valor2;
    }

    /**
     * @param valor2 the valor2 to set
     */
    public void setValor2(String valor2) {
        this.valor2 = valor2;
    }
    
      /**
     * @return the valoresList
     */
    public List<String> getValoresList() {
        final List<String> result = new ArrayList<String>();
        if (valor != null) {
            for (String value : valor.split(",", -1)) {
                final String trimmedValue = value.trim();
                result.add(trimmedValue);
            }
        }
        return result;
    }

    /**
     * @param valoresList the valoresList to set
     */
    public void setValoresList(List<String> valoresList) {
        valor = "";
        boolean first = true;
        for (String string : valoresList) {
            if (first) {
                first = false;
                valor += string;
            } else {
                valor += "," + string;
            }
        }
    }
}
