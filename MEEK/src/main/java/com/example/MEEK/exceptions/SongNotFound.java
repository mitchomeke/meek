package com.example.MEEK.exceptions;

public class SongNotFound extends RuntimeException {
    public SongNotFound(Long message) {
        super("Song with id: "+message+"not found.\n");
    }
}
