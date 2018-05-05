package be.sint_andries.controller;

import be.sint_andries.model.Bestelling;
import be.sint_andries.model.Gerecht;
import be.sint_andries.model.Klant;
import be.sint_andries.model.Restaurantdag;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelperMethods {
    public static void ChangeScene(Event e, String path) throws IOException {      
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HelperMethods.class.getClassLoader().getResource(path));
        Parent viewParent = loader.load();
        Scene AddKlantScenen = new Scene(viewParent);

        //this line gets the stage information
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(AddKlantScenen);
        window.show();
    }

    public static <T> void ChangeSceneWithData(Event e, String path, T dataToInit) throws IOException {
        if (dataToInit instanceof Bestelling || dataToInit instanceof Gerecht || dataToInit instanceof Klant || dataToInit instanceof Restaurantdag) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HelperMethods.class.getClassLoader().getResource(path));
            Parent viewParent = loader.load();

            Scene scene = new Scene(viewParent);
            Controller controller = loader.getController();
            controller.initdata(dataToInit);

            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    public static void setbtnActive(TableView tbl, Button button, MouseEvent event, Method methodToEdit, Object o) throws IllegalAccessException, InvocationTargetException {
        if (event.getClickCount() == 2) {
            methodToEdit.invoke(o, event);
        } else {
            if (tbl.getSelectionModel().getSelectedItem() != null) {
                button.setDisable(false);
            } else {
                button.setDisable(true);
            }
        }
    }
}
