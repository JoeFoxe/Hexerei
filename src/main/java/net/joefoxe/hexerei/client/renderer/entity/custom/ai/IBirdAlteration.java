package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public interface IBirdAlteration {
    /**
     * Called when doin things like loading NBT data or syncing data
     * @param birdIn The bird
     */
    default void init(PigeonEntity birdIn) {}
    default void remove(PigeonEntity birdIn) {}
    default void onWrite(PigeonEntity birdIn, CompoundNBT compound) {}
    default void onRead(PigeonEntity birdIn, CompoundNBT compound) {}
    /**
     * Called at the end of tick
     */
    default void tick(PigeonEntity birdIn) {
    }
    /**
     * Called at the end of livingTick
     */
    default void livingTick(PigeonEntity birdIn) {
    }

    default ActionResult<Integer> healingTick(PigeonEntity birdIn, int healingTick) {return ActionResult.resultPass(healingTick);}
    default ActionResultType processInteract(PigeonEntity birdIn, World worldIn, PlayerEntity playerIn, Hand handIn) {return ActionResultType.PASS;}
    default ActionResultType canAttack(PigeonEntity birdIn, LivingEntity target) {return ActionResultType.PASS;}
    default ActionResultType canAttack(PigeonEntity birdIn, EntityType<?> entityType) {return ActionResultType.PASS;}
    default ActionResultType shouldAttackEntity(PigeonEntity bird, LivingEntity target, LivingEntity owner) {return ActionResultType.PASS;}
    default ActionResultType hitByEntity(PigeonEntity bird, Entity entity) {return ActionResultType.PASS;}
    default ActionResultType attackEntityAsMob(PigeonEntity birdIn, Entity target) {return ActionResultType.PASS;}
    default ActionResult<Float> attackEntityFrom(PigeonEntity bird, DamageSource source, float damage) {return ActionResult.resultPass(damage);}
    default ActionResultType canBlockDamageSource(PigeonEntity bird, DamageSource source) {return ActionResultType.PASS;}
    default void onDeath(PigeonEntity bird, DamageSource source) {}
    default void spawnDrops(PigeonEntity bird, DamageSource source) {}
    default void dropLoot(PigeonEntity bird, DamageSource source, boolean recentlyHitIn) {}

    default void dropInventory(PigeonEntity birdIn) {}
    default ActionResult<Float> attackEntityFrom(PigeonEntity birdIn, float distance, float damageMultiplier) {return ActionResult.resultPass(distance);}
    default ActionResult<Integer> setFire(PigeonEntity birdIn, int second) {return ActionResult.resultPass(second);}
    default ActionResultType isImmuneToFire(PigeonEntity birdIn) {return ActionResultType.PASS;}
    default ActionResultType isInvulnerableTo(PigeonEntity birdIn, DamageSource source) {return ActionResultType.PASS;}
    default ActionResultType isInvulnerable(PigeonEntity birdIn) {return ActionResultType.PASS;}
    default <T> LazyOptional<T> getCapability(PigeonEntity birdIn, Capability<T> cap, Direction side) {return null;}
    default void invalidateCapabilities(PigeonEntity birdIn) {}
    default ActionResultType isPotionApplicable(PigeonEntity birdIn, EffectInstance effectIn) {return ActionResultType.PASS;}
    /**
     * Only called serverside
     * @param birdIn The bird
     * @param source How the bird initially got wet
     */
    default void onShakingDry(PigeonEntity birdIn, Fluid source) {
    }
}