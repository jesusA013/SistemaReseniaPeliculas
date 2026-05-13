/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Pantallas;

import IntegracionApi.TMDBApiAdapter;
import IntegracionApi.TMDBResponse;
import dtos.ActorDTO;
import dtos.PeliculasDTO;
import dtos.UsuarioDTO;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import interfaces.IPeliculaService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import servicios.PeliculaService;

/**
 *
 * @author golea
 */
public class Principal extends javax.swing.JPanel {

    /**
     * Creates new form Principal
     */
    private UsuarioDTO usuarioLogueado;
    private IPeliculaService peliculaService;

    public Principal(UsuarioDTO usuario) {
        initComponents();
        this.usuarioLogueado = usuario;
        lblnombredeUsuario.setText(usuario.getNombre());
        this.peliculaService = new servicios.PeliculaService();
        configurarDiseñoResponsivo();
        javax.swing.JButton[] botonesPeli = {btnNombrePeli0, btnNombrePeli1, btnNombrePeli2, btnNombrePeli3};
        for (javax.swing.JButton btn : botonesPeli) {
            btn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buscarYNavegar(btn.getText());
                }
            });
        }
        cargarActores();
        cargarCaratulas();
    }

    private void cargarCaratulas() {
        List<PeliculasDTO> lista = peliculaService.obtenerUltimasPeliculas();

        javax.swing.JLabel[] labelsImagen = {
            imgCaratulaPelicula,
            imgCaratulaPelicula1,
            imgCaratulaPelicula2,
            imgCaratulaPelicula3
        };

        javax.swing.JButton[] labelsTitulo = {
            btnNombrePeli0,
            btnNombrePeli1,
            btnNombrePeli2,
            btnNombrePeli3
        };

        for (int i = 0; i < lista.size() && i < labelsImagen.length; i++) {
            PeliculasDTO peli = lista.get(i);

            if (peli.getPosterPath() != null && !peli.getPosterPath().isEmpty()) {
                String urlCompleta = "https://image.tmdb.org/t/p/w500" + peli.getPosterPath();
                pintarImagen(labelsImagen[i], urlCompleta);
            }

            if (i < labelsTitulo.length) {
                labelsTitulo[i].setText(peli.getTitulo());
                labelsTitulo[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            }
        }
    }

    private void pintarImagen(JLabel lbl, String urlStr) {
        try {
            URL url = new URL(urlStr);
            Image img = ImageIO.read(url);

            // Si getWidth es 0 (antes de renderizar), usamos el nuevo tamaño grande
            int ancho = lbl.getWidth() > 0 ? lbl.getWidth() : 280;
            int alto = lbl.getHeight() > 0 ? lbl.getHeight() : 400;

            Image imgEscalada = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(imgEscalada));
            lbl.setText("");
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
        }
    }

    private void cargarActores() {
        Daos.ActorDAO actorDAO = new Daos.ActorDAO();
        java.util.List<Entidades.Actor> listaActores = actorDAO.listarUltimosActores(6);
        javax.swing.JLabel[] labelsNombre = {
            lblActor1, lblActor2, lblActor3,
            lblActor4, lblActor5, lblActor6
        };

        javax.swing.JLabel[] labelsImagen = {
            imgactornombre1, imgactornombre2, imgactornombre3,
            imgactornombre4, imgactornombre5, imgactornombre6
        };

        for (int j = 0; j < labelsNombre.length; j++) {
            labelsNombre[j].setText("");
            labelsImagen[j].setIcon(null);
        }

        for (int i = 0; i < listaActores.size() && i < labelsNombre.length; i++) {
            Entidades.Actor actor = listaActores.get(i);

            labelsNombre[i].setText(actor.getNombre());

            if (actor.getRutaPerfil() != null && !actor.getRutaPerfil().isEmpty()) {
                String urlCompleta = "https://image.tmdb.org/t/p/w185" + actor.getRutaPerfil();
                pintarImagen(labelsImagen[i], urlCompleta);
            } else {
                labelsImagen[i].setText("Sin foto");
            }
        }
    }

    private void buscarYNavegar(String titulo) {
        if (titulo != null && !titulo.isEmpty()) {
            PeliculasDTO resultado = peliculaService.buscarOPersistirPelicula(titulo);

            if (resultado != null) {
                String nombreUsuario = lblnombredeUsuario.getText();
                Pelicula pantallaPeli = new Pelicula(resultado, this.usuarioLogueado);
                java.awt.Container contenedor = this.getParent();
                if (contenedor != null) {
                    contenedor.setLayout(new java.awt.BorderLayout());
                    contenedor.removeAll();
                    contenedor.add(pantallaPeli, java.awt.BorderLayout.CENTER);
                    contenedor.revalidate();
                    contenedor.repaint();
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "No se encontró la película: " + titulo);
            }
        }
    }

    private void actualizarImagen(JLabel label, String ruta) {
        if (ruta != null && !ruta.isEmpty()) {
            ImageIcon icon = new ImageIcon(ruta);
            // El secreto es label.getWidth() y label.getHeight()
            Image img = icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        }
    }

    private void configurarDiseñoResponsivo() {
        // Fondo General: #FFFFFF (Blanco)
        this.setBackground(new Color(255, 255, 255));
        this.removeAll();
        this.setLayout(new BorderLayout());

        // --- 1. BARRA SUPERIOR ---
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(255, 255, 255)); // Fondo Blanco
        panelNorte.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel seccionUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        seccionUsuario.setOpaque(false);

        // Texto Principal: #000000 (Negro)
        lblnombredeUsuario.setForeground(new Color(0, 0, 0));
        lblnombredeUsuario.setFont(new Font("SansSerif", Font.BOLD, 22));

        configurarBotonMenuSuperior(btnPlaylist);
        configurarBotonMenuSuperior(btnReseñas);

        seccionUsuario.add(lblnombredeUsuario);
        seccionUsuario.add(btnPlaylist);
        seccionUsuario.add(btnReseñas);

        JPanel seccionBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        seccionBusqueda.setOpaque(false);

        jTextField1.setPreferredSize(new Dimension(300, 35));
        jTextField1.setBackground(new Color(235, 235, 235)); // Placeholder/Fondo input #EBEBEB

        // Botones Principales: #333333 con texto blanco
        btnBuscarPelicula.setPreferredSize(new Dimension(120, 35));
        btnBuscarPelicula.setBackground(new Color(51, 51, 51));
        btnBuscarPelicula.setForeground(Color.WHITE);
        btnBuscarPelicula.setFocusPainted(false);
        btnBuscarPelicula.setBorder(BorderFactory.createEmptyBorder());

        seccionBusqueda.add(jTextField1);
        seccionBusqueda.add(btnBuscarPelicula);

        panelNorte.add(seccionUsuario, BorderLayout.WEST);
        panelNorte.add(seccionBusqueda, BorderLayout.EAST);

        // --- 2. CONTENIDO CENTRAL ---
        JPanel contenedorPrincipal = new JPanel();
        contenedorPrincipal.setLayout(new BoxLayout(contenedorPrincipal, BoxLayout.Y_AXIS));
        contenedorPrincipal.setOpaque(false);
        contenedorPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // A. SECCIÓN PELÍCULAS
        JLabel[] posters = {imgCaratulaPelicula, imgCaratulaPelicula1, imgCaratulaPelicula2, imgCaratulaPelicula3};
        JButton[] nombres = {btnNombrePeli0, btnNombrePeli1, btnNombrePeli2, btnNombrePeli3};

        JPanel panelPelis = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        panelPelis.setOpaque(false);

        Dimension tamañoPeli = new Dimension(280, 400);

        for (int i = 0; i < 4; i++) {
            JPanel card = new JPanel(new BorderLayout(0, 10));
            card.setOpaque(false);

            // Placeholder de Imágenes: #EBEBEB
            posters[i].setOpaque(true);
            posters[i].setBackground(new Color(235, 235, 235));
            posters[i].setPreferredSize(tamañoPeli);
            posters[i].setMinimumSize(tamañoPeli);
            posters[i].setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1)); // Gris suave

            // Texto Películas: #000000
            nombres[i].setFont(new Font("SansSerif", Font.BOLD, 16));
            nombres[i].setForeground(new Color(0, 0, 0));
            nombres[i].setContentAreaFilled(false);
            nombres[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            card.add(posters[i], BorderLayout.CENTER);
            card.add(nombres[i], BorderLayout.SOUTH);
            panelPelis.add(card);
        }

        // B. SECCIÓN ACTORES
        JLabel[] fotosActores = {imgactornombre1, imgactornombre2, imgactornombre3, imgactornombre4, imgactornombre5, imgactornombre6};
        JLabel[] nombresActores = {lblActor1, lblActor2, lblActor3, lblActor4, lblActor5, lblActor6};

        JPanel panelActores = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelActores.setOpaque(false);

        for (int i = 0; i < 6; i++) {
            JPanel cardActor = new JPanel(new BorderLayout(0, 10));
            cardActor.setOpaque(false);

            fotosActores[i].setOpaque(true);
            fotosActores[i].setBackground(new Color(235, 235, 235)); // Placeholder #EBEBEB
            fotosActores[i].setPreferredSize(new Dimension(150, 150));

            nombresActores[i].setFont(new Font("SansSerif", Font.PLAIN, 14));
            nombresActores[i].setForeground(new Color(51, 51, 51)); // Gris oscuro #333333
            nombresActores[i].setHorizontalAlignment(JLabel.CENTER);

            cardActor.add(fotosActores[i], BorderLayout.CENTER);
            cardActor.add(nombresActores[i], BorderLayout.SOUTH);
            panelActores.add(cardActor);
        }

        // Añadir al contenedor
        JLabel tituloPelis = crearEtiquetaSeccion("PELÍCULAS DESTACADAS");
        tituloPelis.setForeground(new Color(0, 0, 0)); // Negro
        tituloPelis.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedorPrincipal.add(tituloPelis);

        contenedorPrincipal.add(Box.createVerticalStrut(20));
        panelPelis.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedorPrincipal.add(panelPelis);

        contenedorPrincipal.add(Box.createVerticalStrut(50));

        JLabel tituloActores = crearEtiquetaSeccion("REPARTO PRINCIPAL");
        tituloActores.setForeground(new Color(0, 0, 0)); // Negro
        tituloActores.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedorPrincipal.add(tituloActores);

        contenedorPrincipal.add(Box.createVerticalStrut(20));
        panelActores.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedorPrincipal.add(panelActores);

        JScrollPane scroll = new JScrollPane(contenedorPrincipal);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(25);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(new Color(255, 255, 255)); // Fondo Blanco

        this.add(panelNorte, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }

    private void configurarBotonMenuSuperior(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(new Color(51, 51, 51)); // Texto #333333
        btn.setContentAreaFilled(false);
        // Borde inferior usando Botones Secundarios: #E1E1E1
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(225, 225, 225)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
    }

// Método auxiliar para títulos de sección
    private JLabel crearEtiquetaSeccion(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // Centra texto
        label.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra componente en BoxLayout
        return label;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imgCaratulaPelicula = new javax.swing.JLabel();
        imgCaratulaPelicula1 = new javax.swing.JLabel();
        imgCaratulaPelicula2 = new javax.swing.JLabel();
        imgCaratulaPelicula3 = new javax.swing.JLabel();
        lblEstrenos = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblActor1 = new javax.swing.JLabel();
        lblActor2 = new javax.swing.JLabel();
        lblActor3 = new javax.swing.JLabel();
        lblActor4 = new javax.swing.JLabel();
        lblActor5 = new javax.swing.JLabel();
        lblActor6 = new javax.swing.JLabel();
        btnReseñas = new javax.swing.JButton();
        lblnombredeUsuario = new javax.swing.JLabel();
        btnBuscarPelicula = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        btnPlaylist = new javax.swing.JButton();
        btnNombrePeli0 = new javax.swing.JButton();
        btnNombrePeli1 = new javax.swing.JButton();
        btnNombrePeli2 = new javax.swing.JButton();
        btnNombrePeli3 = new javax.swing.JButton();
        imgactornombre1 = new javax.swing.JLabel();
        imgactornombre2 = new javax.swing.JLabel();
        imgactornombre3 = new javax.swing.JLabel();
        imgactornombre4 = new javax.swing.JLabel();
        imgactornombre5 = new javax.swing.JLabel();
        imgactornombre6 = new javax.swing.JLabel();

        imgCaratulaPelicula.setText("jLabel1");

        imgCaratulaPelicula1.setText("jLabel1");

        imgCaratulaPelicula2.setText("jLabel1");

        imgCaratulaPelicula3.setText("jLabel1");

        lblEstrenos.setText("ESTRENOS:");

        jLabel1.setText("Actores Recientes:");

        lblActor1.setText("ActorNombre1");

        lblActor2.setText("ActorNombre2");

        lblActor3.setText("ActorNombre3");

        lblActor4.setText("ActorNombre4");

        lblActor5.setText("ActorNombre5");

        lblActor6.setText("ActorNombre6");

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

        btnPlaylist.setText("Playlist");
        btnPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlaylistActionPerformed(evt);
            }
        });

        btnNombrePeli0.setText("jButton1");
        btnNombrePeli0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNombrePeli0ActionPerformed(evt);
            }
        });

        btnNombrePeli1.setText("jButton1");

        btnNombrePeli2.setText("jButton1");

        btnNombrePeli3.setText("jButton1");

        imgactornombre1.setText("jLabel2");

        imgactornombre2.setText("jLabel2");

        imgactornombre3.setText("jLabel2");

        imgactornombre4.setText("jLabel2");

        imgactornombre5.setText("jLabel2");

        imgactornombre6.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(lblEstrenos))
                                .addGap(599, 599, 599))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(imgactornombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblActor1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(imgactornombre2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblActor2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(imgactornombre3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblActor3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(52, 52, 52)
                                        .addComponent(imgactornombre6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(37, 37, 37)
                                        .addComponent(lblActor6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(imgactornombre4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblActor4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(56, 56, 56)
                                        .addComponent(imgactornombre5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(33, 33, 33)
                                        .addComponent(lblActor5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarPelicula)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPlaylist)
                        .addGap(204, 204, 204))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(imgCaratulaPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(imgCaratulaPelicula1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(imgCaratulaPelicula2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(imgCaratulaPelicula3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(61, 61, 61))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnReseñas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblnombredeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
            .addGroup(layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(btnNombrePeli0, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnNombrePeli1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(btnNombrePeli2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(btnNombrePeli3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReseñas)
                    .addComponent(lblnombredeUsuario)
                    .addComponent(btnBuscarPelicula)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPlaylist))
                .addGap(67, 67, 67)
                .addComponent(lblEstrenos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imgCaratulaPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imgCaratulaPelicula1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imgCaratulaPelicula2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imgCaratulaPelicula3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNombrePeli0)
                    .addComponent(btnNombrePeli1)
                    .addComponent(btnNombrePeli2)
                    .addComponent(btnNombrePeli3))
                .addGap(102, 102, 102)
                .addComponent(jLabel1)
                .addGap(66, 66, 66)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblActor1)
                    .addComponent(lblActor4)
                    .addComponent(lblActor5)
                    .addComponent(imgactornombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imgactornombre4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imgactornombre5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imgactornombre2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblActor3)
                    .addComponent(lblActor2)
                    .addComponent(lblActor6)
                    .addComponent(imgactornombre3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imgactornombre6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

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

    private void btnBuscarPeliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPeliculaActionPerformed

        String titulo = jTextField1.getText().trim();

        if (!titulo.isEmpty()) {
            PeliculasDTO resultado = peliculaService.buscarOPersistirPelicula(titulo);

            if (resultado != null) {
                String nombreUsuario = lblnombredeUsuario.getText();
                Pelicula pantallaPeli = new Pelicula(resultado, this.usuarioLogueado);
                java.awt.Container contenedor = this.getParent();

                if (contenedor != null) {
                    contenedor.setLayout(new java.awt.BorderLayout());
                    contenedor.removeAll();

                    contenedor.add(pantallaPeli, java.awt.BorderLayout.CENTER);

                    contenedor.revalidate();
                    contenedor.repaint();
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "No se encontró la película.");
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Escriba un nombre de película.");
        }

    }//GEN-LAST:event_btnBuscarPeliculaActionPerformed

    private void btnNombrePeli0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNombrePeli0ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnNombrePeli0ActionPerformed

    private void btnPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlaylistActionPerformed
        // TODO add your handling code here:
        PantallaPlaylist vistaPlaylist = new PantallaPlaylist(usuarioLogueado);

        // 2. Obtenemos el contenedor padre (donde están alojados los JPanels)
        java.awt.Container panelpadre = this.getParent();

        if (panelpadre != null) {
            // 3. Limpiamos lo que hay actualmente (la pantalla Principal)
            panelpadre.removeAll();
            PantallaPlaylist pp = new PantallaPlaylist(usuarioLogueado);
            panelpadre.setLayout(new java.awt.BorderLayout());
            panelpadre.add(pp, java.awt.BorderLayout.CENTER);
            panelpadre.revalidate();
            panelpadre.repaint();
        }
    }//GEN-LAST:event_btnPlaylistActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarPelicula;
    private javax.swing.JButton btnNombrePeli0;
    private javax.swing.JButton btnNombrePeli1;
    private javax.swing.JButton btnNombrePeli2;
    private javax.swing.JButton btnNombrePeli3;
    private javax.swing.JButton btnPlaylist;
    private javax.swing.JButton btnReseñas;
    private javax.swing.JLabel imgCaratulaPelicula;
    private javax.swing.JLabel imgCaratulaPelicula1;
    private javax.swing.JLabel imgCaratulaPelicula2;
    private javax.swing.JLabel imgCaratulaPelicula3;
    private javax.swing.JLabel imgactornombre1;
    private javax.swing.JLabel imgactornombre2;
    private javax.swing.JLabel imgactornombre3;
    private javax.swing.JLabel imgactornombre4;
    private javax.swing.JLabel imgactornombre5;
    private javax.swing.JLabel imgactornombre6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblActor1;
    private javax.swing.JLabel lblActor2;
    private javax.swing.JLabel lblActor3;
    private javax.swing.JLabel lblActor4;
    private javax.swing.JLabel lblActor5;
    private javax.swing.JLabel lblActor6;
    private javax.swing.JLabel lblEstrenos;
    private javax.swing.JLabel lblnombredeUsuario;
    // End of variables declaration//GEN-END:variables
}
