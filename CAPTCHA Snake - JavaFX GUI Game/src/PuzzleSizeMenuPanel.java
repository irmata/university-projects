import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class PuzzleSizeMenuPanel extends JPanel {
    
    PuzzleSizeMenuPanel (PuzzleSizeMenuFrame puzzleSizeMenuFrame) {

        setLayout(null);

        JLabel messageLabel = new JLabel("Please Select Size of CAPTCHA Puzzle:", SwingConstants.CENTER);
        messageLabel.setFont(new Font("ComicSans", Font.BOLD, 22));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2 ) - 300, 170, 600, 50);
        add(messageLabel);

        JButton buttonFive = new JButton("Five");
        buttonFive.setBackground(new Color(41, 171, 135));
        buttonFive.setForeground(Color.WHITE);
        buttonFive.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) - 160, 250, 80, 40);
        add(buttonFive);

        JButton buttonSix = new JButton("Six");
        buttonSix.setBackground(new Color(59, 122, 87));
        buttonSix.setForeground(Color.WHITE);
        buttonSix.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) - 80, 250, 80, 40);
        add(buttonSix);

        JButton buttonSeven = new JButton("Seven");
        buttonSeven.setBackground(new Color(53, 94, 59));
        buttonSeven.setForeground(Color.WHITE);
        buttonSeven.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2), 250, 80, 40);
        add(buttonSeven);

        JButton buttonEight = new JButton("Eight");
        buttonEight.setBackground(new Color(53, 66, 48));
        buttonEight.setForeground(Color.WHITE);
        buttonEight.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) + 80, 250, 80, 40);
        add(buttonEight);

        buttonFive.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                CAPTCHASnakeGame.setNrOf_CAPTCHA_Symbols(5);
                puzzleSizeMenuFrame.closeFrame();
                new StartMenuFrame();
            }
        });

        buttonSix.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                CAPTCHASnakeGame.setNrOf_CAPTCHA_Symbols(6);
                puzzleSizeMenuFrame.closeFrame();
                new StartMenuFrame();
            }
        });

        buttonSeven.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                CAPTCHASnakeGame.setNrOf_CAPTCHA_Symbols(7);
                puzzleSizeMenuFrame.closeFrame();
                new StartMenuFrame();
            }
        });

        buttonEight.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                CAPTCHASnakeGame.setNrOf_CAPTCHA_Symbols(8);
                puzzleSizeMenuFrame.closeFrame();
                new StartMenuFrame();
            }
        });
    }

    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);

        Image img = new ImageIcon("wallpapers/snakeGameBackground.png").getImage();

        g.drawImage(img, 0, -230, null);
        
    }    
}
