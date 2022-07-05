package com.sirbiladze.homeassistaint.model.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class HttpException extends RuntimeException {

  @JsonProperty("http_status")
  private final HttpStatus httpStatus;
  private final String message;
  private final String deatiled;

  HttpException(
      @JsonProperty(value = "http_status", required = true) HttpStatus httpStatus,
      @JsonProperty(value = "message") String message,
      @JsonProperty(value = "detailed") String detailed
  ) {
    super(message);
    this.message = message;
    this.httpStatus = httpStatus;
    this.deatiled = detailed;
  }

  HttpException(
      @JsonProperty(value = "http_status", required = true) HttpStatus httpStatus,
      @JsonProperty(value = "message") String message,
      @JsonProperty(value = "detailed") String detailed,
      @JsonProperty(value = "cause") Throwable cause
  ) {
    super(message, cause);
    this.message = message;
    this.httpStatus = httpStatus;
    this.deatiled = detailed;
  }
}
