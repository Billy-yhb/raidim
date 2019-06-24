package billy.raidim.client.renderer;

import billy.raidim.RaidImMod;
import billy.raidim.entity.MinerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EvilVillagerEntityModel;
import net.minecraft.util.Identifier;

public class MinerEntityRender extends IllagerEntityRenderer<MinerEntity> {
	public MinerEntityRender(EntityRenderDispatcher entityRenderDispatcher_1) {
		super(entityRenderDispatcher_1, 
				new EvilVillagerEntityModel<MinerEntity>(0f,0f,64,64), 0.5f);
		this.addFeature(new HeldItemFeatureRenderer<MinerEntity,
				EvilVillagerEntityModel<MinerEntity>>(this){
			public void method_17162(MinerEntity miner, float float_1,
					float float_2, float float_3, float float_4,
					float float_5, float float_6, float float_7) {
				if(miner.shouldShowPickaxe())
					super.method_17162(miner, float_1, float_2, float_3,
							float_4, float_5, float_6, float_7);
			}
		});
		((EvilVillagerEntityModel<?>)this.model).method_2812().visible = true;
	}
	public static final Identifier SKIN=RaidImMod.prefix("textures/entity/miner/miner.png");
	@Override
	protected Identifier getTexture(MinerEntity var1) {
		return SKIN;
	}

}
