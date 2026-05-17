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
        // Editar
        view.btnEditar.addActionListener(e -> editarContacto());

     // BÚSQUEDA EN SEGUNDO PLANO
        view.txtBuscar.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {

                SwingWorker<Void, Void> worker = new SwingWorker<>() {

                    @Override
                    protected Void doInBackground() throws Exception {

                        // simulación de búsqueda pesada
                        Thread.sleep(300);

                        return null;
                    }

                    @Override
                    protected void done() {

                        SwingUtilities.invokeLater(() -> {

                            String textoBusqueda = view.txtBuscar.getText();

                            // si está vacío muestra todo
                            if (textoBusqueda.trim().isEmpty()) {

                                sorter.setRowFilter(null);

                            } else {

                                sorter.setRowFilter(
                                RowFilter.regexFilter(
                               "(?i)" + textoBusqueda
                                        )
                                );
                            }
                        });
                    }
                };

                // INICIA EL HILO
                worker.execute();
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

        // validar campos vacíos
        if (view.txtNombre.getText().isEmpty() ||
                view.txtTelefono.getText().isEmpty() ||
                view.txtEmail.getText().isEmpty()) {

            JOptionPane.showMessageDialog(view,
                    "Complete todos los campos");

            return;
        }

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {

            @Override
            protected Boolean doInBackground() throws Exception {

                // mostrar barra
                SwingUtilities.invokeLater(() -> {
                    view.progressBar.setVisible(true);
                    view.progressBar.setIndeterminate(true);
                    mostrarNotificacion("Contacto guardado con éxito");
                });

                Thread.sleep(600);

                // verificacion en segundo plano
                return model.existeContacto(
                        view.txtNombre.getText(),
                        view.txtTelefono.getText(),
                        view.txtEmail.getText()
                );
            }

            @Override
            protected void done() {

                try {

                    boolean existe = get();

                    // si ya existe
                    if (existe) {

                        JOptionPane.showMessageDialog(
                                view,
                                "El contacto ya está registrado"
                        );

                        view.progressBar.setVisible(false);

                        return;
                    }

                    // crear contacto
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

                    // limpiar campos
                    view.txtNombre.setText("");
                    view.txtTelefono.setText("");
                    view.txtEmail.setText("");

                    // ocultar barra
                    view.progressBar.setIndeterminate(false);
                    view.progressBar.setVisible(false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }
    
    private void editarContacto() {

        int fila = view.getFilaSeleccionada();

        // validar selección
        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    view,
                    "Seleccione un contacto"
            );

            return;
        }

        // bloqueo de edición
        synchronized (model) {

            String nuevoNombre = JOptionPane.showInputDialog(
                    view,
                    "Nuevo nombre:",
                    view.tabla.getValueAt(fila, 0)
            );

            String nuevoTelefono = JOptionPane.showInputDialog(
                    view,
                    "Nuevo teléfono:",
                    view.tabla.getValueAt(fila, 1)
            );

            String nuevoEmail = JOptionPane.showInputDialog(
                    view,
                    "Nuevo email:",
                    view.tabla.getValueAt(fila, 2)
            );

            // validación
            if (nuevoNombre == null ||
                    nuevoTelefono == null ||
                    nuevoEmail == null) {

                return;
            }

            // THREAD DE EDICIÓN
            SwingWorker<Void, Void> worker = new SwingWorker<>() {

                @Override
                protected Void doInBackground() throws Exception {

                    Thread.sleep(800);

                    contactos actualizado = new contactos(
                            nuevoNombre,
                            nuevoTelefono,
                            nuevoEmail
                    );

                    model.editarContacto(fila, actualizado);

                    return null;
                }

                @Override
                protected void done() {

                    SwingUtilities.invokeLater(() -> {

                        // actualizar tabla
                        view.modeloTabla.setValueAt(
                                nuevoNombre,
                                fila,
                                0
                        );

                        view.modeloTabla.setValueAt(
                                nuevoTelefono,
                                fila,
                                1
                        );

                        view.modeloTabla.setValueAt(
                                nuevoEmail,
                                fila,
                                2
                        );

                        mostrarNotificacion(
                                "Contacto editado correctamente"
                        );
                    });
                }
            };

            worker.execute();
        }
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
 // NOTIFICACIONES
    private void mostrarNotificacion(String mensaje) {

        Thread hilo = new Thread(() -> {

            SwingUtilities.invokeLater(() -> {
                view.lblNotificacion.setText(mensaje);
            });

            try {

                // duración visible
                Thread.sleep(3000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                view.lblNotificacion.setText(" ");
            });
        });

        hilo.start();
    }
    
    private synchronized void exportar() {

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() throws Exception {

                SwingUtilities.invokeLater(() -> {
                    view.progressBar.setVisible(true);
                    view.progressBar.setIndeterminate(true);
                });

                synchronized (this) {

                    FileWriter writer = new FileWriter("contactos.csv");

                    Thread.sleep(1000);

                    for (contactos c : model.getLista()) {

                        writer.write(
                                c.getNombre() + "," +
                                c.getTelefono() + "," +
                                c.getEmail() + "\n"
                        );
                    }

                    writer.close();
                }

                return null;
            }

            @Override
            protected void done() {

                SwingUtilities.invokeLater(() -> {

                    view.progressBar.setIndeterminate(false);
                    view.progressBar.setVisible(false);

                    mostrarNotificacion("Exportación completada");
                    
                });
            }
        };

        worker.execute();
    }
}