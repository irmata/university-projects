import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class LanguageMenuPanel extends JPanel {
    
    LanguageMenuPanel (LanguageMenuFrame languageMenuFrame) {

        setLayout(null);

        JLabel welcomeMsg = new JLabel("Please Select Your Alphabet:", SwingConstants.CENTER);
        welcomeMsg.setFont(new Font("ComicSans", Font.BOLD, 22));
        welcomeMsg.setForeground(Color.WHITE);
        welcomeMsg.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2 ) - 300, 170, 600, 50);
        add(welcomeMsg);

        JButton swedishLanguageButton = new JButton("Swedish");
        swedishLanguageButton.setFont(new Font("Ariel", Font.BOLD, 12));
        swedishLanguageButton.setForeground(Color.BLUE);
        swedishLanguageButton.setBackground(Color.yellow);
        swedishLanguageButton.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2), 250, 100, 40);
        add(swedishLanguageButton);

        JButton englishLanguageButton = new JButton("English");
        englishLanguageButton.setFont(new Font("Ariel", Font.BOLD, 12));
        englishLanguageButton.setForeground(Color.WHITE);
        englishLanguageButton.setBackground(Color.BLUE);
        englishLanguageButton.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) - 100, 250, 100, 40);
        add(englishLanguageButton);

        swedishLanguageButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                CAPTCHASnakeGame.setIncludeSwedishLetters(true);
                languageMenuFrame.closeFrame();
                new PuzzleSizeMenuFrame();
            }
        });

        englishLanguageButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                CAPTCHASnakeGame.setIncludeSwedishLetters(false);
                languageMenuFrame.closeFrame();
                new PuzzleSizeMenuFrame();
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
