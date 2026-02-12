package com.javiluli.smarthopper.register;

import com.javiluli.smarthopper.SmartHopperMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SmartHopperMod.MODID);

    @SuppressWarnings("null")
	public static final RegistryObject<CreativeModeTab> SMART_HOPPER_TAB = CREATIVE_MODE_TABS.register("smarthopper_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.HOPPER_GRID.get())) // El icono de la pestaña
                    .title(Component.translatable("itemGroup.smarthopper_tab"))
                    .displayItems((parameters, output) -> {
                        // Añadimos los ítems en el orden que queramos que aparezcanw
                        output.accept(ModItems.RAW_GRID.get());
                        output.accept(ModItems.INVERSE_RAW_GRID.get());
                        output.accept(ModItems.SMART_HOPPER_ITEM.get());
                        output.accept(ModItems.HOPPER_GRID.get());
                        output.accept(ModItems.INVERSE_HOPPER_GRID.get());
                    })
                    .build());
}