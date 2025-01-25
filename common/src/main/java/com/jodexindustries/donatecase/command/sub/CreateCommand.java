package com.jodexindustries.donatecase.command.sub;

import com.jodexindustries.donatecase.api.DCAPI;
import com.jodexindustries.donatecase.api.data.storage.CaseInfo;
import com.jodexindustries.donatecase.api.data.storage.CaseLocation;
import com.jodexindustries.donatecase.api.data.subcommand.SubCommandType;
import com.jodexindustries.donatecase.api.manager.HologramManager;
import com.jodexindustries.donatecase.api.platform.DCCommandSender;
import com.jodexindustries.donatecase.api.platform.DCPlayer;
import com.jodexindustries.donatecase.api.tools.DCTools;
import com.jodexindustries.donatecase.command.DefaultCommand;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CreateCommand extends DefaultCommand {
    
    private final DCAPI api;
    
    public CreateCommand(DCAPI api) {
        super(api, "create", SubCommandType.ADMIN);
        this.api = api;
    }

    @Override
    public boolean execute(@NotNull DCCommandSender sender, @NotNull String label, String[] args) {
        if (args.length < 2) {
            return false;
        }

        if (sender instanceof DCPlayer) {
            DCPlayer player = (DCPlayer) sender;

            CaseLocation playerLocation = player.getLocation();
            CaseLocation block = player.getTargetBlock(5);

            String caseType = args[0];
            String caseName = args[1];

            if(!api.getCaseManager().hasByType(caseType)) {
                sender.sendMessage(DCTools.prefix(DCTools.rt(api.getConfig().getMessages().getString("case-does-not-exist"),
                        "%case:" + caseType)));
                return true;
            }

            if(api.getConfig().getCaseStorage().has(block)) {
                sender.sendMessage(DCTools.prefix(api.getConfig().getMessages().getString("case-already-created")));
                return true;
            }

            if(api.getConfig().getCaseStorage().has(caseName)) {
                sender.sendMessage(DCTools.prefix(DCTools.rt(api.getConfig().getMessages().getString("case-already-exist"),
                        "%casename:" + caseName)));
                return true;
            }

            CaseLocation toSave = block.clone();

            toSave.setYaw(((int) playerLocation.getYaw()));
            toSave.setPitch(((int) playerLocation.getPitch()));

            CaseInfo caseInfo = new CaseInfo(caseType, toSave);

            try {
                api.getConfig().getCaseStorage().save(caseName, caseInfo);
                HologramManager manager = api.getPlatform().getHologramManager();
                if (manager != null) manager.create(block, api.getCaseManager().get(caseType));

                sender.sendMessage(DCTools.prefix(DCTools.rt(api.getConfig().getMessages().getString("case-added"),
                        "%casename:" + caseName, "%casetype:" + caseType)));
            } catch (ConfigurateException e) {
                api.getPlatform().getLogger().log(Level.WARNING, "Error with saving case: " + caseName, e);
            }
        }
        return true;
    }

    @Override
    public List<String> getTabCompletions(@NotNull DCCommandSender sender, @NotNull String label, String[] args) {
        List<String> list = new ArrayList<>(api.getCaseManager().getMap().keySet());
        if (args.length >= 2) {
            return new ArrayList<>();
        }
        return list;
    }

}
