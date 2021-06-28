/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlador;

import com.entidades.Rol;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Query;
import org.hibernate.Session;
import utilidad.HibernateUtil;

/**
 * Operación de creación, lectura y eliminación para instancias de clases de
 * entidades mapeadas.
 *
 * @author Usuario
 */
public class RolDB {

    private Session st;

    public RolDB() {
        sessionHibernate();
    }

    /**
     * el mapeo de atributos entre una base de datos relacional tradicional y el
     * modelo de objetos de una aplicación
     */
    public void sessionHibernate() {
        st = HibernateUtil.getSessionFactory().openSession();
    }

    /**
     * Guardar Rol
     *
     * @param r
     */
    public void nuevoRol(Rol r) {
        try {
            st.beginTransaction();
            st.save(r);
            st.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR EL ROL " + e.getMessage());
        }
    }

    /**
     * Cargar el nombre del rol
     *
     * @param lis
     * @return
     */
    public List<Rol> cargaRol(List<Rol> lis) {
        try {
            lis = (List<Rol>) st.createQuery("from Rol").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER LOS ROLES " + e.getMessage());
        }
        return lis;
    }

    /**
     * Trae los datos del Rol a travez del Id
     *
     * @param id
     * @return
     */
    public Rol traeRol(int id) {
        Rol r = new Rol();
        try {
            r = (Rol) st.load(Rol.class, id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER EL ROL " + e.getMessage());
        }
        return r;
    }

    /**
     * Trae el nombre del rol a travez de la descripción
     *
     * @param des
     * @return
     */
    public Rol traeRol(String des) {
        Rol r = null;
        try {
            Query query = st.createQuery("from Rol ro Where ro.des_rol = ?");
            query.setParameter(0, des);
            try {
                r = (Rol) query.uniqueResult();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "EL ROL YA EXISTE EN EL SISTEMA");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER EL ROL " + e.getMessage());
        }
        return r;
    }

    /**
     * Actualiza los datos del rol
     *
     * @param r
     */
    public void actualizaRol(Rol r) {
        try {
            st.beginTransaction();
            st.update(r);
            st.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR EL ROL " + e.getMessage());
        }
    }
}
