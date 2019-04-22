/**
 *
 */
package com.mightywidgets.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class WidgetErrorAdvice extends ResponseEntityExceptionHandler {

    Logger log = LoggerFactory.getLogger(WidgetErrorAdvice.class);

    /**
     * Handles custom {@link WidgetNotFoundException}
     *
     * @param e the exception
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(WidgetNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(WidgetNotFoundException e) {
        ApiError apiError = ApiError.newBuilder()
                .withStatus(HttpStatus.NOT_FOUND)
                .withMessage(e.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        ApiError apiError =
                ApiError.newBuilder().withMessage(error)
                        .withStatus(HttpStatus.BAD_REQUEST)
                        .withDebugMessage(ex.getLocalizedMessage())
                        .build();
        return buildErrorResponseEntity(apiError);
    }

    /**
     * Handles {@link RuntimeException}
     *
     * @param e the exception
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        ApiError apiError = ApiError.newBuilder()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .withMessage(e.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    /**
     * Handles @Valid error
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return {@link ResponseEntity} instance with brief information about validation error
     */
    @Override
    protected ResponseEntity<Object>
    handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                 HttpHeaders headers,
                                 HttpStatus status, WebRequest request) {

        List<InnerError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> FieldValidationError.newBuilder()
                        .withObject(x.getObjectName())
                        .withField(x.getField())
                        .withRejectedValue(x.getRejectedValue())
                        .withMessage(x.getDefaultMessage())
                        .build()
                )
                .collect(Collectors.toList());

        ApiError apiError = ApiError.newBuilder()
                .withStatus(status)
                .withMessage("Validation error")
                .withInnerErrors(errors)
                .build();
        return buildErrorResponseEntity(apiError);
    }

    /**
     * Handles JSON mapping error.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@link ResponseEntity} instance with information about error
     */
    @Override
    protected ResponseEntity<Object>
    handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = ApiError.newBuilder()
                .withStatus(status)
                .withMessage("Malformed JSON. Values does not match their types or they are bigger than JSON format allows.")
                .withDebugMessage(ex.getLocalizedMessage()).build();
        return buildErrorResponseEntity(apiError);
    }

    /**
     * Builds {@link ResponseEntity} object from {@link ApiError} object
     * @param apiError {@link ApiError}  object from which to build {@link ResponseEntity} object
     * @return {@link ResponseEntity}
     */
    private ResponseEntity<Object> buildErrorResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
