package com.example.catalog.utilities;

import java.util.Collection;
import java.util.stream.Collectors;

public class StringUtil {

    public static String getStringCollection(Collection<String> strings) {
        if (strings == null) return "";
        return strings.stream().sorted().collect(Collectors.joining(", "));
    }

}
