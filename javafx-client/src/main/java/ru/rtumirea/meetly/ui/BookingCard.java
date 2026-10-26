package ru.rtumirea.meetly.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingCard extends VBox {
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");
    private static final double RADIUS = 24;
    private static final double AVATAR = 40;

    public record Participant(String name, String avatar, boolean organizer) {
    }

    public BookingCard(LocalTime start, LocalTime end, String title, List<Participant> participants) {
        super(12);
        setPadding(new Insets(20, 24, 20, 24));
        setMaxWidth(Double.MAX_VALUE);

        Styles.glass(this, RADIUS);

        Text time = new Text(TIME.format(start) + " – " + TIME.format(end));
        time.setFont(Font.font("System", FontWeight.BOLD, 40));
        time.setFill(Palette.TEXT);

        Text name = new Text(title);
        name.setFont(Font.font("System", FontWeight.MEDIUM, 18));
        name.setFill(Palette.TEXT);

        getChildren().addAll(time, name, participantsRow(participants));
    }

    private static HBox participantsRow(List<Participant> participants) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 0, 0, 0));

        participants.stream().filter(Participant::organizer).findFirst()
                .ifPresent(o -> row.getChildren().add(Avatars.circle(o.avatar(), AVATAR)));

        HBox others = new HBox(-12);
        others.setAlignment(Pos.CENTER_LEFT);
        participants.stream().filter(p -> !p.organizer())
                .forEach(p -> others.getChildren().add(Avatars.circle(p.avatar(), AVATAR)));
        row.getChildren().add(others);
        return row;
    }
}
