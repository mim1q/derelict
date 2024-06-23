package dev.mim1q.derelict.mixin.client.render;

import dev.mim1q.derelict.client.render.effect.OutlineCuboid;
import dev.mim1q.derelict.client.render.effect.WrappingVertexConsumer;
import dev.mim1q.derelict.interfaces.CuboidAccessor;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ModelPart.Cuboid.class)
public class CuboidMixin implements CuboidAccessor {
    @Unique
    private ModelPart.Cuboid derelict$outlineCuboid = null;

    @Inject(
        method = "<init>",
        at = @At("RETURN")
    )
    private void derelict$init(
        int u, int v,
        float x, float y, float z,
        float sizeX, float sizeY, float sizeZ,
        float extraX, float extraY, float extraZ,
        boolean mirror,
        float textureWidth, float textureHeight,
        Set<Direction> set,
        CallbackInfo ci
    ) {
        if (sizeX <= 0e-6f || sizeY <= 0e-6f || sizeZ <= 0e-6f || set.isEmpty()) {
            return;
        }
        if (derelict$hasOutlineCuboid()) {
            x += extraX;
            y += extraY;
            z += extraZ;

            if (mirror) x -= sizeX;

            this.derelict$outlineCuboid = new OutlineCuboid(
                u, v,
                x, y, z,
                sizeX, sizeY, sizeZ,
                extraX + 0.1f, extraY + 0.1f, extraZ + 0.1f,
                false,
                textureWidth, textureHeight,
                set
            );
        }
    }

    @Inject(
        method = "renderCuboid",
        at = @At("HEAD"),
        cancellable = true
    )
    private void derelict$render(
        MatrixStack.Entry entry,
        VertexConsumer vertexConsumer,
        int light,
        int overlay,
        float red, float green, float blue, float alpha,
        CallbackInfo ci
    ) {
        if (vertexConsumer instanceof WrappingVertexConsumer) {
            if (derelict$hasOutlineCuboid() && derelict$outlineCuboid != null) {
                this.derelict$outlineCuboid.renderCuboid(entry, vertexConsumer, light, overlay, red, green, blue, alpha);
            ci.cancel();
            }
        }
    }

    @Override
    public boolean derelict$hasOutlineCuboid() {
        return true;
    }
}