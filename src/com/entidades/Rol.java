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

public class Rol implements Serializable{
    
    /**
     * Atributos de la entidad Rol
     * @return 
     */
    private int id_rol;
    private String des_rol;
    private List<Persona> personas= new ArrayList<Persona>();
    
    /**
     * Id del Rol
     * @return 
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    /**
     * Descripción del Rol
     * @return 
     */
     @Column(length=20)
    public String getDes_rol() {
        return des_rol;
    }
 
    public void setDes_rol(String des_rol) {
        this.des_rol = des_rol;
    }

    /**
     * Relacion entre Persona y Rol
     * Un Rol puede tener Muchas Personas
     * @return 
     */
    @OneToMany(mappedBy = "rol",cascade = CascadeType.ALL)
    public List<Persona> getPersonas() {
        return personas;
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
    }
}
