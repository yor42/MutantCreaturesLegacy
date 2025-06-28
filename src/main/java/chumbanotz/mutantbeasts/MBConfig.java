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

        @Config.Name("Mutant Creeper Explosions Destroy Terrain")
        @Config.Comment("Allows the Mutant Creeper to destroy terrain when exploding")
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

        @Config.Name("Mutant Creeper Egg Spawn")
        @Config.Comment("Allows Mutant Creepers to spawn a creeper egg on death")
        @Config.RequiresMcRestart
        public boolean mutantCreeperSpawnsEgg = true;

        @Config.Name("Mutant Creeper Spawn Probability")
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

        @Config.Name("Mutant Enderman Armor")
        @Config.Comment("The amount of armor the Mutant Enderman has")
        public double mutantEndermanArmor = 0.0D;

        @Config.Name("Mutant Enderman Attack Damage")
        @Config.Comment("The amount of damage dealt by the Mutant Enderman")
        public double mutantEndermanAttackDamage = 7.0D;

        @Config.Name("Mutant Enderman Follow Range")
        @Config.Comment("The amount of blocks the Mutant Enderman follows entities")
        public double mutantEndermanFollowRange = 96.0D;

        @Config.Name("Mutant Enderman Knockback Resistance")
        @Config.Comment("The amount of knockback resistance the Mutant Enderman has")
        @Config.RangeDouble(min = 0.0D, max = 1.0D)
        public double mutantEndermanKnockbackResistance = 1.0D;

        @Config.Name("Mutant Enderman Max Health")
        @Config.Comment("The amount of maximum health the Mutant Enderman has")
        public double mutantEndermanMaxHealth = 200.0D;

        @Config.Name("Mutant Enderman Movement Speed")
        @Config.Comment("The amount of movement speed the Mutant Enderman has")
        public double mutantEndermanMovementSpeed = 0.3D;

        @Config.Name("Mutant Enderman Renders Teleport")
        @Config.Comment
                ({
                        "Allows the Mutant Enderman to render itself on where they will teleport",
                        "Disable this if you keep getting crashes related to the 'ObfuscationReflectionHelper'"
                })
        @Config.RequiresMcRestart
        public boolean mutantEndermanRendersTeleport = true;

        @Config.Name("Mutant Enderman Fragments Spawn")
        @Config.Comment("Allows the Mutant Enderman to spawn endersoul fragments while dying")
        @Config.RequiresMcRestart
        public boolean mutantEndermanSpawnsFragments = true;

        @Config.Name("Mutant Enderman Spawn Probability")
        @Config.Comment
                ({
                        "The weighted probability for the Mutant Enderman to spawn",
                        "Set to 0 to disable"
                })
        @Config.RangeInt(min = 0)
        @Config.RequiresMcRestart
        public int mutantEndermanSpawnRate = 1;

        @Config.Name("Mutant Enderman Swim Speed")
        @Config.Comment("The amount of swim speed the Mutant Enderman has")
        public double mutantEndermanSwimSpeed = 1.0D;

        @Config.Name("Mutant Skeleton Armor")
        @Config.Comment("The amount of armor the Mutant Skeleton has")
        public double mutantSkeletonArmor = 0.0D;

        @Config.Name("Mutant Skeleton Attack Damage")
        @Config.Comment("The amount of damage dealt by the Mutant Skeleton")
        public double mutantSkeletonAttackDamage = 4.0D;

        @Config.Name("Mutant Skeleton Follow Range")
        @Config.Comment("The amount of blocks the Mutant Skeleton follows entities")
        public double mutantSkeletonFollowRange = 50.0D;

        @Config.Name("Mutant Skeleton Knockback Resistance")
        @Config.Comment("The amount of knockback resistance the Mutant Skeleton has")
        @Config.RangeDouble(min = 0.0D, max = 1.0D)
        public double mutantSkeletonKnockbackResistance = 1.0D;

        @Config.Name("Mutant Skeleton Max Health")
        @Config.Comment("The amount of maximum health the Mutant Skeleton has")
        public double mutantSkeletonMaxHealth = 150.0D;

        @Config.Name("Mutant Skeleton Movement Speed")
        @Config.Comment("The amount of movement speed the Mutant Skeleton has")
        public double mutantSkeletonMovementSpeed = 0.27D;

        @Config.Name("Mutant Skeleton Spawn Probability")
        @Config.Comment
                ({
                        "The weighted probability for the Mutant Skeleton to spawn",
                        "Set to 0 to disable"
                })
        @Config.RangeInt(min = 0)
        @Config.RequiresMcRestart
        public int mutantSkeletonSpawnRate = 1;

        @Config.Name("Mutant Skeleton Swim Speed")
        @Config.Comment("The amount of swim speed the Mutant Skeleton has")
        public double mutantSkeletonSwimSpeed = 5.0D;

        @Config.Name("Mutant Zombie Armor")
        @Config.Comment("The amount of armor the Mutant Zombie has")
        public double mutantZombieArmor = 3.0D;

        @Config.Name("Mutant Zombie Attack Damage")
        @Config.Comment("The amount of damage dealt by the Mutant Zombie")
        public double mutantZombieAttackDamage = 12.0D;

        @Config.Name("Mutant Zombie Follow Range")
        @Config.Comment("The amount of blocks the Mutant Zombie follows entities")
        public double mutantZombieFollowRange = 35.0D;

        @Config.Name("Mutant Zombie Knockback Resistance")
        @Config.Comment("The amount of knockback resistance the Mutant Zombie has")
        @Config.RangeDouble(min = 0.0D, max = 1.0D)
        public double mutantZombieKnockbackResistance = 1.0D;

        @Config.Name("Mutant Zombie Max Health")
        @Config.Comment("The amount of maximum health the Mutant Zombie has")
        public double mutantZombieMaxHealth = 150.0D;

        @Config.Name("Mutant Zombie Movement Speed")
        @Config.Comment("The amount of movement speed the Mutant Zombie has")
        public double mutantZombieMovementSpeed = 0.26D;

        @Config.Name("Mutant Zombie Spawn Probability")
        @Config.Comment
                ({
                        "The weighted probability for the Mutant Zombie to spawn",
                        "Set to 0 to disable"
                })
        @Config.RangeInt(min = 0)
        @Config.RequiresMcRestart
        public int mutantZombieSpawnRate = 1;

        @Config.Name("Mutant Zombie Swim Speed")
        @Config.Comment("The amount of swim speed the Mutant Zombie has")
        public double mutantZombieSwimSpeed = 4.0D;

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
