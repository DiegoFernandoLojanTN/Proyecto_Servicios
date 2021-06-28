/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entidades;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Usuario
 */
@Entity
public class DetaOrden implements Serializable{
    
    /**
     * Atributos de la entidad DetaOrden
     */
    private int id_detall;
    private String tipo_servi;
    private double prec_ser;
    private Orden orde;
    private Servicio servi;

    /**
     * Id de la Entidad DetaOrden
     * @return 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId_detall() {
        return id_detall;
    }
    
    public void setId_detall(int id_detall) {
        this.id_detall = id_detall;
    }
  
    /**
     * Tipo de Servicio
     * @return 
     */
    public String getTipo_servi() {
        return tipo_servi;
    }
    
    public void setTipo_servi(String tipo_servi) {
        this.tipo_servi = tipo_servi;
    }
    
    /**
     * Precio del servicio
     * @return 
     */
    public double getPrec_ser() {
        return prec_ser;
    }
 
    public void setPrec_ser(double prec_ser) {
        this.prec_ser = prec_ser;
    }
    
    /**
     * Relación entre DetaOrden y Orden
     * Muchos Detalles puede tener una Orden
     * @return 
     */
    @ManyToOne
    public Orden getOrde() {
        return orde;
    }

    public void setOrde(Orden orde) {
        this.orde = orde;
    }

    /**
     * Relación entre DetaOrden y Servicio
     * Muchos Detalles puede tener un Servicio.
     * @return 
     */
   @ManyToOne
    public Servicio getServi() {
        return servi;
    }
 
    public void setServi(Servicio servi) {
        this.servi = servi;
    }
}
