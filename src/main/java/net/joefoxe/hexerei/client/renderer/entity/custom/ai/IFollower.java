package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;

public interface IFollower {
    boolean shouldFollow();

    default void followEntity(TameableEntity tameable, LivingEntity owner, double followSpeed){
        tameable.getNavigator().tryMoveToEntityLiving(owner, followSpeed);
    }
}