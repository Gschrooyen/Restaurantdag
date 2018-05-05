package be.sint_andries.controller.CelFactories;


import be.sint_andries.model.Gerecht;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public final class GerechtListCel implements javafx.util.Callback<ListView<Gerecht>, ListCell<Gerecht>> {

    @Override
    public ListCell<Gerecht> call(ListView param) {
        return new ListCell<Gerecht>() {
            @Override
            protected void updateItem(Gerecht item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty && item != null) {
                    setText(item.getNaam());
                } else {
                    setText(null);
                }
            }
        };
    }
}
