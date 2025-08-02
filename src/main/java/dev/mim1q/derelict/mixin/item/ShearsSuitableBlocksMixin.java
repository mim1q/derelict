package dev.mim1q.derelict.mixin.item;

import dev.mim1q.derelict.tag.ModBlockTags;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.item.ShearsItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(ShearsItem.class)
public class ShearsSuitableBlocksMixin {
    @Inject(
        method = "createToolComponent",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void derelict$isSuitableFor(CallbackInfoReturnable<ToolComponent> cir) {
        var result = cir.getReturnValue();
        var newList = new ArrayList<>(result.rules());
        newList.add(ToolComponent.Rule.ofAlwaysDropping(ModBlockTags.INSTANCE.getCOBWEBS(), 15));
        cir.setReturnValue(new ToolComponent(newList, result.defaultMiningSpeed(), result.damagePerBlock()));
    }
}
