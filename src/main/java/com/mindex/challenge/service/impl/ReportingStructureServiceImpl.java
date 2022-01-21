package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String employeeId) {
        LOG.debug("Creating reporting structure for Employee id [{}]", employeeId);

        Employee rootEmployee = employeeRepository.findByEmployeeId(employeeId);
        int numberOfReports = countNumberOfReports(rootEmployee);

        return new ReportingStructure(rootEmployee, numberOfReports);
    }

    public int countNumberOfReports(Employee employee) {
        int totalNumberOfReports = 0;
        List<Employee> reports = employee.getDirectReports();
        if (reports != null) {
            for (Employee reportingEmployee : reports) {
                totalNumberOfReports += 1 + countNumberOfReports(reportingEmployee);
            }
        }
        return totalNumberOfReports;
    }
}
