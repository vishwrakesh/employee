package com.example.rqchallenge.employees;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.rqchallenge.employees.data.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.example.rqchallenge.response.ApiError;
import com.example.rqchallenge.response.RestResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(value = EmployeeController.class)
public class EmployeeControllerTest {
	
	@Autowired 
	private MockMvc mvc;
	
	@MockBean
	private EmployeeService service;
	
	private RestResponse<Employee> allEmployees;
	
	@Before
    public void setUp() throws Exception {
		Employee alex = new Employee("Alex", 1000.00, 25, "");
        Employee john = new Employee("john", 2000.00, 27, "");
        Employee bob = new Employee("bob", 3000.00, 28, "");
        Employee alice = new Employee("Alice", 1000.00, 25, "");

        allEmployees = RestResponse.build(Arrays.asList(alex, john, bob, alice));
    }
	
	@Test
    public void testGetEmployees() throws Exception {
        
        given(service.getAllEmployees()).willReturn(allEmployees);

        MvcResult result = mvc.perform(get("/emp/getAllEmployees")).andExpect(status().isOk()).andReturn();
		
        ObjectMapper mapper = new ObjectMapper();
        RestResponse<Employee> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<RestResponse<Employee>>() {});
        
        assertEquals(allEmployees.getData().size(), actual.getData().size());
        
        assertEquals(allEmployees.getData().get(1).getEmployeeAge(), actual.getData().get(1).getEmployeeAge());
        
        verify(service, VerificationModeFactory.times(1)).getAllEmployees();
        
        reset(service);
    }
	
	@Test
    public void testGetEmployeesByNameSearch() throws Exception {
		Employee alex = new Employee("Alex", 1000.00, 25, "");
        RestResponse<Employee> response = RestResponse.build(alex);
        given(service.getEmployeesByNameSearch("Alex")).willReturn(response);

        MvcResult result = mvc.perform(get("/emp/name/Alex")).andExpect(status().isOk()).andReturn();
		
        ObjectMapper mapper = new ObjectMapper();
        RestResponse<Employee> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<RestResponse<Employee>>() {});
        
        assertEquals(1, actual.getData().size());
        assertEquals("Alex", actual.getData().get(0).getEmployeeName());
        verify(service, VerificationModeFactory.times(1)).getEmployeesByNameSearch("Alex");
        reset(service);
        
        //No result test
        response = RestResponse.build(null);
        given(service.getEmployeesByNameSearch("NotPresent")).willReturn(response);
        result = mvc.perform(get("/emp/name/NotPresent")).andExpect(status().isOk()).andReturn();
        
        assertNotNull(result.getResponse().getContentAsString());
        assertEquals(0, result.getResponse().getContentLength());
        verify(service, VerificationModeFactory.times(1)).getEmployeesByNameSearch("NotPresent");
        verify(service, VerificationModeFactory.times(0)).getEmployeesByNameSearch("Alex");
        reset(service);
        
        //Multiple result
        Employee alice = new Employee("Alice", 1000.00, 25, "");
        allEmployees = RestResponse.build(Arrays.asList(alex, alice));
        given(service.getEmployeesByNameSearch("al")).willReturn(allEmployees);
        result = mvc.perform(get("/emp/name/al")).andExpect(status().isOk()).andReturn();
        actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<RestResponse<Employee>>() {});
        
        assertEquals(2, actual.getData().size());
        assertEquals("Alice", actual.getData().get(1).getEmployeeName());
        assertNotNull(result.getResponse().getContentAsString());
        assertEquals(0, result.getResponse().getContentLength());
        verify(service, VerificationModeFactory.times(0)).getEmployeesByNameSearch("NotPresent");
        verify(service, VerificationModeFactory.times(0)).getEmployeesByNameSearch("Alex");
        verify(service, VerificationModeFactory.times(1)).getEmployeesByNameSearch("al");
        reset(service);
        
    }
	
	@Test
    public void testGetEmployeeById() throws Exception {
        
		Employee alex = new Employee("Alex", 1000.00, 25, "");
		alex.setId(9999);
        RestResponse<Employee> response = RestResponse.build(alex);
        given(service.getEmployeeById(9999)).willReturn(response);

        MvcResult result = mvc.perform(get("/emp/9999")).andExpect(status().isOk()).andReturn();
		
        ObjectMapper mapper = new ObjectMapper();
        RestResponse<Employee> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<RestResponse<Employee>>() {});
        
        assertEquals(9999, actual.getData().get(0).getId());
        assertEquals(1, actual.getData().size());
        
        verify(service, VerificationModeFactory.times(1)).getEmployeeById(9999);
        reset(service);
        
        //Employe not found
        RestResponse<Employee> resp = RestResponse.build(null);
        resp.setException(new ApiError("NOT_FOUND", "Employee not found with id: "+10));
        given(service.getEmployeeById(10)).willReturn(resp);

        result = mvc.perform(get("/emp/10")).andExpect(status().isOk()).andReturn();
        actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<RestResponse<Employee>>() {});
        
        assertNull(actual.getData());
        assertNotNull(actual.getException());
        assertEquals("NOT_FOUND", actual.getException().getCode());
        
        verify(service, VerificationModeFactory.times(1)).getEmployeeById(10);
        reset(service);
        
    }

}
