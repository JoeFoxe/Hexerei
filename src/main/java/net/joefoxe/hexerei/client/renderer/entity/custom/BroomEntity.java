package net.joefoxe.hexerei.client.renderer.entity.custom;

import net.joefoxe.hexerei.client.renderer.entity.ModEntityTypes;
import net.joefoxe.hexerei.item.ModItems;
import net.joefoxe.hexerei.particle.ModParticleTypes;
import net.joefoxe.hexerei.util.HexereiPacketHandler;
import net.joefoxe.hexerei.util.message.MessageAccelerating;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.client.CSteerBoatPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.TeleportationRepositioner;
import net.minecraft.util.TransportationHelper;
import net.minecraftforge.fml.network.NetworkHooks;

public class BroomEntity extends Entity {
    private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.createKey(BroomEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.createKey(BroomEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DAMAGE_TAKEN = EntityDataManager.createKey(BroomEntity.class, DataSerializers.FLOAT);
//    private static final DataParameter<Integer> BOAT_TYPE = EntityDataManager.createKey(BroomEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> LEFT_PADDLE = EntityDataManager.createKey(BroomEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RIGHT_PADDLE = EntityDataManager.createKey(BroomEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> ROCKING_TICKS = EntityDataManager.createKey(BroomEntity.class, DataSerializers.VARINT);
//    protected static final DataParameter<Integer> ACCELERATION_DIRECTION = EntityDataManager.createKey(BroomEntity.class, DataSerializers.VARINT);
//    protected static final DataParameter<Float> CURRENT_SPEED = EntityDataManager.createKey(BroomEntity.class, DataSerializers.FLOAT);
//    protected static final DataParameter<Float> MAX_SPEED = EntityDataManager.createKey(BroomEntity.class, DataSerializers.FLOAT);
//    protected static final DataParameter<Float> ACCELERATION_SPEED = EntityDataManager.createKey(BroomEntity.class, DataSerializers.FLOAT);
//    protected AccelerationDirection prevAcceleration;
//    public float prevCurrentSpeed;
    public float speedMultiplier = 0.75f;
//    protected boolean charging;
    private final float[] paddlePositions = new float[2];
    private float momentum;
    private float outOfControlTicks;
    public float deltaRotation;
    public float floatingOffset;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;
    private double lerpPitch;
    private boolean leftInputDown;
    private boolean rightInputDown;
    private boolean forwardInputDown;
    private boolean backInputDown;
    private boolean jumpInputDown;
    private boolean sneakingInputDown;
    private double waterLevel;
    private float boatGlide;
    private BroomEntity.Status status;
    private BroomEntity.Status previousStatus;
    private double lastYd;
    private boolean rocking;
    private boolean downwards;
    private float rockingIntensity;
    private float rockingAngle;
    private float prevRockingAngle;

    public BroomEntity(World worldIn, double x, double y, double z) {
        this(ModEntityTypes.BROOM.get(), worldIn);
        this.setPosition(x, y, z);
        this.setMotion(Vector3d.ZERO);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    public BroomEntity(EntityType<BroomEntity> broomEntityEntityType, World world) {
        super(broomEntityEntityType, world);
        this.preventEntitySpawning = true;
    }

    @Override
    protected float getEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void registerData() {
        this.dataManager.register(TIME_SINCE_HIT, 0);
        this.dataManager.register(FORWARD_DIRECTION, 1);
        this.dataManager.register(DAMAGE_TAKEN, 0.0F);
        this.dataManager.register(LEFT_PADDLE, false);
        this.dataManager.register(RIGHT_PADDLE, false);
        this.dataManager.register(ROCKING_TICKS, 0);
    }

    @Override
    public boolean canCollide(Entity entity) {
        return func_242378_a(this, entity);
    }

    public static boolean func_242378_a(Entity p_242378_0_, Entity entity) {
        return (entity.func_241845_aY() || entity.canBePushed()) && !p_242378_0_.isRidingSameEntity(entity);
    }

    @Override
    public boolean func_241845_aY() {
        return true;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    protected Vector3d func_241839_a(Direction.Axis axis, TeleportationRepositioner.Result result) {
        return LivingEntity.func_242288_h(super.func_241839_a(axis, result));
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    @Override
    public double getMountedYOffset() {
        return floatingOffset;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!this.world.isRemote && !this.removed) {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
            this.markVelocityChanged();
            boolean flag = source.getTrueSource() instanceof PlayerEntity && ((PlayerEntity)source.getTrueSource()).abilities.isCreativeMode;
            if (flag || this.getDamageTaken() > 40.0F) {
                if (!flag && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                    this.entityDropItem(this.getItemBoat());
                }

                this.remove();
            }

            return true;
        } else {
            return true;
        }
    }

    @Override
    public void onEnterBubbleColumnWithAirAbove(boolean downwards) {
        if (!this.world.isRemote) {
            this.rocking = true;
            this.downwards = downwards;
            if (this.getRockingTicks() == 0) {
                this.setRockingTicks(60);
            }
        }

        this.world.addParticle(ParticleTypes.SPLASH, this.getPosX() + (double)this.rand.nextFloat(), this.getPosY() + 0.7D, this.getPosZ() + (double)this.rand.nextFloat(), 0.0D, 0.0D, 0.0D);
        if (this.rand.nextInt(20) == 0) {
            this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), this.getSplashSound(), this.getSoundCategory(), 1.0F, 0.8F + 0.4F * this.rand.nextFloat(), false);
        }

    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (entityIn instanceof BroomEntity) {
            if (entityIn.getBoundingBox().minY < this.getBoundingBox().maxY) {
                super.applyEntityCollision(entityIn);
            }
        } else if (entityIn.getBoundingBox().minY <= this.getBoundingBox().minY) {
            super.applyEntityCollision(entityIn);
        }

    }

    public Item getItemBoat() {
        return ModItems.BROOM.get();
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void performHurtAnimation() {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11.0F);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return !this.removed;
    }

    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYaw = (double)yaw;
        this.lerpPitch = (double)pitch;
        this.lerpSteps = 10;
    }

    /**
     * Gets the horizontal facing direction of this Entity, adjusted to take specially-treated entity types into account.
     */
    @Override
    public Direction getAdjustedHorizontalFacing() {
        return this.getHorizontalFacing().rotateY();
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        this.previousStatus = this.status;
        this.status = this.getBoatStatus();
        if (this.status != BroomEntity.Status.UNDER_WATER && this.status != BroomEntity.Status.UNDER_FLOWING_WATER) {
            this.outOfControlTicks = 0.0F;
        } else {
            ++this.outOfControlTicks;
        }

        if (!this.world.isRemote && this.outOfControlTicks >= 60.0F) {
            this.removePassengers();
        }

        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0.0F) {
            this.setDamageTaken(this.getDamageTaken() - 1.0F);
        }

        Entity entityPassenger = this.getControllingPassenger();
        if(this.world.isRemote()) {
            if (entityPassenger instanceof LivingEntity && entityPassenger.equals(Minecraft.getInstance().player)) {
                ClientPlayerEntity player = Minecraft.getInstance().player;
                this.setNoGravity(true);
                updateInputs(player.movementInput.leftKeyDown, player.movementInput.rightKeyDown, player.movementInput.forwardKeyDown, player.movementInput.backKeyDown, player.movementInput.jump, player.movementInput.sneaking);
            } else if (entityPassenger == null) {
                this.setNoGravity(false);
            }
        }

//        this.setBoundingBox(AxisAlignedBB); TODO make standing broom

        super.tick();
        this.tickLerp();
        if (this.canPassengerSteer()) {
            if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof PlayerEntity)) {
                this.setPaddleState(false, false);
            }

            floatingOffset = moveTo(floatingOffset, 0.05f + (float)Math.sin(world.getGameTime()/30f) * 0.15f, 0.02f);


            this.updateMotion();
            if (this.world.isRemote) {
                this.controlBoat();
                this.world.sendPacketToServer(new CSteerBoatPacket(this.getPaddleState(0), this.getPaddleState(1)));
            }

            this.move(MoverType.SELF, this.getMotion());


            Random random = new Random();
            if(random.nextInt(50)==0) {
                float rotOffset = random.nextFloat() * 10 - 5;
                world.addParticle(ModParticleTypes.BROOM.get(), getPosX() - Math.sin(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), getPosY() + floatingOffset + 0.2f * random.nextFloat() - this.getMotion().getY(), getPosZ() + Math.cos(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d);
            }
            if(random.nextInt(20)==0) {
                float rotOffset = random.nextFloat() * 10 - 5;
                world.addParticle(ModParticleTypes.BROOM_2.get(), getPosX() - Math.sin(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), getPosY() + floatingOffset + 0.2f * random.nextFloat() - this.getMotion().getY(), getPosZ() + Math.cos(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d);
            }
            if(random.nextInt(80)==0) {
                float rotOffset = random.nextFloat() * 10 - 5;
                world.addParticle(ModParticleTypes.BROOM_3.get(), getPosX() - Math.sin(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), getPosY() + floatingOffset + 0.2f * random.nextFloat() - this.getMotion().getY(), getPosZ() + Math.cos(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d);
            }
            if(random.nextInt(500)==0) {
                float rotOffset = random.nextFloat() * 10 - 5;
                world.addParticle(ModParticleTypes.BROOM_4.get(), getPosX() - Math.sin(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), getPosY() + floatingOffset + 0.2f * random.nextFloat() - this.getMotion().getY(), getPosZ() + Math.cos(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d);
            }
            if(random.nextInt(500)==0) {
                float rotOffset = random.nextFloat() * 10 - 5;
                world.addParticle(ModParticleTypes.BROOM_5.get(), getPosX() - Math.sin(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), getPosY() + floatingOffset + 0.2f * random.nextFloat() - this.getMotion().getY(), getPosZ() + Math.cos(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d);
            }
            if(random.nextInt(500)==0) {
                float rotOffset = random.nextFloat() * 10 - 5;
                world.addParticle(ModParticleTypes.BROOM_6.get(), getPosX() - Math.sin(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), getPosY() + floatingOffset + 0.2f * random.nextFloat() - this.getMotion().getY(), getPosZ() + Math.cos(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d);
            }


        } else {
            this.setMotion(Vector3d.ZERO);
            floatingOffset = moveTo(floatingOffset, 0, 0.02f);
        }

        this.updateRocking();

        for(int i = 0; i <= 1; ++i) {
            if (this.getPaddleState(i)) {
                if (!this.isSilent() && (double)(this.paddlePositions[i] % ((float)Math.PI * 2F)) <= (double)((float)Math.PI / 4F) && ((double)this.paddlePositions[i] + (double)((float)Math.PI / 8F)) % (double)((float)Math.PI * 2F) >= (double)((float)Math.PI / 4F)) {
                    SoundEvent soundevent = this.getPaddleSound();
                    if (soundevent != null) {
                        Vector3d vector3d = this.getLook(1.0F);
                        double d0 = i == 1 ? -vector3d.z : vector3d.z;
                        double d1 = i == 1 ? vector3d.x : -vector3d.x;
                        this.world.playSound((PlayerEntity)null, this.getPosX() + d0, this.getPosY(), this.getPosZ() + d1, soundevent, this.getSoundCategory(), 1.0F, 0.8F + 0.4F * this.rand.nextFloat());
                    }
                }

                this.paddlePositions[i] = (float)((double)this.paddlePositions[i] + (double)((float)Math.PI / 8F));
            } else {
                this.paddlePositions[i] = 0.0F;
            }
        }

        this.doBlockCollisions();
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().grow((double)0.2F, (double)-0.01F, (double)0.2F), EntityPredicates.pushableBy(this));
        if (!list.isEmpty()) {
            boolean flag = !this.world.isRemote && !(this.getControllingPassenger() instanceof PlayerEntity);

            for(int j = 0; j < list.size(); ++j) {
                Entity entity = list.get(j);
                if (!entity.isPassenger(this)) {
                    if (flag && this.getPassengers().size() < 1 && !entity.isPassenger() && entity.getWidth() < this.getWidth() && entity instanceof LivingEntity && !(entity instanceof WaterMobEntity) && !(entity instanceof PlayerEntity)) {
                        //entity.startRiding(this);
                    } else {
                        this.applyEntityCollision(entity);
                    }
                }
            }
        }


        if(world.isRemote() && this.getMotion().length() >= 0.01d) {
            Random random = new Random();
            if(random.nextInt(5)==0) {
                float rotOffset = random.nextFloat() * 10 - 5;
                world.addParticle(ModParticleTypes.BROOM.get(), getPosX() - Math.sin(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), getPosY() + floatingOffset + 0.2f * random.nextFloat() - this.getMotion().getY(), getPosZ() + Math.cos(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d);
            }
            if(random.nextInt(2)==0) {
                float rotOffset = random.nextFloat() * 10 - 5;
                world.addParticle(ModParticleTypes.BROOM_2.get(), getPosX() - Math.sin(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), getPosY() + floatingOffset + 0.2f * random.nextFloat() - this.getMotion().getY(), getPosZ() + Math.cos(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d);
            }
            if(random.nextInt(8)==0) {
                float rotOffset = random.nextFloat() * 10 - 5;
                world.addParticle(ModParticleTypes.BROOM_3.get(), getPosX() - Math.sin(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), getPosY() + floatingOffset + 0.2f * random.nextFloat() - this.getMotion().getY(), getPosZ() + Math.cos(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d);
            }
            if(random.nextInt(50)==0) {
                float rotOffset = random.nextFloat() * 10 - 5;
                world.addParticle(ModParticleTypes.BROOM_4.get(), getPosX() - Math.sin(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), getPosY() + floatingOffset + 0.2f * random.nextFloat() - this.getMotion().getY(), getPosZ() + Math.cos(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d);
            }
            if(random.nextInt(50)==0) {
                float rotOffset = random.nextFloat() * 10 - 5;
                world.addParticle(ModParticleTypes.BROOM_5.get(), getPosX() - Math.sin(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), getPosY() + floatingOffset + 0.2f * random.nextFloat() - this.getMotion().getY(), getPosZ() + Math.cos(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d);
            }
            if(random.nextInt(50)==0) {
                float rotOffset = random.nextFloat() * 10 - 5;
                world.addParticle(ModParticleTypes.BROOM_6.get(), getPosX() - Math.sin(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), getPosY() + floatingOffset + 0.2f * random.nextFloat() - this.getMotion().getY(), getPosZ() + Math.cos(((this.rotationYaw - 90f + deltaRotation + rotOffset) / 180f) * (Math.PI)) * (1.25f + this.getMotion().length()/4), (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d, (random.nextDouble() - 0.5d) * 0.015d);
            }
        }

    }

    private float moveTo(float input, float moveTo, float speed)
    {
        float distance = moveTo - input;

        if(Math.abs(distance) <= speed)
        {
            return moveTo;
        }

        if(distance > 0)
        {
            input += speed;
        } else {
            input -= speed;
        }

        return input;
    }


    @OnlyIn(Dist.CLIENT)
    public void onClientUpdate()
    {
        Entity entity = this.getControllingPassenger();
        if(entity instanceof LivingEntity && entity.equals(Minecraft.getInstance().player))
        {
            LivingEntity livingEntity = (LivingEntity) entity;

            AccelerationDirection acceleration = getAccelerationDirection(livingEntity);
//            if(this.getAcceleration() != acceleration)
//            {
//                this.setAcceleration(acceleration);
//                HexereiPacketHandler.instance.sendToServer(new MessageAccelerating(acceleration));
//            }

//            TurnDirection direction = getTurnDirection(livingEntity);
//            if(this.getTurnDirection() != direction)
//            {
//                this.setTurnDirection(direction);
//                PacketHandler.instance.sendToServer(new MessageTurnDirection(direction));
//            }
//
//            float targetTurnAngle = getTargetTurnAngle(this, false);
//            this.setTargetTurnAngle(targetTurnAngle);
//            PacketHandler.instance.sendToServer(new MessageTurnAngle(targetTurnAngle));
        }

    }


    public static AccelerationDirection getAccelerationDirection(LivingEntity entity)
    {
        GameSettings settings = Minecraft.getInstance().gameSettings;
        boolean forward = settings.keyBindForward.isKeyDown();
        boolean reverse = settings.keyBindBack.isKeyDown();
        if(forward && reverse)
        {
            return BroomEntity.AccelerationDirection.CHARGING;
        }
        else if(forward)
        {
            return BroomEntity.AccelerationDirection.FORWARD;
        }
        else if(reverse)
        {
            return BroomEntity.AccelerationDirection.REVERSE;
        }

        return BroomEntity.AccelerationDirection.fromEntity(entity);
    }

    private void updateRocking() {
        if (this.world.isRemote) {
            int i = this.getRockingTicks();
            if (i > 0) {
                this.rockingIntensity += 0.05F;
            } else {
                this.rockingIntensity -= 0.1F;
            }

            this.rockingIntensity = MathHelper.clamp(this.rockingIntensity, 0.0F, 1.0F);
            this.prevRockingAngle = this.rockingAngle;
            this.rockingAngle = 10.0F * (float)Math.sin((double)(0.5F * (float)this.world.getGameTime())) * this.rockingIntensity;
        } else {
            if (!this.rocking) {
                this.setRockingTicks(0);
            }

            int k = this.getRockingTicks();
            if (k > 0) {
                --k;
                this.setRockingTicks(k);
                int j = 60 - k - 1;
                if (j > 0 && k == 0) {
                    this.setRockingTicks(0);
                    Vector3d vector3d = this.getMotion();
                    if (this.downwards) {
                        this.setMotion(vector3d.add(0.0D, -0.7D, 0.0D));
                        this.removePassengers();
                    } else {
                        this.setMotion(vector3d.x, this.isPassenger(PlayerEntity.class) ? 2.7D : 0.6D, vector3d.z);
                    }
                }

                this.rocking = false;
            }
        }

    }

    @Nullable
    protected SoundEvent getPaddleSound() {
        switch(this.getBoatStatus()) {
            case IN_WATER:
            case UNDER_WATER:
            case UNDER_FLOWING_WATER:
                return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
            case ON_LAND:
                return SoundEvents.ENTITY_BOAT_PADDLE_LAND;
            case IN_AIR:
            default:
                return null;
        }
    }

    private void tickLerp() {
        if (this.canPassengerSteer()) {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getPosX(), this.getPosY(), this.getPosZ());
        }

        if (this.lerpSteps > 0) {
            double d0 = this.getPosX() + (this.lerpX - this.getPosX()) / (double)this.lerpSteps;
            double d1 = this.getPosY() + (this.lerpY - this.getPosY()) / (double)this.lerpSteps;
            double d2 = this.getPosZ() + (this.lerpZ - this.getPosZ()) / (double)this.lerpSteps;
            double d3 = MathHelper.wrapDegrees(this.lerpYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.lerpSteps);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.lerpPitch - (double)this.rotationPitch) / (double)this.lerpSteps);
            --this.lerpSteps;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
    }


    public void setPaddleState(boolean left, boolean right) {
        this.dataManager.set(LEFT_PADDLE, left);
        this.dataManager.set(RIGHT_PADDLE, right);
    }

    @OnlyIn(Dist.CLIENT)
    public float getRowingTime(int side, float limbSwing) {
        return this.getPaddleState(side) ? (float)MathHelper.clampedLerp((double)this.paddlePositions[side] - (double)((float)Math.PI / 8F), (double)this.paddlePositions[side], (double)limbSwing) : 0.0F;
    }

    /**
     * Determines whether the boat is in water, gliding on land, or in air
     */
    private BroomEntity.Status getBoatStatus() {
        BroomEntity.Status boatentity$status = this.getUnderwaterStatus();
        if (boatentity$status != null) {
            this.waterLevel = this.getBoundingBox().maxY;
            return boatentity$status;
        } else if (this.checkInWater()) {
            return BroomEntity.Status.IN_WATER;
        } else {
            float f = this.getBoatGlide();
            if (f > 0.0F) {
                this.boatGlide = f;
                return BroomEntity.Status.ON_LAND;
            } else {
                return BroomEntity.Status.IN_AIR;
            }
        }
    }

    public float getWaterLevelAbove() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(axisalignedbb.maxY - this.lastYd);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        label39:
        for(int k1 = k; k1 < l; ++k1) {
            float f = 0.0F;

            for(int l1 = i; l1 < j; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutable.setPos(l1, k1, i2);
                    FluidState fluidstate = this.world.getFluidState(blockpos$mutable);
                    if (fluidstate.isTagged(FluidTags.WATER)) {
                        f = Math.max(f, fluidstate.getActualHeight(this.world, blockpos$mutable));
                    }

                    if (f >= 1.0F) {
                        continue label39;
                    }
                }
            }

            if (f < 1.0F) {
                return (float)blockpos$mutable.getY() + f;
            }
        }

        return (float)(l + 1);
    }

    /**
     * Decides how much the boat should be gliding on the land (based on any slippery blocks)
     */
    public float getBoatGlide() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY - 0.001D, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        int i = MathHelper.floor(axisalignedbb1.minX) - 1;
        int j = MathHelper.ceil(axisalignedbb1.maxX) + 1;
        int k = MathHelper.floor(axisalignedbb1.minY) - 1;
        int l = MathHelper.ceil(axisalignedbb1.maxY) + 1;
        int i1 = MathHelper.floor(axisalignedbb1.minZ) - 1;
        int j1 = MathHelper.ceil(axisalignedbb1.maxZ) + 1;
        VoxelShape voxelshape = VoxelShapes.create(axisalignedbb1);
        float f = 0.0F;
        int k1 = 0;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int l1 = i; l1 < j; ++l1) {
            for(int i2 = i1; i2 < j1; ++i2) {
                int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);
                if (j2 != 2) {
                    for(int k2 = k; k2 < l; ++k2) {
                        if (j2 <= 0 || k2 != k && k2 != l - 1) {
                            blockpos$mutable.setPos(l1, k2, i2);
                            BlockState blockstate = this.world.getBlockState(blockpos$mutable);
                            if (!(blockstate.getBlock() instanceof LilyPadBlock) && VoxelShapes.compare(blockstate.getCollisionShapeUncached(this.world, blockpos$mutable).withOffset((double)l1, (double)k2, (double)i2), voxelshape, IBooleanFunction.AND)) {
                                f += blockstate.getSlipperiness(this.world, blockpos$mutable, this);
                                ++k1;
                            }
                        }
                    }
                }
            }
        }

        return f / (float)k1;
    }

    private boolean checkInWater() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.ceil(axisalignedbb.minY + 0.001D);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        this.waterLevel = Double.MIN_VALUE;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutable.setPos(k1, l1, i2);
                    FluidState fluidstate = this.world.getFluidState(blockpos$mutable);
                    if (fluidstate.isTagged(FluidTags.WATER)) {
                        float f = (float)l1 + fluidstate.getActualHeight(this.world, blockpos$mutable);
                        this.waterLevel = Math.max((double)f, this.waterLevel);
                        flag |= axisalignedbb.minY < (double)f;
                    }
                }
            }
        }

        return flag;
    }

    /**
     * Decides whether the boat is currently underwater.
     */
    @Nullable
    private BroomEntity.Status getUnderwaterStatus() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        double d0 = axisalignedbb.maxY + 0.001D;
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(d0);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutable.setPos(k1, l1, i2);
                    FluidState fluidstate = this.world.getFluidState(blockpos$mutable);
                    if (fluidstate.isTagged(FluidTags.WATER) && d0 < (double)((float)blockpos$mutable.getY() + fluidstate.getActualHeight(this.world, blockpos$mutable))) {
                        if (!fluidstate.isSource()) {
                            return BroomEntity.Status.UNDER_FLOWING_WATER;
                        }

                        flag = true;
                    }
                }
            }
        }

        return flag ? BroomEntity.Status.UNDER_WATER : null;
    }

    /**
     * Update the boat's speed, based on momentum.
     */
    private void updateMotion() {
        double d0 = (double)-0.04F;
        double d1 = this.hasNoGravity() ? 0.0D : (double)-0.04F;
        double d2 = 0.0D;
        this.momentum = 0.05F;
        if (this.previousStatus == BroomEntity.Status.IN_AIR && this.status != BroomEntity.Status.IN_AIR && this.status != BroomEntity.Status.ON_LAND) {
            this.waterLevel = this.getPosYHeight(1.0D);
            this.setPosition(this.getPosX(), (double)(this.getWaterLevelAbove() - this.getHeight()) + 0.101D, this.getPosZ());
            this.setMotion(this.getMotion().mul(1.0D, 0.0D, 1.0D));
            this.lastYd = 0.0D;
            this.status = BroomEntity.Status.IN_WATER;
        } else {
            if (this.status == BroomEntity.Status.IN_WATER) {
                d2 = (this.waterLevel - this.getPosY()) / (double)this.getHeight();
                this.momentum = 0.9F;
            } else if (this.status == BroomEntity.Status.UNDER_FLOWING_WATER) {
//                d1 = -7.0E-4D;
                this.momentum = 0.9F;
            } else if (this.status == BroomEntity.Status.UNDER_WATER) {
                d2 = (double)0.01F;
                this.momentum = 0.45F;
            } else if (this.status == BroomEntity.Status.IN_AIR) {
                this.momentum = 0.9F;
            } else if (this.status == BroomEntity.Status.ON_LAND) {
                this.momentum = this.boatGlide;
                if (this.getControllingPassenger() instanceof PlayerEntity) {
                    this.boatGlide /= 2.0F;
                }
            }

            Vector3d vector3d = this.getMotion();
            this.setMotion(vector3d.x * (double)this.momentum, vector3d.y + d1, vector3d.z * (double)this.momentum);
            this.deltaRotation *= this.momentum;
            if (d2 > 0.0D) {
                Vector3d vector3d1 = this.getMotion();
                this.setMotion(vector3d1.x, (vector3d1.y + d2 * 0.06153846016296973D) * 0.75D, vector3d1.z);
            }
        }
        if(this.hasNoGravity() && this.getMotion().getY() > 0)
            this.setMotion(this.getMotion().getX(), this.getMotion().getY()/1.5f < 0 ? 0 : this.getMotion().getY()/1.5f, this.getMotion().getZ());
        if(this.hasNoGravity() && this.getMotion().getY() < 0)
            this.setMotion(this.getMotion().getX(), this.getMotion().getY()/1.5f > 0 ? 0 : this.getMotion().getY()/1.5f, this.getMotion().getZ());

    }

    private void controlBoat() {

        GameSettings settings = Minecraft.getInstance().gameSettings;
        boolean down = settings.keyBindSprint.isKeyDown();

        if (this.isBeingRidden()) {
            float f = 0.0F;
            if (this.leftInputDown) {
                --this.deltaRotation;
            }

            if (this.rightInputDown) {
                ++this.deltaRotation;
            }

            if (this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown) {
                f += 0.02F;
            }

            this.rotationYaw += this.deltaRotation;
            if (this.forwardInputDown) {
                f += 0.1F;
            }

            if (this.backInputDown) {
                f -= 0.02F;
            }
            if(this.jumpInputDown) {
                this.setMotion(this.getMotion().add(0, 0.15f * speedMultiplier, 0));
            }
            if(down) {
                this.setMotion(this.getMotion().add(0, -0.15f * speedMultiplier, 0));
            }
            this.setMotion(this.getMotion().add((double)(MathHelper.sin(-(this.rotationYaw + 90) * ((float)Math.PI / 180F)) * f) * speedMultiplier, 0.0D, (double)(MathHelper.cos((this.rotationYaw + 90) * ((float)Math.PI / 180F)) * f) * speedMultiplier));
            this.setPaddleState(this.rightInputDown && !this.leftInputDown || this.forwardInputDown, this.leftInputDown && !this.rightInputDown || this.forwardInputDown);
        }
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            float f = 0.0F;
            float f1 = (float)((this.removed ? (double)0.01F : this.getMountedYOffset()) + passenger.getYOffset());
            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(passenger);
                if (i == 0) {
                    f = 0.2F;
                } else {
                    f = -0.6F;
                }

                if (passenger instanceof AnimalEntity) {
                    f = (float)((double)f + 0.2D);
                }
            }

            Vector3d vector3d = (new Vector3d((double)f, 0.0D, 0.0D)).rotateYaw(-this.rotationYaw * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
            passenger.setPosition(this.getPosX() + vector3d.x, this.getPosY() + (double)f1, this.getPosZ() + vector3d.z);
            passenger.rotationYaw += this.deltaRotation;
            passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
            this.applyYawToEntity(passenger);
            if (passenger instanceof AnimalEntity && this.getPassengers().size() > 1) {
                int j = passenger.getEntityId() % 2 == 0 ? 90 : 270;
                passenger.setRenderYawOffset(((AnimalEntity)passenger).renderYawOffset + (float)j);
                passenger.setRotationYawHead(passenger.getRotationYawHead() + (float)j);
            }

        }
    }

    @Override
    public Vector3d getDismountPosition(LivingEntity livingEntity) {
        Vector3d vector3d = getRiderDismountPlacementOffset((double)(this.getWidth() * MathHelper.SQRT_2), (double)livingEntity.getWidth(), this.rotationYaw);
        double d0 = this.getPosX() + vector3d.x;
        double d1 = this.getPosZ() + vector3d.z;
        BlockPos blockpos = new BlockPos(d0, this.getBoundingBox().maxY, d1);
        BlockPos blockpos1 = blockpos.down();
        if (!this.world.hasWater(blockpos1)) {
            double d2 = (double)blockpos.getY() + this.world.func_242403_h(blockpos);
            double d3 = (double)blockpos.getY() + this.world.func_242403_h(blockpos1);

            for(Pose pose : livingEntity.getAvailablePoses()) {
                Vector3d vector3d1 = TransportationHelper.func_242381_a(this.world, d0, d2, d1, livingEntity, pose);
                if (vector3d1 != null) {
                    livingEntity.setPose(pose);
                    return vector3d1;
                }

                Vector3d vector3d2 = TransportationHelper.func_242381_a(this.world, d0, d3, d1, livingEntity, pose);
                if (vector3d2 != null) {
                    livingEntity.setPose(pose);
                    return vector3d2;
                }
            }
        }

        return super.getDismountPosition(livingEntity);
    }

    /**
     * Applies this boat's yaw to the given entity. Used to update the orientation of its passenger.
     */
    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setRenderYawOffset(this.rotationYaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    /**
     * Applies this entity's orientation (pitch/yaw) to another entity. Used to update passenger orientation.
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void applyOrientationToEntity(Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
//        compound.putString("Type", this.getBoatType().getName());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    protected void readAdditional(CompoundNBT compound) {
//        if (compound.contains("Type", 8)) {
//            this.setBoatType(BroomEntity.Type.getTypeFromString(compound.getString("Type")));
//        }

    }

    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        if (player.isSecondaryUseActive()) {
            return ActionResultType.PASS;
        } else if (this.outOfControlTicks < 60.0F) {
            if (!this.world.isRemote) {
                if(player.startRiding(this) ) {
                    //this.setMotion(getMotion().getX(), getMotion().getY() - 1, getMotion().getZ());
                    this.addVelocity(0,0.25,0);
                    return ActionResultType.CONSUME;
                }
                else {
                    return ActionResultType.PASS;
                }
            } else {
                return ActionResultType.SUCCESS;
            }
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        this.lastYd = this.getMotion().y;
        if (!this.isPassenger()) {
            if (onGroundIn) {
                if (this.fallDistance > 3.0F) {
                    if (this.status != BroomEntity.Status.ON_LAND) {
                        this.fallDistance = 0.0F;
                        return;
                    }

                    this.onLivingFall(this.fallDistance, 1.0F);
                    if (!this.world.isRemote && !this.removed) {
                        this.remove();
                        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            this.entityDropItem(ModItems.BROOM.get());
                        }
                    }
                }

                this.fallDistance = 0.0F;
            } else if (!this.world.getFluidState(this.getPosition().down()).isTagged(FluidTags.WATER) && y < 0.0D) {
                this.fallDistance = (float)((double)this.fallDistance - y);
            }

        }
    }

    public boolean getPaddleState(int side) {
        return this.dataManager.<Boolean>get(side == 0 ? LEFT_PADDLE : RIGHT_PADDLE) && this.getControllingPassenger() != null;
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamageTaken(float damageTaken) {
        this.dataManager.set(DAMAGE_TAKEN, damageTaken);
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamageTaken() {
        return this.dataManager.get(DAMAGE_TAKEN);
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int timeSinceHit) {
        this.dataManager.set(TIME_SINCE_HIT, timeSinceHit);
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit() {
        return this.dataManager.get(TIME_SINCE_HIT);
    }

    private void setRockingTicks(int ticks) {
        this.dataManager.set(ROCKING_TICKS, ticks);
    }

    private int getRockingTicks() {
        return this.dataManager.get(ROCKING_TICKS);
    }

    @OnlyIn(Dist.CLIENT)
    public float getRockingAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.prevRockingAngle, this.rockingAngle);
    }

    /**
     * Sets the forward direction of the entity.
     */
    public void setForwardDirection(int forwardDirection) {
        this.dataManager.set(FORWARD_DIRECTION, forwardDirection);
    }

    /**
     * Gets the forward direction of the entity.
     */
    public int getForwardDirection() {
        return this.dataManager.get(FORWARD_DIRECTION);
    }

//    public void setBoatType(BroomEntity.Type boatType) {
//        this.dataManager.set(BOAT_TYPE, boatType.ordinal());
//    }
//
//    public BroomEntity.Type getBoatType() {
//        return BroomEntity.Type.byId(this.dataManager.get(BOAT_TYPE));
//    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < 2 && !this.areEyesInFluid(FluidTags.WATER);
    }

    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    @Nullable
    @Override
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }



    @OnlyIn(Dist.CLIENT)
    public void updateInputs(boolean leftInputDown, boolean rightInputDown, boolean forwardInputDown, boolean backInputDown, boolean jumpInputDown, boolean sneakingInputDown) {
        this.leftInputDown = leftInputDown;
        this.rightInputDown = rightInputDown;
        this.forwardInputDown = forwardInputDown;
        this.backInputDown = backInputDown;
        this.jumpInputDown = jumpInputDown;
        this.sneakingInputDown = sneakingInputDown;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public boolean canSwim() {
        return this.status == BroomEntity.Status.UNDER_WATER || this.status == BroomEntity.Status.UNDER_FLOWING_WATER;
    }

    // Forge: Fix MC-119811 by instantly completing lerp on board
    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (this.canPassengerSteer() && this.lerpSteps > 0) {
            this.lerpSteps = 0;
            this.setPositionAndRotation(this.lerpX, this.lerpY, this.lerpZ, (float)this.lerpYaw, (float)this.lerpPitch);
        }
    }

    public static enum Status {
        IN_WATER,
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR;
    }

    public enum AccelerationDirection
    {
        FORWARD, NONE, REVERSE,
        CHARGING;

        public static AccelerationDirection fromEntity(LivingEntity entity)
        {
            if(entity.moveForward > 0)
            {
                return FORWARD;
            }
            else if(entity.moveForward < 0)
            {
                return REVERSE;
            }
            return NONE;
        }
    }

    public static enum Type {
        OAK(Blocks.OAK_PLANKS, "oak"),
        SPRUCE(Blocks.SPRUCE_PLANKS, "spruce"),
        BIRCH(Blocks.BIRCH_PLANKS, "birch"),
        JUNGLE(Blocks.JUNGLE_PLANKS, "jungle"),
        ACACIA(Blocks.ACACIA_PLANKS, "acacia"),
        DARK_OAK(Blocks.DARK_OAK_PLANKS, "dark_oak");

        private final String name;
        private final Block block;

        private Type(Block block, String name) {
            this.name = name;
            this.block = block;
        }

        public String getName() {
            return this.name;
        }

        public Block asPlank() {
            return this.block;
        }

        public String toString() {
            return this.name;
        }

        /**
         * Get a boat type by it's enum ordinal
         */
        public static BroomEntity.Type byId(int id) {
            BroomEntity.Type[] aboatentity$type = values();
            if (id < 0 || id >= aboatentity$type.length) {
                id = 0;
            }

            return aboatentity$type[id];
        }

        public static BroomEntity.Type getTypeFromString(String nameIn) {
            BroomEntity.Type[] aboatentity$type = values();

            for(int i = 0; i < aboatentity$type.length; ++i) {
                if (aboatentity$type[i].getName().equals(nameIn)) {
                    return aboatentity$type[i];
                }
            }

            return aboatentity$type[0];
        }
    }
}
