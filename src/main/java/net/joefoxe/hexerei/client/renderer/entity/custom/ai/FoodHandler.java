package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.joefoxe.hexerei.client.renderer.entity.custom.AbstractPigeonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
public class FoodHandler {
    private static final List<IPigeonFoodHandler> HANDLERS = Collections.synchronizedList(new ArrayList<>(4));
    private static final List<IPigeonFoodPredicate> DYN_PREDICATES = Collections.synchronizedList(new ArrayList<>(2));
    public static void registerHandler(IPigeonFoodHandler handler) {
        HANDLERS.add(handler);}
    public static void registerDynPredicate(IPigeonFoodPredicate handler) {
        DYN_PREDICATES.add(handler);}
    public static Optional<IPigeonFoodPredicate> isFood(ItemStack stackIn) {
        for (IPigeonFoodPredicate predicate : DYN_PREDICATES) {
            if (predicate.isFood(stackIn)) {
                return Optional.of(predicate);
            }
        }
        if (stackIn.getItem() instanceof IPigeonFoodHandler) {
            if (((IPigeonFoodHandler) stackIn.getItem()).isFood(stackIn)) {
                return Optional.of((IPigeonFoodHandler) stackIn.getItem());
            }
        }
        for (IPigeonFoodHandler handler : HANDLERS) {
            if (handler.isFood(stackIn)) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }
    public static Optional<IPigeonFoodHandler> getMatch(@Nullable AbstractPigeonEntity birdIn, ItemStack stackIn, @Nullable Entity entityIn) {
        for (IPigeonFoodHandler handler : (birdIn.getFoodHandlers())) {
            if (handler.canConsume(birdIn, stackIn, entityIn)) {
                return Optional.of(handler);
            }
        }
        if (stackIn.getItem() instanceof IPigeonFoodHandler) {
            if (((IPigeonFoodHandler) stackIn.getItem()).canConsume(birdIn, stackIn, entityIn)) {
                return Optional.of((IPigeonFoodHandler) stackIn.getItem());
            }
        }
        for (IPigeonFoodHandler handler : HANDLERS) {
            if (handler.canConsume(birdIn, stackIn, entityIn)) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }
}