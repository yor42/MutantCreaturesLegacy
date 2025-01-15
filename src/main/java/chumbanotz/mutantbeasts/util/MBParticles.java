package chumbanotz.mutantbeasts.util;

import chumbanotz.mutantbeasts.config.MBConfig;
import chumbanotz.mutantbeasts.mutantbeasts.Tags;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Map;

public class MBParticles {
    public static final EnumParticleTypes ENDERSOUL = addEnumParticleType(Tags.MOD_ID + ":endersoul", MBConfig.endersoulParticleID, true);

    public static final EnumParticleTypes SKULL_SPIRIT = addEnumParticleType(Tags.MOD_ID + ":skull_spirit", MBConfig.skullSpiritParticleID, true);

    public static void register() {
        Map<Integer, EnumParticleTypes> PARTICLES = ObfuscationReflectionHelper.getPrivateValue(EnumParticleTypes.class, null, "field_179365_U");
        PARTICLES.put(ENDERSOUL.getParticleID(), ENDERSOUL);
        PARTICLES.put(SKULL_SPIRIT.getParticleID(), SKULL_SPIRIT);
        Map<String, EnumParticleTypes> BY_NAME = ObfuscationReflectionHelper.getPrivateValue(EnumParticleTypes.class, null, "field_186837_Z");
        BY_NAME.put(ENDERSOUL.getParticleName(), ENDERSOUL);
        BY_NAME.put(SKULL_SPIRIT.getParticleName(), SKULL_SPIRIT);
    }

    private static EnumParticleTypes addEnumParticleType(String particleNameIn, int particleIDIn, boolean shouldIgnoreRangeIn) {
        EnumParticleTypes enumParticleTypes = EnumParticleTypes.getParticleFromId(particleIDIn);
        if (enumParticleTypes != null)
            throw new IllegalStateException("The particle " + particleNameIn + " has the same ID as the particle " + enumParticleTypes.getParticleName() + ". Change the ID in your config!");
        return EnumHelper.addEnum(EnumParticleTypes.class, particleNameIn, new Class[]{String.class, int.class, boolean.class, int.class}, particleNameIn, particleIDIn, shouldIgnoreRangeIn, 0);
    }
}
