package me.catsflex.hitcolorplus.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.catsflex.hitcolorplus.config.ModConfig;
import me.catsflex.hitcolorplus.util.OverlayUtil;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EquipmentLayerRenderer.class)
public abstract class EquipmentLayerRendererMixin {
	@Unique private static final ThreadLocal<Boolean> isTakingDamageNow = ThreadLocal.withInitial(() -> false);
	
	@Inject(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
		at = @At("HEAD")
	)
	private void captureState(
		EquipmentClientInfo.LayerType layerType,
		ResourceKey<?> resourceKey,
		Model<?> model,
		Object object,
		ItemStack itemStack,
		PoseStack poseStack,
		SubmitNodeCollector submitNodeCollector,
		int light,
		Identifier identifier,
		int outlineColor,
		int renderOrder,
		CallbackInfo ci
	) {
		// Capture whether the entity is getting damaged or not. 
		isTakingDamageNow.set(object instanceof LivingEntityRenderState state && state.hasRedOverlay);
	}
	
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
		if (!config.isEnabled.get() || !config.shouldColorArmor.get() || !isTakingDamageNow.get())
			return OverlayUtil.NO_OVERLAY;
		
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
		if (!config.isEnabled.get() || !config.shouldColorArmor.get())
			return RenderTypes.armorCutoutNoCull(identifier);
		
		// Swap with the entities' render type to accept colors.
		return RenderTypes.entityCutoutNoCull(identifier);
	}
}
