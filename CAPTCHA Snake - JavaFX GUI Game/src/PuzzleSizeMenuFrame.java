import javax.swing.*;


class PuzzleSizeMenuFrame extends JFrame {
   
    PuzzleSizeMenuFrame (){

        this.setTitle("CAPTCHA Snake Game For User Verification");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(CAPTCHASnakeGame.MENU_WIDTH, CAPTCHASnakeGame.MENU_HEIGHT);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.add(new PuzzleSizeMenuPanel(this));

    }

    public void closeFrame(){
        super.dispose();
    }

}
