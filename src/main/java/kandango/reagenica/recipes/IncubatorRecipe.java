package kandango.reagenica.recipes;

import java.util.List;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.item.bioreagent.BioGrowingPlate;
import kandango.reagenica.recipes.items.ItemStackWithChance;
import net.minecraft.world.item.ItemStack;

public record IncubatorRecipe(ItemStack in, List<ItemStackWithChance> out) {
  public static List<IncubatorRecipe> getRecipes(){
    return List.of(
      new IncubatorRecipe(BioGrowingPlate.getPlate("Crude", 0, false), 
        List.of(new ItemStackWithChance(new ItemStack(ChemiItems.CONTAMINATED_PLATE.get()), 0.4f),
                new ItemStackWithChance(new ItemStack(ChemiItems.YEAST.get()), 0.2f),
                new ItemStackWithChance(new ItemStack(ChemiItems.ORYZAE.get()), 0.2f),
                new ItemStackWithChance(new ItemStack(ChemiItems.ACETOBACTER.get()), 0.2f)
              )
      ),
      new IncubatorRecipe(BioGrowingPlate.getPlate("Yeast", 0, false), 
        List.of(new ItemStackWithChance(new ItemStack(ChemiItems.YEAST.get()), 1.0f))
      ),
      new IncubatorRecipe(BioGrowingPlate.getPlate("Oryzae", 0, false), 
        List.of(new ItemStackWithChance(new ItemStack(ChemiItems.ORYZAE.get()), 1.0f))
      ),
      new IncubatorRecipe(BioGrowingPlate.getPlate("Acetobacter", 0, false), 
        List.of(new ItemStackWithChance(new ItemStack(ChemiItems.ACETOBACTER.get()), 1.0f))
      )
    );
  }
}