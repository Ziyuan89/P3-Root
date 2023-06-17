package h3.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ControlPane extends Pane {

    private final Button nextStepButton = new Button("Next Step");

    public ControlPane() {
        setPadding(new Insets(5));
    }

    public void init(Animation animation) {
        getChildren().add(nextStepButton);

        nextStepButton.setOnAction(event -> {
            synchronized (animation.getSyncObject()) {

                animation.getSyncObject().notify();
            }
        });
    }

    public void disableNextStepButton() {
        nextStepButton.setDisable(true);
    }

}
