public class CAPTCHASnakeGame {
    
    public static final int MENU_WIDTH = 800;
    public static final int MENU_HEIGHT = 500;
    public static final int GAME_WIDTH = 1680;
    public static final int GAME_HEIGHT = 840;

    private static boolean includeSwedishLetters = true;
    private static int nrOf_CAPTCHA_Symbols = 6;

    public static void main(String[] args){

        new StartMenuFrame();

    }
    
    public static boolean getIncludeSwedishLetters () {
        return includeSwedishLetters;
    }

    public static void setIncludeSwedishLetters (boolean arg) {
        includeSwedishLetters = arg;
    }

    public static int getNrOf_CAPTCHA_Symbols () {
        return nrOf_CAPTCHA_Symbols;
    }

    public static void setNrOf_CAPTCHA_Symbols (int arg) {
        nrOf_CAPTCHA_Symbols = arg;
    }
}
