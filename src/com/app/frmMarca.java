/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app;

import com.controlador.MarcaDB;
import com.controlador.Validaciones;
import com.entidades.Marca;
import java.awt.Color;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import rojerusan.necesario.RSScrollBar;

/**
 *
 * @author Usuario
 */
public class frmMarca extends javax.swing.JDialog {

    /**
     * Creates new form frmMarca
     */
    MarcaDB marcDB = new MarcaDB();
    DefaultTableModel model;
    Validaciones val = new Validaciones();

    public frmMarca(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        jScrollPane1.getVerticalScrollBar().setUI(new RSScrollBar());
        jScrollPane1.getViewport().setBackground(Color.WHITE);
        inicio();
    }

    /**
     * Inicio de la Aplicacion
     */
    private void inicio() {
        txtId.setText("Auto");
        llenatablaM("A");
        btnGuardar.setEnabled(false);
        tablaMarcas.setEnabled(true);
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnGuardar.setText("GUARDAR");
        btnModificar.setEnabled(false);
        txtNombre.setText(null);
        txtNombre.setEnabled(false);
        btnDesactivar.setEnabled(false);
        chkDesactivados.setSelected(false);
        btnBuscar.setEnabled(true);
        txtBuscarMarca.setEnabled(true);
        btnCancelar.setEnabled(true);
        txtBuscarMarca.setText("");
    }

    /**
     * Visualiza el dato con un tamaño establecido de los datos en las filas y
     * columnas de la tabla.
     */
    private void tablaModel() {
        tablaMarcas.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaMarcas.getColumnModel().getColumn(0).setMinWidth(0);
        tablaMarcas.getColumnModel().getColumn(0).setPreferredWidth(0);
        tablaMarcas.getColumnModel().getColumn(1).setPreferredWidth(300);
        model = (DefaultTableModel) tablaMarcas.getModel();
        model.setNumRows(0);
    }

    /**
     * Carga los datos en la Tabla
     *
     * @param est
     */
    private void llenatablaM(String est) {
        tablaModel();
        List<Marca> lista = null;
        lista = marcDB.cargaMarca(est, lista);
        for (Marca marLis : lista) {
            model.addRow(new Object[]{
                marLis.getId_mar(), marLis.getNom_mar()
            });
        }
    }

    /**
     * Limpia las Cajas de texto
     */
    private void limpiar_campos() {
        txtNombre.setText("");
    }

    /**
     * Guarda los datos de la marca
     */
    private void guardar() {
        Marca marc = null;
        if (txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "LLENAR CAMPOS REQUERIDOS", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            limpiar_campos();
            txtNombre.setEnabled(true);
        } else {
            if (btnGuardar.getText().equals("GUARDAR")) {
                marc = marcDB.traeMarcas(txtNombre.getText());
                if (marc == null) {
                    marc = new Marca();
                    marc.setNom_mar(txtNombre.getText());
                    marc.setEst_mar("A");

                    marcDB.nuevaMarca(marc);
                } else {
                    JOptionPane.showMessageDialog(null, "EL NOMBRE DE LA MARCA YA EXISTE EN EL SISTEMA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                if (btnGuardar.getText().equals("ACTUALIZAR")) {
                    /**
                     * Actualiza los datos de la marca
                     */
                    marc = marcDB.traeMarcas(txtNombre.getText());
                    if (marc == null) {
                        int id = Integer.parseInt(txtId.getText());
                        marc = marcDB.traeMarca(id);
                        marc.setNom_mar(txtNombre.getText());
                        marcDB.actualizaMarca(marc);
                        JOptionPane.showMessageDialog(null, "DATOS ACTUALIZADOS", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "EL NOMBRE DE LA MARCA YA EXISTE EN EL SISTEMA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }

        }
        inicio();
    }

    /**
     * Cambia el estado de la marca a Pasivo
     */
    private void desactivar() {
        int sele_trow = tablaMarcas.getSelectedRow();

        int si = JOptionPane.showConfirmDialog(this, "ESTÁ SEGURO DE DESACTIVAR LA MARCA", "Desactivar Marca", JOptionPane.YES_NO_OPTION);
        if (si == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "NO SE HA PODIDO DESACTIVAR LA MARCA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int selec_trow = tablaMarcas.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(model.getValueAt(selec_trow, 0)));
            Marca mar = marcDB.traeMarca(id);
            mar.setEst_mar("P");
            marcDB.actualizaMarca(mar);

            JOptionPane.showMessageDialog(this, "MARCA DESACTIVADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            inicio();
        }
    }

    /**
     * Cambia el estado de la marca a Activo
     */
    private void activar() {
        int sele_trow = tablaMarcas.getSelectedRow();

        int si = JOptionPane.showConfirmDialog(this, "DESEA ACTIVAR LA MARCA", " Activar Marca", JOptionPane.YES_NO_OPTION);
        if (si == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "NO SE HA PODIDO ACTIVAR LA MARCA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int selec_trow = tablaMarcas.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(model.getValueAt(selec_trow, 0)));
            Marca mar = marcDB.traeMarca(id);
            mar.setEst_mar("A");
            marcDB.actualizaMarca(mar);
            JOptionPane.showMessageDialog(this, "MARCA ACTIVADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            inicio();
        }
    }

    /**
     * Busca la marca a travez de la descripciòn
     */
    private void buscaMarca(String descrip) {
        model.setNumRows(0);
        List<Marca> lis = null;
        descrip = txtBuscarMarca.getText();

        lis = marcDB.buscarMarca(descrip, lis);

        if (lis.size() > 0) {
            for (Marca marLis : lis) {
                model.addRow(new Object[]{
                    marLis.getId_mar(), marLis.getNom_mar(), marLis.getEst_mar()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "MARCA NO ENCONTRADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscarMarca.requestFocus();
            llenatablaM("A");
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtBuscarMarca = new javax.swing.JTextField();
        btnBuscar = new rojerusan.RSMaterialButtonRound();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaMarcas = new rojerusan.RSTableMetro();
        chkDesactivados = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        btnGuardar = new rojerusan.RSMaterialButtonRound();
        btnCancelar = new rojerusan.RSMaterialButtonRound();
        btnNuevo = new rojerusan.RSMaterialButtonRound();
        btnModificar = new rojerusan.RSMaterialButtonRound();
        btnDesactivar = new rojerusan.RSMaterialButtonRound();
        btnSalir = new rojerusan.RSMaterialButtonRound();
        txtId = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Bahnschrift", 3, 38)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(179, 21, 12));
        jLabel1.setText("MARCAS");

        jLabel2.setFont(new java.awt.Font("Bahnschrift", 3, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(179, 21, 12));
        jLabel2.setText("NOMBRE MARCA:");

        txtBuscarMarca.setBackground(new java.awt.Color(255, 206, 206));
        txtBuscarMarca.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        txtBuscarMarca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarMarcaKeyTyped(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        tablaMarcas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NOMBRE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaMarcas.setColorBackgoundHead(new java.awt.Color(0, 193, 235));
        tablaMarcas.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        tablaMarcas.setColorBordeHead(new java.awt.Color(255, 255, 255));
        tablaMarcas.setColorForegroundHead(new java.awt.Color(0, 0, 0));
        tablaMarcas.setColorSelBackgound(new java.awt.Color(0, 193, 235));
        tablaMarcas.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        tablaMarcas.setFuenteFilas(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        tablaMarcas.setFuenteFilasSelect(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        tablaMarcas.setFuenteHead(new java.awt.Font("Copperplate Gothic Bold", 2, 12)); // NOI18N
        tablaMarcas.setRowHeight(20);
        tablaMarcas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMarcasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaMarcas);

        chkDesactivados.setBackground(new java.awt.Color(255, 255, 255));
        chkDesactivados.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        chkDesactivados.setText("Desactivados");
        chkDesactivados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDesactivadosActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 3, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(179, 21, 12));
        jLabel3.setText("DATOS MARCA");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel4.setText("NOMBRE:");

        txtNombre.setBackground(new java.awt.Color(255, 206, 206));
        txtNombre.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(179, 21, 12));
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(179, 21, 12));
        btnCancelar.setText("CANCELAR");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnNuevo.setBackground(new java.awt.Color(179, 21, 12));
        btnNuevo.setText("NUEVO");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(179, 21, 12));
        btnModificar.setText("MODIFICAR");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnDesactivar.setBackground(new java.awt.Color(179, 21, 12));
        btnDesactivar.setText("DESACTIVAR");
        btnDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarActionPerformed(evt);
            }
        });

        btnSalir.setBackground(new java.awt.Color(179, 21, 12));
        btnSalir.setText("X");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        txtId.setText("Auto");
        txtId.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBuscarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(chkDesactivados))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombre)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(74, 74, 74)
                                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnDesactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtBuscarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkDesactivados)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel3)
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDesactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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

    private void chkDesactivadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDesactivadosActionPerformed
        // TODO add your handling code here:
        tablaModel();
        if (chkDesactivados.isSelected() == true) {
            llenatablaM("P");
            btnDesactivar.setText("DESACTIVAR");
        } else {
            btnDesactivar.setText("DESACTIVAR");
            llenatablaM("A");
        }
    }//GEN-LAST:event_chkDesactivadosActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        if (txtBuscarMarca.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "LLENAR CAMPO REQUERIDO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscarMarca.requestFocus();
        } else {
            buscaMarca(txtBuscarMarca.getText());
            chkDesactivados.setSelected(false);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        btnNuevo.setEnabled(false);
        tablaMarcas.setEnabled(false);
        btnSalir.setEnabled(true);
        txtNombre.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        txtBuscarMarca.setEnabled(false);
        btnBuscar.setEnabled(false);
        txtNombre.requestFocus();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        txtBuscarMarca.setEnabled(false);
        btnBuscar.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnGuardar.setText("ACTUALIZAR");
        txtNombre.setEnabled(true);
        btnModificar.setEnabled(false);
        btnDesactivar.setEnabled(false);
        btnGuardar.setEnabled(true);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
        // TODO add your handling code here:
        if (btnDesactivar.getText().equals("DESACTIVAR")) {
            desactivar();
        } else {
            activar();
        }
    }//GEN-LAST:event_btnDesactivarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        guardar();
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnGuardar.setText("GUARDAR");
        btnModificar.setEnabled(false);
        txtNombre.setText(null);
        txtNombre.setEnabled(false);
        btnBuscar.setEnabled(true);
        txtBuscarMarca.setEnabled(true);
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        inicio();
        tablaMarcas.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tablaMarcasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMarcasMouseClicked
        // TODO add your handling code here:
        if (chkDesactivados.isSelected() == true) {
            int selectRow = tablaMarcas.getSelectedRow();
            int idmarca = Integer.parseInt(model.getValueAt(selectRow, 0).toString());
            Marca mar = marcDB.traeMarca(idmarca);
            txtId.setText(String.valueOf(mar.getId_mar()));
            txtNombre.setText(mar.getNom_mar());
            btnDesactivar.setText("ACTIVAR");
            Bloquear();

        } else {
            int selectRow = tablaMarcas.getSelectedRow();
            int idmarca = Integer.parseInt(model.getValueAt(selectRow, 0).toString());
            Marca mar = marcDB.traeMarca(idmarca);
            txtId.setText(String.valueOf(mar.getId_mar()));
            txtNombre.setText(mar.getNom_mar());
            btnDesactivar.setText("DESACTIVAR");
            Bloquear();
        }
    }//GEN-LAST:event_tablaMarcasMouseClicked

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtBuscarMarcaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarMarcaKeyTyped
        // TODO add your handling code here:
        val.Mayusculas(txtBuscarMarca.getText(), evt);
        val.valLetr(evt, txtBuscarMarca, 20);
    }//GEN-LAST:event_txtBuscarMarcaKeyTyped

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        // TODO add your handling code here:
        val.Mayusculas(txtNombre.getText(), evt);
        val.valLetr(evt, txtNombre, 20);
    }//GEN-LAST:event_txtNombreKeyTyped

    /**
     * Bloquea las cajas de texto
     *
     * @param flag
     */
    private void Bloquear() {
        btnModificar.setEnabled(true);
        txtNombre.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnBuscar.setEnabled(false);
        txtBuscarMarca.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnDesactivar.setEnabled(true);

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
            java.util.logging.Logger.getLogger(frmMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frmMarca dialog = new frmMarca(new javax.swing.JFrame(), true);
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
    private rojerusan.RSMaterialButtonRound btnBuscar;
    private rojerusan.RSMaterialButtonRound btnCancelar;
    private rojerusan.RSMaterialButtonRound btnDesactivar;
    private rojerusan.RSMaterialButtonRound btnGuardar;
    private rojerusan.RSMaterialButtonRound btnModificar;
    private rojerusan.RSMaterialButtonRound btnNuevo;
    private rojerusan.RSMaterialButtonRound btnSalir;
    private javax.swing.JCheckBox chkDesactivados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private rojerusan.RSTableMetro tablaMarcas;
    private javax.swing.JTextField txtBuscarMarca;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
