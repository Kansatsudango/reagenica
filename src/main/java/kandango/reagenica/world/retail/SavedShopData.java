package kandango.reagenica.world.retail;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

public class SavedShopData extends SavedData{
  private final Map<String, RetailCompanyData> companies;
  private SavedShopData(){
    this.companies = new HashMap<>();
  }

  public RetailCompanyData getOrCreateCompany(String name, UUID requestor){
    return companies.computeIfAbsent(name, n -> new RetailCompanyData(requestor, this::setDirty));
  }
  public Optional<RetailCompanyData> getCompany(String name){
    return Optional.ofNullable(companies.get(name));
  }

  public static SavedShopData load(CompoundTag tag) {
    SavedShopData data = new SavedShopData();
    CompoundTag list = tag.getCompound("Datas");
    for (String name : list.getAllKeys()) {
      data.companies.put(name, RetailCompanyData.loadFromCompoundTag(list.getCompound(name), data::setDirty));
    }
    return data;
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    CompoundTag list = new CompoundTag();
    companies.forEach((name, data) -> list.put(name, data.encodeAsCompoundTag()));
    tag.put("Datas", list);
    return tag;
  }

  public static SavedShopData get(MinecraftServer server){
    return server.overworld().getDataStorage().computeIfAbsent(
      SavedShopData::load, 
      SavedShopData::new, 
      "reagenica_shopdatas");
  }
}
