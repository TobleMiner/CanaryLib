package net.canarymod.hook.player;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.hook.Hook;

public class BedExitHook extends Hook{
    private Player player;
    private Block bed;

    public BedExitHook(Player player, Block bed){
	this.player = player;
	this.bed = bed;
    }

    /**
     * Gets the {@link Player} leaving the bed
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Get the bed {@link Block} the {@link Player} is leaving
     *
     * @return bed block player is leaving
     */
    public Block getBed() {
        return bed;
    }
    
    @Override
    public String toString(){
	return String.format("%s[Player=%s, Block=%s]", getHookName(), player, bed);
    }
}