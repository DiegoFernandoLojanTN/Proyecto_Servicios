/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlador;

import com.entidades.Cuenta;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Query;
import org.hibernate.Session;
import utilidad.HibernateUtil;

/**
 *
 * @author Usuario
 */
public class CuentaDB {

    /**
     * Operación de creación, lectura y eliminación para instancias de clases de
     * entidades mapeadas.
     *
     */
    private Session st;

    public CuentaDB() {
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
     * Guarda el Usuario los datos en la base de datos entidad Cuenta
     *
     * @param cuen
     */
    public void nuevoUsuario(Cuenta cuen) {
        try {
            st.beginTransaction();
            st.save(cuen);
            st.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "USUARIO GUARDADO", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR AL USUARIO " + e.getMessage());
        }
    }

    /**
     * Trae los datos del Usuario a travez del id
     *
     * @param id
     * @return
     */
    public Cuenta traeUsuario(int id) {

        Cuenta u = new Cuenta();
        try {
            u = (Cuenta) st.load(Cuenta.class, id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER AL USUARIO " + e.getMessage());
        }
        return u;
    }

    /**
     * Trae los datos de la cuenta a travez de la cédula
     *
     * @param ced
     * @return
     */
    public Cuenta traeCuentaCed(String ced) {

        Cuenta u = new Cuenta();
        try {
            Query query = st.createQuery("from Cuenta where persona_ced_per=? and est_usu='A'");
            query.setParameter(0, ced);
            try {
                u = (Cuenta) query.uniqueResult();
            } catch (Exception e) {
            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "ERROR AL TRAER AL USUARIO " + e.getMessage(), "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        }
        return u;
    }

    /**
     * Trae a los Usuarios con el estado de activo
     *
     * @param est
     * @param list
     * @return
     */
    public List<Cuenta> traeUsuarios(String est, List<Cuenta> list) {
        try {
            list = (List<Cuenta>) st.createQuery("From Cuenta where est_usu='" + est + "'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER A LOS USUARIOS " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }

    /**
     * Carga los Roles a travez de una lista al combobox
     *
     * @param tec
     * @param lis
     * @return
     */
    public List<Cuenta> CargaUsua(String tec, List<Cuenta> lis) {
        try {
            lis = (List<Cuenta>) st.createQuery("From Cuenta where rol_id_rol=1").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS TÉCNICOS " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Actualiza los datos el Usuario
     *
     * @param c
     */
    public void actualizaCuenta(Cuenta c) {
        try {
            st.clear();
            st.beginTransaction();
            st.update(c);
            st.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER AL USUARIO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Tra los datos del Usuario a travez de la Cédula
     *
     * @param ced
     * @return
     */
    public Cuenta traeUsuarios(String ced) {
        Cuenta cue = null;
        try {
            Query query = st.createQuery("from Cuenta us Where us.persona_ced_per= ?");
            query.setParameter(0, ced);
            try {
                cue = (Cuenta) query.uniqueResult();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS USUARIOS " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return cue;
    }

    /**
     * Busca a la Persona a Travez de la Cédula
     *
     * @param cedula
     * @param lis
     * @return
     */
    public List<Cuenta> buscarPersona(String cedula, List<Cuenta> lis) {
        try {
            lis = (List<Cuenta>) st.createQuery("From Cuenta where persona_ced_per LIKE '%" + cedula + "%'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS DATOS DEL CLIENTE " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Trae a los clientes a travez de la Cedula
     *
     * @param ced
     * @return
     */
    public Cuenta traeClientes(String ced) {
        Cuenta m = null;
        try {
            Query query = st.createQuery("from Cuenta Where persona_ced_per = ?");
            query.setParameter(0, ced);
            try {
                m = (Cuenta) query.uniqueResult();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "EL NÚMERO DE CÉDULA YA EXISTE EN EL SISTEMA " + e.getMessage(), "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
        }
        return m;
    }
}
