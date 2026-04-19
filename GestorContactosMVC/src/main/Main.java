package main;

import model.*;
import view.*;
import controller.*;

public class Main {

    public static void main(String[] args) {

        ContactosModel model = new ContactosModel();
        GestionDeContactosView view = new GestionDeContactosView();

        new GestionDeContactosController(model, view);

        view.setVisible(true);
    }
}