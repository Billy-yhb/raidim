package cavenightingale.raidim.client.renderer;

import cavenightingale.raidim.RaidImMod;
import cavenightingale.raidim.entity.MinerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class MinerEntityRenderer extends IllagerEntityRenderer<MinerEntity> {
	public static final Identifier SKIN = RaidImMod.prefix("textures/entity/miner/miner.png");

	public MinerEntityRenderer(EntityRenderDispatcher entityRenderDispatcher_1) {
		super(entityRenderDispatcher_1,
				new IllagerEntityModel<MinerEntity>(0f, 0f, 64, 64),  0.5f);
		this.addFeature(new HeldItemFeatureRenderer<MinerEntity,
						IllagerEntityModel<MinerEntity>>(this){
			@Override
			public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, MinerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				if(entity.shouldShowPickaxe())
					super.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
			}
		});
		((IllagerEntityModel<?>)this.model).method_2812().visible = true;
	}

	@Override
	public Identifier getTexture(MinerEntity var1) {
		return SKIN;
	}
}