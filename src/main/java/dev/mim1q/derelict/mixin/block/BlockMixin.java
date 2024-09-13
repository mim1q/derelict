package dev.mim1q.derelict.mixin.block;

import dev.mim1q.derelict.init.ModBlocksAndItems;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(
        method = "sideCoversSmallSquare",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void derelict$sideCoversSmallSquare(WorldView world, BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (side == Direction.DOWN && world.getBlockState(pos).isOf(ModBlocksAndItems.INSTANCE.getSPIDER_SILK_STRAND())) {
            cir.setReturnValue(true);
        }
    }
}
