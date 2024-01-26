import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.awt.event.ActionEvent;



class StartMenuPanel extends JPanel {

    StartMenuPanel (StartMenuFrame startMenuFrame) {

        setLayout(null);
        setVisible(true);

        JLabel startMessageLabel = new JLabel("Welcome to the CAPTCHA Snake Game!", SwingConstants.CENTER);
        startMessageLabel.setFont(new Font("ComicSans", Font.BOLD, 24));
        startMessageLabel.setForeground(Color.WHITE);
        startMessageLabel.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2 ) - 300, 140, 600, 50);
        this.add(startMessageLabel);

        JButton startGameButton= new JButton("Start Game");
        startGameButton.setFont(new Font("Ariel", Font.BOLD, 12));
        startGameButton.setBackground(new Color(53, 66, 48));
        startGameButton.setForeground(Color.WHITE);
        startGameButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
                startMenuFrame.closeFrame();
                new GameFrame();
            }
        });
        startGameButton.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) - 110, 200, 100, 50);
        this.add(startGameButton);


        JButton customizeGameButton = new JButton("Settings");
        customizeGameButton.setFont(new Font("Ariel", Font.BOLD, 12));
        customizeGameButton.setBackground(new Color(85,107,47));
        customizeGameButton.setForeground(Color.WHITE);
        customizeGameButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
                startMenuFrame.closeFrame();
                new LanguageMenuFrame();
            }
        });

        customizeGameButton.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) + 10, 200, 100, 50);
        this.add(customizeGameButton);

        JLabel lastSessionTitle = new JLabel("Last Successful Verification:", SwingConstants.CENTER);
        lastSessionTitle.setFont(new Font("ComicSans", Font.BOLD, 18));
        lastSessionTitle.setForeground(Color.WHITE);
        lastSessionTitle.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) - (300 / 2), 280, 300, 40);
        add(lastSessionTitle);

        ObjectMapper mapper = new ObjectMapper();
        
        UserSuccessRunStats userSuccessRunStats = null;

        try {

            userSuccessRunStats = mapper.readValue(new FileInputStream("data/LastVerifiedRunData.json"), UserSuccessRunStats.class);

        } catch (Exception e){

            e.printStackTrace();

        }

        JLabel lastVerifiedSessionGameSize = new JLabel("Game Size: " + userSuccessRunStats.getCaptcha_Size() + " Symbols", SwingConstants.CENTER);
        lastVerifiedSessionGameSize.setFont(new Font("ComicSans", Font.BOLD, 16));
        lastVerifiedSessionGameSize.setForeground(Color.WHITE);
        lastVerifiedSessionGameSize.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) - (300 / 2), 310, 300, 40);
        add(lastVerifiedSessionGameSize);
       
       if (userSuccessRunStats.getElapsedTime() >= 0) {
            JLabel lastVerifiedSessionElapsedTime = new JLabel("Elapsed Time: " + userSuccessRunStats.getElapsedTime() + "s", SwingConstants.CENTER);
            lastVerifiedSessionElapsedTime.setFont(new Font("ComicSans", Font.BOLD, 16));
            lastVerifiedSessionElapsedTime.setForeground(Color.WHITE);
            lastVerifiedSessionElapsedTime.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) - (300 / 2), 340, 300, 40);
            add(lastVerifiedSessionElapsedTime);
        }else if(userSuccessRunStats.getElapsedTime() == -1){
            JLabel lastVerifiedSessionElapsedTime = new JLabel("(user verified by typing correct puzzle)", SwingConstants.CENTER);
            lastVerifiedSessionElapsedTime.setFont(new Font("ComicSans", Font.BOLD, 16));
            lastVerifiedSessionElapsedTime.setForeground(Color.WHITE);
            lastVerifiedSessionElapsedTime.setBounds((CAPTCHASnakeGame.MENU_WIDTH / 2) - (300 / 2), 340, 300, 40);
            add(lastVerifiedSessionElapsedTime);
        }

    }

    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);

        Image img = new ImageIcon("wallpapers/snakeGameBackground.png").getImage();

        g.drawImage(img, 0, -230, null);
        
    }

}
