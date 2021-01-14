package com.luv2code.diary.exception;

import java.util.function.Supplier;

public class EntityNotFoundException extends AbstractEntityException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String entityName, String fieldName, String fieldValue) {
        this(entityName, fieldName, fieldValue, null);
    }

    public EntityNotFoundException(String entityName, String fieldName, String fieldValue, Throwable cause) {
        super(entityName, fieldName, fieldValue, createMessage(entityName, fieldName, fieldValue), cause);
    }

    private static String createMessage(String entityName, String fieldName, String fieldValue) {
        return String.format("Entity '%s' with '%s' value '%s' not founded.",
                entityName, fieldName, fieldValue);
    }
}
