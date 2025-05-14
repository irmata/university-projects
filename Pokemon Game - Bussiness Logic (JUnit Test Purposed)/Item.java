package assignment2;

public class Item {

    private String itemName;
    private int healingPower;
    private double itemWeight;
    private int currentPosition;
    
    public Item (String itemName, int healingPower, double itemWeight){

        this.itemName=itemName;
        this.healingPower=healingPower;
        this.itemWeight=itemWeight;
    }

    public double getItemWeight(){

        return itemWeight;
    }

    public int getCurrentPosition(){

        return currentPosition;
    }

    public int getHealingPower(){

        return healingPower;
    }

    public String getItemName(){

        return itemName;
    }

    public String toString(){

        return String.format("%s heals %d HP. (%2.2f)",this.itemName,this.healingPower,(Math.floor(this.itemWeight*100)/100));
    }

    public boolean equals(Object anotherItem){

        if(anotherItem == this){

            return true;

        } else if(anotherItem == null){

            return false;

        } else if( anotherItem instanceof Item ){

            Item otherPok = (Item) anotherItem;

            boolean name = this.itemName.equals( otherPok.itemName );

            boolean Heal = this.healingPower == otherPok.healingPower;

            boolean weight = this.itemWeight  == otherPok.itemWeight;

            return name && Heal && weight;

        } else {

            return false;
        }
    }
}
