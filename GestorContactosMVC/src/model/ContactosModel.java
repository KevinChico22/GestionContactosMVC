package model;

import java.util.ArrayList;

public class ContactosModel {

    private ArrayList<contactos> lista = new ArrayList<>();

    public void agregar(contactos c) {
        lista.add(c);
    }

    public void eliminar(int index) {
        lista.remove(index);
    }

    public ArrayList<contactos> getLista() {
        return lista;
    }
}