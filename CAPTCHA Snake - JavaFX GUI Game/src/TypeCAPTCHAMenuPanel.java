
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;

 class TypeCAPTCHAMenuPanel extends JPanel {

    private CAPTCHA my_CAPTCHA_Puzzle;

    TypeCAPTCHAMenuPanel(CAPTCHA my_CAPTCHA_Puzzle){
        
        this.my_CAPTCHA_Puzzle = my_CAPTCHA_Puzzle;
        startTyping();
        setFocusable(true);
        setLayout(null);
    }

    public void startTyping(){

        for (int i=0; i<my_CAPTCHA_Puzzle.getMy_CAPTCHA_ArrayListImage().size(); i++){
            JLabel myImgLabel = new JLabel();
            myImgLabel.setIcon(new ImageIcon(my_CAPTCHA_Puzzle.getFilePaths().get(i)));
            myImgLabel.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) - ((CAPTCHASnakeGame.getNrOf_CAPTCHA_Symbols() * 35) / 2) + (i * 35), 150, 35, 35);
            add(myImgLabel);
        }

        // create a label to display text
        JLabel l = new JLabel("Type the CAPTCHA Above:", SwingConstants.CENTER);

        l.setFont(new Font("Calibri", Font.BOLD, 16));
        l.setForeground(new Color(150, 180, 150));

        // create a new button
        JButton b = new JButton("Submit");

        // create a object of the text class
        JTextField t = new JTextField(16);

        // addActionListener to submit button -- checks if typed text field matches string equivalent of CAPTCHA puzzle
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String s = e.getActionCommand();
                if (s.equals("Submit")) {
                    // set the text of the label to the text of the field
                    if(t.getText().equals(my_CAPTCHA_Puzzle.getMy_CAPTCHA_PuzzleString())){
                        l.setText("Congratulations! You are not a robot. Type Exit to leave the program.");
                        t.setText("");

                        // Save verification to json file
                        ObjectMapper mapper = new ObjectMapper();
                        UserSuccessRunStats userSuccessRunStats = new UserSuccessRunStats();
                        userSuccessRunStats.setCaptcha_Size(CAPTCHASnakeGame.getNrOf_CAPTCHA_Symbols());
                        int elapsedTime = -1;
                        userSuccessRunStats.setElapsedTime(elapsedTime);
                        try {
                            mapper.writeValue(new FileOutputStream("data/LastVerifiedRunData.json"), userSuccessRunStats);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        
                    }else if(t.getText().equals("Exit") || t.getText().equals("exit")) {
                        //closes window
                        JComponent comp = (JComponent) e.getSource();
                        Window win = SwingUtilities.getWindowAncestor(comp);
                        win.dispose();
                    }else{
                        l.setText("Incorrect. Try again!");
                    }
                }
            }
        });

        l.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) - (500 / 2), 165, 500, 100);
        t.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) - (150 / 2), 250, 150, 30);
        b.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) - (100 / 2), 300, 100, 30);

        // add buttons and textfield to panel
        this.add(t);
        this.add(l);
        this.add(b);

    }

    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);

        Image img = new ImageIcon("wallpapers/wallp6.jpg").getImage();

        g.drawImage(img, 0, 0, null);
        
    }  
}
