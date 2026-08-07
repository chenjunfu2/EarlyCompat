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
	
	public static final ConfigBoolean EASY_PLACE_V2_PROTOCOL_EXTRA = new ConfigBoolean("easyPlaceV2ProtocolExtra", true, "easyPlaceV2ProtocolExtra_Comment");
	public static final ConfigBoolean EASY_PLACE_RAIL_BLOCK_NO_SHAPE_UPDATE = new ConfigBoolean("easyPlaceRailBlockNoShapeUpdate", true, "easyPlaceRailBlockNoShapeUpdate_Comment");
	
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
