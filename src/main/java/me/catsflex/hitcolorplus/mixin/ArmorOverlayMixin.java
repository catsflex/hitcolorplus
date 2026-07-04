package me.catsflex.hitcolorplus.mixin;

import me.catsflex.hitcolorplus.config.ModConfig;
import me.catsflex.hitcolorplus.util.OverlayStateTracker;
import me.catsflex.hitcolorplus.util.OverlayUtil;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EquipmentLayerRenderer.class)
public abstract class ArmorOverlayMixin {
	
	@Redirect(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;NO_OVERLAY:I",
			opcode = Opcodes.GETSTATIC
		)
	)
	private int changeArmorOverlay() {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get() || !config.shouldColorArmor.get() || !OverlayStateTracker.get()) {
			return OverlayUtil.NO_OVERLAY;
		}
		
		return OverlayUtil.ARMOR_OVERLAY;
	}
	
	@Redirect(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;armorCutoutNoCull(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"
		)
	)
	private RenderType changeArmorRenderType(Identifier identifier) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get() || !config.shouldColorArmor.get()) {
			return RenderTypes.armorCutoutNoCull(identifier);
		}
		
		// Swap with the entities' render type to accept colors.
		return RenderTypes.entityCutoutNoCull(identifier);
	}
}
