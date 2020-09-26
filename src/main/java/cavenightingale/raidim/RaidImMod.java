package cavenightingale.raidim;

import cavenightingale.raidim.entity.ElectricianEntity;
import cavenightingale.raidim.entity.MinerEntity;
import cavenightingale.raidim.entity.MiningEggEntity;
import cavenightingale.raidim.item.MiningEggItem;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

public class RaidImMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "raidim";
	public static final ArrayList<RaidMember> MEMBERS = new ArrayList<>();
	public static GameRules.Key<GameRules.IntRule> raidRadius;
	public static GameRules.Key<GameRules.BooleanRule> tradeInRaid;
	public static GameRules.Key<GameRules.BooleanRule> villagerSpawnGolemInRaid;
	public static GameRules.Key<GameRules.BooleanRule> villagerSpawnGolemCommon;
	public static final EntityType<MiningEggEntity> MINING_EGG_ENTITY_TYPE =
			FabricEntityTypeBuilder.<MiningEggEntity>create()
					.trackRangeChunks(32)
					.entityFactory(MiningEggEntity::new)
					.dimensions(EntityDimensions.fixed(0.3f,0.3f))
					.build();
	public static final EntityType<ElectricianEntity> ELECTRICIAN_ENTITY_TYPE =
			FabricEntityTypeBuilder.<ElectricianEntity>create()
					.trackRangeChunks(32)
					.entityFactory(ElectricianEntity::new)
					.dimensions(EntityDimensions.fixed(0.6f,1.8f))
					.build();
	public static final EntityType<MinerEntity> MINER_ENTITY_TYPE =
			FabricEntityTypeBuilder.<MinerEntity>create()
					.trackRangeChunks(32)
					.entityFactory(MinerEntity::new)
					.dimensions(EntityDimensions.fixed(0.6f,1.8f))
					.build();
	private static final Identifier ILLUSIONER_LOOT_TABLE_ID =
			new Identifier("minecraft", "entities/illusioner");

	public static Identifier prefix(String path) {
		return new Identifier(MODID, path);
	}

	@Override
	public void onInitialize() {
		((ArrayList)MEMBERS).addAll(Arrays.asList(Raid.Member.values()));
		MEMBERS.add(new CustomRaidMemberImpl(EntityType.ILLUSIONER, new int[]{0, 0, 0, 0, 1, 0, 1, 2}));
		MEMBERS.add(new CustomRaidMemberImpl(MINER_ENTITY_TYPE, new int[]{0, 0, 0, 0, 1, 2, 1, 3},
				(raid, random, wave, localDifficulty, extra) -> wave >= 5 ? random.nextInt(3) : 0));
		MEMBERS.add(new CustomRaidMemberImpl(ELECTRICIAN_ENTITY_TYPE, new int[]{0, 0, 0, 0, 0, 0, 0, 1}));

		raidRadius = GameRuleRegistry.register("raidRadius",
				GameRules.Category.MOBS, GameRuleFactory.createIntRule(100));
		tradeInRaid = GameRuleRegistry.register("tradeInRaid",
				GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));
		villagerSpawnGolemInRaid = GameRuleRegistry.register("villagerSpawnGolemInRaid",
				GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));
		villagerSpawnGolemCommon = GameRuleRegistry.register("villagerSpawnGolemCommon",
				GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));

		Registry.register(Registry.ITEM, prefix("mining_egg"), MiningEggItem.INSTANCE);
		Registry.register(Registry.ITEM, prefix("electrician_spawn_egg"),
				new SpawnEggItem(ELECTRICIAN_ENTITY_TYPE, 0x832c3f, 0x27be29,
						new FabricItemSettings().group(ItemGroup.MISC).maxCount(64)));
		Registry.register(Registry.ITEM, prefix("miner_spawn_egg"),
				new SpawnEggItem(MINER_ENTITY_TYPE, 0x645834, 0x370629,
						new FabricItemSettings().group(ItemGroup.MISC).maxCount(64)));
		Registry.register(Registry.ITEM, prefix("illusioner_spawn_egg"),
				new SpawnEggItem(EntityType.ILLUSIONER, 0x3408b8, 0x070023,
						new FabricItemSettings().group(ItemGroup.MISC).maxCount(64)));

		Registry.register(Registry.ENTITY_TYPE, prefix("mining_egg"), MINING_EGG_ENTITY_TYPE);
		Registry.register(Registry.ENTITY_TYPE, prefix("electrician"), ELECTRICIAN_ENTITY_TYPE);
		Registry.register(Registry.ENTITY_TYPE, prefix("miner"), MINER_ENTITY_TYPE);

		FabricDefaultAttributeRegistry.register(ELECTRICIAN_ENTITY_TYPE, ElectricianEntity.createElectricianAttributes());
		FabricDefaultAttributeRegistry.register(MINER_ENTITY_TYPE, MinerEntity.createMinerAttributes());

		modifyVillagerHostilesSensor();
		LootTableLoadingCallback.EVENT.register(RaidImMod::onLootTableLoading);
	}

	private static void modifyVillagerHostilesSensor() {
		VillagerHostilesSensor.SQUARED_DISTANCES_FOR_DANGER = ImmutableMap.<EntityType<?>, Float>builder()
				.putAll(VillagerHostilesSensor.SQUARED_DISTANCES_FOR_DANGER)
				.put(ELECTRICIAN_ENTITY_TYPE, 40.0f)
				.put(MINER_ENTITY_TYPE, 20.0f)
				.build();
	}

	private static void onLootTableLoading(ResourceManager var1, LootManager manager,
			Identifier id, FabricLootSupplierBuilder supplier, LootTableLoadingCallback.LootTableSetter setter) {
		if(ILLUSIONER_LOOT_TABLE_ID.equals(id)) {
			FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
					.rolls(ConstantLootTableRange.create(1))
					.with(ItemEntry.builder(Items.GOLD_INGOT));
			supplier.pool(poolBuilder);
		}
	}
}
