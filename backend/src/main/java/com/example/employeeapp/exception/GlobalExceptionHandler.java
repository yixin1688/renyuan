package com.example.employeeapp.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(ex.getMessage()));
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<Map<String, Object>> handleBusiness(BusinessException ex) {
    return ResponseEntity.badRequest().body(errorBody(ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    String message = "请求参数校验失败";
    if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
      FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);
      message = fieldError.getDefaultMessage();
    }
    return ResponseEntity.badRequest().body(errorBody(message));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraint(ConstraintViolationException ex) {
    return ResponseEntity.badRequest().body(errorBody(ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleOther(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody("服务器内部错误"));
  }

  private Map<String, Object> errorBody(String message) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("success", false);
    body.put("message", message);
    return body;
  }
}

