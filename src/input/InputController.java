package input;

public abstract class InputController  {

    // Horizontal and Verticle Movement Input
    protected int inputMoveX, inputMoveY;
    // Horizontal and Verticle direction when shooting
    protected int inputShootX, inputShootY; 

    // Space button
    protected boolean spacePressed;

    public int getInputMoveX() { return inputMoveX; }
    public int getInputMoveY() { return inputMoveY; }

    public int getInputShootX() { return inputShootX; }
    public int getInputShootY() { return inputShootY; }

    public boolean getSpaceInput() { return spacePressed; }
}