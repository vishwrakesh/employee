package com.example.rqchallenge.employees.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.rqchallenge.employees.data.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee, Integer> {

	@Query("from Employee emp where upper(emp.employeeName) like %:searchString%")
	List<Employee> searchByName(@Param("searchString") String searchString);

	@Query("from Employee emp where emp.employeeSalary = (select max(employeeSalary) from Employee)")
	List<Employee> getHighestSalaryOfEmployees();

	Page<Employee> findAll(Pageable employeeSalary);
	
}
