package cavenightingale.raidim.mixin;

import cavenightingale.raidim.RaidImMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.Raid;
import net.minecraft.village.raid.RaidManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
	@Shadow
	public abstract MinecraftServer getServer();

	@Redirect(method = "getRaidAt", at =
		@At(value = "INVOKE", target = "Lnet/minecraft/village/raid/RaidManager;" +
				"getRaidAt(Lnet/minecraft/util/math/BlockPos;I)Lnet/minecraft/village/raid/Raid;"))
	@Nullable
	private Raid raidim_onGetRaidAt(RaidManager raidManager, BlockPos pos, int i){
		int radius = getServer().getGameRules().getInt(RaidImMod.raidRadius);
		return raidManager.getRaidAt(pos, radius * radius);
	}
}
