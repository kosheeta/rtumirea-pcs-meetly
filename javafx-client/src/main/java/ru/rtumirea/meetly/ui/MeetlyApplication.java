package ru.rtumirea.meetly.ui;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import ru.rtumirea.meetly.model.Room;
import ru.rtumirea.meetly.model.User;

public class MeetlyApplication extends Application {
    private final Pane background = background();
    private final StackPane root = new StackPane(background);
    private Scene scene;
    private User account;

    @Override
    public void start(Stage stage) {
        scene = new Scene(root, 1280, 800);
        showLogin();

        stage.setTitle("Meetly");
        stage.setScene(scene);
        stage.show();
    }

    private void showLogin() {
        account = null;
        show(new LoginScreen(user -> {
            account = user;
            showRooms();
        }));
    }

    private void showRooms() {
        show(new RoomsScreen(account, this::showRoom, this::showLogin));
    }

    private void showRoom(Room room) {
        show(content(room));
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                showRooms();
            }
        });
    }

    private void show(Node screen) {
        scene.setOnKeyPressed(null);
        root.getChildren().setAll(background, screen);
    }

    private static Pane background() {
        Pane pane = new Pane();
        pane.setMouseTransparent(true);
        pane.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 0.9, 0.9, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#1c0f47")),
                        new Stop(0.45, Color.web("#0f0922")),
                        new Stop(1, Color.web("#0a0813"))),
                null, null)));
        return pane;
    }

    private static GridPane content(Room room) {
        GridPane grid = new GridPane();
        ColumnConstraints half = new ColumnConstraints();
        half.setPercentWidth(50);
        grid.getColumnConstraints().addAll(half, half);
        RowConstraints row = new RowConstraints();
        row.setVgrow(Priority.ALWAYS);
        grid.getRowConstraints().add(row);
        grid.add(new RoomPanel(room, "https://example.com"), 0, 0);
        grid.add(new BookingsPanel(room.getId()), 1, 0);
        return grid;
    }
}
