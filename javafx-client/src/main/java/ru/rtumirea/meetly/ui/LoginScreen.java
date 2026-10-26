package ru.rtumirea.meetly.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import ru.rtumirea.meetly.model.User;
import ru.rtumirea.meetly.service.AuthService;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class LoginScreen extends StackPane {
    private final AuthService auth = new AuthService();
    private final TextField email = new TextField();
    private final PasswordField password = new PasswordField();
    private final Button submit = new Button("Войти");
    private final Text error = new Text();
    private final Consumer<User> onLogin;

    public LoginScreen(Consumer<User> onLogin) {
        this.onLogin = onLogin;

        Text title = new Text("Meetly");
        title.setFont(Font.font("System", FontWeight.BOLD, 44));
        title.setFill(Palette.TEXT);

        Text subtitle = new Text("Вход для сервисного аккаунта");
        subtitle.setFont(Font.font("System", 17));
        subtitle.setFill(Palette.TEXT_MUTED);

        email.setPromptText("Email");
        email.setStyle(Styles.FIELD);
        password.setPromptText("Пароль");
        password.setStyle(Styles.FIELD);

        submit.setStyle(Styles.BUTTON);
        submit.setMaxWidth(Double.MAX_VALUE);
        submit.setDefaultButton(true);
        submit.setOnAction(e -> login());

        error.setFont(Font.font("System", 15));
        error.setFill(Palette.ERROR);
        error.setWrappingWidth(352);

        VBox card = new VBox(14, title, subtitle, email, password, submit, error);
        VBox.setMargin(email, new Insets(16, 0, 0, 0));
        VBox.setMargin(submit, new Insets(8, 0, 0, 0));
        card.setPadding(new Insets(40, 44, 36, 44));
        card.setMaxSize(440, USE_PREF_SIZE);
        Styles.glass(card, 28);

        setAlignment(Pos.CENTER);
        getChildren().add(card);
        Platform.runLater(email::requestFocus);
    }

    private void login() {
        String emailValue = email.getText();
        String passwordValue = password.getText();
        submit.setDisable(true);
        error.setText("");

        CompletableFuture.supplyAsync(() -> auth.login(emailValue, passwordValue))
                .whenComplete((user, e) -> Platform.runLater(() -> {
                    submit.setDisable(false);
                    if (user != null) {
                        onLogin.accept(user);
                    } else if (e.getCause() instanceof IllegalArgumentException invalid) {
                        error.setText(invalid.getMessage());
                        password.clear();
                        password.requestFocus();
                    } else {
                        e.printStackTrace();
                        error.setText("Не удалось подключиться к базе данных. Проверьте, что она запущена");
                    }
                }));
    }
}
