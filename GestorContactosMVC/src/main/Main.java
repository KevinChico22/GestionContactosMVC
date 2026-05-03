package main;

import model.*;
import view.*;
import controller.*;

import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

         // cambia el estilo visual
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ContactosModel model = new ContactosModel();
        GestionDeContactosView view = new GestionDeContactosView("es", model);
        new GestionDeContactosController(model, view);
        

        view.setVisible(true);
    }
}