package controller;

import model.*;
import view.*;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.io.FileWriter;

public class GestionDeContactosController {

    private ContactosModel model;
    private GestionDeContactosView view;
    private TableRowSorter sorter;

    public GestionDeContactosController(ContactosModel model, GestionDeContactosView view) {
        this.model = model;
        this.view = view;

        sorter = new TableRowSorter(view.modeloTabla);
        view.tabla.setRowSorter(sorter);

        eventos();
        SwingUtilities.invokeLater(() -> {
            view.progressBar.setVisible(false);
        });

        cargarContactos();
    }

    private void eventos() {

        // BOTÓN AGREGAR
        view.btnAgregar.addActionListener(e -> agregar());

        // EXPORTAR
        view.btnExportar.addActionListener(e -> exportar());

        // FILTRO
        view.txtBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter(view.txtBuscar.getText()));
            }
        });

        // ATAJO CTRL + N
        view.getRootPane().registerKeyboardAction(
                e -> agregar(),
                KeyStroke.getKeyStroke("control N"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // CLICK DERECHO
        JPopupMenu menu = new JPopupMenu();
        JMenuItem eliminar = new JMenuItem("Eliminar");

        eliminar.addActionListener(e -> eliminar());
        menu.add(eliminar);

        view.tabla.setComponentPopupMenu(menu);
    }

    private void agregar() {
    	
    	/// Evita guardar datos vacios 
    	
        if (view.txtNombre.getText().isEmpty() ||
                view.txtTelefono.getText().isEmpty() ||
                view.txtEmail.getText().isEmpty()) {

                JOptionPane.showMessageDialog(view, "Complete todos los campos");
                return;
            }

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() throws Exception {

                // 🔥 MOSTRAR BARRA
                SwingUtilities.invokeLater(() -> {
                    view.progressBar.setVisible(true);
                    view.progressBar.setIndeterminate(true);
                });

                Thread.sleep(600); // simulación de proceso

                return null;
            }

            @Override
            protected void done() {

                SwingUtilities.invokeLater(() -> {

                    contactos c = new contactos(
                            view.txtNombre.getText(),
                            view.txtTelefono.getText(),
                            view.txtEmail.getText()
                    );

                    model.agregar(c);

                    view.agregarFila(new Object[]{
                            c.getNombre(),
                            c.getTelefono(),
                            c.getEmail()
                    });
                    
                    //limpia los campos
                    view.txtNombre.setText("");
                    view.txtTelefono.setText("");
                    view.txtEmail.setText("");

              
                    view.progressBar.setIndeterminate(false);
                    view.progressBar.setVisible(false);
                });
            }
        };

        worker.execute();
    }

    private void eliminar() {

        int fila = view.getFilaSeleccionada();

        if (fila >= 0) {
            model.eliminar(fila);
            view.eliminarFila(fila);
        }
    }
    private void cargarContactos() {

        SwingWorker<Void, contactos> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() throws Exception {

                // 🔥 MOSTRAR BARRA (FORZADO UI THREAD)
                SwingUtilities.invokeLater(() -> {
                    view.progressBar.setVisible(true);
                    view.progressBar.setIndeterminate(true);
                });

                Thread.sleep(1200); // simulación de carga

                return null;
            }

            @Override
            protected void done() {

                // 🔥 OCULTAR BARRA
                SwingUtilities.invokeLater(() -> {
                    view.progressBar.setIndeterminate(false);
                    view.progressBar.setVisible(false);
                });
            }
        };

        worker.execute();
    }
 
    private void exportar() {

        try {
            FileWriter writer = new FileWriter("contactos.csv");

            for (contactos c : model.getLista()) {
                writer.write(
                        c.getNombre() + "," +
                        c.getTelefono() + "," +
                        c.getEmail() + "\n"
                );
            }

            writer.close();

            JOptionPane.showMessageDialog(view,
                    "Exportación exitosa");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}