package com.example.MEEK.exceptions;

public class MusicNotFound extends RuntimeException {
    public MusicNotFound(Long message) {
        super("Music of this id not found: "+ message);
    }
}
