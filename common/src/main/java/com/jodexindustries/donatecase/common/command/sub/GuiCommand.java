package com.jodexindustries.donatecase.common.command.sub;

import com.jodexindustries.donatecase.api.DCAPI;
import com.jodexindustries.donatecase.api.data.subcommand.SubCommandException;
import com.jodexindustries.donatecase.api.data.subcommand.SubCommandType;
import com.jodexindustries.donatecase.api.platform.DCCommandSender;
import com.jodexindustries.donatecase.common.command.DefaultCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GuiCommand extends DefaultCommand {
    public GuiCommand(DCAPI api, String name, SubCommandType type) {
        super(api, name, type);
    }

    @Override
    public boolean execute(@NotNull DCCommandSender sender, @NotNull String label, @NotNull String[] args) throws SubCommandException {
        return false;
    }

    @Override
    public List<String> getTabCompletions(@NotNull DCCommandSender sender, @NotNull String label, @NotNull String[] args) throws SubCommandException {
        return List.of();
    }
}
