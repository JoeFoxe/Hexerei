package net.joefoxe.hexerei.client.renderer.entity.custom;

import com.google.common.base.Predicate;
import net.joefoxe.hexerei.client.renderer.entity.ModEntityTypes;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.CreatureAITargetItems;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.DirectPathNavigator;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.ITargetsDroppedItems;
import net.joefoxe.hexerei.util.HexereiTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.*;

public class CrowEntity extends AnimalEntity implements ITargetsDroppedItems {
    private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(CrowEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> FLIGHT_LOOK_YAW = EntityDataManager.createKey(CrowEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> ATTACK_TICK = EntityDataManager.createKey(CrowEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(CrowEntity.class, DataSerializers.BOOLEAN);
    public boolean aiItemFlag = false;
    private boolean isLandNavigator;
    private int timeFlying;
    private BlockPos orbitPos = null;
    private double orbitDist = 5D;
    private boolean orbitClockwise = false;
    private boolean fallFlag = false;
    private int flightLookCooldown = 0;
    private float targetFlightLookYaw;
    private int heldItemTime = 0;
    public int treasureSitTime;
    public UUID feederUUID = null;


    public CrowEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathPriority(PathNodeType.COCOA, -1.0F);
        this.setPathPriority(PathNodeType.FENCE, -1.0F);
        switchNavigator(false);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MonsterEntity.func_234295_eP_().createMutableAttribute(Attributes.MAX_HEALTH, 8.0D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Flying", this.isFlying());
        compound.putBoolean("Sitting", this.isSitting());
        if(feederUUID != null){
            compound.putUniqueId("FeederUUID", feederUUID);
        }
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setFlying(compound.getBoolean("Flying"));
        this.setSitting(compound.getBoolean("Sitting"));
        if(compound.hasUniqueId("FeederUUID")){
            this.feederUUID = compound.getUniqueId("FeederUUID");
        }
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.0D, Ingredient.fromItems(Items.PUMPKIN_SEEDS,Items.MELON_SEEDS,Items.BEETROOT_SEEDS,Items.WHEAT_SEEDS), false){
            public boolean shouldExecute(){
                return !CrowEntity.this.aiItemFlag && super.shouldExecute();
            }
        });
        this.goalSelector.addGoal(5, new AIWanderIdle());
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookAtGoal(this, CreatureEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
//        this.goalSelector.addGoal(9, new AIScatter());
        this.targetSelector.addGoal(1, new AITargetItems(this, false, false, 15, 16));
    }

    public boolean isBreedingItem(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.PUMPKIN_SEEDS;
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveController = new MovementController(this);
            this.navigator = new GroundPathNavigator(this, world);
            this.isLandNavigator = true;
        } else {
            this.moveController = new MoveHelper(this);
            this.navigator = new DirectPathNavigator(this, world);
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FLYING, false);
        this.dataManager.register(SITTING, false);
        this.dataManager.register(ATTACK_TICK, 0);
        this.dataManager.register(FLIGHT_LOOK_YAW, 0F);
    }

    public boolean isSitting() {
        return this.dataManager.get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.dataManager.set(SITTING, sitting);
    }

    public float getFlightLookYaw() {
        return dataManager.get(FLIGHT_LOOK_YAW);
    }

    public void setFlightLookYaw(float yaw) {
        dataManager.set(FLIGHT_LOOK_YAW, yaw);
    }

    public boolean isFlying() {
        return this.dataManager.get(FLYING);
    }

    public void setFlying(boolean flying) {
        if (flying && this.isChild()) {
            flying = false;
        }
        this.dataManager.set(FLYING, flying);
    }

    public void tick() {
        super.tick();
        float yMot = (float) -((float) this.getMotion().y * (double) (180F / (float) Math.PI));
        float absYaw = Math.abs(this.rotationYaw - this.prevRotationYaw);

        if (!world.isRemote) {
            if (isFlying()) {
                float lookYawDist = Math.abs(this.getFlightLookYaw() - targetFlightLookYaw);
                if (flightLookCooldown > 0) {
                    flightLookCooldown--;
                }
                if (flightLookCooldown == 0 && this.rand.nextInt(4) == 0 && lookYawDist < 0.5F) {
                    targetFlightLookYaw = MathHelper.clamp(rand.nextFloat() * 120F - 60, -60, 60);
                    flightLookCooldown = 3 + rand.nextInt(15);
                }
                if (this.getFlightLookYaw() < this.targetFlightLookYaw && lookYawDist > 0.5F) {
                    this.setFlightLookYaw(this.getFlightLookYaw() + Math.min(lookYawDist, 4F));
                }
                if (this.getFlightLookYaw() > this.targetFlightLookYaw && lookYawDist > 0.5F) {
                    this.setFlightLookYaw(this.getFlightLookYaw() - Math.min(lookYawDist, 4F));
                }
                if (this.onGround && !this.isInWaterOrBubbleColumn() && this.timeFlying > 30) {
                    this.setFlying(false);
                }
                timeFlying++;
                this.setNoGravity(true);
                if (this.isPassenger() || this.isInLove()) {
                    this.setFlying(false);
                }
            } else {
                fallFlag = false;
                timeFlying = 0;
                this.setNoGravity(false);
            }
            if (isFlying() && this.isLandNavigator) {
                switchNavigator(false);
            }
            if (!isFlying() && !this.isLandNavigator) {
                switchNavigator(true);
            }
        }
        if (!this.getHeldItemMainhand().isEmpty()) {
            heldItemTime++;
            if (heldItemTime > 200 && canTargetItem(this.getHeldItemMainhand())) {
                heldItemTime = 0;
                this.heal(4);
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, this.getSoundVolume(), this.getSoundPitch());
                if (this.getHeldItemMainhand().hasContainerItem()) {
                    this.entityDropItem(this.getHeldItemMainhand().getContainerItem());
                }
                eatItemEffect(this.getHeldItemMainhand());
                this.getHeldItemMainhand().shrink(1);
            }
        } else {
            heldItemTime = 0;
        }
        if(treasureSitTime > 0){
            treasureSitTime--;
        }
        if(this.isSitting() && this.isInWaterOrBubbleColumn()){
            this.setMotion(this.getMotion().add(0, 0.02F, 0));
        }
    }

    public void eatItem(){
        heldItemTime = 200;
    }

    private void eatItemEffect(ItemStack heldItemMainhand) {
        for (int i = 0; i < 2 + rand.nextInt(2); i++) {
            double d2 = this.rand.nextGaussian() * 0.02D;
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            float radius = this.getWidth() * 0.65F;
            float angle = (0.01745329251F * this.renderYawOffset);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            IParticleData data = new ItemParticleData(ParticleTypes.ITEM, heldItemMainhand);
            if (heldItemMainhand.getItem() instanceof BlockItem) {
                data = new BlockParticleData(ParticleTypes.BLOCK, ((BlockItem) heldItemMainhand.getItem()).getBlock().getDefaultState());
            }
            this.world.addParticle(data, this.getPosX() + extraX, this.getPosY() + this.getHeight() * 0.6F, this.getPosZ() + extraZ, d0, d1, d2);
        }
    }

    public Vector3d getBlockInViewAway(Vector3d fleePos, float radiusAdd) {
        float radius = 5 + radiusAdd + this.getRNG().nextInt(5);
        float neg = this.getRNG().nextBoolean() ? 1 : -1;
        float renderYawOffset = this.renderYawOffset;
        float angle = (0.01745329251F * renderYawOffset) + 3.15F + (this.getRNG().nextFloat() * neg);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        BlockPos radialPos = new BlockPos(fleePos.getX() + extraX, 0, fleePos.getZ() + extraZ);
        BlockPos ground = getCrowGround(radialPos);
        int distFromGround = (int) this.getPosY() - ground.getY();
        int flightHeight = 8 + this.getRNG().nextInt(4);
        BlockPos newPos = ground.up(distFromGround > 3 ? flightHeight : this.getRNG().nextInt(4) + 8);
        if (!this.isTargetBlocked(Vector3d.copyCentered(newPos)) && this.getDistanceSq(Vector3d.copyCentered(newPos)) > 1) {
            return Vector3d.copyCentered(newPos);
        }
        return null;
    }

    public Vector3d getBlockGrounding(Vector3d fleePos) {
        float radius = 10 + this.getRNG().nextInt(15);
        float neg = this.getRNG().nextBoolean() ? 1 : -1;
        float renderYawOffset = this.renderYawOffset;
        float angle = (0.01745329251F * renderYawOffset) + 3.15F + (this.getRNG().nextFloat() * neg);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        BlockPos radialPos = new BlockPos(fleePos.getX() + extraX, getPosY(), fleePos.getZ() + extraZ);
        BlockPos ground = this.getCrowGround(radialPos);
        if (ground.getY() == 0) {
            return this.getPositionVec();
        } else {
            ground = this.getPosition();
            while (ground.getY() > 2 && world.isAirBlock(ground)) {
                ground = ground.down();
            }
        }
        if (!this.isTargetBlocked(Vector3d.copyCentered(ground.up()))) {
            return Vector3d.copyCentered(ground);
        }
        return null;
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.getPosition();
        while (position.getY() > 0 && world.isAirBlock(position)) {
            position = position.down();
        }
        return !world.getFluidState(position).isEmpty() || position.getY() <= 0;
    }

    public ActionResultType getEntityInteractionResult(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        Item item = itemstack.getItem();
        ActionResultType type = super.getEntityInteractionResult(player, hand);
        if (!this.getHeldItemMainhand().isEmpty() && type != ActionResultType.SUCCESS) {
            this.entityDropItem(this.getHeldItemMainhand().copy());
            this.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            return ActionResultType.SUCCESS;
        } else {
            return type;
        }
    }

    public boolean isTargetBlocked(Vector3d target) {
        Vector3d Vector3d = new Vector3d(this.getPosX(), this.getPosYEye(), this.getPosZ());

        return this.world.rayTraceBlocks(new RayTraceContext(Vector3d, target, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)).getType() != RayTraceResult.Type.MISS;
    }

    private Vector3d getOrbitVec(Vector3d vector3d, float gatheringCircleDist) {
        float angle = (0.01745329251F * (float) this.orbitDist * (orbitClockwise ? -ticksExisted : ticksExisted));
        double extraX = gatheringCircleDist * MathHelper.sin((angle));
        double extraZ = gatheringCircleDist * MathHelper.cos(angle);
        if (this.orbitPos != null) {
            Vector3d pos = new Vector3d(orbitPos.getX() + extraX, orbitPos.getY() + rand.nextInt(2), orbitPos.getZ() + extraZ);
            if (this.world.isAirBlock(new BlockPos(pos))) {
                return pos;
            }
        }
        return null;
    }

    public BlockPos getCrowGround(BlockPos in) {
        BlockPos position = new BlockPos(in.getX(), this.getPosY(), in.getZ());
        while (position.getY() < 256 && !world.getFluidState(position).isEmpty()) {
            position = position.up();
        }
        while (position.getY() > 2 && world.isAirBlock(position)) {
            position = position.down();
        }
        return position;
    }


    public void travel(Vector3d vec3d) {
        if (this.isSitting()) {
            if (this.getNavigator().getPath() != null) {
                this.getNavigator().clearPath();
            }
            vec3d = Vector3d.ZERO;
        }
        super.travel(vec3d);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        ItemStack duplicate = e.getItem().copy();
        duplicate.setCount(1);
        if (!this.getHeldItem(Hand.MAIN_HAND).isEmpty() && !this.world.isRemote) {
            this.entityDropItem(this.getHeldItem(Hand.MAIN_HAND), 0.0F);
        }
        if(e.getThrowerId() != null && (e.getItem().getItem() == Items.WHEAT_SEEDS || e.getItem().getItem() == Items.PUMPKIN_SEEDS || e.getItem().getItem() == Items.MELON_SEEDS || e.getItem().getItem() == Items.BEETROOT_SEEDS)){
            PlayerEntity player = world.getPlayerByUuid(e.getThrowerId());
            if(player != null){
                feederUUID = e.getThrowerId();
            }
        }
        this.setFlying(true);
        this.setHeldItem(Hand.MAIN_HAND, duplicate);
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.getItem().equals(Items.WHEAT_SEEDS) && !this.isSitting();
    }

    @Nullable
    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
        return ModEntityTypes.CROW.get().create(world);
    }


//    private class AIScatter extends Goal {
//        protected final CrowEntity.AIScatter.Sorter theNearestAttackableTargetSorter;
//        protected final Predicate<? super Entity> targetEntitySelector;
//        protected int executionChance = 8;
//        protected boolean mustUpdate;
//        private Entity targetEntity;
//        private Vector3d flightTarget = null;
//        private int cooldown = 0;
//        private ITag tag;
//
//        AIScatter() {
//            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
//            tag = EntityTypeTags.getCollection().get(HexereiTags.CrowScatter);
//            this.theNearestAttackableTargetSorter = new CrowEntity.AIScatter.Sorter(CrowEntity.this);
//            this.targetEntitySelector = new Predicate<Entity>() {
//                @Override
//                public boolean apply(@Nullable Entity e) {
//                    return e.isAlive() && e.getType().isContained(tag) || e instanceof PlayerEntity && !((PlayerEntity) e).isCreative();
//                }
//            };
//        }
//
//        @Override
//        public boolean shouldExecute() {
//            if (CrowEntity.this.isPassenger() || CrowEntity.this.isSitting() || CrowEntity.this.aiItemFlag || CrowEntity.this.isBeingRidden()) {
//                return false;
//            }
//            if (!this.mustUpdate) {
//                long worldTime = CrowEntity.this.world.getGameTime() % 10;
//                if (CrowEntity.this.getIdleTime() >= 100 && worldTime != 0) {
//                    return false;
//                }
//                if (CrowEntity.this.getRNG().nextInt(this.executionChance) != 0 && worldTime != 0) {
//                    return false;
//                }
//            }
//            List<Entity> list = CrowEntity.this.world.getEntitiesWithinAABB(Entity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
//            if (list.isEmpty()) {
//                return false;
//            } else {
//                Collections.sort(list, this.theNearestAttackableTargetSorter);
//                this.targetEntity = list.get(0);
//                this.mustUpdate = false;
//                return true;
//            }
//        }
//
//        @Override
//        public boolean shouldContinueExecuting() {
//            return targetEntity != null;
//        }
//
//        public void resetTask() {
//            flightTarget = null;
//            this.targetEntity = null;
//        }
//
//        @Override
//        public void tick() {
//            if (cooldown > 0) {
//                cooldown--;
//            }
//            if (flightTarget != null) {
//                CrowEntity.this.setFlying(true);
//                CrowEntity.this.getMoveHelper().setMoveTo(flightTarget.x, flightTarget.y, flightTarget.z, 1F);
//                if (cooldown == 0 && CrowEntity.this.isTargetBlocked(flightTarget)) {
//                    cooldown = 30;
//                    flightTarget = null;
//                }
//            }
//
//            if (targetEntity != null) {
//                if (CrowEntity.this.onGround || flightTarget == null || flightTarget != null && CrowEntity.this.getDistanceSq(flightTarget) < 3) {
//                    Vector3d vec = CrowEntity.this.getBlockInViewAway(targetEntity.getPositionVec(), 0);
//                    if (vec != null && vec.getY() > CrowEntity.this.getPosY()) {
//                        flightTarget = vec;
//                    }
//                }
//                if (CrowEntity.this.getDistance(targetEntity) > 20.0F) {
//                    this.resetTask();
//                }
//            }
//        }
//
//        protected double getTargetDistance() {
//            return 4D;
//        }
//
//        protected AxisAlignedBB getTargetableArea(double targetDistance) {
//            Vector3d renderCenter = new Vector3d(CrowEntity.this.getPosX(), CrowEntity.this.getPosY() + 0.5, CrowEntity.this.getPosZ());
//            AxisAlignedBB aabb = new AxisAlignedBB(-2, -2, -2, 2, 2, 2);
//            return aabb.offset(renderCenter);
//        }
//
//
//        public class Sorter implements Comparator<Entity> {
//            private final Entity theEntity;
//
//            public Sorter(Entity theEntityIn) {
//                this.theEntity = theEntityIn;
//            }
//
//            public int compare(Entity p_compare_1_, Entity p_compare_2_) {
//                double d0 = this.theEntity.getDistanceSq(p_compare_1_);
//                double d1 = this.theEntity.getDistanceSq(p_compare_2_);
//                return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
//            }
//        }
//    }


    private class AIWanderIdle extends Goal {
        protected final CrowEntity crow;
        protected double x;
        protected double y;
        protected double z;
        private boolean flightTarget = false;
        private int orbitResetCooldown = 0;
        private int maxOrbitTime = 360;
        private int orbitTime = 0;

        public AIWanderIdle() {
            super();
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
            this.crow = CrowEntity.this;
        }

        @Override
        public boolean shouldExecute() {
            if (orbitResetCooldown < 0) {
                orbitResetCooldown++;
            }
            if ((crow.getAttackTarget() != null && crow.getAttackTarget().isAlive() && !this.crow.isBeingRidden()) || crow.isSitting() || this.crow.isPassenger()) {
                return false;
            } else {
                if (this.crow.getRNG().nextInt(20) != 0 && !crow.isFlying() || crow.aiItemFlag) {
                    return false;
                }
                if (this.crow.isChild()) {
                    this.flightTarget = false;
                } else if (this.crow.isInWaterOrBubbleColumn()) {
                    this.flightTarget = true;
                } else if (this.crow.isOnGround()) {
                    this.flightTarget = rand.nextInt(10) == 0;
                } else {
                    if (orbitResetCooldown == 0 && rand.nextInt(6) == 0) {
                        orbitResetCooldown = 100 + rand.nextInt(300);
                        crow.orbitPos = crow.getPosition();
                        crow.orbitDist = 4 + rand.nextInt(5);
                        crow.orbitClockwise = rand.nextBoolean();
                        orbitTime = 0;
                        maxOrbitTime = (int) (180 + 360 * rand.nextFloat());
                    }
                    this.flightTarget = rand.nextInt(5) != 0 && crow.timeFlying < 400;
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
        }

        public void tick() {
            if (orbitResetCooldown > 0) {
                orbitResetCooldown--;
            }
            if (orbitResetCooldown < 0) {
                orbitResetCooldown++;
            }
            if (orbitResetCooldown > 0 && crow.orbitPos != null) {
                if (orbitTime < maxOrbitTime && !crow.isInWaterOrBubbleColumn()) {
                    orbitTime++;
                } else {
                    orbitTime = 0;
                    crow.orbitPos = null;
                    orbitResetCooldown = -400 - rand.nextInt(400);
                }
            }
            if (crow.collidedHorizontally && !crow.onGround) {
                resetTask();
            }
            if (flightTarget) {
                crow.getMoveHelper().setMoveTo(x, y, z, 1F);
            } else {
                if (crow.isFlying() && !crow.onGround) {
                    if (!crow.isInWaterOrBubbleColumn()) {
                        //  crow.setMotion(crow.getMotion().mul(1F, 0.6F, 1F));
                    }
                } else {
                    this.crow.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, 1F);
                }
            }
            if (!flightTarget && isFlying()) {
                crow.fallFlag = true;
                if (crow.onGround) {
                    crow.setFlying(false);
                    orbitTime = 0;
                    crow.orbitPos = null;
                    orbitResetCooldown = -400 - rand.nextInt(400);
                }
            }
            if (isFlying() && (!world.isAirBlock(crow.getPositionUnderneath()) || crow.onGround) && !crow.isInWaterOrBubbleColumn() && crow.timeFlying > 30) {
                crow.setFlying(false);
                orbitTime = 0;
                crow.orbitPos = null;
                orbitResetCooldown = -400 - rand.nextInt(400);
            }
        }

        @Nullable
        protected Vector3d getPosition() {
            Vector3d vector3d = crow.getPositionVec();
            if (orbitResetCooldown > 0 && crow.orbitPos != null) {
                return crow.getOrbitVec(vector3d, 4 + rand.nextInt(4));
            }
            if (crow.isBeingRidden() || crow.isOverWaterOrVoid()) {
                flightTarget = true;
            }
            if (flightTarget) {
                if (crow.timeFlying < 340 || crow.isBeingRidden() || crow.isOverWaterOrVoid()) {
                    return crow.getBlockInViewAway(vector3d, 0);
                } else {
                    return crow.getBlockGrounding(vector3d);
                }
            } else {
                return RandomPositionGenerator.findRandomTarget(this.crow, 10, 7);
            }
        }

        public boolean shouldContinueExecuting() {
            if (flightTarget) {
                return crow.isFlying() && crow.getDistanceSq(x, y, z) > 4F;
            } else {
                return (!this.crow.getNavigator().noPath()) && !this.crow.isBeingRidden();
            }
        }

        public void startExecuting() {
            if (flightTarget) {
                crow.setFlying(true);
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


    class MoveHelper extends MovementController {
        private final CrowEntity parentEntity;

        public MoveHelper(CrowEntity bird) {
            super(bird);
            this.parentEntity = bird;
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.posX - parentEntity.getPosX(), this.posY - parentEntity.getPosY(), this.posZ - parentEntity.getPosZ());
                double d5 = vector3d.length();
                if (d5 < 0.3) {
                    this.action = MovementController.Action.WAIT;
                    parentEntity.setMotion(parentEntity.getMotion().scale(0.5D));
                } else {
                    double d1 = this.posY - this.parentEntity.getPosY();
                    float yScale = d1 > 0 || fallFlag ? 1F : 0.7F;
                    parentEntity.setMotion(parentEntity.getMotion().add(vector3d.scale(speed * 0.03D / d5)));
                    Vector3d vector3d1 = parentEntity.getMotion();
                    parentEntity.rotationYaw = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                    parentEntity.renderYawOffset = parentEntity.rotationYaw;

                }

            }
        }
    }

    private class AITargetItems extends CreatureAITargetItems {

        public AITargetItems(CreatureEntity creature, boolean checkSight, boolean onlyNearby, int tickThreshold, int radius) {
            super(creature, checkSight, onlyNearby, tickThreshold, radius);
            this.executionChance = 1;
        }

        public void resetTask() {
            super.resetTask();
            ((CrowEntity) goalOwner).aiItemFlag = false;
        }

        public boolean shouldExecute() {
            return super.shouldExecute() && !((CrowEntity) goalOwner).isSitting() && (goalOwner.getAttackTarget() == null || !goalOwner.getAttackTarget().isAlive());
        }

        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && !((CrowEntity) goalOwner).isSitting() && (goalOwner.getAttackTarget() == null || !goalOwner.getAttackTarget().isAlive());
        }

        @Override
        protected void moveTo() {
            CrowEntity crow = (CrowEntity) goalOwner;
            if (this.targetEntity != null) {
                crow.aiItemFlag = true;
                if (this.goalOwner.getDistance(targetEntity) < 2) {
                    crow.getMoveHelper().setMoveTo(this.targetEntity.getPosX(), targetEntity.getPosY(), this.targetEntity.getPosZ(), 1.5F);
                }
                if (this.goalOwner.getDistance(this.targetEntity) > 8 || crow.isFlying()) {
                    crow.setFlying(true);
                    float f = (float) (crow.getPosX() - targetEntity.getPosX());
                    float f1 = 1.8F;
                    float f2 = (float) (crow.getPosZ() - targetEntity.getPosZ());
                    float xzDist = MathHelper.sqrt(f * f + f2 * f2);

                    if (!crow.canEntityBeSeen(targetEntity)) {
                        crow.getMoveHelper().setMoveTo(this.targetEntity.getPosX(), 1 + crow.getPosY(), this.targetEntity.getPosZ(), 1.5F);
                    } else {
                        if (xzDist < 5) {
                            f1 = 0;
                        }
                        crow.getMoveHelper().setMoveTo(this.targetEntity.getPosX(), f1 + this.targetEntity.getPosY(), this.targetEntity.getPosZ(), 1.5F);
                    }
                } else {
                    this.goalOwner.getNavigator().tryMoveToXYZ(this.targetEntity.getPosX(), this.targetEntity.getPosY(), this.targetEntity.getPosZ(), 1.5F);
                }
            }
        }

        @Override
        public void tick() {
            super.tick();
            moveTo();
        }
    }

}
