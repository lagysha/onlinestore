package io.teamchallenge.constant;

public final class ExceptionMessage {
    public static final String USER_NOT_FOUND_BY_EMAIL = "There is no user with email: %s";
    public static final String COUNTRY_NOT_FOUND_BY_NAME = "There is no country with name: %s";
    public static final String PASSWORD_DOES_NOT_MATCH = "Password does not match.";
    public static final String USER_WITH_EMAIL_ALREADY_EXISTS =
        "There is already registered user with same email address: %s";
    public static final String USER_WITH_PHONE_NUMBER_ALREADY_EXISTS =
        "There is already registered user with same phone number: %s";
    public static final String PRODUCT_NOT_FOUND_BY_ID = "The product with id: %s is not present in database";
    public static final String BRAND_NOT_FOUND_BY_ID = "The brand with id: %s is not present in database";
    public static final String CATEGORY_NOT_FOUND_BY_ID = "The category with id: %s is not present in database";
    public static final String PRODUCT_WITH_NAME_ALREADY_EXISTS =
        "The product with name: %s is already present in database";
    public static final String PRODUCT_PERSISTENCE_EXCEPTION
        = "The product with such attributes can't be created due to following reasons: "
        + "attributes duplication or non-existence";
    public static final String TOKEN_HAS_BEEN_EXPIRED = "Token has been expired.";
    public static final String TOKEN_DOES_NOT_CONTAIN_SUBJECT = "Token does not contain subject.";
    public static final String TOKEN_WAS_NOT_SIGNED_BY_USER = "Token was not signed using user's key.";
    public static final String TOKEN_CAN_NOT_BE_PARSED = "Token can not be parsed.";
    public static final String CARTITEM_ALREADY_EXISTS =
        "The cartItem with id: %s is already present in database";
    private static final String CARTITEM_NOT_FOUND_BY_ID = "The cartItem with id: %s is not present in database";
}
