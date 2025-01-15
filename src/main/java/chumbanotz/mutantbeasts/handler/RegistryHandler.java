package chumbanotz.mutantbeasts.handler;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.config.MBConfig;
import chumbanotz.mutantbeasts.entity.*;
import chumbanotz.mutantbeasts.entity.mutant.*;
import chumbanotz.mutantbeasts.entity.projectile.ChemicalXEntity;
import chumbanotz.mutantbeasts.entity.projectile.MutantArrowEntity;
import chumbanotz.mutantbeasts.entity.projectile.ThrowableBlockEntity;
import chumbanotz.mutantbeasts.item.*;
import chumbanotz.mutantbeasts.mutantbeasts.Tags;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Arrays;
import java.util.List;

@EventBusSubscriber(modid = Tags.MOD_ID)
public class RegistryHandler {
    private static int entityId;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                setRegistryName("chemical_x", (new ChemicalXItem()).setMaxStackSize(1)),
                setRegistryName("creeper_minion_tracker", (new Item()).setMaxStackSize(1)),
                setRegistryName("creeper_shard", (new CreeperShardItem()).setMaxStackSize(1).setMaxDamage(32)),
                setRegistryName("endersoul_hand", (new EndersoulHandItem()).setMaxStackSize(1).setMaxDamage(240)),
                setRegistryName("hulk_hammer", (new HulkHammerItem()).setMaxStackSize(1).setMaxDamage(64)),
                setRegistryName("mutant_skeleton_arms", new Item()),
                setRegistryName("mutant_skeleton_limb", new Item()),
                setRegistryName("mutant_skeleton_shoulder_pad", new Item()),
                setRegistryName("mutant_skeleton_rib", new Item()),
                setRegistryName("mutant_skeleton_rib_cage", new Item()),
                setRegistryName("mutant_skeleton_pelvis", new Item()),
                setRegistryName("mutant_skeleton_skull", new MutantSkeletonArmorItem(EntityEquipmentSlot.HEAD)),
                setRegistryName("mutant_skeleton_chestplate", new MutantSkeletonArmorItem(EntityEquipmentSlot.CHEST)),
                setRegistryName("mutant_skeleton_leggings", new MutantSkeletonArmorItem(EntityEquipmentSlot.LEGS)),
                setRegistryName("mutant_skeleton_boots", new MutantSkeletonArmorItem(EntityEquipmentSlot.FEET)));
    }

    @SubscribeEvent
    public static void registerEntityEntries(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(
                createEntityEntry("body_part", BodyPartEntity.class).tracker(64, 10, true).build(),
                createEntityEntry("chemical_x", ChemicalXEntity.class).tracker(160, 10, true).build(),
                createEntityEntry("creeper_minion", CreeperMinionEntity.class, 894731, 12040119).build(),
                createEntityEntry("creeper_minion_egg", CreeperMinionEggEntity.class).tracker(160, 20, true).build(),
                createEntityEntry("endersoul_clone", EndersoulCloneEntity.class, 15027455, 15027455).build(),
                createEntityEntry("endersoul_fragment", EndersoulFragmentEntity.class).tracker(64, 10, true).build(),
                createEntityEntry("mutant_arrow", MutantArrowEntity.class).tracker(80, 3, false).build(),
                createEntityEntry("mutant_creeper", MutantCreeperEntity.class, 5349438, 11013646).build(),
                createEntityEntry("mutant_enderman", MutantEndermanEntity.class, 1447446, 8860812).build(),
                createEntityEntry("mutant_skeleton", MutantSkeletonEntity.class, 12698049, 6310217).build(),
                createEntityEntry("mutant_snow_golem", MutantSnowGolemEntity.class, 15073279, 16753434).build(),
                createEntityEntry("mutant_zombie", MutantZombieEntity.class, 7969893, 44975).build(),
                createEntityEntry("skull_spirit", SkullSpiritEntity.class).tracker(160, 20, false).build(),
                createEntityEntry("spider_pig", SpiderPigEntity.class, 3419431, 15771042).build(),
                createEntityEntry("throwable_block", ThrowableBlockEntity.class).tracker(64, 100, true).build());
        addSpawns();
    }

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                createSoundEvent("entity.creeper_minion.ambient"),
                createSoundEvent("entity.creeper_minion.death"),
                createSoundEvent("entity.creeper_minion.hurt"),
                createSoundEvent("entity.creeper_minion.primed"),
                createSoundEvent("entity.creeper_minion_egg.hatch"),
                createSoundEvent("entity.endersoul_clone.death"),
                createSoundEvent("entity.endersoul_clone.teleport"),
                createSoundEvent("entity.endersoul_fragment.explode"),
                createSoundEvent("entity.mutant_creeper.ambient"),
                createSoundEvent("entity.mutant_creeper.charge"),
                createSoundEvent("entity.mutant_creeper.death"),
                createSoundEvent("entity.mutant_creeper.hurt"),
                createSoundEvent("entity.mutant_enderman.ambient"),
                createSoundEvent("entity.mutant_enderman.death"),
                createSoundEvent("entity.mutant_enderman.hurt"),
                createSoundEvent("entity.mutant_enderman.morph"),
                createSoundEvent("entity.mutant_enderman.scream"),
                createSoundEvent("entity.mutant_enderman.stare"),
                createSoundEvent("entity.mutant_enderman.teleport"),
                createSoundEvent("entity.mutant_skeleton.ambient"),
                createSoundEvent("entity.mutant_skeleton.death"),
                createSoundEvent("entity.mutant_skeleton.hurt"),
                createSoundEvent("entity.mutant_skeleton.step"),
                createSoundEvent("entity.mutant_snow_golem.death"),
                createSoundEvent("entity.mutant_snow_golem.hurt"),
                createSoundEvent("entity.mutant_zombie.ambient"),
                createSoundEvent("entity.mutant_zombie.attack"),
                createSoundEvent("entity.mutant_zombie.death"),
                createSoundEvent("entity.mutant_zombie.grunt"),
                createSoundEvent("entity.mutant_zombie.hurt"),
                createSoundEvent("entity.mutant_zombie.roar"),
                createSoundEvent("entity.spider_pig.ambient"),
                createSoundEvent("entity.spider_pig.death"),
                createSoundEvent("entity.spider_pig.hurt"));
        EntityParrot.registerMimicSound(MutantCreeperEntity.class, SoundEvents.E_PARROT_IM_CREEPER);
        EntityParrot.registerMimicSound(MutantSkeletonEntity.class, SoundEvents.E_PARROT_IM_SKELETON);
        EntityParrot.registerMimicSound(MutantZombieEntity.class, SoundEvents.E_PARROT_IM_ZOMBIE);
    }

    @SubscribeEvent
    public static void fixMissingMappings(RegistryEvent.MissingMappings<SoundEvent> event) {
        for (UnmodifiableIterator<RegistryEvent.MissingMappings.Mapping<SoundEvent>> unmodifiableIterator = event.getMappings().iterator(); unmodifiableIterator.hasNext(); ) {
            RegistryEvent.MissingMappings.Mapping<SoundEvent> mapping = unmodifiableIterator.next();
            if (mapping.key.getPath().startsWith("entity.mutant_husk"))
                mapping.ignore();
        }
    }

    private static <T extends EntityLiving> EntityEntryBuilder<T> createEntityEntry(String name, Class<T> entityClass, int eggPrimary, int eggSecondary) {
        return createEntityEntry(name, entityClass).egg(eggPrimary, eggSecondary).tracker(80, 3, true);
    }

    private static <T extends Entity> EntityEntryBuilder<T> createEntityEntry(String name, Class<T> entityClass) {
        if (EntityLiving.class.isAssignableFrom(entityClass))
            LootTableList.register(MutantBeasts.prefix("entities/" + name));
        return EntityEntryBuilder.<T>create().entity(entityClass).id(MutantBeasts.prefix(name), entityId++).name(Tags.MOD_ID + "." + name);
    }

    public static void addSpawns() {
        List<String> biomeWhitelist = Arrays.asList(MBConfig.biomeWhitelist);
        if (biomeWhitelist.isEmpty()) {
            MutantBeasts.LOGGER.warn("Biome whitelist is empty. No mutants will spawn any biomes!");
            return;
        }
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (biome.getRegistryName() == null || !biomeWhitelist.contains(biome.getRegistryName().getNamespace()))
                continue;
            List<Biome.SpawnListEntry> monsterEntries = biome.getSpawnableList(EnumCreatureType.MONSTER);
            if (monsterEntries.isEmpty())
                continue;
            if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.MUSHROOM) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.VOID)) {
                addSpawn(monsterEntries, MutantEndermanEntity.class, MBConfig.mutantEndermanSpawnRate, 1, 1);
                if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.END) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
                    addSpawn(monsterEntries, MutantCreeperEntity.class, MBConfig.mutantCreeperSpawnRate, 1, 1);
                    addSpawn(monsterEntries, MutantSkeletonEntity.class, MBConfig.mutantSkeletonSpawnRate, 1, 1);
                    addSpawn(monsterEntries, MutantZombieEntity.class, MBConfig.mutantZombieSpawnRate, 1, 1);
                }
            }
        }
    }

    private static void addSpawn(List<Biome.SpawnListEntry> spawnListEntries, Class<? extends EntityLiving> entityClass, int weight, int min, int max) {
        if (weight > 0)
            spawnListEntries.add(new Biome.SpawnListEntry(entityClass, weight, min, max));
    }

    private static SoundEvent createSoundEvent(String name) {
        ResourceLocation registryName = MutantBeasts.prefix(name);
        return (new SoundEvent(registryName)).setRegistryName(registryName);
    }

    private static <T extends IForgeRegistryEntry<T>> T setRegistryName(String name, T entry) {
        ResourceLocation registryName = MutantBeasts.prefix(name);
        if (entry instanceof Item) {
            ((Item) entry).setTranslationKey(Tags.MOD_ID + "." + registryName.getPath());
            ((Item) entry).setCreativeTab(MutantBeasts.CREATIVE_TAB);
        }
        return entry.setRegistryName(registryName);
    }
}
