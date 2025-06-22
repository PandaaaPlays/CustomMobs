package ca.pandaaa.custommobs;

import ca.pandaaa.custommobs.configurations.ConfigurationManager;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ManagerTest {

    @TempDir
    File tempDir;

    @Mock
    ConfigurationManager configManager;

    Manager manager;

    // Hold the static mock
    MockedStatic<Bukkit> mockedBukkit;

    @BeforeEach
    void setUp() {
        mockedBukkit = Mockito.mockStatic(Bukkit.class);

        Server mockServer = mock(Server.class);
        ConsoleCommandSender mockSender = mock(ConsoleCommandSender.class);
        ItemFactory mockItemFactory = mock(ItemFactory.class);
        ItemMeta mockMeta = mock(ItemMeta.class);
        PersistentDataContainer mockedDataContainer = mock(PersistentDataContainer.class);

        // ItemMeta behavior
        when(mockMeta.getPersistentDataContainer()).thenReturn(mockedDataContainer);
        doNothing().when(mockedDataContainer).set(any(NamespacedKey.class), any(PersistentDataType.class), any());
        when(mockMeta.getDisplayName()).thenReturn("DummyName");
        when(mockMeta.getLore()).thenReturn(new ArrayList<>());
        doNothing().when(mockMeta).setDisplayName(anyString());
        doNothing().when(mockMeta).setLore(anyList());

        // ItemFactory behavior
        when(mockItemFactory.getItemMeta(any(Material.class))).thenReturn(mockMeta);

        // Server behavior
        when(mockServer.getConsoleSender()).thenReturn(mockSender);
        when(mockServer.getItemFactory()).thenReturn(mockItemFactory);

        // Bukkit static methods
        mockedBukkit.when(Bukkit::getServer).thenReturn(mockServer);
        mockedBukkit.when(Bukkit::getItemFactory).thenReturn(mockItemFactory);

        // Plugin behavior
        CustomMobs mockPlugin = mock(CustomMobs.class);
        when(mockPlugin.getServer()).thenReturn(mockServer);
        when(mockPlugin.getDataFolder()).thenReturn(new File("dummyPath"));
        when(mockPlugin.getName()).thenReturn("MyPlugin");
        CustomMobs.setPlugin(mockPlugin);
    }

    @AfterEach
    void tearDown() {
        if (mockedBukkit != null) {
            mockedBukkit.close(); // This deregisters the static mock
        }
    }

    @Test
    void testAddCustomMobWithStubConfig() {
            // Setup mock server and item factory
            ConsoleCommandSender mockSender = mock(ConsoleCommandSender.class);
            doAnswer(invocation -> {
                String msg = invocation.getArgument(0);
                System.out.println(msg);
                return null;
            }).when(mockSender).sendMessage(anyString());

            ItemFactory mockItemFactory = mock(ItemFactory.class);
            Server mockServer = mock(Server.class);
            when(mockServer.getConsoleSender()).thenReturn(mockSender);
            when(mockServer.getItemFactory()).thenReturn(mockItemFactory);

            // Mock Bukkit static call
            mockedBukkit.when(Bukkit::getServer).thenReturn(mockServer);

            // Mock configuration
            CustomMobConfiguration mockConfiguration = mock(CustomMobConfiguration.class);
            when(mockConfiguration.getFileName()).thenReturn("TestMob.yml");
            when(mockConfiguration.getItem(anyString())).thenReturn(new ItemStack(Material.STONE)); // <- Needs real ItemStack
            when(mockConfiguration.getEquipment()).thenReturn(null);
            when(mockConfiguration.getPotionEffects()).thenReturn(List.of());
            when(mockConfiguration.getSpawner()).thenReturn(null);
            when(mockConfiguration.getDrops()).thenReturn(new ArrayList<>());
            when(mockConfiguration.getName()).thenReturn("TestMob");
            when(mockConfiguration.getSounds()).thenReturn(List.of());
            when(mockConfiguration.getMessages()).thenReturn(new ArrayList<>());
            when(mockConfiguration.getCustomEffectsCooldownDuration()).thenReturn(0);

            // Now create a real CustomMob
            CustomMob expectedMob = new CustomMob(
                    LocalDateTime.now(),
                    EntityType.ZOMBIE,
                    mockConfiguration
            );
            when(mockConfiguration.loadCustomMob()).thenReturn(expectedMob);

            // Inject mock config
            List<CustomMobConfiguration> configs = new ArrayList<>();
            configs.add(mockConfiguration);
            manager = new Manager(configManager, configs);

            CustomMob addedMob = manager.addCustomMob("TestMob", EntityType.ZOMBIE);

            assertNotNull(addedMob);
            assertEquals(EntityType.ZOMBIE, addedMob.getType());
            assertEquals("TestMob.yml", addedMob.getCustomMobFileName());
    }

}
