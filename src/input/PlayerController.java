package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerController extends KeyHandler implements KeyListener {
    
    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Moving Input
        if (code == KeyEvent.VK_W)
            isMoveUpPressed = true;
        if (code == KeyEvent.VK_A)
            isMoveLeftPressed = true;
        if (code == KeyEvent.VK_S)
            isMoveDownPressed = true;
        if (code == KeyEvent.VK_D)
            isMoveRightPressed = true;

        // Shooting Input
        if (code == KeyEvent.VK_UP)
            isShootUpPressed = true;
        if (code == KeyEvent.VK_LEFT)
            isShootLeftPressed = true;
        if (code == KeyEvent.VK_DOWN)
            isShootDownPressed = true;
        if (code == KeyEvent.VK_RIGHT)
            isShootRightPressed = true;
        
        if (code == KeyEvent.VK_SPACE)
            spacePressed = true;

        setAxises();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        // Moving Input
        if (code == KeyEvent.VK_W)
            isMoveUpPressed = false;
        if (code == KeyEvent.VK_A)
            isMoveLeftPressed = false;
        if (code == KeyEvent.VK_S)
            isMoveDownPressed = false;
        if (code == KeyEvent.VK_D)
            isMoveRightPressed = false;

        // Shooting Input
        if (code == KeyEvent.VK_UP)
            isShootUpPressed = false;
        if (code == KeyEvent.VK_LEFT)
            isShootLeftPressed = false;
        if (code == KeyEvent.VK_DOWN)
            isShootDownPressed = false;
        if (code == KeyEvent.VK_RIGHT)
            isShootRightPressed = false;
        
        if (code == KeyEvent.VK_SPACE)
            spacePressed = false;

        setAxises();
    }

    private void setAxises() {
        inputMoveX = calculateAxis(isMoveRightPressed, isMoveLeftPressed);
        inputMoveY = calculateAxis(isMoveUpPressed, isMoveDownPressed);
        inputShootX = calculateAxis(isShootRightPressed, isShootLeftPressed);
        inputShootY = calculateAxis(isShootUpPressed, isShootDownPressed);
    }

    private int calculateAxis(boolean positivePressed, boolean negativePressed) {
        if (positivePressed && negativePressed) {
            return 0;
        }
        if (positivePressed) {
            return 1;
        }
        if (negativePressed) {
            return -1;
        }
        return 0;
    }
}
