package io.teamchallenge.constant;

public final class ExceptionMessage {
    public static final String PRODUCT_NOT_FOUND_BY_ID = "The product with id: %s is not present in database";
    public static final String BRAND_NOT_FOUND_BY_ID = "The brand with id: %s is not present in database";
    public static final String CATEGORY_NOT_FOUND_BY_ID = "The category with id: %s is not present in database";
    public static final String PRODUCT_WITH_NAME_ALREADY_EXISTS =
        "The product with name: %s is already present in database";
    public static final String PRODUCT_PERSISTENCE_EXCEPTION
        = "The product with such attributes can't be created due to following reasons: "
        + "attributes duplication or non-existence";
}
