package com.javiluli.smarthopper.register;

import com.javiluli.smarthopper.SmartHopperMod;
import com.javiluli.smarthopper.block.SmartHopperBlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registro de todos los bloques nuevos que añade el mod.
 */
public class ModBlocks {
    
    // Creamos el registro diferido para los Bloques (BLOCK) vinculándolo a tu MODID.
    public static final DeferredRegister<Block> BLOCKS = 
            DeferredRegister.create(Registries.BLOCK, SmartHopperMod.MODID);

    /**
     * Registramos el objeto físico de la Tolva Inteligente.
     * "smart_hopper" será el ID con el que la invocarás: /give @p smarthopper:smart_hopper
     */
    @SuppressWarnings("null")
	public static final RegistryObject<Block> SMART_HOPPER = BLOCKS.register("smart_hopper",
            () -> new SmartHopperBlock(
                    // Copiamos todas las propiedades de la tolva original de Minecraft
                    // (Dureza, explosión, sonidos al caminar, etc.)
                    BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER)
            ));
}