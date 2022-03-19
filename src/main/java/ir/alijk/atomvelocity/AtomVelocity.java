package ir.alijk.atomvelocity;

import com.google.inject.Inject;
import com.sun.jdi.request.InvalidRequestStateException;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import ir.alijk.atomvelocity.commands.BedwarsCommand;
import ir.alijk.atomvelocity.commands.LobbyCommand;
import ir.alijk.atomvelocity.commands.DuelsCommand;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Plugin(
        id = "atomvelocity",
        name = "AtomVelocity",
        version = "1.0.0",
        description = "AtomVelocity Customized Handlers",
        authors = {"Alijk"}
)
public class AtomVelocity {

    private static Optional<AtomVelocity> instance;
    private final ProxyServer proxyServer;
    private final Logger logger;

    private final List<RegisteredServer> lobbies = new ArrayList<>();
    private final List<RegisteredServer> bedwars = new ArrayList<>();
    private final List<RegisteredServer> duels = new ArrayList<>();

    @Inject
    public AtomVelocity(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataDirectory) {
        instance = Optional.of(this);
        this.proxyServer = proxyServer;
        this.logger = logger;

        proxyServer.getCommandManager().register("lobby", new LobbyCommand());
        proxyServer.getCommandManager().register("bedwars", new BedwarsCommand());
        proxyServer.getCommandManager().register("duels", new DuelsCommand());
    }

    @Subscribe
    public void onProxyInit(ProxyInitializeEvent event) {
        int i = 1;

        while (true) {
            Optional<RegisteredServer> server = proxyServer.getServer("lobby-" + i);
            if (server.isPresent()) {
                lobbies.add(server.get());
                i++;
            } else {
                break;
            }
        }

        i = 1;
        while (true) {
            Optional<RegisteredServer> server = proxyServer.getServer("bedwars-lobby-" + i);
            if (server.isPresent()) {
                bedwars.add(server.get());
                i++;
            } else {
                break;
            }
        }

        i = 1;
        while (true) {
            Optional<RegisteredServer> server = proxyServer.getServer("duels-" + i);
            if (server.isPresent()) {
                duels.add(server.get());
                i++;
            } else {
                break;
            }
        }
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    public Optional<RegisteredServer> bestServer(List<RegisteredServer> listToChoose) {
        return listToChoose.stream().filter(s -> s.getPlayersConnected().size() < 100).max(Comparator.comparingInt(s -> s.getPlayersConnected().size()));
    }

    public static AtomVelocity getInstance() {
        return instance.orElseThrow(InvalidRequestStateException::new);
    }

    public List<RegisteredServer> getLobbies() {
        return lobbies;
    }

    public List<RegisteredServer> getBedwars() {
        return bedwars;
    }

    public List<RegisteredServer> getDuels() {
        return duels;
    }
}
