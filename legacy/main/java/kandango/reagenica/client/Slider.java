package kandango.reagenica.client;

import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SliderValuePacket;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class Slider extends AbstractSliderButton {
    private final BlockPos pos;
    public Slider(int x, int y, int width, int height, Component message, double value, BlockPos pos) {
        super(x, y, width, height, message, value);
        this.pos = pos;
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.literal("値: " + (int)(value * 100)));
    }

    @Override
    protected void applyValue() {
        ModMessages.CHANNEL.sendToServer(new SliderValuePacket(pos, value));
    }
}
