package com.example.employeeapp.repository;

import com.example.employeeapp.entity.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeRepository extends JpaRepository<Employee, Long>,
    JpaSpecificationExecutor<Employee> {

  boolean existsByEmployeeNo(String employeeNo);

  boolean existsByEmployeeNoAndIdNot(String employeeNo, Long id);

  Optional<Employee> findByEmployeeNo(String employeeNo);
}

