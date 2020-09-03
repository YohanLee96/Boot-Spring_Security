//package com.study.login.global.error;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class SecurityExceptionHandler  {
//
//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<?> ExpiredJwtExceptionHandle(ExpiredJwtException ex) {
//
//        ErrorMessage errorMessage = ErrorMessage.builder()
//                .code(ErrorCode.JWT_EXPIRE_DATE.getCode())
//                .message(ErrorCode.JWT_EXPIRE_DATE.getMessage())
//                .build();
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(errorMessage);
//    }
//
//}
