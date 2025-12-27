package edu.itc.enrollment_scheduling_system.repository; 

import edu.itc.enrollment_scheduling_system.model.Classroom; // Import Entity Classroom
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // Import List Java

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    
   
    List<Classroom> findByCourseId(Long courseId);
}