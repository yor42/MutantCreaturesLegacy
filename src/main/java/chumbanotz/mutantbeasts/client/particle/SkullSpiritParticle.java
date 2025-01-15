package chumbanotz.mutantbeasts.client.particle;

import chumbanotz.mutantbeasts.client.ClientEventHandler;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class SkullSpiritParticle extends Particle {
    private final float skullScale;

    private SkullSpiritParticle(World world, double x, double y, double z, double xx, double yy, double zz) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += xx;
        this.motionY += yy;
        this.motionZ += zz;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F - (float) (Math.random() * 0.2D);
        this.particleScale *= 1.0F;
        float scale = 0.4F + this.rand.nextFloat() * 0.6F;
        this.particleScale *= scale;
        this.skullScale = this.particleScale;
        this.particleMaxAge = (int) (8.0D / (world.rand.nextDouble() * 0.8D + 0.2D));
        this.particleMaxAge = (int) (this.particleMaxAge * scale);
        this.canCollide = false;
        setParticleTexture(ClientEventHandler.SKULL_SPIRIT_PARTICLE_SPIRTE);
    }

    public int getFXLayer() {
        return 1;
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float timeScale = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;
        if (timeScale < 0.0F)
            timeScale = 0.0F;
        if (timeScale > 1.0F)
            timeScale = 1.0F;
        this.particleScale = this.skullScale * timeScale;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge)
            this.isExpired = true;
        this.motionY += 0.002D;
        move(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }
        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;
    }

    public static class Factory implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return new SkullSpiritParticle(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
