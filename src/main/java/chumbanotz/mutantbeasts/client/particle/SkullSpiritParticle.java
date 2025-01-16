package chumbanotz.mutantbeasts.client.particle;

import chumbanotz.mutantbeasts.client.ClientEventHandler;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class SkullSpiritParticle
extends Particle {
    private float skullScale;

    private SkullSpiritParticle(World world, double x, double y, double z, double xx, double yy, double zz) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.motionX *= (double)0.1f;
        this.motionY *= (double)0.1f;
        this.motionZ *= (double)0.1f;
        this.motionX += xx;
        this.motionY += yy;
        this.motionZ += zz;
        this.particleGreen = this.particleBlue = 1.0f - (float)(Math.random() * 0.2);
        this.particleRed = this.particleBlue;
        this.particleScale *= 1.0f;
        float scale = 0.4f + this.rand.nextFloat() * 0.6f;
        this.particleScale *= scale;
        this.skullScale = this.particleScale;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.particleMaxAge = (int)((float)this.particleMaxAge * scale);
        this.canCollide = false;
        this.setParticleTexture(ClientEventHandler.SKULL_SPIRIT_PARTICLE_SPIRTE);
    }

    public int getFXLayer() {
        return 1;
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float timeScale = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge * 32.0f;
        if (timeScale < 0.0f) {
            timeScale = 0.0f;
        }
        if (timeScale > 1.0f) {
            timeScale = 1.0f;
        }
        this.particleScale = this.skullScale * timeScale;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.isExpired = true;
        }
        this.motionY += 0.002;
        this.move(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
        }
        this.motionX *= (double)0.96f;
        this.motionY *= (double)0.96f;
        this.motionZ *= (double)0.96f;
    }

    public static class Factory
    implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
            return new SkullSpiritParticle(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
