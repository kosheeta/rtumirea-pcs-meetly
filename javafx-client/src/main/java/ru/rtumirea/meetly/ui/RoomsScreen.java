package ru.rtumirea.meetly.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import ru.rtumirea.meetly.model.Room;
import ru.rtumirea.meetly.model.User;
import ru.rtumirea.meetly.repository.RoomRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class RoomsScreen extends BorderPane {
    private static final double TILE_WIDTH = 280;

    private final RoomRepository rooms = new RoomRepository();
    private final FlowPane tiles = new FlowPane(20, 20);
    private final Consumer<Room> onOpen;

    public RoomsScreen(User account, Consumer<Room> onOpen, Runnable onLogout) {
        this.onOpen = onOpen;
        setPadding(new Insets(48, 64, 48, 64));

        Text title = new Text("Переговорные");
        title.setFont(Font.font("System", FontWeight.BOLD, 44));
        title.setFill(Palette.TEXT);

        Text who = new Text(account.getName());
        who.setFont(Font.font("System", 15));
        who.setFill(Palette.TEXT_MUTED);

        Button logout = new Button("Выйти");
        logout.setStyle(Styles.BUTTON_GHOST);
        logout.setOnAction(e -> onLogout.run());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(16, new VBox(6, title), spacer, who, logout);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 32, 0));
        setTop(header);

        tiles.setPadding(new Insets(4, 16, 32, 4));
        ScrollPane scroll = new ScrollPane(tiles);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        setCenter(scroll);

        load();
    }

    private void load() {
        CompletableFuture.supplyAsync(rooms::findAll)
                .whenComplete((list, e) -> Platform.runLater(() -> {
                    if (e != null) {
                        e.printStackTrace();
                        tiles.getChildren().setAll(message("Не удалось загрузить переговорные. Проверьте, что база запущена"));
                    } else if (list.isEmpty()) {
                        tiles.getChildren().setAll(message("В базе нет переговорных"));
                    } else {
                        show(list);
                    }
                }));
    }

    private void show(List<Room> list) {
        tiles.getChildren().setAll(list.stream().map(this::tile).toList());
    }

    private VBox tile(Room room) {
        Text name = new Text(room.getName());
        name.setFont(Font.font("System", FontWeight.BOLD, 28));
        name.setFill(Palette.TEXT);
        name.setWrappingWidth(TILE_WIDTH - 48);

        Text addr = new Text(room.getAddress());
        addr.setFont(Font.font("System", 16));
        addr.setFill(Palette.TEXT_MUTED);
        addr.setWrappingWidth(TILE_WIDTH - 48);

        Text seats = new Text("до " + room.getCapacity() + " человек");
        seats.setFont(Font.font("System", 16));
        seats.setFill(Palette.TEXT_MUTED);

        VBox tile = new VBox(8, name, addr, seats);
        tile.setPadding(new Insets(24));
        tile.setPrefWidth(TILE_WIDTH);
        tile.setStyle("-fx-cursor: hand;");
        Styles.glass(tile, 24);
        tile.setOnMouseClicked(e -> onOpen.accept(room));
        return tile;
    }

    private static Text message(String text) {
        Text t = new Text(text);
        t.setFont(Font.font("System", 18));
        t.setFill(Palette.TEXT_MUTED);
        return t;
    }
}
