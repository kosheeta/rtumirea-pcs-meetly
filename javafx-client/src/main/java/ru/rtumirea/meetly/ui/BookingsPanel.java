package ru.rtumirea.meetly.ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import ru.rtumirea.meetly.model.Booking;
import ru.rtumirea.meetly.model.User;
import ru.rtumirea.meetly.repository.BookingRepository;
import ru.rtumirea.meetly.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookingsPanel extends VBox {
    private static final int LIMIT = 3;
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("dd.MM");

    private final int roomId;
    private final BookingRepository bookings = new BookingRepository();
    private final UserRepository users = new UserRepository();
    private final ExecutorService loader = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "bookings-loader");
        thread.setDaemon(true);
        return thread;
    });

    public BookingsPanel(int roomId) {
        super(18);
        this.roomId = roomId;
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(40, 56, 40, 24));

        refresh();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(30), e -> refresh()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        sceneProperty().addListener((obs, old, scene) -> {
            if (scene == null) {
                timeline.stop();
                loader.shutdown();
            }
        });
    }

    private void refresh() {
        loader.submit(() -> {
            try {
                List<BookingCard> cards = load();
                Platform.runLater(() -> show(cards.isEmpty()
                        ? List.of(message("Ближайших броней нет"))
                        : cards.stream().map(c -> (Node) c).toList()));
            } catch (RuntimeException e) {
                Platform.runLater(() -> show(List.of(message("Не удалось загрузить брони. Проверьте, что база запущена"))));
            }
        });
    }

    private List<BookingCard> load() {
        LocalDateTime now = LocalDateTime.now();
        return bookings.findUpcomingByRoom(roomId, now, LIMIT).stream().map(b -> card(b, now)).toList();
    }

    private BookingCard card(Booking booking, LocalDateTime now) {
        User organizer = users.findById(booking.getUserId());
        List<BookingCard.Participant> people = new ArrayList<>();
        people.add(participant(organizer, booking.getUserId(), true));
        users.findParticipants(booking.getId()).stream()
                .filter(u -> u.getId() != booking.getUserId())
                .forEach(u -> people.add(participant(u, u.getId(), false)));

        String title = booking.getTitle() != null ? booking.getTitle() : "Без названия";
        LocalDate day = booking.getStartTime().toLocalDate();
        if (!day.equals(now.toLocalDate())) {
            title = DAY.format(day) + ", " + title;
        }
        return new BookingCard(booking.getStartTime().toLocalTime(), booking.getEndTime().toLocalTime(), title, people);
    }

    private static BookingCard.Participant participant(User user, int userId, boolean organizer) {
        String name = user != null ? user.getName() : "Неизвестный пользователь";
        return new BookingCard.Participant(name, Avatars.forUser(userId), organizer);
    }

    private void show(List<Node> content) {
        Text heading = new Text("Ближайшие брони");
        heading.setFont(Font.font("System", FontWeight.BOLD, 22));
        heading.setFill(Palette.TEXT_MUTED);
        getChildren().setAll(heading);
        getChildren().addAll(content);
    }

    private static Text message(String text) {
        Text t = new Text(text);
        t.setFont(Font.font("System", 18));
        t.setFill(Palette.TEXT_MUTED);
        return t;
    }
}
