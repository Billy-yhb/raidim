package cavenightingale.raidim.mixin;

import cavenightingale.raidim.RaidImMod;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	@Shadow
	private ClientWorld world;
	@Inject(method = "onEntitySpawn",at = @At("RETURN"))
	public void onOnEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo info) {
		double x = packet.getX();
		double y = packet.getY();
		double z = packet.getZ();
		EntityType<?> type = packet.getEntityTypeId();
		if(type == RaidImMod.MINING_EGG_ENTITY_TYPE) {
			Entity entity = RaidImMod.MINING_EGG_ENTITY_TYPE.create(world);
			int int_1 = packet.getId();
			entity.setPos(x, y, z);
			entity.pitch = (float)(packet.getPitch() * 360) / 256.0F;
			entity.yaw = (float)(packet.getYaw() * 360) / 256.0F;
			entity.setEntityId(int_1);
			entity.setUuid(packet.getUuid());
			this.world.addEntity(int_1, (Entity)entity);
		}
	}
}
