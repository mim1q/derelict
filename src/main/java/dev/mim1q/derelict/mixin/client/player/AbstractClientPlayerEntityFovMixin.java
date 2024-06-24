package dev.mim1q.derelict.mixin.client.player;

import com.mojang.authlib.GameProfile;
import dev.mim1q.derelict.init.ModStatusEffects;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityFovMixin extends PlayerEntity {
    public AbstractClientPlayerEntityFovMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(
        method = "getFovMultiplier",
        at = @At("HEAD"),
        cancellable = true
    )
    private void derelict$modifyFovMultiplier(CallbackInfoReturnable<Float> cir) {
        // Make the webbed status effect not affect the FOV as much
        if (this.hasStatusEffect(ModStatusEffects.INSTANCE.getWEBBED())) cir.setReturnValue(0.8f);
    }
}
