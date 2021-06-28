/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlador;

import com.entidades.Persona;
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
public class PersonaDB {

    private Session st;

    public PersonaDB() {
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
     * Registra un Nuevo Cliente Ingresado
     *
     * @param cli
     */
    public void nuevoCliente(Persona cli) {
        try {
            st.beginTransaction();
            st.save(cli);
            st.getTransaction().commit();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR EL CLIENTE " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga los datos del cliente en la Tabla a travez de su Estado
     *
     * @param est
     * @param lis
     * @return
     */
    public List<Persona> cargaClientes(String est, List<Persona> lis) {
        try {
            lis = (List<Persona>) st.createQuery("From Persona where est_per='" + est + "'order by ape_per").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR CLIENTES " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Busca y trae los datos de la persona con el rol que sea 1 y su estado sea
     * Activo
     *
     * @param cliente
     * @param est
     * @param lis
     * @return
     */
    public List<Persona> traeCliente(String cliente, String est, List<Persona> lis) {

        try {
            lis = (List<Persona>) st.createQuery("From Persona where rol_id_rol='" + 1 + "'and est_per='" + est + "' order by ape_per").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR TÉCNICOS " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Trae los datos del cliente a travez de la Cédula
     *
     * @param ced
     * @return
     */
    public Persona traeCliente(String ced) {
        Persona per = null;
        try {
            per = (Persona) st.load(Persona.class, ced);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER AL CLIENTE " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return per;
    }

    /**
     * Actualiza los datos del cliente
     *
     * @param cli
     */
    public void actualizaCliente(Persona cli) {
        try {
            st.clear();
            st.beginTransaction();
            st.saveOrUpdate(cli);
            st.getTransaction().commit();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR LOS DATOS " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Busca a la Persona a Travez de la Cédula
     *
     * @param cedula
     * @param lis
     * @return
     */
    public List<Persona> buscarPersona(String cedula, List<Persona> lis) {
        try {
            lis = (List<Persona>) st.createQuery("From Persona where ced_per LIKE '%" + cedula + "%'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL BUSCAR EL CLIENTE " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Cambia el Estado del cliente de activo a Pasivo
     *
     * @param cli
     */
    public void desactivaCliente(Persona cli) {
        try {
            st.beginTransaction();
            st.update(cli);
            st.getTransaction().commit();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL DESACTIVAR CLIENTE " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Trae el Nombre del Tecnico a travez del Rol y del Estado
     *
     * @param tec
     * @param est
     * @param lis
     * @return
     */
    public List<Persona> traeTecnico(String tec, String est, List<Persona> lis) {

        try {
            lis = (List<Persona>) st.createQuery("From Persona where rol_id_rol='" + 3 + "'and est_per='" + est + "'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER A LOS TÉCNICOS " + e.getMessage());
        }
        return lis;
    }

    /**
     * Trae a los clientes a travez de la Cedula
     *
     * @param ced
     * @return
     */
    public Persona traeClientes(String ced) {
        Persona m = null;
        try {
            Query query = st.createQuery("from Persona pe Where pe.ced_per = ?");
            query.setParameter(0, ced);
            try {
                m = (Persona) query.uniqueResult();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "EL NÚMERO DE CÉDULA YA EXISTE EN EL SISTEMA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER LOS CLIENTES " + e.getMessage());
        }
        return m;
    }
}
