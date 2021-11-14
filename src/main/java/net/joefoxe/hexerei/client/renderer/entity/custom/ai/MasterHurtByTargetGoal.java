package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.util.EnumMode;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;

public class MasterHurtByTargetGoal extends OwnerHurtByTargetGoal {
    private PigeonEntity pigeon;

    public MasterHurtByTargetGoal(PigeonEntity pigeonIn) {
        super(pigeonIn);
        this.pigeon = pigeonIn;
    }

    @Override
    public boolean shouldExecute() {
        return this.pigeon.isMode(EnumMode.AGGRESIVE) && super.shouldExecute();
    }
}