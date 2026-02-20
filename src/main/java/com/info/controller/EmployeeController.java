package com.info.controller;


import com.info.entity.Employee;
import com.info.exception.ApplicationError;
import com.info.exception.ApplicationException;
import com.info.exception.ErrorCode;
import com.info.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employee")
@Profile("!local")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/saveEmployee")
	public Mono<Employee> saveEmployee(@RequestBody Employee employee) {
		if (employee.getId() <= 0){
			ApplicationError error = new ApplicationError();
			error.setCode(ErrorCode.ERR_400_00.getCode());
			error.setMessage(ErrorCode.ERR_400_00.getMessage());
					error.setDetail(ErrorCode.ERR_400_00.getMessage());


			throw new ApplicationException(HttpStatus.BAD_REQUEST, "validation failed",error);
		}

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
