package assignment3;

public class Employee implements Comparable<Employee> {

    private final String ID;
    private String name;
    private double initialGrossSalary;
    private double currentGrossSalary;

    public Employee(String ID, String name, double grossSalary) {

        this.ID = ID;
        this.name = name;
        this.initialGrossSalary = Math.floor((grossSalary * 100)) / 100;
        this.currentGrossSalary = initialGrossSalary;
    }

    public String getID(){

        return ID;
    }

    public String getName(){

        return name;
    }

    public double getCurrentGrossSalary(){

        return currentGrossSalary;
    }

    public double getInitialGrossSalary(){

        return initialGrossSalary;
    }    

    public double getNetSalary(){

        double truncatedNetSalary = Math.floor((currentGrossSalary - (0.1 * currentGrossSalary)) * 100) / 100;

        return truncatedNetSalary;
    }

    public void setName(String newName){

        name = newName;
    }

    public void setInitialGrossSalary(double newInitialGrossSalary){

        initialGrossSalary = Math.floor(100* newInitialGrossSalary) / 100;

        setCurrentGrossSalary();
    }

    public void setCurrentGrossSalary(){

        currentGrossSalary = initialGrossSalary;
    }

    public void setCurrentGrossSalary(double newGrossSalary){

        currentGrossSalary = newGrossSalary;
    }
    
    @Override
    public String toString() {

        String persistantTwoDecimalPointsCurrentGrossSalary = String.format("%.2f", currentGrossSalary);

        return getName() + "'s gross salary is " + persistantTwoDecimalPointsCurrentGrossSalary + " SEK per month.";
    }
    
    @Override
    public int compareTo(Employee employee) {

        if (currentGrossSalary == employee.currentGrossSalary) {

            return 0;

        } else if (currentGrossSalary > employee.currentGrossSalary) {

            return 1;

        } else {

            return -1;
        }
    }
}
