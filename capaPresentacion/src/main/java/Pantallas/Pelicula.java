/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Pantallas;

import dtos.ActorDTO;
import dtos.PeliculasDTO;
import dtos.PlaylistDTO;
import dtos.ResenaDTO;
import dtos.UsuarioDTO;
import interfaces.IPeliculaService;
import interfaces.IPlaylistService;
import interfaces.IReseñaService;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author golea
 */
public class Pelicula extends javax.swing.JPanel {

    private UsuarioDTO usuarioActual;
    private IPeliculaService peliculaService;
    private IReseñaService resenaService = new servicios.ResenaService();
    private IPlaylistService playlistService = new servicios.PlaylistService();
    private PeliculasDTO peliculaActual;

    /**
     * Creates new form Pelicula
     */
    public Pelicula(PeliculasDTO datos, UsuarioDTO usuario) {
        initComponents();
        this.peliculaActual = datos;
        this.usuarioActual = usuario;
        lblnombredeUsuario1.setText(usuario.getNombre());
        cbxestrellas.removeAllItems();
        for (int i = 1; i <= 5; i++) {
            cbxestrellas.addItem(String.valueOf(i));
        }

        this.peliculaService = new servicios.PeliculaService();
        lblnombredeUsuario.setText(usuario.getNombre());
        txtareaSinopsis.setLineWrap(true);
        txtareaSinopsis.setWrapStyleWord(true);
        txtareaSinopsis.setEditable(false);
        lblNombredepelicula.setText(datos.getTitulo());
        txtareaSinopsis.setText(datos.getSinopsis());
        lblpuntuacion.setText(String.format("%.1f", datos.getRatingPromedio()));
        if (datos.getPosterPath() != null && !datos.getPosterPath().isEmpty()) {
            cargarImagenPoster(imgcaratulaimagenpelicula, datos.getPosterPath());
        }
        if (datos.getListaActores() != null) {
            List<ActorDTO> actores = datos.getListaActores();

            javax.swing.JLabel[] labelsNombres = {lblActor1, lblActor2, lblActor3, lblActor4};
            javax.swing.JLabel[] labelsFotos = {imgactor1, imgactor2, imgactor3, imgactor4};

            for (int j = 0; j < labelsNombres.length; j++) {
                labelsNombres[j].setText("");
                labelsFotos[j].setIcon(null);

            }

            for (int i = 0; i < Math.min(actores.size(), 4); i++) {
                ActorDTO actor = actores.get(i);
                labelsNombres[i].setText(actor.getNombre());

                if (actor.getRutaPerfil() != null && !actor.getRutaPerfil().isEmpty()) {
                    cargarImagenActor(labelsFotos[i], actor.getRutaPerfil());
                }
            }
        }
        refrescarDatos(datos);
    }

    private void cargarImagenActor(javax.swing.JLabel label, String ruta) {
        try {
            String fullUrl = "https://image.tmdb.org/t/p/w185" + ruta;
            URL url = new URL(fullUrl);
            Image imagen = ImageIO.read(url);

            int width = label.getWidth() > 0 ? label.getWidth() : 81;
            int height = label.getHeight() > 0 ? label.getHeight() : 69;

            Image dimensionada = imagen.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(dimensionada));
            label.setText("");
        } catch (Exception e) {
            System.err.println("Error cargando imagen: " + e.getMessage());
        }
    }

    private void cargarImagenPoster(javax.swing.JLabel label, String ruta) {
        try {
            String fullUrl = "https://image.tmdb.org/t/p/w500" + ruta;
            URL url = new URL(fullUrl);
            Image imagen = ImageIO.read(url);

            int width = label.getWidth() > 0 ? label.getWidth() : 198;
            int height = label.getHeight() > 0 ? label.getHeight() : 354;

            Image dimensionada = imagen.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(dimensionada));
            label.setText("");
        } catch (Exception e) {
            System.err.println("Error cargando carátula: " + e.getMessage());
        }
    }

    private void refrescarDatos(PeliculasDTO datos) {
        lblNombredepelicula.setText(datos.getTitulo());
        txtareaSinopsis.setText(datos.getSinopsis());
        lblpuntuacion.setText(String.format("%.1f", datos.getRatingPromedio()));

        if (datos.getPosterPath() != null && !datos.getPosterPath().isEmpty()) {
            cargarImagenPoster(imgcaratulaimagenpelicula, datos.getPosterPath());
        }

        if (datos.getListaActores() != null) {
            List<ActorDTO> actores = datos.getListaActores();
            javax.swing.JLabel[] labelsNombres = {lblActor1, lblActor2, lblActor3, lblActor4};
            javax.swing.JLabel[] labelsFotos = {imgactor1, imgactor2, imgactor3, imgactor4};
            for (int j = 0; j < labelsNombres.length; j++) {
                labelsNombres[j].setText("Sin información");
                labelsFotos[j].setIcon(null);
                labelsFotos[j].setText("");

            }
            for (int i = 0; i < Math.min(actores.size(), labelsNombres.length); i++) {
                ActorDTO actor = actores.get(i);
                labelsNombres[i].setText(actor.getNombre());
                if (actor.getRutaPerfil() != null && !actor.getRutaPerfil().isEmpty()) {
                    cargarImagenActor(labelsFotos[i], actor.getRutaPerfil());
                }
            }
        }
    }

    private void cargarTablaResenas() {
        try {
            List<ResenaDTO> lista = resenaService.obtenerResenasPorPelicula(this.peliculaActual.getIdPelicula());

            DefaultTableModel modelo = (DefaultTableModel) ReseñasAajenas.getModel();
            modelo.setRowCount(0);

            for (ResenaDTO r : lista) {
                // OPCIÓN A: Enviar solo el número si la columna está configurada como Integer
                // Object[] fila = { r.getUsuario().getNombre(), r.getCalificacion(), r.getComentario() };

                // OPCIÓN B: Enviar todo como String explícito para evitar que el renderizador falle
                Object[] fila = {
                    String.valueOf(r.getUsuario().getNombre()),
                    String.valueOf(r.getCalificacion()) + " / 5", // Cambiamos la estrella por texto simple
                    String.valueOf(r.getComentario())
                };
                modelo.addRow(fila);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar tabla: " + e.getMessage());
        }
    }

    /**
     * **************************
     */
    ///////**********************/////////
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imgcaratulaimagenpelicula = new javax.swing.JLabel();
        lblNombredepelicula = new javax.swing.JLabel();
        lblActor1 = new javax.swing.JLabel();
        lblActor2 = new javax.swing.JLabel();
        lblActor3 = new javax.swing.JLabel();
        lblActor4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtareaSinopsis = new javax.swing.JTextArea();
        lblSinopsis = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblpuntuacion = new javax.swing.JLabel();
        txtareaReseña = new javax.swing.JScrollPane();
        txtareaReseñaTexto = new javax.swing.JTextArea();
        btnResena = new javax.swing.JButton();
        imgactor1 = new javax.swing.JLabel();
        imgactor2 = new javax.swing.JLabel();
        imgactor3 = new javax.swing.JLabel();
        imgactor4 = new javax.swing.JLabel();
        btnPlaylist = new javax.swing.JButton();
        btnReseñas = new javax.swing.JButton();
        lblnombredeUsuario = new javax.swing.JLabel();
        btnBuscarPelicula = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        lblnombredeUsuario1 = new javax.swing.JLabel();
        cbxestrellas = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        ReseñasAajenas = new javax.swing.JTable();
        btnañadiramiplaylist = new javax.swing.JButton();
        btnRegresarApantallaPrincipal = new javax.swing.JButton();
        jPanel = new javax.swing.JPanel();

        imgcaratulaimagenpelicula.setText("jLabel1");

        lblNombredepelicula.setText("Nombre de la pelicula");

        lblActor1.setText("Nombre del actor1");

        lblActor2.setText("Nombre del actor2");

        lblActor3.setText("Nombre del actor3");

        lblActor4.setText("Nombre del actor4");

        txtareaSinopsis.setColumns(20);
        txtareaSinopsis.setRows(5);
        jScrollPane1.setViewportView(txtareaSinopsis);

        lblSinopsis.setText("Sinopsis");

        jLabel2.setText("Escribe tu reseña:");

        lblpuntuacion.setText("Puntuacion");

        txtareaReseñaTexto.setColumns(20);
        txtareaReseñaTexto.setRows(5);
        txtareaReseña.setViewportView(txtareaReseñaTexto);

        btnResena.setText("Prublicar Reseña");
        btnResena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResenaActionPerformed(evt);
            }
        });

        imgactor1.setText("jLabel1");

        imgactor2.setText("jLabel1");

        imgactor3.setText("jLabel1");

        imgactor4.setText("jLabel1");

        btnPlaylist.setText("Playlist");
        btnPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlaylistActionPerformed(evt);
            }
        });

        btnReseñas.setText("Reseñas");
        btnReseñas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReseñasActionPerformed(evt);
            }
        });

        lblnombredeUsuario.setText("Nombre de Usuario");

        btnBuscarPelicula.setText("Buscar Pelicula");
        btnBuscarPelicula.setToolTipText("");
        btnBuscarPelicula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPeliculaActionPerformed(evt);
            }
        });

        jTextField1.setText("Introduce nombre de pelicula");

        lblnombredeUsuario1.setText("Nombre de Usuario:");

        cbxestrellas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Estrellas" }));

        ReseñasAajenas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nombre", "Reseña", "Puntuacion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(ReseñasAajenas);

        btnañadiramiplaylist.setText("Añadir a mi playlist");
        btnañadiramiplaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnañadiramiplaylistActionPerformed(evt);
            }
        });

        btnRegresarApantallaPrincipal.setText("Regresar");
        btnRegresarApantallaPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarApantallaPrincipalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(imgcaratulaimagenpelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblpuntuacion)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblSinopsis)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnañadiramiplaylist, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(67, 67, 67)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imgactor2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(imgactor3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(imgactor1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(imgactor4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblActor1)
                            .addComponent(lblActor2)
                            .addComponent(lblActor3)
                            .addComponent(lblActor4)))
                    .addComponent(lblNombredepelicula))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblnombredeUsuario1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(cbxestrellas, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnResena))
                                    .addComponent(txtareaReseña, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(65, 65, 65)
                        .addComponent(jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarPelicula)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegresarApantallaPrincipal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPlaylist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReseñas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblnombredeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReseñas)
                    .addComponent(lblnombredeUsuario)
                    .addComponent(btnBuscarPelicula)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPlaylist)
                    .addComponent(btnRegresarApantallaPrincipal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(lblNombredepelicula)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblpuntuacion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnañadiramiplaylist, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, Short.MAX_VALUE)
                            .addComponent(lblSinopsis))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(imgactor1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblActor1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(imgactor2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblActor2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(imgactor3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblActor3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(imgactor4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblActor4)))))
                    .addComponent(imgcaratulaimagenpelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(cbxestrellas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblnombredeUsuario1))
                    .addComponent(btnResena))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtareaReseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(192, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                        .addComponent(jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(221, 221, 221))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnReseñasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReseñasActionPerformed
        PantallaVistaReseña vistaResenas = new PantallaVistaReseña(usuarioActual);

        // 2. Obtenemos el contenedor principal (suponiendo que están dentro de un JFrame)
        // Reemplazamos el contenido actual por la nueva pantalla
        this.removeAll();
        this.setLayout(new java.awt.BorderLayout());
        this.add(vistaResenas);

        // 3. Refrescamos la interfaz
        this.revalidate();
        this.repaint();
    }//GEN-LAST:event_btnReseñasActionPerformed

    private void btnBuscarPeliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPeliculaActionPerformed
        // TODO add your handling code here:
        String query = jTextField1.getText().trim();
        if (query.isEmpty()) {
            return;
        }

        PeliculasDTO resultado = peliculaService.buscarOPersistirPelicula(query);

        if (resultado != null) {
            String nombreUsuario = lblnombredeUsuario.getText();
            Pelicula nuevaPantalla = new Pelicula(resultado, this.usuarioActual);

            java.awt.Container padre = this.getParent();

            if (padre != null) {
                padre.removeAll();
                padre.setLayout(new java.awt.BorderLayout());
                padre.add(nuevaPantalla, java.awt.BorderLayout.CENTER);

                padre.revalidate();
                padre.repaint();
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "No se encontró la película.");
        }
    }//GEN-LAST:event_btnBuscarPeliculaActionPerformed

    private void btnResenaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResenaActionPerformed

        try {
            String comentario = txtareaReseñaTexto.getText();
            int estrellas = Integer.parseInt(cbxestrellas.getSelectedItem().toString());

            ResenaDTO nuevaResena = new ResenaDTO();
            nuevaResena.setComentario(comentario);
            nuevaResena.setCalificacion(estrellas);

            nuevaResena.setUsuario(this.usuarioActual);   // Contiene el ID del usuario
            nuevaResena.setPelicula(this.peliculaActual); // Contiene el ID de la película

            resenaService.publicarResena(nuevaResena);
            cargarTablaResenas();

            JOptionPane.showMessageDialog(this, "Reseña publicada con éxito");
            txtareaReseñaTexto.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

    }//GEN-LAST:event_btnResenaActionPerformed

    private void btnañadiramiplaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnañadiramiplaylistActionPerformed
        // TODO add your handling code here:
        try {
            // 1. Obtener las playlists del usuario actual desde el servicio
            List<PlaylistDTO> misListas = playlistService.verPlaylistsUsuario(usuarioActual.getId());

            if (misListas == null || misListas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tienes ninguna playlist creada.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Preparar un ComboBox con los nombres de las listas
            // Usamos un arreglo de objetos para que el diálogo sea interactivo
            String[] nombresListas = new String[misListas.size()];
            for (int i = 0; i < misListas.size(); i++) {
                nombresListas[i] = misListas.get(i).getNombre();
            }

            // 3. Mostrar el diálogo de selección
            String seleccion = (String) JOptionPane.showInputDialog(
                    this,
                    "Selecciona una playlist para añadir '" + peliculaActual.getTitulo() + "':",
                    "Añadir a Playlist",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    nombresListas,
                    nombresListas[0]
            );

            // 4. Si el usuario seleccionó una lista y no canceló
            if (seleccion != null) {
                // Buscamos el ID de la playlist seleccionada
                int idPlaylistSeleccionada = -1;
                for (PlaylistDTO p : misListas) {
                    if (p.getNombre().equals(seleccion)) {
                        idPlaylistSeleccionada = p.getId();
                        break;
                    }
                }

                // 5. Llamar al servicio para persistir la relación
                playlistService.agregarPeliculaALista(idPlaylistSeleccionada, peliculaActual.getIdPelicula());

                JOptionPane.showMessageDialog(this, "¡Película añadida con éxito!");
            }

        } catch (Exception e) {
            // El servicio lanza excepciones si la película ya está en la lista
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnañadiramiplaylistActionPerformed

    private void btnRegresarApantallaPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarApantallaPrincipalActionPerformed
        // TODO add your handling code here:
        // 1. Creamos la instancia de la pantalla Principal pasando el usuario actual
        Principal pantallaPrincipal = new Principal(this.usuarioActual);

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
    }//GEN-LAST:event_btnRegresarApantallaPrincipalActionPerformed

    private void btnPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlaylistActionPerformed
        // TODO add your handling code here:
        PantallaPlaylist vistaPlaylist = new PantallaPlaylist(usuarioActual);

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
    private javax.swing.JTable ReseñasAajenas;
    private javax.swing.JButton btnBuscarPelicula;
    private javax.swing.JButton btnPlaylist;
    private javax.swing.JButton btnRegresarApantallaPrincipal;
    private javax.swing.JButton btnResena;
    private javax.swing.JButton btnReseñas;
    private javax.swing.JButton btnañadiramiplaylist;
    private javax.swing.JComboBox<String> cbxestrellas;
    private javax.swing.JLabel imgactor1;
    private javax.swing.JLabel imgactor2;
    private javax.swing.JLabel imgactor3;
    private javax.swing.JLabel imgactor4;
    private javax.swing.JLabel imgcaratulaimagenpelicula;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblActor1;
    private javax.swing.JLabel lblActor2;
    private javax.swing.JLabel lblActor3;
    private javax.swing.JLabel lblActor4;
    private javax.swing.JLabel lblNombredepelicula;
    private javax.swing.JLabel lblSinopsis;
    private javax.swing.JLabel lblnombredeUsuario;
    private javax.swing.JLabel lblnombredeUsuario1;
    private javax.swing.JLabel lblpuntuacion;
    private javax.swing.JScrollPane txtareaReseña;
    private javax.swing.JTextArea txtareaReseñaTexto;
    private javax.swing.JTextArea txtareaSinopsis;
    // End of variables declaration//GEN-END:variables
}
