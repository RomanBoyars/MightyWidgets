package com.mightywidgets.error;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldValidationError extends InnerError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public static FieldValidationErrorBulder newBuilder() {
        return new FieldValidationError().new FieldValidationErrorBulder();
    }

    public String getField() {
        return field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public String getMessage() {
        return message;
    }

    public String getObject() {
        return object;
    }

    public final class FieldValidationErrorBulder {

        private FieldValidationErrorBulder() {

        }

        public FieldValidationErrorBulder withObject(String object) {
            FieldValidationError.this.object = object;
            return this;
        }

        public FieldValidationErrorBulder withField(String field) {
            FieldValidationError.this.field = field;
            return this;
        }

        public FieldValidationErrorBulder withRejectedValue(Object rejectedValue) {
            FieldValidationError.this.rejectedValue = rejectedValue;
            return this;
        }

        public FieldValidationErrorBulder withMessage(String message) {
            FieldValidationError.this.message = message;
            return this;
        }

        public FieldValidationError build() {
            return FieldValidationError.this;
        }
    }

}
