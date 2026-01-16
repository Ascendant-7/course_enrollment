package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.config.AccountDetails;
import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.model.Classroom;
import edu.itc.enrollment_scheduling_system.model.Schedule;
import edu.itc.enrollment_scheduling_system.repository.ClassroomRepository;
import edu.itc.enrollment_scheduling_system.repository.ScheduleRepository;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ClassroomController {

    @Autowired
    private ClassroomRepository classroomRepository;
    
    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/classrooms")
    public String viewClassroomPage(Model model) {
        // ទាញទិន្នន័យពី MySQL តាមរយៈ Repository
        List<Classroom> classrooms = classroomRepository.findAll();
        
        // Load schedules and group by classroom id for the view
        List<Schedule> schedules = scheduleRepository.findAll();
        Map<Long, java.util.List<Schedule>> schedulesMap = schedules.stream()
            .filter(s -> s.getClassroom() != null && s.getClassroom().getId() != null)
            .collect(Collectors.groupingBy(s -> s.getClassroom().getId()));

        // បញ្ជូនទិន្នន័យទៅកាន់ Thymeleaf
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("schedulesMap", schedulesMap);
        
        // open file index.html in folder templates/classrooms
        return "classrooms/index";
    }


    @GetMapping("/classrooms/{id}/edit")
    public String showEditClassroomForm(
        @PathVariable @NonNull Long id,
        Model model,
        @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        Account teacher = accountDetails.getAccount();
        Classroom classroom = classroomRepository.findById(id).orElse(null);

        if (classroom == null) {
            return "redirect:/teacher/dashboard";
        }

        // Check if this teacher owns this classroom
        if (!classroom.getTeacher().getId().equals(teacher.getId())) {
            return "redirect:/teacher/dashboard?error=unauthorized";
        }

        model.addAttribute("teacher", teacher);
        model.addAttribute("classroom", classroom);

        return "teacher/classroom-form";
    }

    @PostMapping("/{id}/edit")
    public String updateClassroom(
        @PathVariable @NonNull Long id,
        @ModelAttribute Classroom classroom,
        @AuthenticationPrincipal AccountDetails accountDetails,
        RedirectAttributes redirectAttributes
    ) {
        Account teacher = accountDetails.getAccount();
        Classroom existingClassroom = classroomRepository.findById(id).orElse(null);

        if (existingClassroom == null) {
            return "redirect:/teacher/dashboard";
        }

        // Check if this teacher owns this classroom
        if (!existingClassroom.getTeacher().getId().equals(teacher.getId())) {
            return "redirect:/teacher/dashboard?error=unauthorized";
        }

        classroomRepository.save(existingClassroom);

        redirectAttributes.addFlashAttribute("success", "Classroom updated successfully!");
        return "redirect:/teacher/dashboard";
    }
}