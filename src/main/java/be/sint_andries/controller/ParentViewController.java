package be.sint_andries.controller;

import be.sint_andries.model.Gerecht;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ResourceBundle;

public class ParentViewController extends Controller {
    public AddRestaurantdagViewController restaurantdagViewController;
    public AddNewGerechtViewController gerechtViewController;
    public Tab tabGerecht;
    public Tab tabResto;

    @Override
    public <T> void initdata(T dataToInit) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            restaurantdagViewController.init(this);
        }).start();
    }

    public void addHoofdGerecht(Gerecht g){
        restaurantdagViewController.lvAlleHoofd.getItems().add(g);
        restaurantdagViewController.lvGeselHoofd.getItems().add(g);
        tabResto.getTabPane().requestFocus();
    }

    public void addDessert(Gerecht g){
        restaurantdagViewController.lvAlleDes.getItems().add(g);
        restaurantdagViewController.lvGeselDes.getItems().add(g);
        tabResto.getTabPane().requestFocus();
    }

    public void niewDessert(){
        gerechtViewController.cbxDessert.setSelected(true);
        gerechtViewController.txtPrijs.setDisable(true);
        tabGerecht.getTabPane().requestFocus();
    }

    public void niewHoofdgerecht(){
        tabGerecht.getTabPane().requestFocus();
    }
}
