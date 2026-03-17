package com.example.employeeapp.service;

import com.example.employeeapp.dto.EmployeeRequest;
import com.example.employeeapp.dto.EmployeeResponse;
import com.example.employeeapp.dto.PageResponse;
import com.example.employeeapp.entity.Employee;
import com.example.employeeapp.exception.BusinessException;
import com.example.employeeapp.exception.ResourceNotFoundException;
import com.example.employeeapp.repository.EmployeeRepository;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

  private final EmployeeRepository employeeRepository;

  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public PageResponse<EmployeeResponse> list(String keyword, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
    Specification<Employee> specification = (root, query, criteriaBuilder) -> {
      if (keyword == null || keyword.trim().isEmpty()) {
        return criteriaBuilder.conjunction();
      }

      String value = "%" + keyword.trim() + "%";
      List<Predicate> predicates = new ArrayList<Predicate>();
      predicates.add(criteriaBuilder.like(root.get("name"), value));
      predicates.add(criteriaBuilder.like(root.get("employeeNo"), value));
      return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    };

    Page<Employee> employeePage = employeeRepository.findAll(specification, pageable);
    PageResponse<EmployeeResponse> response = new PageResponse<EmployeeResponse>();
    response.setContent(mapToResponseList(employeePage.getContent()));
    response.setTotalElements(employeePage.getTotalElements());
    response.setTotalPages(employeePage.getTotalPages());
    response.setPage(employeePage.getNumber());
    response.setSize(employeePage.getSize());
    return response;
  }

  public EmployeeResponse getById(Long id) {
    return toResponse(findById(id));
  }

  @Transactional
  public EmployeeResponse create(EmployeeRequest request) {
    String employeeNo = normalize(request.getEmployeeNo());
    if (employeeRepository.existsByEmployeeNo(employeeNo)) {
      throw new BusinessException("工号已存在");
    }

    Employee employee = new Employee();
    copyToEntity(request, employee);
    employee.setEmployeeNo(employeeNo);
    return toResponse(employeeRepository.save(employee));
  }

  @Transactional
  public EmployeeResponse update(Long id, EmployeeRequest request) {
    Employee employee = findById(id);
    String employeeNo = normalize(request.getEmployeeNo());
    if (employeeRepository.existsByEmployeeNoAndIdNot(employeeNo, id)) {
      throw new BusinessException("工号已存在");
    }

    copyToEntity(request, employee);
    employee.setEmployeeNo(employeeNo);
    return toResponse(employeeRepository.save(employee));
  }

  @Transactional
  public void delete(Long id) {
    Employee employee = findById(id);
    employeeRepository.delete(employee);
  }

  private Employee findById(Long id) {
    return employeeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
  }

  private void copyToEntity(EmployeeRequest request, Employee employee) {
    employee.setName(normalize(request.getName()));
    employee.setGender(normalizeNullable(request.getGender()));
    employee.setPhone(normalizeNullable(request.getPhone()));
    employee.setEmail(normalizeNullable(request.getEmail()));
    employee.setDepartment(normalizeNullable(request.getDepartment()));
    employee.setTitle(normalizeNullable(request.getTitle()));
    employee.setHireDate(request.getHireDate());
    employee.setStatus(defaultStatus(request.getStatus()));
  }

  private List<EmployeeResponse> mapToResponseList(List<Employee> employees) {
    List<EmployeeResponse> responses = new ArrayList<EmployeeResponse>();
    for (Employee employee : employees) {
      responses.add(toResponse(employee));
    }
    return responses;
  }

  private EmployeeResponse toResponse(Employee employee) {
    EmployeeResponse response = new EmployeeResponse();
    response.setId(employee.getId());
    response.setEmployeeNo(employee.getEmployeeNo());
    response.setName(employee.getName());
    response.setGender(employee.getGender());
    response.setPhone(employee.getPhone());
    response.setEmail(employee.getEmail());
    response.setDepartment(employee.getDepartment());
    response.setTitle(employee.getTitle());
    response.setHireDate(employee.getHireDate());
    response.setStatus(employee.getStatus());
    response.setCreatedAt(employee.getCreatedAt());
    response.setUpdatedAt(employee.getUpdatedAt());
    return response;
  }

  private String normalize(String value) {
    return value == null ? null : value.trim();
  }

  private String normalizeNullable(String value) {
    String normalized = normalize(value);
    return normalized == null || normalized.isEmpty() ? null : normalized;
  }

  private String defaultStatus(String value) {
    String normalized = normalizeNullable(value);
    return normalized == null ? "ACTIVE" : normalized;
  }
}

