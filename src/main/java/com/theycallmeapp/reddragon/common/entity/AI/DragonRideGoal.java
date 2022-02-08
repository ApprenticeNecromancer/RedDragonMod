package com.theycallmeapp.reddragon.common.entity.AI;

import com.theycallmeapp.reddragon.common.entity.RedDragonEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;

public class DragonRideGoal extends Goal {
    public enum Direction {FORWARD, BACKWARD, UP, DOWN, LEFT, RIGHT;}

    Direction direction;
    RedDragonEntity dragon;

    /**
     * We need to setup a control paramter and a message relay control if we can't figure out how to get the player's isJumping boolean
     * Seems like we are leaning towards setting up a separate control message relay. We have to figure out a message relay
     * @param dragon
     */
    public DragonRideGoal(RedDragonEntity dragon) {
        this.dragon = dragon;

    }

    @Override
    public boolean canUse() {
        return dragon.getPilot() != null;
    }

    @Override
    public void start() {

    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    public Direction getControlFlags() {
        return direction;
    }

    public void setControlFlags(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void tick() {
        super.tick();
    }

}
