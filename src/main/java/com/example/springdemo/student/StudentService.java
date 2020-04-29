package com.example.springdemo.student;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
    public Page<Student> findAll(Pageable pageable);
    public Page<Student> search(String searchTerm, Pageable pageable);
    public Student findOne(Integer id);
    public void save(Student student);
    public void delete(Integer id);
}
