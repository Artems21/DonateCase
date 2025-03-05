package com.jodexindustries.donatecase.spigot.gui.InventarImpl;

import com.jodexindustries.donatecase.api.DCAPI;
import com.jodexindustries.donatecase.api.data.casedata.CaseData;
import com.jodexindustries.donatecase.api.manager.CaseManager;
import com.jodexindustries.donatecase.spigot.BukkitBackend;
import com.jodexindustries.donatecase.spigot.gui.BaseGui;
import com.jodexindustries.donatecase.spigot.gui.GuiExecute;
import org.bukkit.entity.Player;

public class CaseGui implements GuiExecute {

    private final BukkitBackend backend;
    private final Player player;
    private final String caseName;


    public CaseGui(BukkitBackend backend, Player player, String caseName) {
        this.backend = backend;
        this.player = player;
        this.caseName = caseName;
    }

    @Override
    public boolean execute() {
        DCAPI api =  DCAPI.getInstance();
        CaseManager caseManager = api.getCaseManager();
        CaseData caseData = caseManager.get(caseName);
        System.out.println(caseData.caseDisplayName());

        return false;
    }
}
