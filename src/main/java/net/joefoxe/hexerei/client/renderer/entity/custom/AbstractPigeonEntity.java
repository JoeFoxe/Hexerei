package net.joefoxe.hexerei.client.renderer.entity.custom;

import net.joefoxe.hexerei.client.renderer.entity.custom.ai.IPigeonFoodHandler;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.Skill;
import net.joefoxe.hexerei.client.renderer.entity.custom.ai.SkillInstance;
import net.joefoxe.hexerei.util.DataKey;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


import javax.annotation.Nullable;

public abstract class AbstractPigeonEntity extends TameableEntity implements IPigeon {
    protected AbstractPigeonEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }


    public void setAttributeModifier(Attribute attribute, UUID modifierUUID, BiFunction<AbstractPigeonEntity, UUID, AttributeModifier> modifierGenerator) {
        ModifiableAttributeInstance attributeInst = this.getAttribute(attribute);
        AttributeModifier currentModifier = attributeInst.getModifier(modifierUUID);
        // Remove modifier if it exists
        if (currentModifier != null) {
            // Use UUID version as it is more efficient since
            // getModifier would need to be called again
            attributeInst.removeModifier(modifierUUID);
        }
        AttributeModifier newModifier = modifierGenerator.apply(this, modifierUUID);
        if (newModifier != null) {
            attributeInst.applyNonPersistentModifier(newModifier);
        }
    }

    public void removeAttributeModifier(Attribute attribute, UUID modifierUUID) {
        this.getAttribute(attribute).removeModifier(modifierUUID);
    }

    @Override public AbstractPigeonEntity getPigeon() {
        return this;
    }

    public void consumeItemFromStack(@Nullable Entity entity, ItemStack stack) {
            stack.shrink(1);
    }

    @Override public float getSoundVolume() {
        return super.getSoundVolume();
    }
    @Override public void playTameEffect(boolean play) {
        super.playTameEffect(play);
    }

}
