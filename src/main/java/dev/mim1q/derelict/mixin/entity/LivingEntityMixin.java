package dev.mim1q.derelict.mixin.entity;

import dev.mim1q.derelict.config.DerelictConfigs;
import dev.mim1q.derelict.init.ModEntities;
import dev.mim1q.derelict.init.ModStatusEffects;
import dev.mim1q.derelict.tag.ModEntityTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.mim1q.derelict.init.component.ModCardinalComponentsKt.hasDerelictStatusEffect;

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

    @Inject(
        method = "jump",
        at = @At("HEAD"),
        cancellable = true
    )
    private void derelict$cancelJump(CallbackInfo ci) {
        if (hasDerelictStatusEffect((LivingEntity) (Object) this, ModStatusEffects.INSTANCE.getCOBWEBBED())) {
            ci.cancel();
        }
    }
}