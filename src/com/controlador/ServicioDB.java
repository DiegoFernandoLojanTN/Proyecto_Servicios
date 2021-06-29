/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlador;

import com.entidades.Servicio;
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
public class ServicioDB {

    private Session st;

    public ServicioDB() {
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
     * Registra un nuevo Servicio Ingresado
     *
     * @param ser
     */
    public void nuevoServicio(Servicio ser) {
        try {
            st.beginTransaction();
            st.save(ser);
            st.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "SERVICIO GUARDADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR EL SERVICIO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Trae los datos sel servicio a travez del ID
     *
     * @param id
     * @return
     */
    public Servicio traeServicio(int id) {
        Servicio ser = null;
        try {
            ser = (Servicio) st.load(Servicio.class, id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR EL SERVICIO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return ser;
    }

    /**
     * Trae el servicio a travez del Código del Servicio y la descripción
     *
     * @param cod
     * @return
     */
    public Servicio traeServicio(String cod) {
        Servicio ser = null;
        try {
            Query query = st.createQuery("from Servicio se Where se.des_ser = ?");
            query.setParameter(0, cod);
            try {
                ser = (Servicio) query.uniqueResult();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "LA DESCRIPCIÓN DEL SERVICIO YA EXISTE EN EL SISTEMA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER EL SERVICIO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return ser;
    }

    /**
     * Carga el estado del servicio en la Tabla a travez del estado
     *
     * @param est
     * @param lis
     * @return
     */
    public List<Servicio> cargaServicios(String est, List<Servicio> lis) {
        try {
            lis = (List<Servicio>) st.createQuery("From Servicio where est_ser='" + est + "'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "EEROR AL CARGAR LOS SERVICIOS " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Carga los servicios a travez del tipo y el estado
     *
     * @param des
     * @param est
     * @param lis
     * @return
     */
    public List<Servicio> cargaServi(String des, String est, List<Servicio> lis) {
        try {
            lis = (List<Servicio>) st.createQuery("From Servicio where tipo_ser='" + des + "' and est_ser='" + est + "'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS SERVICIOS " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Carga los datos del servicio a travez del Código
     *
     * @param list
     * @return
     */
    public List<Servicio> cargarCodigoServicio(List<Servicio> list) {
        try {
            list = (List<Servicio>) st.createQuery("From Servicio order by cod_ser").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR EL SERVICIO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return list;

    }

    /**
     * Actualiza los datos del Servicio
     *
     * @param ser
     */
    public void actualizaServicio(Servicio ser) {
        try {
            st.beginTransaction();
            st.update(ser);
            st.getTransaction().commit();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR EL SERVICIO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Busca el Servicio a travez de la descripción
     *
     * @param Codigo
     * @param lis
     * @return
     */
    public List<Servicio> buscarServicio(String Codigo, List<Servicio> lis) {
        try {
            lis = (List<Servicio>) st.createQuery("From Servicio where des_ser LIKE '%" + Codigo + "%'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS DATOS DEL SERVICIO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Verifica que la descripcion del servicio no sea la misma
     *
     * @param des
     * @return
     */
    public Servicio traeServicios(String des) {
        Servicio ser = null;
        try {
            Query query = st.createQuery("from Servicio se Where se.des_ser = ?");
            query.setParameter(0, des);
            try {
                ser = (Servicio) query.uniqueResult();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "LA DESCRIPCIÓN DEL SERVICIO YA EXISTE EN EL SISTEMA " + e.getMessage(), "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER LOS DATOS DEL SERVICIO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return ser;
    }
}
