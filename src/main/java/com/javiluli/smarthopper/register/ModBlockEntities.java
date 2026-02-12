package com.javiluli.smarthopper.register;

import com.javiluli.smarthopper.SmartHopperMod;
import com.javiluli.smarthopper.block.SmartHopperBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Clase encargada de registrar los tipos de Block Entities del mod.
 */
public class ModBlockEntities {
    
    // Creamos el registro diferido para las Block Entities. 
    // Esto le dice a Forge: "Oye, guarda esta lista y regístrala cuando Minecraft esté listo".
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SmartHopperMod.MODID);

    /**
     * Registramos nuestra SmartHopperBlockEntity.
     * "smart_hopper_be" es el ID interno que usará Minecraft para identificar este tipo de entidad.
     */
    @SuppressWarnings("null")
	public static final RegistryObject<BlockEntityType<SmartHopperBlockEntity>> SMART_HOPPER_BE = BLOCK_ENTITIES
            .register("smart_hopper_be",
                    () -> BlockEntityType.Builder.of(
                        SmartHopperBlockEntity::new, // Referencia al constructor de tu clase lógica
                        ModBlocks.SMART_HOPPER.get()  // El bloque físico al que va unida esta lógica
                    ).build(null)); // El 'null' es para el DataFixer (no necesario en mods nuevos)
}