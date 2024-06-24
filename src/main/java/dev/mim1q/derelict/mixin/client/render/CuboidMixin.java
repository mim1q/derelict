package dev.mim1q.derelict.mixin.client.render;

import dev.mim1q.derelict.client.render.effect.WrappingVertexConsumer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;

import static net.minecraft.util.math.MathHelper.EPSILON;

@Mixin(ModelPart.Cuboid.class)
public class CuboidMixin {
    @Unique
    // @formatter:off
    private static final Map<Vector3f, float[][]> derelict$OFFSETS = Map.of(
        Direction.DOWN.getUnitVector(),  new float[][]{{ 1, -1,  1}, {-1, -1,  1}, {-1, -1, -1}, { 1, -1, -1}},
        Direction.UP.getUnitVector(),    new float[][]{{ 1,  1, -1}, {-1,  1, -1}, {-1,  1,  1}, { 1,  1,  1}},
        Direction.WEST.getUnitVector(),  new float[][]{{-1, -1, -1}, {-1, -1,  1}, {-1,  1,  1}, {-1,  1, -1}},
        Direction.NORTH.getUnitVector(), new float[][]{{ 1, -1, -1}, {-1, -1, -1}, {-1,  1, -1}, { 1,  1, -1}},
        Direction.EAST.getUnitVector(),  new float[][]{{ 1, -1,  1}, { 1, -1, -1}, { 1,  1, -1}, { 1,  1,  1}},
        Direction.SOUTH.getUnitVector(), new float[][]{{-1, -1,  1}, { 1, -1,  1}, { 1,  1,  1}, {-1,  1,  1}}
    );
    // @formatter:on

    @Unique
    private static final float[][] derelict$NO_OFFSETS = new float[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

    @Unique
    private static final float derelict$OVERLAY_TEXTURE_SIZE = 64f;

    @Shadow
    @Final
    private ModelPart.Quad[] sides;
    @Unique
    private boolean derelict$renderWhenWrapping = true;
    @Unique
    private float derelict$textureScaleHorizontal = 1.0f;
    @Unique
    private float derelict$textureScaleVertical = 1.0f;

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
        if (sizeX <= EPSILON || sizeY <= EPSILON || sizeZ <= EPSILON || set.isEmpty()) {
            derelict$renderWhenWrapping = false;
        }
        derelict$textureScaleHorizontal = textureWidth / derelict$OVERLAY_TEXTURE_SIZE;
        derelict$textureScaleVertical = textureHeight / derelict$OVERLAY_TEXTURE_SIZE;
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
        if (vertexConsumer instanceof WrappingVertexConsumer wrappingVertexConsumer) {
            if (!derelict$renderWhenWrapping && wrappingVertexConsumer.getSkipPlanes()) {
                ci.cancel();
                return;
            }

            final var posMatrix = entry.getPositionMatrix();
            final var normalMatrix = entry.getNormalMatrix();
            final var offsetScalar = wrappingVertexConsumer.getOffset();

            for (final var quad : this.sides) {
                final var normalVector = normalMatrix.transform(new Vector3f(quad.direction));
                final var offset = derelict$OFFSETS.getOrDefault(quad.direction, derelict$NO_OFFSETS);
                for (var i = 0; i < 4; ++i) {
                    final var vertex = quad.vertices[i];
                    final var vertexOffsets = offset[i];

                    final var x = (vertex.pos.x() + vertexOffsets[0] * offsetScalar) / 16.0f;
                    final var y = (vertex.pos.y() + vertexOffsets[1] * offsetScalar) / 16.0f;
                    final var z = (vertex.pos.z() + vertexOffsets[2] * offsetScalar) / 16.0f;
                    final var posVector = posMatrix.transform(new Vector4f(x, y, z, 1.0f));
                    vertexConsumer.vertex(
                        posVector.x(), posVector.y(), posVector.z(),
                        red, green, blue, alpha,
                        vertex.u * derelict$textureScaleHorizontal, vertex.v * derelict$textureScaleVertical,
                        overlay, light,
                        normalVector.x, normalVector.y, normalVector.z
                    );
                }
            }
            ci.cancel();
        }
    }
}