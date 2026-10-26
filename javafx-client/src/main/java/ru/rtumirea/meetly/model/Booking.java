package ru.rtumirea.meetly.model;

import java.time.LocalDateTime;

public class Booking {
    private Integer id;
    private int userId;
    private int roomId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;

    public Booking() {
    }

    public Booking(Integer id, int userId, int roomId, String title, LocalDateTime startTime, LocalDateTime endTime, BookingStatus status) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
