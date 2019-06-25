package billy.raidim.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.raid.RaidManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(RaidManager.class)
public abstract class RaidManagerMixin{
	@Shadow
	private Map<Integer, Raid> raids;
	@Shadow
	private ServerWorld world;
	@Shadow
	private int nextId() {return 0;}
	@Overwrite
	public Raid getOrCreateRaid(ServerWorld serverWorld_1, BlockPos p1) {
		for(Raid r:raids.values()) {
			BlockPos pos=r.getCenter();
			if(Math.sqrt((pos.getX()-p1.getX())*(pos.getX()-p1.getX())+
					(pos.getZ()-p1.getZ())*(pos.getZ()-p1.getZ()))<
					serverWorld_1.getGameRules().getInteger("raidRadius")) {
				return r;
			}
		}
		return new Raid(nextId(), serverWorld_1, p1);
	}
}
