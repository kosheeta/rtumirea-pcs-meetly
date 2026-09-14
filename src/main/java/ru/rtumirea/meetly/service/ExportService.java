package ru.rtumirea.meetly.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.rtumirea.meetly.model.Booking;
import ru.rtumirea.meetly.model.Room;
import ru.rtumirea.meetly.model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportService {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final UserService userService;
    private final RoomService roomService;
    private final BookingService bookingService;

    public ExportService() {
        this.userService = new UserService();
        this.roomService = new RoomService();
        this.bookingService = new BookingService();
    }

    public void exportToExcel(String filePath) {

        try (Workbook workbook = new XSSFWorkbook()) {

            writeUsersSheet(workbook);
            writeRoomsSheet(workbook);
            writeBookingsSheet(workbook);

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeUsersSheet(Workbook workbook) {

        Sheet sheet = workbook.createSheet("users");
        writeHeader(sheet, "id", "name", "email");

        List<User> users = userService.findAll();

        int rowNum = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getName());
            row.createCell(2).setCellValue(user.getEmail());
        }

        autoSizeColumns(sheet, 3);
    }

    private void writeRoomsSheet(Workbook workbook) {

        Sheet sheet = workbook.createSheet("rooms");
        writeHeader(sheet, "id", "name", "capacity", "address");

        List<Room> rooms = roomService.findAll();

        int rowNum = 1;
        for (Room room : rooms) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(room.getId());
            row.createCell(1).setCellValue(room.getName());
            row.createCell(2).setCellValue(room.getCapacity());
            row.createCell(3).setCellValue(room.getAddress());
        }

        autoSizeColumns(sheet, 4);
    }

    private void writeBookingsSheet(Workbook workbook) {

        Sheet sheet = workbook.createSheet("bookings");
        writeHeader(sheet, "id", "user_id", "room_id", "start_time", "end_time", "status");

        List<Booking> bookings = bookingService.findAll();

        int rowNum = 1;
        for (Booking booking : bookings) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(booking.getId());
            row.createCell(1).setCellValue(booking.getUserId());
            row.createCell(2).setCellValue(booking.getRoomId());
            row.createCell(3).setCellValue(booking.getStartTime().format(DATE_TIME_FORMAT));
            row.createCell(4).setCellValue(booking.getEndTime().format(DATE_TIME_FORMAT));
            row.createCell(5).setCellValue(booking.getStatus().getDbValue());
        }

        autoSizeColumns(sheet, 6);
    }

    private void writeHeader(Sheet sheet, String... columns) {

        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        Row header = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
