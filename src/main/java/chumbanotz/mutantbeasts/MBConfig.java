package chumbanotz.mutantbeasts;

import net.minecraftforge.common.config.Config;

@Config(modid = MutantBeasts.MOD_ID, name = "MutantBeasts")
public class MBConfig {
    @Config.LangKey("cfg.mutantbeasts.general")
    @Config.Name("General")
    public static final GeneralSettings GENERAL = new GeneralSettings();

    @Config.LangKey("cfg.mutantbeasts.entities")
    @Config.Name("Entities")
    public static final EntitySettings ENTITIES = new EntitySettings();

    public static class GeneralSettings {
        // TODO: Are config options for particle IDs really needed?
        @Config.Name("Endersoul Particle ID")
        @Config.Comment("The particles created by mutant endermen")
        @Config.RequiresMcRestart
        public int endersoulParticleID = 100;

        @Config.Name("Skull Spirit Particle ID")
        @Config.Comment("The particles created by Chemical X")
        @Config.RequiresMcRestart
        public int skullSpiritParticleID = 101;

        // TODO: This does nothing currently
        @Config.Name("Custom Spawn Egg Texture")
        @Config.Comment
                ({
                        "Gives the spawn eggs from this mod the texture they had from Mutant Creatures",
                        "Disable this if your game crashes with the mod 'CodeChickenLib' installed"
                })
        @Config.RequiresMcRestart
        public boolean useCustomSpawnEggTexture = true;
    }

    public static class EntitySettings {
        @Config.Name("Global Spawn Rate")
        @Config.Comment
                ({
                        "Affects spawn rate of all mutants",
                        "The smaller the number, the lower the chance"
                })
        @Config.RangeInt(min = 1)
        public int globalSpawnRate = 10;

        @Config.Name("Mutant Creeper Armor")
        @Config.Comment("The amount of armor the Mutant Creeper has")
        public double mutantCreeperArmor = 0.0D;

        @Config.Name("Mutant Creeper Attack Damage")
        @Config.Comment("The amount of damage dealt by the Mutant Creeper")
        public double mutantCreeperAttackDamage = 5.0D;

        @Config.Name("Mutant Creeper Egg Spawn")
        @Config.Comment("Allows Mutant Creepers to spawn a creeper egg on death")
        @Config.RequiresMcRestart
        public boolean mutantCreeperSpawnsEgg = true;

        @Config.Name("Mutant Creeper Explosions Destroy Terrain")
        @Config.Comment("Allows Mutant Creepers to destroy terrain when exploding")
        @Config.RequiresMcRestart
        public boolean mutantCreeperDestroysTerrain = true;

        @Config.Name("Mutant Creeper Follow Range")
        @Config.Comment("The amount of blocks the Mutant Creeper follows entities")
        public double mutantCreeperFollowRange = 16.0D;

        @Config.Name("Mutant Creeper Knockback Resistance")
        @Config.Comment("The amount of knockback resistance the Mutant Creeper has")
        @Config.RangeDouble(min = 0.0D, max = 1.0D)
        public double mutantCreeperKnockbackResistance = 1.0D;

        @Config.Name("Mutant Creeper Max Health")
        @Config.Comment("The amount of maximum health the Mutant Creeper has")
        public double mutantCreeperMaxHealth = 150.0D;

        @Config.Name("Mutant Creeper Movement Speed")
        @Config.Comment("The amount of movement speed the Mutant Creeper has")
        public double mutantCreeperMovementSpeed = 0.26D;

        @Config.Name("Mutant Creeper Spawn Rate")
        @Config.Comment
                ({
                        "The weighted probability for the Mutant Creeper to spawn",
                        "Set to 0 to disable"
                })
        @Config.RangeInt(min = 0)
        @Config.RequiresMcRestart
        public int mutantCreeperSpawnRate = 1;

        @Config.Name("Mutant Creeper Swim Speed")
        @Config.Comment("The amount of swim speed the Mutant Creeper has")
        public double mutantCreeperSwimSpeed = 4.5D;

        @Config.Name("Mutant Enderman Renders Teleport")
        @Config.Comment
                ({
                        "Allows Mutant Endermen to render themselves where they will teleport",
                        "Disable this if you keep getting crashes related to the 'ObfuscationReflectionHelper'"
                })
        @Config.RequiresMcRestart
        public boolean mutantEndermanRendersTeleport = true;

        @Config.Name("Mutant Enderman Spawn Rate")
        @Config.Comment
                ({
                        "The weighted probability for the Mutant Enderman to spawn",
                        "Set to 0 to disable"
                })
        @Config.RangeInt(min = 0)
        @Config.RequiresMcRestart
        public int mutantEndermanSpawnRate = 1;

        @Config.Name("Mutant Skeleton Spawn Rate")
        @Config.Comment
                ({
                        "The weighted probability for the Mutant Skeleton to spawn",
                        "Set to 0 to disable"
                })
        @Config.RangeInt(min = 0)
        @Config.RequiresMcRestart
        public int mutantSkeletonSpawnRate = 1;

        @Config.Name("Mutant Zombie Spawn Rate")
        @Config.Comment
                ({
                        "The weighted probability for the Mutant Zombie to spawn",
                        "Set to 0 to disable"
                })
        @Config.RangeInt(min = 0)
        @Config.RequiresMcRestart
        public int mutantZombieSpawnRate = 1;

        @Config.Name("Creeper Minion Allowed On Shoulder")
        @Config.Comment
                ({
                        "This option is here because in 1.12.2, the way the parrot is rendered on a player's shoulder is hard-coded and won't render any other mob",
                        "When true, the normal parrot layer is replaced with a custom one to work with creeper minions",
                        "Disable this if there are compatibility issues with other mods"
                })
        @Config.RequiresMcRestart
        public boolean creeperMinionOnShoulder = true;
    }
}
