package kandango.reagenica.block.entity.lamp;

public interface ILampController {
  public LampStates getLampStates();
  public void receivePacket(LampStates states);
}
