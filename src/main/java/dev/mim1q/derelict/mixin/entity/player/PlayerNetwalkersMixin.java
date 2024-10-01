package dev.mim1q.derelict.mixin.entity.player;

import dev.mim1q.derelict.init.ModBlocksAndItems;
import dev.mim1q.derelict.tag.ModBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerNetwalkersMixin {
    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Inject(
        method = "slowMovement",
        at = @At("HEAD"),
        cancellable = true
    )
    private void derelict$slowMovement(BlockState state, Vec3d multiplier, CallbackInfo ci) {
        if (state.isIn(ModBlockTags.INSTANCE.getCOBWEBS())
            && getEquippedStack(EquipmentSlot.LEGS).isOf(ModBlocksAndItems.INSTANCE.getNETWALKERS())
        ) {
            ci.cancel();
        }
    }
}
