package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Classroom;
import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.repository.ClassroomRepository;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import edu.itc.enrollment_scheduling_system.repository.AccountRepository;
import edu.itc.enrollment_scheduling_system.service.EnrollmentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final AccountRepository accountRepository;
    private final CourseRepository courseRepository;
    private final ClassroomRepository classroomRepository;
    private final EnrollmentService enrollmentService;

    public TeacherController(AccountRepository accountRepository,
                            CourseRepository courseRepository,
                            ClassroomRepository classroomRepository,
                            EnrollmentService enrollmentService) {
        this.accountRepository = accountRepository;
        this.courseRepository = courseRepository;
        this.classroomRepository = classroomRepository;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        Account teacher = accountRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (teacher == null) {
            return "redirect:/login";
        }

        List<Course> myCourses = courseRepository.findByTeacherId(teacher.getId());
        List<Classroom> myClassrooms = classroomRepository.findByTeacherId(teacher.getId());
        
        long totalStudents = myCourses.stream()
            .mapToLong(course -> enrollmentService
                .getStudentsInCourse(Objects.requireNonNull(course, "course must not be null"))
                .size())
            .sum();

        model.addAttribute("teacher", teacher);
        model.addAttribute("myCourses", myCourses);
        model.addAttribute("myClassrooms", myClassrooms);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalCourses", myCourses.size());
        model.addAttribute("totalClassrooms", myClassrooms.size());

        return "teacher-dashboard";
    }

    @GetMapping("/courses")
    public String viewMyCourses(Model model, Authentication authentication) {
        Account teacher = accountRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (teacher == null) {
            return "redirect:/login";
        }

        List<Course> myCourses = courseRepository.findByTeacherId(teacher.getId());
        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", myCourses);

        return "teacher-courses";
    }

    @GetMapping("/courses/{id}")
    public String viewCourseDetail(@PathVariable @NonNull Long id, Model model, Authentication authentication) {
        Account teacher = accountRepository.findByUsername(authentication.getName()).orElse(null);
        Course course = courseRepository.findById(id).orElse(null);

        if (teacher == null || course == null) {
            return "redirect:/teacher/courses";
        }

        // Check if this teacher owns this course
        if (!course.getTeacher().getId().equals(teacher.getId())) {
            return "redirect:/teacher/courses?error=unauthorized";
        }

        List<Account> students = enrollmentService.getStudentsInCourse(course);

        model.addAttribute("course", course);
        model.addAttribute("students", students);
        model.addAttribute("teacher", teacher);

        return "teacher-course-detail";
    }

    @GetMapping("/classrooms")
    public String viewMyClassrooms(Model model, Authentication authentication) {
        Account teacher = accountRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (teacher == null) {
            return "redirect:/login";
        }

        List<Classroom> myClassrooms = classroomRepository.findByTeacherId(teacher.getId());
        model.addAttribute("teacher", teacher);
        model.addAttribute("classrooms", myClassrooms);

        return "teacher-classrooms";
    }

    @GetMapping("/classrooms/create")
    public String showCreateClassroomForm(Model model, Authentication authentication) {
        Account teacher = accountRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (teacher == null) {
            return "redirect:/login";
        }

        List<Course> myCourses = courseRepository.findByTeacherId(teacher.getId());
        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", myCourses);
        model.addAttribute("classroom", new Classroom());

        return "teacher-classroom-form";
    }

    @PostMapping("/classrooms/create")
    public String createClassroom(@ModelAttribute Classroom classroom,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        Account teacher = accountRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (teacher == null) {
            return "redirect:/login";
        }

        classroom.setTeacher(teacher);
        classroomRepository.save(classroom);

        redirectAttributes.addFlashAttribute("success", "Classroom created successfully!");
        return "redirect:/teacher/classrooms";
    }

    @GetMapping("/classrooms/{id}/edit")
    public String showEditClassroomForm(@PathVariable @NonNull Long id, Model model, Authentication authentication) {
        Account teacher = accountRepository.findByUsername(authentication.getName()).orElse(null);
        Classroom classroom = classroomRepository.findById(id).orElse(null);

        if (teacher == null || classroom == null) {
            return "redirect:/teacher/classrooms";
        }

        // Check if this teacher owns this classroom
        if (!classroom.getTeacher().getId().equals(teacher.getId())) {
            return "redirect:/teacher/classrooms?error=unauthorized";
        }

        List<Course> myCourses = courseRepository.findByTeacherId(teacher.getId());
        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", myCourses);
        model.addAttribute("classroom", classroom);

        return "teacher-classroom-form";
    }

    @PostMapping("/classrooms/{id}/edit")
    public String updateClassroom(@PathVariable @NonNull Long id,
                                  @ModelAttribute Classroom classroom,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        Account teacher = accountRepository.findByUsername(authentication.getName()).orElse(null);
        Classroom existingClassroom = classroomRepository.findById(id).orElse(null);

        if (teacher == null || existingClassroom == null) {
            return "redirect:/teacher/classrooms";
        }

        // Check if this teacher owns this classroom
        if (!existingClassroom.getTeacher().getId().equals(teacher.getId())) {
            return "redirect:/teacher/classrooms?error=unauthorized";
        }

        existingClassroom.setName(classroom.getName());
        existingClassroom.setBuilding(classroom.getBuilding());
        existingClassroom.setRoomNumber(classroom.getRoomNumber());
        existingClassroom.setCapacity(classroom.getCapacity());
        existingClassroom.setCourse(classroom.getCourse());

        classroomRepository.save(existingClassroom);

        redirectAttributes.addFlashAttribute("success", "Classroom updated successfully!");
        return "redirect:/teacher/classrooms";
    }

    @PostMapping("/classrooms/{id}/delete")
    public String deleteClassroom(@PathVariable @NonNull Long id,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        Account teacher = accountRepository.findByUsername(authentication.getName()).orElse(null);
        Classroom classroom = classroomRepository.findById(id).orElse(null);

        if (teacher == null || classroom == null) {
            return "redirect:/teacher/classrooms";
        }

        // Check if this teacher owns this classroom
        if (!classroom.getTeacher().getId().equals(teacher.getId())) {
            return "redirect:/teacher/classrooms?error=unauthorized";
        }

        classroomRepository.delete(classroom);

        redirectAttributes.addFlashAttribute("success", "Classroom deleted successfully!");
        return "redirect:/teacher/classrooms";
    }
}