package chumbanotz.mutantbeasts.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;

public class EntityAIHurtByNearestTarget extends EntityAIHurtByTarget {
    public EntityAIHurtByNearestTarget(EntityCreature creature) {
        this(creature, false);
    }

    public EntityAIHurtByNearestTarget(EntityCreature creature, boolean entityCallsForHelp, Class<?>... excludedReinforcementTypes) {
        super(creature, entityCallsForHelp, excludedReinforcementTypes);
    }

    public boolean shouldExecute() {
        if (!super.shouldExecute()) {
            EntityLivingBase lastTarget = this.taskOwner.getLastAttackedEntity();
            if (lastTarget != null && this.taskOwner.getRevengeTarget() == null)
                this.taskOwner.setRevengeTarget(lastTarget);
            return false;
        }
        return true;
    }

    public boolean shouldContinueExecuting() {
        if (!super.shouldContinueExecuting())
            return false;
        EntityLivingBase revengeTarget = this.taskOwner.getRevengeTarget();
        if (super.shouldExecute() && revengeTarget != this.target && this.taskOwner.getDistanceSq(revengeTarget) < this.taskOwner.getDistanceSq(this.target)) {
            this.taskOwner.setLastAttackedEntity(this.target);
            return false;
        }
        return true;
    }

    protected void alertOthers() {
        if (this.taskOwner.getRevengeTarget() == null)
            return;
        super.alertOthers();
    }

    protected boolean isSuitableTarget(EntityLivingBase target, boolean includeInvincibles) {
        if (target instanceof net.minecraft.entity.boss.EntityWither && this.taskOwner.isEntityUndead())
            return false;
        return isSuitableTarget(this.taskOwner, target, includeInvincibles, true);
    }

    protected double getTargetDistance() {
        return super.getTargetDistance() * 2.0D;
    }
}
