package edu.cmu.sypet;

public class SyPetException extends Exception {

  public SyPetException() {
  }

  public SyPetException(String message) {
    super(message);
  }

  public SyPetException(String message, Throwable cause) {
    super(message, cause);
  }

  public SyPetException(Throwable cause) {
    super(cause);
  }

  public SyPetException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
