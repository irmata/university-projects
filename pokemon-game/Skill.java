package assignment2;

public class Skill {

    final String skillName;
    final int attackPower;
    final int energyCost;

    Skill (String skillName, int attackPower, int energyCost){

        this.skillName = skillName;
        this.attackPower = attackPower;
        this.energyCost = energyCost;
    }

    String getSkillName(){

        return skillName;
    }

    int getAttackPower(){

        return attackPower;
    }

    int getEnergyCost(){

        return energyCost;
    }
}