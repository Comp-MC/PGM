package tc.oc.pgm.controlpoint;

import net.kyori.text.Component;
import net.kyori.text.ComponentBuilder;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.controlpoint.events.ControllerChangeEvent;
import tc.oc.pgm.util.named.NameStyle;

public class ControlPointAnnouncer implements Listener {
  private final Match match;

  private static final Component NEWLINE = TextComponent.newline();
  private static final String SPACES = "    ";
  private static final String ARROW_SYMBOL = "⇨";

  public ControlPointAnnouncer(Match match) {
    this.match = match;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onOwnerChange(ControllerChangeEvent event) {
    if (event.getControlPoint().isVisible()) {
      ComponentBuilder message = TextComponent.builder(SPACES + ARROW_SYMBOL + SPACES);
      if (event.getOldController() != null && event.getNewController() != null) {
        message
            .append(event.getOldController().getName(NameStyle.COLOR))
            .append(" lost ", TextColor.GRAY)
            .append(event.getControlPoint().getName())
            .append(" to ", TextColor.GRAY)
            .append(event.getNewController().getName(NameStyle.COLOR));
      } else if (event.getOldController() != null && event.getNewController() == null) {
        message
            .append(event.getOldController().getName(NameStyle.COLOR))
            .append(" lost ", TextColor.GRAY)
            .append(event.getControlPoint().getName());
      } else if (event.getOldController() == null && event.getNewController() != null) {
        message
            .append(event.getNewController().getName(NameStyle.COLOR))
            .append(" captured ", TextColor.GRAY)
            .append(event.getControlPoint().getName());
      } else if (event.getOldController() == null && event.getNewController() == null) {
        message.append(event.getControlPoint().getName()).append(" was lost", TextColor.GRAY);
      }
      // separate it from other chat messages
      this.match.sendMessage(NEWLINE);
      this.match.sendMessage(message.build());
      this.match.sendMessage(NEWLINE);
    }
  }
}
