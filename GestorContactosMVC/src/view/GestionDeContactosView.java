package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import model.ContactosModel;
import controller.GestionDeContactosController;

public class GestionDeContactosView extends JFrame {

	///idioma
	private ResourceBundle bundle;
	
	private String idiomaActual;
	
	private ContactosModel model;
	
	// colores 
	private final Color COLOR_FONDO = Color.decode("#ECF0F1");
	private final Color COLOR_PRIMARIO = Color.decode("#2C3E50");
	private final Color COLOR_SECUNDARIO = Color.decode("#3498DB");
	private final Color COLOR_BOTON = Color.decode("#2980B9");
	private final Color COLOR_TEXTO = Color.decode("#2C3E50");
	
    public JTable tabla;
    public DefaultTableModel modeloTabla;
    public JButton btnAgregar, btnExportar;
    public JTextField txtNombre, txtTelefono, txtEmail, txtBuscar;
    public JProgressBar progressBar;

    public GestionDeContactosView(String idioma, ContactosModel model) {
        this.model = model;
        this.idiomaActual = idioma;
    
    	/// ingles 
        Locale locale = new Locale(idioma);
        bundle = ResourceBundle.getBundle("resources.messages", locale);

        setTitle(bundle.getString("titulo"));
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
        //Fondo 
        getContentPane().setBackground(COLOR_FONDO);
        Font fuente = new Font("Segoe UI", Font.PLAIN, 14);


        JTabbedPane tabs = new JTabbedPane();

   // panel principal 
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);

        ///cambio a ingles de la tabla
        modeloTabla = new DefaultTableModel(
        	    new String[]{
        	        bundle.getString("nombre"),
        	        bundle.getString("telefono"),
        	        bundle.getString("email")
        	    }, 0);
        
        tabla = new JTable(modeloTabla);
        
        // mejora de la tabla
        tabla.setFont(fuente);
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        tabla.setAutoCreateRowSorter(true);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Mejora del formulario 
        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBackground(COLOR_FONDO);
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        txtBuscar = new JTextField();
        
    /// mejora campos de texto     
        txtNombre.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtTelefono.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtEmail.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtBuscar.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // aplicacion de fuentes 
        txtNombre.setFont(fuente);
        txtTelefono.setFont(fuente);
        txtEmail.setFont(fuente);
        txtBuscar.setFont(fuente);
        
        
        //actualiza para ingles
        form.add(new JLabel(bundle.getString("nombre")));
        form.add(new JLabel(bundle.getString("telefono")));
        form.add(new JLabel(bundle.getString("email")));
        form.add(new JLabel(bundle.getString("buscar")));

        form.add(txtNombre);
        form.add(txtTelefono);
        form.add(txtEmail);
        form.add(txtBuscar);

        panel.add(form, BorderLayout.NORTH);

 // mejora de los botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(COLOR_FONDO);
        
        //cambio de idioma
        String[] idiomas = {"ES", "EN", "FR"};
        JComboBox<String> comboIdioma = new JComboBox<>(idiomas);
        
        //guarda el cambio de idioma 
        if (idiomaActual.equals("es")) {
            comboIdioma.setSelectedItem("ES");
        } else if (idiomaActual.equals("en")) {
            comboIdioma.setSelectedItem("EN");
        } else {
            comboIdioma.setSelectedItem("FR");
        }

        
        //CAmbio
        comboIdioma.addActionListener(e -> {

            String idiomaSeleccionado = comboIdioma.getSelectedItem().toString();

            if (idiomaSeleccionado.equals("ES")) {
                idiomaSeleccionado = "es";
            } else if (idiomaSeleccionado.equals("EN")) {
                idiomaSeleccionado = "en";
            } else {
                idiomaSeleccionado = "fr";
            }

            dispose();

            GestionDeContactosView nuevaVista = new GestionDeContactosView(idiomaSeleccionado, model);
            new GestionDeContactosController(model, nuevaVista);

            nuevaVista.setVisible(true);
        });
        
        
        
        btnAgregar = new JButton(bundle.getString("agregar"));
        btnExportar = new JButton(bundle.getString("exportar"));
        
        /// iconos y tamaño
    
        ImageIcon iconAgregar = new ImageIcon(getClass().getResource("/icons/add.png"));
        Image imgAgregar = iconAgregar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnAgregar.setIcon(new ImageIcon(imgAgregar));

        ImageIcon iconExportar = new ImageIcon(getClass().getResource("/icons/export.png"));
        Image imgExportar = iconExportar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnExportar.setIcon(new ImageIcon(imgExportar));

      /// Posicion 
        btnAgregar.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnExportar.setHorizontalTextPosition(SwingConstants.RIGHT);
        
        
        //aplicacion de colores en botones
        btnAgregar.setBackground(COLOR_SECUNDARIO);
        btnAgregar.setForeground(Color.WHITE);

        btnExportar.setBackground(COLOR_PRIMARIO);
        btnExportar.setForeground(Color.WHITE);

        botones.add(btnAgregar);
        botones.add(btnExportar);
        
        //boton de ingles 
        botones.add(comboIdioma);

        
        JPanel inferior = new JPanel(new BorderLayout());

        progressBar = new JProgressBar();
        
        //barra de progreso mejora
        progressBar.setVisible(false);
        progressBar.setForeground(COLOR_SECUNDARIO);
        
        progressBar.setPreferredSize(new java.awt.Dimension(100, 20));
        

        inferior.add(botones, BorderLayout.CENTER);
        inferior.add(progressBar, BorderLayout.SOUTH);

        panel.add(inferior, BorderLayout.SOUTH);
        
      
        JPanel stats = new JPanel();
        stats.setBackground(COLOR_FONDO); // mejora con color 
        
        stats.add(new JLabel("Estadísticas próximamente"));

        tabs.addTab(bundle.getString("contactos"), panel);
        tabs.addTab(bundle.getString("estadisticas"), stats);

        add(tabs);
    }

  
    public void agregarFila(Object[] fila) {
        modeloTabla.addRow(fila);
    }

    public void eliminarFila(int fila) {
        modeloTabla.removeRow(fila);
    }

    public int getFilaSeleccionada() {
        return tabla.getSelectedRow();
    }

    public void mostrarProgreso(boolean estado) {
        progressBar.setIndeterminate(estado);
    }
}