package chumbanotz.mutantbeasts;

import net.minecraftforge.common.config.Config;

@Config(modid = "mutantbeasts")
public class MBConfig {

    @Config.Name("Global Spawn Rate")
    @Config.Comment({"Affects spawn rate of all mutants", "The smaller the number, the lower the chance"})
    @Config.RangeInt(
            min = 1,
            max = 20
    )
    public static int globalSpawnRate = 10;
    @Config.Name("Mutant Creeper Spawn Rate")
    @Config.Comment({"Zero disables spawning"})
    @Config.RangeInt(
            min = 0,
            max = 100
    )
    @Config.RequiresMcRestart
    public static int mutantCreeperSpawnRate = 1;
    @Config.Name("Mutant Enderman Spawn Rate")
    @Config.Comment({"Zero disables spawning"})
    @Config.RangeInt(
            min = 0,
            max = 100
    )
    @Config.RequiresMcRestart
    public static int mutantEndermanSpawnRate = 1;
    @Config.Name("Mutant Enderman Renders Teleport")
    @Config.Comment({"Allows mutant endermen to render themselves where they will teleport", "Disable this if you keep getting crashes related to the 'ObfuscationReflectionHelper'"})
    @Config.RequiresMcRestart
    public static boolean mutantEndermanRendersTeleport = true;
    @Config.Name("Mutant Skeleton Spawn Rate")
    @Config.Comment({"Zero disables spawning"})
    @Config.RangeInt(
            min = 0,
            max = 100
    )
    @Config.RequiresMcRestart
    public static int mutantSkeletonSpawnRate = 1;
    @Config.Name("Mutant Zombie Spawn Rate")
    @Config.Comment({"Zero disables spawning"})
    @Config.RangeInt(
            min = 0,
            max = 100
    )
    @Config.RequiresMcRestart
    public static int mutantZombieSpawnRate = 1;
    @Config.Name(value = "Creeper Minion Allowed On Shoulder")
    @Config.Comment(value = {"This option is here because in 1.12.2, the way the parrot is rendered on a player's shoulder is hard-coded and won't render any other mob", "When true, the normal parrot layer is replaced with a custom one to work with creeper minions", "Disable this if there are compatibility issues with other mods"})
    @Config.RequiresMcRestart
    public static boolean creeperMinionOnShoulder = true;
    @Config.Name(value = "Endersoul Particle ID")
    @Config.Comment(value = {"The particles created by mutant endermen"})
    @Config.RangeInt(min = 49)
    @Config.RequiresMcRestart
    public static int endersoulParticleID = 100;
    @Config.Name(value = "Skull Spirit Particle ID")
    @Config.Comment(value = {"The particles created by Chemical X"})
    @Config.RangeInt(min = 49)
    @Config.RequiresMcRestart
    public static int skullSpiritParticleID = 101;


    @Config.Name("Custom Spawn Egg Texture")
    @Config.Comment({"Gives the spawn eggs from this mod the texture they had from Mutant Creatures", "Disable this if your game crashes with the mod 'CodeChickenLib' installed"})
    @Config.RequiresMcRestart
    public static boolean useCustomSpawnEggTexture = true;
}
