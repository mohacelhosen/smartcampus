package com.smartcampus.Student.service;

import com.smartcampus.Student.model.StudentEntity;
import com.smartcampus.Student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public StudentEntity registerStudent(StudentEntity student) {

        return null;
    }
}
