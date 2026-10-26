package ru.rtumirea.meetly.ui;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Map;

final class QrCode {
    private QrCode() {
    }

    static Image render(String text, int scale) {
        BitMatrix matrix;
        try {
            matrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, 0, 0,
                    Map.of(EncodeHintType.MARGIN, 1, EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M));
        } catch (WriterException e) {
            throw new IllegalArgumentException("Не удалось построить QR-код для " + text, e);
        }
        int size = matrix.getWidth() * scale;
        WritableImage image = new WritableImage(size, size);
        PixelWriter pw = image.getPixelWriter();
        Color dark = Color.web("#15102b");
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                pw.setColor(x, y, matrix.get(x / scale, y / scale) ? dark : Color.WHITE);
            }
        }
        return image;
    }
}
