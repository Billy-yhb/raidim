package billy.raidim.client.renderer;

import billy.raidim.RaidImMod;
import billy.raidim.entity.ElectricianEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EvilVillagerEntityModel;
import net.minecraft.util.Identifier;

public class ElectricianEntityRender extends IllagerEntityRenderer<ElectricianEntity> {
	public ElectricianEntityRender(EntityRenderDispatcher entityRenderDispatcher_1) {
		super(entityRenderDispatcher_1, 
				new EvilVillagerEntityModel<ElectricianEntity>(0f,0f,64,64), 0.5f);
		this.addFeature(new HeldItemFeatureRenderer<ElectricianEntity,
				EvilVillagerEntityModel<ElectricianEntity>>(this));
		((EvilVillagerEntityModel<?>)this.model).method_2812().visible = true;
	}
	public static final Identifier SKIN=RaidImMod.prefix("textures/entity/electrician/electrician.png");
	@Override
	protected Identifier getTexture(ElectricianEntity var1) {
		return SKIN;
	}

}
