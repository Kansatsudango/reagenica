package kandango.reagenica.enchantment;

import java.util.Optional;

import kandango.reagenica.family.CrystalFamily;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CrystalizedEnchantment{
  public static Optional<ItemEntity> loot(LivingEntity entity, int enchLevel){
    RandomSource random = entity.getRandom();
    if(random.nextInt(100) < enchLevel+1){
      Item crystal = CrystalFamily.Crystals.get(random.nextInt(CrystalFamily.Crystals.size())).SHARD_ITEM.get();
      ItemStack drop = new ItemStack(crystal);
      ItemEntity item = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), drop);
      return Optional.of(item);
    }else{
      return Optional.empty();
    }
  }
}
