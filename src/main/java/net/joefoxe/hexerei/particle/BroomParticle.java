package net.joefoxe.hexerei.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.block.custom.MixingCauldron;
import net.joefoxe.hexerei.state.properties.LiquidType;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.ICollisionReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class BroomParticle extends SpriteTexturedParticle {
    protected float scale;
    protected float rotationDir;
    protected float fallingSpeed;

    public BroomParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.particleAngle = new Random().nextFloat() * (float)Math.PI;
        this.prevParticleAngle = this.particleAngle;
        this.rotationDir = new Random().nextFloat() - 0.5f;
        this.fallingSpeed = new Random().nextFloat();

        setScale(0.2F);
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.setSize(scale * 0.5f, scale * 0.5f);
    }

    @Override
    public void tick() {

        this.prevParticleAngle = this.particleAngle;
        if(Math.abs(this.motionY) > 0 && this.posY != this.prevPosY)
            this.particleAngle += 0.3f * rotationDir;
        this.motionY -= 0.005f * fallingSpeed;

        super.tick();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Nullable
        @Override
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Random rand = new Random();
            float colorOffset = (rand.nextFloat() * 0.4f);
            BroomParticle broomParticle = new BroomParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            broomParticle.selectSpriteRandomly(this.spriteSet);
            broomParticle.setColor(0.6f + colorOffset,0.6f + colorOffset,0.6f + colorOffset);
            if(this.spriteSet.get(0,1).getName().getPath().toString().matches("particle/broom_particle_4") ||
                  this.spriteSet.get(0,1).getName().getPath().toString().matches("particle/broom_particle_5") ||
                      this.spriteSet.get(0,1).getName().getPath().toString().matches("particle/broom_particle_6")) {
                broomParticle.maxAge += broomParticle.maxAge * 3 + 30;
            }

            return broomParticle;
        }
    }
}
