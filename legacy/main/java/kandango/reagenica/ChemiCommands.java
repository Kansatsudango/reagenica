package kandango.reagenica;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import kandango.reagenica.block.entity.electrical.ElectricGeneratorAbstract;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChemistryMod.MODID)
public class ChemiCommands {
  @SubscribeEvent
  public static void onCommandRegister(RegisterCommandsEvent event){
    CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

    dispatcher.register(Commands.literal("reageniengineer")
      .requires(source -> true)
      .then(Commands.argument("pos", BlockPosArgument.blockPos())
        .executes(ChemiCommands::reageniEngineer))
    );
  }
  public static int reageniEngineer(CommandContext<CommandSourceStack> context){
    CommandSourceStack source = context.getSource();
    try{
      BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
      ServerLevel lv = context.getSource().getLevel();
      BlockEntity be = lv.getBlockEntity(pos);
      if(be instanceof ElectricGeneratorAbstract gen){
        gen.tellCustomers(source);
      }else{
        source.sendFailure(Component.literal("This Block is not a Reagenica Generator."));
      }
      return 1;
    }catch(CommandSyntaxException e){
      source.sendFailure(Component.literal("Invalid Blockpos."));
      return 0;
    }
  }
}
