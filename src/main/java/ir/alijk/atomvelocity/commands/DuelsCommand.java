package ir.alijk.atomvelocity.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import ir.alijk.atomvelocity.AtomVelocity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;

public class DuelsCommand implements SimpleCommand {
    private Component SENDING = Component.text("Darhale ersal shoma be duels...", NamedTextColor.GREEN);
    private Component FAILED = Component.text("Moshkeli dar ersale shoma be duels pish umad!", NamedTextColor.RED);

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) return;

        Player p = (Player) invocation.source();

        if (p.getCurrentServer().get().getServerInfo().getName().startsWith("verify")) return;

        p.sendMessage(SENDING);

        Optional<RegisteredServer> bestServer = AtomVelocity.getInstance().bestServer(AtomVelocity.getInstance().getDuels());

        if (bestServer.isPresent()) {
            p.createConnectionRequest(bestServer.get()).connect();
        } else {
            p.sendMessage(FAILED);
        }
    }
}
