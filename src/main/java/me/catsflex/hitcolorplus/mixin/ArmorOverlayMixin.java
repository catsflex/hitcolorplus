package me.catsflex.hitcolorplus.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.catsflex.hitcolorplus.config.ModConfig;
import me.catsflex.hitcolorplus.util.HitOverlayState;
import me.catsflex.hitcolorplus.util.OverlayCoords;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EquipmentLayerRenderer.class)
public abstract class ArmorOverlayMixin {
	
	@WrapOperation(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;NO_OVERLAY:I",
			opcode = Opcodes.GETSTATIC
		)
	)
	private int addArmorOverlay(Operation<Integer> original) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get() || !config.shouldColorArmor.get() || !HitOverlayState.get()) {
			return original.call();
		}
		
		return OverlayCoords.ARMOR_OVERLAY;
	}
	
	@WrapOperation(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;armorCutoutNoCull(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"
		)
	)
	private RenderType changeArmorRenderType(Identifier identifier, Operation<RenderType> original) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get() || !config.shouldColorArmor.get()) {
			return original.call(identifier);
		}
		
		// Swap with the entities' render type to accept colors.
		// Use the version with z-offset for proper
		// enchantment glint and armor trim render handling.
		return RenderTypes.entityCutoutNoCullZOffset(identifier);
	}
	
	@WrapOperation(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/Sheets;armorTrimsSheet(Z)Lnet/minecraft/client/renderer/rendertype/RenderType;"
		)
	)
	private RenderType changeArmorTrimRenderType(boolean decal, Operation<RenderType> original) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get() || !config.shouldColorArmor.get()) {
			return original.call(decal);
		}
		
		return RenderTypes.entityCutoutNoCullZOffset(Sheets.ARMOR_TRIMS_SHEET);
	}
}
