package assignment2;

import java.util.ArrayList;
import java.util.Locale;

public class ItemBag {
    
    private double maxWeight;
    private double currentWeight;
    private final ArrayList<Item> itemCollection;

    public ItemBag (double maxWeight){

        this.maxWeight = maxWeight;
        currentWeight = 0.0;
        itemCollection = new ArrayList<>();
    }

    public void setupLocale(){

        Locale.setDefault(Locale.ENGLISH);
    }

    public Item popItem(){

        if (itemCollection.isEmpty()){

            return null;

        } else {

            Item poppedItem = itemCollection.get(0);

            itemCollection.remove(0);

            currentWeight = currentWeight - poppedItem.getItemWeight();

            return poppedItem;
        }
    }

    public Object removeItemAt(int desiredRemovalPositionOfItem){

        if(desiredRemovalPositionOfItem < 0 || desiredRemovalPositionOfItem > itemCollection.size()-1){

            return null;
        }

        Item itemOfRemoval = itemCollection.get(desiredRemovalPositionOfItem);

        itemCollection.remove(desiredRemovalPositionOfItem);

        currentWeight = currentWeight - itemOfRemoval.getItemWeight();

        return itemOfRemoval;
    }


    public int addItem(Item potion){

        if (potion.getItemWeight() + currentWeight > maxWeight){

            return -1;

        } else {

            currentWeight += potion.getItemWeight();
          
            if (itemCollection.size() == 0){
            
                itemCollection.add(potion);
    
                return 0;
    
            } else {
    
                int indexOfItem = firstLastPosSearchToAdd(itemCollection, potion);
              
                if (indexOfItem == -1){
    
                    indexOfItem = binarySearchToAdd(itemCollection, itemCollection.size() - 2, 1, potion);
                }
    
                return indexOfItem;
            }
        }
    }

    private static int firstLastPosSearchToAdd(ArrayList<Item> itemCollection, Item potion){
        
        if (itemCollection.get(0).getItemWeight() <= potion.getItemWeight()){

            itemCollection.add(0, potion);

            return 0;

        } else if (itemCollection.get(itemCollection.size() - 1).getItemWeight() > potion.getItemWeight()){

            itemCollection.add(potion);

            return itemCollection.size() - 1;

        } else {

            if (itemCollection.size() == 2){
                
                itemCollection.add(1, potion);
                
                return 1;

            } else {

                return -1;
            }
        }
    }

    private static int binarySearchToAdd(ArrayList<Item> itemCollection, int upperBound, int lowerBound, Item potion){

        int upperLowerDiff = upperBound - lowerBound;

        int mid = (int) Math.ceil((Integer.valueOf(upperBound).doubleValue() + Integer.valueOf(lowerBound).doubleValue()) / 2);

        if (upperLowerDiff == 0){

            if (itemCollection.get(mid).getItemWeight() <= potion.getItemWeight()){
                
                itemCollection.add(mid, potion);
                
                return mid;

            } else {

                itemCollection.add(mid + 1, potion);
                
                return mid + 1;
            }

        } else if (upperLowerDiff == 1){

            if (itemCollection.get(mid).getItemWeight() >= potion.getItemWeight()){
                
                itemCollection.add(mid + 1, potion);
                
                return mid + 1;
               
            } else {

                return binarySearchToAdd(itemCollection, mid - 1, 1, potion);
            }   
        }

        if (itemCollection.get(mid).getItemWeight() == potion.getItemWeight()){

            itemCollection.add(mid, potion);

            return mid;

        } else if (itemCollection.get(mid).getItemWeight() < potion.getItemWeight()){

            return binarySearchToAdd(itemCollection, mid - 1, 1, potion);

        } else if (itemCollection.get(mid).getItemWeight() > potion.getItemWeight()){

            return binarySearchToAdd(itemCollection, itemCollection.size() - 2, mid + 1, potion);
        }

        return -1;
    }

    public String peekItemAt(int desiredPeekPositionOfItem){ 

        if(desiredPeekPositionOfItem < 0 || desiredPeekPositionOfItem > itemCollection.size()-1){

            return "";
        }

        Item peekingItem = itemCollection.get(desiredPeekPositionOfItem);

        return peekingItem.toString();
    }

    public double getCurrentWeight(){

        return currentWeight;
    }

    public int getNumOfItems(){

        return itemCollection.size();
    }

    public double getMaxWeight(){

        return maxWeight;
    }
}