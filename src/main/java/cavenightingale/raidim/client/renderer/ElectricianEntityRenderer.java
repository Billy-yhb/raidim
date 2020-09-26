package cavenightingale.raidim.client.renderer;

import cavenightingale.raidim.RaidImMod;
import cavenightingale.raidim.entity.ElectricianEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.util.Identifier;

public class ElectricianEntityRenderer extends IllagerEntityRenderer<ElectricianEntity> {
	public static final Identifier SKIN = RaidImMod.prefix("textures/entity/electrician/electrician.png");
	public ElectricianEntityRenderer(EntityRenderDispatcher entityRenderDispatcher_1) {
		super(entityRenderDispatcher_1,
				new IllagerEntityModel<>(0f,0f,64,64), 0.5f);
		this.addFeature(new HeldItemFeatureRenderer<>(this));
		this.model.method_2812().visible = true;
	}

	@Override
	public Identifier getTexture(ElectricianEntity var1) {
		return SKIN;
	}
}
