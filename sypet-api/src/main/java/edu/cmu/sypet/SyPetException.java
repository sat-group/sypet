package edu.cmu.sypet;

public class SyPetException extends Exception {

  public SyPetException() {
  }

  public SyPetException(final String message) {
    super(message);
  }

  public SyPetException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public SyPetException(final Throwable cause) {
    super(cause);
  }

  public SyPetException(
      final String message,
      final Throwable cause,
      final boolean enableSuppression,
      final boolean writableStackTrace
  ) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
