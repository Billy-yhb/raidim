package billy.raidim.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import billy.raidim.GameHooks;
import billy.raidim.GameHooks.ClientEntitySpawnHook;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.EntitySpawnS2CPacket;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class NetworkMixin {
	@Shadow
	private ClientWorld world;
	@Inject(method="onEntitySpawn",at=@At("RETURN"))
	public void onOnEntitySpawn(EntitySpawnS2CPacket packet,CallbackInfo info) {
		double x = packet.getX();
		double y = packet.getY();
		double z = packet.getZ();
		EntityType<?> type = packet.getEntityTypeId();
		Entity entity=null;
		for(ClientEntitySpawnHook hook:GameHooks.client_entity_spawn_hooks) {
			entity=hook.getEntity(packet,type,world);
			if(entity!=null) {
				break;
			}
		}
		if(entity!=null) {
			int int_1 = packet.getId();
			((Entity)entity).method_18003(x, y, z);
			((Entity)entity).pitch = (float)(packet.getPitch() * 360) / 256.0F;
			((Entity)entity).yaw = (float)(packet.getYaw() * 360) / 256.0F;
			((Entity)entity).setEntityId(int_1);
			((Entity)entity).setUuid(packet.getUuid());
			this.world.addEntity(int_1, (Entity)entity);
		}
	}
}
