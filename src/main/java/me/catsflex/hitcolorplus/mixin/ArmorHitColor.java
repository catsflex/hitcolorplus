package me.catsflex.hitcolorplus.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.catsflex.hitcolorplus.config.ModConfiguration;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
public class ArmorHitColor {
	
	// Rendering is multithreaded nowadays
	@Unique
	private final ThreadLocal<Boolean> isTakingDamage = ThreadLocal.withInitial(() -> false);
	
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
		int k,
		CallbackInfo ci
	) {
		// Capture whether the entity has a "red" overlay
		isTakingDamage.set(object instanceof LivingEntityRenderState state && state.hasRedOverlay);
	}
	
	@Redirect(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;NO_OVERLAY:I",
			opcode = Opcodes.GETSTATIC)
	)
	private int redirectArmorOverlay() {
		var config = ModConfiguration.getInstance();
		
		// 0 and 3 are the default coordinates (??) for OverlayTexture, which is affected by EntityHitColor mixin
		return config.isEnabled && config.shouldColorArmor && isTakingDamage.get() ?
			OverlayTexture.pack(0, 3) :
			OverlayTexture.NO_OVERLAY;
	}
	
	@Redirect(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;armorCutoutNoCull(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"
		)
	)
	private RenderType changeRenderTypeToSupportOverlay(Identifier identifier) {
		var config = ModConfiguration.getInstance();
		
		// Replace armor shader with entity shader during "red" overlay
		return config.isEnabled && config.shouldColorArmor && isTakingDamage.get() ?
			RenderTypes.entityCutoutNoCull(identifier) :
			RenderTypes.armorCutoutNoCull(identifier);
	}
}
