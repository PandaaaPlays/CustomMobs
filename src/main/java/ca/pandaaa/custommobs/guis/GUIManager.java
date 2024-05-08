package ca.pandaaa.custommobs.guis;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GUIManager {
    private final MainCustomMobsGUI main;

    public GUIManager(List<CustomMobConfiguration> mobConfigurations) {
        this.main = new MainCustomMobsGUI(mobConfigurations.stream()
                .map(CustomMobConfiguration::getItem)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    public MainCustomMobsGUI getMainCustomMobsGUI() {
        return main;
    }
}
