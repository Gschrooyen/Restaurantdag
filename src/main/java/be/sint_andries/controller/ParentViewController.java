package be.sint_andries.controller;

import be.sint_andries.model.Gerecht;

import java.net.URL;
import java.util.ResourceBundle;

public class ParentViewController extends Controller {
    public AddRestaurantdagViewController restaurantdagview;
    public AddNewGerechtViewController gerechtview;


    @Override
    public <T> void initdata(T dataToInit) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        restaurantdagview.inti(this);
        gerechtview.init(this);
    }

    public void addHoofdGerecht(Gerecht g){
        restaurantdagview.lvAlleHoofd.getItems().add(g);
        restaurantdagview.lvGeselHoofd.getItems().add(g);
    }

    public void addDessert(Gerecht g){
        restaurantdagview.lvAlleDes.getItems().add(g);
        restaurantdagview.lvGeselDes.getItems().add(g);
    }
}
