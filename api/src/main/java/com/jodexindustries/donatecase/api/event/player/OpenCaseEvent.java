package com.jodexindustries.donatecase.api.event.player;

import com.jodexindustries.donatecase.api.data.casedata.CaseData;
import com.jodexindustries.donatecase.api.data.storage.CaseLocation;
import com.jodexindustries.donatecase.api.event.DCEvent;
import com.jodexindustries.donatecase.api.platform.DCPlayer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.event.Cancellable;

/**
 * Called when the player successfully opens the case (from gui) and player has keys for opening.
 * <br/>
 * At this time, case gui will be already closed.
 * <br/>
 * Can be cancelled. If you cancel this event, animation will not be started and keys will not be removed.
 * <p> Very similar with {@link com.jodexindustries.donatecase.api.event.animation.AnimationPreStartEvent}</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OpenCaseEvent extends DCEvent implements Cancellable {

    private final DCPlayer player;
    private final CaseData caseData;
    private final CaseLocation block;
    private boolean cancelled;

    @Override
    public boolean cancelled() {
        return cancelled;
    }

    @Override
    public void cancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
