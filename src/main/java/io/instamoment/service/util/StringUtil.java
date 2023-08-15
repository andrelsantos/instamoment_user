package io.instamoment.service.util;

public class StringUtil {
    public static String getNameInitials(String name) {
        if (name == null) {
            return "";
        }
        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
        name = name.replaceAll(characterFilter, "");
        String[] nameSplit = name.trim().split(" ");
        return name.trim().isEmpty() ? "" : String.format("%s%s", nameSplit[0].charAt(0), nameSplit[nameSplit.length - 1].charAt(0));
    }

    public static String getNameInitialCaps(String name) {
        if (name == null) {
            return "";
        }
        return String.format("%s%s", name.charAt(0), name.substring(1).toLowerCase());
    }
}
