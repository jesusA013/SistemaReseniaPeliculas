/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Pantallas;

import dtos.UsuarioDTO;
import interfaces.IUsuarioService;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.border.AbstractBorder;
import servicios.UsuarioService;

/**
 *
 * @author golea
 */
public class Iniciosesion extends javax.swing.JFrame {

    private IUsuarioService usuarioService;

    /**
     * Creates new form Iniciosesion
     */
    public Iniciosesion() {
        initComponents();
        this.usuarioService = new UsuarioService();

        // 1. Maximizar la ventana
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        // 2. CAMBIO CLAVE: Reconfigurar el contenedor principal para centrar de verdad
        this.getContentPane().removeAll(); // Limpiamos lo que NetBeans puso fijo
        this.getContentPane().setLayout(new GridBagLayout());
        this.getContentPane().add(panelCentral, new GridBagConstraints());

        // 3. Aplicar el diseño visual
        personalizarDiseñoAgrandado();

        this.setLocationRelativeTo(null);
        txtUsuarioCorreo.setText("");
        txtContrasena.setText("");
    }

    private void personalizarDiseñoAgrandado() {
        this.getContentPane().setBackground(Color.WHITE);
    
    
    // Configuración del Panel Central (Gris)
    panelCentral.setBackground(new Color(240, 240, 240));
   
    panelCentral.setPreferredSize(new Dimension(500, 600));

        // CAMBIO CLAVE: Usar un Layout que apile los elementos verticalmente y los centre
        panelCentral.setLayout(new javax.swing.BoxLayout(panelCentral, javax.swing.BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50)); // Margen interno

        // Definición de Estilos
        Dimension dimCampos = new Dimension(400, 50);
        Dimension dimBotones = new Dimension(400, 60);
        Font fuenteNormal = new Font("SansSerif", Font.PLAIN, 18);
        Font fuenteNegrita = new Font("SansSerif", Font.BOLD, 18);

        // 1. Organizar Correo
        lblUsuarioCorreo.setFont(fuenteNormal);
        lblUsuarioCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtUsuarioCorreo.setMaximumSize(dimCampos);
        txtUsuarioCorreo.setPreferredSize(dimCampos);
        txtUsuarioCorreo.setFont(fuenteNormal);

        // 2. Organizar Contraseña
        lblContrasena.setFont(fuenteNormal);
        lblContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtContrasena.setMaximumSize(dimCampos);
        txtContrasena.setPreferredSize(dimCampos);
        txtContrasena.setFont(fuenteNormal);

        // Aplicar bordes redondeados con padding interno
        AbstractBorder border = new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.GRAY);
                g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, 15, 15));
                g2.dispose();
            }
        };
        txtUsuarioCorreo.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 15, 5, 15)));

        // 3. Configurar Botones
        configurarBotonGrande(btnIniciarSesion, new Color(40, 40, 40), Color.WHITE, dimBotones, fuenteNegrita);
        configurarBotonGrande(btnRegistrarse, new Color(200, 200, 200), Color.BLACK, dimBotones, fuenteNegrita);
        btnIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrarse.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- AÑADIR AL PANEL CON ESPACIADO ---
        panelCentral.add(lblUsuarioCorreo);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio de 10px
        panelCentral.add(txtUsuarioCorreo);
        panelCentral.add(Box.createVerticalStrut(25)); // Espacio entre secciones
        panelCentral.add(lblContrasena);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(txtContrasena);
        panelCentral.add(Box.createVerticalStrut(40));
        panelCentral.add(btnIniciarSesion);
        panelCentral.add(Box.createVerticalStrut(15));
        panelCentral.add(btnRegistrarse);

        panelCentral.revalidate();
    }

    private void configurarBotonGrande(JButton btn, Color fondo, Color texto, Dimension dim, Font fuente) {
        btn.setBackground(fondo);
        btn.setForeground(texto);
        btn.setPreferredSize(dim);
        btn.setMinimumSize(dim);
        btn.setFont(fuente);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCentral = new javax.swing.JPanel();
        txtUsuarioCorreo = new javax.swing.JTextField();
        lblUsuarioCorreo = new javax.swing.JLabel();
        lblContrasena = new javax.swing.JLabel();
        txtContrasena = new javax.swing.JTextField();
        btnRegistrarse = new javax.swing.JButton();
        btnIniciarSesion = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelCentral.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtUsuarioCorreo.setText("jTextField1");
        panelCentral.add(txtUsuarioCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 135, 34));

        lblUsuarioCorreo.setText("Correo Electornico");
        panelCentral.add(lblUsuarioCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 176, -1));

        lblContrasena.setText("Contrasena");
        panelCentral.add(lblContrasena, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 135, -1));

        txtContrasena.setText("jTextField1");
        panelCentral.add(txtContrasena, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, 135, 38));

        btnRegistrarse.setText("Registrarse");
        btnRegistrarse.setToolTipText("");
        btnRegistrarse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarseActionPerformed(evt);
            }
        });
        panelCentral.add(btnRegistrarse, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 135, 46));

        btnIniciarSesion.setText("Iniciar Sesion");
        btnIniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarSesionActionPerformed(evt);
            }
        });
        panelCentral.add(btnIniciarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, 135, 46));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(180, 180, 180)
                .addComponent(panelCentral, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(251, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addComponent(panelCentral, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(153, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarSesionActionPerformed
        // TODO add your handling code here:
        String email = txtUsuarioCorreo.getText().trim();
        String password = txtContrasena.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, llene todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioDTO usuarioLogueado = usuarioService.iniciarSesion(email, password);

        if (usuarioLogueado != null) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido de nuevo, " + usuarioLogueado.getNombre() + "!");

            Principal pantallaPrincipal = new Principal(usuarioLogueado);

            this.getContentPane().setLayout(new java.awt.BorderLayout());
            this.getContentPane().removeAll();
            this.getContentPane().add(pantallaPrincipal, java.awt.BorderLayout.CENTER);
            this.getContentPane().revalidate();
            this.getContentPane().repaint();

        } else {
            JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos.", "Error de acceso", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnIniciarSesionActionPerformed

    private void btnRegistrarseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarseActionPerformed
        RegistroUsuario registro = new RegistroUsuario();

        registro.setSize(this.getContentPane().getSize());

        this.getContentPane().removeAll();
        this.getContentPane().add(registro);
        this.getContentPane().revalidate();
        this.getContentPane().repaint();

        registro.setVisible(true);
        JOptionPane.showMessageDialog(this, "Abriendo pantalla de registro...");
    }//GEN-LAST:event_btnRegistrarseActionPerformed

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
            java.util.logging.Logger.getLogger(Iniciosesion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Iniciosesion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Iniciosesion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Iniciosesion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Iniciosesion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciarSesion;
    private javax.swing.JButton btnRegistrarse;
    private javax.swing.JLabel lblContrasena;
    private javax.swing.JLabel lblUsuarioCorreo;
    private javax.swing.JPanel panelCentral;
    private javax.swing.JTextField txtContrasena;
    private javax.swing.JTextField txtUsuarioCorreo;
    // End of variables declaration//GEN-END:variables
}
