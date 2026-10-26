package ru.rtumirea.meetly.ui;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

final class Avatars {
    private Avatars() {
    }

    private static final String[] FILES = {"men-32.jpg", "women-44.jpg", "men-75.jpg", "women-68.jpg", "men-12.jpg"};

    static String forUser(int userId) {
        return FILES[Math.floorMod(userId, FILES.length)];
    }

    static Circle circle(String resource, double diameter) {
        Image image = new Image(Avatars.class.getResource("/avatars/" + resource).toExternalForm(),
                diameter * 2, diameter * 2, true, true);
        Circle circle = new Circle(diameter / 2, new ImagePattern(image));
        return circle;
    }
}
