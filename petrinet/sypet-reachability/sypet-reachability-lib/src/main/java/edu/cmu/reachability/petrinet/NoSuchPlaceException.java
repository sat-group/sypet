package edu.cmu.reachability.petrinet;

public class NoSuchPlaceException extends RuntimeException {

  public NoSuchPlaceException() {}

  public NoSuchPlaceException(String message) {
    super(message);
  }

  public NoSuchPlaceException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoSuchPlaceException(Throwable cause) {
    super(cause);
  }

  public NoSuchPlaceException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
