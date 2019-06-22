package billy.raidim.client.renderer;

import billy.raidim.entity.MinerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.model.EvilVillagerEntityModel;
import net.minecraft.util.Identifier;

public class MinerEntityRender extends IllagerEntityRenderer<MinerEntity> {
	protected MinerEntityRender(EntityRenderDispatcher entityRenderDispatcher_1,
			EvilVillagerEntityModel<MinerEntity> evilVillagerEntityModel_1, float float_1) {
		super(entityRenderDispatcher_1, evilVillagerEntityModel_1, float_1);
	}

	@Override
	protected Identifier getTexture(MinerEntity var1) {
		return new Identifier("raidim:textures/entity/miner/miner.png");
	}

}
