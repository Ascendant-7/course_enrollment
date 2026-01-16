package edu.itc.enrollment_scheduling_system.util;

import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;

public class RecordIntrosepector {
    public static List<String> getRecordFieldNames(Class<?> recordClass) {
        return Arrays
            .stream(recordClass.getRecordComponents())
            .map(RecordComponent::getName)
            .toList();
    }
}
