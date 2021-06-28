/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Usuario
 */
@Entity
public class Cuenta implements Serializable{
    
    /**
     * Atributos de la entidad Cuenta
     */
    private int id_cue;
    private Persona persona;
    private String contra_usu;
    private String est_usu;

    /**
     * Id del Usuario
     * @return 
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
     public int getId_cue() {
        return id_cue;
    }
     
    public void setId_cue(int id_cue) {
        this.id_cue = id_cue;
    }
    
    /**
     * Contraseña de Usuario
     * @return 
     */
    @Column(length = 15)
    public String getContra_usu() {
        return contra_usu;
    }

    public void setContra_usu(String contra_usu) {
        this.contra_usu = contra_usu;
    }
 
    /**
     * Estado Usuario
     * @return 
     */
    @Column(length = 1)
    public String getEst_usu() {
        return est_usu;
    }
    
    public void setEst_usu(String est_usu) {
        this.est_usu = est_usu;
    }
    
    /**
     * Relacion entre Persona y Cuenta (uno a uno)
     * @return 
     */
   @OneToOne
    public Persona getPersona() {
        return persona;
    }
 
    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
