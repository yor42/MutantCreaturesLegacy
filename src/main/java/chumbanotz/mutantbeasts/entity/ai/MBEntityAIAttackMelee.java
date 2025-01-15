package chumbanotz.mutantbeasts.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class MBEntityAIAttackMelee extends EntityAIAttackMelee {
    private final double moveSpeed;
    private int maxAttackTick = 20;
    private int delayCounter;

    public MBEntityAIAttackMelee(EntityCreature entityCreature, double moveSpeed) {
        super(entityCreature, moveSpeed, true);
        this.moveSpeed = moveSpeed;
    }

    public boolean shouldExecute() {
        this.attackTick = Math.max(this.attackTick - 1, 0);
        if (this.attacker.getAttackTarget() == null)
            return false;
        if (!this.attacker.getAttackTarget().isEntityAlive()) {
            this.attacker.setAttackTarget(null);
            return false;
        }
        return true;
    }

    public void startExecuting() {
        this.delayCounter = 0;
    }

    public void updateTask() {
        EntityLivingBase target = this.attacker.getAttackTarget();
        if (target == null)
            return;
        this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        double distSq = this.attacker.getDistanceSq(target.posX, (target.getEntityBoundingBox()).minY, target.posZ);
        if (--this.delayCounter <= 0) {
            this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
            float followRange = this.attacker.getNavigator().getPathSearchRange();
            if (distSq > (followRange * followRange)) {
                if (!this.attacker.hasPath()) {
                    Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.attacker, 16, 7, target.getPositionVector());
                    if (vec3d == null || !this.attacker.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, this.moveSpeed))
                        this.delayCounter += 5;
                }
            } else {
                this.attacker.getNavigator().tryMoveToEntityLiving(target, this.moveSpeed);
            }
        }
        this.attackTick = Math.max(this.attackTick - 1, 0);
        checkAndPerformAttack(target, distSq);
    }

    protected void checkAndPerformAttack(EntityLivingBase enemy, double distToEnemySqr) {
        if ((distToEnemySqr <= getAttackReachSqr(enemy) || this.attacker.getEntityBoundingBox().intersects(enemy.getEntityBoundingBox())) && this.attackTick <= 0) {
            this.attackTick = this.maxAttackTick;
            this.attacker.attackEntityAsMob(enemy);
        }
    }

    public void resetTask() {
        this.attacker.getNavigator().clearPath();
    }

    public MBEntityAIAttackMelee setMaxAttackTick(int max) {
        this.maxAttackTick = max;
        return this;
    }
}
