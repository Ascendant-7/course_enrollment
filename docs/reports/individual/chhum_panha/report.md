# Report: Adding Validation for Course Entity



## 1. Objective

The goal of adding validation is to:

1. Ensure that all required fields are filled.
2. Prevent invalid data from being stored in the database.
3. Provide meaningful feedback to users when input errors occur.
4. Improve application stability and reduce runtime exceptions.

## 2. Implementation

### 2.1 Using JSR-380 / Jakarta Bean Validation

Spring Boot supports **Bean Validation** (JSR-380) annotations. Common annotations used in the `Course` entity include:

| Annotation        | Description                                            |
| ----------------- | ------------------------------------------------------ |
| `@NotNull`        | Ensures a field is not null.                           |
| `@NotBlank`       | Ensures a String is not null or empty.                 |
| `@Size(min, max)` | Ensures a String or collection has a valid size range. |
| `@Positive`       | Ensures numeric values are positive.                   |

**Example: Course Entity Validation**

```java
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Course {

    @NotBlank(message = "Course code is required")
    private String code;

    @NotBlank(message = "Course name is required")
    @Size(max = 100, message = "Course name must be less than 100 characters")
    private String name;

    private int availableSeats;
}
```

### 2.2 Controller-side Validation

In the `AdminController`, validation is triggered using:

```java
@PostMapping("/courses/save")
public String saveCourse(
        @Valid @ModelAttribute("course") Course course,
        BindingResult result) {

    // Null-safety check
    Objects.requireNonNull(course, "Course must not be null");

    // Return to form if there are validation errors
    if (result.hasErrors()) {
        return "admin/course-form";
    }

    // Save validated course
    courseRepository.save(course);

    return "redirect:/admin/courses";
}
```

**Explanation:**

* `@Valid` triggers validation on the `Course` object.
* `BindingResult` stores validation results and error messages.
* If errors exist, the user is redirected back to the form with messages.
* `Objects.requireNonNull()` ensures null-safety and satisfies the compiler/IDE.

### 2.3 Benefits of Validation

1. **Data Integrity**: Only valid courses are saved to the database.
2. **User Feedback**: Administrators receive clear messages about missing or invalid fields.
3. **Error Prevention**: Avoids runtime exceptions like null pointer errors.
4. **Maintainability**: Centralized validation in the entity simplifies code and reduces duplicate checks in multiple controllers.

## 3. Conclusion

Adding validation in the **Course Management** module improves the reliability and usability of the **Enrollment Scheduling System**. Using **Bean Validation annotations** along with proper controller handling ensures that administrators submit correct and safe data while providing a better user experience.
