package com.smartcampus.classroom.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcampus.classroom.model.Attendance;
import com.smartcampus.classroom.model.SingleClass;
import com.smartcampus.classroom.model.StudentAttendance;
import com.smartcampus.classroom.repoository.AttendanceRepo;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.exception.NotFoundException;

@Service
public class AttendanceService {
	@Autowired
	private AttendanceRepo attendanceRepo;

	@Autowired
	private SingleClassService singleClassService;

	public Attendance saveToDb(Attendance attendance) {
		if (attendance.getClassId() == null || attendance.getClassId().isEmpty()) {
			throw new IllegalArgumentException("Class id can't be null or empty");
		}

		try {
			SingleClass dbClass = singleClassService.findSingleClass(attendance.getClassId());
			attendance.setAttendanceCreationTime(new ModelLocalDateTime(null));
			if (dbClass != null) {
				Attendance save = attendanceRepo.save(attendance);
				String id = save.getId();
				List<String> attendancesLIst = dbClass.getAttendancesLIst();
				attendancesLIst.add(id);
				dbClass.setAttendancesLIst(attendancesLIst);
				return save;
			} else {
				throw new NotFoundException("Class not found for id: " + attendance.getClassId());
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to save attendance", e);
		}
	}

	public Attendance updateAttendance(Attendance attendance) {
		if (attendance.getClassId() == null || attendance.getClassId().isEmpty()) {
			throw new IllegalArgumentException("Class id can't be null or empty");
		}

		try {
			SingleClass dbClass = singleClassService.findSingleClass(attendance.getClassId());
			attendance.setAttendanceCreationTime(new ModelLocalDateTime(null));
			if (dbClass != null) {

				Optional<Attendance> optionalAttendace = attendanceRepo.findById(attendance.getId());
				Attendance dbAttendance = optionalAttendace.get();

				dbAttendance.setClassTitle(attendance.getClassTitle());

				attendanceRepo.save(dbAttendance);
				return dbAttendance;
			} else {
				throw new NotFoundException("Class not found for id: " + attendance.getClassId());
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to save attendance", e);
		}
	}

	public Attendance updateAttendance(String attendanceRecordId, List<StudentAttendance> newPresentStatusList) {
		Optional<Attendance> optionalAttendance = attendanceRepo.findById(attendanceRecordId);
		return optionalAttendance.map(dbAttendance -> {
			List<StudentAttendance> presentStudentIds = dbAttendance.getPresentStudentIds();
			presentStudentIds.addAll(newPresentStatusList);
			dbAttendance.setPresentStudentIds(presentStudentIds);
			attendanceRepo.save(dbAttendance);
			return dbAttendance;
		}).orElseThrow(() -> new NotFoundException("Attendance record not found for ID: " + attendanceRecordId));
	}

	public Integer getTotalPresentDaysForStudent(String classId, String studentId) {
		List<Attendance> totalPresent = attendanceRepo.findByPresentStudentIds(classId, studentId);
		Integer present = 0;
		for (Attendance singlePresent : totalPresent) {
			List<StudentAttendance> presentStudentIds = singlePresent.getPresentStudentIds();
			for (StudentAttendance eachDayPresnt : presentStudentIds) {
				if (eachDayPresnt.getStudentId().equalsIgnoreCase(studentId) && eachDayPresnt.isPresent() == true) {
					present = present + 1;
					break;
				}
			}
		}

		return present;
	}

}
