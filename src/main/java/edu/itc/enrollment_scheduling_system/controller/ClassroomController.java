package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Classroom;
import edu.itc.enrollment_scheduling_system.model.Schedule;
import edu.itc.enrollment_scheduling_system.repository.ClassroomRepository;
import edu.itc.enrollment_scheduling_system.repository.ScheduleRepository;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}