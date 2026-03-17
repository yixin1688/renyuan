package com.example.employeeapp.controller;

import com.example.employeeapp.dto.EmployeeRequest;
import com.example.employeeapp.dto.EmployeeResponse;
import com.example.employeeapp.dto.PageResponse;
import com.example.employeeapp.service.EmployeeService;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RequestMapping("/api/employees")
public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping
  public PageResponse<EmployeeResponse> list(
      @RequestParam(value = "keyword", required = false) String keyword,
      @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
      @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size) {
    return employeeService.list(keyword, page, size);
  }

  @GetMapping("/{id}")
  public EmployeeResponse getById(@PathVariable Long id) {
    return employeeService.getById(id);
  }

  @PostMapping
  public Map<String, Object> create(@Valid @RequestBody EmployeeRequest request) {
    EmployeeResponse employee = employeeService.create(request);
    return successBody("新增成功", employee);
  }

  @PutMapping("/{id}")
  public Map<String, Object> update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
    EmployeeResponse employee = employeeService.update(id, request);
    return successBody("更新成功", employee);
  }

  @DeleteMapping("/{id}")
  public Map<String, Object> delete(@PathVariable Long id) {
    employeeService.delete(id);
    return successBody("删除成功", null);
  }

  private Map<String, Object> successBody(String message, Object data) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("success", true);
    body.put("message", message);
    body.put("data", data);
    return body;
  }
}

