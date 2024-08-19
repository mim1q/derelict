package dev.mim1q.derelict.mixin.entity;

import dev.mim1q.derelict.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {
    @Inject(
        method = "isValidSpawn",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void derelict$isValidSpawn(ServerWorld world, MobEntity entity, double squaredDistance, CallbackInfoReturnable<Boolean> cir) {
        if (ModEntities.INSTANCE.getSPAWN_ON_GROUND().contains(entity.getType())) {
            //noinspection unchecked
            cir.setReturnValue(cir.getReturnValue() && MobEntity.canMobSpawn(
                (EntityType<MobEntity>) entity.getType(),
                world,
                SpawnReason.NATURAL,
                entity.getBlockPos(),
                world.random)
            );
        }
    }
}
