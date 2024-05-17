package dev.mim1q.derelict.mixin.item;

import dev.mim1q.derelict.block.tag.ModBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ShearsItem.class, SwordItem.class})
public class SwordAndShearsSuitableBlocksMixin {
    @Inject(
        method = "isSuitableFor",
        at = @At("HEAD"),
        cancellable = true
    )
    private void derelict$isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.isIn(ModBlockTags.INSTANCE.getCOBWEBS())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
        method = "getMiningSpeedMultiplier",
        at = @At("HEAD"),
        cancellable = true
    )
    private void derelict$getMiningSpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if (state.isIn(ModBlockTags.INSTANCE.getCOBWEBS())) {
            cir.setReturnValue(15f);
        }
    }
}
