/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baja;

import alta.AltaComic;
import clases.Coleccion;
import clases.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import modificacion.ModificacionColeccion;
import modificacion.ModificacionComic;
import ventanaInformes.InformesColecciones;
import ventanaInformes.InformesComics;

/**
 *
 * @author Dani
 */
public class BajaColeccion extends javax.swing.JInternalFrame {

    /**
     * Creates new form BajaColeccion
     */
    public BajaColeccion() {
        initComponents();
        cmbColecciones.setModel(modeloColecciones);
        rellenarColecciones();
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
        cmbColecciones = new javax.swing.JComboBox<>();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Baja Colección");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Colección : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(cmbColecciones, gridBagConstraints);

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(btnCancelar, gridBagConstraints);

        setBounds(0, 0, 503, 135);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // cierra ventana
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        // validacion
        if(cmbColecciones.getSelectedIndex() == -1){
            JOptionPane.showMessageDialog(this, "Ninguna colección seleccionada", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //eliminar coleccion de BD
        try {
            Statement stm = Conexion.getConexion().createStatement();
            Coleccion coleccion = (Coleccion) modeloColecciones.getSelectedItem();
            stm.executeUpdate("delete from colecciones where id=" + coleccion.getId());
        } catch (SQLException e) {
            System.out.println(""+ e.getErrorCode());
            //error 1451, relacion de fk existente en otra tabla. Eliminar la otra
            //primero
            if(e.getErrorCode() == 1451){
                JOptionPane.showMessageDialog(this, "La colección no se puede "
                        + "eliminar. Está asociada a uno o varios comics."
                        + " Primero elimina todos sus comics"
                        + " asociados", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        rellenarColecciones();
        reset();
        //rellenamos el resto de componentes de las ventanas
        AltaComic.rellenarColecciones();
        BajaColeccion.rellenarColecciones();
        BajaComic.rellenarComics();
        ModificacionColeccion.rellenarColecciones();
        ModificacionComic.rellenarColecciones();
        ModificacionComic.rellenarComics();
        InformesComics.rellenarComics();
        InformesColecciones.rellenarColecciones();
        JOptionPane.showMessageDialog(this, "Colección eliminada correctamente", 
            "Éxito", JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_btnAceptarActionPerformed
    /**
     * resetea el combobox a su posicion original
     */
    private void reset(){
        if(cmbColecciones.getSelectedIndex() != -1){
            cmbColecciones.setSelectedIndex(0);
        }
    }
    /**
     * rellena el combobox de colecciones
     */
    public static void rellenarColecciones(){
        modeloColecciones.removeAllElements();
        try {
            Statement stm = Conexion.getConexion().createStatement();
            ResultSet rs = stm.executeQuery("select id, nombre from "
                    + "colecciones order by nombre");
            while(rs.next()){
                Coleccion coleccion = new Coleccion(rs.getInt(1), rs.getString(2));
                modeloColecciones.addElement(coleccion);
            }
        } catch (SQLException e) {
            System.out.println(""+e.getErrorCode());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<Coleccion> cmbColecciones;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
    private static DefaultComboBoxModel<Coleccion> modeloColecciones = new DefaultComboBoxModel<>();
}
