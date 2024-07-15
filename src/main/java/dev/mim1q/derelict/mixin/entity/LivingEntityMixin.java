package dev.mim1q.derelict.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.mim1q.derelict.config.DerelictConfigs;
import dev.mim1q.derelict.init.ModEntities;
import dev.mim1q.derelict.init.ModStatusEffects;
import dev.mim1q.derelict.init.component.ModCardinalComponents;
import dev.mim1q.derelict.tag.ModEntityTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.lang.Math.min;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        method = "onDeath",
        at = @At("HEAD")
    )
    private void derelict$onDeath(DamageSource source, CallbackInfo ci) {
        if (this.getType().isIn(ModEntityTags.INSTANCE.getSPAWNS_SPIDERLINGS_ON_DEATH())
            && !getWorld().isClient
            && random.nextFloat() < DerelictConfigs.CONFIG.spiderlingSpawnChance() / 100f
        ) {
            var spiderling = ModEntities.INSTANCE.getSPIDERLING().create(getWorld());
            if (spiderling == null) return;
            spiderling.refreshPositionAndAngles(getX(), getY(), getZ(), random.nextFloat() * 360, 0);
            spiderling.initialize(
                (ServerWorldAccess) getWorld(),
                getWorld().getLocalDifficulty(getBlockPos()),
                SpawnReason.EVENT,
                null,
                null
            );
            getWorld().spawnEntity(spiderling);
        }
    }

    @WrapOperation(
        method = "applyMovementInput",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"
        )
    )
    private void derelict$cancelMovement(LivingEntity instance, float v, Vec3d movementInput, Operation<Void> original) {
        if (ModCardinalComponents.INSTANCE.hasClientSyncedEffect((LivingEntity) (Object) this, ModStatusEffects.INSTANCE.getCOBWEBBED())) {
            original.call(instance, v, new Vec3d(0.0, min(0.0, movementInput.y), 0.0));
            return;
        }
        original.call(instance, v, movementInput);
    }
}