import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

 class KeyboardControls extends KeyAdapter {

    private char snakeMovingDirection;

    KeyboardControls () {

        snakeMovingDirection = 'R';
    }

    public char getSnakeMovingDirection() {
        
        return snakeMovingDirection;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {

        switch(e.getKeyCode()) {

            case KeyEvent.VK_LEFT:

                if(snakeMovingDirection != 'R') {

                    snakeMovingDirection = 'L';

                }

                break;

            case KeyEvent.VK_RIGHT:

                if(snakeMovingDirection != 'L') {

                    snakeMovingDirection = 'R';

                }

                break;

            case KeyEvent.VK_UP:

                if(snakeMovingDirection != 'D') {

                    snakeMovingDirection = 'U';

                }
                
                break;

            case KeyEvent.VK_DOWN:

                if(snakeMovingDirection != 'U') {

                    snakeMovingDirection = 'D';

                }

                break;

        }

    }

}
