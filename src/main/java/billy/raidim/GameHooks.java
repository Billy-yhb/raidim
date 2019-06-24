package billy.raidim;

import java.util.ArrayList;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.packet.EntitySpawnS2CPacket;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class GameHooks {
	@Environment(EnvType.CLIENT)
	public static final ArrayList<ClientEntitySpawnHook> 
		client_entity_spawn_hooks
		=new ArrayList<>();
	@Environment(EnvType.CLIENT)
	public static interface ClientEntitySpawnHook{
		public Entity getEntity(EntitySpawnS2CPacket packet,
				EntityType<?> type,ClientWorld world);
	}
	public static final ArrayList<ServerLivingEntityHurtHook> 
		server_living_entity_hurt_hooks
		=new ArrayList<>();
	public static interface ServerLivingEntityHurtHook{
		public void onLivingHurt(LivingEntity e,DamageSource src,float amount);
	}
}
