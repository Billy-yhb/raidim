package cavenightingale.raidim.client;

import cavenightingale.raidim.RaidImMod;
import cavenightingale.raidim.client.renderer.ElectricianEntityRenderer;
import cavenightingale.raidim.client.renderer.MinerEntityRenderer;
import cavenightingale.raidim.entity.MiningEggEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class RaidImClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(RaidImMod.MINING_EGG_ENTITY_TYPE,
				(m, c) -> new FlyingItemEntityRenderer<MiningEggEntity>(m, MinecraftClient.getInstance().getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(RaidImMod.ELECTRICIAN_ENTITY_TYPE,
				(m, c) -> new ElectricianEntityRenderer(m));
		EntityRendererRegistry.INSTANCE.register(RaidImMod.MINER_ENTITY_TYPE,
				(m, c) -> new MinerEntityRenderer(m));
	}
}
