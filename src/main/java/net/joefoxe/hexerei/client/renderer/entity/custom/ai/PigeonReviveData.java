package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import com.google.common.collect.Lists;
import net.joefoxe.hexerei.client.renderer.entity.ModEntityTypes;
import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.util.NBTUtilities;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class PigeonReviveData implements IBirdData {

    private final PigeonReviveStorage storage;
    private final UUID uuid;
    private CompoundNBT data;

    private static final List<String> TAGS_TO_REMOVE = Lists.newArrayList(
            "Pos", "Health", "Motion", "Rotation", "FallDistance", "Fire", "Air", "OnGround",
            "Dimension", "PortalCooldown", "Passengers", "Leash", "InLove", "Leash", "HurtTime",
            "HurtByTimestamp", "DeathTime", "AbsorptionAmount", "FallFlying", "Brain", "Sitting");

    protected PigeonReviveData(PigeonReviveStorage storageIn, UUID uuid) {
        this.storage = storageIn;
        this.uuid = uuid;
    }

    @Override
    public UUID getBirdId() {return this.uuid;}

    @Override
    public String getBirdName() {
        ITextComponent name = NBTUtilities.getTextComponent(this.data, "CustomName");
        return name == null ? "" : name.getString();
    }

    @Override
    public UUID getOwnerId() {
        String str = data.getString("OwnerUUID");
        return "".equals(str) ? null : UUID.fromString(str);
    }

    @Override
    public String getOwnerName() {
        ITextComponent name = NBTUtilities.getTextComponent(this.data, "lastKnownOwnerName");
        return name == null ? "" : name.getString();
    }

    public void populate(PigeonEntity pigeonIn) {
        this.data = new CompoundNBT();
        pigeonIn.writeWithoutTypeId(this.data);

        // Remove tags that don't need to be saved
        for (String tag : TAGS_TO_REMOVE) {
            this.data.remove(tag);
        }
        this.data.remove("UUID");
        this.data.remove("LoveCause");
    }

    @Nullable
    public PigeonEntity respawn(ServerWorld worldIn, PlayerEntity playerIn, BlockPos pos) {
        PigeonEntity pigeon = ModEntityTypes.PIGEON.get().spawn(worldIn, null, null, playerIn, pos, SpawnReason.TRIGGERED, true, false);
        // Failed for some reason
        if (pigeon == null) {
            return null;
        }
        CompoundNBT compoundnbt = pigeon.writeWithoutTypeId(new CompoundNBT());
        UUID uuid = pigeon.getUniqueID();
        compoundnbt.merge(this.data);
        pigeon.setUniqueId(uuid);
        pigeon.read(compoundnbt);

        return pigeon;
    }
    public void read(CompoundNBT compound) {this.data = compound.getCompound("data");}
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("data", this.data);
        return compound;
    }
}