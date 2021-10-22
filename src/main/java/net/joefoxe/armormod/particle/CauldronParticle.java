package net.joefoxe.armormod.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.joefoxe.armormod.block.ModBlocks;
import net.joefoxe.armormod.block.custom.MixingCauldron;
import net.joefoxe.armormod.state.properties.LiquidType;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
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
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class CauldronParticle extends SpriteTexturedParticle {

    /*
     * Just like all other SpriteTexturedParticles this class is more or less copied, take a look at existing
     * particles and add change some numbers around until you get what you need!
     */
    public static final Vector3d[] CUBE = {
            // TOP
            new Vector3d(0.5, 0.1, -0.5), new Vector3d(0.5, 0.1, 0.5), new Vector3d(-0.5, 0.1, 0.5), new Vector3d(-0.5, 0.1, -0.5),

            // BOTTOM
            new Vector3d(-0.5, -0.1, -0.5), new Vector3d(-0.5, -0.1, 0.5), new Vector3d(0.5, -0.1, 0.5), new Vector3d(0.5, -0.1, -0.5),

            // FRONT
            new Vector3d(-0.5, -0.1, 0.5), new Vector3d(-0.5, 0.1, 0.5), new Vector3d(0.5, 0.1, 0.5), new Vector3d(0.5, -0.1, 0.5),

            // BACK
            new Vector3d(0.5, -0.1, -0.5), new Vector3d(0.5, 0.1, -0.5), new Vector3d(-0.5, 0.1, -0.5), new Vector3d(-0.5, -0.1, -0.5),

            // LEFT
            new Vector3d(-0.5, -0.1, -0.5), new Vector3d(-0.5, 0.1, -0.5), new Vector3d(-0.5, 0.1, 0.5), new Vector3d(-0.5, -0.1, 0.5),

            // RIGHT
            new Vector3d(0.5, -0.1, 0.5), new Vector3d(0.5, 0.1, 0.5), new Vector3d(0.5, 0.1, -0.5), new Vector3d(0.5, -0.1, -0.5) };

    public static final Vector3d[] CUBE_NORMALS = {
            // modified normals for the sides
            new Vector3d(0, 0.1, 0), new Vector3d(0, -0.5, 0), new Vector3d(0, 0, 0.5), new Vector3d(0, 0, 0.5), new Vector3d(0, 0, 0.5),
            new Vector3d(0, 0, 0.5),

            /*
             * new Vector3d(0, 1, 0), new Vector3d(0, -1, 0), new Vector3d(0, 0, 1), new Vector3d(0, 0,
             * -1), new Vector3d(-1, 0, 0), new Vector3d(1, 0, 0)
             */
    };

    private static final IParticleRenderType renderType = new IParticleRenderType() {
        @Override
        public void beginRender(BufferBuilder bufferBuilder, TextureManager textureManager) {
            RenderSystem.disableTexture();

            // transparent, additive blending
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            RenderSystem.enableLighting();
            RenderSystem.enableColorMaterial();

            // opaque
//			RenderSystem.depthMask(true);
//			RenderSystem.disableBlend();
//			RenderSystem.enableLighting();

            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        }

        @Override
        public void finishRender(Tessellator tesselator) {

            tesselator.draw();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.disableLighting();
            RenderSystem.enableTexture();
        }

    };

    protected float scale;
    protected boolean hot;
    protected float rotationDirection;
    protected float rotation;


    public CauldronParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.rotation = 0;

        Random random = new Random();
        setScale(0.2F);
        setRotationDirection(random.nextFloat() - 0.5f);
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.setSize(scale * 0.5f, scale * 0.5f);
    }

    public void averageAge(int age) {
        Random random = new Random();
        this.maxAge = (int) (age + (random.nextDouble() * 2D - 1D) * 8);
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public void setRotationDirection(float rotationDirection) {
        this.rotationDirection = rotationDirection;
    }


    private boolean billowing = false;

    @Override
    public void tick() {
        if (this.hot && this.age > 0) {
            if (this.prevPosY == this.posY) {
                billowing = true;
                this.canCollide = false; // Prevent motion being ignored due to vertical collision
                if (this.motionX == 0 && this.motionZ == 0) {
                    Vector3d diff = Vector3d.copyCenteredHorizontally(new BlockPos(this.posX, this.posY, this.posZ)).add(0.5, 0.5, 0.5).subtract(this.posX, this.posY, this.posZ);
                    this.motionX = -diff.x * 0.1;
                    this.motionZ = -diff.z * 0.1;
                }
                this.motionX *= 1.1;
                this.motionY *= 0.9;
                this.motionZ *= 1.1;
            } else if (billowing) {
                this.motionY *= 1.2;
            }
        }
        this.rotation = (this.rotationDirection * 0.1f) + this.rotation;



        super.tick();
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        Vector3d projectedView = renderInfo.getProjectedView();
        float lerpedX = (float) (MathHelper.lerp(partialTicks, this.prevPosX, this.posX) - projectedView.getX());
        float lerpedY = (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.posY) - projectedView.getY());
        float lerpedZ = (float) (MathHelper.lerp(partialTicks, this.prevPosZ, this.posZ) - projectedView.getZ());

        // int light = getBrightnessForRender(p_225606_3_);
        int light = 15728880;// 15<<20 && 15<<4
        double ageMultiplier = 1 - Math.pow(age, 3) / Math.pow(this.maxAge, 3);

        for (int i = 0; i < 6; i++) {
            // 6 faces to a cube
            for (int j = 0; j < 4; j++) {
                Vector3d vec = CUBE[i * 4 + j];
                vec = vec
                        .rotateYaw(this.rotation)
                        .scale(scale * ageMultiplier)
                        //.mul(1, 0.25 + 0.55 * (age/4f), 1)
                        .add(lerpedX, lerpedY, lerpedZ);

                Vector3d normal = CUBE_NORMALS[i];
                buffer.addVertex((float) vec.x, (float) vec.y, (float) vec.z, this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha, 0, 0, 0, light,(float) normal.x, (float) normal.y, (float) normal.z);


            }
        }
    }



    @Override
    public IParticleRenderType getRenderType() {
        return renderType;
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
            CauldronParticle cauldronParticle = new CauldronParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            Random random = new Random();

            Color color = new Color(BiomeColors.getWaterColor(worldIn, new BlockPos(x, y, z)));

            BlockState blockStateAtPos = worldIn.getBlockState(new BlockPos(x, y-0.5, z));

            if(blockStateAtPos.getBlock() == ModBlocks.MIXING_CAULDRON.get()){
                if(blockStateAtPos.get(MixingCauldron.FLUID) == LiquidType.WATER) {
                    float colorOffset = (random.nextFloat() * 0.1f);
                    cauldronParticle.setColor(color.getRed()/450f + colorOffset,color.getGreen()/450f + colorOffset,color.getBlue()/450f + colorOffset);}
                if(blockStateAtPos.get(MixingCauldron.FLUID) == LiquidType.MILK) {
                    float colorOffset = (random.nextFloat() * 0.05f);
                    cauldronParticle.setColor(0.85f + colorOffset, 0.85f + colorOffset, 0.85f + colorOffset);}
                if(blockStateAtPos.get(MixingCauldron.FLUID) == LiquidType.LAVA) {
                    cauldronParticle.setColor(0.8f + (random.nextFloat() * 0.1f), 0.24f + (random.nextFloat() * 0.5f), (random.nextFloat() * 0.3f));}
                if(blockStateAtPos.get(MixingCauldron.FLUID) == LiquidType.QUICKSILVER) {
                    float colorOffset = (random.nextFloat() * 0.15f);
                    cauldronParticle.setColor(0.12f + colorOffset, 0.12f + colorOffset, 0.12f + colorOffset);
                }
            }// .24 - .72


            cauldronParticle.setAlphaF(2.0f);


            cauldronParticle.selectSpriteRandomly(this.spriteSet);
            return cauldronParticle;
        }
    }


}
