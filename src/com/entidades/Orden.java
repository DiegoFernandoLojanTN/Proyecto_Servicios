/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Usuario
 */
@Entity
public class Orden implements Serializable{
    
    /**
     * Atributos de la entidad Orden
     */
    private int Id_orden;
    private String num_ord;
    private Calendar fec_ing;
    private Calendar fec_sal;
    private String est_ord;
    private String accesorios;
    private String encargado;
    private String falla;
    private Double total;
    private String observacion;
    
    private List<DetaOrden>orde = new ArrayList<DetaOrden>();
    private Equipo orden;
    
    /**
     * Id de la Orden
     * @return 
     */
    @Id
    public int getId_orden() {
        return Id_orden;
    }
 
    public void setId_orden(int Id_orden) {
        this.Id_orden = Id_orden;
    }
    
    /**
     * Número de la Orden
     * @return 
     */
    public String getNum_ord() {
        return num_ord;
    }
    
    public void setNum_ord(String num_ord) {
        this.num_ord = num_ord;
    }

    /**
     * Fecha de Ingreso
     * @return 
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getFec_ing() {
        return fec_ing;
    }
 
    public void setFec_ing(Calendar fec_ing) {
        this.fec_ing = fec_ing;
    }

    /**
     * Fecha de Salida
     * @return 
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getFec_sal() {
        return fec_sal;
    }
 
    public void setFec_sal(Calendar fec_sal) {
        this.fec_sal = fec_sal;
    }

  /**
   * Estado de la Orden
   * @return 
   */
    public String getEst_ord() {
        return est_ord;
    }
    
    public void setEst_ord(String est_ord) {
        this.est_ord = est_ord;
    }
    
    /**
     * Observación Orden 
     * @return 
     */
    public String getObservacion() {
        return observacion;
    }
    
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    
    /**
     * Total de la Orden
     * @return 
     */
    public Double getTotal() {
        return total;
    }
 
    public void setTotal(Double total) {
        this.total = total;
    }
    
    /**
     * Accesorios Orden
     * @return 
     */
    public String getAccesorios() {
        return accesorios;
    }
 
    public void setAccesorios(String accesorios) {
        this.accesorios = accesorios;
    }
 
    /**
     * Persona encargada de la Orden
     * @return 
     */
    public String getEncargado() {
        return encargado;
    }
 
    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }
 
    /**
     * Falla
     * @return 
     */
    public String getFalla() {
        return falla;
    }
 
    public void setFalla(String falla) {
        this.falla = falla;
    }
   
    /**
     * Relación entre Orden y el Detalle de la Orden (DetaOrden)
     * @return 
     */
    @OneToMany(mappedBy = "orde",cascade = CascadeType.ALL)
    public List<DetaOrden> getOrde() {
        return orde;
    }

    public void setOrde(List<DetaOrden> orde) {
        this.orde = orde;
    }

    @ManyToOne
    public Equipo getOrden() {
        return orden;
    }
    public void setOrden(Equipo orden) {
        this.orden = orden;
    }
}
