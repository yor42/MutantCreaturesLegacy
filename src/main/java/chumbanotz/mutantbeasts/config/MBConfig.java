package chumbanotz.mutantbeasts.config;

import chumbanotz.mutantbeasts.mutantbeasts.Tags;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

@Config(modid = Tags.MOD_NAME)
public class MBConfig {
    @Name("Creeper Minion Allowed On Shoulder")
    @Comment({"This option is here because in 1.12.2, the way the parrot is rendered on a player's shoulder is hard-coded and won't render any other mob", "When true, the normal parrot layer is replaced with a custom one to work with creeper minions", "Disable this if there are compatibility issues with other mods"})
    @RequiresMcRestart
    public static boolean creeperMinionOnShoulder = true;

    @Name("Biome Whitelist")
    @Comment({"The mod IDs of the biomes that the mutants are allowed to spawn in, only vanilla biomes by default", "Example - 'minecraft, twilightforest'", "You can see a mod's ID by clicking on the 'Mods' button in-game and looking on the right"})
    @RequiresMcRestart
    public static String[] biomeWhitelist = new String[]{"minecraft"};

    @Name("Endersoul Particle ID")
    @Comment({"The particles created by mutant endermen"})
    @RangeInt(min = 49)
    @RequiresMcRestart
    public static int endersoulParticleID = 100;

    @Name("Mutant Creeper Spawn Rate")
    @Comment({"Zero disables spawning"})
    @RangeInt(min = 0, max = 100)
    @RequiresMcRestart
    public static int mutantCreeperSpawnRate = 5;

    @Name("Mutant Enderman Spawn Rate")
    @Comment({"Zero disables spawning"})
    @RangeInt(min = 0, max = 100)
    @RequiresMcRestart
    public static int mutantEndermanSpawnRate = 3;

    @Name("Mutant Skeleton Spawn Rate")
    @Comment({"Zero disables spawning"})
    @RangeInt(min = 0, max = 100)
    @RequiresMcRestart
    public static int mutantSkeletonSpawnRate = 5;

    @Name("Mutant Zombie Spawn Rate")
    @Comment({"Zero disables spawning"})
    @RangeInt(min = 0, max = 100)
    @RequiresMcRestart
    public static int mutantZombieSpawnRate = 5;

    @Name("Skull Spirit Particle ID")
    @Comment({"The particles created by Chemical X"})
    @RangeInt(min = 49)
    @RequiresMcRestart
    public static int skullSpiritParticleID = 101;
}
