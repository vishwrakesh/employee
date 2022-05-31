package com.example.rqchallenge.employees;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rqchallenge.employees.data.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.example.rqchallenge.response.RestResponse;

@RestController
@RequestMapping("/emp")
public class EmployeeController {
	
	private static Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired EmployeeService employeeService;


	@GetMapping("/getAllEmployees")
	public RestResponse<?> getAllEmployees() throws IOException {
		logger.debug("{} - method getAllEmployees", this.getClass().getSimpleName());
		return employeeService.getAllEmployees();
	}

	@GetMapping("/name/{searchString}")
	public RestResponse<?> getEmployeesByNameSearch(@PathVariable("searchString") String searchString) {
		logger.debug("{} - method getEmployeesByNameSearch", this.getClass().getSimpleName());
		return employeeService.getEmployeesByNameSearch(searchString);
	}

	@GetMapping("/{id}")
	public RestResponse<?> getEmployeeById(@PathVariable("id") String id) {
		logger.debug("{} - method getEmployeeById", this.getClass().getSimpleName());
		return employeeService.getEmployeeById(Integer.parseInt(id));
	}

	@GetMapping("/getHigestSalaryOfEmployees")
	public RestResponse<?> getHighestSalaryOfEmployees() {
		logger.debug("{} - method getHighestSalaryOfEmployees", this.getClass().getSimpleName());
		return employeeService.getHighestSalaryOfEmployees();
	}

	@GetMapping("/getTopTenHighestEarningEmployeeNames")
	public RestResponse<?> getTopTenHighestEarningEmployeeNames() {
		logger.debug("{} - method getTopTenHighestEarningEmployeeNames", this.getClass().getSimpleName());
		return employeeService.getTopTenHighestEarningEmployeeNames();
	}

	@PostMapping("/createEmployees")
	public RestResponse<?> createEmployees(@RequestBody List<Employee> employeeInput) {
		logger.debug("{} - method createEmployees", this.getClass().getSimpleName());
		return employeeService.createEmployees(employeeInput);
	}
	
	@PostMapping("/createEmployee")
	public RestResponse<?> createEmployee(@RequestBody Employee employee) {
		logger.debug("{} - method createEmployee", this.getClass().getSimpleName());
		return employeeService.createEmployee(employee);
	}

	@DeleteMapping("/delete/{id}")
	public RestResponse<?> deleteEmployeeById(@PathVariable("id") String id) {
		logger.debug("{} - method deleteEmployeeById", this.getClass().getSimpleName());
		return employeeService.deleteEmployeeById(Integer.parseInt(id));
	}
}
