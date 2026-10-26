package ru.rtumirea.meetly.ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import ru.rtumirea.meetly.model.Room;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RoomPanel extends VBox {

    public RoomPanel(Room room, String bookingUrl) {
        super(8);
        setPadding(new Insets(64, 48, 56, 64));

        Text title = new Text(room.getName());
        title.setFont(Font.font("System", FontWeight.BOLD, 64));
        title.setFill(Palette.TEXT);
        title.wrappingWidthProperty().bind(widthProperty().subtract(112));
        title.setLineSpacing(-14);

        Text addr = new Text(room.getAddress());
        addr.setFont(Font.font("System", 20));
        addr.setFill(Palette.TEXT_MUTED);
        addr.wrappingWidthProperty().bind(widthProperty().subtract(112));

        Text seats = new Text("до " + room.getCapacity() + " человек");
        seats.setFont(Font.font("System", 20));
        seats.setFill(Palette.TEXT_MUTED);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(title, addr, seats, clock(), spacer, qr(bookingUrl));
    }

    private static Text clock() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
        Text text = new Text(LocalTime.now().format(format));
        text.setFont(Font.font("System", FontWeight.BOLD, 140));
        text.setFill(Palette.TEXT);
        VBox.setMargin(text, new Insets(40, 0, 0, 0));

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                e -> text.setText(LocalTime.now().format(format))));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        text.sceneProperty().addListener((obs, old, scene) -> {
            if (scene == null) {
                timeline.stop();
            }
        });
        return text;
    }

    private static HBox qr(String url) {
        ImageView view = new ImageView(QrCode.render(url, 8));
        view.setFitWidth(148);
        view.setFitHeight(148);
        view.setSmooth(false);

        StackPane tile = new StackPane(view);
        tile.setPadding(new Insets(10));
        tile.setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
        tile.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        Text hint = new Text("Наведите камеру,\nчтобы занять переговорку");
        hint.setFont(Font.font("System", 16));
        hint.setFill(Palette.TEXT_MUTED);

        HBox row = new HBox(20, tile, hint);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }
}
