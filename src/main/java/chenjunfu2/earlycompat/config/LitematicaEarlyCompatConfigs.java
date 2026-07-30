package chenjunfu2.earlycompat.config;

import chenjunfu2.earlycompat.EarlyCompat;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.File;

@Environment(EnvType.CLIENT)
public class LitematicaEarlyCompatConfigs
{
	public static final String CONFIG_FILE_NAME = EarlyCompat.MOD_ID + ".json";
	
	public static final ConfigBoolean EASY_PLACE_V2_PROTOCOL_EXTRA = new ConfigBoolean("easyPlaceV2ProtocolExtra | 启用V2-Extra轻松放置协议", true, "在支持V2-Extra轻松放置协议的服务器中，启用客户端V2-Extra轻松放置协议");
	public static final ConfigBoolean EASY_PLACE_RAIL_BLOCK_NO_SHAPE_UPDATE = new ConfigBoolean("easyPlaceRailBlockNoShapeUpdate | 轻松放置铁轨方块不进行形状更新", true, "在使用支持V2-Extra轻松放置协议的服务器中，放置铁轨方块不进行形状更新");
	
	public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of
	(
		EASY_PLACE_V2_PROTOCOL_EXTRA,
		EASY_PLACE_RAIL_BLOCK_NO_SHAPE_UPDATE
	);
	
	public static void loadFromFile()
    {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead())
        {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();
                ConfigUtils.readConfigBase(root, "options", OPTIONS);
            }
        }
    }

    public static void saveToFile()
    {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
        {
            JsonObject root = new JsonObject();
            ConfigUtils.writeConfigBase(root, "options", OPTIONS);
            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }
}
