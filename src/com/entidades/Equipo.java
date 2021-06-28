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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Usuario
 */
@Entity
public class Equipo implements Serializable{
    
    /**
     * Atributos de la entidad Equipo
     */
    private Persona persona;
    private Marca marc;
    private int IdEquipo;
    private String cod_equi;
    private String tip_equi;
    private String mod_equi;
    private String ser_equi;
    private String col_equi;
    
     private List<Orden>Ordene = new ArrayList<Orden>();

     
     /**
      * Id del Equipo
      * @return 
      */
     @Id
     @GeneratedValue(strategy=GenerationType.IDENTITY) 
      public int getIdEquipo() {
        return IdEquipo;
    }

    public void setIdEquipo(int IdEquipo) {
        this.IdEquipo = IdEquipo;
    }
    
    /**
     * Código del Equipo
     * @return 
     */
    public String getCod_equi() {
        return cod_equi;
    }

    public void setCod_equi(String cod_equi) {
        this.cod_equi = cod_equi;
    }

    /**
     * Tipo (Categoría) del Equipo
     * @return 
     */
    @Column(length = 20)
    public String getTip_equi() {
        return tip_equi;
    }

    public void setTip_equi(String tip_equi) {
        this.tip_equi = tip_equi;
    }

    /**
     * Modelo del Equipo
     * @return 
     */
   @Column(length = 20)
    public String getMod_equi() {
        return mod_equi;
    }
 
    public void setMod_equi(String mod_equi) {
        this.mod_equi = mod_equi;
    }
 
    /**
     * Número de Serie del Equipo
     * @return 
     */
    @Column(length = 20)
    public String getSer_equi() {
        return ser_equi;
    }
 
    public void setSer_equi(String ser_equi) {
        this.ser_equi = ser_equi;
    }
 
    /**
     * Color del Equipo
     * @return 
     */
    @Column(length = 10)
    public String getCol_equi() {
        return col_equi;
    }
    
    public void setCol_equi(String col_equi) {
        this.col_equi = col_equi;
    }
    
    /**
     * Relación entre Equipo y Persona
     * Muchos Equipos tiene una Persona
     * @return 
     */
    @ManyToOne
    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    /**
     * Relación entre Equipo y Marca
     * Muchos Equipos tiene una Marca
     * @return 
     */
    @ManyToOne
    public Marca getMarc() {
        return marc;
    }
    
    public void setMarc(Marca marc) {
        this.marc = marc;
    }

    /**
     * Relación entre Equipo y Orden
     * Un Equipo puede tener muchas Ordenes
     * @return 
     */
    @OneToMany(mappedBy = "orden",cascade = CascadeType.ALL)
    public List<Orden> getOrdene() {
        return Ordene;
    }
 
    public void setOrdene(List<Orden> Ordene) {
        this.Ordene = Ordene;
    }

}
