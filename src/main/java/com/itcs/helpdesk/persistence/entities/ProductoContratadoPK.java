/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jonathan
 */
@Embeddable
public class ProductoContratadoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_cliente")
    private int idCliente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "id_producto")
    private String idProducto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "id_componente")
    private String idComponente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "id_sub_componente")
    private String idSubComponente;

    public ProductoContratadoPK() {
    }

    public ProductoContratadoPK(int idCliente, String idProducto, String idComponente, String idSubComponente) {
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.idComponente = idComponente;
        this.idSubComponente = idSubComponente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getIdComponente() {
        return idComponente;
    }

    public void setIdComponente(String idComponente) {
        this.idComponente = idComponente;
    }

    public String getIdSubComponente() {
        return idSubComponente;
    }

    public void setIdSubComponente(String idSubComponente) {
        this.idSubComponente = idSubComponente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCliente;
        hash += (idProducto != null ? idProducto.hashCode() : 0);
        hash += (idComponente != null ? idComponente.hashCode() : 0);
        hash += (idSubComponente != null ? idSubComponente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoContratadoPK)) {
            return false;
        }
        ProductoContratadoPK other = (ProductoContratadoPK) object;
        if (this.idCliente != other.idCliente) {
            return false;
        }
        if ((this.idProducto == null && other.idProducto != null) || (this.idProducto != null && !this.idProducto.equals(other.idProducto))) {
            return false;
        }
        if ((this.idComponente == null && other.idComponente != null) || (this.idComponente != null && !this.idComponente.equals(other.idComponente))) {
            return false;
        }
        if ((this.idSubComponente == null && other.idSubComponente != null) || (this.idSubComponente != null && !this.idSubComponente.equals(other.idSubComponente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
      
        builder.append(idCliente);
        builder.append(";");
        builder.append(idComponente);
        builder.append(";");
        builder.append(idProducto);
        builder.append(";");
        builder.append(idSubComponente);
        
        return builder.toString();
    }

   
    
}
