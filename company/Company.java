package assignment3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class Company {

    private ArrayList<Employee> employees;
    private HashMap<String, Integer> nrOfRegisteredForEachDegree;
    Employee employeeBeforePromotion;
    Employee employeeAfterPromotion;

    public Company() {

        employees = new ArrayList<>();
        nrOfRegisteredForEachDegree = new HashMap<>();
    }

    private String runEmployeeIDNameGrossSalaryValidityCheck (String ID, String name, double grossSalary) {

        if (ID.isEmpty() || ID.isEmpty()) {

            return "ID cannot be blank.";

        } else if (name.isEmpty() || name.isBlank()) {

            return "Name cannot be blank.";

        } else if (grossSalary <= 0) {

            return "Salary must be greater than zero.";
        }

        for (Employee employee : employees){
            
            if (employee.getID().equals(ID)){

                return "Cannot register. ID " + ID + " is already registered.";
            }  
        }

        return null;
    }

    private void prepareEmployeeIDNameGrossSalaryValidityCheck(String ID, String name, double grossSalary) throws InvalidDataAttributionException{

        String result = runEmployeeIDNameGrossSalaryValidityCheck(ID, name, grossSalary);

        if (result != null){

            throw new InvalidDataAttributionException(result);
        }
    }

    private void employeesExistCheck() throws NoEmployeesFoundException{

        if (employees.size() == 0){

            throw new NoEmployeesFoundException("No employees registered yet.");
        } 
    }

    private Employee employeeIDSearch(String ID) throws SpecificEmployeeNotFoundException{

        for (Employee employee : employees){

            if(employee.getID().equals(ID)){

                return employee;
            }
        }

        throw new SpecificEmployeeNotFoundException("Employee " + ID + " was not registered yet.");
    }

    private void resolveTargetEmployeeForPromotion (String ID) throws SpecificEmployeeNotFoundException{

        employeeBeforePromotion = employeeIDSearch(ID);

        employeeAfterPromotion = employeeBeforePromotion;
    }

    public double getNetSalary(String ID) throws SpecificEmployeeNotFoundException{
        
        return employeeIDSearch(ID).getNetSalary();
    } 

    public double getTotalNetSalary () throws NoEmployeesFoundException{

        employeesExistCheck();

        double companyWideAggregatedNetSalaries = 0.0;

        for (Employee employee : employees){

            companyWideAggregatedNetSalaries += employee.getNetSalary();

        }
            
        return companyWideAggregatedNetSalaries;
    }

    public String createEmployee(String ID, String name, double grossSalary) throws InvalidDataAttributionException{

        prepareEmployeeIDNameGrossSalaryValidityCheck(ID, name, grossSalary);
        
        Employee newEmployee = new Employee(ID, name, grossSalary);

        employees.add(newEmployee);

        return "Employee " + newEmployee.getID() + " was registered successfully.";
    }

    public String createEmployee(String ID, String name, double grossSalary, String degree) throws InvalidDataAttributionException{

        prepareEmployeeIDNameGrossSalaryValidityCheck(ID, name, grossSalary);

        if (degree != "BSc" && degree != "MSc" && degree != "PhD") {

            throw new InvalidDataAttributionException("Degree must be one of the options: BSc, MSc or PhD.");

        } else {
            
            Employee newManager = new Manager(ID, name, grossSalary, degree);

            employees.add(newManager);

            return "Employee " + newManager.getID() + " was registered successfully.";
        }
    }

    public String createEmployee(String ID, String name, double grossSalary, String degree, String department) throws InvalidDataAttributionException{
        
        prepareEmployeeIDNameGrossSalaryValidityCheck(ID, name, grossSalary);

        if (department != "Business" && department != "Human Resources" && department != "Technical") {
            
            throw new InvalidDataAttributionException("Department must be one of the options: Business, Human Resources or Technical.");

        } else {
            
            Employee newDirector = new Director(ID, name, grossSalary, degree, department);
            
            employees.add(newDirector);

            return "Employee " + newDirector.getID() + " was registered successfully.";
        }
    }
    
    public String createEmployee(String ID, String name, double grossSalary, int gpa) throws InvalidDataAttributionException{
    
        prepareEmployeeIDNameGrossSalaryValidityCheck(ID, name, grossSalary);

        if (gpa < 0 || gpa > 10) {

            throw new InvalidDataAttributionException(gpa + " outside range. Must be between 0-10.");

        } else {

            Employee newIntern = new Intern(ID, name, grossSalary, gpa);

            employees.add(newIntern);

            return "Employee " + newIntern.getID() + " was registered successfully.";
        }
    }

    public String removeEmployee(String ID) throws SpecificEmployeeNotFoundException {

        employees.remove(employees.indexOf(employeeIDSearch(ID)));

        return "Employee " + ID + " was successfully removed.";
    }

    public String printEmployee(String ID) throws SpecificEmployeeNotFoundException{

        return employeeIDSearch(ID).toString();
    } 

    public String printAllEmployees() throws NoEmployeesFoundException{

        employeesExistCheck();

        String allEmployeesString = "All registered employees:" + System.lineSeparator();

        for (Employee employee : employees){

            allEmployeesString = allEmployeesString + employee.toString() + System.lineSeparator();

        }

        return allEmployeesString;
    }

    public String printSortedEmployees() throws NoEmployeesFoundException{

        employeesExistCheck();

        Collections.sort(employees);

        String output = "Employees sorted by gross salary (ascending order):" + System.lineSeparator();

        for (Employee employee : employees){

            output += employee.toString() + System.lineSeparator();

        }

        return output;
    }

    public HashMap<String, Integer> mapEachDegree() throws NoEmployeesFoundException{

        nrOfRegisteredForEachDegree.clear();

        employeesExistCheck();

        for (Employee employee : employees){
            
            if (Manager.class.isAssignableFrom(employee.getClass())){

                if (((Manager) employee).getDegree().equals("BSc")){

                    nrOfRegisteredForEachDegree.merge("BSc", 1, (val1, val2) -> val1 + val2);

                } else if (((Manager) employee).getDegree().equals("MSc")) {

                    nrOfRegisteredForEachDegree.merge("MSc", 1, (val1, val2) -> val1 + val2);

                } else if (((Manager) employee).getDegree().equals("PhD")) {

                    nrOfRegisteredForEachDegree.merge("PhD", 1, (val1, val2) -> val1 + val2);
                }
            }
        }
            
        return nrOfRegisteredForEachDegree;
    }

    public String promoteToManager(String ID, String degree) throws SpecificEmployeeNotFoundException{

        resolveTargetEmployeeForPromotion(ID);

        employeeAfterPromotion = new Manager(ID, employeeAfterPromotion.getName(), employeeAfterPromotion.getInitialGrossSalary(), degree);

        employees.set(employees.indexOf(employeeBeforePromotion), employeeAfterPromotion);

        return ID + " promoted successfully to Manager.";
    }

    public String promoteToDirector(String ID, String degree, String directorDepartment) throws SpecificEmployeeNotFoundException{

        resolveTargetEmployeeForPromotion(ID);

        employeeAfterPromotion = new Director(ID, employeeAfterPromotion.getName(), employeeAfterPromotion.getInitialGrossSalary(), degree, directorDepartment);

        employees.set(employees.indexOf(employeeBeforePromotion), employeeAfterPromotion);

        return ID + " promoted successfully to Director.";
    }

    public String promoteToIntern(String ID, int gpa) throws SpecificEmployeeNotFoundException{
        
        resolveTargetEmployeeForPromotion(ID);

        employeeAfterPromotion = new Intern(ID, employeeAfterPromotion.getName(), employeeAfterPromotion.getInitialGrossSalary(), gpa);

        employees.set(employees.indexOf(employeeBeforePromotion), employeeAfterPromotion);

        return ID + " promoted successfully to Intern.";
    }

    public String updateGrossSalary (String ID, double newGrossSalary) throws InvalidDataAttributionException, SpecificEmployeeNotFoundException{

        if (newGrossSalary <= 0) {

            throw new InvalidDataAttributionException("Salary must be greater than zero.");

        } else {

            employeeIDSearch(ID).setInitialGrossSalary(newGrossSalary);

            return "Employee " + ID + " was updated successfully";
        }
    }

    public String updateEmployeeName (String ID, String newName) throws InvalidDataAttributionException, SpecificEmployeeNotFoundException {
        
        if (newName.isBlank() || newName.isEmpty()) {

            throw new InvalidDataAttributionException("Name cannot be blank.");

        } else {

            employeeIDSearch(ID).setName(newName);
                
            return "Employee " + ID + " was updated successfully";
        }
    }

    public String updateManagerDegree (String ID, String newDegree) throws InvalidDataAttributionException, SpecificEmployeeNotFoundException{

        if (newDegree != "BSc" && newDegree != "MSc" && newDegree != "PhD") {

            throw new InvalidDataAttributionException("Degree must be one of the options: BSc, MSc or PhD.");

        } else {
            
            ((Manager) employeeIDSearch(ID)).setDegree(newDegree);

            return "Employee " + ID + " was updated successfully";
        }
    }

    public String updateDirectorDept (String ID, String newDepartment) throws InvalidDataAttributionException, SpecificEmployeeNotFoundException{
        
        if (newDepartment != "Business" && newDepartment != "Human Resources" && newDepartment != "Technical") {
            
            throw new InvalidDataAttributionException("Department must be one of the options: Business, Human Resources or Technical.");

        } else {

            ((Director) employeeIDSearch(ID)).setDepartment(newDepartment);
                
            return "Employee " + ID + " was updated successfully";
        }
    }

    public String updateInternGPA (String ID, int newGpa) throws InvalidDataAttributionException, SpecificEmployeeNotFoundException{

        if (newGpa < 0 || newGpa > 10) {

            throw new InvalidDataAttributionException(newGpa + " outside range. Must be between 0-10.");

        } else {

            ((Intern) employeeIDSearch(ID)).setGpa(newGpa);

            return "Employee " + ID + " was updated successfully";
        }
    }
}


