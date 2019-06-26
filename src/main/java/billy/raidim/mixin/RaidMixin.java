package billy.raidim.mixin;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.raid.Raid;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import billy.raidim.RaidImMod.RaidPlayerSelector;

@Mixin(Raid.class)
public abstract class RaidMixin {
	@Shadow
	private BlockPos center;
	@Shadow
	private ServerWorld world;
	@Shadow
	private boolean hasSpawnedFinalWave() {return false;}
	@Shadow
	private int getRaiderCount() {return 0;}
	@Shadow
	private boolean hasExtraWave() {return false;}
	@Shadow
	private int wavesSpawned;
	@Overwrite
	private Predicate<ServerPlayerEntity> isInRaidDistance(){
		return new RaidPlayerSelector(center);
	}
	@Inject(method="tick",at=@At("HEAD"))
	public void onTick(CallbackInfo info){
		ArrayList<VillagerEntity> villagers=new ArrayList<>();
		int raidRadius=world.getGameRules().getInteger("raidRadius");
		for(Entity e:world.getEntities(EntityType.VILLAGER, 
				new Predicate<Entity>() {
					@Override
					public boolean test(Entity t) {
						return true;
					}
				})) {
			if(e instanceof VillagerEntity) {
				VillagerEntity v=(VillagerEntity)e;
				if(v.getBlockPos().getSquaredDistance(center)<=
						raidRadius*raidRadius) {
					villagers.add(v);
				}
			}
		}
		double x=0,y=0,z=0;
		if(!villagers.isEmpty()) {
			for(VillagerEntity e:villagers) {
				x+=e.x;
				y+=e.y;
				z+=e.z;
			}
			x/=villagers.size();
			y/=villagers.size();
			z/=villagers.size();
			if(center.getSquaredDistance(x,y,z,true)>0.06*raidRadius*raidRadius) {
				x=(2*x+center.getX())/3;
				y=(2*y+center.getY())/3;
				z=(2*z+center.getZ())/3;
				center=new BlockPos(x,y,z);
			}
		}
	}
	@Inject(method="getBonusCount",at=@At("RETURN"),cancellable=true)
	public void getBonusCount(@Coerce Enum<?> raid$Member_1, Random random_1,
			int int_1, LocalDifficulty localDifficulty_1, boolean boolean_1,
			CallbackInfoReturnable<Integer> info) {
		if(raid$Member_1.name()=="MINER") {
			if(int_1>4) {
				info.setReturnValue(random_1.nextInt(4));
			}
		}else if(raid$Member_1.name()=="ILLUSIONER") {
			if(int_1>6) {
				info.setReturnValue(random_1.nextInt(3));
			}
		}
	}
}
