/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app;

import com.controlador.CuentaDB;
import com.controlador.DetaOrdenDB;
import com.controlador.EquipoDB;
import com.controlador.OrdenDB;
import com.controlador.PersonaDB;
import com.controlador.ServicioDB;
import com.controlador.Validaciones;
import com.entidades.DetaOrden;
import com.entidades.Equipo;
import com.entidades.Orden;
import com.entidades.Persona;
import com.entidades.Servicio;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import modelo.Conexion;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Usuario
 */
public class frmOrden extends javax.swing.JDialog {

    int numOrden = 0;
    int indice = 0;
    OrdenDB ordDB = new OrdenDB();
    ServicioDB servDB = new ServicioDB();
    EquipoDB equiDB = new EquipoDB();
    Validaciones vali = new Validaciones();
    CuentaDB usuDB = new CuentaDB();
    PersonaDB perDB = new PersonaDB();
    DetaOrdenDB detaDB = new DetaOrdenDB();
    Vector vectorDetalleId;

    DefaultTableModel modelo1 = new DefaultTableModel();
    DefaultTableModel modelo2 = new DefaultTableModel();
    DefaultTableModel modelo3 = new DefaultTableModel();

    DefaultTableModel modeloEquipo = new DefaultTableModel();
    DefaultTableModel modelEquipo;
    Calendar fechaActual = new GregorianCalendar();

    /**
     * Creates new form frmOrden
     *
     * @param parent
     * @param modal
     */
    public frmOrden(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        inicio();
    }

    /**
     * Inicio de la Aplicación
     */
    private void inicio() {

        txtCod_Orden.setText(vali.ObtenerCodString(obtenerCodigoOrden()));
        jdcFechaIngreso.setCalendar(fechaActual);
        btnReportePendientes.setVisible(false);
        tablaModel2();
        llenaTablaOrdenP("Pendiente");
        llenaTablaOrdenF("Finalizada");
        tablaModelEquipo();
        llenaTablaEqui();

        visibles_Bloqueados();
        indice = 0;
        jTabbedPane1.setSelectedIndex(indice);
        jTabbedPane1.setTitleAt(1, "Nueva Orden");

        bloquear(false);
        cboDesServicio.removeAllItems();
        LimpiarCampos(true);
    }

    /**
     * Carga el nombre del Técnico en la lista desplegable
     *
     * @param tecn
     * @param est
     */
    private void llenaUsuario(String tecn, String est) {
        cboTecnico.removeAllItems();
        List<Persona> lisper = null;
        lisper = perDB.traeTecnico(tecn, est, lisper);

        if (lisper.size() > 0) {
            for (Persona per : lisper) {
                cboTecnico.addItem(per.getNom_per() + " " + per.getApe_per());
            }
        } else {
            JOptionPane.showMessageDialog(this, "NO EXISTEN TÉCNICOS REGISTRADOS EN EL SISTEMA", "Mensaje", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Limpia los campos de las cajas de textos
     *
     * @param flag
     */
    private void LimpiarCampos(boolean flag) {
        if (flag == true) {
            txtId_Serv.setText(null);
            txtIdEquipo.setText(null);

            txtTotal.setText("0.00");
            txtBuscaCod.setText(null);
            txtCodigo.setText(null);
            txtColor1.setText(null);
            txtTipo1.setText(null);
            txtCliente1.setText(null);
            txtNum_Ser1.setText(null);
            jdcFechaEntrega.setCalendar(null);
            jdcFechaIngreso.setCalendar(null);
            buttonGroup4.clearSelection();
            txtFalla.setText(null);
            txtObser_Previa.setText(null);
            txtAccesorios.setText(null);
            DefaultComboBoxModel modeloc = new DefaultComboBoxModel();
            modeloc.addElement("Seleccione");
            txtCosto.setText("0.00");
            txtModel1.setText(null);
            txtMarca1.setText(null);
            cboDesServicio.setModel(modeloc);
            lblTipoMensaje.setVisible(false);
            chkAnuladas.setSelected(false);

            tablaModel2();
        }
    }

    private void bloquear(boolean s) {
        if (s == true) {
            txtBuscaCod.setEnabled(false);
            btnBuscar.setEnabled(false);
            txtCod_Orden.setEnabled(false);
            jdcFechaEntrega.setEnabled(false);
            jdcFechaIngreso.setEnabled(false);
            txtFalla.setEnabled(false);
            chkMantenimiento.setEnabled(false);
            chkReparac.setEnabled(false);
            cboDesServicio.setEnabled(false);
            btnAgrega.setEnabled(false);
            btnQuitar.setEnabled(false);

        } else {
            txtBuscaCod.setEnabled(true);
            btnBuscar.setEnabled(true);
            txtCod_Orden.setEnabled(true);
            jdcFechaEntrega.setEnabled(true);
            jdcFechaIngreso.setEnabled(true);
            txtFalla.setEnabled(true);
            chkMantenimiento.setEnabled(true);
            chkReparac.setEnabled(true);
            btnAgrega.setEnabled(true);
            btnQuitar.setEnabled(true);
        }
    }

    /**
     * Trae los datos guardados de la base de datos y me los carga en la Tabla
     * para visualizar
     */
    public void llenaTablaEqui() {
        List<Equipo> lista = null;
        lista = equiDB.cargaEquipos(lista);
        for (Equipo equipoList : lista) {
            modeloEquipo.addRow(new Object[]{
                equipoList.getIdEquipo(), equipoList.getCod_equi(), equipoList.getMod_equi(),
                equipoList.getSer_equi(), equipoList.getTip_equi(), equipoList.getCol_equi(),
                equipoList.getMarc().getNom_mar(),
                equipoList.getPersona().getNom_per() + " " + equipoList.getPersona().getApe_per()
            });
        }
    }

    /**
     * Ocualpa y presenta las cajas de texto Bloquea funciones en los botones
     */
    private void visibles_Bloqueados() {
        txtId_Serv.setVisible(false);
        btnAnularOrden.setEnabled(false);
        btnModifica.setEnabled(false);
    }

    /**
     * Visualiza el dato con un tamaño establecido de los datos en las filas y
     * columnas de la tabla
     */
    public void tablaModel2() {
        Tabla3.getColumnModel().getColumn(0).setPreferredWidth(20);
        Tabla3.getColumnModel().getColumn(1).setMinWidth(0);
        Tabla3.getColumnModel().getColumn(1).setMaxWidth(0);
        Tabla3.getColumnModel().getColumn(1).setPreferredWidth(0);
        Tabla3.getColumnModel().getColumn(2).setPreferredWidth(70);
        Tabla3.getColumnModel().getColumn(3).setPreferredWidth(350);
        Tabla3.getColumnModel().getColumn(4).setPreferredWidth(30);
        modelo2 = (DefaultTableModel) Tabla3.getModel();
        modelo2.setNumRows(0);
    }

    public void tablaModelEquipo() {

        TablaEquipo.getColumnModel().getColumn(0).setMaxWidth(0);
        TablaEquipo.getColumnModel().getColumn(0).setMinWidth(0);
        TablaEquipo.getColumnModel().getColumn(0).setPreferredWidth(0);
        TablaEquipo.getColumnModel().getColumn(1).setPreferredWidth(130);
        TablaEquipo.getColumnModel().getColumn(2).setPreferredWidth(220);
        TablaEquipo.getColumnModel().getColumn(3).setPreferredWidth(210);
        TablaEquipo.getColumnModel().getColumn(4).setPreferredWidth(180);
        TablaEquipo.getColumnModel().getColumn(5).setPreferredWidth(200);
        TablaEquipo.getColumnModel().getColumn(6).setPreferredWidth(200);
        TablaEquipo.getColumnModel().getColumn(7).setPreferredWidth(400);
        modeloEquipo = (DefaultTableModel) TablaEquipo.getModel();
        modeloEquipo.setNumRows(0);
    }

    /**
     * Carga las ordenes en la Tabla con el estado Finalizado
     *
     * @param est
     */
    private void llenaTablaOrdenF(String est) {
        tablaModel3();
        List<Orden> listaOrd = null;
        listaOrd = ordDB.traeOrdenesFinali(est, listaOrd);

        for (Orden ord : listaOrd) {
            modelo3.addRow(new Object[]{
                ord.getId_orden(), ord.getNum_ord(), ord.getOrden().getTip_equi(),
                ord.getOrden().getPersona().getApe_per() + ", " + ord.getOrden().getPersona().getNom_per(),
                retornaFechaString(ord.getFec_ing(), 0), retornaFechaString(ord.getFec_sal(), 0),
                ord.getEst_ord(), ord.getTotal()
            });
        }
    }

    /**
     * Visualiza el dato con un tamaño establecido de los datos en las filas y
     * columnas de la tabla
     */
    public void tablaModel3() {

        Tabla2.getColumnModel().getColumn(0).setMaxWidth(0);
        Tabla2.getColumnModel().getColumn(0).setMinWidth(0);
        Tabla2.getColumnModel().getColumn(0).setPreferredWidth(0);
        Tabla2.getColumnModel().getColumn(1).setPreferredWidth(15);
        Tabla2.getColumnModel().getColumn(2).setPreferredWidth(35);
        Tabla2.getColumnModel().getColumn(3).setPreferredWidth(200);
        Tabla2.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tabla2.getColumnModel().getColumn(5).setPreferredWidth(40);
        Tabla2.getColumnModel().getColumn(6).setPreferredWidth(42);
        Tabla2.getColumnModel().getColumn(7).setPreferredWidth(5);
        modelo3 = (DefaultTableModel) Tabla2.getModel();
        modelo3.setNumRows(0);
    }

    /**
     * Obtener el código automatico
     *
     * @param c
     * @return
     */
    public String ObtenerCodString(int c) {
        String cdd = "000001";
        if (c < 10) {
            cdd = "00000" + c;
        } else if (c < 100) {
            cdd = "0000" + c;
        } else if (c < 1000) {
            cdd = "000" + c;
        } else if (c < 10000) {
            cdd = "00" + c;
        } else if (c < 100000) {
            cdd = "0" + c;
        } else {
            cdd = "" + c;
        }
        return cdd;
    }

    int num_resp = 0;

    public int obtenerCodigoOrden() {
        num_resp = 0;
        List<Orden> lista = null;
        lista = ordDB.cargarCodigoOrden(lista);
        for (Iterator<Orden> it = lista.iterator(); it.hasNext();) {
            Orden ord = it.next();
            num_resp = Integer.parseInt(ord.getNum_ord()) + 1;
        }
        if (num_resp == 0) {
            num_resp = 1;
        }
        return num_resp;
    }

    /**
     * Carga las ordenes en la Tabla con el estado de Pendiente
     *
     * @param est
     */
    private void llenaTablaOrdenP(String est) {
        tablaModel1();
        List<Orden> listaOrd = null;
        listaOrd = ordDB.traeOrdenesTabla(est, listaOrd);

        for (Orden ord : listaOrd) {
            modelo1.addRow(new Object[]{
                ord.getId_orden(), ord.getNum_ord(), ord.getOrden().getTip_equi(),
                ord.getOrden().getPersona().getApe_per() + ", " + ord.getOrden().getPersona().getNom_per(),
                retornaFechaString(ord.getFec_ing(), 0), retornaFechaString(ord.getFec_sal(), 0),
                ord.getEst_ord(), ord.getTotal()
            });
        }
    }

    /**
     * Nueva retorna el formato de las fechas
     *
     * @param fecha
     * @param dias
     * @return
     */
    private String retornaFechaString(Calendar fecha, int dias) {
        String retorno = null;
        fecha.add(Calendar.DAY_OF_YEAR, dias);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (fecha != null) {
            retorno = sdf.format(fecha.getTime());
        }
        return retorno;
    }

    /**
     * Visualiza el dato con un tamaño establecido de los datos en las filas y
     * columnas de la tabla
     */
    public void tablaModel1() {
        Tabla1.getColumnModel().getColumn(0).setMaxWidth(0);
        Tabla1.getColumnModel().getColumn(0).setMinWidth(0);
        Tabla1.getColumnModel().getColumn(0).setPreferredWidth(0);
        Tabla1.getColumnModel().getColumn(1).setPreferredWidth(15);
        Tabla1.getColumnModel().getColumn(2).setPreferredWidth(35);
        Tabla1.getColumnModel().getColumn(3).setPreferredWidth(200);
        Tabla1.getColumnModel().getColumn(4).setPreferredWidth(40);
        Tabla1.getColumnModel().getColumn(5).setPreferredWidth(40);
        Tabla1.getColumnModel().getColumn(6).setPreferredWidth(42);
        Tabla1.getColumnModel().getColumn(7).setPreferredWidth(5);
        modelo1 = (DefaultTableModel) Tabla1.getModel();
        modelo1.setNumRows(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frmEquipoCod = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        btnCancel = new rojerusan.RSMaterialButtonRound();
        btnSalor = new rojerusan.RSMaterialButtonRound();
        btnAcep = new rojerusan.RSMaterialButtonRound();
        jScrollPane7 = new javax.swing.JScrollPane();
        TablaEquipo = new rojerusan.RSTableMetro();
        txtBuscaE = new javax.swing.JTextField();
        btnBuscaCod = new rojerusan.RSMaterialButtonRound();
        jLabel25 = new javax.swing.JLabel();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtBuscaOrden = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnbuscaorden = new rojerusan.RSMaterialButtonRound();
        txtBuscaOrden1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnbuscaorden1 = new rojerusan.RSMaterialButtonRound();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla1 = new rojerusan.RSTableMetro();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tabla2 = new rojerusan.RSTableMetro();
        btnNueva = new rojerusan.RSMaterialButtonRound();
        btnModifica = new rojerusan.RSMaterialButtonRound();
        btnAnularOrden = new rojerusan.RSMaterialButtonRound();
        btnCancela = new rojerusan.RSMaterialButtonRound();
        chkAnuladas = new javax.swing.JCheckBox();
        btnReportePendientes = new rojerusan.RSMaterialButtonRound();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtBuscaCod = new javax.swing.JTextField();
        btnBuscar = new rojerusan.RSMaterialButtonRound();
        jPanel3 = new javax.swing.JPanel();
        txtCodigo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtModel1 = new javax.swing.JTextField();
        txtNum_Ser1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtColor1 = new javax.swing.JTextField();
        txtTipo1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtMarca1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtCliente1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jdcFechaIngreso = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        jdcFechaEntrega = new com.toedter.calendar.JDateChooser();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtFalla = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtObser_Previa = new javax.swing.JTextArea();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtAccesorios = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        chkMantenimiento = new javax.swing.JCheckBox();
        chkReparac = new javax.swing.JCheckBox();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        cboDesServicio = new javax.swing.JComboBox<>();
        cboTecnico = new javax.swing.JComboBox<>();
        txtCosto = new javax.swing.JTextField();
        btnQuitar = new rojerusan.RSMaterialButtonRound();
        btnAgrega = new rojerusan.RSMaterialButtonRound();
        jScrollPane6 = new javax.swing.JScrollPane();
        Tabla3 = new rojerusan.RSTableMetro();
        jLabel3 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btnGuardar = new rojerusan.RSMaterialButtonRound();
        jButton9 = new rojerusan.RSMaterialButtonRound();
        jButton10 = new rojerusan.RSMaterialButtonRound();
        txtCod_Orden = new javax.swing.JTextField();
        txtId_Serv = new javax.swing.JLabel();
        txtIdEquipo = new javax.swing.JLabel();
        txtIdOrden = new javax.swing.JLabel();
        lblTipoMensaje = new javax.swing.JLabel();

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        btnCancel.setBackground(new java.awt.Color(179, 21, 12));
        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSalor.setBackground(new java.awt.Color(179, 21, 12));
        btnSalor.setText("salir");
        btnSalor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalorActionPerformed(evt);
            }
        });

        btnAcep.setBackground(new java.awt.Color(179, 21, 12));
        btnAcep.setText("Aceptar");
        btnAcep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcepActionPerformed(evt);
            }
        });

        TablaEquipo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CODIGO", "MODELO", "NRO.SERIE", "CATEGORIA", "COLOR", "MARCA", "CLIENTE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaEquipo.setColorBackgoundHead(new java.awt.Color(0, 193, 235));
        TablaEquipo.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        TablaEquipo.setColorBordeHead(new java.awt.Color(255, 255, 255));
        TablaEquipo.setColorForegroundHead(new java.awt.Color(0, 0, 0));
        TablaEquipo.setColorSelBackgound(new java.awt.Color(0, 193, 235));
        TablaEquipo.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        TablaEquipo.setFuenteFilas(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        TablaEquipo.setFuenteFilasSelect(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        TablaEquipo.setFuenteHead(new java.awt.Font("Copperplate Gothic Bold", 2, 12)); // NOI18N
        TablaEquipo.setRowHeight(20);
        TablaEquipo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaEquipoMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(TablaEquipo);

        txtBuscaE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscaEKeyTyped(evt);
            }
        });

        btnBuscaCod.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscaCod.setText("BUSCAR");
        btnBuscaCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscaCodActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 3, 20)); // NOI18N
        jLabel25.setText("CODIGO EQUIPO:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(148, Short.MAX_VALUE)
                .addComponent(jLabel25)
                .addGap(24, 24, 24)
                .addComponent(txtBuscaE, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBuscaCod, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(btnAcep, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153)
                .addComponent(btnSalor, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscaE, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscaCod, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(btnSalor, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAcep, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout frmEquipoCodLayout = new javax.swing.GroupLayout(frmEquipoCod.getContentPane());
        frmEquipoCod.getContentPane().setLayout(frmEquipoCodLayout);
        frmEquipoCodLayout.setHorizontalGroup(
            frmEquipoCodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        frmEquipoCodLayout.setVerticalGroup(
            frmEquipoCodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Bahnschrift", 3, 42)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(179, 21, 12));
        jLabel4.setText("ORDENES");

        jLabel5.setFont(new java.awt.Font("Bahnschrift", 3, 28)); // NOI18N
        jLabel5.setText("Numero De Orden");

        jLabel6.setFont(new java.awt.Font("Bahnschrift", 3, 34)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(179, 21, 12));
        jLabel6.setText("ORDENES FINALIZADAS");

        txtBuscaOrden.setBackground(new java.awt.Color(255, 206, 206));
        txtBuscaOrden.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        txtBuscaOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscaOrdenActionPerformed(evt);
            }
        });
        txtBuscaOrden.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscaOrdenKeyTyped(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/lupa_Mesa de trabajo 1 copia 2.png"))); // NOI18N

        btnbuscaorden.setBackground(new java.awt.Color(179, 21, 12));
        btnbuscaorden.setText("BUSCAR");
        btnbuscaorden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscaordenActionPerformed(evt);
            }
        });

        txtBuscaOrden1.setBackground(new java.awt.Color(255, 206, 206));
        txtBuscaOrden1.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        txtBuscaOrden1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscaOrden1ActionPerformed(evt);
            }
        });
        txtBuscaOrden1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscaOrden1KeyTyped(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/lupa_Mesa de trabajo 1 copia 2.png"))); // NOI18N

        btnbuscaorden1.setBackground(new java.awt.Color(179, 21, 12));
        btnbuscaorden1.setText("BUSCAR");
        btnbuscaorden1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscaorden1ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Bahnschrift", 3, 34)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(179, 21, 12));
        jLabel7.setText("ORDENES PENDIENTES");

        jLabel8.setFont(new java.awt.Font("Bahnschrift", 3, 28)); // NOI18N
        jLabel8.setText("Numero De Orden");

        Tabla1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NRO ORDEN", "EQUIPO", "NOMBRE CLIENTE", "FECHA INGRESO", "FECHA ENTREGA", "ESTADO ORDEN", "TOTAL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabla1.setColorBackgoundHead(new java.awt.Color(0, 193, 235));
        Tabla1.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        Tabla1.setColorBordeHead(new java.awt.Color(255, 255, 255));
        Tabla1.setColorFilasBackgound2(new java.awt.Color(203, 203, 203));
        Tabla1.setColorForegroundHead(new java.awt.Color(51, 51, 51));
        Tabla1.setColorSelBackgound(new java.awt.Color(0, 193, 235));
        Tabla1.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        Tabla1.setFuenteFilas(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        Tabla1.setFuenteFilasSelect(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        Tabla1.setFuenteHead(new java.awt.Font("Copperplate Gothic Bold", 2, 12)); // NOI18N
        Tabla1.setGrosorBordeFilas(0);
        Tabla1.setGrosorBordeHead(0);
        Tabla1.setMultipleSeleccion(false);
        Tabla1.setRowHeight(20);
        Tabla1.getTableHeader().setReorderingAllowed(false);
        Tabla1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Tabla1MousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(Tabla1);

        Tabla2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NRO ORDEN", "EQUIPO", "NOMBRE CLIENTE", "FECHA INGRESO", "FECHA ENTREGA", "ESTADO ORDEN", "TOTAL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabla2.setColorBackgoundHead(new java.awt.Color(0, 193, 235));
        Tabla2.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        Tabla2.setColorBordeHead(new java.awt.Color(255, 255, 255));
        Tabla2.setColorFilasBackgound2(new java.awt.Color(203, 203, 203));
        Tabla2.setColorForegroundHead(new java.awt.Color(51, 51, 51));
        Tabla2.setColorSelBackgound(new java.awt.Color(0, 193, 235));
        Tabla2.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        Tabla2.setFuenteFilas(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        Tabla2.setFuenteFilasSelect(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        Tabla2.setFuenteHead(new java.awt.Font("Copperplate Gothic Bold", 2, 12)); // NOI18N
        Tabla2.setGrosorBordeFilas(0);
        Tabla2.setGrosorBordeHead(0);
        Tabla2.setMultipleSeleccion(false);
        Tabla2.setRowHeight(20);
        Tabla2.getTableHeader().setReorderingAllowed(false);
        Tabla2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(Tabla2);

        btnNueva.setBackground(new java.awt.Color(179, 21, 12));
        btnNueva.setText("NUEVA ORDEN");
        btnNueva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaActionPerformed(evt);
            }
        });

        btnModifica.setBackground(new java.awt.Color(179, 21, 12));
        btnModifica.setText("MODIFICAR ORDEN");
        btnModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificaActionPerformed(evt);
            }
        });

        btnAnularOrden.setBackground(new java.awt.Color(179, 21, 12));
        btnAnularOrden.setText("ANULAR ORDEN");
        btnAnularOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnularOrdenActionPerformed(evt);
            }
        });

        btnCancela.setBackground(new java.awt.Color(179, 21, 12));
        btnCancela.setText("CANCELAR");
        btnCancela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelaActionPerformed(evt);
            }
        });

        chkAnuladas.setBackground(new java.awt.Color(255, 255, 255));
        chkAnuladas.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        chkAnuladas.setText("ANULADOS");
        chkAnuladas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAnuladasActionPerformed(evt);
            }
        });

        btnReportePendientes.setBackground(new java.awt.Color(179, 21, 12));
        btnReportePendientes.setText("Ver Reporte");
        btnReportePendientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportePendientesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(346, 346, 346))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnNueva, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(113, 113, 113)
                        .addComponent(btnModifica, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(119, 119, 119)
                        .addComponent(btnAnularOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87)
                        .addComponent(btnCancela, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 955, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(btnReportePendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtBuscaOrden1, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30)
                                        .addComponent(btnbuscaorden1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 193, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(txtBuscaOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(34, 34, 34)
                                                .addComponent(btnbuscaorden, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(chkAnuladas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(117, 117, 117))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscaorden, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtBuscaOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 32, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReportePendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkAnuladas))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscaOrden1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnbuscaorden1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNueva, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModifica, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnularOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancela, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("VER ORDEN", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Bahnschrift", 3, 20)); // NOI18N
        jLabel9.setText("Cdo Equipo");

        txtBuscaCod.setBackground(new java.awt.Color(255, 206, 206));
        txtBuscaCod.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtBuscaCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscaCodActionPerformed(evt);
            }
        });
        txtBuscaCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscaCodKeyTyped(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Informacion Equipo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 18))); // NOI18N

        txtCodigo.setBackground(new java.awt.Color(255, 206, 206));
        txtCodigo.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel11.setText("Modelo:");

        txtModel1.setEditable(false);
        txtModel1.setBackground(new java.awt.Color(255, 206, 206));
        txtModel1.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtModel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModel1ActionPerformed(evt);
            }
        });
        txtModel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtModel1KeyTyped(evt);
            }
        });

        txtNum_Ser1.setBackground(new java.awt.Color(255, 206, 206));
        txtNum_Ser1.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtNum_Ser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNum_Ser1ActionPerformed(evt);
            }
        });
        txtNum_Ser1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNum_Ser1KeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel12.setText("Nro Serie:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel13.setText("Color:");

        txtColor1.setBackground(new java.awt.Color(255, 206, 206));
        txtColor1.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtColor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColor1ActionPerformed(evt);
            }
        });
        txtColor1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtColor1KeyTyped(evt);
            }
        });

        txtTipo1.setBackground(new java.awt.Color(255, 206, 206));
        txtTipo1.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtTipo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTipo1ActionPerformed(evt);
            }
        });
        txtTipo1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTipo1KeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel14.setText("Categoria:");

        txtMarca1.setBackground(new java.awt.Color(255, 206, 206));
        txtMarca1.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtMarca1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMarca1ActionPerformed(evt);
            }
        });
        txtMarca1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMarca1KeyTyped(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel15.setText("Marca:");

        txtCliente1.setBackground(new java.awt.Color(255, 206, 206));
        txtCliente1.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtCliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliente1ActionPerformed(evt);
            }
        });
        txtCliente1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCliente1KeyTyped(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel16.setText("Cliente:");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel17.setText("Cdo Equipo:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMarca1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTipo1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtColor1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNum_Ser1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtModel1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtModel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNum_Ser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtColor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTipo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMarca1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Informacion Orden", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 18))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel10.setText("Fecha Ingreso:");

        jdcFechaIngreso.setBackground(new java.awt.Color(255, 206, 206));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel18.setText("Fecha Entrega:");

        jdcFechaEntrega.setBackground(new java.awt.Color(255, 206, 206));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel19.setText("Observacion Previa:");

        txtFalla.setBackground(new java.awt.Color(255, 206, 206));
        txtFalla.setColumns(20);
        txtFalla.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtFalla.setRows(5);
        jScrollPane1.setViewportView(txtFalla);

        jLabel20.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel20.setText("Problema/Falla:");

        txtObser_Previa.setBackground(new java.awt.Color(255, 206, 206));
        txtObser_Previa.setColumns(20);
        txtObser_Previa.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtObser_Previa.setRows(5);
        jScrollPane4.setViewportView(txtObser_Previa);

        jLabel21.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel21.setText("Accesorios:");

        txtAccesorios.setBackground(new java.awt.Color(255, 206, 206));
        txtAccesorios.setColumns(20);
        txtAccesorios.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtAccesorios.setRows(5);
        jScrollPane5.setViewportView(txtAccesorios);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Servicio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 12))); // NOI18N

        chkMantenimiento.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup4.add(chkMantenimiento);
        chkMantenimiento.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        chkMantenimiento.setText("Mantenimiento");
        chkMantenimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMantenimientoActionPerformed(evt);
            }
        });

        chkReparac.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup4.add(chkReparac);
        chkReparac.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        chkReparac.setText("Reparacion");
        chkReparac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkReparacActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel22.setText("Accesorios:");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel23.setText("Costo:");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel24.setText("Tecnico:");

        cboDesServicio.setBackground(new java.awt.Color(255, 206, 206));
        cboDesServicio.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        cboDesServicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione" }));
        cboDesServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDesServicioActionPerformed(evt);
            }
        });

        cboTecnico.setBackground(new java.awt.Color(255, 206, 206));
        cboTecnico.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N

        txtCosto.setEditable(false);
        txtCosto.setBackground(new java.awt.Color(255, 206, 206));
        txtCosto.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtCosto.setText("0.00");
        txtCosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCostoActionPerformed(evt);
            }
        });
        txtCosto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(chkMantenimiento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkReparac, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboDesServicio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboTecnico, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(12, 12, 12))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkMantenimiento)
                    .addComponent(chkReparac))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(cboDesServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(cboTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdcFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdcFechaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jdcFechaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jdcFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnQuitar.setBackground(new java.awt.Color(0, 0, 0));
        btnQuitar.setText("QUITAR ITEM");
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });

        btnAgrega.setBackground(new java.awt.Color(0, 0, 0));
        btnAgrega.setText("AGREGAR");
        btnAgrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregaActionPerformed(evt);
            }
        });

        Tabla3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NRO ORDEN", "ID SERVICIO", "TIPO SERVICIO", "DESCRIPCION SERVICIO", "TOTAL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabla3.setColorBackgoundHead(new java.awt.Color(0, 193, 235));
        Tabla3.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        Tabla3.setColorBordeHead(new java.awt.Color(255, 255, 255));
        Tabla3.setColorFilasBackgound2(new java.awt.Color(203, 203, 203));
        Tabla3.setColorForegroundHead(new java.awt.Color(51, 51, 51));
        Tabla3.setColorSelBackgound(new java.awt.Color(0, 193, 235));
        Tabla3.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        Tabla3.setFuenteFilas(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        Tabla3.setFuenteFilasSelect(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        Tabla3.setFuenteHead(new java.awt.Font("Copperplate Gothic Bold", 2, 12)); // NOI18N
        Tabla3.setGrosorBordeFilas(0);
        Tabla3.setGrosorBordeHead(0);
        Tabla3.setMultipleSeleccion(false);
        Tabla3.setRowHeight(20);
        Tabla3.getTableHeader().setReorderingAllowed(false);
        Tabla3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla3MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(Tabla3);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        jLabel3.setText("TOTAL:");

        txtTotal.setBackground(new java.awt.Color(102, 255, 51));
        txtTotal.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtTotal.setForeground(new java.awt.Color(204, 0, 51));
        txtTotal.setText("0.00");
        txtTotal.setEnabled(false);
        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });
        txtTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTotalKeyTyped(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(179, 21, 12));
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(179, 21, 12));
        jButton9.setText("CANCELAR");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(179, 21, 12));
        jButton10.setText("SALIR");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        txtCod_Orden.setBackground(new java.awt.Color(255, 206, 206));
        txtCod_Orden.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtCod_Orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCod_OrdenActionPerformed(evt);
            }
        });
        txtCod_Orden.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCod_OrdenKeyTyped(evt);
            }
        });

        txtId_Serv.setText("ID SE");

        txtIdEquipo.setText("ID EQ");

        txtIdOrden.setText("txtIdOrden");

        lblTipoMensaje.setText("Tipo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtBuscaCod, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(lblTipoMensaje)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(txtIdEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtId_Serv, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(103, 103, 103)
                                        .addComponent(txtIdOrden)
                                        .addGap(165, 165, 165)
                                        .addComponent(txtCod_Orden, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1371, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btnAgrega, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(56, 56, 56)
                                .addComponent(btnQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55))))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(179, 179, 179)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(167, 167, 167)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtBuscaCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCod_Orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtIdOrden)
                        .addComponent(txtIdEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtId_Serv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTipoMensaje))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgrega, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("NUEVA ORDEN", jPanel2);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1400, 860));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscaOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscaOrdenActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtBuscaOrdenActionPerformed

    private void txtBuscaOrdenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaOrdenKeyTyped
        // TODO add your handling code here:
        vali.valNum(evt, txtBuscaOrden, 6);
    }//GEN-LAST:event_txtBuscaOrdenKeyTyped

    private void btnbuscaordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscaordenActionPerformed
        // TODO add your handling code here:

        if (txtBuscaOrden.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "LLENAR CAMPO REQUERIDO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            buscaOrdenPendiente(txtBuscaOrden.getText());
            chkAnuladas.setSelected(false);
        }
    }//GEN-LAST:event_btnbuscaordenActionPerformed

    /**
     * Busca la orden a travez del número en estado Pendiente
     *
     * @param numero
     */
    private void buscaOrdenPendiente(String numero) {
        tablaModel1();
        List<Orden> listaOrd = null;
        listaOrd = ordDB.buscaOrdenPendiente(numero, listaOrd);

        if (listaOrd.size() > 0) {
            for (Orden ord : listaOrd) {
                modelo1.addRow(new Object[]{
                    ord.getId_orden(), ord.getNum_ord(), ord.getOrden().getTip_equi(),
                    ord.getOrden().getPersona().getApe_per() + ", " + ord.getOrden().getPersona().getNom_per(),
                    retornaFechaString(ord.getFec_ing(), 0),
                    retornaFechaString(ord.getFec_sal(), 0),
                    ord.getEst_ord(), ord.getTotal()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "ORDEN NO ENCONTRADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscaOrden.requestFocus();
            llenaTablaOrdenP("Pendiente");
        }
    }


    private void txtBuscaOrden1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscaOrden1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscaOrden1ActionPerformed

    private void txtBuscaOrden1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaOrden1KeyTyped
        // TODO add your handling code here:
        vali.valNum(evt, txtBuscaOrden1, 6);
    }//GEN-LAST:event_txtBuscaOrden1KeyTyped

    private void btnbuscaorden1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscaorden1ActionPerformed
        // TODO add your handling code here:
        if (txtBuscaOrden1.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "LLENAR CAMPO REQUERIDO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            buscaOrdenFinalizado(txtBuscaOrden1.getText());
            chkAnuladas.setSelected(false);
        }
    }//GEN-LAST:event_btnbuscaorden1ActionPerformed

    /**
     * Busca la orden a travez del número en estado Finalizado
     *
     * @param numero
     */
    private void buscaOrdenFinalizado(String numero) {
        tablaModel3();
        List<Orden> listaOrd = null;
        listaOrd = ordDB.buscaOrdenFinalizado(numero, listaOrd);

        if (listaOrd.size() > 0) {
            for (Orden ord : listaOrd) {
                modelo3.addRow(new Object[]{
                    ord.getId_orden(), ord.getNum_ord(), ord.getOrden().getTip_equi(),
                    ord.getOrden().getPersona().getApe_per() + ", " + ord.getOrden().getPersona().getNom_per(),
                    retornaFechaString(ord.getFec_ing(), 0),
                    retornaFechaString(ord.getFec_sal(), 0), ord.getEst_ord(),
                    ord.getTotal()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "ORDEN NO ENCONTRADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscaOrden1.requestFocus();
            llenaTablaOrdenF("Finalizada");
        }

    }


    private void Tabla1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla1MouseClicked
        // TODO add your handling code here:
        if (chkAnuladas.isSelected() == true) {
            if (evt.getClickCount() == 1) {
                btnAnularOrden.setEnabled(false);
                btnModifica.setEnabled(false);
                btnReportePendientes.setVisible(true);
            }
        } else {
            btnAnularOrden.setEnabled(true);
            btnModifica.setEnabled(true);
            btnGuardar.setEnabled(true);
            btnReportePendientes.setVisible(true);
            btnNueva.setEnabled(false);
        }
    }//GEN-LAST:event_Tabla1MouseClicked

    private void Tabla2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Tabla2MouseClicked

    private void btnNuevaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaActionPerformed
        // TODO add your handling code here:
        nuevaOrden();

    }//GEN-LAST:event_btnNuevaActionPerformed

    private void nuevaOrden() {
        indice = 1;
        jTabbedPane1.setSelectedIndex(indice);
        txtCod_Orden.setEnabled(false);
        btnGuardar.setText("Guardar");
        bloquear(false);
        txtCod_Orden.setEnabled(false);
        llenaUsuario("Técnico", "A");
        txtBuscaCod.requestFocus();
        txtIdOrden.setText(String.valueOf(obtenerId()));
    }

    /**
     * Obtiene el Id de la Orden
     *
     * @return
     */
    private int obtenerId() {
        numOrden = 0;

        List<Orden> lista = null;
        lista = ordDB.traeorden(lista);
        for (Iterator<Orden> it = lista.iterator(); it.hasNext();) {
            Orden orde = it.next();
            numOrden = orde.getId_orden() + 1;
        }
        if (numOrden == 0) {
            numOrden = 1;
        }
        return numOrden;

    }

    private void btnModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificaActionPerformed
        // TODO add your handling code here:

        if (Tabla1.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "SELECCIONE UNA ORDEN DE LA TABLA PENDIENTES", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {

            indice = 1;
            jTabbedPane1.setSelectedIndex(indice);
            jTabbedPane1.setTitleAt(1, "Modificar Orden");
            txtCod_Orden.setEnabled(false);
            btnBuscar.setEnabled(false);
            txtBuscaCod.setEnabled(false);
            txtFalla.setEnabled(true);
            txtObser_Previa.setEnabled(true);
            txtAccesorios.setEnabled(true);
            jdcFechaEntrega.setEnabled(true);
            btnGuardar.setText("Actualizar Orden");

            traeOrdenDetalle();
            llenaUsuario("Técnico", "A");
        }
    }//GEN-LAST:event_btnModificaActionPerformed

    /**
     * Carga los datos de la orden seleccionada
     */
    private void traeOrdenDetalle() {
        vectorDetalleId = new Vector();

        int selectRow = Tabla1.getSelectedRow();
        int idOrden = Integer.parseInt(modelo1.getValueAt(selectRow, 0).toString());
        Orden ord = ordDB.traeOrden(idOrden);

        txtIdOrden.setText(String.valueOf(ord.getId_orden()));
        txtBuscaCod.setText(ord.getOrden().getCod_equi());
        txtCod_Orden.setText(ord.getNum_ord());
        txtIdEquipo.setText(String.valueOf(ord.getOrden().getIdEquipo()));
        cboTecnico.setSelectedItem(ord.getEncargado());
        txtCodigo.setText(ord.getOrden().getCod_equi());
        txtModel1.setText(ord.getOrden().getMod_equi());
        txtNum_Ser1.setText(ord.getOrden().getSer_equi());
        txtColor1.setText(ord.getOrden().getCol_equi());
        txtTipo1.setText(ord.getOrden().getTip_equi());
        txtMarca1.setText(ord.getOrden().getMarc().getNom_mar());
        txtCliente1.setText(ord.getOrden().getPersona().getNom_per() + ", " + ord.getOrden().getPersona().getApe_per());
        txtObser_Previa.setText(ord.getObservacion());
        txtFalla.setText(ord.getFalla());
        txtAccesorios.setText(ord.getAccesorios());

        jdcFechaIngreso.setCalendar(ord.getFec_ing());
        jdcFechaEntrega.setCalendar(ord.getFec_sal());
        txtObser_Previa.setText(ord.getObservacion());
        txtTotal.setText(String.valueOf(ord.getTotal()));

        List<DetaOrden> listaD = null;
        listaD = ordDB.cargaDetalleOrden(listaD, ord.getId_orden());
        modelo2.setNumRows(0);

        for (DetaOrden detaList : listaD) {
            modelo2.addRow(new Object[]{
                detaList.getOrde().getNum_ord(), detaList.getServi().getIdServ(), detaList.getServi().getTipo_ser(),
                detaList.getServi().getDes_ser(), detaList.getServi().getPre_ser()
            });
            vectorDetalleId.add(detaList.getId_detall());
        }
    }


    private void btnAnularOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnularOrdenActionPerformed
        // TODO add your handling code here:
        if (Tabla1.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "SELECCIONE UNA ORDEN DE LA TABLA PENDIENTES", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int si = JOptionPane.showConfirmDialog(this, "ESTÁ SEGURO DE ANULAR LA ORDEN", "Anular Orden", JOptionPane.YES_NO_OPTION);
            if (si == JOptionPane.YES_OPTION) {
                int selectRow = Tabla1.getSelectedRow();
                int idOrdn = Integer.parseInt(modelo1.getValueAt(selectRow, 0).toString());
                Orden ord = ordDB.traeOrden(idOrdn);
                ord.setEst_ord("Anulada");
                ordDB.anulaOrden(ord);
                inicio();
            }
        }
    }//GEN-LAST:event_btnAnularOrdenActionPerformed

    private void btnCancelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelaActionPerformed
        // TODO add your handling code here:
        btnCancela.setEnabled(true);
        btnAnularOrden.setEnabled(false);
        txtBuscaOrden.setText(null);
        txtBuscaOrden1.setText(null);
        chkAnuladas.setSelected(false);
        btnModifica.setEnabled(false);
        btnNueva.setEnabled(true);
        btnReportePendientes.setVisible(false);
        llenaTablaOrdenP("Pendiente");
        llenaTablaOrdenF("Finalizada");
    }//GEN-LAST:event_btnCancelaActionPerformed

    private void chkAnuladasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAnuladasActionPerformed
        // TODO add your handling code here:
        if (chkAnuladas.isSelected() == true) {
            btnModifica.setEnabled(false);
            btnAnularOrden.setEnabled(false);
            llenaTablaOrdenA("Anulada");
            btnGuardar.setEnabled(false);
        } else {
            llenaTablaOrdenP("Pendiente");
            btnGuardar.setEnabled(true);
            btnReportePendientes.setVisible(false);
        }
    }//GEN-LAST:event_chkAnuladasActionPerformed

    /**
     * Carga las ordenes en la Tabla con el estado de Pendiente
     *
     * @param est
     */
    private void llenaTablaOrdenA(String est) {
        tablaModel1();
        List<Orden> listaOrd = null;
        listaOrd = ordDB.traeOrdenesTabla(est, listaOrd);
        for (Orden ord : listaOrd) {
            modelo1.addRow(new Object[]{
                ord.getId_orden(), ord.getNum_ord(), ord.getOrden().getTip_equi(),
                ord.getOrden().getPersona().getApe_per() + ", " + ord.getOrden().getPersona().getNom_per(),
                retornaFechaString(ord.getFec_ing(), 0), retornaFechaString(ord.getFec_sal(), 0),
                ord.getEst_ord(), ord.getTotal()
            });
        }
    }

    private void txtBuscaCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscaCodActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtBuscaCodActionPerformed

    private void txtBuscaCodKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaCodKeyTyped
        // TODO add your handling code here:
        vali.valNum(evt, txtBuscaCod, 6);
    }//GEN-LAST:event_txtBuscaCodKeyTyped

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        tablaModelEquipo();
        llenaTablaEqui();
        txtBuscaE.setText(null);
        frmEquipoCod.setTitle("LISTA DE EQUIPOS");
        frmEquipoCod.setSize(900, 400);
        frmEquipoCod.setLocationRelativeTo(null);
        frmEquipoCod.setModal(true);
        frmEquipoCod.setVisible(true);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCodigoActionPerformed

    private void txtCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCodigoKeyTyped

    private void txtModel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModel1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtModel1ActionPerformed

    private void txtModel1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtModel1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtModel1KeyTyped

    private void txtNum_Ser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNum_Ser1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNum_Ser1ActionPerformed

    private void txtNum_Ser1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNum_Ser1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNum_Ser1KeyTyped

    private void txtColor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColor1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtColor1ActionPerformed

    private void txtColor1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtColor1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtColor1KeyTyped

    private void txtTipo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTipo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipo1ActionPerformed

    private void txtTipo1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTipo1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipo1KeyTyped

    private void txtMarca1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMarca1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMarca1ActionPerformed

    private void txtMarca1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMarca1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMarca1KeyTyped

    private void txtCliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliente1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliente1ActionPerformed

    private void txtCliente1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliente1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliente1KeyTyped

    private void chkMantenimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMantenimientoActionPerformed
        // TODO add your handling code here:
        llenaServicios("Mantenimiento", "A");
        cboTecnico.setEnabled(true);
        cboDesServicio.setEnabled(true);
    }//GEN-LAST:event_chkMantenimientoActionPerformed

    /**
     * llena los servicios con el estado Activo
     *
     * @param tipo
     * @param est
     */
    private void llenaServicios(String tipo, String est) {

        cboDesServicio.removeAllItems();
        List<Servicio> lista = null;
        lista = servDB.cargaServi(tipo, est, lista);

        if (lista.size() > 0) {
            for (Servicio ser : lista) {
                cboDesServicio.addItem(ser.getDes_ser());
            }
        } else {
            JOptionPane.showMessageDialog(null, "NO EXISTEN SERVICIOS REGISTRADOS EN EL SISTEMA", "Mensaje", JOptionPane.WARNING_MESSAGE);
            buttonGroup4.clearSelection();
            DefaultComboBoxModel modeloc = new DefaultComboBoxModel();
            modeloc.addElement("SELECCIONE");
            cboDesServicio.setModel(modeloc);
        }

    }


    private void chkReparacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkReparacActionPerformed
        // TODO add your handling code here:
        llenaServicios("Reparación", "A");
        cboTecnico.setEnabled(true);
        cboDesServicio.setEnabled(true);
    }//GEN-LAST:event_chkReparacActionPerformed

    private void txtCostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoActionPerformed

    private void txtCostoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoKeyTyped

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        // TODO add your handling code here:
        quitarItem();
    }//GEN-LAST:event_btnQuitarActionPerformed

    /**
     * Quita el Ítem de la tabla Seleccionada
     */
    private void quitarItem() {
        if (Tabla3.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "ÍTEM NO SELECCIONADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {

            int conf = JOptionPane.showConfirmDialog(null, "ESTA SEGURO DE QUITAR EL REGISTRO ", "Mensaje", JOptionPane.YES_NO_OPTION);

            if (conf == 0) {
                int sel = Tabla3.getSelectedRow();
                modelo2.removeRow(sel);
                SumarTotal();
            }

        }
    }
    private void btnAgregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregaActionPerformed
        // TODO add your handling code here:
        if (btnAgrega.getText().equals("AGREGAR")) {

            if (validar_llenos() == true) {
                if (Repite_Tabla() == false) {
                    AgregarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "LA DESCRIPCIÓN YA SE ENCUENTRA AGREGADA EN LA TABLA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "LLENAR CAMPOS REQUERIDOS", "Mensaje", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnAgregaActionPerformed

    /**
     * valida los campos que no esten vacios
     *
     * @return
     */
    private boolean validar_llenos() {

        boolean lleno = true;

        if (jdcFechaEntrega.getCalendar() == null || jdcFechaIngreso.getCalendar() == null
                || txtBuscaCod.getText().equals("")
                || txtCosto.getText().equals("") || buttonGroup4.isSelected(null)) {
            lleno = false;
        } else {
            lleno = true;
        }
        return lleno;
    }

    /**
     * Verifica que los datos no se repitan al momento de Agregar en la Tabla
     *
     * @return
     */
    private boolean Repite_Tabla() {
        boolean flag = false;
        for (int i = 0; i < Tabla3.getRowCount(); i++) {
            if (Tabla3.getValueAt(i, 3).toString().equals(cboDesServicio.getSelectedItem().toString())) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Agrega los datos en el detalle de la Tabla
     */
    private void AgregarTabla() {

        if (btnAgrega.getText().equals("AGREGAR")) {
            String tipo = "";
            String Tec = cboTecnico.getSelectedItem().toString();
            String descrip = cboDesServicio.getSelectedItem().toString();
            double costo = Double.parseDouble(txtCosto.getText());

            if (chkMantenimiento.isSelected()) {
                tipo = "Mantenimiento";
            } else if (chkReparac.isSelected()) {
                tipo = "Reparación";
            }
            modelo2.addRow(new Object[]{
                txtCod_Orden.getText(), txtId_Serv.getText(), tipo,
                descrip, costo
            });
            SumarTotal();
        }
    }

    /**
     * Calcula el total de la orden
     */
    private void SumarTotal() {
        if (Tabla3.getRowCount() > 0) {
            double total = 0;

            for (int i = 0; i < Tabla3.getRowCount(); i++) {

                total += Double.parseDouble(modelo2.getValueAt(i, 4).toString());
            }
            txtTotal.setText(String.valueOf(total));
        } else {
            txtTotal.setText("0.00");
        }
    }
    private void Tabla3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla3MouseClicked

    }//GEN-LAST:event_Tabla3MouseClicked

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    private void txtTotalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalKeyTyped

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        if (modelo2.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "NO HAY DATOS AGREGADOS A LA TABLA ", "Mensaje", JOptionPane.WARNING_MESSAGE);
        } else {
            guardarOrden();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        inicio();
        btnNueva.setEnabled(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void txtCod_OrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCod_OrdenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCod_OrdenActionPerformed

    private void txtCod_OrdenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCod_OrdenKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCod_OrdenKeyTyped

    private void btnReportePendientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportePendientesActionPerformed
        // TODO add your handling code here:
        int selectRow = Tabla1.getSelectedRow();
        try {
            if (selectRow == -1) {
                JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA DE LA TABLA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            } else {
                try {
                    Conexion con = new Conexion();
                    Connection conn = con.getConexion();
                    JasperReport reporte = null;
                    Map parametro = new HashMap();
                    parametro.put("codigoOrden", Tabla1.getValueAt(selectRow, 1).toString());
                    reporte = (JasperReport) JRLoader.loadObject(getClass().getResource("/Reportes/ReporteOrden.jasper"));
                    JasperPrint jprint = JasperFillManager.fillReport(reporte, parametro, conn);
                    JasperViewer view = new JasperViewer(jprint, false);
                    view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    view.setVisible(true);
                    this.dispose();

                } catch (Exception e) {
                    java.util.logging.Logger.getLogger(frmPrincipal.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL TRAER LOS DATOS DE LA ORDEN " + e.getMessage(), "Mensaje", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnReportePendientesActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        tablaModelEquipo();
        txtBuscaE.setText(null);
        llenaTablaEqui();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSalorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalorActionPerformed
        // TODO add your handling code here:
        frmEquipoCod.dispose();
    }//GEN-LAST:event_btnSalorActionPerformed

    private void btnAcepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcepActionPerformed
        // TODO add your handling code here:

        int selectRow = TablaEquipo.getSelectedRow();
        try {
            if (selectRow == -1) {
                JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA DE LA TABLA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            } else {
                modelEquipo = (DefaultTableModel) TablaEquipo.getModel();
                String idEquipo = TablaEquipo.getValueAt(selectRow, 0).toString();
                String Codigo = TablaEquipo.getValueAt(selectRow, 1).toString();
                String modelo = TablaEquipo.getValueAt(selectRow, 2).toString();
                String num_serie = TablaEquipo.getValueAt(selectRow, 3).toString();
                String categoria = TablaEquipo.getValueAt(selectRow, 4).toString();
                String color = TablaEquipo.getValueAt(selectRow, 5).toString();
                String marcaEqui = TablaEquipo.getValueAt(selectRow, 6).toString();
                String cliente = TablaEquipo.getValueAt(selectRow, 7).toString();

                txtBuscaCod.setText(Codigo);
                txtIdEquipo.setText(idEquipo);
                txtCodigo.setText(Codigo);
                txtModel1.setText(modelo);
                txtNum_Ser1.setText(num_serie);
                txtColor1.setText(color);
                txtTipo1.setText(categoria);
                txtMarca1.setText(marcaEqui);
                txtCliente1.setText(cliente);
                frmEquipoCod.dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL AGREGAR CLIENTE" + e.getMessage(), "Mensaje", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_btnAcepActionPerformed

    private void TablaEquipoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaEquipoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TablaEquipoMouseClicked

    private void txtBuscaEKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaEKeyTyped

    }//GEN-LAST:event_txtBuscaEKeyTyped

    private void btnBuscaCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscaCodActionPerformed
        // TODO add your handling code here:
        if (txtBuscaE.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "INGRESE EL CÓDIGO DEL EQUIPO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            buscaEquipo(txtBuscaE.getText());
        }
    }//GEN-LAST:event_btnBuscaCodActionPerformed

    /**
     * Busca los datos del Equipo por el Código del Equipo y los presenta en el
     * Panel de la Informacíon del Equipo
     */
    private void buscaEquipo(String codigo) {

        modeloEquipo.setNumRows(0);
        List<Equipo> lis = null;
        lis = equiDB.buscarEqui(codigo, lis);

        if (lis.size() > 0) {
            for (Equipo equipoList : lis) {
                modeloEquipo.addRow(new Object[]{
                    equipoList.getIdEquipo(), equipoList.getCod_equi(), equipoList.getMod_equi(),
                    equipoList.getSer_equi(), equipoList.getTip_equi(), equipoList.getCol_equi(),
                    equipoList.getMarc().getNom_mar(),
                    equipoList.getPersona().getNom_per() + " " + equipoList.getPersona().getApe_per()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "EQUIPO NO ENCONTRADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscaE.requestFocus();
            llenaTablaEqui();
        }
    }


    private void cboDesServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDesServicioActionPerformed
        // TODO add your handling code here:
        if (cboDesServicio.getItemCount() != 0) {
            if (chkMantenimiento.isSelected() == true || chkReparac.isSelected() == true) {
                String cod_ser = cboDesServicio.getSelectedItem().toString();
                Servicio serv = servDB.traeServicio(cod_ser);
                txtId_Serv.setText(String.valueOf(serv.getIdServ()));
                txtCosto.setText(String.valueOf(serv.getPre_ser()));
            }
        }
    }//GEN-LAST:event_cboDesServicioActionPerformed
    
    
    /**
     * Trae los datos de la orden a travez del Id de la orden
     */
    private void traeOrden() {

        int selectRow = Tabla1.getSelectedRow();
        int idOrden = Integer.parseInt(modelo1.getValueAt(selectRow, 0).toString());

        Orden ord = ordDB.traeOrden(idOrden);

        List<DetaOrden> listaDetalle = new ArrayList<DetaOrden>();
        listaDetalle = ordDB.cargaDetalleOrden(listaDetalle, ord.getId_orden());
        txtBuscaCod.setText(ord.getOrden().getCod_equi());

        txtCod_Orden.setText(ord.getNum_ord());
        txtIdEquipo.setText(String.valueOf(ord.getOrden().getIdEquipo()));
        cboTecnico.setSelectedItem(ord.getEncargado());
        txtCodigo.setText(ord.getOrden().getCod_equi());
        txtModel1.setText(ord.getOrden().getMod_equi());
        txtNum_Ser1.setText(ord.getOrden().getSer_equi());
        txtColor1.setText(ord.getOrden().getCol_equi());
        txtTipo1.setText(ord.getOrden().getTip_equi());
        txtMarca1.setText(ord.getOrden().getMarc().getNom_mar());
        txtCliente1.setText(ord.getOrden().getPersona().getNom_per() + ", " + ord.getOrden().getPersona().getApe_per());
        txtObser_Previa.setText(ord.getObservacion());
        txtFalla.setText(ord.getFalla());
        txtAccesorios.setText(ord.getAccesorios());

        jdcFechaIngreso.setCalendar(ord.getFec_ing());
        jdcFechaEntrega.setCalendar(ord.getFec_sal());
        txtObser_Previa.setText(ord.getObservacion());

        for (DetaOrden deta : listaDetalle) {
            modelo2.addRow(new Object[]{
                deta.getOrde().getNum_ord(), deta.getServi().getIdServ(), deta.getServi().getTipo_ser(),
                deta.getServi().getDes_ser(), deta.getServi().getPre_ser()
            });
        }
        txtTotal.setText(String.valueOf(ord.getTotal()));
    }


    private void Tabla1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla1MousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            indice = 1;
            jTabbedPane1.setSelectedIndex(indice);
            jTabbedPane1.setTitleAt(1, "Revisión Orden");
            btnGuardar.setText("Finalizar Orden");
            txtCosto.setText("0.00");
            llenaUsuario("Técnico", "A");
            traeOrden();
            bloquear(true);
            DefaultComboBoxModel modeloc = new DefaultComboBoxModel();
            modeloc.addElement("Seleccione");
            cboDesServicio.setModel(modeloc);
            txtFalla.setEnabled(false);
            txtObser_Previa.setEnabled(false);
            txtAccesorios.setEnabled(false);
            Tabla3.setEnabled(true);
        }
    }//GEN-LAST:event_Tabla1MousePressed

    /*
     * Valida las fechas que han sido Ingresadas
     * @return
     */
    private boolean validaFechas() {
        boolean flag = false;
        Calendar fechaI = jdcFechaIngreso.getCalendar();
        Calendar fechaE = jdcFechaEntrega.getCalendar();

        if (fechaE.after(fechaI) || fechaI.equals(fechaE)) {
            flag = true;
        } else {

            if (fechaE.before(fechaI)) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * Guardar la Orden
     */
    private void guardarOrden() {
        Orden ordn = null;
        int nro;
        if (btnGuardar.getText().equals("Guardar")) {
            if (validaFechas() == true) {
                nro = Integer.parseInt(txtIdOrden.getText());
                int idEquipo = Integer.parseInt(txtIdEquipo.getText());
                ordn = new Orden();
                ordn.setId_orden(nro);
                Equipo equ = equiDB.traeEquipo(idEquipo);

                ordn.setNum_ord(txtCod_Orden.getText());
                ordn.setFec_ing(jdcFechaIngreso.getCalendar());
                ordn.setFec_sal(jdcFechaEntrega.getCalendar());
                ordn.setTotal(Double.parseDouble(txtTotal.getText()));
                ordn.setEst_ord("Pendiente");
                ordn.setAccesorios(txtAccesorios.getText());
                ordn.setFalla(txtFalla.getText());
                ordn.setEncargado(cboTecnico.getSelectedItem().toString());
                ordn.setObservacion(txtObser_Previa.getText());
                ordn.setOrden(equ);

                for (int i = 0; i < Tabla3.getRowCount(); i++) {
                    DetaOrden detaOrd = new DetaOrden();
                    int idSer = Integer.parseInt(modelo2.getValueAt(i, 1).toString());
                    Servicio ser = servDB.traeServicio(idSer);
                    detaOrd.setTipo_servi(Tabla3.getValueAt(i, 3).toString());
                    detaOrd.setPrec_ser(Double.parseDouble(Tabla3.getValueAt(i, 4).toString()));
                    detaOrd.setServi(ser);
                    detaOrd.setOrde(ordn);
                    ordn.getOrde().add(detaOrd);
                }
                ordDB.nuevaOrden(ordn);
                imprimirOrden();

            } else {
                JOptionPane.showMessageDialog(null, "ERROR AL INGRESAR LAS FECHAS", "Mensaje", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            if (btnGuardar.getText().equals("Actualizar Orden")) {
                if (validaFechas() == true) {
                    int selectrow = Tabla1.getSelectedRow();
                    int idOrden = Integer.parseInt(modelo1.getValueAt(selectrow, 0).toString());
                    ordn = ordDB.traeOrden(idOrden);
                    int idE = Integer.parseInt(txtIdEquipo.getText());
                    Equipo equ = equiDB.traeEquipo(idE);
                    ordn.setId_orden(idOrden);
                    ordn.setNum_ord(txtCod_Orden.getText());
                    ordn.setFec_ing(jdcFechaIngreso.getCalendar());
                    ordn.setFec_sal(jdcFechaEntrega.getCalendar());
                    ordn.setTotal(Double.parseDouble(txtTotal.getText()));
                    ordn.setEst_ord("Pendiente");
                    ordn.setAccesorios(txtAccesorios.getText());
                    ordn.setFalla(txtFalla.getText());
                    ordn.setEncargado(cboTecnico.getSelectedItem().toString());
                    ordn.setObservacion(txtObser_Previa.getText());
                    ordn.setOrden(equ);

                    for (int i = vectorDetalleId.size() - 1; i >= 0; i--) {
                        DetaOrden detalleOrden = new DetaOrden();
                        int ide = Integer.parseInt(vectorDetalleId.elementAt(i).toString());
                        detalleOrden = detaDB.traeDetalleOrden(ide);
                        detaDB.eliminaDetalleOrden(detalleOrden);
                    }

                    for (int i = 0; i < Tabla3.getRowCount(); i++) {
                        DetaOrden detaOrden2 = new DetaOrden();
                        int idSer = Integer.parseInt(modelo2.getValueAt(i, 1).toString());
                        Servicio ser = servDB.traeServicio(idSer);
                        detaOrden2.setTipo_servi(Tabla3.getValueAt(i, 3).toString());
                        detaOrden2.setPrec_ser(Double.parseDouble(Tabla3.getValueAt(i, 4).toString()));
                        detaOrden2.setServi(ser);
                        detaOrden2.setOrde(ordn);
                        ordn.getOrde().add(detaOrden2);
                    }
                    ordDB.actualizaOrden(ordn);
                    imprimirOrdenModificar();

                } else {
                    JOptionPane.showMessageDialog(null, "NO SE PUEDE AGREGAR UNA FECHA PASADA DE LA FECHA ACTUAL", "Mensaje", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (btnGuardar.getText().equals("Finalizar Orden")) {
                    Orden ord = new Orden();
                    int selectRow = Tabla1.getSelectedRow();
                    int idOrdn = Integer.parseInt(modelo1.getValueAt(selectRow, 0).toString());
                    ord = ordDB.traeOrden(idOrdn);
                    ord.setObservacion(txtObser_Previa.getText());
                    ord.setEst_ord("Finalizada");
                    ordDB.finalizaOrden(ord);
                }
            }
        }
        inicio();
    }

    /**
     * Imprime el reporte con la orden guardada
     */
    private void imprimirOrdenModificar() {

        int si = JOptionPane.showConfirmDialog(this, "¿DESEA IMPRIMIR LA ORDEN?", "Mensaje", JOptionPane.YES_NO_OPTION);

        if (si == JOptionPane.NO_OPTION) {
            inicio();
        } else {
            try {
                Conexion con = new Conexion();
                Connection conn = con.getConexion();

                JasperReport reporte = null;
                Map parametro = new HashMap();
                parametro.put("codigoOrden", txtCod_Orden.getText());
                reporte = (JasperReport) JRLoader.loadObject(getClass().getResource("/Reportes/ReporteOrden.jasper"));

                JasperPrint jprint = JasperFillManager.fillReport(reporte, parametro, conn);
                JasperViewer view = new JasperViewer(jprint, false);
                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                view.setVisible(true);
                this.dispose();

            } catch (Exception e) {
                java.util.logging.Logger.getLogger(frmPrincipal.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * Imprime el reporte con la orden guardada
     */
    private void imprimirOrden() {

        int si = JOptionPane.showConfirmDialog(this, "¿DESEA IMPRIMIR LA ORDEN?", "Mensaje", JOptionPane.YES_NO_OPTION);

        if (si == JOptionPane.NO_OPTION) {
            this.dispose();
        } else {
            try {
                Conexion con = new Conexion();
                Connection conn = con.getConexion();

                JasperReport reporte = null;
                Map parametro = new HashMap();
                parametro.put("codigoOrden", txtCod_Orden.getText());
                reporte = (JasperReport) JRLoader.loadObject(getClass().getResource("/Reportes/ReporteOrden.jasper"));

                JasperPrint jprint = JasperFillManager.fillReport(reporte, parametro, conn);
                JasperViewer view = new JasperViewer(jprint, false);
                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                view.setVisible(true);
                this.dispose();

            } catch (Exception e) {
                java.util.logging.Logger.getLogger(frmPrincipal.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmOrden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmOrden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmOrden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmOrden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frmOrden dialog = new frmOrden(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSTableMetro Tabla1;
    private rojerusan.RSTableMetro Tabla2;
    private rojerusan.RSTableMetro Tabla3;
    private rojerusan.RSTableMetro TablaEquipo;
    private rojerusan.RSMaterialButtonRound btnAcep;
    private rojerusan.RSMaterialButtonRound btnAgrega;
    private rojerusan.RSMaterialButtonRound btnAnularOrden;
    private rojerusan.RSMaterialButtonRound btnBuscaCod;
    private rojerusan.RSMaterialButtonRound btnBuscar;
    private rojerusan.RSMaterialButtonRound btnCancel;
    private rojerusan.RSMaterialButtonRound btnCancela;
    private rojerusan.RSMaterialButtonRound btnGuardar;
    private rojerusan.RSMaterialButtonRound btnModifica;
    private rojerusan.RSMaterialButtonRound btnNueva;
    private rojerusan.RSMaterialButtonRound btnQuitar;
    private rojerusan.RSMaterialButtonRound btnReportePendientes;
    private rojerusan.RSMaterialButtonRound btnSalor;
    private rojerusan.RSMaterialButtonRound btnbuscaorden;
    private rojerusan.RSMaterialButtonRound btnbuscaorden1;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JComboBox<String> cboDesServicio;
    private javax.swing.JComboBox<String> cboTecnico;
    private javax.swing.JCheckBox chkAnuladas;
    private javax.swing.JCheckBox chkMantenimiento;
    private javax.swing.JCheckBox chkReparac;
    private javax.swing.JDialog frmEquipoCod;
    private rojerusan.RSMaterialButtonRound jButton10;
    private rojerusan.RSMaterialButtonRound jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.toedter.calendar.JDateChooser jdcFechaEntrega;
    private com.toedter.calendar.JDateChooser jdcFechaIngreso;
    private javax.swing.JLabel lblTipoMensaje;
    private javax.swing.JTextArea txtAccesorios;
    private javax.swing.JTextField txtBuscaCod;
    private javax.swing.JTextField txtBuscaE;
    private javax.swing.JTextField txtBuscaOrden;
    private javax.swing.JTextField txtBuscaOrden1;
    private javax.swing.JTextField txtCliente1;
    private javax.swing.JTextField txtCod_Orden;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtColor1;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextArea txtFalla;
    private javax.swing.JLabel txtIdEquipo;
    private javax.swing.JLabel txtIdOrden;
    private javax.swing.JLabel txtId_Serv;
    private javax.swing.JTextField txtMarca1;
    private javax.swing.JTextField txtModel1;
    private javax.swing.JTextField txtNum_Ser1;
    private javax.swing.JTextArea txtObser_Previa;
    private javax.swing.JTextField txtTipo1;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
