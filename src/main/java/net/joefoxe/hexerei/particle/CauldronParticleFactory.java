package net.joefoxe.hexerei.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;


@OnlyIn(Dist.CLIENT)
public class CauldronParticleFactory implements IParticleFactory<BasicParticleType> {
    private final IAnimatedSprite spriteSet;

    public CauldronParticleFactory(IAnimatedSprite sprite) {
        this.spriteSet = sprite;
    }

    @Nullable
    @Override
    public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        CauldronParticle cauldronParticle = new CauldronParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
        cauldronParticle.setColor(1.0f, 1.0f, 1.0f);
        cauldronParticle.selectSpriteRandomly(this.spriteSet);
        return cauldronParticle;
    }
}
/**
 * Created by TGG on 25/03/2020.
 *
 * On the client side:
 * When the client wants to spawn a Particle, it gives the CauldronParticleData to this factory method
 * The factory selects an appropriate Particle class and instantiates it
 *
 */
//public class CauldronParticleFactory implements IParticleFactory<CauldronParticleData> {  //IParticleFactory
//
//    @Nullable
//    @Override
//    public Particle makeParticle(CauldronParticleData cauldronParticleData, ClientWorld world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity) {
//        CauldronParticle newParticle = new CauldronParticle(world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity,
//                cauldronParticleData.getTint(), cauldronParticleData.getDiameter(),
//                sprites);
//        return newParticle;
//    }
//
//    private final IAnimatedSprite sprites;  // contains a list of textures; choose one using either
//    // newParticle.selectSpriteRandomly(sprites); or newParticle.selectSpriteWithAge(sprites);
//
//    // this method is needed for proper registration of your Factory:
//    // The ParticleManager.register method creates a Sprite and passes it to your factory for subsequent use when rendering, then
//    //   populates it with the textures from your textures/particle/xxx.json
//
//    public CauldronParticleFactory(IAnimatedSprite sprite) {
//        this.sprites = sprite;
//    }
//
//    // This is private to prevent you accidentally registering the Factory using the default constructor.
//    // ParticleManager has two register methods, and if you use the wrong one the game will enter an infinite loop
//    private CauldronParticleFactory() {
//        throw new UnsupportedOperationException("Use the CauldronParticleFactory(IAnimatedSprite sprite) constructor");
//    }
//
//}