package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.util.EnumMode;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

public class PigeonFollowMasterGoal extends Goal {

    private final PigeonEntity pigeon;
    private final PathNavigator navigator;
    private final World world;
    private final double followSpeed;
    private final float stopDist; // If closer than stopDist stop moving towards owner
    private final float startDist; // If further than startDist moving towards owner

    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public static int getRandomNumber(LivingEntity entityIn, int minIn, int maxIn) {return entityIn.getRNG().nextInt(maxIn - minIn + 1) + minIn;}

    public PigeonFollowMasterGoal(PigeonEntity pigeonIn, double speedIn, float minDistIn, float maxDistIn) {
        this.pigeon = pigeonIn;
        this.world = pigeonIn.world;
        this.followSpeed = speedIn;
        this.navigator = pigeonIn.getNavigator();
        this.startDist = minDistIn;
        this.stopDist = maxDistIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity owner = this.pigeon.getOwner();
        if (owner == null) {
            return false;
        } else if (this.pigeon.getMode() == EnumMode.PATROL) {
            return false;
        } else if (owner.isSpectator()) {
            return false;
        } else {
            this.owner = owner;
            return true;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.navigator.noPath()) {
            return false;
        } else {
            return this.pigeon.getDistanceSq(this.owner) > this.stopDist * this.stopDist;
        }
    }

    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.pigeon.getPathPriority(PathNodeType.WATER);
        this.pigeon.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.navigator.clearPath();
        this.pigeon.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.pigeon.getLookController().setLookPositionWithEntity(this.owner, 10.0F, this.pigeon.getVerticalFaceSpeed());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;

            if (this.pigeon.getDistanceSq(this.owner) >= 144.0D) { // Further than 12 blocks away teleport
                tryToTeleportNearEntity(this.pigeon, this.navigator, this.owner, 4);
            } else {
                this.navigator.tryMoveToEntityLiving(this.owner, this.followSpeed);
            }

        }
    }

    public static boolean tryToTeleportNearEntity(LivingEntity entityIn, PathNavigator navigator, LivingEntity target, int radius) {
        return tryToTeleportNearEntity(entityIn, navigator, target.getPosition(), radius);
    }

    public static boolean tryToTeleportNearEntity(LivingEntity entityIn, PathNavigator navigator, BlockPos targetPos, int radius) {
        for (int i = 0; i < 10; ++i) {
            int j = getRandomNumber(entityIn, -radius, radius);
            int k = getRandomNumber(entityIn, -1, 1);
            int l = getRandomNumber(entityIn, -radius, radius);
            boolean flag = tryToTeleportToLocation(entityIn, navigator, targetPos, targetPos.getX() + j, targetPos.getY() + k, targetPos.getZ() + l);
            if (flag) {
                return true;
            }
        }
        return false;
    }

    public static boolean tryToTeleportToLocation(LivingEntity entityIn, PathNavigator navigator, BlockPos targetPos, int x, int y, int z) {
        if (Math.abs(x - targetPos.getX()) < 2.0D && Math.abs(z - targetPos.getZ()) < 2.0D) {
            return false;
        } else if (!isTeleportFriendlyBlock(entityIn, new BlockPos(x, y, z), false)) {
            return false;
        } else {
            entityIn.moveToBlockPosAndAngles(new BlockPos(x + 0.5F, y, z + 0.5F), entityIn.rotationYaw, entityIn.rotationPitch);
            navigator.clearPath();
            return true;
        }
    }

    private static boolean isTeleportFriendlyBlock(LivingEntity entityIn, BlockPos pos, boolean teleportToLeaves) {
        PathNodeType pathnodetype = WalkNodeProcessor.getFloorNodeType(entityIn.world, pos.toMutable());
        if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = entityIn.world.getBlockState(pos.down());
            if (!teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(entityIn.getPosition());
                return entityIn.world.hasNoCollisions(entityIn, entityIn.getBoundingBox().offset(blockpos));
            }
        }
    }


    public float getMinStartDistanceSq() {
        return this.startDist * this.startDist;
    }
}