package chenjunfu2.earlycompat.util;

import net.minecraft.block.BlockState;
import org.jetbrains.annotations.NotNull;

public interface BlockProtocolStateAdapter
{
	int earlycompat$toProtocolValue(int protocolValue, BlockState fromState);
	@NotNull BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState);
	@NotNull ProtocolType earlycompat$getProtocolType();
	
	enum ProtocolType
	{
		ADDED,//添加模式-在原协议值上添加额外状态->协议识别位(bit3)不可使用，协议方向位(bit0~2)不可使用，协议高位根据实际情况选择使用
		REPLACE,//替换模式-直接替换协议为自定义协议值->整个值空间都可使用
	}
	
	/*
		对于VerticallyAttachableBlock来说，默认情况下，protocol的最低位始终用来区分是Wall(1)还是Norm(0)，bit0使用后自动丢弃，用户不可知
		如果是Wall情况，那么还会使用bit1~bit2用来存储4个方向，使用后自动丢弃，用户不可知，剩余的值作为自定义空间，且必须设置状态为REPLACE，否则结果不正确
	*/
}
