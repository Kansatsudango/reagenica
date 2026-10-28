package kandango.reagenica.generator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.item.reagent.Reagent;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class RawJsonProvider implements DataProvider{
  private final PackOutput output;
  public RawJsonProvider(PackOutput output) {
    this.output = output;
  }

  private List<RawJsonGenerator> processes(){
    List<RawJsonGenerator> list = new ArrayList<>();
    ChemiItems.listReagents.stream().map(r -> new CreateFillReagenicaTesttubeRecipeGenerator(r)).forEach(x -> list.add(x));
    return list;
  }

  @Override
  public CompletableFuture<?> run(CachedOutput cache) {
    List<CompletableFuture<?>> list = new ArrayList<>();
    processes().forEach(r -> r.register(cache).ifPresent(cf -> list.add(cf)));
    return CompletableFuture.allOf(list.toArray(i -> new CompletableFuture[i]));
  }

  @Override
  public String getName() {
    return "Reagenica Raw Json Generator System";
  }
  private class CreateFillReagenicaTesttubeRecipeGenerator implements RawJsonGenerator{
    private final RegistryObject<? extends Reagent> REAGENT;
    public CreateFillReagenicaTesttubeRecipeGenerator(RegistryObject<? extends Reagent> reagent){
      REAGENT = reagent;
    }
    public Optional<CompletableFuture<?>> register(CachedOutput cache){
      Path path = output.getOutputFolder()
            .resolve("data/reagenica/recipes/create_compat")
            .resolve("filling_"+REAGENT.getId().getPath()+".json");
      return REAGENT.get().getRelativeFluidTag().map(tag -> DataProvider.saveStable(cache, buildRawJsonText(REAGENT.getId(), tag), path));
    }
    private JsonObject buildRawJsonText(ResourceLocation itemLoc, ResourceLocation tagLoc){
      String top = """
      {
        "type": "forge:conditional",
        "recipes": [
          {
            "conditions": [
              {
                "type": "forge:mod_loaded",
                "modid": "create"
              }
            ],
            "recipe": {
              "type": "create:filling",
              "ingredients": [
                {
                  "item": "reagenica:testtube"
                },
                {
                  "amount": 100,
          """;
      String mid = """
                  "nbt": {}
                }
              ],
              "results": [
                {
          """;
      String bottom = """
                }
              ]
            }
          }
        ]
      }
          """;
      String fluidTagName = "\"fluidTag\": \""+tagLoc.toString()+"\",\n";
      String itemName = "\"item\": \""+itemLoc.toString()+"\"\n";
      return JsonParser.parseString(top+fluidTagName+mid+itemName+bottom).getAsJsonObject();
    }
  }
  private interface RawJsonGenerator {
    public Optional<CompletableFuture<?>> register(CachedOutput cache);
  }
}
