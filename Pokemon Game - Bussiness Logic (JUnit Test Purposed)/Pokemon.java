package assignment2;

public class Pokemon {

    private Skill skill;
    private String pokemonName;
    final String type;
    private int maxHP;
    private int currentHP; 
    private int energyPoints;
    private int currentEnergyPoints;

    public Pokemon(String pokemonName, int maxHP, String type){
       
        this.pokemonName = pokemonName;
        this.maxHP= maxHP;
        this.type = type;
        currentHP = maxHP;
        energyPoints = 100;
        currentEnergyPoints = energyPoints;

        equals(this);
    }

    public boolean equals(Object anotherPok){ 

        if(anotherPok == this){

            return true;

        } else if(anotherPok == null){

            return false;

        } else if( anotherPok instanceof Pokemon ){

            Pokemon otherPok = (Pokemon) anotherPok;

            boolean pokName = pokemonName.equals( otherPok.pokemonName);

            boolean pokType = currentHP == otherPok.currentHP;

            boolean pokHP = type.equals( otherPok.type);

            return pokName && pokHP && pokType;

        } else {

            return false;
        }
    }

    public String toString(){

        if (skill == null){
           
            return String.format("%s (%s)", pokemonName, type);
        
        } else {

            return String.format("%s (%s). Knows %s - AP: %d EC: %d", pokemonName, type, skill.getSkillName(), skill.getAttackPower(), skill.getEnergyCost());   
        }   
    }

    public int getEnergy(){

        return currentEnergyPoints;
    }
    
    public int getCurrentHP(){

        return currentHP;
    }
    
    public String getName(){

        return pokemonName;
    }
    
    public String getType(){

        return type;
    }
    
    public int getMAX_HP(){

        return maxHP;
    }
    
    public boolean knowsSkill(){

        if (skill != null){

            return true;

        } else {

            return false;
        }
    }

    public void learnSkill(String skillName, int attackPower, int energyCost){
        
        skill = new Skill(skillName, attackPower, energyCost);
    }

    public void forgetSkill(){

        skill = null;
    }

    private void consumeEnergyPoints(){

        currentEnergyPoints -= skill.getEnergyCost();
    }

    private void recieveDamage(Pokemon defendingPokemon, double appliedMultiplier){

        if (((skill.getAttackPower() * appliedMultiplier) > defendingPokemon.currentHP)){

            defendingPokemon.currentHP = 0;

        } else {

            defendingPokemon.currentHP = (int) Math.ceil(defendingPokemon.currentHP - (skill.getAttackPower() * appliedMultiplier));
        }
    }

    public String attack(Pokemon defendingPokemon){

        if (skill == null){

            return "Attack failed. " + pokemonName + " does not know a skill.";
        }

        else if (currentEnergyPoints < skill.getEnergyCost()){

            return "Attack failed. " + pokemonName + " lacks energy: " + currentEnergyPoints + "/" + skill.getEnergyCost();
        }

        else if (getCurrentHP() == 0){

            return "Attack failed. " + pokemonName + " fainted.";

        } else if (defendingPokemon.getCurrentHP() == 0){

            return "Attack failed. " + defendingPokemon.pokemonName + " fainted.";

        } else {

            consumeEnergyPoints();

            double appliedMultiplier = BattleCalculator.performTypeCalulcations(this, defendingPokemon);

            recieveDamage(defendingPokemon, appliedMultiplier);

            String attackString = String.format("%s uses %s on %s.", pokemonName, skill.getSkillName(), defendingPokemon.pokemonName);

            String hpLeftString = String.format("\n%s has %d HP left.", defendingPokemon.pokemonName, defendingPokemon.currentHP);

            String faintsString = String.format(" %s faints.", defendingPokemon.pokemonName);

            String effectivenessString = "";

            if (appliedMultiplier == 2.0){

                effectivenessString = " It is super effective!";

                attackString += effectivenessString + hpLeftString;

                if (defendingPokemon.currentHP == 0){

                    attackString += faintsString;
                } 

            } else if (appliedMultiplier == 0.5){

                effectivenessString = " It is not very effective...";

                attackString += effectivenessString + hpLeftString;

                if (defendingPokemon.currentHP == 0){   

                    attackString += faintsString;
                } 

            } else {

                attackString += hpLeftString;

                if (defendingPokemon.currentHP == 0){

                    attackString += faintsString;
                } 
            }

            return attackString; 
        } 
    }

    public void rest(){ 

        if (currentHP != 0){

            if ((currentHP + 20) > maxHP){

                currentHP = maxHP;

            } else {

                 currentHP = currentHP + 20;
            }
        }
    }

    public void recoverEnergy(){

        if (currentEnergyPoints != 0){

            if ((currentEnergyPoints + 25) > energyPoints){

                currentEnergyPoints = energyPoints;

            } else {

                currentEnergyPoints = currentEnergyPoints + 25;
            }
        }
    }

    public String useItem(Item item){

        if (currentHP == maxHP){

            return String.format("%s could not use %s. HP is already full.", pokemonName, item.getItemName());

        } else {

            if (((currentHP + item.getHealingPower()) > maxHP) == true){
                
                int remainingToBeHealedHealth = maxHP - currentHP;

                currentHP = maxHP;

                return String.format("%s used %s. It healed %d HP.", pokemonName, item.getItemName(), remainingToBeHealedHealth);

            } else {

                currentHP = currentHP + item.getHealingPower();

                return String.format("%s used %s. It healed %d HP.", pokemonName, item.getItemName(), item.getHealingPower());
            }
        }
    }
}