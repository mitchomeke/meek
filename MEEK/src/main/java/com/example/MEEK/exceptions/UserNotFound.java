package com.example.MEEK.exceptions;

public class UserNotFound extends RuntimeException {
  public UserNotFound(Long message) {
    super("User with id: "+message+"not found.");
  }
}
