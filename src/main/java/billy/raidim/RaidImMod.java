package billy.raidim;

import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.RaidMember;

public class RaidImMod implements ModInitializer {
	public static final Logger logger=
			org.apache.logging.log4j.LogManager.getLogger("RaidIm");
	@Override
	public void onInitialize() {
		RaidMember.addRaidMember("ILLUSIONER", EntityType.ILLUSIONER, 
				0,0,0,0,0,1,2);
	}
}
