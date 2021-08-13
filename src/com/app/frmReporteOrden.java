/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app;

import com.controlador.OrdenDB;
import com.entidades.Orden;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
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
public class frmReporteOrden extends javax.swing.JDialog {

    /**
     * Creates new form frmReporteOrden
     */
    DefaultTableModel modelo1 = new DefaultTableModel();
    OrdenDB ordDB = new OrdenDB();

    public frmReporteOrden(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        inicio();
    }

    /**
     * Inicio de la Aplicación
     */
    private void inicio() {
        llenaTablaOrden("Finalizada");
        sumaTotal();
    }

    /**
     * Visualiza el dato con un tamaño establecido de los datos en las filas y
     * columnas de la tabla
     */
    public void TablaModel() {
        Tabla1.getColumnModel().getColumn(0).setMaxWidth(0);
        Tabla1.getColumnModel().getColumn(0).setMinWidth(0);
        Tabla1.getColumnModel().getColumn(0).setPreferredWidth(0);
        Tabla1.getColumnModel().getColumn(1).setPreferredWidth(150);
        Tabla1.getColumnModel().getColumn(2).setPreferredWidth(150);
        Tabla1.getColumnModel().getColumn(3).setPreferredWidth(400);
        Tabla1.getColumnModel().getColumn(4).setPreferredWidth(200);
        Tabla1.getColumnModel().getColumn(5).setPreferredWidth(100);
        modelo1 = (DefaultTableModel) Tabla1.getModel();
        modelo1.setNumRows(0);
    }

    /**
     * Carga los datos de la Orden
     */
    private void llenaTablaOrden(String est) {

        TablaModel();
        List<Orden> listaOrd = null;
        listaOrd = ordDB.traeOrdenesFinali(est, listaOrd);
        for (Orden ord : listaOrd) {
            modelo1.addRow(new Object[]{
                ord.getId_orden(), ord.getNum_ord(), ord.getOrden().getTip_equi(),
                ord.getOrden().getPersona().getNom_per() + " " + ord.getOrden().getPersona().getApe_per(),
                retornaFechaString(ord.getFec_ing(), 0), ord.getTotal()
            });
        }
    }

    /**
     * Retorna una fecha en formato string
     *
     * @param fecha
     * @param dias
     * @return
     */
    private String retornaFechaString(Calendar fecha, int dias) {
        String retorno = null;
        fecha.add(Calendar.DAY_OF_YEAR, dias);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (fecha != null) {
            retorno = sdf.format(fecha.getTime());
        }
        return retorno;
    }

    /**
     * Retorna una fecha en formato string
     *
     * @param fecha
     * @param dias
     * @return
     */
    private String retornaFechaString2(Calendar fecha, int dias) {
        String retorno = null;
        fecha.add(Calendar.DAY_OF_YEAR, dias);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (fecha != null) {
            retorno = sdf.format(fecha.getTime());
        }
        return retorno;
    }

    /**
     * Busca la orden a travez del número de la orden
     *
     * @param numero
     */
    private void buscaOrden(String numero) {
        TablaModel();
        List<Orden> listaOrd = null;
        listaOrd = ordDB.BuscarOrden(numero, listaOrd);

        if (listaOrd.size() > 0) {
            for (Orden ord : listaOrd) {
                modelo1.addRow(new Object[]{
                    ord.getId_orden(), ord.getNum_ord(), ord.getOrden().getTip_equi(),
                    ord.getOrden().getPersona().getNom_per() + " " + ord.getOrden().getPersona().getApe_per(),
                    retornaFechaString(ord.getFec_ing(), 0), ord.getTotal()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "ORDEN NO ENCONTRADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtNumero.requestFocus();
            llenaTablaOrden("Finalizada");
        }
    }

    /**
     * Busca la orden a travez de la fecha de ingreso
     *
     * @param numero
     */
    private void buscaOrdenF(String f1, String f2) {
        TablaModel();
        List<Orden> listaOrd = null;

        f1 = retornaFechaString2(jdcFecha.getCalendar(), 0);
        f2 = retornaFechaString2(jdcFechaF.getCalendar(), 0);

        listaOrd = ordDB.BuscarOrdenFecha(listaOrd, f1, f2);
        if (listaOrd.size() > 0) {
            for (Orden ord : listaOrd) {
                modelo1.addRow(new Object[]{
                    ord.getId_orden(), ord.getNum_ord(), ord.getOrden().getTip_equi(),
                    ord.getOrden().getPersona().getNom_per() + " " + ord.getOrden().getPersona().getApe_per(),
                    retornaFechaString(ord.getFec_ing(), 0), ord.getTotal()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "ORDEN NO ENCONTRADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            jdcFecha.requestFocus();
            llenaTablaOrden("Finalizada");
        }
    }

    /**
     * Suma el total de las ordenes que hayan sido Finalizadas
     */
    private void sumaTotal() {
        if (Tabla1.getRowCount() > 0) {
            double total = 0;
            for (int i = 0; i < Tabla1.getRowCount(); i++) {
                total += Double.parseDouble(modelo1.getValueAt(i, 5).toString());
            }
            txtValorTotal.setText(String.valueOf(total));
        } else {
            txtValorTotal.setText("0.00");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        rdbNumero = new javax.swing.JRadioButton();
        rdbFecha = new javax.swing.JRadioButton();
        jdcFecha = new com.toedter.calendar.JDateChooser();
        jdcFechaF = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtValorTotal = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla1 = new rojerusan.RSTableMetro();
        btnBuscar = new rojerusan.RSMaterialButtonRound();
        btnBuscaFecha = new rojerusan.RSMaterialButtonRound();
        txtNumero = new javax.swing.JTextField();
        btnVer = new rojerusan.RSMaterialButtonRound();
        btnCancel = new rojerusan.RSMaterialButtonRound();
        btnSalir = new rojerusan.RSMaterialButtonRound();
        btnReportesClientes = new rojerusan.RSMaterialButtonRound();
        btnReportesEquipos = new rojerusan.RSMaterialButtonRound();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Orden", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 14), new java.awt.Color(255, 51, 51))); // NOI18N

        buttonGroup1.add(rdbNumero);
        rdbNumero.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        rdbNumero.setText("Número de Orden");
        rdbNumero.setOpaque(false);
        rdbNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbNumeroActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdbFecha);
        rdbFecha.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        rdbFecha.setText("Fecha ");
        rdbFecha.setOpaque(false);
        rdbFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbFechaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdbNumero)
                    .addComponent(rdbFecha))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdbNumero)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rdbFecha)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jdcFecha.setBackground(new java.awt.Color(255, 206, 206));
        jdcFecha.setDateFormatString("dd-MM-yyyy");
        jdcFecha.setEnabled(false);
        jdcFecha.setOpaque(false);

        jdcFechaF.setBackground(new java.awt.Color(255, 206, 206));
        jdcFechaF.setDateFormatString("dd-MM-yyyy");
        jdcFechaF.setEnabled(false);
        jdcFechaF.setOpaque(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Fecha Fin");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Fecha Ingreso");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Numero de Orden");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 51, 51));
        jLabel4.setText("Total de Ganancias");

        txtValorTotal.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        txtValorTotal.setForeground(new java.awt.Color(0, 153, 0));
        txtValorTotal.setText("0.00");
        txtValorTotal.setEnabled(false);

        Tabla1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NRO.ORDEN", "EQUIPO", "CLIENTE", "FECHA INICIO", "TOTAL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
        });
        jScrollPane2.setViewportView(Tabla1);

        btnBuscar.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnBuscaFecha.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscaFecha.setText("BUSCAR");
        btnBuscaFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscaFechaActionPerformed(evt);
            }
        });

        txtNumero.setBackground(new java.awt.Color(255, 206, 206));
        txtNumero.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        txtNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumeroKeyTyped(evt);
            }
        });

        btnVer.setBackground(new java.awt.Color(179, 21, 12));
        btnVer.setText("Ver Orden");
        btnVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(179, 21, 12));
        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSalir.setBackground(new java.awt.Color(179, 21, 12));
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnReportesClientes.setBackground(new java.awt.Color(0, 0, 0));
        btnReportesClientes.setText("REPORTE CLIENTES");
        btnReportesClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportesClientesActionPerformed(evt);
            }
        });

        btnReportesEquipos.setBackground(new java.awt.Color(0, 0, 0));
        btnReportesEquipos.setText("REPORTE EQUIPOS");
        btnReportesEquipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportesEquiposActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNumero, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                            .addComponent(jdcFechaF, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jdcFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscaFecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(87, 87, 87)
                                    .addComponent(btnVer, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(112, 112, 112)
                                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(123, 123, 123)
                                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnReportesEquipos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnReportesClientes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jdcFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jdcFechaF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(btnBuscaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(btnReportesClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(btnReportesEquipos, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVer, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdbNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbNumeroActionPerformed
        txtNumero.setEnabled(true);
        jdcFecha.setEnabled(false);
        jdcFecha.setCalendar(null);
        jdcFechaF.setEnabled(false);
        jdcFechaF.setCalendar(null);
        btnBuscaFecha.setEnabled(false);
        btnBuscar.setEnabled(true);
        txtNumero.requestFocus();

    }//GEN-LAST:event_rdbNumeroActionPerformed

    private void rdbFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbFechaActionPerformed
        txtNumero.setText(null);
        txtNumero.setEnabled(false);
        btnBuscar.setEnabled(false);
        btnBuscaFecha.setEnabled(true);
        jdcFecha.setEnabled(true);
        jdcFechaF.setEnabled(true);
        jdcFecha.requestFocusInWindow();
    }//GEN-LAST:event_rdbFechaActionPerformed

    private void Tabla1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            btnVer.setEnabled(true);
        }
    }//GEN-LAST:event_Tabla1MouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        if (txtNumero.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "LLENAR CAMPO REQUERIDO", "Mensaje", JOptionPane.WARNING_MESSAGE);
        } else {
            buscaOrden(txtNumero.getText());
            sumaTotal();
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnBuscaFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscaFechaActionPerformed
        // TODO add your handling code here:
        Calendar fechaI = jdcFecha.getCalendar();
        Calendar fechaE = jdcFechaF.getCalendar();
        if (jdcFecha.getCalendar() == null || jdcFechaF.getCalendar() == null) {
            JOptionPane.showMessageDialog(null, "LLENAR CAMPOS REQUERIDOS", "Mensaje", JOptionPane.WARNING_MESSAGE);
        } else {
            if (fechaE.before(fechaI)) {
                JOptionPane.showMessageDialog(null, "ERROR AL INGRESAR LAS FECHAS", "Mensaje", JOptionPane.ERROR_MESSAGE);
            } else {
                String f1 = retornaFechaString2(jdcFecha.getCalendar(), 0);
                String f2 = retornaFechaString2(jdcFechaF.getCalendar(), 0);
                buscaOrdenF(f1, f2);
                sumaTotal();
            }
        }
    }//GEN-LAST:event_btnBuscaFechaActionPerformed

    private void txtNumeroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_txtNumeroKeyTyped

    private void btnVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerActionPerformed
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
    }//GEN-LAST:event_btnVerActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        buttonGroup1.clearSelection();
        txtNumero.setText(null);
        jdcFecha.setCalendar(null);
        jdcFechaF.setCalendar(null);
        jdcFecha.setEnabled(false);
        jdcFechaF.setEnabled(false);
        btnBuscaFecha.setEnabled(false);
        btnBuscar.setEnabled(false);
        btnVer.setEnabled(false);
        llenaTablaOrden("Finalizada");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnReportesClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportesClientesActionPerformed
        // TODO add your handling code here:
        try {
            Conexion con = new Conexion();
            Connection conn = con.getConexion();

            JasperReport reporte = null;
            //String path = "src\\Reportes\\ReporteClientes.jasper";
            reporte = (JasperReport) JRLoader.loadObject(getClass().getResource("/Reportes/ReporteClientes.jasper"));
            JasperPrint jprint = JasperFillManager.fillReport(reporte, null, conn);
            JasperViewer view = new JasperViewer(jprint, false);

            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            view.setVisible(true);

        } catch (Exception e) {
            java.util.logging.Logger.getLogger(frmPrincipal.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_btnReportesClientesActionPerformed

    private void btnReportesEquiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportesEquiposActionPerformed
        // TODO add your handling code here:
        try {
            Conexion con = new Conexion();
            Connection conn = con.getConexion();

            JasperReport reporte = null;
            reporte = (JasperReport) JRLoader.loadObject(getClass().getResource("/Reportes/ReporteEquipo.jasper"));

            JasperPrint jprint = JasperFillManager.fillReport(reporte, null, conn);
            JasperViewer view = new JasperViewer(jprint, false);
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            view.setVisible(true);

        } catch (Exception e) {
            java.util.logging.Logger.getLogger(frmPrincipal.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_btnReportesEquiposActionPerformed

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
            java.util.logging.Logger.getLogger(frmReporteOrden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmReporteOrden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmReporteOrden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmReporteOrden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frmReporteOrden dialog = new frmReporteOrden(new javax.swing.JFrame(), true);
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
    private rojerusan.RSMaterialButtonRound btnBuscaFecha;
    private rojerusan.RSMaterialButtonRound btnBuscar;
    private rojerusan.RSMaterialButtonRound btnCancel;
    private rojerusan.RSMaterialButtonRound btnReportesClientes;
    private rojerusan.RSMaterialButtonRound btnReportesEquipos;
    private rojerusan.RSMaterialButtonRound btnSalir;
    private rojerusan.RSMaterialButtonRound btnVer;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser jdcFecha;
    private com.toedter.calendar.JDateChooser jdcFechaF;
    private javax.swing.JRadioButton rdbFecha;
    private javax.swing.JRadioButton rdbNumero;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtValorTotal;
    // End of variables declaration//GEN-END:variables
}
