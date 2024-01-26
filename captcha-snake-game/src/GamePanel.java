import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.SwingUtilities;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;

 class GamePanel extends JPanel implements ActionListener {

    private GameFrame gameFrame;
    private final int BLOCK_LENGTH = 35;
    private final int NEW_FRAME_DELAY_MILLISECONDS = 100;
    
    private int nrOfSnakeBodyParts;
    private int nrOf_CAPTCHA_Taken;
    private boolean gameIsRunning;

    private Timer timer;
    private Random random;

    private int[] snake_X_Coordinates;
    private int[] snake_Y_Coordinates;
    private ArrayList<Integer> my_CAPTCHA_Puzzle_X_Coordinates;
    private ArrayList<Integer> my_CAPTCHA_Puzzle_Y_Coordinates;

    private KeyboardControls keyboardControls;
    private CAPTCHA my_CAPTCHA_Puzzle;
    private ArrayList<Image> my_CAPTCHA_PuzzleArrayListImage;

    private final int TIME_LIMIT = 60;
    private int elapsedTimeMilliseconds;

    private FontMetrics fontMetrics;

    GamePanel (GameFrame gameFrame) {

        this.gameFrame = gameFrame;

        nrOfSnakeBodyParts = 6;
        timer = new Timer(NEW_FRAME_DELAY_MILLISECONDS, this);
        random = new Random();
        snake_X_Coordinates = new int[600];
        snake_Y_Coordinates = new int[600];
        my_CAPTCHA_Puzzle_X_Coordinates = new ArrayList<>();
        my_CAPTCHA_Puzzle_Y_Coordinates = new ArrayList<>();
        keyboardControls = new KeyboardControls();
        addKeyListener(keyboardControls);
        my_CAPTCHA_Puzzle = new CAPTCHA();
        my_CAPTCHA_Puzzle.generatePuzzle();
        my_CAPTCHA_PuzzleArrayListImage = my_CAPTCHA_Puzzle.getMy_CAPTCHA_ArrayListImage();

        setFocusable(true);
        setLayout(null);

        startGame();

    }

    private void startGame() {
        
        gameIsRunning = true;

        generate_CAPTCHA_ImageCoordinates();

        timer.start();

    }

    private void generate_CAPTCHA_ImageCoordinates () {

        int CAPTCHA_Image_X_Coordinate = 0;
        int CAPTCHA_Image_Y_Coordinate = 0;
        boolean noSameCoordinates = true;

        for (int i = 0; i < my_CAPTCHA_PuzzleArrayListImage.size(); i++) {

            do {
                noSameCoordinates = true;

                CAPTCHA_Image_X_Coordinate = random.nextInt((CAPTCHASnakeGame.GAME_WIDTH / 2) / BLOCK_LENGTH) * BLOCK_LENGTH;
                CAPTCHA_Image_Y_Coordinate = random.nextInt(1, (CAPTCHASnakeGame.GAME_HEIGHT / BLOCK_LENGTH) - 1) * BLOCK_LENGTH;



                    for (int j = 0; j < my_CAPTCHA_Puzzle_X_Coordinates.size(); j++) {

                        if (my_CAPTCHA_Puzzle_X_Coordinates.get(j) == CAPTCHA_Image_X_Coordinate && my_CAPTCHA_Puzzle_Y_Coordinates.get(j) == CAPTCHA_Image_Y_Coordinate) {
    
                            noSameCoordinates = false;
                            break;
    
                        }
                    }


            } while (noSameCoordinates == false);

                my_CAPTCHA_Puzzle_X_Coordinates.add(CAPTCHA_Image_X_Coordinate);
                my_CAPTCHA_Puzzle_Y_Coordinates.add(CAPTCHA_Image_Y_Coordinate);

        }
    }

    @Override
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        if (gameIsRunning == false && nrOf_CAPTCHA_Taken == my_CAPTCHA_PuzzleArrayListImage.size()){

            timer.stop();
            drawVerificationSuccessScreen(g);

            ObjectMapper mapper = new ObjectMapper();

            UserSuccessRunStats userSuccessRunStats = new UserSuccessRunStats();

            userSuccessRunStats.setCaptcha_Size(CAPTCHASnakeGame.getNrOf_CAPTCHA_Symbols());

            int elapsedTime = elapsedTimeMilliseconds / 1000;

            userSuccessRunStats.setElapsedTime(elapsedTime);

            try {
                
                mapper.writeValue(new FileOutputStream("data/LastVerifiedRunData.json"), userSuccessRunStats);

            } catch (Exception e){
                
                e.printStackTrace();

            }

        } else if (gameIsRunning == false) {

            timer.stop();
            drawVerificationFailureScreen(g);

        } else {

            drawRunningGame(g);

        }

    }

    private void drawRunningGame(Graphics g){

        //left side of panel

        Image img = new ImageIcon("wallpapers/backgroundLeftGamePanel.png").getImage();

        g.drawImage(img, 0, 0, null);

            for (int i = 0; i < nrOfSnakeBodyParts; i++) {

                if (i == 0) {
                //snake head
                g.setColor(new Color(111, 78, 55));
                g.fillOval(snake_X_Coordinates[i], snake_Y_Coordinates[i], BLOCK_LENGTH, BLOCK_LENGTH);

                //snake eyes and mouth
                switch(keyboardControls.getSnakeMovingDirection()) {
                    case 'R' -> {
                        g.setColor(Color.BLACK);
                        g.fillOval(snake_X_Coordinates[i] + BLOCK_LENGTH / 2, snake_Y_Coordinates[i] + BLOCK_LENGTH / 4, BLOCK_LENGTH / 10, BLOCK_LENGTH / 10);
                        g.fillOval(snake_X_Coordinates[i] + BLOCK_LENGTH / 2, snake_Y_Coordinates[i] + BLOCK_LENGTH / 4 * 3, BLOCK_LENGTH / 10, BLOCK_LENGTH / 10);
                        g.setColor(Color.RED);
                        g.fillRect(snake_X_Coordinates[i] + BLOCK_LENGTH / 4 * 3, snake_Y_Coordinates[i] + BLOCK_LENGTH / 2, BLOCK_LENGTH / 2, BLOCK_LENGTH / 4);
                    }
                    case 'D' -> {
                        g.setColor(Color.BLACK);
                        g.fillOval(snake_X_Coordinates[i] + BLOCK_LENGTH / 4, snake_Y_Coordinates[i] + BLOCK_LENGTH / 2, BLOCK_LENGTH / 8, BLOCK_LENGTH / 10);
                        g.fillOval(snake_X_Coordinates[i] + BLOCK_LENGTH / 4 * 3, snake_Y_Coordinates[i] + BLOCK_LENGTH / 2, BLOCK_LENGTH / 8, BLOCK_LENGTH / 10);
                    }
                    case 'U' -> {
                        g.setColor(Color.BLACK);
                        g.fillOval(snake_X_Coordinates[i] + BLOCK_LENGTH / 4, snake_Y_Coordinates[i] + BLOCK_LENGTH / 2, BLOCK_LENGTH / 8, BLOCK_LENGTH / 10);
                        g.fillOval(snake_X_Coordinates[i] + BLOCK_LENGTH / 4 * 3, snake_Y_Coordinates[i] + BLOCK_LENGTH / 2, BLOCK_LENGTH / 8, BLOCK_LENGTH / 10);
                    }
                    case 'L' -> {
                        g.setColor(Color.BLACK);
                        g.fillOval(snake_X_Coordinates[i] + BLOCK_LENGTH - BLOCK_LENGTH / 10 - BLOCK_LENGTH / 2, snake_Y_Coordinates[i] + BLOCK_LENGTH / 4, BLOCK_LENGTH / 10, BLOCK_LENGTH / 10);
                        g.fillOval(snake_X_Coordinates[i] + BLOCK_LENGTH - BLOCK_LENGTH / 10 - BLOCK_LENGTH / 2, snake_Y_Coordinates[i] + BLOCK_LENGTH / 4 * 3, BLOCK_LENGTH / 10, BLOCK_LENGTH / 10);
                        g.setColor(Color.RED);
                        g.fillRect(snake_X_Coordinates[i] + BLOCK_LENGTH - BLOCK_LENGTH / 2 - BLOCK_LENGTH / 4 * 3, snake_Y_Coordinates[i] + BLOCK_LENGTH / 2, BLOCK_LENGTH / 2, BLOCK_LENGTH / 4);
                    }
                }
            } else {
                //snake body
                g.setColor(new Color(80, 125, 42));
                g.fillRect(snake_X_Coordinates[i], snake_Y_Coordinates[i], BLOCK_LENGTH, BLOCK_LENGTH);
            }
    
            }

            for (int i = nrOf_CAPTCHA_Taken; i < my_CAPTCHA_PuzzleArrayListImage.size(); i++) {

                g.drawImage(my_CAPTCHA_PuzzleArrayListImage.get(i), my_CAPTCHA_Puzzle_X_Coordinates.get(i), my_CAPTCHA_Puzzle_Y_Coordinates.get(i), null);
    
            }

            //right side of panel

            Image img2 = new ImageIcon("wallpapers/backgroundRightGamePanel.png").getImage();

            g.drawImage(img2, 840, 0, null);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Courier", Font.BOLD, 28));
            fontMetrics = getFontMetrics(g.getFont());

            g.drawString("Collect Symbols of CAPTCHA Puzzle:", (CAPTCHASnakeGame.GAME_WIDTH - (CAPTCHASnakeGame.GAME_WIDTH / 2 / 2)) - (fontMetrics.stringWidth("Collect The CAPTCHA Symbols:") / 2), 230);

            for (int i = nrOf_CAPTCHA_Taken; i < my_CAPTCHA_PuzzleArrayListImage.size(); i++) {

                g.drawImage(my_CAPTCHA_PuzzleArrayListImage.get(i), (CAPTCHASnakeGame.GAME_WIDTH - (CAPTCHASnakeGame.GAME_WIDTH / 2 / 2)) - (35 * my_CAPTCHA_PuzzleArrayListImage.size() / 2) + (35 * i), 260, null);
                
            }

            g.drawString("Collected:", (CAPTCHASnakeGame.GAME_WIDTH - (CAPTCHASnakeGame.GAME_WIDTH / 2 / 2)) - (fontMetrics.stringWidth("Collected:") / 2), 400);
            g.setColor(Color.WHITE);
            if (nrOf_CAPTCHA_Taken == 0) {

                g.drawString("None", (CAPTCHASnakeGame.GAME_WIDTH - (CAPTCHASnakeGame.GAME_WIDTH / 2 / 2)) - (fontMetrics.stringWidth("None") / 2), 440);

            } else {

                for (int i = 0; i < nrOf_CAPTCHA_Taken; i++) {

                    g.drawImage(my_CAPTCHA_PuzzleArrayListImage.get(i), (CAPTCHASnakeGame.GAME_WIDTH - (CAPTCHASnakeGame.GAME_WIDTH / 2 / 2)) - (35 * my_CAPTCHA_PuzzleArrayListImage.size() / 2) + (35 * i), 440, null);
    
                }

            }

            g.setColor(Color.RED);
            g.drawString("Time Remaining: " + (TIME_LIMIT - (elapsedTimeMilliseconds / 1000)) + "s", (CAPTCHASnakeGame.GAME_WIDTH - (CAPTCHASnakeGame.GAME_WIDTH / 2 / 2)) - (fontMetrics.stringWidth("Time Remaining: " + (TIME_LIMIT - (elapsedTimeMilliseconds / 1000)) + "s") / 2), 550);

    }

    private void drawVerificationSuccessScreen(Graphics g) {

        Image img = new ImageIcon("wallpapers/userVerifiedBackground.png").getImage();

        g.drawImage(img, 0, 0, null);
        
        g.setFont(new Font("Courier", Font.BOLD, 54));
        fontMetrics = getFontMetrics(g.getFont());
        g.setColor(new Color(230,230,0));
        g.drawString("Verification Complete", (CAPTCHASnakeGame.GAME_WIDTH / 2) - (fontMetrics.stringWidth("Verification Complete") / 2), 200);

        g.setFont(new Font("Courier", Font.BOLD, 36));
        fontMetrics = getFontMetrics(g.getFont());
        g.setColor(Color.WHITE);
        g.drawString("You Are Not A Robot!", (CAPTCHASnakeGame.GAME_WIDTH / 2) - (fontMetrics.stringWidth("You Are Not A Robot!") / 2), 300);

        g.setColor(new Color(230,230,0));
        fontMetrics = getFontMetrics(g.getFont());

        g.drawString("Time Elapsed: " + (elapsedTimeMilliseconds / 1000) + "s", (CAPTCHASnakeGame.GAME_WIDTH / 2) - (fontMetrics.stringWidth("Elapsed Time: " + (elapsedTimeMilliseconds / 1000) + "s") / 2), 400);

        g.drawString("Game Size: " + my_CAPTCHA_PuzzleArrayListImage.size() + " Symbols", (CAPTCHASnakeGame.GAME_WIDTH / 2) - (fontMetrics.stringWidth("Game Size: " + my_CAPTCHA_PuzzleArrayListImage.size() + " Symbols") / 2), 480);

        for (int i = 0; i < my_CAPTCHA_PuzzleArrayListImage.size(); i++){
            g.drawImage(my_CAPTCHA_PuzzleArrayListImage.get(i), (CAPTCHASnakeGame.GAME_WIDTH / 2) - (my_CAPTCHA_PuzzleArrayListImage.size() * 35 / 2) + (35 * i), 530, null);
        }

        JButton exitButton = new JButton("Exit");
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(Color.WHITE);
        exitButton.setBounds((CAPTCHASnakeGame.GAME_WIDTH / 2) - 50, 650, 100, 50);
        add(exitButton);

        exitButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
                
                //closes window
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }

        });

    }

    private void drawVerificationFailureScreen(Graphics g){

        Image img = new ImageIcon("wallpapers/wallp5.jpg").getImage();

        g.drawImage(img, 0, 0, null);

        g.setColor(Color.RED);
        g.setFont(new Font("Courier", Font.BOLD ,48));
        fontMetrics = getFontMetrics(g.getFont());

        g.drawString("Game Over", (CAPTCHASnakeGame.GAME_WIDTH / 2) - (fontMetrics.stringWidth("Game Over") / 2), 270);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Courier", Font.BOLD ,30));
        fontMetrics = getFontMetrics(g.getFont());

        g.drawString("Verification Failed", (CAPTCHASnakeGame.GAME_WIDTH / 2) - (fontMetrics.stringWidth("Verification Failed") / 2), 340);

        JButton mainMenuButton = new JButton("Main Menu");
        JButton retryButton = new JButton("Retry");
        JButton type_CAPTCHA_Button = new JButton("Type CAPTCHA");

        retryButton.setForeground(Color.WHITE);
        type_CAPTCHA_Button.setForeground(Color.WHITE);
        mainMenuButton.setForeground(Color.WHITE);

        retryButton.setBackground(new Color(150, 200, 162));
        type_CAPTCHA_Button.setBackground(new Color(0, 112, 60));
        mainMenuButton.setBackground(new Color(0, 66, 37));

        retryButton.setBounds((CAPTCHASnakeGame.GAME_WIDTH / 2) - (100 / 2), 420, 100, 50);
        type_CAPTCHA_Button.setBounds((CAPTCHASnakeGame.GAME_WIDTH / 2) - (150 / 2), 500, 150, 50);
        mainMenuButton.setBounds((CAPTCHASnakeGame.GAME_WIDTH / 2) - (100 / 2), 580, 100, 50);

        add(mainMenuButton);
        add(retryButton);
        add(type_CAPTCHA_Button);
        
        mainMenuButton.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e) {

                gameFrame.closeFrame();
                new StartMenuFrame();
            }

        });


        retryButton.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e) {

                gameFrame.closeFrame();
                new GameFrame();
            }

        });


        type_CAPTCHA_Button.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e) {

                gameFrame.closeFrame();
                new TypeCAPTCHAMenuFrame(my_CAPTCHA_Puzzle);
            }

        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        elapsedTimeMilliseconds += NEW_FRAME_DELAY_MILLISECONDS;

        if (gameIsRunning)
        {
            move();
            check_CAPTCHA_SymbolCollison();
            checkWallOrSelfCollision();
            checkTime();
        }
        repaint();
    }

    private void move(){ 
        for (int i = nrOfSnakeBodyParts; i>0; i--){
            snake_X_Coordinates[i] = snake_X_Coordinates[i-1];
            snake_Y_Coordinates[i] = snake_Y_Coordinates[i-1];
        }

        switch(keyboardControls.getSnakeMovingDirection()) {
            case 'L':
            snake_X_Coordinates[0] = snake_X_Coordinates[0] - BLOCK_LENGTH;
                break;
            case 'R':
            snake_X_Coordinates[0] = snake_X_Coordinates[0] + BLOCK_LENGTH;
                break;
            case 'U':
            snake_Y_Coordinates[0] = snake_Y_Coordinates[0] - BLOCK_LENGTH;
                break;
            case 'D':
            snake_Y_Coordinates[0] = snake_Y_Coordinates[0] + BLOCK_LENGTH;
                break;
        }
    }

    private void check_CAPTCHA_SymbolCollison(){

        if (nrOf_CAPTCHA_Taken == my_CAPTCHA_PuzzleArrayListImage.size()){
           
            gameIsRunning = false;
            return;

        } else if ((snake_X_Coordinates[0] == my_CAPTCHA_Puzzle_X_Coordinates.get(nrOf_CAPTCHA_Taken)) && (snake_Y_Coordinates[0] == my_CAPTCHA_Puzzle_Y_Coordinates.get(nrOf_CAPTCHA_Taken)))  {
            
            my_CAPTCHA_Puzzle_X_Coordinates.set(nrOf_CAPTCHA_Taken, -1);
            my_CAPTCHA_Puzzle_Y_Coordinates.set(nrOf_CAPTCHA_Taken, -1);
            nrOfSnakeBodyParts++;
            nrOf_CAPTCHA_Taken++;

            return;

        } else {

            for (int i = my_CAPTCHA_PuzzleArrayListImage.size() - 1; i > 0; i--){

                if ((snake_X_Coordinates[0] == my_CAPTCHA_Puzzle_X_Coordinates.get(i)) && (snake_Y_Coordinates[0] == my_CAPTCHA_Puzzle_Y_Coordinates.get(i))){
                    gameIsRunning = false;
                    return;
                }
            }

        }
    }

    private void checkWallOrSelfCollision(){

        for(int i = nrOfSnakeBodyParts;i>0;i--) {
            if((snake_X_Coordinates[0] == snake_X_Coordinates[i]) && (snake_Y_Coordinates[0] == snake_Y_Coordinates[i])) {
                gameIsRunning = false;
                return;
            }
        }

        if(snake_X_Coordinates[0] < 0) {
            gameIsRunning = false;
        }
        if(snake_X_Coordinates[0] > CAPTCHASnakeGame.GAME_WIDTH / 2 - BLOCK_LENGTH) {
            gameIsRunning = false;
        }
        if(snake_Y_Coordinates[0] < 0) {
            gameIsRunning = false;
        }
        if(snake_Y_Coordinates[0] > CAPTCHASnakeGame.GAME_HEIGHT - BLOCK_LENGTH) {
            gameIsRunning = false;
        }
        if(!gameIsRunning) {
        }
    }

    private void checkTime(){

        if (elapsedTimeMilliseconds >= TIME_LIMIT*1000){
            gameIsRunning = false;
        }
    }

}
