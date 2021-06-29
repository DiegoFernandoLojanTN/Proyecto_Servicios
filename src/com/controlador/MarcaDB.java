/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlador;

import com.entidades.Marca;
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
public class MarcaDB {

    private Session st;

    public MarcaDB() {
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
     * Guarda los datos de la Marca
     *
     * @param m
     */
    public void nuevaMarca(Marca m) {
        try {
            st.beginTransaction();
            st.save(m);
            st.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "MARCA GUARDADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR LA MARCA " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga los datos de la marca en la tabla a tráves del estado
     *
     * @param est
     * @param lis
     * @return
     */
    public List<Marca> cargaMarca(String est, List<Marca> lis) {
        try {
            lis = (List<Marca>) st.createQuery("from Marca where est_mar='" + est + "'order by nom_mar").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER LAS MARCAS " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }

    /**
     * Trae la marca a tráves del ID
     *
     * @param id
     * @return
     */
    public Marca traeMarca(int id) {
        Marca m = null;
        try {
            m = (Marca) st.load(Marca.class, id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER LA MARCA " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return m;
    }

    /**
     * Trae la marca a tráves de la descripción
     *
     * @param des
     * @return
     */
    public Marca traeMarcas(String des) {
        Marca m = null;
        try {
            Query query = st.createQuery("from Marca ma Where ma.nom_mar = ?");
            query.setParameter(0, des);
            try {
                m = (Marca) query.uniqueResult();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "EL NOMBRE DE LA MARCA YA EXISTE EN EL SISTEMA " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LAS MARCAS " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return m;
    }

    /**
     * Actualiza los datos de la marca
     *
     * @param m
     */
    public void actualizaMarca(Marca m) {
        try {
            st.beginTransaction();
            st.update(m);
            st.getTransaction().commit();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR LOS DATOS DE LA MARCA " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Busca la marca a tráves del nombre
     *
     * @param nom_mar
     * @param lis
     * @return
     */
    public List<Marca> buscarMarca(String nom_mar, List<Marca> lis) {
        try {
            lis = (List<Marca>) st.createQuery("From Marca where nom_mar LIKE '%" + nom_mar + "%'").list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL BUSCAR LOS DATOS DE LA MARCA " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return lis;
    }
}
