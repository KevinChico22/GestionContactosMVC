package model;

import java.util.ArrayList;

public class ContactosModel {

    private ArrayList<contactos> lista = new ArrayList<>();
    
    private final Object bloqueo = new Object();

    public synchronized void agregar(contactos c) {

        synchronized (bloqueo) {

            lista.add(c);
        }
    }
    
    public synchronized void eliminar(int index) {

        synchronized (bloqueo) {

            lista.remove(index);
        }
    }
    
    public synchronized ArrayList<contactos> getLista() {

        synchronized (bloqueo) {

            return lista;
        }
    }
    
 // edición segura de contactos
    public synchronized void editarContacto(
            int index,
            contactos nuevoContacto
    ) {

        synchronized (bloqueo) {

            lista.set(index, nuevoContacto);
        }
    }
    
    //validar si el contacto ya existe 
    public boolean existeContacto(String nombre, String telefono, String email) {

        for (contactos c : lista) {

            if (
                    c.getNombre().equalsIgnoreCase(nombre) ||
                    c.getTelefono().equalsIgnoreCase(telefono) ||
                    c.getEmail().equalsIgnoreCase(email)
            ) {

                return true;
            }
        }

        return false;
    }
    
    
    
}