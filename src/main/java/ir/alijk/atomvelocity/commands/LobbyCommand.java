package ir.alijk.atomvelocity.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import ir.alijk.atomvelocity.AtomVelocity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;

public class LobbyCommand implements SimpleCommand {
    private Component SENDING = Component.text("Darhale ersal shoma be lobby...", NamedTextColor.GREEN);
    private Component FAILED = Component.text("Moshkeli dar ersale shoma be lobby pish umad!", NamedTextColor.RED);

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 1) {
            if (invocation.source().hasPermission("lobby.force")) {
                Optional<RegisteredServer> bestServer = AtomVelocity.getInstance().bestServer(AtomVelocity.getInstance().getLobbies());
                if (bestServer.isPresent()) {
                    Optional<Player> p = AtomVelocity.getInstance().getProxyServer().getPlayer(args[0]);
                    if (p.isPresent()) {
                        p.get().createConnectionRequest(bestServer.get()).connect();
                        return;
                    }
                } else {
                    invocation.source().sendMessage(FAILED);
                }
            }
        }

        if (!(invocation.source() instanceof Player)) return;

        Player p = (Player) invocation.source();

        if (p.getCurrentServer().get().getServerInfo().getName().startsWith("verify")) return;

        p.sendMessage(SENDING);

        Optional<RegisteredServer> bestServer = AtomVelocity.getInstance().bestServer(AtomVelocity.getInstance().getLobbies());

        if (bestServer.isPresent()) {
            p.createConnectionRequest(bestServer.get()).connect();
        } else {
            p.sendMessage(FAILED);
        }
    }
}
