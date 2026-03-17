INSERT IGNORE INTO employees (employee_no, name, gender, phone, email, department, title, hire_date, status)
VALUES
  ('E1001', '张三', '男', '13800000001', 'zhangsan@example.com', '人事部', 'HR专员', '2022-03-01', 'ACTIVE'),
  ('E1002', '李四', '女', '13800000002', 'lisi@example.com', '技术部', 'Java工程师', '2021-07-15', 'ACTIVE'),
  ('E1003', '王五', '男', '13800000003', 'wangwu@example.com', '财务部', '财务专员', '2020-11-20', 'INACTIVE');

