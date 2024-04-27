package io.teamchallenge.constant;

public final class ExceptionMessage {
    public static final String USER_NOT_FOUND_BY_EMAIL = "There is no user with email: ";
    public static final String COUNTRY_NOT_FOUND_BY_NAME = "There is no country with name: ";
    public static final String PASSWORD_DOES_NOT_MATCH = "Password does not match.";
    public static final String PRODUCT_NOT_FOUND_BY_ID = "The product with id: %s is not present in database";
    public static final String BRAND_NOT_FOUND_BY_ID = "The brand with id: %s is not present in database";
    public static final String CATEGORY_NOT_FOUND_BY_ID = "The category with id: %s is not present in database";
    public static final String PRODUCT_WITH_NAME_ALREADY_EXISTS =
        "The product with name: %s is already present in database";
    public static final String PRODUCT_CREATION_EXCEPTION
        = "The product with such attributes can't be created due to following reasons: "
        + "attributes duplication and non-existence";
    public static final String ATTRIBUTE_VALUE_NOT_FOUND_BY_ID =
        "The attribute value with id: %s is not present in database";
}
