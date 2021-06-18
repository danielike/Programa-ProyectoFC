/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modificacion;

import alta.AltaColeccion;
import alta.AltaComic;
import baja.BajaColeccion;
import baja.BajaComic;
import clases.Coleccion;
import clases.Comic;
import clases.DocumentoLongitudRestringida;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import clases.Conexion;
import clases.FtpClient;
import com.mysql.cj.conf.PropertyKey;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.Blob;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.concurrent.ArrayBlockingQueue;
import javax.swing.Icon;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import ventanaInformes.InformesColecciones;
import ventanaInformes.InformesComics;

/**
 *
 * @author Dani
 */
public class ModificacionComic extends javax.swing.JInternalFrame implements 
        KeyListener, InternalFrameListener{

    private File file;
    private Blob imagen;
    private Image imagenABD;
    private FtpClient cliente;
    /**
     * Creates new form ModificacionComic
     */
    public ModificacionComic() {
        setSize(1000, 1000);
        initComponents();
        txtTitulo.setDocument(new DocumentoLongitudRestringida(50));
        cmbComic.setModel(modeloComics);
        cmbColeccion.setModel(modeloColecciones);
        rellenarComics();
        rellenarColecciones();
        if(cmbComic.getSelectedIndex() != -1){
            Comic comic = (Comic) cmbComic.getSelectedItem();
            colocarCampos(comic);
        }
        txtTitulo.addKeyListener(this);
        addInternalFrameListener(this);
        try {
            cliente = new FtpClient();
            cliente.open();
            System.out.println("Abierta la conexion a ftpserver");
        } catch (IOException ex) {
            Logger.getLogger(ModificacionComic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/**
     * rellena el combobox de colecciones
     */
    public static void rellenarColecciones(){
        modeloColecciones.removeAllElements();
        try {
            
            Statement stm = Conexion.getConexion().createStatement();
            ResultSet rs = stm.executeQuery("select id, nombre, sinopsis from "
                    + "colecciones order by nombre");
            while(rs.next()){
                Coleccion coleccion = new Coleccion(rs.getInt(1), rs.getString(2),
                rs.getString(3));
                modeloColecciones.addElement(coleccion);
            }
        } catch (SQLException e) {
            System.out.println(""+e.getErrorCode());
            e.printStackTrace();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    /**
     * rellena el combobox de comics
     */
    public static void rellenarComics(){
        modeloComics.removeAllElements();
        try {
            Statement stm = Conexion.getConexion().createStatement();
            ResultSet rs = stm.executeQuery("select id, titulo, fecha_adquisicion,"
                    + "numero_coleccion, precio, portada, estado, id_coleccion, "
                    + "url_imagen from comics order by titulo");
            while(rs.next()){
                Comic comic = new Comic(rs.getInt(1), rs.getString(2),
                new Date(rs.getDate(3).getTime()), rs.getInt(4), rs.getFloat(5), rs.getBlob(6),
                        rs.getString(7), rs.getInt(8),rs.getString(9));
                modeloComics.addElement(comic);
                System.out.println("Obteniendo comics...");
            }
        } catch (SQLException e) {
            System.out.println(""+e.getErrorCode());
        }
    }
    /**
     * convierte una imagen en formato blob a Image
     * @param imagen
     * @return Image: imagen transformada
     */
    public Image obtenerImagen(Blob imagen){
            Image img = null;
        try {
            byte[] data = imagen.getBytes(1, (int) imagen.length());
            img = ImageIO.read(new ByteArrayInputStream(data));
        } catch (SQLException ex) {
            Logger.getLogger(ModificacionComic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ModificacionComic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return img;
    }
    /**
     * coloca los campos de comic en el formulario
     * @param comic 
     */
    private void colocarCampos(Comic comic) {
        try {
            txtTitulo.setText(comic.getTitulo());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(comic.getFecha_adquisicion());
            jdateChooser.setCalendar(calendar);
            spnNumColeccion.setValue(comic.getNum_coleccion());
            cmbEstado.setSelectedItem(comic.getEstado());
            spnPrecio.setValue(comic.getPrecio());
            imagen = comic.getPortada();
            imagenABD = obtenerImagen(imagen);
            if(comic.getPortada() != null){
                lblImagen.setIcon(new ImageIcon(comic.getPortada().getBytes(1, (int)
                    comic.getPortada().length())));
            }
            Coleccion coleccion = null;
            /*recorremos el modelo en busca de la coleccion coincidente*/
            for (int i = 0; i < modeloColecciones.getSize(); i++) {
                coleccion = modeloColecciones.getElementAt(i);
               if(coleccion.getId() == comic.getId_coleccion()){
                   cmbColeccion.setSelectedIndex(i);
                   return;
               }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModificacionComic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * limpia los campos del formulario
     */
    private void limpiar(){
        txtTitulo.setText("");
        spnNumColeccion.setValue(0);
        jdateChooser.setCalendar(null);
        spnPrecio.setValue(0);
        cmbEstado.setSelectedIndex(0);
        lblImagen.setIcon(null);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        cmbComic = new javax.swing.JComboBox<>();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtTitulo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jdateChooser = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        spnNumColeccion = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblImagen = new javax.swing.JLabel();
        btnCambiar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        spnPrecio = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbColeccion = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Modificación Comic");
        setToolTipText("");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Comic : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel1, gridBagConstraints);

        cmbComic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbComicActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(cmbComic, gridBagConstraints);

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(btnAceptar, gridBagConstraints);

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(btnCancelar, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(txtTitulo, gridBagConstraints);

        jLabel2.setText("Fecha adquisición");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jdateChooser, gridBagConstraints);

        jLabel3.setText("Estado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel3, gridBagConstraints);

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BUENO", "REGULAR", "MALO", "PERFECTO" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(cmbEstado, gridBagConstraints);

        jLabel4.setText("Num. Colección");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel4, gridBagConstraints);

        spnNumColeccion.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(spnNumColeccion, gridBagConstraints);

        jLabel5.setText("Título");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel6, gridBagConstraints);

        lblImagen.setText("Imagen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(lblImagen, gridBagConstraints);

        btnCambiar.setText("Cambiar");
        btnCambiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(btnCambiar, gridBagConstraints);

        jLabel7.setText("Precio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel7, gridBagConstraints);

        spnPrecio.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(9999.9f), Float.valueOf(0.1f)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(spnPrecio, gridBagConstraints);

        jLabel8.setText("€");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel8, gridBagConstraints);

        jLabel9.setText("Colección");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel9, gridBagConstraints);

        cmbColeccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbColeccionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(cmbColeccion, gridBagConstraints);

        setBounds(0, 0, 1315, 527);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed
/**
     * actualiza los datos del formulario
     * @param posSeleccionada 
     */
    private void actualizarComic(int posSeleccionada){
        try {
            int id = modeloComics.getElementAt
                    (posSeleccionada).getId();
            Statement stm = Conexion.getConexion().createStatement();
            String consulta = "select titulo, fecha_adquisicion, numero_coleccion,"
                    + "precio,portada,estado,id_coleccion "
                    + "from comics where id=" + id;
            ResultSet rs = stm.executeQuery(consulta);
            while(rs.next()){
                Comic comic = new Comic(id, rs.getString(1), rs.getDate(2), 
                        rs.getInt(3), rs.getFloat(4), rs.getBlob(5), rs.getString(6), 
                        rs.getInt(7));
                modeloComics.removeElementAt(posSeleccionada);
                modeloComics.insertElementAt(comic, posSeleccionada);
            }
        } catch (SQLException e) {
            System.out.println(""+e.getErrorCode());
            e.printStackTrace();
            return;
        }
        cmbComic.setSelectedIndex(posSeleccionada);
        colocarCampos((Comic) cmbComic.getSelectedItem());
        
    }
    private void btnCambiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarActionPerformed
        // muesta filechooser y cambia imagen
        // abre filechooser
        JFileChooser filechooser = new JFileChooser();
        filechooser.setDialogType(JFileChooser.SAVE_DIALOG);
        filechooser.setDialogTitle("Guardar");
        FileFilter filtro = new FileNameExtensionFilter("imágenes (.jpeg, .jpg,"
                + " .png)", new String[]{"png","jpeg","jpg"});
        filechooser.setFileFilter(filtro);
        filechooser.setAcceptAllFileFilterUsed(false);
        filechooser.setFont(new Font("Lucida Handwriting", 0, 18));
        do{
            //se abre la ventana
            int seleccion = filechooser.showSaveDialog(this);
            //en funcion de la opcion seleccionada, se hace una cosa u otra
            switch(seleccion){
                case JFileChooser.APPROVE_OPTION:
                    //obtenemos la imagen y la almacenamos en un File
                    file = filechooser.getSelectedFile();
                    if(!file.exists()){
                        JOptionPane.showMessageDialog(this, "Selecciona una "
                                + "imagen válida");
                    }
                    System.out.println("Ruta archivo completa"+file.getAbsolutePath());
                    break;
                case JFileChooser.CANCEL_OPTION:
                    //cancelar
                    filechooser.cancelSelection();
                    return;
                default:
                    //error
                    System.out.println("error");
                break;
            }
        }while(!file.exists());
        lblImagen.setIcon(new ImageIcon(file.getAbsolutePath()));
    }//GEN-LAST:event_btnCambiarActionPerformed

    private void cmbComicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbComicActionPerformed
        if(cmbComic.getSelectedIndex() != -1){
            Comic comic = (Comic) cmbComic.getSelectedItem();
            colocarCampos(comic);
        }
    }//GEN-LAST:event_cmbComicActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
         //validacion campos
        if(cmbComic.getSelectedIndex() == -1){
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un "
                    + "comic", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }if(txtTitulo.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Falta título", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }if(jdateChooser.getDate() == null){
            JOptionPane.showMessageDialog(this, "Falta fecha de adquisición", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }if(file == null && imagen == null){
            JOptionPane.showMessageDialog(this, "Selecciona una imagen");
            return;
        }if(cmbColeccion.getSelectedIndex() == -1){
            JOptionPane.showMessageDialog(this, "Selecciona una colección");
            return;
        }if(spnPrecio.getValue().toString().equalsIgnoreCase("0")){
            JOptionPane.showConfirmDialog(this, "El precio no puede ser 0");
            return;
        }
        int posSeleccionada = cmbComic.getSelectedIndex();
        //modificar comic de BD
        try {  
            FileInputStream fis = null;
            PreparedStatement ps = null;
            if(file != null){
                fis = new FileInputStream(file);
            }
            Comic comic = (Comic) cmbComic.getSelectedItem();
            String titulo = txtTitulo.getText().trim();
            Date fecha_adquisicion = new Date(jdateChooser.getDate().getTime());
            int num_coleccion = (int) spnNumColeccion.getValue();
            float precio = Float.parseFloat(spnPrecio.getValue().toString());
            String consulta = "update comics set titulo = ?"
                    + ", fecha_adquisicion = ?, " 
                    + "numero_coleccion =?,"
                    + "precio= ?, portada=?,estado=?, id_coleccion=?, url_imagen=?"
                    + "where id =?";
            System.out.println(""+consulta);
            Conexion.getConexion().setAutoCommit(false);
            ps =  Conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, txtTitulo.getText().trim());
            ps.setDate(2, fecha_adquisicion);
            ps.setInt(3, num_coleccion);
            ps.setFloat(4, precio);
            if(file != null){
                ps.setBinaryStream(5, fis, (int) file.length());
            }else{
                ps.setBlob(5, imagen);
            }
            ps.setString(6, (String) cmbEstado.getSelectedItem());
            Coleccion coleccion = (Coleccion) cmbColeccion.getSelectedItem();
            ps.setInt(7, coleccion.getId());
            if(file != null){
                ps.setString(8, file.getName());
            }else{
                ps.setString(8, comic.getUrl_imagen());
            }
            ps.setInt(9, comic.getId());
            System.out.println(""+ps.toString());
            ps.executeUpdate();
            Conexion.getConexion().commit();
            if(file!=null){
                System.out.println("Nombre file "+file.getName());
                System.out.println("Absolute path "+file.getAbsolutePath());
                System.out.println(cliente.subirFichero(file.getAbsolutePath(), 
                        file.getName()));
            }
            actualizarComic(posSeleccionada);
        } catch (SQLException e) {
            System.out.println(""+e.getErrorCode());
            e.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AltaComic.class.getName()).log(Level.SEVERE, null, ex);
        }
        limpiar();
        rellenarComics();
        actualizarComic(posSeleccionada);
        //rellenamos el resto de componentes de las ventanas
        AltaComic.rellenarColecciones();
        BajaColeccion.rellenarColecciones();
        BajaComic.rellenarComics();
        ModificacionColeccion.rellenarColecciones();
        ModificacionComic.rellenarColecciones();
        ModificacionComic.rellenarComics();
        InformesComics.rellenarComics();
        InformesColecciones.rellenarColecciones();
        JOptionPane.showMessageDialog(this, "Comic modificado correctamente", 
                "Éxito", JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_btnAceptarActionPerformed
    /**
     * guarda imagen en servidor
     */
    private void guardarImagen(File img){
        try {
            BufferedImage bi = ImageIO.read(img);
            File outputFile = new File("http://danielike-001-site1.itempurl.com/android_connect/img/");
            
            ImageIO.write(bi, file.getName(), outputFile);
        } catch (IOException ex) {
            Logger.getLogger(ModificacionComic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void cmbColeccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbColeccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbColeccionActionPerformed
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCambiar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<Coleccion> cmbColeccion;
    private javax.swing.JComboBox<Comic> cmbComic;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private com.toedter.calendar.JDateChooser jdateChooser;
    private javax.swing.JLabel lblImagen;
    private javax.swing.JSpinner spnNumColeccion;
    private javax.swing.JSpinner spnPrecio;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
    private static DefaultComboBoxModel<Comic> modeloComics = new DefaultComboBoxModel<>();
    private static DefaultComboBoxModel<Coleccion> modeloColecciones = new DefaultComboBoxModel<>();
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == 10){
            btnAceptar.doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
        
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        try {
            cliente.close();
        } catch (IOException ex) {
            Logger.getLogger(ModificacionComic.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            dispose();
        }
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
        
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
        
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
        
    }
}
