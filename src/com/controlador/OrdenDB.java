/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlador;

import com.entidades.DetaOrden;
import com.entidades.Orden;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import utilidad.HibernateUtil;

/**
 * Operación de creación, lectura y eliminación para instancias de clases de
 * entidades mapeadas.
 *
 * @author Usuario
 */
public class OrdenDB {

    private Session st;

    public OrdenDB() {
        sessionHibernate();
    }

    /**
     * el mapeo de atributos entre una base de datos relacional tradicional y el
     * modelo de objetos de una aplicación
     */
    public void sessionHibernate() {
        st = HibernateUtil.getSessionFactory().openSession();
    }

    public List<Orden> cargarCodigoOrden(List<Orden> list) {
        try {
            list = (List<Orden>) st.createQuery("From Orden order by num_ord").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al traer Equipo " + e.getMessage());
        }
        return list;

    }

    /**
     * Guarda los datos en la Orden
     *
     * @param ord
     */
    public void nuevaOrden(Orden ord) {
        try {
            st.clear();
            st.beginTransaction();
            st.save(ord);
            st.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "ORDEN GUARDADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR LA ORDEN " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Guarda los datos en la Orden
     *
     * @param ord
     */
    public void actualizaOrden(Orden ord) {
        try {
            st.clear();
            st.beginTransaction();
            st.update(ord);
            st.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "ORDEN ACTUALIZADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR LOS DATOS DE LA ORDEN " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Trae los datos de la orden con el estado Pendiente
     *
     * @param est
     * @param lis
     * @return
     */
    public List<Orden> traeOrdenesTabla(String est, List<Orden> lis) {
        try {
            lis = (List<Orden>) st.createQuery("From Orden where est_ord='" + est + "'order by id_orden").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LAS ORDENES" + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Trae los datos de la orden con el estado Pendiente
     *
     * @param lis
     * @return
     */
    public List<Orden> traeorden(List<Orden> lis) {
        try {
            lis = (List<Orden>) st.createQuery("From Orden order by num_ord").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LAS ORDENES" + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Trae lso datos de la orden con el estado Finalizado
     *
     * @param est
     * @param lis
     * @return
     */
    public List<Orden> traeOrdenesFinali(String est, List<Orden> lis) {
        try {
            lis = (List<Orden>) st.createQuery("From Orden where est_ord='" + est + "'order by id_orden").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LAS ORDENES" + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Trae los datos de la Orden a Travez de su ID
     *
     * @param id
     * @return
     */
    public Orden traeOrden(int id) {
        Orden ord = null;
        try {
            ord = (Orden) st.load(Orden.class, id);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER LA ORDEN " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return ord;
    }

    /**
     * Cambia el estado de la orden de Pendiente a Finalizado
     *
     * @param ord
     */
    public void finalizaOrden(Orden ord) {
        try {
            st.clear();
            st.beginTransaction();
            st.update(ord);
            st.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "ORDEN FINALIZADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL FINALIZAR LA ORDEN" + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga los datos del detalle en la Tabla de la orden
     *
     * @param list
     * @param id
     * @return
     */
    public List<DetaOrden> cargaDetalleOrden(List<DetaOrden> list, int id) {
        try {
            list = (List<DetaOrden>) st.createQuery("From DetaOrden where orde_id_orden=" + id).list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER EL DETALLE DE LA ORDEN " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }

    /**
     * Anula la orden y cambia su estado a Anulada
     *
     * @param Ord
     */
    public void anulaOrden(Orden Ord) {
        try {
            st.beginTransaction();
            st.update(Ord);
            st.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "ORDEN ANULADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL ANULAR LA ORDEN " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Busca la orden a travez del número de la orden en estado Pendiente
     *
     * @param numero
     * @param lis
     * @return
     */
    public List<Orden> buscaOrdenPendiente(String numero, List<Orden> lis) {
        try {
            lis = (List<Orden>) st.createQuery("From Orden where num_ord LIKE '%" + numero + "%' and est_ord= 'Pendiente'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS DATOS EN LA TABLA " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Busca la orden a travez del número de la orden
     *
     * @param numero
     * @param lis
     * @return
     */
    public List<Orden> BuscarOrden(String numero, List<Orden> lis) {
        try {
            lis = (List<Orden>) st.createQuery("From Orden where num_ord LIKE '%" + numero + "%'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS DATOS EN LA TABLA " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Busca la orden a travez de la fecha de ingreso de la orden
     *
     * @param Fecha
     * @param f2
     * @param lis
     * @return
     */
    public List<Orden> BuscarOrdenFecha(List<Orden> lis, String Fecha, String f2) {
        try {

            lis = (List<Orden>) st.createQuery("From Orden where fec_ing >='" + Fecha + "'and fec_sal <='" + f2 + "'and est_ord= 'Finalizada'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS DATOS EN LA TABLA " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Busca la orden a travez del número de la orden en estado Pendiente
     *
     * @param numero
     * @param lis
     * @return
     */
    public List<Orden> buscaOrdenFinalizado(String numero, List<Orden> lis) {
        try {
            lis = (List<Orden>) st.createQuery("From Orden where num_ord LIKE '%" + numero + "%' and est_ord= 'Finalizada'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS DATOS EN LA TABLA " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }
}
