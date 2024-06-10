package dev.mim1q.derelict.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.mim1q.derelict.entity.SpiderlingEntity;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Bucketable.class)
public interface BucketableMixin {
    @WrapOperation(
        method = "tryBucket",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;",
            ordinal = 0
        )
    )
    private static <T extends LivingEntity & Bucketable> Item derelict$modifyItemInTryBucket(
        ItemStack instance,
        Operation<Item> original,
        PlayerEntity playerEntity,
        Hand hand,
        T entity
    ) {
        if (entity instanceof SpiderlingEntity) {
            if (instance.isOf(Items.WATER_BUCKET)) return Items.AIR;
            if (instance.isOf(Items.BUCKET)) return Items.WATER_BUCKET;
        }
        return original.call(instance);
    }
}
