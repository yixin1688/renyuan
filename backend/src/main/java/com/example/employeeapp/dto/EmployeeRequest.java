package com.example.employeeapp.dto;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EmployeeRequest {

  @NotBlank(message = "工号不能为空")
  @Size(max = 32, message = "工号长度不能超过32位")
  private String employeeNo;

  @NotBlank(message = "姓名不能为空")
  @Size(max = 64, message = "姓名长度不能超过64位")
  private String name;

  @Size(max = 16, message = "性别长度不能超过16位")
  private String gender;

  @Size(max = 32, message = "手机号长度不能超过32位")
  private String phone;

  @Email(message = "邮箱格式不正确")
  @Size(max = 128, message = "邮箱长度不能超过128位")
  private String email;

  @Size(max = 64, message = "部门长度不能超过64位")
  private String department;

  @Size(max = 64, message = "职位长度不能超过64位")
  private String title;

  private LocalDate hireDate;

  @Size(max = 32, message = "状态长度不能超过32位")
  private String status;

  public String getEmployeeNo() {
    return employeeNo;
  }

  public void setEmployeeNo(String employeeNo) {
    this.employeeNo = employeeNo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public LocalDate getHireDate() {
    return hireDate;
  }

  public void setHireDate(LocalDate hireDate) {
    this.hireDate = hireDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}

