package assignment3;

public class Manager extends Employee implements AdjustGrossSalaryInterface {

    private String degree;
    
    public Manager(String ID, String name, double grossSalary, String degree) {
        super(ID, name, grossSalary);
       
        this.degree = degree;
        adjustGrossSalary();
    }

    @Override
    public void adjustGrossSalary() {

        double grossSalary = getInitialGrossSalary();

        if (degree.equals("BSc")){

            grossSalary = getInitialGrossSalary() * 1.1;

        } else if (degree.equals("MSc")){

            grossSalary = getInitialGrossSalary() * 1.2;

        } else if (degree.equals("PhD")){

            grossSalary = getInitialGrossSalary() * 1.35;
            
        }

        setCurrentGrossSalary(Math.floor(grossSalary * 100) / 100);
    }

    public String getDegree(){

        return degree;
    }

    public void setDegree(String newDegree){

        degree = newDegree;

        adjustGrossSalary();
    }

    @Override
    public void setCurrentGrossSalary(){

        adjustGrossSalary();
    }

    @Override
    public String toString() {

        String persistantTwoDecimalPointsGrossSalary = String.format("%.2f", getCurrentGrossSalary());

        return degree + " " + getName() + "'s gross salary is " + persistantTwoDecimalPointsGrossSalary + " SEK per month.";
    }
}
