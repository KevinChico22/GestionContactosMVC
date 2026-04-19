package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionDeContactosView extends JFrame {

    public JTable tabla;
    public DefaultTableModel modeloTabla;
    public JButton btnAgregar, btnExportar;
    public JTextField txtNombre, txtTelefono, txtEmail, txtBuscar;
    public JProgressBar progressBar;

    public GestionDeContactosView() {

        setTitle("Gestión de Contactos");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        JTabbedPane tabs = new JTabbedPane();

   
        JPanel panel = new JPanel(new BorderLayout());

        modeloTabla = new DefaultTableModel(
                new String[]{"Nombre", "Teléfono", "Email"}, 0);
        

        tabla = new JTable(modeloTabla);
        tabla.setAutoCreateRowSorter(true);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        
        JPanel form = new JPanel(new GridLayout(2, 4));

        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        txtBuscar = new JTextField();

        form.add(new JLabel("Nombre"));
        form.add(new JLabel("Teléfono"));
        form.add(new JLabel("Email"));
        form.add(new JLabel("Buscar"));

        form.add(txtNombre);
        form.add(txtTelefono);
        form.add(txtEmail);
        form.add(txtBuscar);

        panel.add(form, BorderLayout.NORTH);

 
        JPanel botones = new JPanel();

        btnAgregar = new JButton("Agregar");
        btnExportar = new JButton("Exportar CSV");

        botones.add(btnAgregar);
        botones.add(btnExportar);

        
        JPanel inferior = new JPanel(new BorderLayout());

        progressBar = new JProgressBar();
        progressBar.setVisible(true);
        progressBar.setPreferredSize(new java.awt.Dimension(100, 20));

        inferior.add(botones, BorderLayout.CENTER);
        inferior.add(progressBar, BorderLayout.SOUTH);

        panel.add(inferior, BorderLayout.SOUTH);
        
      
        JPanel stats = new JPanel();
        stats.add(new JLabel("Estadísticas próximamente"));

        tabs.addTab("Contactos", panel);
        tabs.addTab("Estadísticas", stats);

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