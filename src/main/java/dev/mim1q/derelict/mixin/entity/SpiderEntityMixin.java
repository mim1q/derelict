package dev.mim1q.derelict.mixin.entity;

import dev.mim1q.derelict.tag.ModBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpiderEntity.class)
public class SpiderEntityMixin {
    @Inject(
        method = "slowMovement",
        at = @At("HEAD"),
        cancellable = true
    )
    private void derelict$slowMovement(BlockState state, Vec3d multiplier, CallbackInfo ci) {
        if (state.isIn(ModBlockTags.INSTANCE.getCOBWEBS())) {
            ci.cancel();
        }
    }
}
