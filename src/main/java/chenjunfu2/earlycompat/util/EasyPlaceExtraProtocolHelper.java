package chenjunfu2.earlycompat.util;

import net.minecraft.util.math.Vec3d;

public class EasyPlaceExtraProtocolHelper
{
	public static boolean isExtraProtocol(int protocolValue)
	{
		return (protocolValue & 0b0000_1000) == 0b0000_1000;//判断bit3是否为1，是的话则为扩展协议
	}
	
	public static int decodeProtocolValue(double relativeHitX)
	{
		return ((int)relativeHitX - 2) >>> 1;
	}
	
	public static int decodeExtraProtocolRawValue(int protocolValue)
	{
		return (((protocolValue & 0b1111_0000) >>> 1) | (protocolValue & 0b0000_0111));//摘除bit3，拼接剩余位，一共剩余6bit可用，最大7bit，因为bit3作为协议判断所以少一位
	}
	
	public static int addExtraProtocolBit(int protocolValue)
	{
		return protocolValue | 0b0000_1000;
	}
	
	public static int removeExtraProtocolBit(int protocolValue)
	{
		return protocolValue & ~((int)0b0000_1000);
	}
	
	public static int toExtraProtocolValue(int protocolValue)
	{
		return ((protocolValue & 0b0111_1000) << 1) | (protocolValue & 0b0000_0111) | 0b0000_1000;//把值从bit3中间分开，设置bit3为1
	}
	
	public static Vec3d encodeExtraProtocolRawValue(int protocolRawValue, Vec3d hitVec)//值最多7bit
	{
		int protocolValue = toExtraProtocolValue(protocolRawValue);
		return new Vec3d(hitVec.x + (double)(protocolValue * 2 + 2), hitVec.y, hitVec.z);
	}
}
