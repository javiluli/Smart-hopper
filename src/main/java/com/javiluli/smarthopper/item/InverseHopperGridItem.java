package com.javiluli.smarthopper.item;

import com.javiluli.smarthopper.item.base.ConfigurableHopperGridItem;

import net.minecraft.world.item.ItemStack;

/**
 * Define el comportamiento del objeto "Rejilla" en el inventario y su uso
 * manual.
 */
public class InverseHopperGridItem extends ConfigurableHopperGridItem {
	public InverseHopperGridItem(Properties props) {
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
		// Si no hay filtro, en una lista negra dejamos pasar todo
		if (filter.isEmpty())
			return true;
		// Denegamos si es el mismo ítem
		return !ItemStack.isSameItem(candidate, filter);
	}
}