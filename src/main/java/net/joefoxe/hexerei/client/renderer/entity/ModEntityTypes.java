package net.joefoxe.hexerei.client.renderer.entity;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.client.renderer.entity.custom.BroomEntity;
import net.joefoxe.hexerei.client.renderer.entity.custom.BuffZombieEntity;
import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.util.PigeonAttributes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
@Mod.EventBusSubscriber(modid = Hexerei.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES
            = DeferredRegister.create(ForgeRegistries.ENTITIES, Hexerei.MOD_ID);

//    public static final EntityType<CrowEntity> CROW2 = registerEntity(EntityType.Builder.create(CrowEntity::new, EntityClassification.CREATURE).size(0.45F, 0.45F), "crow");

    public static final RegistryObject<EntityType<BuffZombieEntity>> BUFF_ZOMBIE =
            ENTITY_TYPES.register("buff_zombie",
                    () -> EntityType.Builder.create(BuffZombieEntity::new,
                                    EntityClassification.MONSTER).size(1f, 3f)
                            .build(new ResourceLocation(Hexerei.MOD_ID, "buff_zombie").toString()));

    public static final RegistryObject<EntityType<PigeonEntity>> PIGEON =
            ENTITY_TYPES.register("pigeon",
                    () -> EntityType.Builder.create(PigeonEntity::new,
                                    EntityClassification.CREATURE).size(0.4f, 0.3f)
                            .build(new ResourceLocation(Hexerei.MOD_ID, "pigeon").toString()));

    public static final RegistryObject<EntityType<BroomEntity>> BROOM =
            ENTITY_TYPES.register("broom",
                    () -> EntityType.Builder.<BroomEntity>create(BroomEntity::new,
                                    EntityClassification.MISC).size(1.175F, 0.3625F).trackingRange(10)
                            .build(new ResourceLocation(Hexerei.MOD_ID, "broom").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    public static void addEntityAttributes() {
        GlobalEntityTypeAttributes.put(PIGEON.get(),
                MobEntity.func_233666_p_()
                        .createMutableAttribute(Attributes.MAX_HEALTH, 80.0D)
                        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D)
                        .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D)
                        .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.25D) // createMutableAttributeed
                        .createMutableAttribute(PigeonAttributes.CRIT_CHANCE.get(), 0.01D)
                        .createMutableAttribute(PigeonAttributes.CRIT_BONUS.get(), 1D)
                        .create()
        );
    }

    private static <T extends Entity> EntityType<T> register2(String key, EntityType.Builder<T> builder) {
        return Registry.register(Registry.ENTITY_TYPE, key, builder.build(key));
    }

    private static final EntityType registerEntity(EntityType.Builder builder, String entityName) {
        ResourceLocation nameLoc = new ResourceLocation(Hexerei.MOD_ID, entityName);
        return (EntityType) builder.build(entityName).setRegistryName(nameLoc);
    }

    public static Predicate<LivingEntity> buildPredicateFromTag(ITag entityTag){
        if(entityTag == null){
            return Predicates.alwaysFalse();
        }else{
            return (com.google.common.base.Predicate<LivingEntity>) e -> e.isAlive() && e.getType().isContained(entityTag);
        }
    }

    public static Predicate<LivingEntity> buildPredicateFromTagTameable(ITag entityTag, LivingEntity owner){
        if(entityTag == null){
            return Predicates.alwaysFalse();
        }else{
            return (com.google.common.base.Predicate<LivingEntity>) e -> e.isAlive() && e.getType().isContained(entityTag) && !owner.isOnSameTeam(e);
        }
    }

}