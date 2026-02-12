package com.javiluli.smarthopper.item;

import com.javiluli.smarthopper.item.base.ConfigurableHopperGridItem;

import net.minecraft.world.item.ItemStack;

/**
 * Define el comportamiento del objeto "Rejilla" en el inventario y su uso
 * manual.
 */
public class HopperGridItem extends ConfigurableHopperGridItem {
	public HopperGridItem(Properties props) {
		super(props);
	}

	/**
	 * LÓGICA DE FILTRADO: Este es el método que heredarán los demás. Por defecto
	 * funciona como LISTA BLANCA.
	 */
	@SuppressWarnings("null")
	@Override
	public boolean canItemPass(ItemStack gridStack, ItemStack candidate) {
		ItemStack filter = getStoredStack(gridStack);
		if (filter.isEmpty())
			return false; // Si no hay nada configurado, no pasa nada
		return ItemStack.isSameItem(candidate, filter);
	}

}