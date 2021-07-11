/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entidades;
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Usuario
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona implements Serializable {
    
    /**
     * Atributos de la entidad Persona
     */
    private String ced_per;
    private String ape_per;
    private String nom_per;
    private String dir_per;
    private String tel_per;
    private String correo_per;
    private String sex_per;
    private String est_per;
    private Rol rol;
    private List<Equipo>Lista_Equipo = new ArrayList<Equipo>();
    
    /**
     * Id de la persona
     * @return 
     */
    @Id
    @Column(length = 10)
    public String getCed_per() {
        return ced_per;
    }
    
    public void setCed_per(String ced_per) {
        this.ced_per = ced_per;
    }

    /**Column(length = 10)
     * Apellido de la Persona
     * @return 
     */
    @Column(length = 30)
    public String getApe_per() {
        return ape_per;
    }
 
    public void setApe_per(String ape_per) {
        this.ape_per = ape_per;
    }

    /**
     * Nombre de la Persona
     * @return 
     */
    @Column(length = 20)
    public String getNom_per() {
        return nom_per;
    }
 
    public void setNom_per(String nom_per) {
        this.nom_per = nom_per;
    }

    /**
     * Dirección de la Persona
     * @return 
     */
    @Column(length = 50)
    public String getDir_per() {
        return dir_per;
    }
 
    public void setDir_per(String dir_per) {
        this.dir_per = dir_per;
    }

    /**
     * Teléfono de la Persona
     * @return 
     */
    @Column(length = 10)
    public String getTel_per() {
        return tel_per;
    }
 
    public void setTel_per(String tel_per) {
        this.tel_per = tel_per;
    }

    /**
     * Correo de la Persona
     * @return 
     */
    public String getCorreo_per() {
        return correo_per;
    }

    public void setCorreo_per(String correo_per) {
        this.correo_per = correo_per;
    }

    /**
     * Sexo de la Persona
     * @return 
     */
    @Column(length = 1)
    public String getSex_per() {
        return sex_per;
    }
    
    public void setSex_per(String sex_per) {
        this.sex_per = sex_per;
    }
    
    /**
     * Estado de la Persona
     * @return 
     */
    @Column(length = 1)
      public String getEst_per() {
        return est_per;
    }
      
    public void setEst_per(String est_per) {
        this.est_per = est_per;
    }
    
    /**
     * Relación entre Persona y Equipo
     * Una Persona tiene Muchos Equipos
     * @return 
     */
    @OneToMany(mappedBy = "persona",cascade = CascadeType.ALL)
    public List<Equipo> getLista_Equipo() {
        return Lista_Equipo;
    }
    public void setLista_Equipo(List<Equipo> Lista_Equipo) {
        this.Lista_Equipo = Lista_Equipo;
    }

    /**
     * Relación entre Persona y Rol
     * Un Rol tiene Muchas Personas
     * @return 
     */
    @ManyToOne
    public Rol getRol() {
        return rol;
    }
    
    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
