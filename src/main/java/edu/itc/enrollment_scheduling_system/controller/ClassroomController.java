package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Classroom;
import edu.itc.enrollment_scheduling_system.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class ClassroomController {

    @Autowired
    private ClassroomRepository classroomRepository;

    @GetMapping("/classrooms")
    public String viewClassroomPage(Model model) {
        // ទាញទិន្នន័យពី MySQL តាមរយៈ Repository
        List<Classroom> classrooms = classroomRepository.findAll();
        
        // បញ្ជូនទិន្នន័យទៅកាន់ Thymeleaf
        model.addAttribute("classrooms", classrooms);
        
        // open file index.html in folder templates/classrooms
        return "classrooms/index";
    }
}