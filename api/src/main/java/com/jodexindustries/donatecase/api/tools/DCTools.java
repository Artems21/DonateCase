package com.jodexindustries.donatecase.api.tools;

import com.jodexindustries.donatecase.api.DCAPI;
import com.jodexindustries.donatecase.api.armorstand.ArmorStandCreator;
import com.jodexindustries.donatecase.api.chat.rgb.RGBUtils;
import com.jodexindustries.donatecase.api.data.casedata.CaseData;
import com.jodexindustries.donatecase.api.data.casedata.CaseDataItem;
import com.jodexindustries.donatecase.api.data.material.CaseMaterial;
import com.jodexindustries.donatecase.api.data.storage.CaseLocation;
import com.jodexindustries.donatecase.api.data.subcommand.SubCommand;
import com.jodexindustries.donatecase.api.manager.MaterialManager;
import com.jodexindustries.donatecase.api.platform.DCCommandSender;
import com.jodexindustries.donatecase.api.platform.DCOfflinePlayer;
import com.jodexindustries.donatecase.api.platform.DCPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility interface for the DonateCase system, providing tools for parsing, validation, and manipulation.
 */
public interface DCTools {

    ArmorStandCreator createArmorStand(CaseLocation location);

    PAPI getPAPI();

    Object loadCaseItem(String id);

    static boolean isValidPlayerName(String player) {
        if (DCAPI.getInstance().getConfig().getConfig().node("DonateCase", "CheckPlayerName").getBoolean()) {
            return Arrays.stream(DCAPI.getInstance().getPlatform().getOfflinePlayers())
                    .map(DCOfflinePlayer::getName)
                    .anyMatch(name -> name != null && name.equals(player.trim()));
        }
        return true;
    }

    static @NotNull List<String> resolveSDGCompletions(String[] args) {
        List<String> value = new ArrayList<>(DCAPI.getInstance().getConfig().getConfigCases().getMap().keySet());
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.addAll(
                    Arrays.stream(DCAPI.getInstance().getPlatform().getOnlinePlayers())
                    .map(DCPlayer::getName)
                    .filter(px -> px.startsWith(args[0]))
                    .collect(Collectors.toList())
            );
            return list;
        } else if (args.length >= 3) {
            if (args.length == 4) {
                list.add("-s");
                return list;
            }
            return new ArrayList<>();
        }
        if (args[args.length - 1].isEmpty()) {
            list = value;
        } else {
            list.addAll(
                    value.stream()
                    .filter(tmp -> tmp.startsWith(args[args.length - 1]))
                    .collect(Collectors.toList())
            );
        }
        return list;
    }

    @Nullable
    static Object getItemFromManager(@NotNull String id) {
        MaterialManager manager = DCAPI.getInstance().getMaterialManager();

        String temp = manager.getByStart(id);

        if (temp != null) {
            CaseMaterial caseMaterial = manager.getRegisteredMaterial(temp);
            if (caseMaterial != null) {
                String context = id.replace(temp, "").replaceFirst(":", "").trim();
                return caseMaterial.handle(context);
            }
        }
        return null;
    }

    static String prefix(String text) {
        return rc(DCAPI.getInstance().getConfig().getMessages().getString("prefix") + text);
    }

    static String rc(@NotNull String text) {
        return RGBUtils.getInstance().applyFormats(text);
    }

    static String rt(String text, String... repl) {
        if (text != null) {
            for (String s : repl) {
                if (s != null) {
                    int l = s.split(":")[0].length();
                    text = text.replace(s.substring(0, l), s.substring(l + 1));
                }
            }

            text = rc(text);
        }
        return text;
    }

    static List<String> rt(List<String> text, String... repl) {
        return text.stream().map(t -> rt(t, repl)).collect(Collectors.toCollection(ArrayList::new));
    }


    static List<String> rc(List<String> t) {
        return t.stream().map(DCTools::rc).collect(Collectors.toCollection(ArrayList::new));
    }

    static boolean isHasCommandForSender(DCCommandSender sender, Map<String, List<Map<String, SubCommand>>> addonsMap, String addon) {
        List<Map<String, SubCommand>> commands = addonsMap.get(addon);
        return isHasCommandForSender(sender, commands);
    }

    static boolean isHasCommandForSender(DCCommandSender sender, Map<String, List<Map<String, SubCommand>>> addonsMap) {
        return addonsMap.keySet().stream().map(addonsMap::get).anyMatch(commands -> isHasCommandForSender(sender, commands));
    }

    /**
     * Check sender for permission to executing commands
     * Checks only if sender has permission for one or more commands, not all
     * @param sender Player or Console
     * @param commands List of commands, that loaded in DonateCase
     * @return true, if sender has permission
     */
    static boolean isHasCommandForSender(DCCommandSender sender, List<Map<String, SubCommand>> commands) {
        return commands.stream().flatMap(command -> command.values().stream()).map(SubCommand::getPermission).anyMatch(permission -> permission == null || sender.hasPermission(permission));
    }

    /**
     * Extracts the local placeholder from a string, delimited by `%`.
     *
     * @param string the input string containing placeholders in the format `%placeholder%`.
     * @return the extracted placeholder without `%` symbols, or "null" if no placeholder is found.
     */
    static String getLocalPlaceholder(String string) {
        Pattern pattern = Pattern.compile("%(.*?)%");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            int startIndex = string.indexOf("%") + 1;
            int endIndex = string.lastIndexOf("%");
            return string.substring(startIndex, endIndex);
        } else {
            return "null";
        }
    }

    /**
     * Parses a plugin version string into a numbered version.
     *
     * @param version the version string to be parsed (e.g., "2.2.2").
     * @return the numeric representation of the version (e.g., "2220").
     * <p>
     * Examples:
     * <ul>
     *     <li>Input: <code>"2.2.2"</code> → Output: <code>2220</code></li>
     *     <li>Input: <code>"2.2.2.2"</code> → Output: <code>2222</code></li>
     * </ul>
     */
    static int getPluginVersion(String version) {
        version = version.replaceAll("\\.", "");
        if (version.length() == 4) {
            return Integer.parseInt(version);
        }
        version = version.concat("0000");
        return Integer.parseInt(version.substring(0, 4));
    }

    /**
     * Extracts a cooldown value from an action string.
     *
     * @param action the action string containing a cooldown in the format <code>[cooldown:int]</code>.
     * @return the cooldown value, or 0 if no cooldown is specified.
     */
    static int extractCooldown(String action) {
        Pattern pattern = Pattern.compile("\\[cooldown:(.*?)]");
        Matcher matcher = pattern.matcher(action);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    /**
     * Sorts and filters case history data based on a specific case type.
     *
     * @param historyData the list of {@link CaseData.CaseDataHistory} objects to sort and filter.
     * @param caseType    the type of case to filter by.
     * @return a sorted list of {@link CaseData.CaseDataHistory}, filtered by the specified case type,
     * sorted in descending order of time.
     */
    static List<CaseData.CaseDataHistory> sortHistoryDataByCase(List<CaseData.CaseDataHistory> historyData, String caseType) {
        return historyData.stream()
                .filter(Objects::nonNull)
                .filter(data -> data.getCaseType().equals(caseType))
                .sorted(Comparator.comparingLong(CaseData.CaseDataHistory::getTime).reversed())
                .collect(Collectors.toList());
    }

    static List<CaseData.CaseDataHistory> sortHistoryDataByDate(List<CaseData.CaseDataHistory> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(CaseData.CaseDataHistory::getTime)
                        .reversed())
                .collect(Collectors.toList());
    }

    /**
     * Sort case items by index
     * @param items Map with Case items
     * @return New map with sorted items
     */
    static Map<String, CaseDataItem> sortItemsByIndex(Map<String, CaseDataItem> items) {
        return items.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(CaseDataItem::getIndex)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    static boolean isValidGuiSize(int size) {
        return size >= 9 && size <= 54 && size % 9 == 0;
    }
}
