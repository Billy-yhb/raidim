package billy.raidim;

import java.util.function.Predicate;

import org.apache.logging.log4j.Logger;

import billy.raidim.entity.DiggingEggEntity;
import billy.raidim.entity.MinerEntity;
import billy.raidim.item.DiggingEggItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules.Type;

public class RaidImMod implements ModInitializer {
	public static final Logger logger=
			org.apache.logging.log4j.LogManager.getLogger("RaidIm");
	public static String modid="raidim";
	public static EntityType<DiggingEggEntity> DIGGINGEGG=null;
	public static EntityType<MinerEntity> MINER=null;
	@Environment(EnvType.SERVER)
	public static int test=0;
	@Override
	public void onInitialize() {
		Registry.register(Registry.ENTITY_TYPE, prefix("diggingegg"),
				DIGGINGEGG=FabricEntityTypeBuilder.
				<DiggingEggEntity>create(EntityCategory.MISC,
						DiggingEggEntity::new)
				.size(new EntitySize(0.3f,0.3f,true))
				.trackable(80, 2).build());
		Registry.register(Registry.ENTITY_TYPE, prefix("miner"),
				MINER=FabricEntityTypeBuilder.
				<MinerEntity>create(EntityCategory.MISC,
						MinerEntity::new)
				.size(new EntitySize(0.6f,1.8f,true))
				.trackable(80, 2).build());
		Registry.register(Registry.ITEM, prefix("diggingegg"),
				DiggingEggItem.INSTANCE);
		GameObjects.addGameRule("raidRadius", "100", Type.NUMERICAL_VALUE);
		GameObjects.addGameRule("tradeInRaid", "true", Type.BOOLEAN_VALUE);
		GameObjects.addGameRule("villagerSpawnGoleamInRaid",
				"false", Type.BOOLEAN_VALUE);
		GameObjects.addGameRule("villagerSpawnGoleamCommon",
				"true", Type.BOOLEAN_VALUE);
		GameObjects.addRaidMember("ILLUSIONER", EntityType.ILLUSIONER, 
				0,0,0,0,0,1,2);
		GameObjects.addRaidMember("MINER", MINER,0,0,0,1,0,0,3);
		GameObjects.addSpawnEgg(EntityType.ILLUSIONER, 0x3408b8, 0x070023);
		GameObjects.addSpawnEgg(MINER, 0x645834, 0x370629);
	}
	public static Identifier prefix(String val) {
		return new Identifier(modid,val);
	}
	public static class RaidPlayerSelector implements Predicate<ServerPlayerEntity>{
		@Override
		public boolean test(ServerPlayerEntity t) {
			return t.isAlive()&&getXZDistance(center,t.getBlockPos())
					<t.world.getGameRules().getInteger("raidRadius");
		}
		private double getXZDistance(BlockPos pos,BlockPos pos1) {
			return Math.sqrt((pos.getX()-pos1.getX())*(pos.getX()-pos1.getX())
					+(pos.getZ()-pos1.getZ())*(pos.getZ()-pos1.getZ()));
		}
		public BlockPos center;
		public RaidPlayerSelector(BlockPos centerIn) {
			center=centerIn;
		}
	}
}
