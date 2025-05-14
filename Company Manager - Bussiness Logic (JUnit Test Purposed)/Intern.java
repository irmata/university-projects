package assignment3;

public class Intern extends Employee implements AdjustGrossSalaryInterface {

    private int gpa;

    Intern(String ID, String name, double grossSalary, int gpa) {
        super(ID, name, grossSalary);

        this.gpa = gpa;
        adjustGrossSalary();
    }

    @Override
    public void adjustGrossSalary() {

        if (gpa <= 5){

            setCurrentGrossSalary(0);

        } else if (gpa > 5 && gpa < 8){

            return;

        } else if (gpa >= 8){

            setCurrentGrossSalary(getInitialGrossSalary() + 1000);
        }
    }

    @Override
    public double getNetSalary(){

        return getCurrentGrossSalary();
    }

    public void setGpa(int newGpa){
        
        gpa = newGpa;

        adjustGrossSalary();
    }

    @Override
    public String toString() {

        String persistantTwoDecimalPointsGrossSalary = String.format("%.2f", getCurrentGrossSalary());
        
        return getName() + "'s gross salary is " + persistantTwoDecimalPointsGrossSalary + " SEK per month. GPA: " + gpa;
    }
}
