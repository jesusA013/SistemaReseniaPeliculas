/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Pantallas;

import dtos.PeliculasDTO;
import dtos.ResenaDTO;
import dtos.UsuarioDTO;
import java.awt.Component;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author golea
 */
public class PantallaVistaReseña extends javax.swing.JPanel {

    private UsuarioDTO usuarioLogueado;
    private interfaces.IReseñaService resenaService;

    /**
     * Creates new form CreacionResena
     */
    public PantallaVistaReseña(UsuarioDTO usuario) {
        initComponents();
        this.usuarioLogueado = usuario;
        this.resenaService = new servicios.ResenaService();

        // 1. FORZAR DISEÑO DE PANTALLA COMPLETA
        // Esto ignora las dimensiones fijas del editor de NetBeans
        this.setLayout(new java.awt.BorderLayout());

        // Creamos un panel superior para los botones y el título
        javax.swing.JPanel panelSuperior = new javax.swing.JPanel(new java.awt.BorderLayout());
        panelSuperior.setBackground(java.awt.Color.WHITE);
        panelSuperior.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Re-ubicamos los componentes en el nuevo Layout responsivo
        panelSuperior.add(btnRegresar, java.awt.BorderLayout.WEST);
        panelSuperior.add(lblVistareseña, java.awt.BorderLayout.CENTER);
        panelSuperior.add(lblnombredeUsuario, java.awt.BorderLayout.EAST);

        this.add(panelSuperior, java.awt.BorderLayout.NORTH);
        this.add(jScrollPane1, java.awt.BorderLayout.CENTER); // El scroll ahora ocupa todo el resto

        // 2. ESTILOS Y CARGA
        jScrollPane1.setBorder(null);
        jScrollPane1.getViewport().setBackground(java.awt.Color.WHITE);
        estilizarBoton(btnRegresar, "← Regresar");
        estilizarBoton(btnPlaylist, "♫ Mi Playlist");

        lblVistareseña.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVistareseña.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 22));

        cargarResenasEnTabla();

        // 3. ESCUCHADOR RESPONSIVO
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                ajustarAnchosResponsivos();
            }
        });
    }

   
     
    private void cambiarPantalla(javax.swing.JPanel nuevaPantalla) {
        java.awt.Container padre = this.getParent();
        if (padre != null) {
            padre.removeAll();
            // ESTO ES LO QUE FUERZA EL TAMAÑO COMPLETO
            padre.setLayout(new java.awt.BorderLayout());
            padre.add(nuevaPantalla, java.awt.BorderLayout.CENTER);
            padre.revalidate();
            padre.repaint();
        }
    }

// Método para que los botones se vean modernos y limpios
    private void estilizarBoton(javax.swing.JButton boton, String texto) {
        boton.setText(texto);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        boton.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
        boton.setForeground(new java.awt.Color(106, 27, 154)); // Color morado
    }

    public class ImageRenderer extends javax.swing.table.DefaultTableCellRenderer {

        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setText("");
            label.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margen interno

            if (value instanceof javax.swing.Icon) {
                label.setIcon((javax.swing.Icon) value);
                label.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            }

            // Color de selección suave
            if (isSelected) {
                label.setBackground(new java.awt.Color(240, 240, 255));
            } else {
                label.setBackground(java.awt.Color.WHITE);
            }

            return label;
        }
    }

    private void ajustarColumnasResponsivas() {
        if (tablacargarreseñasusuario.getColumnCount() <= 0) {
            return;
        }

        int anchoTotal = jScrollPane1.getWidth();

        // Definimos anchos basados en el tamaño actual
        // El póster siempre mide 120px (Fijo)
        tablacargarreseñasusuario.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablacargarreseñasusuario.getColumnModel().getColumn(0).setMaxWidth(150);

        // La columna de detalles crece un poco
        int anchoDetalles = (int) (anchoTotal * 0.25);
        tablacargarreseñasusuario.getColumnModel().getColumn(1).setPreferredWidth(anchoDetalles);

        // La columna de la reseña es la más elástica (ocupa el resto)
        int anchoResena = anchoTotal - 120 - anchoDetalles;
        tablacargarreseñasusuario.getColumnModel().getColumn(2).setPreferredWidth(anchoResena);
    }

    private void cargarResenasEnTabla() {
        List<dtos.ResenaDTO> lista = resenaService.obtenerResenasPorUsuario(usuarioLogueado.getId());

        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(
                new Object[]{"Póster", "Película", "Tu Reseña"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 0) ? javax.swing.Icon.class : Object.class;
            }
        };

        tablacargarreseñasusuario.setModel(modelo);
        tablacargarreseñasusuario.setRowHeight(170);
        tablacargarreseñasusuario.setIntercellSpacing(new java.awt.Dimension(0, 20));
        tablacargarreseñasusuario.setBackground(java.awt.Color.WHITE);

        for (dtos.ResenaDTO r : lista) {
            Object[] fila = new Object[3];
            fila[0] = obtenerIconoPoster(r.getPelicula().getPosterPath());

            // Título con padding
            fila[1] = "<html><body style='padding: 10px;'><b style='font-size: 13px; color: #333;'>"
                    + r.getPelicula().getTitulo() + "</b><br><small style='color: #999;'>Película</small></body></html>";

            // Reseña responsiva (usa width: 100% para adaptarse al ancho de la columna)
            String estrellas = "★".repeat(Math.max(0, (int) r.getCalificacion() / 2));
            fila[2] = "<html><body style='padding: 10px; width: 100%;'>"
                    + "<span style='color: #6a1b9a; font-weight: bold;'>" + estrellas + " (" + r.getCalificacion() + "/10)</span><br>"
                    + "<p style='color: #555; margin-top: 5px;'>" + r.getComentario() + "</p></body></html>";

            modelo.addRow(fila);
        }

        tablacargarreseñasusuario.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
        ajustarColumnasResponsivas(); // Llamada inicial
    }

    private javax.swing.ImageIcon obtenerIconoPoster(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        try {
            String urlFull = path.startsWith("http") ? path : "https://image.tmdb.org/t/p/w200" + path;
            java.net.URL url = new java.net.URL(urlFull);
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(url);
            java.awt.Image img = icon.getImage().getScaledInstance(100, 145, java.awt.Image.SCALE_SMOOTH);
            return new javax.swing.ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    private void ajustarAnchosResponsivos() {
        // Obtenemos el ancho actual del JScrollPane que ya está estirado
        int anchoTotal = jScrollPane1.getWidth();

        if (anchoTotal > 100) {
            // Columna Póster: Fija
            tablacargarreseñasusuario.getColumnModel().getColumn(0).setPreferredWidth(130);
            tablacargarreseñasusuario.getColumnModel().getColumn(0).setMaxWidth(130);

            // Columna Película: 30%
            int anchoC1 = (int) (anchoTotal * 0.30);
            tablacargarreseñasusuario.getColumnModel().getColumn(1).setPreferredWidth(anchoC1);

            // Columna Reseña: El resto (aprox 60-70%)
            int anchoC2 = anchoTotal - 130 - anchoC1;
            tablacargarreseñasusuario.getColumnModel().getColumn(2).setPreferredWidth(anchoC2);
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

        btnPlaylist = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();
        lblnombredeUsuario = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablacargarreseñasusuario = new javax.swing.JTable();
        lblVistareseña = new javax.swing.JLabel();

        btnPlaylist.setText("Playlist");
        btnPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlaylistActionPerformed(evt);
            }
        });

        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        lblnombredeUsuario.setText("Nombre de Usuario");

        tablacargarreseñasusuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Caratula Pelicula", "Nombre", "Reseña"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablacargarreseñasusuario);

        lblVistareseña.setText("Vista de Reseñas  ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(btnPlaylist, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99)
                        .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84)
                        .addComponent(lblnombredeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 724, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(343, 343, 343)
                        .addComponent(lblVistareseña)))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegresar)
                    .addComponent(lblnombredeUsuario)
                    .addComponent(btnPlaylist))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(lblVistareseña)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        // TODO add your handling code here:
        Principal pantallaPrincipal = new Principal(usuarioLogueado);

        // 2. Obtenemos el contenedor padre (el JFrame o Panel principal que contiene a este)
        java.awt.Container padre = this.getParent();

        if (padre != null) {
            // 3. Limpiamos el contenido actual
            padre.removeAll();

            // 4. Establecemos el layout (asegurando que se expanda correctamente)
            padre.setLayout(new java.awt.BorderLayout());

            // 5. Agregamos la pantalla principal y refrescamos
            padre.add(pantallaPrincipal, java.awt.BorderLayout.CENTER);
            padre.revalidate();
            padre.repaint();
        }

    }//GEN-LAST:event_btnRegresarActionPerformed

    private void btnPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlaylistActionPerformed
        // TODO add your handling code here:
        PantallaPlaylist vistaPlaylist = new PantallaPlaylist(usuarioLogueado);

        // 2. Obtenemos el contenedor padre (donde están alojados los JPanels)
        java.awt.Container padre = this.getParent();

        if (padre != null) {
            // 3. Limpiamos lo que hay actualmente (la pantalla Principal)
            padre.removeAll();

            // 4. Configuramos el diseño para que la nueva pantalla se ajuste
            padre.setLayout(new java.awt.BorderLayout());

            // 5. Agregamos la nueva pantalla y refrescamos
            padre.add(vistaPlaylist, java.awt.BorderLayout.CENTER);
            padre.revalidate();
            padre.repaint();
        }
    }//GEN-LAST:event_btnPlaylistActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPlaylist;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblVistareseña;
    private javax.swing.JLabel lblnombredeUsuario;
    private javax.swing.JTable tablacargarreseñasusuario;
    // End of variables declaration//GEN-END:variables
}
