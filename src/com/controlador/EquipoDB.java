/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlador;

import com.entidades.Equipo;
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
public class EquipoDB {

    private Session st;

    public EquipoDB() {
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
     * Guarda el Equipo Ingresado
     *
     * @param eq
     */
    public void nuevoEquipo(Equipo eq) {
        try {
            st.beginTransaction();
            st.save(eq);
            st.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "EQUIPO GUARDADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR EL EQUIPO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga en la Tabla A los Equipos a travez del Código del Equipo
     *
     * @param list
     * @return
     */
    public List<Equipo> cargarCodigoEquipo(List<Equipo> list) {
        try {
            list = (List<Equipo>) st.createQuery("From Equipo order by idEquipo").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER EL EQUIPO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }

    /**
     * Carga en la Tabla A los Equipos.
     *
     * @param lis
     * @return
     */
    public List<Equipo> cargaEquipos(List<Equipo> lis) {
        try {
            lis = (List<Equipo>) st.createQuery("From Equipo order by cod_equi").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS DATOS DEL EQUIPO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Trae los datos del Equipo a travez del Id
     *
     * @param id
     * @return
     */
    public Equipo traeEquipo(int id) {
        Equipo equip = null;
        try {
            equip = (Equipo) st.load(Equipo.class, id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER LOS DATOS DEL EQUIPO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return equip;
    }

    /**
     * Trae los datos del Equipo de la Tabla a travez del Código
     *
     * @param cod
     * @return
     */
    public Equipo trae_Equipo(String cod) {
        Equipo eq = null;
        try {
            Query query = st.createQuery("from Equipo eq where eq.cod_equi=?");
            query.setParameter(0, cod);
            try {
                eq = (Equipo) query.uniqueResult();
            } catch (Exception e) {
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER LOS DATOS DEL EQUIPO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return eq;
    }

    /**
     * Actualiza el Equipo
     *
     * @param equipo
     */
    public void actualizaEquipo(Equipo equipo) {
        try {
            st.beginTransaction();
            st.update(equipo);
            st.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "EQUIPO ACTUALIZADO", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
            st.clear();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR EL EQUIPO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizaEquipoGarantia(Equipo equipo) {
        try {
            st.clear();
            st.beginTransaction();
            st.update(equipo);
            st.getTransaction().commit();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR EL EQUIPO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Busca al Equipo a travez del tipo
     *
     * @param tipo
     * @param lis
     * @return
     */
    public List<Equipo> buscarEqui(String tipo, List<Equipo> lis) {
        try {
            lis = (List<Equipo>) st.createQuery("From Equipo where cod_equi LIKE '%" + tipo + "%'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS DATOS DEL EQUIPO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Verifica que el numero de serie no sea el mismo
     *
     * @param num_serie
     * @return
     */
    public Equipo traeEquipos(String num_serie) {
        Equipo equi = null;
        try {
            Query query = st.createQuery("from Equipo e Where e.ser_equi = ?");
            query.setParameter(0, num_serie);
            try {
                equi = (Equipo) query.uniqueResult();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "EL NÚMERO DE SERIE DEL EQUIPO YA EXISTE EN EL SISTEMA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER LOS DATOS DEL EQUIPO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return equi;
    }
}
