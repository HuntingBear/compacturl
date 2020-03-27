package ru.testtask.compacturl.util;

import org.apache.commons.validator.routines.UrlValidator;

public class Validator {
    private static final String[] schemes = {"http", "https"};

    public static boolean isUrlValid(String url) {
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }
}
