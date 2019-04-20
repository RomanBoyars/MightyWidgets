package com.mightywidgets.error;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldValidationError extends InnerError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public static FieldValidationErrorBuilder newBuilder() {
        return new FieldValidationError().new FieldValidationErrorBuilder();
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

    public final class FieldValidationErrorBuilder {

        private FieldValidationErrorBuilder() {

        }

        public FieldValidationErrorBuilder withObject(String object) {
            FieldValidationError.this.object = object;
            return this;
        }

        public FieldValidationErrorBuilder withField(String field) {
            FieldValidationError.this.field = field;
            return this;
        }

        public FieldValidationErrorBuilder withRejectedValue(Object rejectedValue) {
            FieldValidationError.this.rejectedValue = rejectedValue;
            return this;
        }

        public FieldValidationErrorBuilder withMessage(String message) {
            FieldValidationError.this.message = message;
            return this;
        }

        public FieldValidationError build() {
            return FieldValidationError.this;
        }
    }

}
