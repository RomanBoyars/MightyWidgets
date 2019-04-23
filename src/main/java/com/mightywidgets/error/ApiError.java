/*

Created 20.04.2019

 */

package com.mightywidgets.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * represents any error.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String debugMessage;
    private List<InnerError> innerErrors;

    public static ApiErrorBuilder newBuilder() {
        return new ApiError().new ApiErrorBuilder();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public List<InnerError> getInnerErrors() {
        return innerErrors;
    }

    public final class ApiErrorBuilder {

        private ApiErrorBuilder() {
            ApiError.this.timestamp = LocalDateTime.now();
            ApiError.this.status = HttpStatus.INTERNAL_SERVER_ERROR; //by default status is 500 if not defined by
            // builder
        }

        public ApiErrorBuilder withStatus(HttpStatus status) {
            ApiError.this.status = status;
            return this;
        }

        public ApiErrorBuilder withMessage(String message) {
            ApiError.this.message = message;
            return this;
        }

        public ApiErrorBuilder withDebugMessage(String debugMessage) {
            ApiError.this.debugMessage = debugMessage;
            return this;
        }

        public ApiErrorBuilder withInnerErrors(List<InnerError> innerErrors) {
            ApiError.this.innerErrors = innerErrors;
            return this;
        }

        public ApiError build() {
            return ApiError.this;
        }
    }

}
