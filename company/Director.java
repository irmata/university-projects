package assignment3;

public class Director extends Manager {

    private String department;
    
    Director(String ID, String name, double grossSalary, String degree, String department) {
        super(ID, name, grossSalary, degree);

        this.department = department;
        adjustGrossSalary();
        setCurrentGrossSalary(getCurrentGrossSalary() + 5000); 
    }

    @Override
    public double getNetSalary(){

        double netSalary = 0.0;

        if (getCurrentGrossSalary() < 30000) {

            netSalary = getCurrentGrossSalary() - (0.1 * getCurrentGrossSalary());

        } else if (getCurrentGrossSalary() >= 30000 && getCurrentGrossSalary() <= 50000){

            netSalary = getCurrentGrossSalary() - (0.2 * getCurrentGrossSalary());

        } else if (getCurrentGrossSalary() > 50000 && getCurrentGrossSalary() <= 60000){

            netSalary = getCurrentGrossSalary() - (0.2 * 30000);

        } else if (getCurrentGrossSalary() > 60000){

            netSalary = getCurrentGrossSalary() - (0.2 * 30000) - (0.4 * (getCurrentGrossSalary() - 30000));

        }

        return netSalary;
    }

    public void setDepartment(String newDepartment){

        department = newDepartment;
    }

    @Override
    public void setDegree(String newDegree){

        super.setDegree(newDegree);

        setCurrentGrossSalary(getCurrentGrossSalary() + 5000);
    }

    @Override
    public String toString() {

        String persistantTwoDecimalPointsGrossSalary = String.format("%.2f", getCurrentGrossSalary());

        return getDegree() + " " + getName() + "'s gross salary is " + persistantTwoDecimalPointsGrossSalary + " SEK per month. Dept: " + department;
    }
}
