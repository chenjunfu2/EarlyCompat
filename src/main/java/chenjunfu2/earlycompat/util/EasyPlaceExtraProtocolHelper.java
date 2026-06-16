package chenjunfu2.earlycompat.util;

public class EasyPlaceExtraProtocolHelper
{
	public static boolean isExtraProtocol(int protocolValue)
	{
		return (protocolValue & 0b0000_1000) == 0b0000_1000;//判断bit3是否为1，是的话则为扩展协议
	}
	
	public static int getExtraProtocolRawValue(int protocolValue)
	{
		return (((protocolValue & 0b1111_0000) >>> 1) | (protocolValue & 0b0000_0111));//摘除bit3，拼接剩余位，一共剩余6bit可用，最大7bit，因为bit3作为协议判断所以少一位
	}
}
