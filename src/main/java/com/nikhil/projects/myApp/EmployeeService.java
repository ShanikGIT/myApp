package com.nikhil.projects.myApp;
import com.hazelcast.map.IMap;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private HazelcastInstance hazelcastInstance;


    private final EmployeeRepository repository;

    private IMap<Long, Employee> getEmployeeCache() {
        return hazelcastInstance.getMap("employees");
    }

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee save(Employee employee) {

        // Save to Database
        Employee savedEmployee = repository.save(employee);

        // Save to Hazelcast Cache
        getEmployeeCache().put(savedEmployee.getId(), savedEmployee);

        System.out.println("Employee added to Hazelcast Cache");

        return savedEmployee;
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Employee getById(Long id) {

//        -------------Level 1 cache---------------
//        // Step 1: Check Hazelcast Cache
//        Employee cachedEmployee = getEmployeeCache().get(id);
//
//        if (cachedEmployee != null) {
//            System.out.println("Employee found in Hazelcast Cache");
//            return cachedEmployee;
//        }
//
//        System.out.println("Employee NOT found in Cache. Loading from Database...");
//
//        // Step 2: Load from Database
//        Employee employee = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//
//        // Step 3: Store in Cache
//        getEmployeeCache().put(id, employee);
//
//        System.out.println("Employee added to Cache");
//
//        return employee;
        System.out.println("fetching data");
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    //L1 cache
//    public Employee update(Long id, Employee employee) {
//
//        Employee existing = getById(id);
//
//        existing.setName(employee.getName());
//        existing.setDepartment(employee.getDepartment());
//        existing.setSalary(employee.getSalary());
//
//        // Update database
//        Employee updatedEmployee = repository.save(existing);
//
//        // Update cache
//        getEmployeeCache().put(updatedEmployee.getId(), updatedEmployee);
//
//        System.out.println("Employee updated in Hazelcast Cache");
//
//        return updatedEmployee;
//    }

    //l2 cache
    public Employee update(Long id, Employee employee) {

        System.out.println("Updating employee " + id);

        Employee existing = getById(id);

        existing.setName(employee.getName());
        existing.setDepartment(employee.getDepartment());
        existing.setSalary(employee.getSalary());

        return repository.save(existing);
    }
    public void delete(Long id) {

        repository.deleteById(id);

        getEmployeeCache().remove(id);

        System.out.println("Employee removed from Hazelcast Cache");
    }}