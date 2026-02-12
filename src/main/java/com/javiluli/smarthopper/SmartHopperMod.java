package com.javiluli.smarthopper;

// Importaciones de tus propias clases (Lógica del mod)
import com.javiluli.smarthopper.client.HopperGridScreen;
import com.javiluli.smarthopper.client.SmartHopperRenderer;
import com.javiluli.smarthopper.register.ModBlockEntities;
import com.javiluli.smarthopper.register.ModBlocks;
import com.javiluli.smarthopper.register.ModCreativeModeTabs;
import com.javiluli.smarthopper.register.ModItems;
import com.javiluli.smarthopper.register.ModMenuTypes;

// Importaciones de Minecraft y Forge
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Clase principal del Mod.
 * La anotación @Mod vincula este código con el archivo mods.toml usando el MODID.
 */
@Mod(SmartHopperMod.MODID)
public class SmartHopperMod {
    // El ID del mod es la "llave" para todos sus recursos (texturas, modelos, etc.)
    public static final String MODID = "smarthopper";

    @SuppressWarnings("removal")
	public SmartHopperMod() {
        // Obtenemos el bus de eventos de Forge para registrar nuestras cosas durante el inicio
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registramos los Bloques (físicos)
        ModBlocks.BLOCKS.register(bus);
        // Registramos los Ítems (inventario)
        ModItems.ITEMS.register(bus);
        // Registramos las Entidades de Bloque (la lógica/memoria de la tolva)
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        // Registramos los tipos de Menú (Interfaces de usuario)
        ModMenuTypes.MENUS.register(bus);
        // Registramos los componentes de datos (para guardar info en los items en 1.21)
        ModMenuTypes.DATA_COMPONENT_TYPES.register(bus);
        
        // Pestaña propia en el modo creativo
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(bus);
        
        // Escuchamos el evento para añadir nuestros ítems a las pestañas del creativo
        bus.addListener(this::addCreative);
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Este método inyecta nuestros objetos en el menú creativo de Minecraft.
     */
    public void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Verificamos si la pestaña que se está cargando es Redstone o Bloques Funcionales
    	if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.RAW_GRID); // Añade la rejilla base a la pestaña de ingredientes
        }
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.SMART_HOPPER_ITEM); // Tu tolva inteligente
            event.accept(ModItems.HOPPER_GRID);
            event.accept(ModItems.INVERSE_RAW_GRID);
            event.accept(ModItems.INVERSE_HOPPER_GRID);
        }
    }

    /**
     * Sub-clase para eventos que SOLO ocurren en el cliente (PC del jugador).
     * Evita que el servidor intente cargar gráficos y crashee.
     */
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        
        /**
         * Registra los renderizadores especiales (para ver items dentro del bloque, etc.)
         */
        @SuppressWarnings("null")
		@SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            // Vincula nuestra entidad de bloque con su clase de renderizado visual
            event.registerBlockEntityRenderer(ModBlockEntities.SMART_HOPPER_BE.get(), SmartHopperRenderer::new);
        }

        /**
         * Configuración final del cliente (interfaces de usuario).
         */
        @SuppressWarnings("null")
		@SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // enqueueWork asegura que el registro sea seguro para los hilos de ejecución (Thread-safe)
            event.enqueueWork(() -> {
                // Conecta el Menú lógico con la Pantalla (GUI) visual
                MenuScreens.register(ModMenuTypes.HOPPER_GRID_MENU.get(), HopperGridScreen::new);
            });
        }
    }
}