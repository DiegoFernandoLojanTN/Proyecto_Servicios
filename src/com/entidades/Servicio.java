/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entidades;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Usuario
 */
@Entity
public class Servicio implements Serializable{
    
    /**
     * Atributos de la entidad Servicio
     */
    private int IdServ;
    private String cod_ser ;
    private String des_ser;
    private Double pre_ser;
    private String est_ser;
    private String tipo_ser;
    
    private List<DetaOrden> servi = new ArrayList<DetaOrden>();
    
    /**
     * Id del Servicio
     * @return 
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    public int getIdServ() {
        return IdServ;
    }
 
    public void setIdServ(int IdServ) {
        this.IdServ = IdServ;
    }
    
     /**
     * Código del Servicio
     * @return 
     */
    public String getCod_ser() {
        return cod_ser;
    }
 
    public void setCod_ser(String cod_ser) {
        this.cod_ser = cod_ser;
    }
    /**
     * Descripción del Servicio
     * @return 
     */
    public String getDes_ser() {
        return des_ser;
    }
 
    public void setDes_ser(String des_ser) {
        this.des_ser = des_ser;
    }
    
    /**
     * Precio del Servicio
     * @return 
     */
    public Double getPre_ser() {
        return pre_ser;
    }
 
    public void setPre_ser(Double pre_ser) {
        this.pre_ser = pre_ser;
    }
 
    /**
     * Estado del Servicio
     * @return 
     */
    @Column(length = 1)
    public String getEst_ser() {
        return est_ser;
    }

    public void setEst_ser(String est_ser) {
        this.est_ser = est_ser;
    }
    
    /**
     * Tipo del Servicio
     * @return 
     */
     public String getTipo_ser() {
        return tipo_ser;
    }

    public void setTipo_ser(String tipo_ser) {
        this.tipo_ser = tipo_ser;
    }

    /**
     * Relación entre Servicio y el Detalle de la Orden
     * Un Servicio puede tener muchos detalles
     * @return 
     */
    @OneToMany(mappedBy = "servi",cascade = CascadeType.ALL)
    public List<DetaOrden> getServi() {
        return servi;
    }
    
    public void setServi(List<DetaOrden> servi) {
        this.servi = servi;
    }  
}
