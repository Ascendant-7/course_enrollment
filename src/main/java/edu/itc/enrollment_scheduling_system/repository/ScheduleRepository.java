package edu.itc.enrollment_scheduling_system.repository;

import edu.itc.enrollment_scheduling_system.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByClassroomIdOrderByDayOfWeekAscStartTimeAsc(Long classroomId);
}
