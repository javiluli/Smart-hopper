package com.javiluli.smarthopper.register;

import com.javiluli.smarthopper.SmartHopperMod;
import com.javiluli.smarthopper.inventory.HopperGridMenu;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registro de tipos de inventario (Menús) y componentes de datos (Data Components).
 */
public class ModMenuTypes {
    
    // Registro para los tipos de Menú (GUIs)
    public static final DeferredRegister<MenuType<?>> MENUS = 
            DeferredRegister.create(Registries.MENU, SmartHopperMod.MODID);

    /**
     * Registra el tipo de contenedor para la interfaz de la rejilla.
     * IForgeMenuType.create permite que el menú pase datos adicionales entre servidor y cliente si fuera necesario.
     */
    public static final RegistryObject<MenuType<HopperGridMenu>> HOPPER_GRID_MENU = MENUS.register("hopper_grid_menu",
            () -> IForgeMenuType.create(HopperGridMenu::new));

    // Registro para los Data Component Types (Nuevo sistema de la 1.21 para guardar datos en ítems)
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = 
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, SmartHopperMod.MODID);

    /**
     * Registra el componente "filter_item".
     * Este componente permite que un ItemStack (la rejilla) guarde otro ItemStack dentro (el filtro).
     */
    @SuppressWarnings("null")
	public static final RegistryObject<DataComponentType<ItemStack>> FILTER_ITEM = 
            DATA_COMPONENT_TYPES.register("filter_item", () -> DataComponentType.<ItemStack>builder()
                // .persistent hace que el dato se guarde en el archivo del mundo (NBT) usando CODECs
                .persistent(ItemStack.CODEC) 
                .build());
}