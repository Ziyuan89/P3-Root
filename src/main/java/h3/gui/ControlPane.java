package h3.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ControlPane<N> extends Pane {

    private final Button nextStepButton = new Button("Next Step");

    public ControlPane() {
        setPadding(new Insets(5));
    }

    public void init(Animation<N> animation) {
        getChildren().add(nextStepButton);

        nextStepButton.setOnAction(event -> {
            synchronized (animation.getSyncObject()) {

                animation.getSyncObject().notify();
            }
        });
    }

}
