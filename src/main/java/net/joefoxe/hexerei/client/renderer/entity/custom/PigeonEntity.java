package net.joefoxe.hexerei.client.renderer.entity.custom;

import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.client.renderer.entity.ModEntityTypes;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.*;
import net.joefoxe.hexerei.util.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
//
//public class PigeonEntity extends AbstractPigeonEntity {
//
////    private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.BOOLEAN);
////    private static final DataParameter<Integer> ATTACK_TICK = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.VARINT);
////    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.BOOLEAN);
////    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.VARINT);
////    private static final DataParameter<Optional<BlockPos>> PERCH_POS = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
//    public int timeFlying = 0;
//    private boolean isLandNavigator;
//    protected BlockPos targetBlock;
//
//    private static final DataParameter<Optional<ITextComponent>> LAST_KNOWN_NAME = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.OPTIONAL_TEXT_COMPONENT);
//    private static final Cache<DataParameter<DimensionDependantArg<Optional<BlockPos>>>> BIRD_COFFER_LOCATION = Cache.make(() -> (DataParameter<DimensionDependantArg<Optional<BlockPos>>>) EntityDataManager.createKey(PigeonEntity.class, SerializersHexerei.COFFER_LOC_SERIALIZER.get().getSerializer()));
//
//    public static final void initDataParameters() {
//        BIRD_COFFER_LOCATION.get();
//    }
//
//    public PigeonEntity(EntityType type, World worldIn) {
//        super(type, worldIn);
//        this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
//        this.setPathPriority(PathNodeType.WATER, -1.0F);
//        this.setPathPriority(PathNodeType.WATER_BORDER, 16.0F);
//        this.setPathPriority(PathNodeType.COCOA, -1.0F);
//        this.setPathPriority(PathNodeType.FENCE, -1.0F);
//        switchNavigator(false);
//    }
//
//    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
//        return MobEntity.func_233666_p_()
//                .createMutableAttribute(Attributes.MAX_HEALTH,3.0D)
//                .createMutableAttribute(Attributes.FLYING_SPEED, 1.3D)
//                .createMutableAttribute(Attributes.MOVEMENT_SPEED,0.2D);
//
//    }
//
//    @Override
//    public void writeAdditional(CompoundNBT compound) {
//        super.writeAdditional(compound);
//        DimensionDependantArg<Optional<BlockPos>> coffersData = this.dataManager.get(BIRD_COFFER_LOCATION.get());
//
//        if (!coffersData.isEmpty()) {
//            ListNBT coffersList = new ListNBT();
//
//            for (Map.Entry<RegistryKey<World>, Optional<BlockPos>> entry : coffersData.entrySet()) {
//                CompoundNBT cofferNBT = new CompoundNBT();
//                NBTUtilities.putResourceLocation(cofferNBT, "dim", entry.getKey().getLocation());
//                NBTUtilities.putBlockPos(cofferNBT, "pos", entry.getValue());
//                coffersList.add(cofferNBT);
//            }
//
//            compound.put("coffers", coffersList);
//        }
//    }
//
//    public Optional<ITextComponent> getOwnersName() {
//        return this.dataManager.get(LAST_KNOWN_NAME);
//    }
//
//    public void setOwnersName(@Nullable ITextComponent comp) {
//        this.setOwnersName(Optional.ofNullable(comp));
//    }
//
//    public void setOwnersName(Optional<ITextComponent> name) {
//        this.dataManager.set(LAST_KNOWN_NAME, name);
//    }
//    @Override
//    public void tick() {
//        super.tick();
//
//        if (this.isAlive()) {
//            if (this.world.isRemote()) {
//                // Every 2 seconds
//                if (this.ticksExisted % 40 == 0) {
//                    PigeonLocationStorage.get(this.world).getOrCreateData(this).update(this);
//
//                    if (this.getOwner() != null) {
//                        this.setOwnersName(this.getOwner().getName());
//                    }
//                }
//            }
//        }
////        if (!world.isRemote) {
////            if (isFlying() && this.isLandNavigator) {
////                switchNavigator(false);
////            }
////            if (!isFlying() && !this.isLandNavigator) {
////                switchNavigator(true);
////            }
////            if (isFlying()) {
////                timeFlying++;
////
////                //if(timeFlying > 20 && this.getMotion().getY() > 0)
////                this.setNoGravity(true);
//////                System.out.println("Flying");
////
////            } else {
////                timeFlying = 0;
////                this.setNoGravity(false);
//////                System.out.println("Sitting");
////            }
////        }
//    }
//
////    @Override
////    public AgeableEntity createChild(ServerWorld worldIn, AgeableEntity partner) {
////        PigeonEntity child = ModEntityTypes.PIGEON.get().create(worldIn);
////        UUID uuid = this.getOwnerId();
////
////        if (uuid != null) {
////            child.setOwnerId(uuid);
////            child.setTamed(true);
////        }
////        return child;
////    }
//
//
//
//
//
//    @Override public void onRemovedFromWorld() {super.onRemovedFromWorld();}
//
//    @Override
//    public void onDeath(DamageSource cause) {
//        super.onDeath(cause);
//        // Save inventory after onDeath is called so that (if you implement an inventory)
//        // the inventory can be dropped and not saved
//        if (this.world != null && this.world.isRemote()) {
//            PigeonLocationStorage.get(this.world).remove(this);
//        }
//    }
//
//    @Override
//    public Entity changeDimension(ServerWorld server, ITeleporter teleporter) {
//        Entity transportedEntity = super.changeDimension(server, teleporter);
//        if (transportedEntity instanceof PigeonEntity) {
//            PigeonLocationStorage.get(this.world).getOrCreateData(this).update((PigeonEntity) transportedEntity);
//        }
//        return transportedEntity;
//    }
//
//    @Override
//    public void setUniqueId(UUID uniqueIdIn) {
//        // If the UUID is changed remove old one and add new one
//        UUID oldUniqueId = this.getUniqueID();
//        if (uniqueIdIn.equals(oldUniqueId)) {
//            return; // No change do nothing
//        }
//        super.setUniqueId(uniqueIdIn);
//        if (this.world != null && this.world.isRemote()) {
//            PigeonLocationStorage.get(this.world).remove(oldUniqueId);
//            PigeonLocationStorage.get(this.world).getOrCreateData(this).update(this);
//        }
//    }
//
//    @Override
//    public void setTamedBy(PlayerEntity player) {
//        super.setTamedBy(player);
//        // When tamed by player this caches their display name
//        this.setOwnersName(player.getName());
//    }
//
//    @Override
//    public void setTamed(boolean tamed) {
//        super.setTamed(tamed);
//    }
//
//    @Override
//    public void setOwnerId(@Nullable UUID uuid) {
//        super.setOwnerId(uuid);
//        if (uuid == null) {
//            this.setOwnersName((ITextComponent) null);
//        }
//    }
//
//    @Override
//    protected void registerData() {
//        super.registerData();
//        this.dataManager.register(BIRD_COFFER_LOCATION.get(), new DimensionDependantArg<>(() -> DataSerializers.OPTIONAL_BLOCK_POS));
//    }
//
//    public boolean isPathFinding() {
//        return !this.getNavigator().noPath();
//    }
//
//
//    public boolean canTrample(BlockState state, BlockPos pos, float fallDistance) {
//        return false;
//    }
//
//    public boolean onLivingFall(float distance, float damageMultiplier) {
//        return false;
//    }
//    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
//    }
//
//    public void setTargetBlock(BlockPos pos) {this.targetBlock = pos;}
//    public BlockPos getTargetBlock() {return this.targetBlock;}
//
//    @Override
//    public boolean isInvulnerableTo(DamageSource source) {
//        return source == DamageSource.IN_WALL || source == DamageSource.FALLING_BLOCK || source == DamageSource.CACTUS || super.isInvulnerableTo(source);
//    }
//
//    @Nullable
//    @Override
//    public AgeableEntity createChild(ServerWorld serverWorld, AgeableEntity ageableEntity) {
//        return ModEntityTypes.PIGEON.get().create(serverWorld);
//    }
//
//
//    @Override
//    protected void registerGoals() {
//        super.registerGoals();
//        this.goalSelector.addGoal(0, new SwimGoal(this));
//        this.goalSelector.addGoal(1,new PanicGoal(this,1.25D));
//        this.goalSelector.addGoal(2,new WaterAvoidingRandomWalkingGoal(this,1.0D));
//        this.goalSelector.addGoal(3,new LookAtGoal(this, PlayerEntity.class, 6.0F));
//        this.goalSelector.addGoal(7,new LookRandomlyGoal(this));
//        this.goalSelector.addGoal(7, new PigeonEntity.AIWalkIdle());
//    }
//
//    private void switchNavigator(boolean onLand) {
//        if (onLand) {
//            this.moveController = new FlightMoveController(this, 0.7F, false);
//            this.navigator = new GroundPathNavigator(this, world);
//            this.isLandNavigator = true;
//            this.setAIMoveSpeed(0.01f);
//        } else {
//            this.moveController = new FlightMoveController(this, 0.7F, false);
//            this.setMotion(this.getMotion().getX(), this.getMotion().getY(), this.getMotion().getZ());
//            this.navigator = new DirectPathNavigator(this, world);
//            this.isLandNavigator = false;
//        }
//    }
//
//    @Override
//    protected int getExperiencePoints(PlayerEntity player)
//    {
//        return 1 + this.world.rand.nextInt(4);
//    }
//
//    private boolean isOverWater() {
//        BlockPos position = this.getPosition();
//        while (position.getY() > 2 && world.isAirBlock(position)) {
//            position = position.down();
//        }
//        return !world.getFluidState(position).isEmpty();
//    }
//
//    @Override
//    public SoundEvent getAmbientSound() {
//        this.playSound(SoundEvents.ENTITY_CHICKEN_AMBIENT, 0.2F, 1.0F);
//        return null;
//    }
//
//    @Override
//    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
//        this.playSound(SoundEvents.ENTITY_RABBIT_HURT, 1.0F, 1.7F);
//        return null;
//    }
//
//    @Override
//    protected SoundEvent getDeathSound() {
//        this.playSound(SoundEvents.ENTITY_PARROT_DEATH, 0.7F, 2.0F);
//        return null;
//    }
//
//
//    private class AIWalkIdle extends Goal {
//        protected final PigeonEntity crow;
//        protected double x;
//        protected double y;
//        protected double z;
//        private boolean flightTarget = false;
//
//        public AIWalkIdle() {
//            super();
//            this.setMutexFlags(EnumSet.of(Flag.MOVE));
//            this.crow = PigeonEntity.this;
//        }
//
//        @Override
//        public boolean shouldExecute() {
//
//            if (this.crow.getRNG().nextInt(30) != 0 && !crow.isLandNavigator) {
//                return false;
//            }
//            if (this.crow.isOnGround()) {
//                this.flightTarget = rand.nextBoolean();
//            } else {
//                this.flightTarget = rand.nextInt(5) > 0 && crow.timeFlying < 200;
//            }
//            Vector3d lvt_1_1_ = this.getPosition();
//            if (lvt_1_1_ == null) {
//                return false;
//            } else {
//                this.x = lvt_1_1_.x;
//                this.y = lvt_1_1_.y;
//                this.z = lvt_1_1_.z;
//                return true;
//            }
//
//        }
//
//        public void tick() {
//            if (flightTarget) {
//                crow.getMoveHelper().setMoveTo(x, y, z, 1F);
//            } else {
//                this.crow.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, 1F);
//            }
////            if (!flightTarget && isFlying() && crow.onGround) {
////                crow.switchNavigator(false);
////            }
////            if (isFlying() && crow.onGround && crow.timeFlying > 10) {
////                crow.switchNavigator(true);
////            }
//        }
//
//        @Nullable
//        protected Vector3d getPosition() {
//            Vector3d vector3d = crow.getPositionVec();
//
//            if(crow.isOverWater()){
//                flightTarget = true;
//            }
//            if (flightTarget) {
//                return RandomPositionGenerator.findRandomTarget(this.crow, 10, 7);
////                if (crow.timeFlying < 50 || crow.isOverWater()) {
////                    return crow.getBlockInViewAway(vector3d, 0);
////                } else {
////                    return crow.getBlockGrounding(vector3d);
////                }
//            } else {
//
//                return RandomPositionGenerator.findRandomTarget(this.crow, 10, 7);
//            }
//        }
//
//        public boolean shouldContinueExecuting() {
//
////            if (flightTarget) {
////                return crow.isFlying() && crow.getDistanceSq(x, y, z) > 2F;
////            } else {
//                return (!this.crow.getNavigator().noPath()) && !this.crow.isBeingRidden();
////            }
//        }
//
//        public void startExecuting() {
//            if (flightTarget) {
////                crow.setFlying(true);
//                crow.getMoveHelper().setMoveTo(x, y, z, 1F);
//            } else {
//                this.crow.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, 1F);
//            }
//        }
//
//        public void resetTask() {
//            this.crow.getNavigator().clearPath();
//            super.resetTask();
//        }
//    }
//
//
//
//    @Override
//    public Vector3d getLeashStartPosition() {
//        return new Vector3d(5.0D, (5.5F * this.getEyeHeight()), (this.getWidth() * 5.4F));
//    }
//}



import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PigeonEntity extends AbstractPigeonEntity {

    private static final DataParameter<Optional<ITextComponent>> LAST_KNOWN_NAME = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.OPTIONAL_TEXT_COMPONENT);
    private static final DataParameter<Byte> PIGEON_FLAGS = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Float> HUNGER_INT = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.FLOAT);
    //    private static final DataParameter<Byte> SIZE = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.BYTE);
    private static final DataParameter<ItemStack> BONE_VARIANT = EntityDataManager.createKey(PigeonEntity.class, DataSerializers.ITEMSTACK);
    // Use Cache.make to ensure static fields are not initialised too early (before Serializers have been registered)
    private static final Cache<DataParameter<List<SkillInstance>>> SKILLS = Cache.make(() -> (DataParameter<List<SkillInstance>>) EntityDataManager.createKey(PigeonEntity.class, SerializersHexerei.SKILL_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<PigeonLevel>> PIGEON_LEVEL = Cache.make(() -> (DataParameter<PigeonLevel>) EntityDataManager.createKey(PigeonEntity.class, SerializersHexerei.PIGEON_LEVEL_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<EnumMode>> MODE = Cache.make(() -> (DataParameter<EnumMode>) EntityDataManager.createKey(PigeonEntity.class, SerializersHexerei.MODE_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<DimensionDependantArg<Optional<BlockPos>>>> BIRD_COFFER_LOCATION = Cache.make(() -> (DataParameter<DimensionDependantArg<Optional<BlockPos>>>) EntityDataManager.createKey(PigeonEntity.class, SerializersHexerei.COFFER_LOC_SERIALIZER.get().getSerializer()));

    public static final void initDataParameters() {
        SKILLS.get();
        PIGEON_LEVEL.get();
        MODE.get();
        BIRD_COFFER_LOCATION.get();
    }

    // Cached values
    private final Cache<Integer> spendablePoints = Cache.make(this::getSpendablePointsInternal);
    private final List<IBirdAlteration> alterations = new ArrayList<>(4);
    private final List<IPigeonFoodHandler> foodHandlers = new ArrayList<>(4);
    public final Map<Integer, Object> objects = new HashMap<>();
    public final StatsTracker statsTracker = new StatsTracker();
    private int hungerTick;
    private int prevHungerTick;
    private int healingTick;
    private int prevHealingTick;

    protected BlockPos targetBlock;

    public PigeonEntity(EntityType<? extends PigeonEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }


    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SKILLS.get(), new ArrayList<>(4));
        this.dataManager.register(LAST_KNOWN_NAME, Optional.empty());
        this.dataManager.register(PIGEON_FLAGS, (byte) 0);
        this.dataManager.register(MODE.get(), EnumMode.DOCILE);
        this.dataManager.register(HUNGER_INT, 60F);
        this.dataManager.register(PIGEON_LEVEL.get(), new PigeonLevel(0));
//        this.dataManager.register(BONE_VARIANT, ItemStack.EMPTY);
        this.dataManager.register(BIRD_COFFER_LOCATION.get(), new DimensionDependantArg<>(() -> DataSerializers.OPTIONAL_BLOCK_POS));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(1, new FindWaterGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new MoveToSpecificBlockGoal(this));
        this.goalSelector.addGoal(5, new PigeonWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new PigeonFollowMasterGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new MasterHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new MasterHurtTargetGoal(this));
//        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeletonEntity.class, false));
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15F, 1.0F);
    }

//    @Override
//    protected SoundEvent getAmbientSound() {
//
//        if (this.rand.nextInt(3) == 0) {
//            return this.isTamed() && this.getHealth() < 10.0F ? SoundEvents.WOLF_WHINE : SoundEvents.WOLF_PANT;
//        } else {
//            return RigoranthusSoundRegistry.PIGEON_AMBIENT.get();
//        }
//    }

    @Override protected SoundEvent getDeathSound() {return SoundEvents.ENTITY_PARROT_DEATH;}
    @Override protected SoundEvent getHurtSound(DamageSource damageSourceIn) {return SoundEvents.ENTITY_PARROT_HURT;}
    protected void playEatingSound() {this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);}

    @Override
    public float getSoundVolume() {
        return 0.4F;
    }

//    @Override
//    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
//        return sizeIn.height * 0.8F;
//    }



//    @Override
//    public Vector3d getLeashOffset() {
//        return new Vector3d(0.0D, 0.6F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
//    }

    @Override
    public void tick() {
        super.tick();

        if (this.isAlive()) {

            boolean inWater = this.isInWater();
            // If inWater is false then isInRain is true in the or statement
            // On server side
            if (!this.world.isRemote) {
                // Every 2 seconds
                if (this.ticksExisted % 40 == 0) {
                    PigeonLocationStorage.get(this.world).getOrCreateData(this).update(this);

                    if (this.getOwner() != null) {
                        this.setOwnersName(this.getOwner().getName());
                    }
                }
            }
        }
        this.alterations.forEach((alter) -> alter.tick(this));
    }



    @Override
    public void livingTick() {
        super.livingTick();

        if (!this.world.isRemote) {

            this.prevHealingTick = this.healingTick;
            this.healingTick += 8;


            for (IBirdAlteration alter : this.alterations) {
                ActionResult<Integer> result = alter.healingTick(this, this.healingTick - this.prevHealingTick);
                if (result.getType().isSuccess()) {
                    this.healingTick = result.getResult() + this.prevHealingTick;
                }
            }

            if (this.healingTick >= 6000) {
                if (this.getHealth() < this.getMaxHealth()) {
                    this.heal(1);
                }
                this.healingTick = 0;
            }
        }
        this.alterations.forEach((alter) -> alter.livingTick(this));
    }



    @Override
    public ActionResultType getEntityInteractionResult(PlayerEntity player, Hand hand) {

        ItemStack stack = player.getHeldItem(hand);

//        if (this.isTamed()) {
//            if (stack.getItem() == ItemInit.PACT_OF_SERVITUDE.get() && this.canInteract(player)) {
//                if (this.world.isRemote) {
//                    PigeonInfoScreen.open(this);
//                }
//                return ActionResultType.SUCCESS;
//            }
//        }


        /////       NOT NEEDED SINCE A TAME PIGEON CANNOT SPAWN ON ITS OWN      /////
//        else { // Not tamed
////            if (stack.getItem() == ItemInit.PACT_OF_SERVITUDE.get()) {
//                if (!this.world.isRemote) {
////                    this.usePlayerItem(player, stack);
////                    if (stack.getItem() == ItemInit.PACT_OF_SERVITUDE.get() || this.rand.nextInt(14) == 0) {
//                        this.tame(player);
//                        this.navigation.stop();
//                        this.setTarget((LivingEntity) null);
//                        this.setOrderedToSit(true);
//                        this.setHealth(80.0F);
//                        this.world.broadcastEntityEvent(this, HexereiConstants.EntityState.PIGEON_HEARTS);
////                    } else {
////                        this.world.broadcastEntityEvent(this, HexereiConstants.EntityState.PIGEON_SMOKE);
////                    }
//                }
//                return ActionResultType.SUCCESS;
////            }
//        }

        Optional<IPigeonFoodHandler> foodHandler = FoodHandler.getMatch(this, stack, player);
        if (foodHandler.isPresent()) {
            this.playEatingSound();
            return foodHandler.get().consume(this, stack, player);

        }
        ActionResultType interactResult = InteractionHandler.getMatch(this, stack, player, hand, world);
        if (interactResult != ActionResultType.PASS) {return interactResult;}

        for (IBirdAlteration alter : this.alterations) {
            ActionResultType result = alter.processInteract(this, this.world, player, hand);
            if (result != ActionResultType.PASS) {
                return result;
            }
        }
        ActionResultType actionresulttype = super.getEntityInteractionResult(player, hand);
        if ((!actionresulttype.isSuccessOrConsume() || this.isChild()) && this.canInteract(player)) {
//            this.setOrderedToSit(!this.isOrderedToSit());
            this.getNavigator().clearPath();
            this.setTargetBlock(null);
            return ActionResultType.SUCCESS;
        }
        return actionresulttype;
    }


    @Override
    public boolean canTrample(BlockState state, BlockPos pos, float fallDistance) {

        return false;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.isMode(EnumMode.DOCILE)) {
            return false;
        }
        for (IBirdAlteration alter : this.alterations) {
            ActionResultType result = alter.canAttack(this, target);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }
        // Stops Cani being able to attack creepers. If the pigeon has lvl 5 powderkeg then we will return true in the for loop above.
        if (target instanceof CreeperEntity) {
            return false;
        }
        return super.canAttack(target);
    }


    @Override
    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        if (this.isMode(EnumMode.DOCILE)) {
            return false;
        }

        //TODO make wolves not able to attack cani
        for (IBirdAlteration alter : this.alterations) {
            ActionResultType result = alter.shouldAttackEntity(this, target, owner);
            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }
        if (target instanceof WolfEntity) {
            WolfEntity wolfentity = (WolfEntity)target;
            return !wolfentity.isTamed() || wolfentity.getOwner() != owner;
        } else if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).canAttackPlayer((PlayerEntity)target)) {
            return false;
        } else if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTame()) {
            return false;
        } else {
            return !(target instanceof TameableEntity) || !((TameableEntity)target).isTamed();
        }
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        for (IBirdAlteration alter : this.alterations) {
            ActionResult<Float> result = alter.attackEntityFrom(this, source, amount);

            // TODO
            if (result.getType() == ActionResultType.FAIL) {
                return false;
            } else {
                amount = result.getResult();
            }
        }
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getImmediateSource();
            // Must be checked here too as hitByEntity only applies to when the Pigeon is
            // directly hit not indirect damage like sweeping effect etc
            if (entity instanceof PlayerEntity && !this.canPlayersAttack()) {
                return false;
            }
//            this.setOrderedToSit(false);
            if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof AbstractArrowEntity)) {
                amount = (amount + 1.0F) / 2.0F;
            }
            return super.attackEntityFrom(source, amount);
        }
    }


    @Override
    public boolean attackEntityAsMob(Entity target) {
        for (IBirdAlteration alter : this.alterations) {
            ActionResultType result = alter.attackEntityAsMob(this, target);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }
        ModifiableAttributeInstance attackDamageInst = this.getAttribute(Attributes.ATTACK_DAMAGE);
        Set<AttributeModifier> critModifiers = null;
        if (this.getAttribute(PigeonAttributes.CRIT_CHANCE.get()).getValue() > this.rand.nextDouble()) {
            critModifiers = this.getAttribute(PigeonAttributes.CRIT_BONUS.get()).getModifierListCopy();
            critModifiers.forEach(attackDamageInst::applyNonPersistentModifier);
        }
        int damage = ((int) attackDamageInst.getValue());
        if (critModifiers != null) {
            critModifiers.forEach(attackDamageInst::removeModifier);
        }
        boolean flag = target.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
        if (flag) {
            this.applyEnchantments(this, target);
            this.statsTracker.increaseDamageDealt(damage);

            if (critModifiers != null) {
                // TODO Might want to make into a packet
                DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().particles.addParticleEmitter(target, ParticleTypes.CRIT));
            }
        }
        return flag;
    }

    @Override
    public void awardKillScore(Entity killed, int scoreValue, DamageSource damageSource) {
        super.awardKillScore(killed, scoreValue, damageSource);
        this.statsTracker.incrementKillCount(killed);
    }

    @Override
    public boolean canBlockDamageSource(DamageSource source) {
        for (IBirdAlteration alter : this.alterations) {
            ActionResultType result = alter.canBlockDamageSource(this, source);
            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }
        return super.canBlockDamageSource(source);
    }

    @Override
    public void setFire(int second) {
        for (IBirdAlteration alter : this.alterations) {
            ActionResult<Integer> result = alter.setFire(this, second);
            if (result.getType().isSuccess()) {
                second = result.getResult();
            }
        }
        super.setFire(second);
    }

    @Override
    public boolean isImmuneToFire() {
        for (IBirdAlteration alter : this.alterations) {
            ActionResultType result = alter.isImmuneToFire(this);
            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }
        return super.isImmuneToFire();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        for (IBirdAlteration alter : this.alterations) {
            ActionResultType result = alter.isInvulnerableTo(this, source);
            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean isInvulnerable() {
        for (IBirdAlteration alter : this.alterations) {
            ActionResultType result = alter.isInvulnerable(this);
            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }
        return super.isInvulnerable();
    }

    @Override
    public boolean canBeHitWithPotion() {
        return super.canBeHitWithPotion();
    }

    @Override
    public boolean isPotionApplicable(EffectInstance effectIn) {
        for (IBirdAlteration alter : this.alterations) {
            ActionResultType result = alter.isPotionApplicable(this, effectIn);
            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }
        return super.isPotionApplicable(effectIn);
    }

    @Override
    public void setUniqueId(UUID uniqueIdIn) {
        // If the UUID is changed remove old one and add new one
        UUID oldUniqueId = this.getUniqueID();
        if (uniqueIdIn.equals(oldUniqueId)) {
            return; // No change do nothing
        }
        super.setUniqueId(uniqueIdIn);
        if (this.world != null && !this.world.isRemote) {
            PigeonLocationStorage.get(this.world).remove(oldUniqueId);
            PigeonLocationStorage.get(this.world).getOrCreateData(this).update(this);
        }
    }



    @Override
    public void setTamedBy(PlayerEntity player) {
        super.setTamedBy(player);
        // When tamed by player this caches their display name
        this.setOwnersName(player.getName());
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(80.0D); // was 20
            this.setHealth(80.0F); //was 20
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(80.0D); // was 8
        }
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0D); // was 4
    }

    @Override
    public void setOwnerId(@Nullable UUID uuid) {
        super.setOwnerId(uuid);
        if (uuid == null) {
            this.setOwnersName((ITextComponent) null);
        }
    }



//    @Override // blockAttackFromPlayer
//    public boolean skipAttackInteraction(Entity entityIn) {
//        if (entityIn instanceof PlayerEntity && !this.canPlayersAttack()) {
//            return true;
//        }
//        for (IBirdAlteration alter : this.alterations) {
//            ActionResultType result = alter.hitByEntity(this, entityIn);
//            if (result.isSuccess()) {
//                return true;
//            } else if (result == ActionResultType.FAIL) {
//                return false;
//            }
//        }
//        return false;
//    }

//    @Override
//    public ItemStack getPickedResult(RayTraceResult target) {
//        return new ItemStack(PigeonItems.PIGEON_SUMMONING_CHARM.get());
//    }

    @Override
    public AgeableEntity createChild(ServerWorld worldIn, AgeableEntity partner) {
        PigeonEntity child = ModEntityTypes.PIGEON.get().create(worldIn);
        UUID uuid = this.getOwner().getUniqueID();

        if (uuid != null) {
            child.setOwnerId(uuid);
            child.setTamed(true);
        }
        if (partner instanceof PigeonEntity/* && Config.PIGEON_PUPS_GET_PARENT_LEVELS.get()*/) {
            child.setLevel(this.getLevel().combine(((PigeonEntity) partner).getLevel()));
        }
        return child;
    }

    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return (/*ConfigValues.ALWAYS_SHOW_PIGEON_NAME && */this.hasCustomName()) || super.getAlwaysRenderNameTagForRender();
    }


//    @Override
//    public float getScale() {
//        if (this.isBaby()) {
//            return 0.5F;
//        } else {
//            return this.getPigeonSize() * 0.3F + 0.1F;
//        }
//    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        // Any mod that tries to access capabilities from entity size/entity
        // creation event will crash here because of the order java inits the
        // classes fields and so will not have been initialised and are
        // accessed during the classes super() call.
        // Since this.alterations will be empty anyway as we have not read
        // NBT data at this point just avoid silent error
        if (this.alterations == null) {
            return super.getCapability(cap, side);
        }
        for (IBirdAlteration alter : this.alterations) {
            LazyOptional<T> result = alter.getCapability(this, cap, side);
            if (result != null) {
                return result;
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public Entity changeDimension(ServerWorld worldIn, ITeleporter teleporter) {
        Entity transportedEntity = super.changeDimension(worldIn, teleporter);
        if (transportedEntity instanceof PigeonEntity) {
            PigeonLocationStorage.get(this.world).getOrCreateData(this).update((PigeonEntity) transportedEntity);
        }
        return transportedEntity;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld(); // When the entity is added to tracking list
        if (this.world != null && !this.world.isRemote) {
            //PigeonLocationData locationData = PigeonLocationStorage.get(this.world).getOrCreateData(this);
            //locationData.update(this);
        }
    }
    @Override public void onRemovedFromWorld() {super.onRemovedFromWorld();}

    /**
     * When the entity is brought back to life
     */
    @Override public void revive() {super.revive();}

    @Override
    protected void onDeathUpdate() {
        if (this.deathTime == 19) { // 1 second after death
            if (this.world != null && !this.world.isRemote) {
//                PigeonReviveStorage.get(this.world).putData(this);
//                Hexerei.LOGGER.debug("Saved pigeon as they died {}", this);
//
//                PigeonLocationStorage.get(this.world).remove(this);
//                Hexerei.LOGGER.debug("Removed pigeon location as they were removed from the world {}", this);
            }
        }

        super.onDeathUpdate();
    }

    @Override
    public void onDeath(DamageSource cause) {

        this.alterations.forEach((alter) -> alter.onDeath(this, cause));
        super.onDeath(cause);

        // Save inventory after onDeath is called so that pack puppy inventory
        // can be dropped and not saved
        if (this.world != null && !this.world.isRemote) {
            PigeonReviveStorage.get(this.world).putData(this);
            PigeonLocationStorage.get(this.world).remove(this);
        }
    }


//    @Override
//    public void dropEquipment() {
//        super.dropEquipment();
//
//        this.alterations.forEach((alter) -> alter.dropInventory(this));
//    }

    /**
     * When the entity is removed
     */
    @Override
    public void remove(boolean keepData) {
        super.remove(keepData);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        this.alterations.forEach((alter) -> alter.invalidateCapabilities(this));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        ListNBT skillList = new ListNBT();
        List<SkillInstance> skills = this.getSkillMap();

        for (int i = 0; i < skills.size(); i++) {
            CompoundNBT skillTag = new CompoundNBT();
            skills.get(i).writeInstance(this, skillTag);
            skillList.add(skillTag);
        }

        compound.put("skills", skillList);


        compound.putString("mode", this.getMode().getSaveName());
        this.getOwnersName().ifPresent((comp) -> {
            NBTUtilities.putTextComponent(compound, "lastKnownOwnerName", comp);
        });

//        compound.putString("customSkinHash", this.getSkinHash());
        compound.putBoolean("willObey", this.willObeyOthers());
        compound.putBoolean("friendlyFire", this.canPlayersAttack());
//        compound.putInt("pigeonSize", this.getPigeonSize());
        compound.putInt("level_normal", this.getLevel().getLevel(PigeonLevel.Type.NORMAL));
        NBTUtilities.writeItemStack(compound, "fetchItem", this.getBoneVariant());

        DimensionDependantArg<Optional<BlockPos>> cofferData = this.dataManager.get(BIRD_COFFER_LOCATION.get());

        if (!cofferData.isEmpty()) {
            ListNBT bedsList = new ListNBT();

            for (Map.Entry<RegistryKey<World>, Optional<BlockPos>> entry : cofferData.entrySet()) {
                CompoundNBT bedNBT = new CompoundNBT();
                NBTUtilities.putResourceLocation(bedNBT, "dim", entry.getKey().getLocation());
                NBTUtilities.putBlockPos(bedNBT, "pos", entry.getValue());
                bedsList.add(bedNBT);
            }

            compound.put("coffers", bedsList);
        }

//        DimensionDependantArg<Optional<BlockPos>> bowlsData = this.dataManager.get(PIGEON_BOWL_LOCATION.get());
//
//        if (!bowlsData.isEmpty()) {
//            ListNBT bowlsList = new ListNBT();
//
//            for (Map.Entry<RegistryKey<World>, Optional<BlockPos>> entry : bowlsData.entrySet()) {
//                CompoundNBT bowlsNBT = new CompoundNBT();
//                NBTUtilities.putResourceLocation(bowlsNBT, "dim", entry.getKey().location());
//                NBTUtilities.putBlockPos(bowlsNBT, "pos", entry.getValue());
//                bowlsList.add(bowlsNBT);
//            }
//
//            compound.put("bowls", bowlsList);
//        }

        this.statsTracker.writeAdditional(compound);

        this.alterations.forEach((alter) -> alter.onWrite(this, compound));
    }



    @Override
    public void read(CompoundNBT compound) {

        // DataFix uuid entries and attribute ids
        try {
            if (NBTUtilities.hasOldUniqueId(compound, "UUID")) {
                UUID entityUUID = NBTUtilities.getOldUniqueId(compound, "UUID");

                compound.putUniqueId("UUID", entityUUID);
                NBTUtilities.removeOldUniqueId(compound, "UUID");
            }

            if (compound.contains("OwnerUUID", Constants.NBT.TAG_STRING)) {
                UUID ownerUUID = UUID.fromString(compound.getString("OwnerUUID"));

                compound.putUniqueId("Owner", ownerUUID);
                compound.remove("OwnerUUID");
            } else if (compound.contains("Owner", Constants.NBT.TAG_STRING)) {
                UUID ownerUUID = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), compound.getString("Owner"));

                compound.putUniqueId("Owner", ownerUUID);
            }

            if (NBTUtilities.hasOldUniqueId(compound, "LoveCause")) {
                UUID entityUUID = NBTUtilities.getOldUniqueId(compound, "LoveCause");

                compound.putUniqueId("LoveCause", entityUUID);
                NBTUtilities.removeOldUniqueId(compound, "LoveCause");
            }
        } catch (Exception e) {
            Hexerei.LOGGER.error("Failed to data fix UUIDs: " + e.getMessage());
        }

        try {
            if (compound.contains("Attributes", Constants.NBT.TAG_LIST)) {
                ListNBT attributeList = compound.getList("Attributes", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < attributeList.size(); i++) {
                    CompoundNBT attributeData = attributeList.getCompound(i);
                    String namePrev = attributeData.getString("Name");
                    Object name = namePrev;

                    switch (namePrev) {
                        case "forge.swimSpeed": name = ForgeMod.SWIM_SPEED; break;
                        case "forge.nameTagDistance": name = ForgeMod.NAMETAG_DISTANCE; break;
                        case "forge.entity_gravity": name = ForgeMod.ENTITY_GRAVITY; break;
                        case "forge.reachDistance": name = ForgeMod.REACH_DISTANCE; break;
                        case "generic.maxHealth": name = Attributes.MAX_HEALTH; break;
                        case "generic.knockbackResistance": name = Attributes.KNOCKBACK_RESISTANCE; break;
                        case "generic.movementSpeed": name = Attributes.MOVEMENT_SPEED; break;
                        case "generic.armor": name = Attributes.ARMOR; break;
                        case "generic.armorToughness": name = Attributes.ARMOR_TOUGHNESS; break;
                        case "generic.followRange": name = Attributes.FOLLOW_RANGE; break;
                        case "generic.attackKnockback": name = Attributes.ATTACK_KNOCKBACK; break;
                        case "generic.attackDamage": name = Attributes.ATTACK_DAMAGE; break;
//                        case "generic.jumpStrength": name = PigeonAttributes.JUMP_POWER; break;
                        case "generic.critChance": name = PigeonAttributes.CRIT_CHANCE; break;
                        case "generic.critBonus": name = PigeonAttributes.CRIT_BONUS; break;
                    }

                    ResourceLocation attributeRL = HexereiUtil.getRegistryId(name);

                    if (attributeRL != null && ForgeRegistries.ATTRIBUTES.containsKey(attributeRL)) {
                        attributeData.putString("Name", attributeRL.toString());
                        ListNBT modifierList = attributeData.getList("Modifiers", Constants.NBT.TAG_COMPOUND);
                        for (int j = 0; j < modifierList.size(); j++) {
                            CompoundNBT modifierData = modifierList.getCompound(j);
                            if (NBTUtilities.hasOldUniqueId(modifierData, "UUID")) {
                                UUID entityUUID = NBTUtilities.getOldUniqueId(modifierData, "UUID");

                                modifierData.putUniqueId("UUID", entityUUID);
                                NBTUtilities.removeOldUniqueId(modifierData, "UUID");
                            }
                        }
                    } else {Hexerei.LOGGER.warn("Failed to data fix '" + namePrev + "'");}
                }
            }
        } catch (Exception e) {Hexerei.LOGGER.error("Failed to data fix attribute IDs: " + e.getMessage());}
        super.read(compound);
    }



    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);

        List<SkillInstance> skillMap = this.getSkillMap();
        skillMap.clear();

        if (compound.contains("skills", Constants.NBT.TAG_LIST)) {
            ListNBT skillList = compound.getList("skills", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < skillList.size(); i++) {
                // Add directly so that nothing is lost, if number allowed on changes
                SkillInstance.readInstance(this, skillList.getCompound(i)).ifPresent(skillMap::add);
            }
        }

        this.markDataParameterDirty(SKILLS.get(), false); // Mark dirty so data is synced to client

        // Does what notifyDataManagerChange would have done but this way only does it once
        this.recalculateAlterationsCache();
        this.spendablePoints.markForRefresh();

        try {
            for (IBirdAlteration inst : this.alterations) {
                inst.init(this);
            }
        } catch (Exception e) {
            Hexerei.LOGGER.error("Failed to init alteration: " + e.getMessage());
            e.printStackTrace();
        }

        try {
//            this.setGender(EnumGender.bySaveName(compound.getString("pigeonGender")));

            if (compound.contains("mode", Constants.NBT.TAG_STRING)) {
                this.setMode(EnumMode.bySaveName(compound.getString("mode")));
            }

//            if (compound.contains("customSkinHash", Constants.NBT.TAG_STRING)) {
//                this.setSkinHash(compound.getString("customSkinHash"));
//            } else {
//                BackwardsCompat.readPigeonTexture(compound, this::setSkinHash);
//            }

            if (compound.contains("fetchItem", Constants.NBT.TAG_COMPOUND)) {
                this.setBoneVariant(NBTUtilities.readItemStack(compound, "fetchItem"));
            }
//            else {
//                BackwardsCompat.readHasBone(compound, this::setBoneVariant);
//            }

            this.setOwnersName(NBTUtilities.getTextComponent(compound, "lastKnownOwnerName"));
            this.setWillObeyOthers(compound.getBoolean("willObey"));
            this.setCanPlayersAttack(compound.getBoolean("friendlyFire"));
//            if (compound.contains("pigeonSize", Constants.NBT.TAG_ANY_NUMERIC)) {
//                this.setPigeonSize(compound.getInt("pigeonSize"));
//            }
        } catch (Exception e) {
            Hexerei.LOGGER.error("Failed to load levels: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            if (compound.contains("level_normal", Constants.NBT.TAG_ANY_NUMERIC)) {
                this.getLevel().setLevel(compound.getInt("level_normal"));
                this.markDataParameterDirty(PIGEON_LEVEL.get());
            }

        } catch (Exception e) {
            Hexerei.LOGGER.error("Failed to load levels: " + e.getMessage());
            e.printStackTrace();
        }

        DimensionDependantArg<Optional<BlockPos>> cofferData = this.dataManager.get(BIRD_COFFER_LOCATION.get()).copyEmpty();

        try {
            if (compound.contains("coffers", Constants.NBT.TAG_LIST)) {
                ListNBT bedsList = compound.getList("coffers", Constants.NBT.TAG_COMPOUND);

                for (int i = 0; i < bedsList.size(); i++) {
                    CompoundNBT bedNBT = bedsList.getCompound(i);
                    ResourceLocation loc = NBTUtilities.getResourceLocation(bedNBT, "dim");
                    RegistryKey<World> type = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, loc);
                    Optional<BlockPos> pos = NBTUtilities.getBlockPos(bedNBT, "pos");
                    cofferData.put(type, pos);
                }
            }
        } catch (Exception e) {
            Hexerei.LOGGER.error("Failed to load coffers: " + e.getMessage());
            e.printStackTrace();
        }

        this.dataManager.set(BIRD_COFFER_LOCATION.get(), cofferData);

//        DimensionDependantArg<Optional<BlockPos>> bowlsData = this.dataManager.get(PIGEON_BOWL_LOCATION.get()).copyEmpty();
//
//        try {
//            if (compound.contains("bowls", Constants.NBT.TAG_LIST)) {
//                ListNBT bowlsList = compound.getList("bowls", Constants.NBT.TAG_COMPOUND);
//
//                for (int i = 0; i < bowlsList.size(); i++) {
//                    CompoundNBT bowlsNBT = bowlsList.getCompound(i);
//                    ResourceLocation loc = NBTUtilities.getResourceLocation(bowlsNBT, "dim");
//                    RegistryKey<World> type = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, loc);
//                    Optional<BlockPos> pos = NBTUtilities.getBlockPos(bowlsNBT, "pos");
//                    bowlsData.put(type, pos);
//                }
//            }
//        } catch (Exception e) {
//            Hexerei.LOGGER.error("Failed to load bowls: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        this.dataManager.set(PIGEON_BOWL_LOCATION.get(), bowlsData);

        try {
            this.statsTracker.readAdditional(compound);
        } catch (Exception e) {
            Hexerei.LOGGER.error("Failed to load stats tracker: " + e.getMessage());
            e.printStackTrace();
        }
        this.alterations.forEach((alter) -> {
            try {
                alter.onRead(this, compound);
            } catch (Exception e) {
                Hexerei.LOGGER.error("Failed to load alteration: " + e.getMessage());
                e.printStackTrace();
            }
        });

    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (SKILLS.get().equals(key)) {
            this.recalculateAlterationsCache();
            for (IBirdAlteration inst : this.alterations) {
                inst.init(this);
            }
        }
        if (SKILLS.get().equals(key)) {this.spendablePoints.markForRefresh();}
        if (PIGEON_LEVEL.get().equals(key)) {this.spendablePoints.markForRefresh();}

//        if (SIZE.equals(key)) {
//            this.refreshDimensions();
//        }
    }

    public void recalculateAlterationsCache() {
        this.alterations.clear();
        this.foodHandlers.clear();

//        List<SkillInstance> skills = this.getSkillMap();
//        this.alterations.addAll(skills);
//        for (SkillInstance inst : skills) {
//            if (inst instanceof IPigeonFoodHandler) {
//                this.foodHandlers.add((IPigeonFoodHandler) inst);
//            }
//        }
    }

    /**
     * If the entity can make changes to the pigeon
     * @param livingEntity The entity
     */
    @Override
    public boolean canInteract(LivingEntity livingEntity) {
        return this.willObeyOthers() || this.isOwner(livingEntity);
    }

    public Optional<ITextComponent> getOwnersName() {
        return this.dataManager.get(LAST_KNOWN_NAME);
    }

    public void setOwnersName(@Nullable ITextComponent comp) {
        this.setOwnersName(Optional.ofNullable(comp));
    }

    public void setOwnersName(Optional<ITextComponent> collar) {
        this.dataManager.set(LAST_KNOWN_NAME, collar);
    }

    public static Cache<DataParameter<EnumMode>> getMODE() {
        return MODE;
    }

    @Override
    public EnumMode getMode() {
        return this.dataManager.get(MODE.get());
    }

    public boolean isMode(EnumMode... modes) {
        EnumMode mode = this.getMode();
        for (EnumMode test : modes) {
            if (mode == test) {
                return true;
            }
        }
        return false;
    }

    public void setMode(EnumMode collar) {
        this.dataManager.set(MODE.get(), collar);
    }

    public Optional<BlockPos> getBedPos() {
        return this.getBedPos(this.world.getDimensionKey());
    }

    public Optional<BlockPos> getBedPos(RegistryKey<World> registryKey) {
        return this.dataManager.get(BIRD_COFFER_LOCATION.get()).getOrDefault(registryKey, Optional.empty());
    }

    public void setBedPos(@Nullable BlockPos pos) {
        this.setBedPos(this.world.getDimensionKey(), pos);
    }

    public void setBedPos(RegistryKey<World> registryKey, @Nullable BlockPos pos) {
        this.setBedPos(registryKey, WorldUtil.toImmutable(pos));
    }

    public void setBedPos(RegistryKey<World> registryKey, Optional<BlockPos> pos) {
        this.dataManager.set(BIRD_COFFER_LOCATION.get(), this.dataManager.get(BIRD_COFFER_LOCATION.get()).copy().set(registryKey, pos));
    }
//
//    public Optional<BlockPos> getBowlPos() {
//        return this.getBowlPos(this.world.getDimensionKey());
//    }

//    public Optional<BlockPos> getBowlPos(RegistryKey<World> registryKey) {
//        return this.dataManager.get(PIGEON_BOWL_LOCATION.get()).getOrDefault(registryKey, Optional.empty());
//    }
//
//    public void setBowlPos(@Nullable BlockPos pos) {
//        this.setBowlPos(this.world.getDimensionKey(), pos);
//    }
//
//    public void setBowlPos(RegistryKey<World> registryKey, @Nullable BlockPos pos) {
//        this.setBowlPos(registryKey, WorldUtil.toImmutable(pos));
//    }
//
//    public void setBowlPos(RegistryKey<World> registryKey, Optional<BlockPos> pos) {
//        this.dataManager.set(PIGEON_BOWL_LOCATION.get(), this.dataManager.get(PIGEON_BOWL_LOCATION.get()).copy().set(registryKey, pos));
//    }



    @Override
    public PigeonLevel getLevel() {
        return this.dataManager.get(PIGEON_LEVEL.get());
    }

    public void setLevel(PigeonLevel level) {
        this.dataManager.set(PIGEON_LEVEL.get(), level);
    }

    @Override
    public void increaseLevel(PigeonLevel.Type typeIn) {
        this.getLevel().incrementLevel(typeIn);
        this.markDataParameterDirty(PIGEON_LEVEL.get());
    }

//    @Override
//    public void setPigeonSize(int value) {
//        this.dataManager.set(SIZE, (byte)Math.min(5, Math.max(1, value)));
//    }
//
//    @Override
//    public int getPigeonSize() {
//        return this.dataManager.get(SIZE);
//    }

    public void setBoneVariant(ItemStack stack) {
        this.dataManager.set(BONE_VARIANT, stack);
    }

    public ItemStack getBoneVariant() {
        return this.dataManager.get(BONE_VARIANT);
    }

//    public boolean hasBone() {
//        return !this.getBoneVariant().isEmpty();
//    }

    private boolean getPigeonFlag(int bit) {
        return (this.dataManager.get(PIGEON_FLAGS) & bit) != 0;
    }

    private void setPigeonFlag(int bits, boolean flag) {
        byte c = this.dataManager.get(PIGEON_FLAGS);
        this.dataManager.set(PIGEON_FLAGS, (byte)(flag ? c | bits : c & ~bits));
    }

    public void setWillObeyOthers(boolean obeyOthers) {
        this.setPigeonFlag(2, obeyOthers);
    }
    public boolean willObeyOthers() {
        return this.getPigeonFlag(2);
    }
    public void setCanPlayersAttack(boolean flag) {
        this.setPigeonFlag(4, flag);
    }
    public boolean canPlayersAttack() {
        return this.getPigeonFlag(4);
    }
//    public void setHasSunglasses(boolean sunglasses) {
//        this.setPigeonFlag(16, sunglasses);
//    }
//    public boolean hasSunglasses() {
//        return this.getPigeonFlag(16);
//    }

    public List<SkillInstance> getSkillMap() {
        return this.dataManager.get(SKILLS.get());
    }
    public void setSkillMap(List<SkillInstance> map) {
        this.dataManager.set(SKILLS.get(), map);
    }

    public ActionResultType setSkillLevel(Skill skill, int level) {
        if (0 > level || level > skill.getMaxLevel()) {
            return ActionResultType.FAIL;
        }
        List<SkillInstance> activeSkills = this.getSkillMap();

        SkillInstance inst = null;
        for (SkillInstance activeInst : activeSkills) {
            if (activeInst.of(skill)) {
                inst = activeInst;
                break;
            }
        }
        if (inst == null) {
            if (level == 0) {
                return ActionResultType.PASS;
            }

            inst = skill.getDefault(level);
            activeSkills.add(inst);
            inst.init(this);
        } else {
            int previousLevel = inst.level();
            if (previousLevel == level) {
                return ActionResultType.PASS;
            }

            inst.setLevel(level);
            inst.set(this, previousLevel);

            if (level == 0) {
                activeSkills.remove(inst);
            }
        }
        this.markDataParameterDirty(SKILLS.get());
        return ActionResultType.SUCCESS;
    }


    public <T> void markDataParameterDirty(DataParameter<T> key) {
        this.markDataParameterDirty(key, true);
    }

    public <T> void markDataParameterDirty(DataParameter<T> key, boolean notify) {
        if (notify) {
            this.notifyDataManagerChange(key);
        }
        // Force the entry to update
        EntityDataManager.DataEntry<T> dataentry = this.dataManager.getEntry(key);
        dataentry.setDirty(true);
        this.dataManager.dirty = true;
    }

    @Override
    public Optional<SkillInstance> getSkill(Skill skillIn) {
        List<SkillInstance> activeSkills = this.getSkillMap();

        for (SkillInstance activeInst : activeSkills) {
            if (activeInst.of(skillIn)) {
                return Optional.of(activeInst);
            }
        }
        return Optional.empty();
    }

    @Override
    public int getLevel(Skill skillIn) {
        return this.getSkill(skillIn).map(SkillInstance::level).orElse(0);
    }

    @Override
    public <T> void setData(DataKey<T> key, T value) {
        if (key.isFinal() && this.hasData(key)) {
            throw new RuntimeException("Key is final but was tried to be set again.");
        }
        this.objects.put(key.getIndex(), value);
    }
    /**
     * Tries to put the object in the map, does nothing if the key already exists
     */
    @Override
    public <T> void setDataIfEmpty(DataKey<T> key, T value) {
        if (!this.hasData(key)) {
            this.objects.put(key.getIndex(), value);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getData(DataKey<T> key) {
        return (T) this.objects.get(key.getIndex());
    }

    @Override
    public <T> T getDataOrGet(DataKey<T> key, Supplier<T> other) {
        if (this.hasData(key)) {
            return this.getData(key);
        }
        return other.get();
    }

    @Override
    public <T> T getDataOrDefault(DataKey<T> key, T other) {
        if (this.hasData(key)) {
            return this.getData(key);
        }
        return other;
    }

    @Override
    public <T> boolean hasData(DataKey<T> key) {
        return this.objects.containsKey(key.getIndex());
    }

    @Override
    public void untame() {
        this.setTamed(false);
        this.navigator.clearPath();
//        this.setOrderedToSit(false);
        this.setHealth(8);

        this.getSkillMap().clear();
        this.markDataParameterDirty(SKILLS.get());

        this.setOwnerId(null);
        this.setWillObeyOthers(false);
        this.setMode(EnumMode.DOCILE);
    }

    public boolean canSpendPoints(int amount) {
        return this.getSpendablePoints() >= amount;
    }

    // When this method is changed the cache may need to be updated at certain points
    private final int getSpendablePointsInternal() {
        int totalPoints = 15 + this.getLevel().getLevel(PigeonLevel.Type.NORMAL);
        for (SkillInstance entry : this.getSkillMap()) {
            totalPoints -= entry.getSkill().getCummulativeCost(entry.level());
        }
        return totalPoints;
    }
    public int getSpendablePoints() {return this.spendablePoints.get();}
    @Override public boolean canBeCollidedWith() {return super.canBeCollidedWith();}
    @Override public boolean canBePushed() {return super.canBePushed();}

//    @Override
//    public void travel(Vector3d positionIn) {
//        if (this.isAlive()) {
//                this.stepHeight = 0.5F; // Default
//                this.jumpMovementFactor = 0.02F; // Default
//                super.travel(positionIn);
//
//            this.addMovementStat(this.getPosX() - this.prevPosX, this.getPosY() - this.prevPosY, this.getPosZ() - this.prevPosZ);
//        }
//    }
////
//    @Override
//    public boolean isLying() {
//        LivingEntity owner = this.getOwner();
//        boolean ownerSleeping = owner != null && owner.isSleeping();
//        if (ownerSleeping) {
//            return true;
//        }
//        Block blockBelow = this.world.getBlockState(this.blockPosition().below()).getBlock();
//        boolean onBed = blockBelow == PigeonBlocks.BIRD_COFFER.get() || BlockTags.BEDS.contains(blockBelow);
//        if (onBed) {
//            return true;
//        }
//        return false;
//    }
    @Override public List<IPigeonFoodHandler> getFoodHandlers() {return this.foodHandlers;}
    public void setTargetBlock(BlockPos pos) {this.targetBlock = pos;}
    public BlockPos getTargetBlock() {return this.targetBlock;}
}