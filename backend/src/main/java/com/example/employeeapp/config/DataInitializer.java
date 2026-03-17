package com.example.employeeapp.config;

import com.example.employeeapp.entity.Employee;
import com.example.employeeapp.repository.EmployeeRepository;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

  @Bean
  public CommandLineRunner seedEmployees(EmployeeRepository employeeRepository) {
    return args -> {
      if (employeeRepository.count() > 0) {
        return;
      }

      employeeRepository.save(buildEmployee(
          "E1001", "张三", "男", "13800000001", "zhangsan@example.com",
          "人事部", "HR专员", LocalDate.of(2022, 3, 1), "ACTIVE"));
      employeeRepository.save(buildEmployee(
          "E1002", "李四", "女", "13800000002", "lisi@example.com",
          "技术部", "Java工程师", LocalDate.of(2021, 7, 15), "ACTIVE"));
      employeeRepository.save(buildEmployee(
          "E1003", "王五", "男", "13800000003", "wangwu@example.com",
          "财务部", "财务专员", LocalDate.of(2020, 11, 20), "INACTIVE"));
    };
  }

  private Employee buildEmployee(
      String employeeNo,
      String name,
      String gender,
      String phone,
      String email,
      String department,
      String title,
      LocalDate hireDate,
      String status) {
    Employee employee = new Employee();
    employee.setEmployeeNo(employeeNo);
    employee.setName(name);
    employee.setGender(gender);
    employee.setPhone(phone);
    employee.setEmail(email);
    employee.setDepartment(department);
    employee.setTitle(title);
    employee.setHireDate(hireDate);
    employee.setStatus(status);
    return employee;
  }
}

