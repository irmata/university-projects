package assignment2;

public abstract class BattleCalculator {
    
    static double performTypeCalulcations(Pokemon attackingPokemon, Pokemon defendingPokemon){

        if(attackingPokemon.getType().equals("Fire")){

            if (defendingPokemon.getType().equals("Fire") || defendingPokemon.getType().equals("Water")){

                return 0.5;

            } else if (defendingPokemon.getType().equals("Grass")){

                return 2.0;
            } 

        } else if (attackingPokemon.getType().equals("Grass")){

            if (defendingPokemon.getType().equals("Fire") || defendingPokemon.getType().equals("Grass")){

                return 0.5;

            } else if (defendingPokemon.getType().equals("Water")){

                return 2.0;
            } 

        } else if (attackingPokemon.getType().equals("Water")){

            if (defendingPokemon.getType().equals("Grass") || defendingPokemon.getType().equals("Water")){

                return 0.5;

            } else if (defendingPokemon.getType().equals("Fire")){

                return 2.0;
            } 
        }
            
        return 1.0;
    }
}