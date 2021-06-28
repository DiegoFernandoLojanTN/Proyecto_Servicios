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
public class Marca implements Serializable{
    
    /**
     * Atributos de la entidad Marca
     */
    private int id_mar;
    private String nom_mar;
    private String est_mar;
    private List<Equipo> Lista_Equipo = new ArrayList<Equipo>();

    /**
     * Id de la Marca
     * @return 
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    public int getId_mar() {
        return id_mar;
    }

    public void setId_mar(int id_mar) {
        this.id_mar = id_mar;
    }

    /**
     * Nombre de la Marca
     * @return 
     */
    @Column(length = 10)
    public String getNom_mar() {
        return nom_mar;
    }
 
    public void setNom_mar(String nom_mar) {
        this.nom_mar = nom_mar;
    }

    /**
     * Estado de la Marca
     * @return 
     */
    @Column(length = 1)
    public String getEst_mar() {
        return est_mar;
    }

    public void setEst_mar(String est_mar) {
        this.est_mar = est_mar;
    }
    
    /**
     * Relación entre Marca y Equipo
     * Una Marca puede tener muchos Equipos
     * @return 
     */
    @OneToMany(mappedBy = "marc", cascade = CascadeType.ALL)
    public List<Equipo> getLista_Equipo() {
        return Lista_Equipo;
    }

    public void setLista_Equipo(List<Equipo> Lista_Equipo) {
        this.Lista_Equipo = Lista_Equipo;
    } 
}
