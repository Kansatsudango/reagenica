package kandango.reagenica.block.entity;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.entity.electrical.*;
import kandango.reagenica.ChemiBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ChemistryMod.MODID);

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<DraftChamberBlockEntity>> DRAFT_CHAMBER =
        BLOCK_ENTITIES.register("experiment_block_entity",
                () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<DraftChamberBlockEntity>)DraftChamberBlockEntity::new,
                        ChemiBlocks.EXPERIMENT_BLOCK.get()).build(null));
    
    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<ChemicalFermenterBlockEntity>> CHEMICAL_FERMENTER = 
        BLOCK_ENTITIES.register("chemical_fermenter", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<ChemicalFermenterBlockEntity>)ChemicalFermenterBlockEntity::new,ChemiBlocks.CHEMICAL_FERMENTER.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<FractionalDistillerBlockEntity>> FRACTIONAL_DISTILLER = 
        BLOCK_ENTITIES.register("fractional_distiller", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<FractionalDistillerBlockEntity>)FractionalDistillerBlockEntity::new,ChemiBlocks.FRACTIONAL_DISTILLER_BOTTOM.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<FractionalDistillerSubBlockEntity>> FRACTIONAL_DISTILLER_SUB = 
        BLOCK_ENTITIES.register("fractional_distiller_sub", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<FractionalDistillerSubBlockEntity>)FractionalDistillerSubBlockEntity::new,ChemiBlocks.FRACTIONAL_DISTILLER_TOP.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<BlastFurnaceBlockEntity>> BLAST_FURNACE = 
        BLOCK_ENTITIES.register("blast_furnace", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<BlastFurnaceBlockEntity>)BlastFurnaceBlockEntity::new,ChemiBlocks.BLASTFURNACE_BOTTOM.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<BlastFurnaceSubBlockEntity>> BLAST_FURNACE_SUB = 
        BLOCK_ENTITIES.register("blast_furnace_sub", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<BlastFurnaceSubBlockEntity>)BlastFurnaceSubBlockEntity::new,ChemiBlocks.BLASTFURNACE_SUB.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<HeatFurnaceBlockEntity>> HEAT_FURNACE = 
        BLOCK_ENTITIES.register("heat_furnace", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<HeatFurnaceBlockEntity>)HeatFurnaceBlockEntity::new,ChemiBlocks.HEAT_FURNACE.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<CrusherBlockEntity>> CRUSHER = 
        BLOCK_ENTITIES.register("crusher", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<CrusherBlockEntity>)CrusherBlockEntity::new,ChemiBlocks.CRUSHER.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<AnalyzerBlockEntity>> ANALYZER = 
        BLOCK_ENTITIES.register("analyzer", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<AnalyzerBlockEntity>)AnalyzerBlockEntity::new,ChemiBlocks.ANALYZER.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<ElectroLysisBlockEntity>> ELECTROLYSIS_DEVICE = 
        BLOCK_ENTITIES.register("electrolysis_device", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<ElectroLysisBlockEntity>)ElectroLysisBlockEntity::new,ChemiBlocks.ELECTROLYSIS_DEVICE.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<DebugGeneratorBlockEntity>> GENERATOR_DEBUG = 
        BLOCK_ENTITIES.register("debug_generator", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<DebugGeneratorBlockEntity>)DebugGeneratorBlockEntity::new,ChemiBlocks.DEBUG_GENERTOR.get()).build(null));
    
    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<DebugEnergyConsumerBlockEntity>> ENERGYCONSUMER_DEBUG = 
        BLOCK_ENTITIES.register("debug_consumer", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<DebugEnergyConsumerBlockEntity>)DebugEnergyConsumerBlockEntity::new,ChemiBlocks.DEBUG_CONSUMER.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<ElectricCableCopperBlockEntity>> CABLE_COPPER = 
        BLOCK_ENTITIES.register("copper_cable", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<ElectricCableCopperBlockEntity>)ElectricCableCopperBlockEntity::new,ChemiBlocks.CABLE_COPPER.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<FuelGeneratorBlockEntity>> GENERATOR_FUEL = 
        BLOCK_ENTITIES.register("fuel_generator", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<FuelGeneratorBlockEntity>)FuelGeneratorBlockEntity::new,ChemiBlocks.FUEL_GENERATOR.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<FluidFuelGeneratorBlockEntity>> GENERATOR_FLUID = 
        BLOCK_ENTITIES.register("fluid_fuel_generator", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<FluidFuelGeneratorBlockEntity>)FluidFuelGeneratorBlockEntity::new,ChemiBlocks.FLUID_FUEL_GENERATOR.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<RadioIsotopeGeneratorBlockEntity>> GENERATOR_RADIOISOTOPE = 
        BLOCK_ENTITIES.register("radioisotope_generator", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<RadioIsotopeGeneratorBlockEntity>)RadioIsotopeGeneratorBlockEntity::new,ChemiBlocks.RADIOISOTOPE_GENERATOR.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<LeadBatteryBlockEntity>> LEAD_BATTERY = 
        BLOCK_ENTITIES.register("lead_battery", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<LeadBatteryBlockEntity>)LeadBatteryBlockEntity::new,ChemiBlocks.LEAD_BATTERY.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<TradingStallBlockEntity>> TRADING_STALL = 
        BLOCK_ENTITIES.register("trading_stall", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<TradingStallBlockEntity>)TradingStallBlockEntity::new,ChemiBlocks.TRADING_STALL.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<PipeCopperBlockEntity>> PIPE_COPPER = 
        BLOCK_ENTITIES.register("copper_pipe", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<PipeCopperBlockEntity>)PipeCopperBlockEntity::new,ChemiBlocks.PIPE_COPPER.get()).build(null));
        
    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<PipeGoldBlockEntity>> PIPE_GOLD = 
        BLOCK_ENTITIES.register("gold_pipe", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<PipeGoldBlockEntity>)PipeGoldBlockEntity::new,ChemiBlocks.PIPE_GOLD.get()).build(null));
        
    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<PipePVCBlockEntity>> PIPE_PVC = 
        BLOCK_ENTITIES.register("pvc_pipe", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<PipePVCBlockEntity>)PipePVCBlockEntity::new,ChemiBlocks.PIPE_PVC.get()).build(null));
    
    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<PumpBlockEntity>> PUMP = 
        BLOCK_ENTITIES.register("pump", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<PumpBlockEntity>)PumpBlockEntity::new,ChemiBlocks.PUMP.get()).build(null));

        @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<CopperTankBlockEntity>> COPPER_TANK = 
        BLOCK_ENTITIES.register("copper_tank", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<CopperTankBlockEntity>)CopperTankBlockEntity::new,ChemiBlocks.COPPER_TANK.get()).build(null));

        @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<DissolverBlockEntity>> DISSOLVER = 
        BLOCK_ENTITIES.register("dissolver", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<DissolverBlockEntity>)DissolverBlockEntity::new,ChemiBlocks.DISSOLVER.get()).build(null));

        @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<ReactorBlockEntity>> REACTOR = 
        BLOCK_ENTITIES.register("reactor", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<ReactorBlockEntity>)ReactorBlockEntity::new,ChemiBlocks.REACTOR.get()).build(null));

        @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<HeatGeneratorBlockEntity>> HEAT_GENERATOR = 
        BLOCK_ENTITIES.register("heat_generator", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<HeatGeneratorBlockEntity>)HeatGeneratorBlockEntity::new,ChemiBlocks.HEAT_GENERATOR.get()).build(null));

        @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<StackLampBlockEntity>> STACK_LAMP = 
        BLOCK_ENTITIES.register("stack_lamp", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<StackLampBlockEntity>)StackLampBlockEntity::new,ChemiBlocks.STACK_LAMP.get()).build(null));
    
        @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<CookingPotBlockEntity>> COOKING_POT = 
        BLOCK_ENTITIES.register("cooking_pot", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<CookingPotBlockEntity>)CookingPotBlockEntity::new,ChemiBlocks.COOKING_POT.get()).build(null));

        @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<IncubatorBlockEntity>> INCUBATOR = 
        BLOCK_ENTITIES.register("incubator", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<IncubatorBlockEntity>)IncubatorBlockEntity::new,ChemiBlocks.INCUBATOR.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<AirSeparatorBlockEntity>> AIR_SEPARATOR = 
        BLOCK_ENTITIES.register("air_separator", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<AirSeparatorBlockEntity>)AirSeparatorBlockEntity::new,ChemiBlocks.AIR_SEPARATOR.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<HaberBoschBlockEntity>> HABER_BOSCH = 
        BLOCK_ENTITIES.register("haber_bosch", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<HaberBoschBlockEntity>)HaberBoschBlockEntity::new,ChemiBlocks.HABER_BOSCH.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<LargeTankCoreBlockEntity>> LARGE_TANK_CORE = 
        BLOCK_ENTITIES.register("large_tank_core", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<LargeTankCoreBlockEntity>)LargeTankCoreBlockEntity::new,ChemiBlocks.LARGE_TANK_CORE.get()).build(null));

    @SuppressWarnings("null")
    public static final RegistryObject<BlockEntityType<LargeTankInterfaceBlockEntity>> LARGE_TANK_INTERFACE = 
        BLOCK_ENTITIES.register("large_tank_interface", 
        () -> BlockEntityType.Builder.of((BlockEntityType.BlockEntitySupplier<LargeTankInterfaceBlockEntity>)LargeTankInterfaceBlockEntity::new,ChemiBlocks.LARGE_TANK_INTERFACE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
