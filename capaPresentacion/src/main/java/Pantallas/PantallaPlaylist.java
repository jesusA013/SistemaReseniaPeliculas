/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Pantallas;

import dtos.PeliculasDTO;
import dtos.PlaylistDTO;
import dtos.UsuarioDTO;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import servicios.PlaylistService;

/**
 *
 * @author golea
 */
public class PantallaPlaylist extends javax.swing.JPanel {

    private int estadoFiltro = 0;
    private UsuarioDTO usuarioLogueado;
    private servicios.PlaylistService playlistService = new servicios.PlaylistService();

    /**
     * Creates new form PantallaPlaylist
     */
    public PantallaPlaylist(UsuarioDTO usuario) {
        initComponents();
        this.usuarioLogueado = usuario; // Guardamos el usuario recibido
        lblnombredeUsuario1.setText(usuario.getNombre());
        cargarPlaylistsPublicas();
    }

    private void cargarPlaylistsPublicas() {
        System.out.println("Iniciando carga de carruseles...");
        try {
            List<PlaylistDTO> listas = playlistService.obtenerPlaylistsPublicas();

            // Panel contenedor con BoxLayout vertical
            JPanel contenedorPrincipal = new JPanel();
            contenedorPrincipal.setLayout(new javax.swing.BoxLayout(contenedorPrincipal, javax.swing.BoxLayout.Y_AXIS));
            contenedorPrincipal.setBackground(java.awt.Color.WHITE);

            for (PlaylistDTO lista : listas) {
                JPanel panelFila = new JPanel(new java.awt.BorderLayout());
                panelFila.setOpaque(false);
                // Margen para que no peguen las listas entre sí
                panelFila.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 20, 20));

                // Título de la Playlist
                javax.swing.JLabel lblTitulo = new javax.swing.JLabel(lista.getNombre());
                lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20));
                panelFila.add(lblTitulo, java.awt.BorderLayout.NORTH);

                // Panel para las "Cards" de películas
                JPanel panelCarrusel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 10));
                panelCarrusel.setOpaque(false);

                if (lista.getPeliculas() != null && !lista.getPeliculas().isEmpty()) {
                    for (dtos.PeliculasDTO peli : lista.getPeliculas()) {
                        panelCarrusel.add(crearCardPelicula(peli));
                    }
                } else {
                    panelCarrusel.add(new javax.swing.JLabel("Esta playlist no tiene películas aún."));
                }

                // Scroll horizontal para cada fila
                javax.swing.JScrollPane scrollH = new javax.swing.JScrollPane(panelCarrusel);
                scrollH.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                scrollH.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                scrollH.setBorder(null);
                // Aumentamos el alto preferido para que se vean bien las cards (mínimo 280)
                scrollH.setPreferredSize(new java.awt.Dimension(jScrollPane1.getWidth(), 280));

                panelFila.add(scrollH, java.awt.BorderLayout.CENTER);
                contenedorPrincipal.add(panelFila);
            }

            // IMPORTANTE: Asignar al Viewport
            jScrollPane1.setViewportView(contenedorPrincipal);

            // Refrescar componentes
            jScrollPane1.revalidate();
            jScrollPane1.repaint();

        } catch (Exception e) {
            System.err.println("Error al cargar playlists: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private JPanel crearCardPelicula(dtos.PeliculasDTO peli) {
        JPanel card = new JPanel();
        card.setLayout(new javax.swing.BoxLayout(card, javax.swing.BoxLayout.Y_AXIS));
        card.setOpaque(false);

        javax.swing.JLabel lblFoto = new javax.swing.JLabel();
        lblFoto.setPreferredSize(new java.awt.Dimension(120, 180));
        lblFoto.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY));

        // Carga de imagen asíncrona
        new Thread(() -> {
            try {
                if (peli.getPosterPath() != null) {
                    java.net.URL url = new java.net.URL("https://image.tmdb.org/t/p/w200" + peli.getPosterPath());
                    javax.swing.ImageIcon icon = new javax.swing.ImageIcon(url);
                    java.awt.Image img = icon.getImage().getScaledInstance(120, 180, java.awt.Image.SCALE_SMOOTH);
                    javax.swing.SwingUtilities.invokeLater(() -> lblFoto.setIcon(new javax.swing.ImageIcon(img)));
                }
            } catch (Exception ignored) {
            }
        }).start();

        javax.swing.JLabel lblNombre = new javax.swing.JLabel(peli.getTitulo());
        lblNombre.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        card.add(lblFoto);
        card.add(lblNombre);
        return card;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnRegresar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        btnReseñas = new javax.swing.JButton();
        lblnombredeUsuario1 = new javax.swing.JLabel();
        btnFiltrar = new javax.swing.JButton();
        btnCrearplaylist = new javax.swing.JButton();

        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        btnReseñas.setText("Reseñas");
        btnReseñas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReseñasActionPerformed(evt);
            }
        });

        lblnombredeUsuario1.setText("Nombre de Usuario");

        btnFiltrar.setText("Filtrar");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });

        btnCrearplaylist.setText("Crear playlist");
        btnCrearplaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearplaylistActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRegresar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReseñas)
                .addGap(18, 18, 18)
                .addComponent(lblnombredeUsuario1)
                .addGap(35, 35, 35))
            .addGroup(layout.createSequentialGroup()
                .addGap(284, 284, 284)
                .addComponent(btnCrearplaylist)
                .addGap(18, 18, 18)
                .addComponent(btnFiltrar)
                .addContainerGap(123, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(53, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(69, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblnombredeUsuario1)
                    .addComponent(btnReseñas)
                    .addComponent(btnRegresar))
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFiltrar)
                    .addComponent(btnCrearplaylist))
                .addContainerGap(447, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(173, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(24, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        // TODO add your handling code here:
        // 1. Creamos la instancia de la pantalla Principal pasando el usuario que está logueado
        // Usamos 'usuarioLogueado' que es la variable global de tu clase
        Principal pantallaPrincipal = new Principal(this.usuarioLogueado);

        // 2. Obtenemos el contenedor padre (el JFrame o Panel que contiene a este JPanel)
        java.awt.Container padre = this.getParent();

        if (padre != null) {
            // 3. Limpiamos el contenido actual del contenedor
            padre.removeAll();

            // 4. Establecemos el layout para que la nueva pantalla ocupe todo el espacio
            padre.setLayout(new java.awt.BorderLayout());

            // 5. Agregamos la pantalla principal al centro y refrescamos la interfaz
            padre.add(pantallaPrincipal, java.awt.BorderLayout.CENTER);

            // 6. Forzamos a la interfaz a redibujarse para mostrar los cambios
            padre.revalidate();
            padre.repaint();
        }
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void btnReseñasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReseñasActionPerformed
        // TODO add your handling code here:
        PantallaVistaReseña vistaResenas = new PantallaVistaReseña(usuarioLogueado);

        // 2. Obtenemos el contenedor principal (suponiendo que están dentro de un JFrame)
        // Reemplazamos el contenido actual por la nueva pantalla
        this.removeAll();
        this.setLayout(new java.awt.BorderLayout());
        this.add(vistaResenas);

        // 3. Refrescamos la interfaz
        this.revalidate();
        this.repaint();
    }//GEN-LAST:event_btnReseñasActionPerformed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnCrearplaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearplaylistActionPerformed
        // TODO add your handling code here:
        // 1. Crear los componentes de la interfaz para el diálogo
        javax.swing.JTextField txtNombre = new javax.swing.JTextField();
        javax.swing.JCheckBox chkPublica = new javax.swing.JCheckBox("Hacer pública");

        Object[] message = {
            "Nombre de la Playlist:", txtNombre,
            "Privacidad:", chkPublica
        };

        // 2. Mostrar el diálogo emergente
        int option = javax.swing.JOptionPane.showConfirmDialog(
                this,
                message,
                "Nueva Playlist",
                javax.swing.JOptionPane.OK_CANCEL_OPTION
        );

        // 3. Si el usuario presionó OK
        if (option == javax.swing.JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();

            if (nombre.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                return;
            }

            try {
                // 4. Configurar el DTO
                dtos.PlaylistDTO nuevaPlaylist = new dtos.PlaylistDTO();
                nuevaPlaylist.setNombre(nombre);
                nuevaPlaylist.setEsPublica(chkPublica.isSelected());
                nuevaPlaylist.setUsuario(this.usuarioLogueado); // Asegúrate de tener el usuario logueado en esta pantalla

                // 5. Llamar al servicio
                servicios.PlaylistService service = new servicios.PlaylistService();
                service.crearPlaylist(nuevaPlaylist);

                javax.swing.JOptionPane.showMessageDialog(this, "¡Playlist '" + nombre + "' creada con éxito!");

                // 6. Opcional: Refrescar la tabla o lista de playlists
                // cargarPlaylistsEnTabla(); 
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Error al crear: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnCrearplaylistActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCrearplaylist;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton btnReseñas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblnombredeUsuario1;
    // End of variables declaration//GEN-END:variables
}
