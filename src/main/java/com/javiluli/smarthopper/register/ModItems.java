package com.javiluli.smarthopper.register;

import com.javiluli.smarthopper.SmartHopperMod;
import com.javiluli.smarthopper.item.HopperGridItem;
import com.javiluli.smarthopper.item.InverseHopperGridItem;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registro de todos los objetos (ítems) del mod.
 */
public class ModItems {

	// Registro diferido para los Ítems, vinculado a tu MODID.
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, SmartHopperMod.MODID);

	// Este es el ítem "inerte". No necesita clase propia porque no hace nada.
	// El paso previo (Inerte)
	public static final RegistryObject<Item> RAW_GRID = ITEMS.register("raw_grid",
			() -> new Item(new Item.Properties()));
	
	public static final RegistryObject<Item> INVERSE_RAW_GRID = ITEMS.register("inverse_raw_grid",
			() -> new Item(new Item.Properties()));

	/**
	 * Registramos la Rejilla de la Tolva. Usamos nuestra clase personalizada
	 * HopperGridItem para que tenga la lógica de filtros.
	 */
	public static final RegistryObject<Item> HOPPER_GRID = ITEMS.register("hopper_grid",
			() -> new HopperGridItem(new Item.Properties().stacksTo(64)));

	/**
	 * Registramos el ítem del bloque Smart Hopper. BlockItem es una clase especial
	 * que, al usarse contra el suelo, coloca el bloque asociado.
	 */
	@SuppressWarnings("null")
	public static final RegistryObject<Item> SMART_HOPPER_ITEM = ITEMS.register("smart_hopper",
			() -> new BlockItem(ModBlocks.SMART_HOPPER.get(), new Item.Properties()));
	

	public static final RegistryObject<Item> INVERSE_HOPPER_GRID = ITEMS.register("inverse_hopper_grid",
	        () -> new InverseHopperGridItem(new Item.Properties().stacksTo(64)));
}