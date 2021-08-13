/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app;

import com.controlador.CuentaDB;
import com.controlador.PersonaDB;
import com.controlador.RolDB;
import com.controlador.Validaciones;
import com.entidades.Cuenta;
import com.entidades.Persona;
import com.entidades.Rol;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class frmCuenta extends javax.swing.JDialog {

    /**
     * Instancias
     */
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel modelo2 = new DefaultTableModel();
    DefaultTableModel modelPer;
    String opc = "";
    CuentaDB usuarioDB = new CuentaDB();
    PersonaDB personaDB = new PersonaDB();
    RolDB rolDB = new RolDB();
    Validaciones val = new Validaciones();

    /**
     * Creates new form frmCuenta
     */
    public frmCuenta(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        inicio();
    }

    /**
     * Inicio de la Aplicación
     */
    private void inicio() {
        llenaTabla("A");
        llenaTablaPersona("Cliente", "A");
        llenaRol();
        btnNuevo.setEnabled(true);
        btnGuardar.setText("GUARDAR");
        btnDesactivar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnGuardar.setEnabled(false);

        btnDesactivar.setText("DESACTIVAR");
        chkdesactivados.setSelected(false);
        btnCancelar.setEnabled(true);
        activar_persona(false);
        activar_usuario(false);
        tablaClientes.setEnabled(true);
        btnListaClientes.setEnabled(false);
        btnRergistrarNuevo.setEnabled(false);
        txtBuscarCedula.setEnabled(true);
        btnBuscar.setEnabled(true);
    }

    /**
     * Activa y Desactiva las cajas para que se pueda ingresar los datos del
     * usuario
     *
     * @param flag
     */
    private void activar_persona(boolean flag) {
        txtapellido.setEnabled(flag);
        txtcedula.setEnabled(flag);
        txtdireccion.setEnabled(flag);
        txtnombre.setEnabled(flag);
        txttelefono.setEnabled(flag);
        rdFemenino.setEnabled(flag);
        rdMasculino.setEnabled(flag);
        txtcorreo.setEnabled(flag);
        tablaClientes.setEnabled(flag);

        if (flag == false) {

            txtapellido.setText(null);
            txtcedula.setText(null);
            txtdireccion.setText(null);
            txtnombre.setText(null);
            txttelefono.setText(null);
            txtcorreo.setText(null);
            tipoSexo.clearSelection();
            tablaClientes.setEnabled(flag);
        }
    }

    /**
     * Activa el panel del Usuario
     *
     * @param flag
     */
    private void activar_usuario(boolean flag) {
        txtcontra.setEnabled(flag);
        cboRol.setEnabled(flag);
        btnGuardar.setEnabled(flag);
        if (flag == false) {
            txtcontra.setText(null);
        }
    }

    /**
     * Llena la Tabla del usuario con el estado Activo
     *
     * @param est
     */
    private void llenaTabla(String est) {
        tablaModel();
        List<Cuenta> listusu = null;
        listusu = usuarioDB.traeUsuarios(est, listusu);
        for (Cuenta u : listusu) {
            model.addRow(new Object[]{
                u.getId_cue(), u.getPersona().getCed_per(),
                u.getPersona().getNom_per(), u.getPersona().getApe_per(),
                u.getPersona().getRol().getDes_rol()
            });
        }
    }

    /**
     * Visualiza el dato con un tamaño establecido de los datos en las filas y
     * columnas de la tabla
     */
    private void tablaModel() {
        tablaClientes.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaClientes.getColumnModel().getColumn(0).setMinWidth(0);
        tablaClientes.getColumnModel().getColumn(0).setPreferredWidth(0);
        model = (DefaultTableModel) tablaClientes.getModel();
        model.setNumRows(0);
    }

    /**
     * Llena los datos de la tabla con la informacion de la persona
     *
     * @param est
     */
    private void llenaTablaPersona(String cliente, String est) {
        tablaModelPer();
        List<Persona> lista = null;
        lista = personaDB.traeCliente(cliente, est, lista);
        for (Persona perLis : lista) {
            modelo2.addRow(new Object[]{
                perLis.getCed_per(), perLis.getNom_per(),
                perLis.getApe_per(), perLis.getDir_per(),
                perLis.getTel_per()
            });
        }
    }

    /**
     * * Visualiza el dato con un tamaño establecido de los datos en las filas
     * y columnas de la tabla
     */
    private void tablaModelPer() {
        modelo2 = (DefaultTableModel) TablaP.getModel();
        modelo2.setNumRows(0);
    }

    /**
     * llena la lista desplegable con el rol
     */
    public void llenaRol() {
        cboRol.removeAllItems();
        List<Rol> lista = null;
        lista = rolDB.cargaRol(lista);
        for (Rol rolList : lista) {
            if (rolList.getId_rol() > 1) {
                cboRol.addItem(rolList.getDes_rol());
            }
        }
    }

    /**
     * Valida los campos vacios de las Cajas de Textos
     *
     * @return
     */
    private boolean validaCampos() {
        boolean lleno = true;
        if (txtcedula.getText().equals("") || txtnombre.getText().equals("") || txtapellido.getText().equals("")
                || tipoSexo.isSelected(null) || txtcontra.getText() == null) {
            lleno = false;
        } else {
            lleno = true;
        }
        return lleno;
    }

    /**
     * Guardar Usuario
     */
    private void guardar() {
        Cuenta usu;
        Rol ro;
        Persona per;

        if (btnGuardar.getText().equals("GUARDAR")) {
            usu = usuarioDB.traeClientes(txtcedula.getText());
            if (usu == null) {
                if (opc.equals("N")) {
                    usu = new Cuenta();
                    per = personaDB.traeCliente(txtcedula.getText());
                    usu.setPersona(per);
                    ro = rolDB.traeRol(cboRol.getSelectedItem().toString());
                    per.setRol(ro);
                    usu.setContra_usu(txtcontra.getText());
                    usu.setEst_usu("A");

                    personaDB.nuevoCliente(per);
                    usuarioDB.nuevoUsuario(usu);
                    ro.getPersonas().add(per);
                } else {
                    if (opc.equals("R")) {
                        per = personaDB.traeClientes(txtcedula.getText());
                        if (per == null) {
                            if (validaCampos() == true) {
                                Persona person = new Persona();
                                person.setCed_per(txtcedula.getText());
                                person.setApe_per(txtapellido.getText());
                                person.setNom_per(txtnombre.getText());
                                person.setDir_per(txtdireccion.getText());
                                person.setTel_per(txttelefono.getText());
                                person.setCorreo_per(txtcorreo.getText());
                                if (rdMasculino.isSelected()) {
                                    person.setSex_per("M");
                                } else {
                                    person.setSex_per("F");
                                }
                                person.setEst_per("A");
                                ro = rolDB.traeRol(cboRol.getSelectedItem().toString());
                                person.setRol(ro);
                                usu = new Cuenta();
                                usu.setPersona(person);
                                usu.setContra_usu(txtcontra.getText());
                                usu.setEst_usu("A");
                                personaDB.nuevoCliente(person);
                                usuarioDB.nuevoUsuario(usu);
                                ro.getPersonas().add(person);
                                activar_persona(false);

                            } else {
                                JOptionPane.showMessageDialog(null, "LLENAR CAMPOS REQUERIDOS", "Mensaje", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "EL NÚMERO DE CÉDULA YA SE ENCUENTRA REGISTRADO EN EL SISTEMA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "EL NÚMERO DE CÉDULA YA EXISTE EN EL SISTEMA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }

        } else {
            /**
             * Modificar datos del Usuario
             */
            usu = new Cuenta();
            per = personaDB.traeCliente(txtcedula.getText());
            usu.setPersona(per);

            ro = rolDB.traeRol(cboRol.getSelectedItem().toString());
            per.setRol(ro);

            int selectRow = tablaClientes.getSelectedRow();
            int ideusu = Integer.parseInt(model.getValueAt(selectRow, 0).toString());
            usu = usuarioDB.traeUsuario(ideusu);
            usu.setContra_usu(txtcontra.getText());
            usu.setEst_usu("A");

            ro.getPersonas().add(per);
            usuarioDB.actualizaCuenta(usu);
            personaDB.actualizaCliente(per);

            JOptionPane.showMessageDialog(null, "DATOS ACTUALIZADOS", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        }
        inicio();
    }

    /**
     * Limpia los datos de la caja de texto, bloquea botones
     */
    private void cancelar() {
        activar_persona(false);
        tablaClientes.setEnabled(true);
        btnBuscar.setEnabled(true);
        txtBuscarCedula.setEnabled(true);
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnDesactivar.setEnabled(false);
        txtcedula.setText(null);
        txtapellido.setText(null);
        txtdireccion.setText(null);
        txttelefono.setText(null);
        tipoSexo.clearSelection();
        cboRol.setEnabled(false);
        txtcontra.setEnabled(false);
        txtBuscarCedula.setEnabled(true);
        btnBuscar.setEnabled(true);
        txtnombre.setText(null);
        txtBuscarCedula.setText(null);
        txtcontra.setText(null);
        inicio();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frmUsuario = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        txtbuscaP = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        btnBuscarP = new rojerusan.RSMaterialButtonRound();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaP = new rojerusan.RSTableMetro();
        btnAceptar = new rojerusan.RSMaterialButtonRound();
        btnCan = new rojerusan.RSMaterialButtonRound();
        btnSale = new rojerusan.RSMaterialButtonRound();
        tipoSexo = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtBuscarCedula = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        chkdesactivados = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaClientes = new rojerusan.RSTableMetro();
        jLabel3 = new javax.swing.JLabel();
        btnModificar = new rojerusan.RSMaterialButtonRound();
        btnNuevo = new rojerusan.RSMaterialButtonRound();
        btnDesactivar = new rojerusan.RSMaterialButtonRound();
        btnBuscar = new rojerusan.RSMaterialButtonRound();
        btnListaClientes = new rojerusan.RSMaterialButtonRound();
        btnRergistrarNuevo = new rojerusan.RSMaterialButtonRound();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtdireccion = new javax.swing.JTextField();
        txttelefono = new javax.swing.JTextField();
        txtapellido = new javax.swing.JTextField();
        txtnombre = new javax.swing.JTextField();
        txtcedula = new javax.swing.JTextField();
        txtcorreo = new javax.swing.JTextField();
        rdMasculino = new javax.swing.JRadioButton();
        rdFemenino = new javax.swing.JRadioButton();
        btnSalir = new javax.swing.JButton();
        btnGuardar = new rojerusan.RSMaterialButtonRound();
        btnCancelar = new rojerusan.RSMaterialButtonRound();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtcontra = new javax.swing.JTextField();
        cboRol = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();

        frmUsuario.setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        txtbuscaP.setBackground(new java.awt.Color(255, 206, 206));
        txtbuscaP.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        txtbuscaP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtbuscaPActionPerformed(evt);
            }
        });
        txtbuscaP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtbuscaPKeyTyped(evt);
            }
        });

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/lupa_Mesa de trabajo 1 copia 2.png"))); // NOI18N

        btnBuscarP.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscarP.setText("BUSCAR");
        btnBuscarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPActionPerformed(evt);
            }
        });

        TablaP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CÉDULA", "NOMBRES", "APELLIDOS", "DIRECCION", "TELEFONO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaP.setColorBackgoundHead(new java.awt.Color(0, 193, 235));
        TablaP.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        TablaP.setColorBordeHead(new java.awt.Color(255, 255, 255));
        TablaP.setColorFilasBackgound2(new java.awt.Color(203, 203, 203));
        TablaP.setColorForegroundHead(new java.awt.Color(51, 51, 51));
        TablaP.setColorSelBackgound(new java.awt.Color(0, 193, 235));
        TablaP.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        TablaP.setFuenteFilas(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        TablaP.setFuenteFilasSelect(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        TablaP.setFuenteHead(new java.awt.Font("Copperplate Gothic Bold", 2, 12)); // NOI18N
        TablaP.setGrosorBordeFilas(0);
        TablaP.setGrosorBordeHead(0);
        TablaP.setMultipleSeleccion(false);
        TablaP.setRowHeight(20);
        TablaP.getTableHeader().setReorderingAllowed(false);
        TablaP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaPMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TablaP);

        btnAceptar.setBackground(new java.awt.Color(179, 21, 12));
        btnAceptar.setText("ACEPTAR");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        btnCan.setBackground(new java.awt.Color(179, 21, 12));
        btnCan.setText("CANCELAR");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });

        btnSale.setBackground(new java.awt.Color(179, 21, 12));
        btnSale.setText("SALIR");
        btnSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtbuscaP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscarP, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101)
                .addComponent(btnCan, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addComponent(btnSale, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtbuscaP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBuscarP, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCan, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSale, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout frmUsuarioLayout = new javax.swing.GroupLayout(frmUsuario.getContentPane());
        frmUsuario.getContentPane().setLayout(frmUsuarioLayout);
        frmUsuarioLayout.setHorizontalGroup(
            frmUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        frmUsuarioLayout.setVerticalGroup(
            frmUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

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
                "ID", "CÉDULA", "NOMBRES", "APELLIDOS", "ROL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
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
        jLabel3.setText("USUARIO");

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

        btnDesactivar.setBackground(new java.awt.Color(179, 21, 12));
        btnDesactivar.setText("DESACTIVAR");
        btnDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarActionPerformed(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnListaClientes.setBackground(new java.awt.Color(179, 21, 12));
        btnListaClientes.setText("LISTA/CLIENTES");
        btnListaClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListaClientesActionPerformed(evt);
            }
        });

        btnRergistrarNuevo.setBackground(new java.awt.Color(179, 21, 12));
        btnRergistrarNuevo.setText("REGISTRAR USUARIO");
        btnRergistrarNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRergistrarNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkdesactivados, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtBuscarCedula)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDesactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnListaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(btnRergistrarNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(391, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtBuscarCedula))
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnListaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRergistrarNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkdesactivados)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDesactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
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
        jLabel2.setText("DATOS USUARIO");

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
        tipoSexo.add(rdMasculino);
        rdMasculino.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        rdMasculino.setText("Masculino");

        rdFemenino.setBackground(new java.awt.Color(255, 206, 206));
        tipoSexo.add(rdFemenino);
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
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
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
                                    .addComponent(txtcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(rdMasculino, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(rdFemenino, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(rdMasculino)
                    .addComponent(rdFemenino))
                .addContainerGap(17, Short.MAX_VALUE))
        );

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

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel11.setText("CONTRASEÑA:");

        txtcontra.setBackground(new java.awt.Color(255, 206, 206));
        txtcontra.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtcontra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcontraKeyTyped(evt);
            }
        });

        cboRol.setBackground(new java.awt.Color(255, 206, 206));
        cboRol.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel12.setText("ROL:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboRol, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcontra, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtcontra, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboRol, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
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

    private void txtBuscarCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarCedulaActionPerformed
        // TODO add your handling code here:
        val.validadorDeCedula(txtBuscarCedula.getText(), txtBuscarCedula);
    }//GEN-LAST:event_txtBuscarCedulaActionPerformed

    private void txtBuscarCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarCedulaKeyTyped
        // TODO add your handling code here:
        val.valNum(evt, txtBuscarCedula, 10);
    }//GEN-LAST:event_txtBuscarCedulaKeyTyped

    private void chkdesactivadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkdesactivadosActionPerformed
        // TODO add your handling code here:
        txtnombre.setText(null);
        txtcedula.setText(null);
        txtapellido.setText(null);
        txtdireccion.setText(null);
        tipoSexo.clearSelection();
        txttelefono.setText(null);
        txtcorreo.setText(null);
        tablaModel();
        txtcontra.setText(null);

        if (chkdesactivados.isSelected() == true) {
            btnDesactivar.setText("DESACTIVAR");
            llenaTabla("P");
        } else {
            btnDesactivar.setText("DESACTIVAR");
            llenaTabla("A");
        }


    }//GEN-LAST:event_chkdesactivadosActionPerformed

    private void tablaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClientesMouseClicked
        // TODO add your handling code here:
        String ced = String.valueOf(model.getValueAt(tablaClientes.getSelectedRow(), 1));
        cargaPersonaTabla(ced);
        cargaUsuario();

        if (chkdesactivados.isSelected() == true) {
            btnDesactivar.setText("ACTIVAR");
            cargaPersonaTabla(ced);
            cargaUsuario();
            Bloquear(true);
        } else {
            btnDesactivar.setText("DESACTIVAR");
            cargaPersonaTabla(ced);
            cargaUsuario();
            Bloquear(true);
        }

    }//GEN-LAST:event_tablaClientesMouseClicked

    /**
     * Bloquea las cajas de texto
     *
     * @param flag
     */
    private void Bloquear(boolean flag) {

        if (flag == true) {
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
            txtcontra.setEnabled(false);
            cboRol.setEnabled(false);
        }
    }

    /**
     * Carga los dato de la persona en la Tabla
     *
     * @param ced
     */
    private void cargaPersonaTabla(String ced) {
        Persona per = null;
        per = personaDB.traeCliente(ced);
        txtcedula.setText(per.getCed_per());
        txtapellido.setText(per.getApe_per());
        txtdireccion.setText(per.getDir_per());
        txtnombre.setText(per.getNom_per());
        txttelefono.setText(per.getTel_per());
        txtcorreo.setText(per.getCorreo_per());
        if (per.getSex_per().equals("M")) {
            rdMasculino.setSelected(true);
        } else {
            rdFemenino.setSelected(true);
        }
        cboRol.setSelectedItem(per.getRol().getDes_rol());
    }

    /**
     * Carga los datos del Usuario
     */
    private void cargaUsuario() {

        int selectRow = tablaClientes.getSelectedRow();
        int ideusu = Integer.parseInt(model.getValueAt(selectRow, 0).toString());
        Cuenta usu = null;
        usu = usuarioDB.traeUsuario(ideusu);
        txtcontra.setText(usu.getContra_usu());
    }


    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        btnGuardar.setText("ACTUALIZAR");
        btnDesactivar.setEnabled(false);
        btnGuardar.setEnabled(true);
        txtcontra.setEnabled(true);
        cboRol.setEnabled(true);
        opc = "M";
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:

        if (chkdesactivados.isSelected() == true) {
            llenaTabla("A");
            chkdesactivados.setSelected(false);
        }
        btnListaClientes.setEnabled(true);
        btnRergistrarNuevo.setEnabled(true);
        btnNuevo.setEnabled(false);
        tablaClientes.setEnabled(false);
        txtBuscarCedula.setEnabled(false);
        btnBuscar.setEnabled(false);
        txtBuscarCedula.setText(null);

    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
        // TODO add your handling code here:
        if (btnDesactivar.getText().equals("DESACTIVAR")) {
            if (ultimoAdministrador() == false) {
                desactivar();
            }
        } else {
            activar();
        }

    }//GEN-LAST:event_btnDesactivarActionPerformed

    /**
     * Desactivar Ultimo ADMINISTRADOR
     */
    int cont = 0;

    private boolean ultimoAdministrador() {

        /**
         * Me sale cuando tengo a 2 gerentes jeje pero solo a
         */
        int selectRow = tablaClientes.getSelectedRow();
        boolean flag = false;
        cont = 0;
        for (int i = 0; i < tablaClientes.getRowCount(); i++) {
            if (tablaClientes.getValueAt(selectRow, 4).toString().equals("Administrador")) {
                cont++;
            }
        }
        if (cont == 1) {
            flag = true;
            JOptionPane.showMessageDialog(null, "NO SE PUEDE DESACTIVAR AL ULTIMO ADMINISTRADOR", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        }
        return flag;
    }

    /**
     * Desactivar Usuario
     */
    private void desactivar() {
        int sele_trow = tablaClientes.getSelectedRow();
        String ape = String.valueOf(model.getValueAt(sele_trow, 2));
        String nom = String.valueOf(model.getValueAt(sele_trow, 3));
        String nombres = nom + " " + ape;

        int si = JOptionPane.showConfirmDialog(this, "ESTÁ SEGURO DE DESACTIVAR A: " + nombres, "Desactivar Usuario", JOptionPane.YES_NO_OPTION);
        if (si == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "NO SE HA PODIDO DESACTIVAR AL USUARIO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int selec_trow = tablaClientes.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(model.getValueAt(selec_trow, 0)));
            Cuenta cuen = usuarioDB.traeUsuario(id);
            cuen.setEst_usu("P");
            usuarioDB.actualizaCuenta(cuen);
            JOptionPane.showMessageDialog(this, "USUARIO DESACTIVADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            inicio();
        }
    }

    /**
     * Activa al Usuario
     */
    private void activar() {
        int sele_trow = tablaClientes.getSelectedRow();
        String ape = String.valueOf(model.getValueAt(sele_trow, 2));
        String nom = String.valueOf(model.getValueAt(sele_trow, 3));
        String nombres = nom + " " + ape;
        int si = JOptionPane.showConfirmDialog(this, "DESEA ACTIVAR A: " + nombres, "Activar Usuario", JOptionPane.YES_NO_OPTION);
        if (si == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "NO SE HA PODIDO ACTIVAR AL USUARIO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int selec_trow = tablaClientes.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(model.getValueAt(selec_trow, 0)));
            Cuenta cuen = usuarioDB.traeUsuario(id);
            cuen.setEst_usu("A");
            usuarioDB.actualizaCuenta(cuen);
            JOptionPane.showMessageDialog(this, "USUARIO ACTIVADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            inicio();
        }
    }


    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        if (txtBuscarCedula.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "LLENAR CAMPO REQUERIDO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            buscaUsuario(txtBuscarCedula.getText());
            chkdesactivados.setSelected(false);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    /**
     * Busca al cliente a travez de la Cédula
     */
    private void buscaUsuario(String ced) {
        model.setNumRows(0);

        List<Cuenta> lis = null;
        lis = usuarioDB.buscarPersona(ced, lis);

        if (lis.size() > 0) {
            for (Cuenta usuLis : lis) {
                model.addRow(new Object[]{
                    usuLis.getId_cue(), usuLis.getPersona().getCed_per(),
                    usuLis.getPersona().getNom_per(), usuLis.getPersona().getApe_per(),
                    usuLis.getPersona().getRol().getDes_rol()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "USUARIO NO ENCONTRADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscarCedula.requestFocus();
            llenaTabla("A");
        }
    }


    private void txtdireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdireccionKeyTyped
        // TODO add your handling code here:
        val.Mayusculas(txtdireccion.getText(), evt);
        val.valLetr(evt, txtdireccion, 100);
        val.EnterAJtexFiel(txttelefono, evt);
    }//GEN-LAST:event_txtdireccionKeyTyped

    private void txttelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttelefonoKeyTyped
        // TODO add your handling code here:
        val.valNum(evt, txttelefono, 10);
    }//GEN-LAST:event_txttelefonoKeyTyped

    private void txtapellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtapellidoKeyTyped
        // TODO add your handling code here:
        val.Mayusculas(txtapellido.getText(), evt);
        val.valLetr(evt, txtapellido, 50);
        val.EnterAJtexFiel(txtnombre, evt);
    }//GEN-LAST:event_txtapellidoKeyTyped

    private void txtnombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreKeyTyped
        // TODO add your handling code here:
        val.Mayusculas(txtnombre.getText(), evt);
        val.valLetr(evt, txtnombre, 50);
        val.EnterAJtexFiel(txtdireccion, evt);
    }//GEN-LAST:event_txtnombreKeyTyped

    private void txtcedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcedulaActionPerformed
        // TODO add your handling code here:
        val.validadorDeCedula(txtcedula.getText(), txtcedula);

    }//GEN-LAST:event_txtcedulaActionPerformed

    private void txtcedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcedulaKeyTyped
        // TODO add your handling code here:
        val.valNum(evt, txtcedula, 10);
        val.EnterAJtexFiel(txtapellido, evt);
    }//GEN-LAST:event_txtcedulaKeyTyped

    private void txtcorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcorreoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcorreoKeyTyped

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtcontraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcontraKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcontraKeyTyped

    private void btnListaClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListaClientesActionPerformed
        // TODO add your handling code here:
        tablaModelPer();
        llenaTablaPersona("Cliente", "A");
        frmUsuario.setTitle("LISTA DE CLIENTES");
        frmUsuario.setSize(713, 489);
        frmUsuario.setLocationRelativeTo(null);
        frmUsuario.setModal(true);
        frmUsuario.setVisible(true);
        txtbuscaP.setText(null);
        txtbuscaP.requestFocus();
    }//GEN-LAST:event_btnListaClientesActionPerformed

    private void btnRergistrarNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRergistrarNuevoActionPerformed
        // TODO add your handling code here:
        opc = "R";
        limpiaCampos();
        activar_persona(true);
        activar_usuario(true);
        btnListaClientes.setEnabled(false);
        txtcedula.requestFocus();
    }//GEN-LAST:event_btnRergistrarNuevoActionPerformed

    /**
     * Limpia los campos al presionar el boton Nuevo Usuario
     */
    private void limpiaCampos() {
        txtcedula.setText(null);
        txtnombre.setText(null);
        txtapellido.setText(null);
        txtdireccion.setText(null);
        txttelefono.setText(null);
        tipoSexo.clearSelection();
        txtcorreo.setText(null);

    }

    private void txtbuscaPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbuscaPActionPerformed
        // TODO add your handling code here:
        val.validadorDeCedula(txtbuscaP.getText(), txtbuscaP);
    }//GEN-LAST:event_txtbuscaPActionPerformed

    private void txtbuscaPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbuscaPKeyTyped
        // TODO add your handling code here:
        val.valNum(evt, txtbuscaP, 10);
    }//GEN-LAST:event_txtbuscaPKeyTyped

    private void btnBuscarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPActionPerformed
        // TODO add your handling code here:
        if (txtbuscaP.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "LLENAR CAMPO REQUERIDO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtbuscaP.requestFocus();
        } else {
            buscaPersona(txtbuscaP.getText());
        }
    }//GEN-LAST:event_btnBuscarPActionPerformed

    /**
     * Busca la Persona a travez de la Cédula
     */
    private void buscaPersona(String ced) {
        modelo2.setNumRows(0);

        List<Persona> lis = null;
        lis = personaDB.buscarPersona(ced, lis);

        if (lis.size() > 0) {
            for (Persona perLis : lis) {
                modelo2.addRow(new Object[]{
                    perLis.getCed_per(), perLis.getNom_per(), perLis.getApe_per(),
                    perLis.getTel_per(), perLis.getDir_per()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "CLIENTE NO ENCONTRADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtbuscaP.requestFocus();
            llenaTablaPersona("Cliente", "A");
        }
    }

    private void TablaPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaPMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TablaPMouseClicked

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        // TODO add your handling code here:
        int selectRow = TablaP.getSelectedRow();
        try {
            if (selectRow == -1) {
                JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA DE LA TABLA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            } else {
                modelPer = (DefaultTableModel) TablaP.getModel();
                String ced = TablaP.getValueAt(selectRow, 0).toString();
                txtcedula.setText(ced);

                if (txtcedula.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "LLENAR CAMPO REQUERIDO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    cargaPersona(txtcedula.getText());
                    opc = "N";
                }
                frmUsuario.dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL AGREGAR USUARIO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAceptarActionPerformed

    /**
     * Busca a la Persona a travez de la Cédula
     */
    private void cargaPersona(String ced) {

        Persona per = personaDB.traeClientes(ced);

        if (per != null) {
            if (per.getCed_per().equals(txtcedula.getText().trim())) {
                txtcedula.setText(per.getCed_per());
                txtapellido.setText(per.getApe_per());
                txtdireccion.setText(per.getDir_per());
                txtnombre.setText(per.getNom_per());
                txttelefono.setText(per.getTel_per());
                txtcorreo.setText(per.getCorreo_per());
                cboRol.setSelectedItem(per.getRol().getDes_rol());
            }
            if (per.getSex_per().equals("M")) {
                rdMasculino.setSelected(true);
            } else {
                rdFemenino.setSelected(true);
            }
            activar_usuario(true);

        } else {
            JOptionPane.showMessageDialog(null, "CÉDULA NO ENCONTRADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        // TODO add your handling code here:
        tablaModelPer();
        txtbuscaP.setText(null);
        llenaTablaPersona("Cliente", "A");
    }//GEN-LAST:event_btnCanActionPerformed

    private void btnSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleActionPerformed
        // TODO add your handling code here:
        frmUsuario.dispose();
    }//GEN-LAST:event_btnSaleActionPerformed

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
            java.util.logging.Logger.getLogger(frmCuenta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmCuenta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmCuenta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmCuenta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frmCuenta dialog = new frmCuenta(new javax.swing.JFrame(), true);
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
    private rojerusan.RSTableMetro TablaP;
    private rojerusan.RSMaterialButtonRound btnAceptar;
    private rojerusan.RSMaterialButtonRound btnBuscar;
    private rojerusan.RSMaterialButtonRound btnBuscarP;
    private rojerusan.RSMaterialButtonRound btnCan;
    private rojerusan.RSMaterialButtonRound btnCancelar;
    private rojerusan.RSMaterialButtonRound btnDesactivar;
    private rojerusan.RSMaterialButtonRound btnGuardar;
    private rojerusan.RSMaterialButtonRound btnListaClientes;
    private rojerusan.RSMaterialButtonRound btnModificar;
    private rojerusan.RSMaterialButtonRound btnNuevo;
    private rojerusan.RSMaterialButtonRound btnRergistrarNuevo;
    private rojerusan.RSMaterialButtonRound btnSale;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cboRol;
    private javax.swing.JCheckBox chkdesactivados;
    private javax.swing.JDialog frmUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JRadioButton rdFemenino;
    private javax.swing.JRadioButton rdMasculino;
    private rojerusan.RSTableMetro tablaClientes;
    private javax.swing.ButtonGroup tipoSexo;
    private javax.swing.JTextField txtBuscarCedula;
    private javax.swing.JTextField txtapellido;
    private javax.swing.JTextField txtbuscaP;
    private javax.swing.JTextField txtcedula;
    private javax.swing.JTextField txtcontra;
    private javax.swing.JTextField txtcorreo;
    private javax.swing.JTextField txtdireccion;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txttelefono;
    // End of variables declaration//GEN-END:variables
}
