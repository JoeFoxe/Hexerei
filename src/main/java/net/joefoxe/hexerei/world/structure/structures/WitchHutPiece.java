package net.joefoxe.hexerei.world.structure.structures;

import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class WitchHutPiece extends ScatteredStructurePiece {
    private boolean witch;
    private boolean field_214822_f;

    public WitchHutPiece(Random random, int x, int z) {
        super(IModStructurePieceType.WITCH_HUT_PIECE, random, x, 64, z, 7, 7, 9);
    }

    public WitchHutPiece(TemplateManager p_i51340_1_, CompoundNBT p_i51340_2_) {
        super(IModStructurePieceType.WITCH_HUT_PIECE, p_i51340_2_);
        this.witch = p_i51340_2_.getBoolean("Witch");
        this.field_214822_f = p_i51340_2_.getBoolean("Cat");
    }

    /**
     * (abstract) Helper method to read subclass data from NBT
     */
    protected void readAdditional(CompoundNBT tagCompound) {
        super.readAdditional(tagCompound);
        tagCompound.putBoolean("Witch", this.witch);
        tagCompound.putBoolean("Cat", this.field_214822_f);
    }

    public boolean func_230383_a_(ISeedReader p_230383_1_, StructureManager p_230383_2_, ChunkGenerator p_230383_3_, Random p_230383_4_, MutableBoundingBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
        if (!this.isInsideBounds(p_230383_1_, p_230383_5_, 0)) {
            return false;
        } else {
            if (!this.witch) {
                int l = this.getXWithOffset(2, 5);
                int i1 = this.getYWithOffset(2);
                int k = this.getZWithOffset(2, 5);
                if (p_230383_5_.isVecInside(new BlockPos(l, i1, k))) {
                    this.witch = true;
                    WitchEntity witchentity = EntityType.WITCH.create(p_230383_1_.getWorld());
                    witchentity.enablePersistence();
                    witchentity.setLocationAndAngles((double)l + 0.5D, (double)i1, (double)k + 0.5D, 0.0F, 0.0F);
                    witchentity.onInitialSpawn(p_230383_1_, p_230383_1_.getDifficultyForLocation(new BlockPos(l, i1, k)), SpawnReason.STRUCTURE, (ILivingEntityData)null, (CompoundNBT)null);
                    p_230383_1_.func_242417_l(witchentity);
                }
            }

            this.func_214821_a(p_230383_1_, p_230383_5_);
            return true;
        }
    }

    void replaceAirAndLiquidDownwardsNoCheck(ISeedReader worldIn, BlockState blockstateIn, int x, int y, int z, MutableBoundingBox boundingboxIn) {
        int i = this.getXWithOffset(x, z);
        int j = this.getYWithOffset(y);
        int k = this.getZWithOffset(x, z);
            while((worldIn.isAirBlock(new BlockPos(i, j, k)) || worldIn.getBlockState(new BlockPos(i, j, k)).getMaterial().isLiquid()) && j > 1) {
                worldIn.setBlockState(new BlockPos(i, j, k), blockstateIn, 2);
                --j;
            }

    }


    private void func_214821_a(IServerWorld p_214821_1_, MutableBoundingBox p_214821_2_) {
        if (!this.field_214822_f) {
            int i = this.getXWithOffset(2, 5);
            int j = this.getYWithOffset(2);
            int k = this.getZWithOffset(2, 5);
            if (p_214821_2_.isVecInside(new BlockPos(i, j, k))) {
                this.field_214822_f = true;
                CatEntity catentity = EntityType.CAT.create(p_214821_1_.getWorld());
                catentity.enablePersistence();
                catentity.setLocationAndAngles((double)i + 0.5D, (double)j, (double)k + 0.5D, 0.0F, 0.0F);
                catentity.onInitialSpawn(p_214821_1_, p_214821_1_.getDifficultyForLocation(new BlockPos(i, j, k)), SpawnReason.STRUCTURE, (ILivingEntityData)null, (CompoundNBT)null);
                p_214821_1_.func_242417_l(catentity);
            }
        }

    }
}