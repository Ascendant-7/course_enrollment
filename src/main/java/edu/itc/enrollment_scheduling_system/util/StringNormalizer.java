package edu.itc.enrollment_scheduling_system.util;

public class StringNormalizer {
    public static String trimToNull(String str) {
        return (str == null || str.trim().isEmpty()) ? null : str.trim();
    }
}
