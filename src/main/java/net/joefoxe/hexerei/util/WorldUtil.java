package net.joefoxe.hexerei.util;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import com.google.common.collect.AbstractIterator;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class WorldUtil {
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends TileEntity> T getTileEntity(IBlockReader worldIn, BlockPos posIn, Class<T> type) {
        TileEntity tileEntity = worldIn.getTileEntity(posIn);
        if (tileEntity != null && tileEntity.getClass().isAssignableFrom(type)) {
            return (T) tileEntity;
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    @Nullable
    public static <T extends Entity> T getCachedEntity(@Nullable World worldIn, Class<T> type, @Nullable T cached, @Nullable UUID uuid) {
        if ((cached == null || cached.removed) && uuid != null && worldIn instanceof ServerWorld) {
            Entity entity = ((ServerWorld) worldIn).getEntityByUuid(uuid);
            if (entity != null && entity.getClass().isAssignableFrom(type)) {
                return (T) entity;
            } else {
                return null;
            }
        }
        return cached;
    }
    public static Optional<BlockPos> toImmutable(BlockPos pos) {return pos != null ? Optional.of(pos.toImmutable()) : Optional.empty();}
    public static Optional<BlockPos> toImmutable(Optional<BlockPos> pos) {
        return pos.map(BlockPos::toImmutable);
    }
}