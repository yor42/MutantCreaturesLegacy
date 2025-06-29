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

    @Config.LangKey("cfg.mutantbeasts.items")
    @Config.Name("Items")
    public static final ItemSettings ITEMS = new ItemSettings();

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
        @Config.Comment("Allows the Mutant Creeper to spawn a creeper egg on death")
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

        @Config.Name("Mutant Snowman Armor")
        @Config.Comment("The amount of armor the Mutant Snowman has")
        public double mutantSnowmanArmor = 0.0D;

        @Config.Name("Mutant Snowman Knockback Resistance")
        @Config.Comment("The amount of knockback resistance the Mutant Snowman has")
        @Config.RangeDouble(min = 0.0D, max = 1.0D)
        public double mutantSnowmanKnockbackResistance = 1.0D;

        @Config.Name("Mutant Snowman Max Health")
        @Config.Comment("The amount of maximum health the Mutant Snowman has")
        public double mutantSnowmanMaxHealth = 80.0D;

        @Config.Name("Mutant Snowman Movement Speed")
        @Config.Comment("The amount of movement speed the Mutant Snowman has")
        public double mutantSnowmanMovementSpeed = 0.26D;

        @Config.Name("Mutant Snowman Swim Speed")
        @Config.Comment("The amount of swim speed the Mutant Snowman has")
        public double mutantSnowmanSwimSpeed = 1.0D;

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

        @Config.Name("Mutant Zombie Lives")
        @Config.Comment
                ({
                        "The amount of lives the Mutant Zombie has",
                        "Each time the Mutant Zombie is defeated it will lose one live",
                        "It will keep reviving itself until it is out of lives or set on fire"
                })
        @Config.RangeInt(min = 1)
        public int mutantZombieLives = 3;

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

        @Config.Name("Spider-Pig Armor")
        @Config.Comment("The amount of armor the Spider-Pig has")
        public double spiderPigArmor = 0.0D;

        @Config.Name("Spider-Pig Attack Damage")
        @Config.Comment("The amount of damage dealt by the Spider-Pig")
        public double spiderPigAttackDamage = 3.0D;

        @Config.Name("Spider-Pig Knockback Resistance")
        @Config.Comment("The amount of knockback resistance the Spider-Pig has")
        @Config.RangeDouble(min = 0.0D, max = 1.0D)
        public double spiderPigKnockbackResistance = 0.0D;

        @Config.Name("Spider-Pig Max Health")
        @Config.Comment("The amount of maximum health the Spider-Pig has")
        public double spiderPigMaxHealth = 40.0D;

        @Config.Name("Spider-Pig Movement Speed")
        @Config.Comment("The amount of movement speed the Spider-Pig has")
        public double spiderPigMovementSpeed = 0.25D;

        @Config.Name("Spider-Pig Swim Speed")
        @Config.Comment("The amount of swim speed the Spider-Pig has")
        public double spiderPigSwimSpeed = 1.0D;

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

    public static class ItemSettings {
        @Config.Name("Creeper Shard Charges")
        @Config.Comment
                ({
                        "The charges required for the Creeper Shard to fully charge",
                        "One charge is always gained when hitting any mob with it",
                        "Right-clicking with a fully charged Creeper Shard will cause an explosion and revert the shard back to zero"
                })
        @Config.RangeInt(min = 1)
        public int creeperShardCharges = 32;

        @Config.Name("Endersoul Hand Attack Speed")
        @Config.Comment("The amount of attack speed the Endersoul Hand has")
        public double endersoulHandAttackSpeed = 1.6D;

        @Config.Name("Endersoul Hand Cooldown")
        @Config.Comment("The cooldown of the Endersoul Hand's teleportation ability when sneaking and right-clicking with it")
        public int endersoulHandCooldown = 40;

        @Config.Name("Endersoul Hand Damage")
        @Config.Comment("The amount of damage dealt by the Endersoul Hand")
        public double endersoulHandDamage = 6.0D;

        @Config.Name("Endersoul Hand Durability")
        @Config.Comment("The amount of durability the Endersoul Hand has")
        @Config.RangeInt(min = 1)
        public int endersoulHandDurability = 240;

        @Config.Name("Endersoul Hand Enchantability")
        @Config.Comment("The amount of enchantability the Endersoul Hand has")
        public int endersoulHandEnchantability = 20;

        @Config.Name("Endersoul Hand Teleportation")
        @Config.Comment
                ({
                        "Allows the Endersoul Hand to teleport you to any block within a radius when sneaking and right-clicking",
                        "Setting to false will disable this ability completely"
                })
        @Config.RequiresMcRestart
        public boolean endersoulHandTeleports = true;

        @Config.Name("Endersoul Hand Teleportation Cost")
        @Config.Comment("The cost in durability when teleporting with the Endersoul Hand")
        public int endersoulHandTeleportationCost = 4;

        @Config.Name("Endersoul Hand Teleportation Radius")
        @Config.Comment("The maximum radius in blocks when teleporting with the Endersoul Hand")
        public double endersoulHandTeleportationRadius = 128.0D;

        @Config.Name("Hulk Hammer Attack Speed")
        @Config.Comment("The amount of attack speed the Hulk Hammer has")
        public double hulkHammerAttackSpeed = 1.0D;

        @Config.Name("Hulk Hammer Cooldown")
        @Config.Comment("The cooldown of the Hulk Hammer's ground pound attack when right-clicking with it")
        public int hulkHammerCooldown = 25;

        @Config.Name("Hulk Hammer Damage")
        @Config.Comment("The amount of damage dealt by the Hulk Hammer (also affects the damage of the ground pound attack)")
        public double hulkHammerDamage = 9.0D;

        @Config.Name("Hulk Hammer Enchantability")
        @Config.Comment("The amount of enchantability the Hulk Hammer has")
        public int hulkHammerEnchantability = 10;

        @Config.Name("Hulk Hammer Durability")
        @Config.Comment("The amount of durability the Hulk Hammer has")
        @Config.RangeInt(min = 1)
        public int hulkHammerDurability = 64;

        @Config.Name("Hulk Hammer Shield Disabling")
        @Config.Comment("Allows the Hulk Hammer to disable shields")
        @Config.RequiresMcRestart
        public boolean hulkHammerDisablesShields = true;

        @Config.Name("Mutant Skeleton Armor Durability")
        @Config.Comment
                ({
                        "The amount of durability Mutant Skeleton Armor has (the number is calculated below)",
                        "Helmet: X * 11",
                        "Chestplate: X * 16",
                        "Leggings: X * 15",
                        "Boots: X * 13"
                })
        public int mutantSkeletonArmorDurability = 15;

        @Config.Name("Mutant Skeleton Armor Enchantability")
        @Config.Comment("The amount of enchantability Mutant Skeleton Armor has")
        public int mutantSkeletonArmorEnchantability = 9;

        @Config.Name("Mutant Skeleton Armor Protection (Boots)")
        @Config.Comment("The amount of armor this piece of Mutant Skeleton Armor has")
        public int mutantSkeletonArmorProtectionBoots = 2;

        @Config.Name("Mutant Skeleton Armor Protection (Chestplate)")
        @Config.Comment("The amount of armor this piece of Mutant Skeleton Armor has")
        public int mutantSkeletonArmorProtectionChestplate = 6;

        @Config.Name("Mutant Skeleton Armor Protection (Skull)")
        @Config.Comment("The amount of armor this piece of Mutant Skeleton Armor has")
        public int mutantSkeletonArmorProtectionHelmet = 2;

        @Config.Name("Mutant Skeleton Armor Protection (Leggings)")
        @Config.Comment("The amount of armor this piece of Mutant Skeleton Armor has")
        public int mutantSkeletonArmorProtectionLeggings = 5;

        @Config.Name("Mutant Skeleton Armor Toughness")
        @Config.Comment("The amount of toughness Mutant Skeleton Armor has")
        public double mutantSkeletonArmorToughness = 0.0D;

        @Config.Name("Mutant Skeleton Boots Jump Boost Ability")
        @Config.Comment
                ({
                        "Allows the Mutant Skeleton Boots to grant Jump Boost while worn",
                        "While sprinting, it will grant Jump Boost II instead"
                })
        @Config.RequiresMcRestart
        public boolean mutantSkeletonBootsJumpBoost = true;

        @Config.Name("Mutant Skeleton Chestplate Bow Charging Ability")
        @Config.Comment("Allows the Mutant Skeleton Chestplate to make bows charge at four times the original rate while worn")
        @Config.RequiresMcRestart
        public boolean mutantSkeletonChestplateBowCharging = true;

        @Config.Name("Mutant Skeleton Leggings Speed Ability")
        @Config.Comment("Allows the Mutant Skeleton Leggings to grant Speed II while worn")
        @Config.RequiresMcRestart
        public boolean mutantSkeletonLeggingsSpeed = true;

        @Config.Name("Mutant Skeleton Skull Arrow Ability")
        @Config.Comment
                ({
                        "Allows the Mutant Skeleton Skull to grant special bonuses (and negatives) to arrows fired from bows",
                        "All critical mid-air arrow shots will deal double the damage, while non-airborne arrows will deal half the damage",
                        "There is a 50% chance for fired arrows to not be depleted like the Infinity enchantment"
                })
        @Config.RequiresMcRestart
        public boolean mutantSkeletonHelmetArrow = true;
    }
}
