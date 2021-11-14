package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import static net.minecraftforge.common.util.Constants.NBT.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.util.HexereiConstants;
import net.joefoxe.hexerei.util.NBTUtilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class PigeonLocationStorage  extends WorldSavedData {

    private Map<UUID, PigeonLocationData> locationDataMap = Maps.newConcurrentMap();
    public PigeonLocationStorage() {
        super(HexereiConstants.STORAGE_PIGEON_LOCATION);
    }

    public static PigeonLocationStorage get(World world) {
        if (!(world instanceof ServerWorld)) {
            throw new RuntimeException("Tried to access canis location data from the client. This should not happen...");
        }

        ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);

        DimensionSavedDataManager storage = overworld.getSavedData();
        return storage.getOrCreate(PigeonLocationStorage::new, HexereiConstants.STORAGE_PIGEON_LOCATION);
    }

    public Stream<PigeonLocationData> getCani(LivingEntity owner) {
        UUID ownerId = owner.getUniqueID();

        return this.locationDataMap.values().stream()
                .filter(data -> ownerId.equals(data.getOwnerId()));
    }

    public Stream<PigeonLocationData> getCani(LivingEntity owner, RegistryKey<World> key) {
        UUID ownerId = owner.getUniqueID();
        return this.locationDataMap.values().stream()
                .filter(data -> ownerId.equals(data.getOwnerId()))
                .filter(data -> key.equals(data.getDimension()));
    }

    @Nullable
    public PigeonLocationData getData(PigeonEntity pigeonIn) {
        return getData(pigeonIn.getUniqueID());
    }

    @Nullable
    public PigeonLocationData getData(UUID uuid) {
        if (this.locationDataMap.containsKey(uuid)) {
            return this.locationDataMap.get(uuid);
        }
        return null;
    }

    @Nullable
    public PigeonLocationData remove(PigeonEntity pigeonIn) {
        return remove(pigeonIn.getUniqueID());
    }

    @Nullable
    public PigeonLocationData getOrCreateData(PigeonEntity pigeonIn) {
        UUID uuid = pigeonIn.getUniqueID();
        return this.locationDataMap.computeIfAbsent(uuid, ($) -> {
            this.markDirty();
            return PigeonLocationData.from(this, pigeonIn);
        });
    }

    @Nullable
    public PigeonLocationData remove(UUID uuid) {
        if (this.locationDataMap.containsKey(uuid)) {
            PigeonLocationData storage = this.locationDataMap.remove(uuid);
            // Mark dirty so changes are saved
            this.markDirty();
            return storage;
        }
        return null;
    }

    @Nullable
    public PigeonLocationData putData(PigeonEntity pigeonIn) {
        UUID uuid = pigeonIn.getUniqueID();
        PigeonLocationData storage = new PigeonLocationData(this, uuid);
        this.locationDataMap.put(uuid, storage);
        // Mark dirty so changes are saved
        this.markDirty();
        return storage;
    }
    public Set<UUID> getAllUUID() {return Collections.unmodifiableSet(this.locationDataMap.keySet());}
    public Collection<PigeonLocationData> getAll() {return Collections.unmodifiableCollection(this.locationDataMap.values());}

    @Override
    public void read(CompoundNBT nbt) {
        this.locationDataMap.clear();
        ListNBT list = nbt.getList("locationData", TAG_COMPOUND);
        // Old style
        if (list.isEmpty()) {
            list = nbt.getList("canis_locations", TAG_COMPOUND);
        }

        for (int i = 0; i < list.size(); ++i) {
            CompoundNBT locationCompound = list.getCompound(i);
            UUID uuid = NBTUtilities.getUniqueId(locationCompound, "uuid");

            // Old style
            if (uuid == null) {
                uuid = NBTUtilities.getUniqueId(locationCompound, "entityId");
            }
            PigeonLocationData locationData = new PigeonLocationData(this, uuid);
            locationData.read(locationCompound);
            if (uuid == null) {
                Hexerei.LOGGER.info("Failed to load canis location data. Please report to mod author...");
                Hexerei.LOGGER.info(locationData);
                continue;
            }
            this.locationDataMap.put(uuid, locationData);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        for (Entry<UUID, PigeonLocationData> entry : this.locationDataMap.entrySet()) {
            CompoundNBT locationCompound = new CompoundNBT();
            PigeonLocationData locationData = entry.getValue();
            NBTUtilities.putUniqueId(locationCompound, "uuid", entry.getKey());
            locationData.write(locationCompound);
            list.add(locationCompound);
        }
        compound.put("locationData", list);
        return compound;
    }
}