package p3.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class AnimationScene extends Scene {

    public AnimationScene(Parent root) {
        super(root);
    }

    public abstract String getTitle();
}
