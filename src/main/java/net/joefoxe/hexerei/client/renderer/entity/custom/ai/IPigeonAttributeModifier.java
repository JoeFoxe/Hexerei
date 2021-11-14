package net.joefoxe.hexerei.client.renderer.entity.custom.ai;


import net.joefoxe.hexerei.client.renderer.entity.custom.AbstractPigeonEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public interface IPigeonAttributeModifier {
//      Called when ever this instance is first added to a pidgeon, this is called when
//      the level is first set on the pidgeon or when it is loaded from NBT and when the
//      skills are synced to the client

        default void init(AbstractPigeonEntity pidgeonIn) {}
        default void remove(AbstractPigeonEntity pidgeonIn) {}
        default void onWrite(AbstractPigeonEntity pidgeonIn, CompoundNBT compound) {}
        default void onRead(AbstractPigeonEntity pidgeonIn, CompoundNBT compound) {}
        default void tick(AbstractPigeonEntity pidgeonIn) {}
        default void livingTick(AbstractPigeonEntity pidgeonIn) {}
        default ActionResult<Integer> hungerTick(AbstractPigeonEntity pidgeonIn, int hungerTick) {return ActionResult.resultPass(hungerTick);}
        default ActionResult<Integer> healingTick(AbstractPigeonEntity pidgeonIn, int healingTick) {return ActionResult.resultPass(healingTick);}
        default ActionResultType processInteract(AbstractPigeonEntity pidgeonIn, World worldIn, PlayerEntity playerIn, Hand handIn) {return ActionResultType.PASS;}
        default ActionResultType canBeRiddenInWater(AbstractPigeonEntity pidgeonIn, Entity rider) {return ActionResultType.PASS;}
        default ActionResultType canTrample(AbstractPigeonEntity pidgeonIn, BlockState state, BlockPos pos, float fallDistance) {return ActionResultType.PASS;}
        default ActionResult<Float> calculateFallDistance(AbstractPigeonEntity pidgeonIn, float distance) {return ActionResult.resultPass(0F);}
        default ActionResultType canBreatheUnderwater(AbstractPigeonEntity pidgeonIn) {return ActionResultType.PASS;}
        default ActionResultType canAttack(AbstractPigeonEntity pidgeonIn, LivingEntity target) {return ActionResultType.PASS;}
        default ActionResultType canAttack(AbstractPigeonEntity pidgeonIn, EntityType<?> entityType) {return ActionResultType.PASS;}
        default ActionResultType shouldAttackEntity(AbstractPigeonEntity pidgeon, LivingEntity target, LivingEntity owner) {return ActionResultType.PASS;}
        default ActionResultType hitByEntity(AbstractPigeonEntity pidgeon, Entity entity) {return ActionResultType.PASS;}
        default ActionResultType attackEntityAsMob(AbstractPigeonEntity pidgeonIn, Entity target) {return ActionResultType.PASS;}
        default ActionResult<Float> attackEntityFrom(AbstractPigeonEntity pidgeon, DamageSource source, float damage) {return ActionResult.resultPass(damage);}
        default ActionResultType canBlockDamageSource(AbstractPigeonEntity pidgeon, DamageSource source) {return ActionResultType.PASS;}
        default ActionResult<Float> attackEntityFrom(AbstractPigeonEntity pidgeonIn, float distance, float damageMultiplier) {return ActionResult.resultPass(distance);}
        default ActionResult<Integer> decreaseAirSupply(AbstractPigeonEntity pidgeonIn, int air) {return ActionResult.resultPass(air);}
        default ActionResult<Integer> determineNextAir(AbstractPigeonEntity pidgeonIn, int currentAir) {return ActionResult.resultPass(currentAir);}
        default ActionResult<Integer> setFire(AbstractPigeonEntity pidgeonIn, int second) {return ActionResult.resultPass(second);}
        default ActionResultType isImmuneToFire(AbstractPigeonEntity pidgeonIn) {return ActionResultType.PASS;}
        default ActionResultType isInvulnerableTo(AbstractPigeonEntity pidgeonIn, DamageSource source) {return ActionResultType.PASS;}
        default ActionResultType isInvulnerable(AbstractPigeonEntity pidgeonIn) {return ActionResultType.PASS;}
        default ActionResultType onLivingFall(AbstractPigeonEntity pidgeonIn, float distance, float damageMultiplier) {return ActionResultType.PASS;}
        default <T> LazyOptional<T> getCapability(AbstractPigeonEntity pidgeonIn, Capability<T> cap, Direction side) {return null;}
        default void invalidateCapabilities(AbstractPigeonEntity pidgeonIn) {}
        default ActionResult<Float> getMaxHunger(AbstractPigeonEntity pidgeonIn, float currentMax) {return ActionResult.resultPass(currentMax);}
        default ActionResult<Float> setPidgeonHunger(AbstractPigeonEntity pidgeonIn, float hunger, float diff) {return ActionResult.resultPass(hunger);}
        default ActionResultType isPotionApplicable(AbstractPigeonEntity pidgeonIn, EffectInstance effectIn) {return ActionResultType.PASS;}
}