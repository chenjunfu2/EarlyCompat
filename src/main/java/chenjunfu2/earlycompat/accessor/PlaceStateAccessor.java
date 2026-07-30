package chenjunfu2.earlycompat.accessor;

import java.util.Map;

public interface PlaceStateAccessor
{
	boolean earlycompat$isEasyPlaceState();
	
	long earlycompat$placeProperty();
	void earlycompat$placeProperty(long val);
	
	default boolean earlycompat$hasPlaceFlag(long flag)
	{
        return (earlycompat$placeProperty() & flag) == flag;
    }
	
	default void earlycompat$setPlaceFlag(long flag)
	{
		earlycompat$placeProperty(earlycompat$placeProperty() | flag);
    }
	
	long easyPlaceRailBlockNoShapeUpdate = 1L << 0;
}
