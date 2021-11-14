package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.util.EnumMode;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class PigeonWanderGoal extends Goal {

    protected final PigeonEntity pigeon;
    protected final double speed;
    protected int executionChance;

    public PigeonWanderGoal(PigeonEntity pigeonIn, double speedIn) {
        this.pigeon = pigeonIn;
        this.speed = speedIn;
        this.executionChance = 60;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.pigeon.isTamed()) {
            return false;
        }
        if (!this.pigeon.isMode(EnumMode.WANDERING)) {
            return false;
        }
        return true;
    }

    @Override
    public void tick() {
        if (this.pigeon.getIdleTime() >= 100) {
            return;
        } else if (this.pigeon.getRNG().nextInt(this.executionChance) != 0) {
            return;
        } if (this.pigeon.hasPath()) {
            return;
        }

//        Vector3d pos = this.getPosition();
//        this.pigeon.getNavigator().tryMoveToXYZ(pos.x, pos.y, pos.z, this.speed);
    }

//    @Nullable
//    protected Vector3d getPosition() {
//        PathNavigator pathNavigate = this.pigeon.getNavigator();
//        Random random = this.pigeon.getRNG();
//
//        int xzRange = 5;
//        int yRange = 3;
//
//        float bestWeight = Float.MIN_VALUE;
//        Optional<BlockPos> bowlPos = this.pigeon.getBowlPos();
//        BlockPos bestPos = bowlPos.get();
//
//        for (int attempt = 0; attempt < 5; ++attempt) {
//            int l = random.nextInt(2 * xzRange + 1) - xzRange;
//            int i1 = random.nextInt(2 * yRange + 1) - yRange;
//            int j1 = random.nextInt(2 * xzRange + 1) - xzRange;
//
//            BlockPos testPos = bowlPos.get().offset(l, i1, j1);
//
//            if (pathNavigate.isStableDestination(testPos)) {
//                float weight = this.pigeon.getWalkTargetValue(testPos);
//
//                if (weight > bestWeight) {
//                    bestWeight = weight;
//                    bestPos = testPos;
//                }
//            }
//        }
//        return new Vector3d(bestPos.getX(), bestPos.getY(), bestPos.getZ());
//    }
}