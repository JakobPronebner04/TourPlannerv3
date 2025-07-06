package at.jp.tourplanner.utils;

import java.util.*;
import java.util.stream.*;

public class SupportedLocales {

    private static final String BUNDLE_BASE_NAME = "at.jp.tourplanner.i18n";

    public static List<Locale> getSupportedLocales() {
        Locale[] commonLocales = {
                Locale.ENGLISH,
                Locale.GERMAN
        };

        return Arrays.stream(commonLocales)
                .filter(locale -> {
                    try {
                        ResourceBundle bundle = ResourceBundle.getBundle(
                                BUNDLE_BASE_NAME,
                                locale
                        );
                        return bundle.getLocale().getLanguage().equals(locale.getLanguage());
                    } catch (MissingResourceException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
