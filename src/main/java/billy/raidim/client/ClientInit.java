package billy.raidim.client;

import billy.raidim.GameHooks;
import billy.raidim.RaidImMod;
import billy.raidim.client.renderer.MinerEntityRender;
import billy.raidim.entity.DiggingEggEntity;
import billy.raidim.entity.MinerEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class ClientInit implements ClientModInitializer{
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(DiggingEggEntity.class,
				(m,c)->new FlyingItemEntityRenderer<DiggingEggEntity>(m,
						MinecraftClient.getInstance().getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(MinerEntity.class,
				(m,c)->new MinerEntityRender(m));
		GameHooks.client_entity_spawn_hooks.add((packet,type,world)->{
			if(type==RaidImMod.DIGGINGEGG) {
				return new DiggingEggEntity(RaidImMod.DIGGINGEGG,
						packet.getX(),packet.getY(),packet.getZ(),world);
			}
			return null;
		});
	}
}
