package dev.mim1q.derelict.mixin.block;

import dev.mim1q.derelict.block.cobweb.FancyCobwebBlock;
import dev.mim1q.derelict.interfaces.AbstractBlockAccessor;
import dev.mim1q.derelict.tag.ModBlockTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin implements AbstractBlockAccessor {
    @Unique
    private boolean waxable = false;
    @Unique
    private boolean ageable = false;

    @Override
    public boolean isWaxable() {
        return waxable;
    }

    @Override
    public void setWaxable(boolean value) {
        waxable = value;
    }

    @Override
    public boolean isAgeable() {
        return ageable;
    }

    @Override
    public void setAgeable(boolean value) {
        ageable = value;
    }

    @Inject(
        method = "getCollisionShape",
        at = @At("HEAD"),
        cancellable = true
    )
    private void derelict$getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (state.isIn(ModBlockTags.INSTANCE.getCOBWEBS())
            && FancyCobwebBlock.Companion.shouldBeSolid(pos, context)
        ) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }
}
