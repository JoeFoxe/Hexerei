package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.util.EnumMode;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;

public class MasterHurtTargetGoal extends OwnerHurtTargetGoal {
    private PigeonEntity pigeon;

    public MasterHurtTargetGoal(PigeonEntity pigeonIn) {
        super(pigeonIn);
        this.pigeon = pigeonIn;
    }

    @Override
    public boolean shouldExecute() {
        return this.pigeon.isMode(EnumMode.AGGRESIVE) && super.shouldExecute();
    }
}