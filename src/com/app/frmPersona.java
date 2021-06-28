/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app;

import com.controlador.PersonaDB;
import com.controlador.RolDB;
import com.controlador.Validaciones;
import com.entidades.Persona;
import com.entidades.Rol;
import java.awt.Color;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import rojerusan.necesario.RSScrollBar;

/**
 *
 * @author Usuario
 */
public class frmPersona extends javax.swing.JDialog {

    DefaultTableModel model;
    PersonaDB perDB = new PersonaDB();
    RolDB rolDB = new RolDB();
    Validaciones val = new Validaciones();

    /**
     * Creates new form frmPersona
     */
    public frmPersona(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        jScrollPane2.getVerticalScrollBar().setUI(new RSScrollBar());
        jScrollPane2.getViewport().setBackground(Color.WHITE);
        inicio();

    }

    private void inicio() {

        btnNuevo.setEnabled(true);
        btnGuardar.setText("Guardar");
        btnDesactivar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(true);
        chkdesactivados.setSelected(false);
        btnDesactivar.setText("Desactivar");
        tablaModel();
        llenaTabla("A");
        tablaClientes.setEnabled(true);
        txtcedula.setEnabled(false);
        txtapellido.setEnabled(false);
        txtnombre.setEnabled(false);
        txttelefono.setEnabled(false);
        txtdireccion.setEnabled(false);
        txtcorreo.setEnabled(false);
        rdMasculino.setEnabled(false);
        rdFemenino.setEnabled(false);
    }

    private void tablaModel() {
        tablaClientes.getColumnModel().getColumn(0).setPreferredWidth(200);
        tablaClientes.getColumnModel().getColumn(1).setPreferredWidth(320);
        tablaClientes.getColumnModel().getColumn(2).setPreferredWidth(320);
        tablaClientes.getColumnModel().getColumn(3).setPreferredWidth(200);
        tablaClientes.getColumnModel().getColumn(4).setPreferredWidth(350);
        model = (DefaultTableModel) tablaClientes.getModel();
        model.setNumRows(0);
    }

    /**
     * Presenta los datos en la Tabla1
     *
     * @param est
     */
    private void llenaTabla(String est) {
        List<Persona> lista = null;
        lista = perDB.cargaClientes(est, lista);
        for (Persona perLis : lista) {
            model.addRow(new Object[]{
                perLis.getCed_per(), perLis.getNom_per(),
                perLis.getApe_per(), perLis.getTel_per(),
                perLis.getDir_per()
            });
        }
    }

    /**
     * Valida los campos vacios de las Cajas de Textos
     *
     * @return
     */
    private boolean validar_llenos() {
        boolean lleno = true;

        if (txtcedula.getText().equals("") || txtnombre.getText().equals("") || txtapellido.getText().equals("")
                || grupoSexo.isSelected(null)) {
            lleno = false;
        } else {
            lleno = true;
        }
        return lleno;
    }

    /**
     * Guarda los datos de la Persona
     */
    private void guardar() {
        Persona per = null;
        if (btnGuardar.getText().equals("Guardar")) {
            per = perDB.traeClientes(txtcedula.getText());
            if (per == null) {
                if (validar_llenos() == true) {
                    per = new Persona();
                    Rol r = new Rol();
                    r = rolDB.traeRol("Cliente");
                    per.setRol(r);
                    per.setCed_per(txtcedula.getText().trim());
                    per.setApe_per(txtapellido.getText().trim());
                    per.setNom_per(txtnombre.getText().trim());
                    per.setDir_per(txtdireccion.getText().trim());
                    per.setTel_per(txttelefono.getText().trim());
                    per.setCorreo_per(txtcorreo.getText().trim());

                    if (rdMasculino.isSelected()) {
                        per.setSex_per("M");
                    } else {
                        per.setSex_per("F");
                    }
                    per.setEst_per("A");

                    r.getPersonas().add(per);
                    perDB.nuevoCliente(per);
                    JOptionPane.showMessageDialog(null, "CLIENTE GUARDADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "LLENAR CAMPOS REQUERIDOS", "Mensaje", JOptionPane.WARNING_MESSAGE);
                    inicio();
                }
            } else {
                JOptionPane.showMessageDialog(null, "EL NÚMERO DE CÉDULA YA EXISTE EN EL SISTEMA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            /**
             * Actualiza los datos del cliente
             */
            if (validar_llenos() == true) {
                per = new Persona();
                per = perDB.traeCliente(txtcedula.getText());
                per.setCed_per(txtcedula.getText().trim());
                per.setApe_per(txtapellido.getText().trim());
                per.setNom_per(txtnombre.getText().trim());
                per.setDir_per(txtdireccion.getText().trim());
                per.setCorreo_per(txtcorreo.getText().trim());
                per.setTel_per(txttelefono.getText().trim());
                per.setEst_per("A");

                if (rdMasculino.isSelected()) {
                    per.setSex_per("M");
                } else {
                    per.setSex_per("F");
                }
                perDB.actualizaCliente(per);
                JOptionPane.showMessageDialog(null, "DATOS ACTUALIZADOS", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                inicio();
            } else {
                JOptionPane.showMessageDialog(null, "LLENAR CAMPOS REQUERIDOS", "Mensaje", JOptionPane.WARNING_MESSAGE);
                inicio();
            }
        }
        inicio();
    }

    /**
     * Habilita campos para modificar al cliente
     */
    private void Editar() {
        btnNuevo.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnGuardar.setText("Actualizar");
        btnDesactivar.setEnabled(false);
        btnModificar.setEnabled(false);
        txtcedula.setEnabled(true);
        txtapellido.setEnabled(true);
        txtnombre.setEnabled(true);
        txttelefono.setEnabled(true);
        txtdireccion.setEnabled(true);
        txtcorreo.setEnabled(true);
        rdMasculino.setEnabled(true);
        rdFemenino.setEnabled(true);

    }

    /**
     * Desactiva al Cliente
     */
    private void desactivar() {
        int sele_trow = tablaClientes.getSelectedRow();
        String nom = String.valueOf(model.getValueAt(sele_trow, 1));
        String ape = String.valueOf(model.getValueAt(sele_trow, 2));

        String nombres = nom + " " + ape;
        int si = JOptionPane.showConfirmDialog(this, "ESTÁ SEGURO DE DESACTIVAR A: " + nombres, "Desactivar Cliente", JOptionPane.YES_NO_OPTION);

        if (si == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "NO SE HA PODIDO DESACTIVAR AL CLIENTE", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int selec_trow = tablaClientes.getSelectedRow();
            String ced = String.valueOf(model.getValueAt(selec_trow, 0));
            Persona per = perDB.traeCliente(ced);
            per.setEst_per("P");
            perDB.actualizaCliente(per);
            JOptionPane.showMessageDialog(this, "CLIENTE DESACTIVADO", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
            inicio();
        }
    }

    /**
     * Activa al Cliente
     */
    private void activar() {
        int sele_trow = tablaClientes.getSelectedRow();
        String nom = String.valueOf(model.getValueAt(sele_trow, 1));
        String ape = String.valueOf(model.getValueAt(sele_trow, 2));

        String nombres = nom + " " + ape;
        int si = JOptionPane.showConfirmDialog(this, "DESEA ACTIVAR A: " + nombres, "Activar Cliente", JOptionPane.YES_NO_OPTION);
        if (si == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "NO SE HA PODIDO ACTIVAR AL CLIENTE", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int selec_trow = tablaClientes.getSelectedRow();
            String ced = String.valueOf(model.getValueAt(selec_trow, 0));
            Persona per = perDB.traeCliente(ced);
            per.setEst_per("A");
            perDB.actualizaCliente(per);
            JOptionPane.showMessageDialog(this, "CLIENTE ACTIVADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            inicio();
        }
    }

    /**
     * Busca al cliente a travez de la Cédula
     */
    private void buscaCliente(String ced) {
        model.setNumRows(0);
        List<Persona> lis = null;
        lis = perDB.buscarPersona(ced, lis);

        if (lis.size() > 0) {
            for (Persona perLis : lis) {
                model.addRow(new Object[]{
                    perLis.getCed_per(), perLis.getNom_per(), perLis.getApe_per(),
                    perLis.getTel_per(), perLis.getDir_per()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "CLIENTE NO ENCONTRADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscarCedula.requestFocus();
            llenaTabla("A");
        }
    }

    /**
     * Carga los datos del cliente
     *
     * @param ced
     */
    private void cargaClientes(String ced) {
        Persona per = perDB.traeCliente(ced);
        txtcedula.setText(per.getCed_per());
        txtapellido.setText(per.getApe_per());
        txtnombre.setText(per.getNom_per());
        txtdireccion.setText(per.getDir_per());
        txttelefono.setText(per.getTel_per());
        txtcorreo.setText(per.getCorreo_per());

        if (per.getSex_per().equals("M")) {
            rdMasculino.setSelected(true);
        } else {
            rdFemenino.setSelected(true);
        }
    }

    private void bloquear() {
        btnModificar.setEnabled(true);
        btnNuevo.setEnabled(false);
        txtcedula.setEnabled(false);
        txtnombre.setEnabled(false);
        txtapellido.setEnabled(false);
        txtdireccion.setEnabled(false);
        txttelefono.setEnabled(false);
        txtcorreo.setEnabled(false);
        rdFemenino.setEnabled(false);
        rdMasculino.setEnabled(false);

        btnBuscar.setEnabled(false);
        txtBuscarCedula.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnDesactivar.setEnabled(true);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoSexo = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtBuscarCedula = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnDesactivar = new javax.swing.JButton();
        chkdesactivados = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaClientes = new rojerusan.RSTableMetro();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtdireccion = new javax.swing.JTextField();
        txttelefono = new javax.swing.JTextField();
        txtapellido = new javax.swing.JTextField();
        txtnombre = new javax.swing.JTextField();
        txtcedula = new javax.swing.JTextField();
        txtcorreo = new javax.swing.JTextField();
        rdMasculino = new javax.swing.JRadioButton();
        rdFemenino = new javax.swing.JRadioButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtBuscarCedula.setBackground(new java.awt.Color(255, 206, 206));
        txtBuscarCedula.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        txtBuscarCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarCedulaActionPerformed(evt);
            }
        });
        txtBuscarCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarCedulaKeyTyped(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/lupa_Mesa de trabajo 1 copia 2.png"))); // NOI18N

        btnBuscar.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscar.setFont(new java.awt.Font("Segoe UI Semibold", 3, 14)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnNuevo.setBackground(new java.awt.Color(179, 21, 12));
        btnNuevo.setFont(new java.awt.Font("Segoe UI Semibold", 3, 14)); // NOI18N
        btnNuevo.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevo.setText("NUEVO");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(179, 21, 12));
        btnModificar.setFont(new java.awt.Font("Segoe UI Semibold", 3, 14)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setText("MODIFICAR");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnDesactivar.setBackground(new java.awt.Color(179, 21, 12));
        btnDesactivar.setFont(new java.awt.Font("Segoe UI Semibold", 3, 14)); // NOI18N
        btnDesactivar.setForeground(new java.awt.Color(255, 255, 255));
        btnDesactivar.setText("DESACTIVAR");
        btnDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarActionPerformed(evt);
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

        tablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CÉDULA", "NOMBRES", "APELLIDOS", "TELEFONO", "DIRECCION"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaClientes.setColorBackgoundHead(new java.awt.Color(0, 193, 235));
        tablaClientes.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        tablaClientes.setColorBordeHead(new java.awt.Color(255, 255, 255));
        tablaClientes.setColorFilasBackgound2(new java.awt.Color(203, 203, 203));
        tablaClientes.setColorForegroundHead(new java.awt.Color(51, 51, 51));
        tablaClientes.setColorSelBackgound(new java.awt.Color(0, 193, 235));
        tablaClientes.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        tablaClientes.setFuenteFilas(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        tablaClientes.setFuenteFilasSelect(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        tablaClientes.setFuenteHead(new java.awt.Font("Copperplate Gothic Bold", 2, 12)); // NOI18N
        tablaClientes.setGrosorBordeFilas(0);
        tablaClientes.setGrosorBordeHead(0);
        tablaClientes.setMultipleSeleccion(false);
        tablaClientes.setRowHeight(20);
        tablaClientes.getTableHeader().setReorderingAllowed(false);
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaClientesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaClientes);

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 3, 42)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(179, 21, 12));
        jLabel3.setText("CLIENTES");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtBuscarCedula)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                        .addComponent(btnModificar)
                        .addGap(118, 118, 118)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDesactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkdesactivados, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(391, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtBuscarCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkdesactivados)
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDesactivar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(591, Short.MAX_VALUE)))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Bahnschrift", 3, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(179, 21, 12));
        jLabel2.setText("DATOS CLIENTE");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel4.setText("CEDULA:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel5.setText("NOMBRE:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel6.setText("APELLIDO:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel7.setText("DIRECCION:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel8.setText("TELEFONO:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel9.setText("CORREO:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel10.setText("SEXO:");

        btnGuardar.setBackground(new java.awt.Color(179, 21, 12));
        btnGuardar.setFont(new java.awt.Font("Segoe UI Semibold", 3, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(179, 21, 12));
        btnCancelar.setFont(new java.awt.Font("Segoe UI Semibold", 3, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("CANCELAR");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        txtdireccion.setBackground(new java.awt.Color(255, 206, 206));
        txtdireccion.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtdireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtdireccionKeyTyped(evt);
            }
        });

        txttelefono.setBackground(new java.awt.Color(255, 206, 206));
        txttelefono.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txttelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txttelefonoKeyTyped(evt);
            }
        });

        txtapellido.setBackground(new java.awt.Color(255, 206, 206));
        txtapellido.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtapellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtapellidoKeyTyped(evt);
            }
        });

        txtnombre.setBackground(new java.awt.Color(255, 206, 206));
        txtnombre.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtnombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnombreKeyTyped(evt);
            }
        });

        txtcedula.setBackground(new java.awt.Color(255, 206, 206));
        txtcedula.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtcedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcedulaActionPerformed(evt);
            }
        });
        txtcedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcedulaKeyTyped(evt);
            }
        });

        txtcorreo.setBackground(new java.awt.Color(255, 206, 206));
        txtcorreo.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtcorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcorreoKeyTyped(evt);
            }
        });

        rdMasculino.setBackground(new java.awt.Color(255, 206, 206));
        grupoSexo.add(rdMasculino);
        rdMasculino.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        rdMasculino.setText("Masculino");

        rdFemenino.setBackground(new java.awt.Color(255, 206, 206));
        grupoSexo.add(rdFemenino);
        rdFemenino.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        rdFemenino.setText("Femenino");

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
                .addGap(35, 35, 35)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtcedula, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtapellido, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8)
                                .addComponent(jLabel9))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(rdFemenino, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdMasculino, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(65, 65, 65)))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcedula, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtapellido, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabel10))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(rdMasculino)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rdFemenino)))
                .addGap(64, 64, 64)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        if (chkdesactivados.isSelected() == true) {
            llenaTabla("A");
            chkdesactivados.setSelected(false);
        }
        tablaClientes.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        txtBuscarCedula.setEnabled(true);
        txtBuscarCedula.setText(null);
        txtcedula.requestFocus();
        btnBuscar.setEnabled(false);
        //
        txtcedula.setText(null);
        txtapellido.setText(null);
        txtnombre.setText(null);
        txttelefono.setText(null);
        txtcorreo.setText(null);
        txtdireccion.setText(null);
        grupoSexo.clearSelection();
        chkdesactivados.setSelected(false);
        //
        txtcedula.setEnabled(true);
        txtapellido.setEnabled(true);
        txtnombre.setEnabled(true);
        txttelefono.setEnabled(true);
        txtdireccion.setEnabled(true);
        txtcorreo.setEnabled(true);
        rdMasculino.setEnabled(true);
        rdFemenino.setEnabled(true);


    }//GEN-LAST:event_btnNuevoActionPerformed

    private void chkdesactivadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkdesactivadosActionPerformed
        // TODO add your handling code here:
        txtnombre.setText(null);
        txtcedula.setText(null);
        txtapellido.setText(null);
        txtdireccion.setText(null);
        grupoSexo.clearSelection();
        txttelefono.setText(null);
        txtcorreo.setText(null);
        tablaModel();

        if (chkdesactivados.isSelected() == true) {
            btnDesactivar.setText("Desactivar");
            llenaTabla("P");
        } else {
            llenaTabla("A");
        }
    }//GEN-LAST:event_chkdesactivadosActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        tablaClientes.setEnabled(true);
        txtcedula.setEnabled(false);
        txtBuscarCedula.setEnabled(true);
        btnBuscar.setEnabled(true);
        txtBuscarCedula.setText(null);
        inicio();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        Editar();
        btnModificar.setEnabled(false);
        txtcedula.setEnabled(false);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void tablaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClientesMouseClicked
        // TODO add your handling code here:
        if (chkdesactivados.isSelected()) {
            String ced = String.valueOf(model.getValueAt(tablaClientes.getSelectedRow(), 0));
            cargaClientes(ced);
            btnDesactivar.setText("Activar");
            txtBuscarCedula.setText(null);
            bloquear();

        } else {
            String ced = String.valueOf(model.getValueAt(tablaClientes.getSelectedRow(), 0));
            cargaClientes(ced);
            btnDesactivar.setText("Desactivar");
            txtBuscarCedula.setText(null);
            bloquear();
        }
    }//GEN-LAST:event_tablaClientesMouseClicked

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
        // TODO add your handling code here:
        if (btnDesactivar.getText().equals("Desactivar")) {
            desactivar();
        } else {
            activar();
        }
    }//GEN-LAST:event_btnDesactivarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        if (txtBuscarCedula.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "LLENAR CAMPO REQUERIDO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscarCedula.requestFocus();
        } else {
            buscaCliente(txtBuscarCedula.getText());
            chkdesactivados.setSelected(false);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtcedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcedulaKeyTyped
        // TODO add your handling code here:
        val.valNum(evt, txtcedula, 10);
        val.EnterAJtexFiel(txtapellido, evt);
    }//GEN-LAST:event_txtcedulaKeyTyped

    private void txtnombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreKeyTyped
        // TODO add your handling code here:
        val.Mayusculas(txtnombre.getText(), evt);
        val.valLetr(evt, txtnombre, 50);
        val.EnterAJtexFiel(txtapellido, evt);
    }//GEN-LAST:event_txtnombreKeyTyped

    private void txtapellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtapellidoKeyTyped
        // TODO add your handling code here:
        val.Mayusculas(txtapellido.getText(), evt);
        val.valLetr(evt, txtapellido, 50);
        val.EnterAJtexFiel(txtdireccion, evt);
    }//GEN-LAST:event_txtapellidoKeyTyped

    private void txtdireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdireccionKeyTyped
        // TODO add your handling code here:
        val.Mayusculas(txtdireccion.getText(), evt);
        val.EnterAJtexFiel(txttelefono, evt);
    }//GEN-LAST:event_txtdireccionKeyTyped

    private void txttelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttelefonoKeyTyped
        // TODO add your handling code here:
        val.valNum(evt, txttelefono, 10);
        val.EnterAJtexFiel(txtcorreo, evt);
    }//GEN-LAST:event_txttelefonoKeyTyped

    private void txtcorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcorreoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcorreoKeyTyped

    private void txtBuscarCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarCedulaKeyTyped
        // TODO add your handling code here:
        val.valNum(evt, txtBuscarCedula, 10);
    }//GEN-LAST:event_txtBuscarCedulaKeyTyped

    private void txtcedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcedulaActionPerformed
        // TODO add your handling code here:
        val.validadorDeCedula(txtcedula.getText(), txtcedula);
    }//GEN-LAST:event_txtcedulaActionPerformed

    private void txtBuscarCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarCedulaActionPerformed
        // TODO add your handling code here:
        val.validadorDeCedula(txtBuscarCedula.getText(), txtBuscarCedula);
    }//GEN-LAST:event_txtBuscarCedulaActionPerformed

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
            java.util.logging.Logger.getLogger(frmPersona.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmPersona.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmPersona.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmPersona.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frmPersona dialog = new frmPersona(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDesactivar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JCheckBox chkdesactivados;
    private javax.swing.ButtonGroup grupoSexo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rdFemenino;
    private javax.swing.JRadioButton rdMasculino;
    private rojerusan.RSTableMetro tablaClientes;
    private javax.swing.JTextField txtBuscarCedula;
    private javax.swing.JTextField txtapellido;
    private javax.swing.JTextField txtcedula;
    private javax.swing.JTextField txtcorreo;
    private javax.swing.JTextField txtdireccion;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txttelefono;
    // End of variables declaration//GEN-END:variables
}
