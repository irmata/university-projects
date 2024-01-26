
import javax.swing.*;

 class TypeCAPTCHAMenuFrame extends JFrame{

    TypeCAPTCHAMenuFrame(CAPTCHA my_CAPTCHA_Puzzle){
        //sets frame properties
        add(new TypeCAPTCHAMenuPanel(my_CAPTCHA_Puzzle));
        this.setTitle("CAPTCHA Snake Game For User Verification");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(CAPTCHASnakeGame.MENU_WIDTH, CAPTCHASnakeGame.MENU_HEIGHT);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
