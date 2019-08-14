package edu.cmu.reachability.petrinet;

public class NoSuchFlowException extends RuntimeException {

  public NoSuchFlowException() {}

  public NoSuchFlowException(String message) {
    super(message);
  }

  public NoSuchFlowException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoSuchFlowException(Throwable cause) {
    super(cause);
  }

  public NoSuchFlowException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

