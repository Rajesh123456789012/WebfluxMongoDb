package com.info.controller;


import com.info.entity.Employee;
import com.info.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/saveEmployee")
	public Mono<Employee> saveEmployee(@RequestBody Employee employee) {

		return employeeService.saveEmployee(employee);
	}

	@GetMapping("/findById/{empId}")
	public Mono<Employee> getEmployee(@PathVariable String empId) {
		return employeeService.findById(Long.valueOf(empId));
	}

	@GetMapping("/findAllEmployees")
	public Flux<Employee> findAllEmployee() {
		return employeeService.findAllEmployees();
	}
	
	@GetMapping("/findAllEmployeesBySalary")
	public Flux<Employee> findAllEmployees() {
		return employeeService.fetchEmployessBySalary();
	}
	
	

}
