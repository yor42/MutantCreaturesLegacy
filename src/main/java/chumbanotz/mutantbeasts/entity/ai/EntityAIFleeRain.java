package chumbanotz.mutantbeasts.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityAIFleeRain extends EntityAIBase {
    private final EntityCreature creature;
    private final double movementSpeed;
    private double shelterX;
    private double shelterY;
    private double shelterZ;

    public EntityAIFleeRain(EntityCreature creature, double movementSpeedIn) {
        this.creature = creature;
        this.movementSpeed = movementSpeedIn;
        setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (this.creature.getAttackTarget() != null)
            return false;
        if (!this.creature.world.isRainingAt(new BlockPos(this.creature.posX, (this.creature.getEntityBoundingBox()).minY, this.creature.posZ)))
            return false;
        Vec3d vec3d = findPossibleShelter();
        if (vec3d == null)
            return false;
        this.shelterX = vec3d.x;
        this.shelterY = vec3d.y;
        this.shelterZ = vec3d.z;
        return true;
    }

    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath();
    }

    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    @Nullable
    private Vec3d findPossibleShelter() {
        Random random = this.creature.getRNG();
        BlockPos blockPos = new BlockPos(this.creature.posX, (this.creature.getEntityBoundingBox()).minY, this.creature.posZ);
        for (int i = 0; i < 10; i++) {
            BlockPos blockPos1 = blockPos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (!this.creature.world.isRainingAt(blockPos1) && !this.creature.world.getBlockState(blockPos1).getMaterial().isLiquid())
                return new Vec3d(blockPos1.getX(), blockPos1.getY(), blockPos1.getZ());
        }
        return null;
    }
}
