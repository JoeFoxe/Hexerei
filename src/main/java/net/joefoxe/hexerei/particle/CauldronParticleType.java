package net.joefoxe.hexerei.particle;

import com.mojang.serialization.Codec;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;

/**
 * Created by TGG on 25/03/2020.
 * Simple class used to describe the Particle
 */
public class CauldronParticleType extends ParticleType<CauldronParticleData> {
    private static boolean ALWAYS_SHOW_REGARDLESS_OF_DISTANCE_FROM_PLAYER = false;
    public CauldronParticleType() {
        super(ALWAYS_SHOW_REGARDLESS_OF_DISTANCE_FROM_PLAYER, CauldronParticleData.DESERIALIZER);
    }

    // get the Codec used to
    // a) convert a FlameParticleData to a serialised format
    // b) construct a FlameParticleData object from the serialised format
    public Codec<CauldronParticleData> func_230522_e_() {
        return CauldronParticleData.CODEC;
    }
}