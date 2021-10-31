package net.joefoxe.hexerei.client.renderer.entity.custom;

import net.joefoxe.hexerei.client.renderer.entity.ModEntityTypes;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.DirectPathNavigator;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.FlightMoveController;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;

public class PigeonEntity extends ParrotEntity {

//    private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.BOOLEAN);
//    private static final DataParameter<Integer> ATTACK_TICK = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.VARINT);
//    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.BOOLEAN);
//    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.VARINT);
//    private static final DataParameter<Optional<BlockPos>> PERCH_POS = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
    public int timeFlying = 0;
    private boolean isLandNavigator;


    public PigeonEntity(EntityType<? extends ParrotEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathPriority(PathNodeType.COCOA, -1.0F);
        this.setPathPriority(PathNodeType.FENCE, -1.0F);
        switchNavigator(false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH,3.0D)
                .createMutableAttribute(Attributes.FLYING_SPEED, 1.3D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED,0.2D);

    }

    public boolean canTrample(BlockState state, BlockPos pos, float fallDistance) {
        return false;
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }


    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source == DamageSource.IN_WALL || source == DamageSource.FALLING_BLOCK || source == DamageSource.CACTUS || super.isInvulnerableTo(source);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return ModEntityTypes.PIGEON.get().create(serverWorld);
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1,new PanicGoal(this,1.25D));
        this.goalSelector.addGoal(2,new WaterAvoidingRandomWalkingGoal(this,1.0D));
        this.goalSelector.addGoal(3,new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7,new LookRandomlyGoal(this));
        this.goalSelector.addGoal(7, new PigeonEntity.AIWalkIdle());
    }

    public void tick() {
        super.tick();

        if (!world.isRemote) {
            if (isFlying() && this.isLandNavigator) {
                switchNavigator(false);
            }
            if (!isFlying() && !this.isLandNavigator) {
                switchNavigator(true);
            }
            if (isFlying()) {
                timeFlying++;

                //if(timeFlying > 20 && this.getMotion().getY() > 0)
                    this.setNoGravity(true);
//                System.out.println("Flying");

            } else {
                timeFlying = 0;
                this.setNoGravity(false);
//                System.out.println("Sitting");
            }
        }
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveController = new FlightMoveController(this, 0.7F, false);
            this.navigator = new GroundPathNavigator(this, world);
            this.isLandNavigator = true;
            this.setAIMoveSpeed(0.01f);
        } else {
            this.moveController = new FlightMoveController(this, 0.7F, false);
            this.setMotion(this.getMotion().getX(), this.getMotion().getY(), this.getMotion().getZ());
            this.navigator = new DirectPathNavigator(this, world);
            this.isLandNavigator = false;
        }
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player)
    {
        return 1 + this.world.rand.nextInt(4);
    }

    private boolean isOverWater() {
        BlockPos position = this.getPosition();
        while (position.getY() > 2 && world.isAirBlock(position)) {
            position = position.down();
        }
        return !world.getFluidState(position).isEmpty();
    }

    @Override
    public SoundEvent getAmbientSound() {
        this.playSound(SoundEvents.ENTITY_CHICKEN_AMBIENT, 0.2F, 1.0F);
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        this.playSound(SoundEvents.ENTITY_RABBIT_HURT, 1.0F, 1.7F);
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(SoundEvents.ENTITY_PARROT_DEATH, 0.7F, 2.0F);
        return null;
    }


    private class AIWalkIdle extends Goal {
        protected final PigeonEntity crow;
        protected double x;
        protected double y;
        protected double z;
        private boolean flightTarget = false;

        public AIWalkIdle() {
            super();
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
            this.crow = PigeonEntity.this;
        }

        @Override
        public boolean shouldExecute() {

            if (this.crow.getRNG().nextInt(30) != 0 && !crow.isLandNavigator) {
                return false;
            }
            if (this.crow.isOnGround()) {
                this.flightTarget = rand.nextBoolean();
            } else {
                this.flightTarget = rand.nextInt(5) > 0 && crow.timeFlying < 200;
            }
            Vector3d lvt_1_1_ = this.getPosition();
            if (lvt_1_1_ == null) {
                return false;
            } else {
                this.x = lvt_1_1_.x;
                this.y = lvt_1_1_.y;
                this.z = lvt_1_1_.z;
                return true;
            }

        }

        public void tick() {
            if (flightTarget) {
                crow.getMoveHelper().setMoveTo(x, y, z, 1F);
            } else {
                this.crow.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, 1F);
            }
            if (!flightTarget && isFlying() && crow.onGround) {
                crow.switchNavigator(false);
            }
            if (isFlying() && crow.onGround && crow.timeFlying > 10) {
                crow.switchNavigator(true);
            }
        }

        @Nullable
        protected Vector3d getPosition() {
            Vector3d vector3d = crow.getPositionVec();

            if(crow.isOverWater()){
                flightTarget = true;
            }
            if (flightTarget) {
                return RandomPositionGenerator.findRandomTarget(this.crow, 10, 7);
//                if (crow.timeFlying < 50 || crow.isOverWater()) {
//                    return crow.getBlockInViewAway(vector3d, 0);
//                } else {
//                    return crow.getBlockGrounding(vector3d);
//                }
            } else {

                return RandomPositionGenerator.findRandomTarget(this.crow, 10, 7);
            }
        }

        public boolean shouldContinueExecuting() {

            if (flightTarget) {
                return crow.isFlying() && crow.getDistanceSq(x, y, z) > 2F;
            } else {
                return (!this.crow.getNavigator().noPath()) && !this.crow.isBeingRidden();
            }
        }

        public void startExecuting() {
            if (flightTarget) {
//                crow.setFlying(true);
                crow.getMoveHelper().setMoveTo(x, y, z, 1F);
            } else {
                this.crow.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, 1F);
            }
        }

        public void resetTask() {
            this.crow.getNavigator().clearPath();
            super.resetTask();
        }
    }



    @Override
    public Vector3d getLeashStartPosition() {
        return new Vector3d(5.0D, (5.5F * this.getEyeHeight()), (this.getWidth() * 5.4F));
    }
}