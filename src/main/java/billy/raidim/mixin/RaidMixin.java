package billy.raidim.mixin;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.raid.Raid;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import billy.raidim.RaidImMod.RaidPlayerSelector;

@Mixin(Raid.class)
public abstract class RaidMixin {
	@Shadow
	private BlockPos center;
	@Overwrite
	private Predicate<ServerPlayerEntity> isInRaidDistance(){
		return new RaidPlayerSelector(center);
	}
}
