/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app;

import com.controlador.ServicioDB;
import com.controlador.Validaciones;
import com.entidades.Servicio;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class frmServicio extends javax.swing.JDialog {

    /**
     * Importaciones e instancias
     */
    Validaciones val = new Validaciones();
    ServicioDB serDB = new ServicioDB();
    DefaultTableModel model;

    public frmServicio(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        inicio();
        setLocationRelativeTo(null);
    }

    /**
     * Inicio la Aplicación
     */
    private void inicio() {
        txtcodigo.setText("Auto");
        tablaModel();
        llenaTabla("A");
        limpia_campos();
        chkdesactivados.setSelected(false);
        btnGuardar.setText("GUARDAR");
    }

    /**
     * Limpia las cajas de texto
     */
    private void limpia_campos() {
        btnBuscar.setEnabled(true);
        txtBuscarServicio.setEnabled(true);
        txtcodigo.setText("Auto");
        txtDescripcion.setText(null);
        txtCosto.setText(null);
        grupoTipo.clearSelection();
        btnNuevo.setEnabled(true);
        btnModificar.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnDesactivar.setEnabled(false);
        txtcodigo.setEnabled(false);
        txtDescripcion.setEnabled(false);
        txtCosto.setEnabled(false);
        rdMantenimiento.setEnabled(false);
        rdReparacion.setEnabled(false);
        tablaServicios.setEnabled(true);
    }

    /**
     * Visualiza el dato con un tamaño establecido de los datos en las filas y
     * columnas de la tabla
     */
    private void tablaModel() {
        tablaServicios.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaServicios.getColumnModel().getColumn(0).setMinWidth(0);
        tablaServicios.getColumnModel().getColumn(0).setPreferredWidth(0);
        tablaServicios.getColumnModel().getColumn(1).setPreferredWidth(110);
        tablaServicios.getColumnModel().getColumn(2).setPreferredWidth(300);
        tablaServicios.getColumnModel().getColumn(3).setPreferredWidth(130);
        tablaServicios.getColumnModel().getColumn(4).setPreferredWidth(70);
        model = (DefaultTableModel) tablaServicios.getModel();
        model.setNumRows(0);
    }

    /**
     * Llena los datos del servicio en la tabla
     *
     * @param est
     */
    private void llenaTabla(String est) {
        List<Servicio> lista = null;
        lista = serDB.cargaServicios(est, lista);
        for (Servicio serLis : lista) {
            model.addRow(new Object[]{
                serLis.getIdServ(), serLis.getCod_ser(), serLis.getDes_ser(),
                serLis.getTipo_ser(), serLis.getPre_ser(), serLis.getEst_ser()
            });
        }
    }

    /**
     * Obtiene el código del servicio PATRON ITERATOR CLASE-LIBRERIA
     */
    int num_resp = 0;

    public int obtenerCodigoServicio() {
        num_resp = 0;
        List<Servicio> lista = null;
        lista = serDB.cargarCodigoServicio(lista);
        for (Iterator<Servicio> it = lista.iterator(); it.hasNext();) {
            Servicio ser = it.next();
            num_resp = Integer.parseInt(ser.getCod_ser()) + 1;
        }
        if (num_resp == 0) {
            num_resp = 1;
        }
        return num_resp;
    }

    /**
     * Bloquea los datos
     *
     * @param nada
     */
    private void Bloquear() {
        btnModificar.setEnabled(true);
        txtDescripcion.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnBuscar.setEnabled(false);
        txtBuscarServicio.setEnabled(false);
        txtCosto.setEnabled(false);
        rdMantenimiento.setEnabled(false);
        rdReparacion.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnDesactivar.setEnabled(true);
    }

    /**
     * Carga los datos del servicio a traves del ID
     *
     * @param id
     */
    private void cargaServicios(int id) {

        Servicio ser = serDB.traeServicio(id);
        txtcodigo.setText(ser.getCod_ser());
        txtDescripcion.setText(ser.getDes_ser());
        txtCosto.setText(String.valueOf(ser.getPre_ser()));

        if (ser.getTipo_ser().equals("MANTENIMIENTO")) {
            rdMantenimiento.setSelected(true);
        } else if (ser.getTipo_ser().equals("REPARACION")) {
            rdReparacion.setSelected(true);
        }
    }

    /**
     * Guarda los datos del servicio
     */
    private void Guardar() {
        Servicio ser = null;
        if (btnGuardar.getText().equals("GUARDAR")) {
            ser = serDB.traeServicios(txtDescripcion.getText());
            if (ser == null) {
                if (validar_llenos() == true) {
                    ser = new Servicio();
                    ser.setCod_ser(txtcodigo.getText());
                    ser.setPre_ser(Double.parseDouble(txtCosto.getText()));
                    ser.setDes_ser(txtDescripcion.getText());

                    if (rdMantenimiento.isSelected()) {
                        ser.setTipo_ser("Mantenimiento");
                    } else if (rdReparacion.isSelected()) {
                        ser.setTipo_ser("Reparacion");
                    }

                    ser.setEst_ser("A");
                    serDB.nuevoServicio(ser);
                } else {
                    JOptionPane.showMessageDialog(null, "LLENAR CAMPOS REQUERIDOS", "Mensaje", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "LA DESCRIPCIÓN DEL SERVICIO YA EXISTE EN EL SISTEMA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }

        } else {
            /**
             * Actualiza los datos del servicio
             */
            if (btnGuardar.getText().equals("ACTUALIZAR")) {
                ser = serDB.traeServicios(txtDescripcion.getText());
                if (ser == null) {
                    int selectRow = tablaServicios.getSelectedRow();
                    int idServ = Integer.parseInt(model.getValueAt(selectRow, 0).toString());
                    ser = serDB.traeServicio(idServ);
                    ser.setCod_ser(txtcodigo.getText());
                    ser.setPre_ser(Double.parseDouble(txtCosto.getText()));
                    ser.setDes_ser(txtDescripcion.getText());

                    if (rdMantenimiento.isSelected()) {
                        ser.setTipo_ser("Mantenimiento");
                    } else if (rdReparacion.isSelected()) {
                        ser.setTipo_ser("Reparacion");
                    }

                    serDB.actualizaServicio(ser);
                    JOptionPane.showMessageDialog(null, "DATOS ACTUALIZADOS", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "LA DESCRIPCIÓN DEL SERVICIO YA EXISTE EN EL SISTEMA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        inicio();
    }

    /**
     * Cambia el estado del servicio a pasivo
     */
    private void desactivar() {
        int sele_trow = tablaServicios.getSelectedRow();
        int si = JOptionPane.showConfirmDialog(this, "ESTÁ SEGURO DE DESACTIVAR EL SERVICIO", "Desactivar Servicio", JOptionPane.YES_NO_OPTION);
        if (si == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "NO SE HA PODIDO DESACTIVAR AL SERVICIO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int selec_trow = tablaServicios.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(model.getValueAt(selec_trow, 0)));
            Servicio serv = serDB.traeServicio(id);
            serv.setEst_ser("P");
            serDB.actualizaServicio(serv);
            JOptionPane.showMessageDialog(this, "SERVICIO DESACTIVADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            inicio();
        }
    }

    /**
     * Cambia el estado del servicio a Activo
     */
    private void activar() {
        int sele_trow = tablaServicios.getSelectedRow();

        int si = JOptionPane.showConfirmDialog(this, "DESEA ACTIVAR AL SERVICIO", "Activar Servicio", JOptionPane.YES_NO_OPTION);
        if (si == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "NO SE HA PODIDO ACTIVAR AL SERVICIO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int selec_trow = tablaServicios.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(model.getValueAt(selec_trow, 0)));
            Servicio serv = serDB.traeServicio(id);
            serv.setEst_ser("A");
            serDB.actualizaServicio(serv);
            JOptionPane.showMessageDialog(this, "SERVICIO ACTIVADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            inicio();
        }
    }

    /**
     * Valida que los campos no esten vacios
     *
     * @return
     */
    private boolean validar_llenos() {
        boolean lleno = true;
        boolean F = false;
        if (txtDescripcion.getText().equals("") || txtCosto.getText().equals("") || grupoTipo.isSelected(null)) {
            lleno = false;
        } else {
            lleno = true;
        }
        return lleno;
    }

    /**
     * Busca el Servicio a travez de su nombre
     *
     * @param nombre
     */
    private void buscaServicio(String nombre) {
        model.setNumRows(0);
        List<Servicio> lis = null;
        lis = serDB.buscarServicio(nombre, lis);

        if (lis.size() > 0) {
            for (Servicio serLis : lis) {
                model.addRow(new Object[]{
                    serLis.getIdServ(), serLis.getCod_ser(), serLis.getDes_ser(), serLis.getTipo_ser(), serLis.getPre_ser(), serLis.getEst_ser()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "SERVICIO NO ENCONTRADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscarServicio.requestFocus();
            llenaTabla("A");
        }
    }

    /**
     * Limpia y bloquea las cajas de texto
     */
    private void Cancelar() {
        btnBuscar.setEnabled(true);
        txtBuscarServicio.setEnabled(true);
        txtcodigo.setText("Auto");
        txtDescripcion.setText(null);
        txtCosto.setText(null);
        grupoTipo.clearSelection();
        btnNuevo.setEnabled(true);
        btnModificar.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnDesactivar.setEnabled(false);
        txtcodigo.setEnabled(false);
        txtDescripcion.setEnabled(false);
        txtCosto.setEnabled(false);
        rdMantenimiento.setEnabled(false);
        btnGuardar.setText("Guardar");
        rdReparacion.setEnabled(false);
        tablaServicios.setEnabled(true);
        chkdesactivados.setSelected(false);
        tablaModel();
        llenaTabla("A");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoTipo = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtBuscarServicio = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnBuscar = new rojerusan.RSMaterialButtonRound();
        chkdesactivados = new javax.swing.JCheckBox();
        btnDesactivar = new rojerusan.RSMaterialButtonRound();
        btnModificar = new rojerusan.RSMaterialButtonRound();
        btnNuevo = new rojerusan.RSMaterialButtonRound();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaServicios = new rojerusan.RSTableMetro();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtcodigo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCosto = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        rdMantenimiento = new javax.swing.JRadioButton();
        rdReparacion = new javax.swing.JRadioButton();
        btnCancelar = new rojerusan.RSMaterialButtonRound();
        btnGuardar = new rojerusan.RSMaterialButtonRound();
        jLabel8 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 3, 42)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(179, 21, 12));
        jLabel3.setText("SERVICIOS");

        txtBuscarServicio.setBackground(new java.awt.Color(255, 206, 206));
        txtBuscarServicio.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        txtBuscarServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarServicioActionPerformed(evt);
            }
        });
        txtBuscarServicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarServicioKeyTyped(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/lupa_Mesa de trabajo 1 copia 2.png"))); // NOI18N

        btnBuscar.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        chkdesactivados.setBackground(new java.awt.Color(255, 255, 255));
        chkdesactivados.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        chkdesactivados.setText("Desactivados");
        chkdesactivados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkdesactivadosActionPerformed(evt);
            }
        });

        btnDesactivar.setBackground(new java.awt.Color(179, 21, 12));
        btnDesactivar.setText("DESACTIVAR");
        btnDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(179, 21, 12));
        btnModificar.setText("MODIFICAR");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnNuevo.setBackground(new java.awt.Color(179, 21, 12));
        btnNuevo.setText("NUEVO");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        tablaServicios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CODIGO", "DESCRIPCION", "TIPO", "COSTO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaServicios.setColorBackgoundHead(new java.awt.Color(0, 193, 235));
        tablaServicios.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        tablaServicios.setColorBordeHead(new java.awt.Color(255, 255, 255));
        tablaServicios.setColorFilasBackgound2(new java.awt.Color(203, 203, 203));
        tablaServicios.setColorForegroundHead(new java.awt.Color(51, 51, 51));
        tablaServicios.setColorSelBackgound(new java.awt.Color(0, 193, 235));
        tablaServicios.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        tablaServicios.setFuenteFilas(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        tablaServicios.setFuenteFilasSelect(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        tablaServicios.setFuenteHead(new java.awt.Font("Copperplate Gothic Bold", 2, 12)); // NOI18N
        tablaServicios.setGrosorBordeFilas(0);
        tablaServicios.setGrosorBordeHead(0);
        tablaServicios.setMultipleSeleccion(false);
        tablaServicios.setRowHeight(20);
        tablaServicios.getTableHeader().setReorderingAllowed(false);
        tablaServicios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaServiciosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaServicios);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(chkdesactivados, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61)
                                .addComponent(btnDesactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(189, 189, 189))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtBuscarServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2))
                        .addGap(156, 156, 156))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtBuscarServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkdesactivados)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnDesactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Bahnschrift", 3, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(179, 21, 12));
        jLabel2.setText("DATOS SERVICIO");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel4.setText("CODIGO:");

        txtcodigo.setBackground(new java.awt.Color(255, 206, 206));
        txtcodigo.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtcodigo.setText("Auto");
        txtcodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcodigoActionPerformed(evt);
            }
        });
        txtcodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcodigoKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel5.setText("DESCRIPCION:");

        txtDescripcion.setBackground(new java.awt.Color(255, 206, 206));
        txtDescripcion.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescripcionActionPerformed(evt);
            }
        });
        txtDescripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel6.setText("COSTO:");

        txtCosto.setBackground(new java.awt.Color(255, 206, 206));
        txtCosto.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
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

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel7.setText("TIPO:");

        rdMantenimiento.setBackground(new java.awt.Color(255, 206, 206));
        grupoTipo.add(rdMantenimiento);
        rdMantenimiento.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        rdMantenimiento.setText("Mantenimiento");
        rdMantenimiento.setAlignmentY(0.8F);
        rdMantenimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdMantenimientoActionPerformed(evt);
            }
        });

        rdReparacion.setBackground(new java.awt.Color(255, 206, 206));
        grupoTipo.add(rdReparacion);
        rdReparacion.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        rdReparacion.setText("Reparacion");

        btnCancelar.setBackground(new java.awt.Color(179, 21, 12));
        btnCancelar.setText("CANCELAR");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(179, 21, 12));
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8_money_50px.png"))); // NOI18N

        btnSalir.setBackground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/btn salir_Mesa de trabajo 1 copia 2.png"))); // NOI18N
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtcodigo)
                            .addComponent(txtDescripcion)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(57, 57, 57)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(rdMantenimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rdReparacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtcodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8))
                .addGap(29, 29, 29)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(rdMantenimiento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rdReparacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 632, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void txtBuscarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarServicioActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtBuscarServicioActionPerformed

    private void txtBuscarServicioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarServicioKeyTyped
        // TODO add your handling code here:
        val.Mayusculas(txtBuscarServicio.getText(), evt);
        val.valLetr(evt, txtBuscarServicio, 100);

    }//GEN-LAST:event_txtBuscarServicioKeyTyped

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        if (txtBuscarServicio.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "LLENAR CAMPO REQUERIDO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            buscaServicio(txtBuscarServicio.getText());
            chkdesactivados.setSelected(false);
        }

    }//GEN-LAST:event_btnBuscarActionPerformed

    private void chkdesactivadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkdesactivadosActionPerformed
        // TODO add your handling code here:
        tablaModel();
        txtDescripcion.setText(null);
        txtCosto.setText(null);
        grupoTipo.clearSelection();
        if (chkdesactivados.isSelected() == true) {
            btnDesactivar.setText("DESACTIVAR");
            llenaTabla("P");
        } else {
            btnDesactivar.setText("DESACTIVAR");
            llenaTabla("A");
        }

    }//GEN-LAST:event_chkdesactivadosActionPerformed

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
        // TODO add your handling code here:
        if (btnDesactivar.getText().equals("DESACTIVAR")) {
            desactivar();
        } else {
            activar();
        }

    }//GEN-LAST:event_btnDesactivarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        btnGuardar.setEnabled(true);
        btnGuardar.setText("ACTUALIZAR");
        btnDesactivar.setEnabled(false);
        txtDescripcion.setEnabled(true);
        txtCosto.setEnabled(true);
        txtCosto.setEnabled(true);
        rdMantenimiento.setEnabled(true);
        rdReparacion.setEnabled(true);
        btnModificar.setEnabled(false);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        tablaServicios.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        txtDescripcion.setEnabled(true);
        txtCosto.setEnabled(true);
        rdMantenimiento.setEnabled(true);
        rdReparacion.setEnabled(true);
        txtBuscarServicio.setEnabled(false);
        btnBuscar.setEnabled(false);
        txtcodigo.setText(val.ObtenerCodString(obtenerCodigoServicio()));
        txtDescripcion.requestFocus();

    }//GEN-LAST:event_btnNuevoActionPerformed

    private void tablaServiciosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaServiciosMouseClicked
        if (chkdesactivados.isSelected()) {
            int selectRow = tablaServicios.getSelectedRow();
            int idServ = Integer.parseInt(model.getValueAt(selectRow, 0).toString());
            cargaServicios(idServ);
            btnDesactivar.setText("ACTIVAR");
            Bloquear();
        } else {
            int selectRow = tablaServicios.getSelectedRow();
            int idServ = Integer.parseInt(model.getValueAt(selectRow, 0).toString());
            cargaServicios(idServ);
            btnDesactivar.setText("DESACTIVAR");
            Bloquear();
        }
    }//GEN-LAST:event_tablaServiciosMouseClicked

    private void txtcodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcodigoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtcodigoActionPerformed

    private void txtcodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcodigoKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_txtcodigoKeyTyped

    private void txtDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionActionPerformed

    private void txtDescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionKeyTyped
        // TODO add your handling code here:
        val.Mayusculas(txtDescripcion.getText(), evt);
        val.EnterAJtexFiel(txtCosto, evt);
    }//GEN-LAST:event_txtDescripcionKeyTyped

    private void txtCostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoActionPerformed

    private void txtCostoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoKeyTyped
        // TODO add your handling code here:
        val.valNumReal(evt, txtCosto, 10);
    }//GEN-LAST:event_txtCostoKeyTyped

    private void rdMantenimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdMantenimientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdMantenimientoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        Cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        Guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

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
            java.util.logging.Logger.getLogger(frmServicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmServicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmServicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmServicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frmServicio dialog = new frmServicio(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnSalir;
    private javax.swing.JCheckBox chkdesactivados;
    private javax.swing.ButtonGroup grupoTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rdMantenimiento;
    private javax.swing.JRadioButton rdReparacion;
    private rojerusan.RSTableMetro tablaServicios;
    private javax.swing.JTextField txtBuscarServicio;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtcodigo;
    // End of variables declaration//GEN-END:variables
}
