package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.util.HexereiConstants;
import net.joefoxe.hexerei.util.NBTUtilities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import static net.minecraftforge.common.util.Constants.NBT.*;

public class PigeonReviveStorage extends WorldSavedData {

    private Map<UUID, PigeonReviveData> reviveDataMap = Maps.newConcurrentMap();

    public PigeonReviveStorage() {
        super(HexereiConstants.STORAGE_PIGEON_RESPAWN); // TODO -->  may be STORAGE_PIDGEON_RESPAWN  for you so watch out
    }

    public static PigeonReviveStorage get(World world) {
        if (!(world instanceof ServerWorld)) {throw new RuntimeException("Tried to access pidgeon revive data from the client. This should not happen...");}

        ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);

        DimensionSavedDataManager storage = overworld.getSavedData();
        return storage.getOrCreate(PigeonReviveStorage::new, HexereiConstants.STORAGE_PIGEON_RESPAWN);  // TODO -->  may be STORAGE_PIDGEON_RESPAWN  for you so watch out
    }
    public Stream<PigeonReviveData> getPigeons(@Nonnull UUID ownerId) {return this.reviveDataMap.values().stream().filter(data -> ownerId.equals(data.getOwnerId()));}
    public Stream<PigeonReviveData> getPigeons(@Nonnull String ownerName) {return this.reviveDataMap.values().stream().filter(data -> ownerName.equals(data.getOwnerName()));}

    @Nullable
    public PigeonReviveData getData(UUID uuid) {
        if (this.reviveDataMap.containsKey(uuid)) {
            return this.reviveDataMap.get(uuid);
        }
        return null;
    }

    @Nullable
    public PigeonReviveData remove(UUID uuid) {
        if (this.reviveDataMap.containsKey(uuid)) {
            PigeonReviveData storage = this.reviveDataMap.remove(uuid);
            // Mark dirty so changes are saved
            this.markDirty();
            return storage;
        }
        return null;
    }

    @Nullable
    public PigeonReviveData putData(PigeonEntity pidgeonIn) {
        UUID uuid = pidgeonIn.getUniqueID();
        PigeonReviveData storage = new PigeonReviveData(this, uuid);
        storage.populate(pidgeonIn);
        this.reviveDataMap.put(uuid, storage);
        // Mark dirty so changes are saved
        this.markDirty();
        return storage;
    }
    public Set<UUID> getAllUUID() {return Collections.unmodifiableSet(this.reviveDataMap.keySet());}
    public Collection<PigeonReviveData> getAll() {return Collections.unmodifiableCollection(this.reviveDataMap.values());}

    @Override
    public void read(CompoundNBT nbt) {
        this.reviveDataMap.clear();
        ListNBT list = nbt.getList("reviveData", TAG_COMPOUND);
        for (int i = 0; i < list.size(); ++i) {
            CompoundNBT reviveCompound = list.getCompound(i);
            UUID uuid = NBTUtilities.getUniqueId(reviveCompound, "uuid");
            PigeonReviveData reviveData = new PigeonReviveData(this, uuid);
            reviveData.read(reviveCompound);
            if (uuid == null) {
                Hexerei.LOGGER.info("Failed to load pidgeon revive data. Please complain to Joe...");
                Hexerei.LOGGER.info(reviveData);
                continue;
            }
            this.reviveDataMap.put(uuid, reviveData);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        for (Map.Entry<UUID, PigeonReviveData> entry : this.reviveDataMap.entrySet()) {
            CompoundNBT reviveCompound = new CompoundNBT();
            PigeonReviveData reviveData = entry.getValue();
            NBTUtilities.putUniqueId(reviveCompound, "uuid", entry.getKey());
            reviveData.write(reviveCompound);
            list.add(reviveCompound);
        }
        compound.put("reviveData", list);
        return compound;
    }
}