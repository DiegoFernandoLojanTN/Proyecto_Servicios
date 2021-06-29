/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlador;

import com.entidades.DetaOrden;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import utilidad.HibernateUtil;

/**
 * Operación de creación, lectura y eliminación para instancias de clases de
 * entidades mapeadas.
 *
 * @author Usuario
 */
public class DetaOrdenDB {

    private Session st;

    public DetaOrdenDB() {
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
     * trae los datos del detalle de la orden a travez del ID
     *
     * @param id
     * @return
     */
    public DetaOrden traeDetalleOrden(int id) {
        DetaOrden Ordendetalle = null;

        try {
            Ordendetalle = (DetaOrden) st.load(DetaOrden.class, id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER LOS DATOS " + e.getMessage());
        }
        return Ordendetalle;
    }

    /**
     * Elimina el registro de la tabla del detalle de la orden
     *
     * @param detalle
     */
    public void eliminaDetalleOrden(DetaOrden detalle) {
        try {
            st.beginTransaction();
            st.delete(detalle);
            st.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL ELIMINAR EL DETALLE EN EL CONTROLADOR " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }
}
