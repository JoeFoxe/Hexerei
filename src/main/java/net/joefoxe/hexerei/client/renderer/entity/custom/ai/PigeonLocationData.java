package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.util.NBTUtilities;
import net.joefoxe.hexerei.util.WorldUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class PigeonLocationData implements IBirdData {

    private final PigeonLocationStorage storage;
    private final UUID uuid;
    private @Nullable UUID ownerId;

    // Location data
    private @Nullable Vector3d position;
    private @Nullable RegistryKey<World> dimension;

    // Other saved data
    private @Nullable ITextComponent name;
    private @Nullable ITextComponent ownerName;

    // Cached objects
    private PigeonEntity pigeon;
    private LivingEntity owner;
    protected PigeonLocationData(PigeonLocationStorage storageIn, UUID uuid) {
        this.storage = storageIn;
        this.uuid = uuid;
    }
    @Override public UUID getBirdId() {return this.uuid;}

    @Override
    @Nullable public UUID getOwnerId() {return this.ownerId;}

    @Override public String getBirdName() {return this.name == null ? "" : this.name.getString();}

    @Override
    public String getOwnerName() {return this.ownerName == null ? "" : this.ownerName.getString();}

    public void populate(PigeonEntity pigeonIn) {this.update(pigeonIn);}

    public void update(PigeonEntity pigeonIn) {
        this.ownerId = pigeonIn.getOwnerId();
        this.position = pigeonIn.getPositionVec();
        this.dimension = pigeonIn.world.getDimensionKey();

        this.name = pigeonIn.getName();
        this.ownerName = pigeonIn.getOwnersName().orElse(null);

        this.pigeon = pigeonIn;
        this.storage.markDirty();
    }
    public void read(CompoundNBT compound) {
        this.ownerId = NBTUtilities.getUniqueId(compound, "ownerId");
        this.position = NBTUtilities.getVector3d(compound);
        this.dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, NBTUtilities.getResourceLocation(compound, "dimension"));
        this.name = NBTUtilities.getTextComponent(compound, "name_text_component");
    }

    public CompoundNBT write(CompoundNBT compound) {
        NBTUtilities.putUniqueId(compound, "ownerId", this.ownerId);
        NBTUtilities.putVector3d(compound, this.position);
        NBTUtilities.putResourceLocation(compound, "dimension", this.dimension.getLocation());
        NBTUtilities.putTextComponent(compound, "name_text_component", this.name);

        return compound;
    }

    public static PigeonLocationData from(PigeonLocationStorage storageIn, PigeonEntity pigeonIn) {
        PigeonLocationData locationData = new PigeonLocationData(storageIn, pigeonIn.getUniqueID());
        locationData.populate(pigeonIn);
        return locationData;
    }

    @Nullable
    public Optional<LivingEntity> getOwner(@Nullable World worldIn) {
        if (worldIn == null) {
            return Optional.ofNullable(this.owner);
        }
        MinecraftServer server = worldIn.getServer();
        if (server == null) {
            throw new IllegalArgumentException("worldIn must be of ServerWorld");
        }
        for (ServerWorld world : server.getWorlds()) {
            LivingEntity possibleOwner = WorldUtil.getCachedEntity(world, LivingEntity.class, this.owner, this.uuid);
            if (possibleOwner != null) {
                this.owner = possibleOwner;
                return Optional.of(this.owner);
            }
        }
        this.owner = null;
        return Optional.empty();
    }

    @Nullable
    public Optional<PigeonEntity> getPigeon(@Nullable World worldIn) {
        if (worldIn == null) {
            return Optional.ofNullable(this.pigeon);
        }
        MinecraftServer server = worldIn.getServer();
        if (server == null) {
            throw new IllegalArgumentException("worldIn must be of ServerWorld");
        }
        for (ServerWorld world : server.getWorlds()) {
            PigeonEntity possiblePigeon = WorldUtil.getCachedEntity(world, PigeonEntity.class, this.pigeon, this.uuid);
            if (possiblePigeon != null) {
                this.pigeon = possiblePigeon;
                return Optional.of(this.pigeon);
            }
        }
        this.pigeon = null;
        return Optional.empty();
    }
    public boolean shouldDisplay(World worldIn, PlayerEntity playerIn, Hand handIn) {return playerIn.isCreative();}
    @Nullable public ITextComponent getName(@Nullable World worldIn) {return this.getPigeon(worldIn).map(PigeonEntity::getDisplayName).orElse(this.name);}
    @Nullable public Vector3d getPos(@Nullable ServerWorld worldIn) {return this.getPigeon(worldIn).map(PigeonEntity::getPositionVec).orElse(this.position);}
    @Nullable public Vector3d getPos() {return this.position;}
    @Nullable public RegistryKey<World> getDimension() {return this.dimension;}
    @Override public String toString() {return "PigeonLocationData [uuid=" + uuid + ", owner=" + ownerId + ", position=" + position + ", dimension=" + dimension + ", name=" + name + "]";}
}