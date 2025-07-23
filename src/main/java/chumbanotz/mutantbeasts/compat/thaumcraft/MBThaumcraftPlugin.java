package chumbanotz.mutantbeasts.compat.thaumcraft;

import chumbanotz.mutantbeasts.MutantBeasts;
import chumbanotz.mutantbeasts.item.MBItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectEventProxy;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

@SuppressWarnings("deprecation")
public class MBThaumcraftPlugin {
    @SubscribeEvent
    public static void registerAspects(AspectRegistryEvent event) {
        AspectEventProxy proxy = event.register;

        // Items
        proxy.registerObjectTag(new ItemStack(MBItems.CHEMICAL_X), new AspectList().add(Aspect.ALCHEMY, 12).add(Aspect.DEATH, 10).add(Aspect.MAGIC, 8).add(Aspect.EXCHANGE, 8));
        proxy.registerObjectTag(new ItemStack(MBItems.CREEPER_SHARD), new AspectList().add(Aspect.ENTROPY, 23).add(Aspect.FIRE, 18).add(Aspect.ALCHEMY, 14));
        proxy.registerObjectTag(new ItemStack(MBItems.ENDERSOUL_HAND), new AspectList().add(Aspect.MOTION, 20).add(Aspect.ELDRITCH, 20).add(Aspect.SOUL, 15).add(Aspect.MAGIC, 10).add(Aspect.EARTH, 10));
        proxy.registerObjectTag(new ItemStack(MBItems.HULK_HAMMER), new AspectList().add(Aspect.ENTROPY, 20).add(Aspect.EARTH, 20).add(Aspect.AVERSION, 20));
        proxy.registerObjectTag(new ItemStack(MBItems.MUTANT_SKELETON_LIMB), new AspectList().add(Aspect.DEATH, 8).add(Aspect.LIFE, 8).add(Aspect.PROTECT, 8));
        proxy.registerObjectTag(new ItemStack(MBItems.MUTANT_SKELETON_PELVIS), new AspectList().add(Aspect.DEATH, 8).add(Aspect.LIFE, 8).add(Aspect.PROTECT, 8));
        proxy.registerObjectTag(new ItemStack(MBItems.MUTANT_SKELETON_RIB), new AspectList().add(Aspect.DEATH, 8).add(Aspect.LIFE, 8).add(Aspect.PROTECT, 8));
        proxy.registerObjectTag(new ItemStack(MBItems.MUTANT_SKELETON_SHOULDER_PAD), new AspectList().add(Aspect.DEATH, 8).add(Aspect.BEAST, 8).add(Aspect.PROTECT, 8));
        proxy.registerObjectTag(new ItemStack(MBItems.MUTANT_SKELETON_SKULL, 1, OreDictionary.WILDCARD_VALUE), new AspectList().add(Aspect.DEATH, 16).add(Aspect.LIFE, 16).add(Aspect.PROTECT, 16));

        // Entities
        ThaumcraftApi.registerEntityTag(MutantBeasts.MOD_ID + "." + "creeper_minion", new AspectList().add(Aspect.PLANT, 5).add(Aspect.FIRE, 5));
        ThaumcraftApi.registerEntityTag(MutantBeasts.MOD_ID + "." + "endersoul_clone", new AspectList().add(Aspect.ELDRITCH, 10).add(Aspect.MIND, 15).add(Aspect.SOUL, 5));
        ThaumcraftApi.registerEntityTag(MutantBeasts.MOD_ID + "." + "mutant_creeper", new AspectList().add(Aspect.ALCHEMY, 25).add(Aspect.PLANT, 25).add(Aspect.FIRE, 25).add(Aspect.ENTROPY, 25));
        ThaumcraftApi.registerEntityTag(MutantBeasts.MOD_ID + "." + "mutant_enderman", new AspectList().add(Aspect.ALCHEMY, 25).add(Aspect.MIND, 25).add(Aspect.ELDRITCH, 25).add(Aspect.MOTION, 25).add(Aspect.MAGIC, 25));
        ThaumcraftApi.registerEntityTag(MutantBeasts.MOD_ID + "." + "mutant_skeleton", new AspectList().add(Aspect.ALCHEMY, 25).add(Aspect.UNDEAD, 25).add(Aspect.MAN, 25).add(Aspect.MOTION, 25));
        ThaumcraftApi.registerEntityTag(MutantBeasts.MOD_ID + "." + "mutant_snow_golem", new AspectList().add(Aspect.ALCHEMY, 20).add(Aspect.COLD, 20).add(Aspect.MAN, 15).add(Aspect.MECHANISM, 15).add(Aspect.MAGIC, 15));
        ThaumcraftApi.registerEntityTag(MutantBeasts.MOD_ID + "." + "mutant_zombie", new AspectList().add(Aspect.ALCHEMY, 25).add(Aspect.UNDEAD, 25).add(Aspect.MAN, 25).add(Aspect.ENTROPY, 25));
        ThaumcraftApi.registerEntityTag(MutantBeasts.MOD_ID + "." + "spider_pig", new AspectList().add(Aspect.ALCHEMY, 20).add(Aspect.MOTION, 15).add(Aspect.TRAP, 15).add(Aspect.BEAST, 20));
    }
}
