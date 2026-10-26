package ru.rtumirea.meetly.ui;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

final class Styles {
    static final String FIELD = """
            -fx-background-color: rgba(255,255,255,0.08);
            -fx-border-color: rgba(255,255,255,0.22);
            -fx-background-radius: 14; -fx-border-radius: 14;
            -fx-text-fill: #f4f1ff; -fx-prompt-text-fill: rgba(244,241,255,0.45);
            -fx-highlight-fill: #5a37d6; -fx-font-size: 16px; -fx-padding: 12 16;
            """;
    static final String BUTTON = """
            -fx-background-color: #5a37d6; -fx-background-radius: 14;
            -fx-text-fill: #f4f1ff; -fx-font-size: 16px; -fx-font-weight: bold;
            -fx-padding: 12 24; -fx-cursor: hand;
            """;
    static final String BUTTON_GHOST = """
            -fx-background-color: rgba(255,255,255,0.08); -fx-background-radius: 14;
            -fx-border-color: rgba(255,255,255,0.22); -fx-border-radius: 14;
            -fx-text-fill: #f4f1ff; -fx-font-size: 15px; -fx-padding: 10 20; -fx-cursor: hand;
            """;

    private Styles() {
    }

    static void glass(Region region, double radius) {
        region.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.rgb(255, 255, 255, 0.22)),
                        new Stop(1, Color.rgb(255, 255, 255, 0.06))),
                new CornerRadii(radius), Insets.EMPTY)));
        region.setBorder(new Border(new BorderStroke(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.rgb(255, 255, 255, 0.55)),
                        new Stop(1, Color.rgb(255, 255, 255, 0.08))),
                BorderStrokeStyle.SOLID, new CornerRadii(radius), new BorderWidths(1.2))));
        region.setEffect(new DropShadow(30, 0, 10, Color.rgb(0, 0, 0, 0.35)));
    }
}
