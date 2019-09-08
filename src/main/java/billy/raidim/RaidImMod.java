package billy.raidim;

import java.util.List;
import java.util.function.Predicate;

import org.apache.logging.log4j.Logger;

import billy.raidim.entity.DiggingEggEntity;
import billy.raidim.entity.ElectricianEntity;
import billy.raidim.entity.MinerEntity;
import billy.raidim.item.DiggingEggItem;
import billy.raidim.reflect.FieldHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules.Type;

public class RaidImMod implements ModInitializer {
	public static final Logger logger=
			org.apache.logging.log4j.LogManager.getLogger("RaidIm");
	public static String modid="raidim";
	public static final EntityType<DiggingEggEntity> DIGGINGEGG=FabricEntityTypeBuilder.
			<DiggingEggEntity>create(EntityCategory.MISC,
					DiggingEggEntity::new)
			.size(new EntitySize(0.3f,0.3f,true))
			.trackable(80, 2).build();
	public static final EntityType<MinerEntity> MINER=FabricEntityTypeBuilder.
			<MinerEntity>create(EntityCategory.MONSTER,
					MinerEntity::new)
			.size(new EntitySize(0.6f,1.8f,true))
			.trackable(80, 2).build();
	public static final EntityType<ElectricianEntity> ELECTRICIAN=FabricEntityTypeBuilder.
			<ElectricianEntity>create(EntityCategory.MONSTER,
					ElectricianEntity::new)
			.size(new EntitySize(0.6f,1.8f,true))
			.trackable(80,2).build();
	public static FieldHelper<List<Entity>> entities_fld=
			new FieldHelper<>(ServerWorld.class,"field_17913","globalEntities");
	@Environment(EnvType.SERVER)
	public static int test=0;
	public static Predicate<Entity> ALWAYS_TRUE=new Predicate<Entity>() {
		@Override
		public boolean test(Entity t) {
			return true;
		}
	};
	@Override
	public void onInitialize() {
		Registry.register(Registry.ENTITY_TYPE, prefix("diggingegg"),
				DIGGINGEGG);
		Registry.register(Registry.ENTITY_TYPE, prefix("miner"),
				MINER);
		Registry.register(Registry.ENTITY_TYPE, prefix("electrician"), 
				ELECTRICIAN);
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
		GameObjects.addRaidMember("MINER", MINER,0,0,0,1,0,1,3);
		GameObjects.addRaidMember("ELECTRICIAN", ELECTRICIAN, 0,0,0,0,0,1,1);
		GameObjects.addSpawnEgg(EntityType.ILLUSIONER, 0x3408b8, 0x070023);
		GameObjects.addSpawnEgg(EntityType.IRON_GOLEM, 0x424242, 0x676767,true);
		GameObjects.addSpawnEgg(EntityType.SNOW_GOLEM, 0x525252, 0xa58200,true);
		GameObjects.addSpawnEgg(MINER, 0x645834, 0x370629);
		GameObjects.addSpawnEgg(ELECTRICIAN, 0x832c3f, 0x27be29);
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
