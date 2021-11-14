package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.tileentity.CofferTile;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class MoveToSpecificBlockGoal extends Goal {

    protected final MobEntity mob;

    public MoveToSpecificBlockGoal(MobEntity mobIn) {
        this.mob = mobIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    //
    @Override
    public boolean shouldExecute() {
        return this.mob.getBlockState() != null && this.mob.isAlive();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return ((PigeonEntity)this.mob).hasPath() && !((PigeonEntity)this.mob).getTargetBlock().withinDistance(this.mob.getPosition(), 0.5);
    }

//    @Override
//    public void stop() {
//        BlockPos target = ((PigeonEntity)this.mob).getTargetBlock();
//        CofferTile cofferTileEntity = WorldUtil.getTileEntity(mob, target, CofferTile.class);
//
//        if (cofferTileEntity != null) {
//            // Double check the bed still has no owner
//            if (cofferTileEntity.getOwnerUUID() == null) {
//                cofferTileEntity.setOwner(this.mob);
//                this.mob.setCofferPos(this.mob.getEntityWorld().getDimensionKey(), target);
//            }
//        }
//        this.mob.setTargetBlock(null);
//        this.mob.setImmobile(true); // idk make it stop somewhere specific or something
//        //this.mob.level. // figure the rest out idk
//    }
//
//    @Override
//    public void start() {
//        BlockPos target = this.mob.getTargetBlock();
//        this.mob.getNavigator().tryMoveToXYZ((target.getX()) + 0.5D, target.getY() + 1, (target.getZ()) + 0.5D, 1.0D);
//    }
}