package input;

public abstract class InputController  {

    // Horizontal and Verticle Movement Input
    protected float inputMoveX, inputMoveY;
    // Horizontal and Verticle direction when shooting
    protected int inputShootX, inputShootY; 

    // Space button
    protected boolean spacePressed;

    public float getInputMoveX() { return inputMoveX; }
    public float getInputMoveY() { return inputMoveY; }

    public int getInputShootX() { return inputShootX; }
    public int getInputShootY() { return inputShootY; }

    public boolean getSpaceInput() { return spacePressed; }

    public abstract void calculateInput();
}