package com.example.rqchallenge.employees.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rqchallenge.employees.data.Employee;
import com.example.rqchallenge.employees.repository.EmployeeRepository;
import com.example.rqchallenge.response.ApiError;
import com.example.rqchallenge.response.RestResponse;

@Service
@Transactional
public class EmployeeService {
	
	private static Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	private static String ERROR_MESSAGE =  "Employee not found with id: ";

	public RestResponse<Employee> getAllEmployees() {
		logger.debug("Service - Getting list of all employees.");
		List<Employee> employeeList = employeeRepository.findAll();
		logger.debug("Completed fetch all. Total number of employees fetched {}.", employeeList.size());
		logger.debug("Building response having {} employee objects...", employeeList.size());
		RestResponse<Employee> response = RestResponse.build(employeeList);
		logger.debug("Completed building response.");
		logger.debug("Response {}", employeeList);
		return response;
	}

	public RestResponse<Employee> getEmployeesByNameSearch(String searchString) {
		logger.debug("Service - getEmployeesByNameSearch. Search: {}", searchString);
		List<Employee> employeeList = employeeRepository.searchByName(searchString.toUpperCase());
		logger.debug("{} matching employees found.", employeeList.size());
		logger.debug("Building response having {} employee objects...", employeeList.size());
		RestResponse<Employee> response = RestResponse.build(employeeList);
		logger.debug("Completed building response.");
		logger.debug("Response {}", employeeList);
		return response;
	}

	public RestResponse<Employee> getEmployeeById(int id) {
		logger.debug("Service - getEmployeeById. Id: {}", id);
		Optional<Employee> employee = employeeRepository.findById(id);
		RestResponse<Employee> response = RestResponse.build(null);
		if(employee.isPresent()) {
			logger.debug("Found employee with id: ", id);
			response = RestResponse.build(employee.get());
		} else {
			logger.debug(ERROR_MESSAGE+id);
			response.setException(new ApiError("NOT_FOUND", ERROR_MESSAGE + id));
		}
		logger.debug("Completed building response.");
		logger.debug("Response {}", response);
		return response;
	}

	public RestResponse<Employee> getHighestSalaryOfEmployees() {
		logger.debug("Service - getHighestSalaryOfEmployees.");
		List<Employee> employeeList = employeeRepository.getHighestSalaryOfEmployees();
		logger.debug("Completed fetching highest earning employees. # of employees: {}", employeeList.size());
		RestResponse<Employee> response = RestResponse.build(employeeList);
		logger.debug("Completed building response.");
		logger.debug("Response {}", employeeList);
		return response;
	}

	public RestResponse<Employee> getTopTenHighestEarningEmployeeNames() {
		logger.debug("Service - getTopTenHighestEarningEmployeeNames.");
		Pageable topTenEarners = PageRequest.of(0, 9, Sort.by("employeeSalary").descending());
		List<Employee> employeeList = employeeRepository.findAll(topTenEarners).getContent();
		logger.debug("Completed getting top ten highest earning employees.");
		Map<String, Employee> empMap = new HashMap<>();
		for(Employee emp : employeeList) {
			empMap.put(emp.getEmployeeName(), emp);
		}
		RestResponse<Employee> response = RestResponse.build(employeeList);
		logger.debug("Completed building response.");
		logger.debug("Response {}", employeeList);
		return response;
	}
	
	public RestResponse<Employee> createEmployees(List<Employee> employeeInput) {
		logger.debug("Service - createEmployees. # of employees to be saved: {}", employeeInput.size());
		logger.debug("Employees to be created [{}]", employeeInput);
		List<Employee> employeesSaved = employeeRepository.saveAll(employeeInput);
		logger.debug("Completed employee save operation. # of employees saved: {}", employeesSaved.size());
		RestResponse<Employee> response = RestResponse.build(employeesSaved);
		logger.debug("Completed building response.");
		logger.debug("Response {}", employeesSaved);
		return response;
	}

	public RestResponse<Employee> createEmployee(Employee employee) {
		logger.debug("Service - createEmployee. Creating employee with following details: {}", employee);
		Employee savedEmployee = employeeRepository.save(employee);
		logger.debug("Completed save operation.");
		RestResponse<Employee> response = RestResponse.build(savedEmployee);
		logger.debug("Completed building response.");
		logger.debug("Response {}", savedEmployee);
		return response;
	}

	public RestResponse<Employee> deleteEmployeeById(int id) {
		logger.debug("Service - deleteEmployeeById. Deleting employee having id: {}", id);
		Optional<Employee> employee = employeeRepository.findById(id);
		RestResponse<Employee> response = RestResponse.build(null);
		if(employee.isPresent()) {
			logger.debug("Deleted employee: {}", employee.get());
			employeeRepository.deleteById(id);
			response = RestResponse.build(employee.get());
		} else {
			logger.error("Delete operation failed. Employee not found.");
			response.setException(new ApiError("NOT_FOUND", ERROR_MESSAGE + id));
		}
		logger.debug("Completed building response.");
		logger.debug("Response[ Data: [{}] \nError: [{}]]", response.getData(), response.getException());
		return response;
	}

}
