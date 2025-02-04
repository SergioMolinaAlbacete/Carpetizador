package DesarrolloInterfaces.Carpetizador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Carpetizador extends JFrame {
    
    private JButton selectFolderButton;
    private JLabel titleLabel;

    public Carpetizador() {
        // Configuración de la ventana principal
        setTitle("Guardar contenido de carpeta");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en pantalla

        // Crear un panel personalizado para dibujar la imagen de fondo
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Cargar y dibujar la imagen de fondo
                ImageIcon icon = new ImageIcon("DesarrolloInterfaces\\Carpetizador\\FONDO.jpg"); // 
                Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout()); // Cambiar a GridBagLayout para centrar los componentes

        // Crear la etiqueta con el título "CARPETIZADOR!!!"
        titleLabel = new JLabel("¡¡¡CARPETIZADOR!!!");
        titleLabel.setFont(new Font("IMPACT", Font.ROMAN_BASELINE, 24)); // Fuente y tamaño
        titleLabel.setForeground(Color.CYAN); // Cambia el color del texto

        // Crear botón
        selectFolderButton = new JButton("Seleccionar carpeta");
        selectFolderButton.setPreferredSize(new Dimension(200, 50));
        selectFolderButton.setBackground(Color.ORANGE); 
        selectFolderButton.setForeground(Color.black); // Cambia el color del texto
        selectFolderButton.setFocusPainted(false);


        // Agregar ActionListener al botón
        selectFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFolder();
            }
        });

        // Crear restricciones de diseño para los componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0); // Añadir márgenes entre componentes
        backgroundPanel.add(titleLabel, gbc); // Añadir título

        gbc.gridy = 1; // Cambiar la posición vertical para el botón
        backgroundPanel.add(selectFolderButton, gbc); // Añadir botón

        // Establecer el panel de fondo como el contenido de la ventana
        setContentPane(backgroundPanel);
    }

    // Método para seleccionar carpeta y guardar contenido
    private void selectFolder() {
        // Crear un JFileChooser para seleccionar directorios
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Solo directorios

        int option = fileChooser.showOpenDialog(this);
        
        // Si selecciona una carpeta
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            try {
                // Guardar el contenido de la carpeta en un archivo
                saveFolderContent(selectedFolder);
                JOptionPane.showMessageDialog(this, "El contenido de la carpeta se guardó correctamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para guardar el contenido de la carpeta seleccionada en un archivo de texto
    private void saveFolderContent(File folder) throws IOException {
        // Crear un archivo de texto con el nombre de la carpeta seleccionada
        String fileName = folder.getName() + ".txt";
        File outputFile = new File(fileName);

        // Crear FileWriter para escribir en el archivo
        FileWriter writer = new FileWriter(outputFile);

        // Llamar al método recursivo para obtener el contenido de la carpeta
        listFolderContent(folder, writer, "");

        // Cerrar el writer
        writer.close();
    }

    // Método recursivo para listar el contenido de la carpeta
    private void listFolderContent(File folder, FileWriter writer, String indent) throws IOException {
        // Obtener la lista de archivos y carpetas dentro de la carpeta seleccionada
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                // Escribir el nombre del archivo o carpeta en el archivo de texto
                writer.write(indent + (file.isDirectory() ? "[Carpeta] " : "[Archivo] ") + file.getName() + "\n");
                
                // Si es un directorio, listar recursivamente su contenido
                if (file.isDirectory()) {
                    listFolderContent(file, writer, indent + "    "); // Aumentar la indentación
                }
            }
        }
    }

    public static void main(String[] args) {
        // Ejecutar la aplicación en el hilo de la interfaz gráfica (Event Dispatch Thread)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Carpetizador app = new Carpetizador();
                app.setVisible(true);
            }
        });
    }
}
