package dev.mim1q.derelict.mixin.entity;

import dev.mim1q.derelict.Derelict;
import dev.mim1q.derelict.init.ModEntities;
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

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        method = "onDeath",
        at = @At("HEAD")
    )
    private void onDeath(DamageSource source, CallbackInfo ci) {
        if (this.getType().isIn(ModEntityTags.INSTANCE.getSPAWNS_SPIDERLINGS_ON_DEATH())
            && !getWorld().isClient
            && random.nextFloat() < Derelict.INSTANCE.getCONFIG().spiderlingSpawnChance()
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
}