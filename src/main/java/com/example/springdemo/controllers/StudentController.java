package com.example.springdemo.controllers;

import com.example.springdemo.student.*;
import com.example.springdemo.util.PageableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public ResponseEntity getAllStudent(@RequestParam Optional<String> q, PageableWrapper wrapper) {
        Pageable pageable = wrapper.getPageable();
        Page<Student> pageOfStudents = null;
        if (q.isPresent()) {
            pageOfStudents = studentService.search(q.get(), pageable);
        } else {
            pageOfStudents = studentService.findAll(pageable);
        }
        if (!pageOfStudents.isEmpty()) {
            return new ResponseEntity(pageOfStudents, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/students/{id}")
    public ResponseEntity getStudentById(@PathVariable Integer id) {
        Student studentFound = studentService.findOne(id);

        if (studentFound != null) {
            return new ResponseEntity(studentFound, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/students")
    public ResponseEntity addNewStudent(@RequestBody Student student) {
        studentService.save(student);
        return new ResponseEntity(student, HttpStatus.CREATED);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        Student studentFound = studentService.findOne(id);
        if (studentFound != null) {
            studentFound.setName(student.getName());
            studentFound.setEmail(student.getEmail());
            studentFound.setPhone(student.getPhone());
            studentService.save(studentFound);

            return new ResponseEntity(studentFound, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity deleteStudent(@PathVariable Integer id) {
        Student studentFound = studentService.findOne(id);
        if (studentFound != null) {
            studentService.delete(id);
            return new ResponseEntity(studentFound, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
