package kandango.reagenica.worldgen;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.worldgen.crater.MegaCraterPiece;
import kandango.reagenica.worldgen.crater.MegaCraterStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ChemiStructures {
  public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, ChemistryMod.MODID);
  public static final DeferredRegister<StructurePieceType> PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, ChemistryMod.MODID);

  public static final RegistryObject<StructureType<MegaCraterStructure>> MEGA_CRATER = STRUCTURE_TYPES.register(
    "mega_crater", () -> () -> MegaCraterStructure.CODEC);

  public static final RegistryObject<StructurePieceType>
    MEGA_CRATER_PIECE =
    PIECES.register("mega_crater_piece",
        () -> MegaCraterPiece::new);
}
