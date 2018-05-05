package be.sint_andries.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {

    /**
     * the child classes are responsible for checking the class of dataToInit
     *
     * @param dataToInit de data die geinitialiseerd moet worden
     */
    public abstract <T> void initdata(T dataToInit);

    @Override
    public abstract void initialize(URL location, ResourceBundle resources);
}
