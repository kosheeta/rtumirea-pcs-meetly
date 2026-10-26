package ru.rtumirea.meetly.exception;

public abstract class MeetlyException extends RuntimeException {
    protected MeetlyException(String message) {
        super(message);
    }
}
