package is.hi.skemmti.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class ApiException extends ErrorResponseException {

  public ApiException(HttpStatus status, String message) {
    super(status, ProblemDetail.forStatusAndDetail(status, message), null);
  }

  public static class NotFound extends ApiException {
    public NotFound(String message) {
      super(HttpStatus.NOT_FOUND, message);
    }
  }

  public static class InvalidData extends ApiException {
    public InvalidData(String message) {
      super(HttpStatus.BAD_REQUEST, message);
    }
  }

  public static class Conflict extends ApiException {
    public Conflict(String message) {
      super(HttpStatus.CONFLICT, message);
    }
  }
}
