import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

 class CAPTCHA {
    
    public static final String UPPER_CASE= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String SWEDISH_UPPER_CASE= "ÅÄÖ";
    public static final String LOWER_CASE= "abcdefghijklmnopqrstuvwxyz";
    public static final String SWEDISH_LOWER_CASE= "åäö";
    public static final String NUMBERS = "0123456789";

    private String my_CAPTCHA_PuzzleString; //String representation of CAPTCHA puzzle eg. "Aö7u..."
    private ArrayList<String> my_CAPTCHA_PuzzleArrayListString; //arraylist of string letters of CAPTCHA puzzle eg. (A, ö, 7, u, ...)
    private ArrayList<Image> my_CAPTCHA_PuzzleArrayListImage; //arraylist of CAPTCHA Images corresponding to each letter eg. (A.png, ö.png, 7.png, ...)
    private ArrayList<String> filePaths; //Arraylist of file paths eg. (CAPTCHA/upperCase/A.png, CAPTCHA/lowerCase/ö.png, CAPTCHA/numbers/7.png, ...)
    private Random random;

    public CAPTCHA(){
        random = new Random();
        my_CAPTCHA_PuzzleArrayListString = new ArrayList<>();
        my_CAPTCHA_PuzzleArrayListImage = new ArrayList<>();
        filePaths = new ArrayList<>();
        my_CAPTCHA_PuzzleString = "";
    }

    //generatePuzzle() generates CAPTCHA puzzle according to user specifications
    public void generatePuzzle(){
        String alphabet = UPPER_CASE + LOWER_CASE + NUMBERS;

        //if user wants to include Swedish letters, we append the alphabet
        if (CAPTCHASnakeGame.getIncludeSwedishLetters()){
            alphabet = alphabet + SWEDISH_UPPER_CASE + SWEDISH_LOWER_CASE;
        }

        // the for loop generates a CAPTCHA puzzle of size specified by user
        for (int i=0; i < CAPTCHASnakeGame.getNrOf_CAPTCHA_Symbols(); i++){
            while(my_CAPTCHA_PuzzleArrayListString.size()<=i){
                //generate random character from specified alphabet incl. uppercase letters, lowercase letters, numbers
                int randomInt = random.nextInt(0, alphabet.length()-1);
                String randomSymbol = String.valueOf(alphabet.charAt(randomInt));

                //adds the randomly generated character to list if it is not already in list (no repeats allowed)
                if(!my_CAPTCHA_PuzzleArrayListString.contains(randomSymbol)){
                    my_CAPTCHA_PuzzleArrayListString.add(randomSymbol);
                    my_CAPTCHA_PuzzleString = my_CAPTCHA_PuzzleString + randomSymbol;
                }
            }

            //sets filepath folder based on type of letter
            String filePathModifier = "";
            if(UPPER_CASE.contains(my_CAPTCHA_PuzzleArrayListString.get(i))){
                filePathModifier = "upperCase";
            }else if(LOWER_CASE.contains(my_CAPTCHA_PuzzleArrayListString.get(i))){
                filePathModifier = "lowerCase";
            }else if(SWEDISH_UPPER_CASE.contains(my_CAPTCHA_PuzzleArrayListString.get(i))){
                filePathModifier = "upperCase";
            }else if(SWEDISH_LOWER_CASE.contains(my_CAPTCHA_PuzzleArrayListString.get(i))){
                filePathModifier = "lowerCase";
            }else{
                filePathModifier = "numbers";
            }

            //MyCAPTCHAImage is arraylist used to store the CAPTCHA images
            my_CAPTCHA_PuzzleArrayListImage.add(new ImageIcon("CAPTCHA/" + filePathModifier + "/" + my_CAPTCHA_PuzzleArrayListString.get(i) + ".png").getImage());
            //filePaths is used to store the filePaths in the directory
            filePaths.add("CAPTCHA/" + filePathModifier + "/" + my_CAPTCHA_PuzzleArrayListString.get(i) + ".png");
        }
    }

    //getters
    public String getMy_CAPTCHA_PuzzleString(){
        return this.my_CAPTCHA_PuzzleString;
    }
    public ArrayList<String> getMy_CAPTCHA_PuzzleStringArrayList(){
        return this.my_CAPTCHA_PuzzleArrayListString;
    }
    public ArrayList<Image> getMy_CAPTCHA_ArrayListImage(){
        return this.my_CAPTCHA_PuzzleArrayListImage;
    }
    public ArrayList<String> getFilePaths(){
        return this.filePaths;
    }

}
