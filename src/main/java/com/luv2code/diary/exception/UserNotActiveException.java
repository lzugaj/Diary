package com.luv2code.diary.exception;

public class UserNotActiveException extends AbstractEntityException {

    private static final long serialVersionUid = 1L;

    public UserNotActiveException(String entityName, String fieldName, String fieldValue) {
        this(entityName, fieldName, fieldValue, null);
    }

    public UserNotActiveException(String entityName, String fieldName, String fieldValue, Throwable cause) {
        super(entityName, fieldName, fieldValue, createMessage(entityName, fieldName, fieldValue), cause);
    }

    private static String createMessage(final String entityName, final String fieldName, final String fieldValue) {
        return String.format("Entity '%s' with '%s' value '%s' is not active.",
                entityName, fieldName, fieldValue);
    }
}
