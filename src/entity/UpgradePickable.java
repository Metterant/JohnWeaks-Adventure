package entity;

import java.awt.Graphics2D;

import entity.player.Player;
import ui.PopUpText;
import util.EntityManager;
import util.GameConstants;

public abstract class UpgradePickable extends Pickable {

    protected Integer itemCost;
    public String getUpgradePickable() { return name; }
    private PopUpText popUpText;

    //#region CONSTRUCTORS
    protected UpgradePickable() {
        super();
    }
    protected UpgradePickable(int row, int col) {
        super(row, col);
    }
    protected UpgradePickable(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion
    
    /**
     * Handle picking up action
     * 
     * @param entity Player that is going to pick up this item
     */
    @Override
    public void getPickedUp(Player player) {
        EntityManager.getInstance().removeAllPickables();
    }

    @Override
    public void start() {
        super.start();
        
        itemCost = 0;
        timeToLive = -1;
        setUpPopUpText();
    }

    @Override
    public void update() {
        super.update();
    }
    
    private void setUpPopUpText() {
        int textPosX = (int)posX + GameConstants.TILE_SIZE / 2;
        int textPosY = (int)(posY + GameConstants.TILE_SIZE * 1.5f);

        popUpText = new PopUpText(textPosX, textPosY);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, (int)posX, (int)posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);

        if (itemCost != 0) {
            String format = "%d Coins";
            String text = String.format(format, itemCost);

            popUpText.drawText(text, g2);
        }
    }
}
