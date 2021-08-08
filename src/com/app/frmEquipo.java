/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app;

import com.controlador.EquipoDB;
import com.controlador.MarcaDB;
import com.controlador.PersonaDB;
import com.controlador.Validaciones;
import com.entidades.Equipo;
import com.entidades.Marca;
import com.entidades.Persona;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class frmEquipo extends javax.swing.JDialog {

    /**
     * Importa las entidades y los controladores Instancias declaradas
     */
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel model2 = new DefaultTableModel();
    DefaultTableModel model3 = new DefaultTableModel();

    Validaciones vali = new Validaciones();
    EquipoDB equiDB = new EquipoDB();
    MarcaDB marcDB = new MarcaDB();
    PersonaDB persDB = new PersonaDB();
    Validaciones val = new Validaciones();
    Calendar cal = Calendar.getInstance();
    String opcion = "";

    DefaultTableModel modelPer;
    DefaultTableModel modelMarca;

    /**
     * Creates new form frmEquipo
     */
    public frmEquipo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        inicio();
    }

    /**
     * Carga los datos en la Tabla al iniciar
     */
    private void inicio() {
        txtCodigoEquipo.setText("Auto");
        tablaModel();
        tablaModelPer();
        llenaTablaEqui();
        llenaTablaPersona("A");
        llenaMarca("A");
        limpia_campos();
    }

    /**
     * Visualiza el dato con un tamaño establecido de los datos en las filas y
     * columnas de la tabla Equipo
     */
    public void tablaModel() {

        tablaEquipo.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaEquipo.getColumnModel().getColumn(0).setMinWidth(0);
        tablaEquipo.getColumnModel().getColumn(0).setPreferredWidth(0);
        model = (DefaultTableModel) tablaEquipo.getModel();
        model.setNumRows(0);
    }

    /**
     * Visualiza el dato con un tamaño establecido de los datos en las filas y
     * columnas de la tabla Persona
     */
    private void tablaModelPer() {
        TablaPersonas.getColumnModel().getColumn(0).setPreferredWidth(200);
        TablaPersonas.getColumnModel().getColumn(1).setPreferredWidth(300);
        TablaPersonas.getColumnModel().getColumn(2).setPreferredWidth(300);
        TablaPersonas.getColumnModel().getColumn(3).setPreferredWidth(400);
        TablaPersonas.getColumnModel().getColumn(4).setPreferredWidth(200);
        model2 = (DefaultTableModel) TablaPersonas.getModel();
        model2.setNumRows(0);
    }

    /**
     * Visualiza el dato con un tamaño establecido de los datos en las filas y
     * columnas de la tabla Marca
     */
    private void tablaModelMarca() {
        TablaMarca.getColumnModel().getColumn(0).setPreferredWidth(200);
        model3 = (DefaultTableModel) TablaMarca.getModel();
        model3.setNumRows(0);
    }

    /**
     * Trae los datos guardados de la base de datos y me los carga en la Tabla
     * para visualizar
     */
    public void llenaTablaEqui() {

        List<Equipo> lista = null;
        lista = equiDB.cargaEquipos(lista);
        for (Equipo equipoList : lista) {
            model.addRow(new Object[]{
                equipoList.getIdEquipo(), equipoList.getCod_equi(), equipoList.getMod_equi(),
                equipoList.getSer_equi(), equipoList.getTip_equi(), equipoList.getMarc().getNom_mar(),
                equipoList.getPersona().getNom_per() + " " + equipoList.getPersona().getApe_per()
            });
        }
    }

    /**
     * Llena los datos de la tabla con la información de la persona
     *
     * @param est
     */
    private void llenaTablaPersona(String est) {
        List<Persona> lista = null;
        lista = persDB.cargaClientes(est, lista);
        for (Persona perLis : lista) {
            model2.addRow(new Object[]{
                perLis.getCed_per(), perLis.getApe_per(),
                perLis.getNom_per(), perLis.getDir_per(),
                perLis.getTel_per()
            });
        }
    }

    /**
     * Llena las marcas con el estado Activo
     *
     * @param est
     */
    private void llenaMarca(String est) {
        tablaModelMarca();
        List<Marca> lista = null;
        lista = marcDB.cargaMarca(est, lista);

        for (Marca marLis : lista) {
            model3.addRow(new Object[]{
                marLis.getNom_mar()
            });
        }
    }

    /**
     * Me permite validar los campos vacios de los datos que son requeridos para
     * poder guardar o actualizar
     */
    private boolean ValidaLlenos() {
        boolean lleno = true;
        if (txtCedulaCliente.getText().equals("") || txtCodigoEquipo.getText().equals("") || txtModelo.getText().equals("")
                || txtNroSerie.getText().equals("")) {
            lleno = true;
        } else {
            lleno = false;
        }
        return lleno;
    }

    /**
     * Metodo que me permite guardar los datos ingresados en la Base de datos en
     * Entidad Equipo, Garantía.
     */
    private void guardar() {
        Equipo equi;
        Marca marc = new Marca();
        Persona pers = new Persona();

        if (btnGuardar.getText().equals("GUARDAR")) {
            equi = equiDB.traeEquipos(txtNroSerie.getText());
            if (equi == null) {
                if (ValidaLlenos() == false) {
                    equi = new Equipo();
                    equi.setPersona(persDB.traeClientes(txtCedulaCliente.getText()));
                    equi.setMarc(marcDB.traeMarcas(txtBuscarMarca.getText()));
                    equi.setCod_equi(txtCodigoEquipo.getText());
                    equi.setTip_equi(cboCategoria.getSelectedItem().toString());
                    equi.setMod_equi(txtModelo.getText());
                    equi.setSer_equi(txtNroSerie.getText());
                    equi.setCol_equi(txtColor.getText());

                    pers.getLista_Equipo().add(equi);
                    marc.getLista_Equipo().add(equi);
                    equiDB.nuevoEquipo(equi);

                } else {
                    JOptionPane.showMessageDialog(null, "LLENAR CAMPOS REQUERIDOS", "Mensaje", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "EL NÚMERO DE SERIE DEL EQUIPO YA EXISTE EN EL SISTEMA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }

        } else {

            /**
             * Me permite actualizar los datos del Equipo
             */
            if (btnGuardar.getText().equals("ACTUALIZAR")) {
                if (ValidaLlenos() == false) {
                    int selectrow = tablaEquipo.getSelectedRow();
                    int idequipo = Integer.parseInt(model.getValueAt(selectrow, 0).toString());

                    equi = equiDB.traeEquipo(idequipo);
                    equi.setPersona(persDB.traeClientes(txtCedulaCliente.getText()));
                    equi.setMarc(marcDB.traeMarcas(txtBuscarMarca.getText()));
                    equi.setCod_equi(txtCodigoEquipo.getText());
                    equi.setTip_equi(cboCategoria.getSelectedItem().toString());
                    equi.setMod_equi(txtModelo.getText());
                    equi.setSer_equi(txtNroSerie.getText());
                    equi.setCol_equi(txtColor.getText());

                    marc.getLista_Equipo().add(equi);
                    pers.getLista_Equipo().add(equi);

                    equiDB.actualizaEquipo(equi);

                } else {
                    JOptionPane.showMessageDialog(null, "LLENAR CAMPOS REQUERIDOS", "Mensaje", JOptionPane.WARNING_MESSAGE);
                }
            }

        }

        inicio();
    }

    /**
     * Obtiene el codigo del Equipo
     */
    int num_resp = 0;

    public int obtenerCodigoEquipo() {
        num_resp = 0;
        List<Equipo> lista = null;
        lista = equiDB.cargarCodigoEquipo(lista);
        for (Iterator<Equipo> it = lista.iterator(); it.hasNext();) {
            Equipo equi = it.next();
            num_resp = Integer.parseInt(equi.getCod_equi()) + 1;
        }
        if (num_resp == 0) {
            num_resp = 1;
        }
        return num_resp;
    }

    /**
     * Limpia los campos tanto en tabla y en las cajas de texto, Bloquea botones
     */
    private void HabilitaCamposEquipo() {
        tablaEquipo.setEnabled(false);
        btnBuscarCliente.setEnabled(true);
        txtBuscarEquipo.setEnabled(false);
        btnBuscar.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        txtNroSerie.setEnabled(true);
        txtColor.setEnabled(true);
        txtModelo.setEnabled(true);
        cboCategoria.setEnabled(true);
        txtCodigoEquipo.setText(vali.ObtenerCodString(obtenerCodigoEquipo()));
        btnGenerar.setEnabled(true);
        btnBuscarMarca.setEnabled(true);
    }

    /**
     * Desabilita campos tanto en botones como en cajas de Texto Cambia la
     * funcion del Boton Guardar por "Actualizar" para poder actualizar los
     * datos.
     */
    private void modificar() {

        opcion = "M";
        txtCodigoEquipo.setEnabled(false);
        btnModificar.setEnabled(false);
        btnGuardar.setText("ACTUALIZAR");
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnNuevo.setEnabled(false);
        btnBuscar.setEnabled(false);
        txtBuscarEquipo.setText(null);
        txtBuscarEquipo.setEnabled(false);
        cboCategoria.setEnabled(true);
        txtColor.setEnabled(true);
        txtModelo.setEnabled(true);
        txtCedulaCliente.setEnabled(true);
        txtNroSerie.setEnabled(false);
        btnGenerar.setEnabled(false);
        btnBuscarCliente.setEnabled(true);
        txtCedulaCliente.requestFocus();
        btnBuscarMarca.setEnabled(true);
    }

    /**
     * Limpia los campos tanto en tabla y en las cajas de texto, Bloquea botones
     */
    private void limpia_campos() {
        txtCodigoEquipo.setText("Auto");
        txtCodigoEquipo.setEnabled(false);
        txtColor.setText(null);
        txtModelo.setText(null);
        txtNroSerie.setText(null);
        txtColor.setEnabled(false);
        txtModelo.setEnabled(false);
        txtNroSerie.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnCancelar.setEnabled(false);
        btnBuscar.setEnabled(true);
        txtBuscarEquipo.setEnabled(true);
        cboCategoria.setEnabled(false);
        btnGuardar.setText("Guardar");
        btnModificar.setEnabled(false);
        txtCedulaCliente.setText(null);
        txtCliente.setText(null);
        btnBuscar.setEnabled(true);
        txtCedulaCliente.setEnabled(false);
        tablaEquipo.setEnabled(true);
        txtBuscarMarca.setText(null);
        btnBuscarMarca.setEnabled(false);
        cboCategoria.setEnabled(false);
    }

    /**
     * Busca los datos del Equipo por el Código del Equipo y los presenta en el
     * Panel de la Informacíon del Equipo
     */
    private void buscaEquipo(String codigo) {

        model.setNumRows(0);
        List<Equipo> lis = null;
        lis = equiDB.buscarEqui(codigo, lis);

        if (lis.size() > 0) {
            for (Equipo equipoList : lis) {
                model.addRow(new Object[]{
                    equipoList.getIdEquipo(), equipoList.getCod_equi(),
                    equipoList.getMod_equi(), equipoList.getSer_equi(), equipoList.getTip_equi(),
                    equipoList.getMarc().getNom_mar(), equipoList.getPersona().getNom_per() + " " + equipoList.getPersona().getApe_per()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "EQUIPO NO ENCONTRADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscarEquipo.requestFocus();
            llenaTablaEqui();
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

        frmVentanaMarca = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        btnSalirMarca = new rojerusan.RSMaterialButtonRound();
        btnCancelarMarca = new rojerusan.RSMaterialButtonRound();
        btnAceptarMarca = new rojerusan.RSMaterialButtonRound();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaMarca = new rojerusan.RSTableMetro();
        jLabel19 = new javax.swing.JLabel();
        txtBuscaMa = new javax.swing.JTextField();
        btnBuscarMarcaVentana = new rojerusan.RSMaterialButtonRound();
        frmVerPersonas = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TablaPersonas = new rojerusan.RSTableMetro();
        btnAgregarPer = new rojerusan.RSMaterialButtonRound();
        btnCancelarPer = new rojerusan.RSMaterialButtonRound();
        btnSalirPer = new rojerusan.RSMaterialButtonRound();
        jLabel16 = new javax.swing.JLabel();
        txtBuscaPers = new javax.swing.JTextField();
        btnBuscarPersonaVentana1 = new rojerusan.RSMaterialButtonRound();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaEquipo = new rojerusan.RSTableMetro();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCedulaCliente = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnBuscarCliente = new rojerusan.RSMaterialButtonRound();
        jLabel6 = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtCodigoEquipo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cboCategoria = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtNroSerie = new javax.swing.JTextField();
        btnBuscarMarca = new rojerusan.RSMaterialButtonRound();
        jLabel10 = new javax.swing.JLabel();
        txtBuscarMarca = new javax.swing.JTextField();
        btnGenerar = new rojerusan.RSMaterialButtonRound();
        jLabel11 = new javax.swing.JLabel();
        txtModelo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtColor = new javax.swing.JTextField();
        btnGuardar = new rojerusan.RSMaterialButtonRound();
        btnCancelar = new rojerusan.RSMaterialButtonRound();
        txtBuscarEquipo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnBuscar = new rojerusan.RSMaterialButtonRound();
        jLabel4 = new javax.swing.JLabel();
        btnNuevo = new rojerusan.RSMaterialButtonRound();
        btnModificar = new rojerusan.RSMaterialButtonRound();
        btnSalir = new javax.swing.JButton();

        frmVentanaMarca.setBackground(new java.awt.Color(255, 255, 255));
        frmVentanaMarca.setResizable(false);
        frmVentanaMarca.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        btnSalirMarca.setBackground(new java.awt.Color(179, 21, 12));
        btnSalirMarca.setText("salir");
        btnSalirMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirMarcaActionPerformed(evt);
            }
        });

        btnCancelarMarca.setBackground(new java.awt.Color(179, 21, 12));
        btnCancelarMarca.setText("Cancelar");
        btnCancelarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarMarcaActionPerformed(evt);
            }
        });

        btnAceptarMarca.setBackground(new java.awt.Color(179, 21, 12));
        btnAceptarMarca.setText("Aceptar");
        btnAceptarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarMarcaActionPerformed(evt);
            }
        });

        TablaMarca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NOMBRE MARCA"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TablaMarca.setColorBackgoundHead(new java.awt.Color(0, 193, 235));
        TablaMarca.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        TablaMarca.setColorBordeHead(new java.awt.Color(255, 255, 255));
        TablaMarca.setColorForegroundHead(new java.awt.Color(0, 0, 0));
        TablaMarca.setColorSelBackgound(new java.awt.Color(0, 193, 235));
        TablaMarca.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        TablaMarca.setFuenteFilas(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        TablaMarca.setFuenteFilasSelect(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        TablaMarca.setFuenteHead(new java.awt.Font("Copperplate Gothic Bold", 2, 12)); // NOI18N
        TablaMarca.setRowHeight(20);
        TablaMarca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaMarcaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TablaMarca);

        jLabel19.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel19.setText("NOMBRE MARCA:");

        txtBuscaMa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscaMaKeyTyped(evt);
            }
        });

        btnBuscarMarcaVentana.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscarMarcaVentana.setText("BUSCAR");
        btnBuscarMarcaVentana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarMarcaVentanaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(13, 13, 13)
                        .addComponent(txtBuscaMa, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarMarcaVentana, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnAceptarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(150, 150, 150)
                        .addComponent(btnCancelarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(btnSalirMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtBuscaMa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBuscarMarcaVentana, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAceptarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(btnSalirMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        frmVentanaMarca.getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 430, 300));

        frmVerPersonas.setBackground(new java.awt.Color(255, 255, 255));
        frmVerPersonas.setResizable(false);
        frmVerPersonas.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        TablaPersonas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CÉDULA", "APELLIDOS", "NOMBRES", "DIRECCION", "TELEFONO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TablaPersonas.setColorBackgoundHead(new java.awt.Color(0, 193, 235));
        TablaPersonas.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        TablaPersonas.setColorBordeHead(new java.awt.Color(255, 255, 255));
        TablaPersonas.setColorFilasBackgound2(new java.awt.Color(203, 203, 203));
        TablaPersonas.setColorForegroundHead(new java.awt.Color(51, 51, 51));
        TablaPersonas.setColorSelBackgound(new java.awt.Color(0, 193, 235));
        TablaPersonas.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        TablaPersonas.setFuenteFilas(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        TablaPersonas.setFuenteFilasSelect(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        TablaPersonas.setFuenteHead(new java.awt.Font("Copperplate Gothic Bold", 2, 12)); // NOI18N
        TablaPersonas.setGrosorBordeFilas(0);
        TablaPersonas.setGrosorBordeHead(0);
        TablaPersonas.setMultipleSeleccion(false);
        TablaPersonas.setRowHeight(20);
        TablaPersonas.getTableHeader().setReorderingAllowed(false);
        TablaPersonas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaPersonasMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(TablaPersonas);

        btnAgregarPer.setBackground(new java.awt.Color(179, 21, 12));
        btnAgregarPer.setText("aceptar");
        btnAgregarPer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarPerActionPerformed(evt);
            }
        });

        btnCancelarPer.setBackground(new java.awt.Color(179, 21, 12));
        btnCancelarPer.setText("cancelar");
        btnCancelarPer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarPerActionPerformed(evt);
            }
        });

        btnSalirPer.setBackground(new java.awt.Color(179, 21, 12));
        btnSalirPer.setText("salir");
        btnSalirPer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirPerActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel16.setText("Número de Cédula");

        txtBuscaPers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscaPersActionPerformed(evt);
            }
        });
        txtBuscaPers.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscaPersKeyTyped(evt);
            }
        });

        btnBuscarPersonaVentana1.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscarPersonaVentana1.setText("BUSCAR");
        btnBuscarPersonaVentana1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPersonaVentana1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(14, 14, 14)
                        .addComponent(txtBuscaPers, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnBuscarPersonaVentana1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(btnAgregarPer, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(133, 133, 133)
                .addComponent(btnCancelarPer, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalirPer, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscaPers, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarPersonaVentana1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalirPer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelarPer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarPer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        frmVerPersonas.getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 440));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tablaEquipo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CODIGO", "MODELO", "NRO.SERIE", "CATEGORIA", "MARCA", "CLIENTE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaEquipo.setColorBackgoundHead(new java.awt.Color(0, 193, 235));
        tablaEquipo.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        tablaEquipo.setColorBordeHead(new java.awt.Color(255, 255, 255));
        tablaEquipo.setColorFilasBackgound2(new java.awt.Color(203, 203, 203));
        tablaEquipo.setColorForegroundHead(new java.awt.Color(51, 51, 51));
        tablaEquipo.setColorSelBackgound(new java.awt.Color(0, 193, 235));
        tablaEquipo.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        tablaEquipo.setFuenteFilas(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        tablaEquipo.setFuenteFilasSelect(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        tablaEquipo.setFuenteHead(new java.awt.Font("Copperplate Gothic Bold", 2, 12)); // NOI18N
        tablaEquipo.setGrosorBordeFilas(0);
        tablaEquipo.setGrosorBordeHead(0);
        tablaEquipo.setMultipleSeleccion(false);
        tablaEquipo.setRowHeight(20);
        tablaEquipo.getTableHeader().setReorderingAllowed(false);
        tablaEquipo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEquipoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaEquipo);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 3, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(179, 21, 12));
        jLabel3.setText("INFORMACION DE EQUIPO");

        txtCedulaCliente.setBackground(new java.awt.Color(255, 206, 206));
        txtCedulaCliente.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtCedulaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaClienteActionPerformed(evt);
            }
        });
        txtCedulaCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaClienteKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel5.setText("CEDULA CLIENTE:");

        btnBuscarCliente.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscarCliente.setText("Buscar Cliente");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel6.setText("CLIENTE:");

        txtCliente.setBackground(new java.awt.Color(255, 206, 206));
        txtCliente.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClienteActionPerformed(evt);
            }
        });
        txtCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtClienteKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel7.setText("CODIGO DEL EQUIPO:");

        txtCodigoEquipo.setBackground(new java.awt.Color(255, 206, 206));
        txtCodigoEquipo.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtCodigoEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoEquipoActionPerformed(evt);
            }
        });
        txtCodigoEquipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoEquipoKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel8.setText("CATEGORIA:");

        cboCategoria.setBackground(new java.awt.Color(255, 206, 206));
        cboCategoria.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        cboCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Portatil", "CPU", "Monitor", "Impresora" }));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel9.setText("NRO.SERIE:");

        txtNroSerie.setBackground(new java.awt.Color(255, 206, 206));
        txtNroSerie.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtNroSerie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNroSerieActionPerformed(evt);
            }
        });
        txtNroSerie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNroSerieKeyTyped(evt);
            }
        });

        btnBuscarMarca.setBackground(new java.awt.Color(179, 21, 12));
        btnBuscarMarca.setText("Buscar Marca");
        btnBuscarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarMarcaActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel10.setText("MARCA:");

        txtBuscarMarca.setBackground(new java.awt.Color(255, 206, 206));
        txtBuscarMarca.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtBuscarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarMarcaActionPerformed(evt);
            }
        });
        txtBuscarMarca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarMarcaKeyTyped(evt);
            }
        });

        btnGenerar.setBackground(new java.awt.Color(179, 21, 12));
        btnGenerar.setText("GENERAR");
        btnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel11.setText("MODELO:");

        txtModelo.setBackground(new java.awt.Color(255, 206, 206));
        txtModelo.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModeloActionPerformed(evt);
            }
        });
        txtModelo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtModeloKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel12.setText("COLOR:");

        txtColor.setBackground(new java.awt.Color(255, 206, 206));
        txtColor.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        txtColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColorActionPerformed(evt);
            }
        });
        txtColor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtColorKeyTyped(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(0, 0, 0));
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(0, 0, 0));
        btnCancelar.setText("CANCELAR");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCedulaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtCliente))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCodigoEquipo))))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(154, 154, 154)
                        .addComponent(txtBuscarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(txtModelo)
                                .addContainerGap())
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(txtNroSerie)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addComponent(txtColor)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(339, 339, 339))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(600, Short.MAX_VALUE)
                    .addComponent(jLabel10)
                    .addGap(402, 402, 402)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCedulaCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBuscarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBuscarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtNroSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtCodigoEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(60, 60, 60)
                    .addComponent(jLabel10)
                    .addContainerGap(252, Short.MAX_VALUE)))
        );

        txtBuscarEquipo.setBackground(new java.awt.Color(255, 206, 206));
        txtBuscarEquipo.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        txtBuscarEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarEquipoActionPerformed(evt);
            }
        });
        txtBuscarEquipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarEquipoKeyTyped(evt);
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

        jLabel4.setFont(new java.awt.Font("Bahnschrift", 3, 42)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(179, 21, 12));
        jLabel4.setText("EQUIPOS");

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

        btnSalir.setBackground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/btn salir_Mesa de trabajo 1 copia 2.png"))); // NOI18N
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtBuscarEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(821, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(txtBuscarEquipo))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(720, Short.MAX_VALUE)))
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

    private void txtBuscarEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarEquipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarEquipoActionPerformed

    private void txtBuscarEquipoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEquipoKeyTyped
        // TODO add your handling code here:
        val.valNum(evt, txtBuscarEquipo, 6);

    }//GEN-LAST:event_txtBuscarEquipoKeyTyped

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        if (txtBuscarEquipo.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "INGRESE CÓDIGO DEL EQUIPO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            buscaEquipo(txtBuscarEquipo.getText());
        }

    }//GEN-LAST:event_btnBuscarActionPerformed

    /**
     * Carga los datos en la tabla al seleccionar una fila de la tabla El Metodo
     * se utiliza cuando el usuario requiera modificar algun dato.
     */
    private void cargarEquipos() {
        int selectRow = tablaEquipo.getSelectedRow();
        int idequipo = Integer.parseInt(model.getValueAt(selectRow, 0).toString());

        Equipo equ = equiDB.traeEquipo(idequipo);

        txtCedulaCliente.setText(equ.getPersona().getCed_per());
        txtCliente.setText(equ.getPersona().getNom_per() + " " + equ.getPersona().getApe_per());
        txtCodigoEquipo.setText(equ.getCod_equi());
        txtModelo.setText(equ.getMod_equi());
        txtColor.setText(equ.getCol_equi());
        txtNroSerie.setText(equ.getSer_equi());
        txtBuscaMa.setText(equ.getMarc().getNom_mar());
        cboCategoria.setSelectedItem(equ.getTip_equi());

        Bloquear(true);
    }

    /**
     * Bloquea las cajas de texto
     *
     * @param flag
     */
    private void Bloquear(boolean flag) {

        if (flag == true) {
            txtCedulaCliente.setEnabled(false);
            btnNuevo.setEnabled(false);
            btnBuscar.setEnabled(false);
            txtCodigoEquipo.setEnabled(false);
            txtModelo.setEnabled(false);
            txtBuscarMarca.setEnabled(false);
            txtModelo.setEnabled(false);
            txtColor.setEnabled(false);
            cboCategoria.setEnabled(false);
            btnBuscarCliente.setEnabled(false);
            btnBuscarMarca.setEnabled(false);
            btnGuardar.setEnabled(false);
            btnCancelar.setEnabled(true);
        }
    }


    private void tablaEquipoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEquipoMouseClicked
        cargarEquipos();
        btnNuevo.setEnabled(false);
        txtBuscarEquipo.setEnabled(false);
        btnBuscar.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnModificar.setEnabled(true);
    }//GEN-LAST:event_tablaEquipoMouseClicked

    private void txtCedulaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaClienteActionPerformed

    private void txtCedulaClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaClienteKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaClienteKeyTyped

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        tablaModelPer();
        llenaTablaPersona("A");
        txtBuscaPers.setText(null);
        frmVerPersonas.setTitle("LISTA DE CLIENTES");
        frmVerPersonas.setSize(700, 440);
        frmVerPersonas.setLocationRelativeTo(null);
        frmVerPersonas.setModal(true);
        frmVerPersonas.setVisible(true);
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void txtClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClienteActionPerformed

    private void txtClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClienteKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClienteKeyTyped

    private void txtCodigoEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoEquipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoEquipoActionPerformed

    private void txtCodigoEquipoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoEquipoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoEquipoKeyTyped

    private void txtNroSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNroSerieActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNroSerieActionPerformed

    private void txtNroSerieKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNroSerieKeyTyped
        // TODO add your handling code here:
        val.Mayusculas(txtNroSerie.getText(), evt);
        val.EnterAJtexFiel(txtColor, evt);
    }//GEN-LAST:event_txtNroSerieKeyTyped

    private void btnBuscarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarMarcaActionPerformed
        // TODO add your handling code here:
        tablaModelMarca();
        llenaMarca("A");
        txtBuscaMa.setText(null);
        txtBuscaMa.requestFocus();
        frmVentanaMarca.setTitle("LISTA DE MARCAS");
        frmVentanaMarca.setSize(400, 309);
        frmVentanaMarca.setLocationRelativeTo(null);
        frmVentanaMarca.setModal(true);
        frmVentanaMarca.setVisible(true);
    }//GEN-LAST:event_btnBuscarMarcaActionPerformed

    private void txtBuscarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarMarcaActionPerformed

    private void txtBuscarMarcaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarMarcaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarMarcaKeyTyped

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarActionPerformed
        // TODO add your handling code here:
        Random rnd = new Random();
        String abecedario = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String cadena = "";
        int m = 0, pos = 0, num;
        while (m < 1) {
            pos = (int) (rnd.nextDouble() * abecedario.length() - 1 + 0);
            num = (int) (rnd.nextDouble() * 9999 + 1000);
            cadena = cadena + abecedario.charAt(pos) + num;
            pos = (int) (rnd.nextDouble() * abecedario.length() - 1 + 0);
            cadena = cadena + abecedario.charAt(pos);
            txtNroSerie.setText(cadena);
            cadena = "";
            m++;
        }
    }//GEN-LAST:event_btnGenerarActionPerformed

    private void txtModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModeloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtModeloActionPerformed

    private void txtModeloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtModeloKeyTyped
        // TODO add your handling code here:
        val.Mayusculas(txtModelo.getText(), evt);
        val.EnterAJtexFiel(txtNroSerie, evt);
    }//GEN-LAST:event_txtModeloKeyTyped

    private void txtColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtColorActionPerformed

    private void txtColorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtColorKeyTyped
        // TODO add your handling code here:
        vali.Mayusculas(txtColor.getText(), evt);
        val.valLetr(evt, txtColor, 20);
    }//GEN-LAST:event_txtColorKeyTyped

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        tablaEquipo.setEnabled(true);
        btnBuscarCliente.setEnabled(false);
        cboCategoria.setEnabled(false);
        btnGenerar.setEnabled(false);
        btnNuevo.requestFocus();
        limpia_campos();
        inicio();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        HabilitaCamposEquipo();
        txtCedulaCliente.requestFocus();
        opcion = "N";
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        modificar();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtBuscaMaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaMaKeyTyped
        val.Mayusculas(txtBuscaMa.getText(), evt);
        val.valLetr(evt, txtBuscaMa, 20);
    }//GEN-LAST:event_txtBuscaMaKeyTyped

    private void txtBuscaPersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscaPersActionPerformed
        val.validadorDeCedula(txtBuscaPers.getText(), txtBuscaPers);
    }//GEN-LAST:event_txtBuscaPersActionPerformed

    private void txtBuscaPersKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaPersKeyTyped

        val.valNum(evt, txtBuscaPers, 10);
    }//GEN-LAST:event_txtBuscaPersKeyTyped

    private void btnBuscarMarcaVentanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarMarcaVentanaActionPerformed
        // TODO add your handling code here:
        if (txtBuscaMa.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "LLENAR CAMPO REQUERIDO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscaMa.requestFocus();
        } else {
            buscaMarca(txtBuscaMa.getText());
        }
    }//GEN-LAST:event_btnBuscarMarcaVentanaActionPerformed

    /**
     * Busca la marca a travez de la descripciòn
     */
    private void buscaMarca(String descrip) {
        model3.setNumRows(0);
        List<Marca> lis = null;
        String nomMar = txtBuscaMa.getText();

        lis = marcDB.buscarMarca(nomMar, lis);

        if (lis.size() > 0) {
            for (Marca marLis : lis) {
                model3.addRow(new Object[]{
                    marLis.getNom_mar()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "MARCA NO ENCONTRADA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscaMa.requestFocus();
            llenaMarca("A");
        }
    }


    private void btnCancelarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarMarcaActionPerformed
        // TODO add your handling code here:
        tablaModelMarca();
        txtBuscaMa.setText(null);
        llenaMarca("A");
    }//GEN-LAST:event_btnCancelarMarcaActionPerformed

    private void btnAceptarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarMarcaActionPerformed
        // TODO add your handling code here:
        int selectRow = TablaMarca.getSelectedRow();
        try {
            if (selectRow == -1) {
                JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA DE LA MARCA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            } else {
                modelMarca = (DefaultTableModel) TablaMarca.getModel();
                String nombre = TablaMarca.getValueAt(selectRow, 0).toString();
                txtBuscarMarca.setText(nombre);
                frmVentanaMarca.dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL AGREGAR LA MARCA" + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAceptarMarcaActionPerformed

    private void btnSalirMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirMarcaActionPerformed
        // TODO add your handling code here:

        frmVentanaMarca.dispose();
    }//GEN-LAST:event_btnSalirMarcaActionPerformed

    private void btnAgregarPerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarPerActionPerformed
        // TODO add your handling code here:
        int selectRow = TablaPersonas.getSelectedRow();
        try {
            if (selectRow == -1) {
                JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA DE LA TABLA", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            } else {
                modelPer = (DefaultTableModel) TablaPersonas.getModel();
                String ced = TablaPersonas.getValueAt(selectRow, 0).toString();
                String nombre = TablaPersonas.getValueAt(selectRow, 1).toString();
                String apellido = TablaPersonas.getValueAt(selectRow, 2).toString();
                txtCedulaCliente.setText(ced);
                txtCliente.setText(nombre + " " + apellido);
                frmVerPersonas.dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL AGREGAR EL EQUIPO " + e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAgregarPerActionPerformed

    private void btnBuscarPersonaVentana1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPersonaVentana1ActionPerformed
        // TODO add your handling code here:
        if (txtBuscaPers.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "LLENAR CAMPO REQUERIDO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscaPers.requestFocus();
        } else {
            buscaCliente(txtBuscaPers.getText());
        }
    }//GEN-LAST:event_btnBuscarPersonaVentana1ActionPerformed

    /**
     * Busca la Persona a travez de la Cédula
     */
    private void buscaCliente(String ced) {
        model2.setNumRows(0);

        List<Persona> lis = null;
        lis = persDB.buscarPersona(ced, lis);

        if (lis.size() > 0) {
            for (Persona perLis : lis) {
                model2.addRow(new Object[]{
                    perLis.getCed_per(), perLis.getNom_per(), perLis.getApe_per(),
                    perLis.getTel_per(), perLis.getDir_per()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "CLIENTE NO ENCONTRADO", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            txtBuscaPers.requestFocus();
            llenaTablaPersona("A");
        }
    }

    private void btnCancelarPerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarPerActionPerformed
        // TODO add your handling code here:
        tablaModelPer();
        txtBuscaPers.setText(null);
        llenaTablaPersona("A");
    }//GEN-LAST:event_btnCancelarPerActionPerformed

    private void btnSalirPerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirPerActionPerformed
        // TODO add your handling code here:
        frmVerPersonas.dispose();
    }//GEN-LAST:event_btnSalirPerActionPerformed

    private void TablaPersonasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaPersonasMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_TablaPersonasMouseClicked

    private void TablaMarcaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaMarcaMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_TablaMarcaMouseClicked

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
            java.util.logging.Logger.getLogger(frmEquipo.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmEquipo.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmEquipo.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmEquipo.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frmEquipo dialog = new frmEquipo(new javax.swing.JFrame(), true);
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
    private rojerusan.RSTableMetro TablaMarca;
    private rojerusan.RSTableMetro TablaPersonas;
    private rojerusan.RSMaterialButtonRound btnAceptarMarca;
    private rojerusan.RSMaterialButtonRound btnAgregarPer;
    private rojerusan.RSMaterialButtonRound btnBuscar;
    private rojerusan.RSMaterialButtonRound btnBuscarCliente;
    private rojerusan.RSMaterialButtonRound btnBuscarMarca;
    private rojerusan.RSMaterialButtonRound btnBuscarMarcaVentana;
    private rojerusan.RSMaterialButtonRound btnBuscarPersonaVentana1;
    private rojerusan.RSMaterialButtonRound btnCancelar;
    private rojerusan.RSMaterialButtonRound btnCancelarMarca;
    private rojerusan.RSMaterialButtonRound btnCancelarPer;
    private rojerusan.RSMaterialButtonRound btnGenerar;
    private rojerusan.RSMaterialButtonRound btnGuardar;
    private rojerusan.RSMaterialButtonRound btnModificar;
    private rojerusan.RSMaterialButtonRound btnNuevo;
    private javax.swing.JButton btnSalir;
    private rojerusan.RSMaterialButtonRound btnSalirMarca;
    private rojerusan.RSMaterialButtonRound btnSalirPer;
    private javax.swing.JComboBox<String> cboCategoria;
    private javax.swing.JDialog frmVentanaMarca;
    private javax.swing.JDialog frmVerPersonas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private rojerusan.RSTableMetro tablaEquipo;
    private javax.swing.JTextField txtBuscaMa;
    private javax.swing.JTextField txtBuscaPers;
    private javax.swing.JTextField txtBuscarEquipo;
    private javax.swing.JTextField txtBuscarMarca;
    private javax.swing.JTextField txtCedulaCliente;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtCodigoEquipo;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtNroSerie;
    // End of variables declaration//GEN-END:variables
}
