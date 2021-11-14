package net.joefoxe.hexerei.client.renderer.entity.custom.ai;

import static net.minecraft.command.Commands.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.joefoxe.hexerei.client.renderer.entity.custom.PigeonEntity;
import net.joefoxe.hexerei.util.HexereiUtil;
import net.joefoxe.hexerei.util.UUIDArgument;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class PigeonReviveCommand {

    public static final DynamicCommandExceptionType COLOR_INVALID = new DynamicCommandExceptionType((arg) -> {
        return new TranslationTextComponent("command.pigeonrevive.invalid", arg);
    });
    public static final DynamicCommandExceptionType SPAWN_EXCEPTION = new DynamicCommandExceptionType((arg) -> {
        return new TranslationTextComponent("command.pigeonrevive.exception", arg);
    });
    public static final Dynamic2CommandExceptionType TOO_MANY_OPTIONS = new Dynamic2CommandExceptionType((arg1, arg2) -> {
        return new TranslationTextComponent("command.pigeonrevive.imprecise", arg1, arg2);
    });

    public static void register(final CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                literal("pigeon")
                        .requires(s -> s.hasPermissionLevel(2))
//                        .then(Commands.literal("locate")
//                                .then(Commands.literal("byuuid")
//                                        .then(Commands.argument("pigeon_owner", UUIDArgument.uuid()).suggests(PigeonReviveCommand.getOwnerIdSuggestionsLocate())
//                                                .then(Commands.argument("pigeon_uuid", UUIDArgument.uuid()).suggests(PigeonReviveCommand.getPigeonIdSuggestionsLocate())
//                                                        .executes(c -> locate(c)))))
//                                .then(Commands.literal("byname")
//                                        .then(Commands.argument("owner_name", StringArgumentType.string()).suggests(PigeonReviveCommand.getOwnerNameSuggestionsLocate())
//                                                .then(Commands.argument("pigeon_name", StringArgumentType.string()).suggests(PigeonReviveCommand.getPigeonNameSuggestionsLocate())
//                                                        .executes(c -> locate2(c))))))
                        .then(Commands.literal("revive")
                                .then(Commands.literal("byuuid")
                                        .then(Commands.argument("pigeon_owner", UUIDArgument.uuid()).suggests(PigeonReviveCommand.getOwnerIdSuggestionsRevive())
                                                .then(Commands.argument("pigeon_uuid", UUIDArgument.uuid()).suggests(PigeonReviveCommand.getPigeonIdSuggestionsRevive())
                                                        .executes(c -> respawn(c)))))
                                .then(Commands.literal("byname")
                                        .then(Commands.argument("owner_name", StringArgumentType.string()).suggests(PigeonReviveCommand.getOwnerNameSuggestionsRevive())
                                                .then(Commands.argument("pigeon_name", StringArgumentType.string()).suggests(PigeonReviveCommand.getPigeonNameSuggestionsRevive())
                                                        .executes(c -> respawn2(c))))))
        );
    }

    public static void registerSerializers() {
        ArgumentTypes.register(HexereiUtil.getResourcePath("uuid"), UUIDArgument.class, new ArgumentSerializer<>(UUIDArgument::uuid));
    }

//    private static <S extends ISuggestionProvider> SuggestionProvider<S> getOwnerIdSuggestionsLocate() {
//        return (context, builder) -> getOwnerIdSuggestions(PigeonLocationStorage.get(((CommandSource) context.getSource()).getWorld()).getAll(), context, builder);
//    }

    private static <S extends ISuggestionProvider> SuggestionProvider<S> getOwnerIdSuggestionsRevive() {
        return (context, builder) -> getOwnerIdSuggestions(PigeonReviveStorage.get(((CommandSource) context.getSource()).getWorld()).getAll(), context, builder);
    }


    private static <S extends ISuggestionProvider> CompletableFuture<Suggestions> getOwnerIdSuggestions(Collection<? extends IBirdData> possibilities, final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {

            return ISuggestionProvider.suggest(possibilities.stream()
                            .map(IBirdData::getOwnerId)
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .collect(Collectors.toSet()),
                    builder);

        } else if (context.getSource() instanceof ISuggestionProvider) {
            return context.getSource().getSuggestionsFromServer((CommandContext<ISuggestionProvider>) context, builder);
        } else {
            return Suggestions.empty();
        }
    }

//    private static <S extends ISuggestionProvider> SuggestionProvider<S> getPigeonIdSuggestionsLocate() {
//        return (context, builder) -> getPigeonIdSuggestions(PigeonLocationStorage.get(((CommandSource)context.getSource()).getWorld()).getAll(), context, builder);
//    }

    private static <S extends ISuggestionProvider> SuggestionProvider<S> getPigeonIdSuggestionsRevive() {
        return (context, builder) -> getPigeonIdSuggestions(PigeonReviveStorage.get(((CommandSource)context.getSource()).getWorld()).getAll(), context, builder);
    }

    private static <S extends ISuggestionProvider> CompletableFuture<Suggestions> getPigeonIdSuggestions(Collection<? extends IBirdData> possibilities, final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {
            UUID ownerId = context.getArgument("pigeon_owner", UUID.class);
            if (ownerId == null) {
                return Suggestions.empty();
            }
            return ISuggestionProvider.suggest(possibilities.stream()
                            .filter(data -> ownerId.equals(data.getOwnerId()))
                            .map(IBirdData::getBirdId)
                            .map(Object::toString)
                            .collect(Collectors.toSet()),
                    builder);
        } else if (context.getSource() instanceof ISuggestionProvider) {
            return context.getSource().getSuggestionsFromServer((CommandContext<ISuggestionProvider>) context, builder);
        } else {
            return Suggestions.empty();
        }
    }

//    private static <S extends ISuggestionProvider> SuggestionProvider<S> getOwnerNameSuggestionsLocate() {
//        return (context, builder) -> getOwnerNameSuggestions(PigeonLocationStorage.get(((CommandSource) context.getSource()).getWorld()).getAll(), context, builder);
//    }

    private static <S extends ISuggestionProvider> SuggestionProvider<S> getOwnerNameSuggestionsRevive() {
        return (context, builder) -> getOwnerNameSuggestions(PigeonReviveStorage.get(((CommandSource) context.getSource()).getWorld()).getAll(), context, builder);
    }

    public static <S extends ISuggestionProvider> CompletableFuture<Suggestions> getOwnerNameSuggestions(Collection<? extends IBirdData> possibilities, final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {
            return ISuggestionProvider.suggest(possibilities.stream()
                            .map(IBirdData::getOwnerName)
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .collect(Collectors.toSet()),
                    builder);
        } else if (context.getSource() instanceof ISuggestionProvider) {
            return context.getSource().getSuggestionsFromServer((CommandContext<ISuggestionProvider>) context, builder);
        } else {
            return Suggestions.empty();
        }
    }

//    private static <S extends ISuggestionProvider> SuggestionProvider<S> getPigeonNameSuggestionsLocate() {
//        return (context, builder) -> getPigeonNameSuggestions(PigeonLocationStorage.get(((CommandSource) context.getSource()).getWorld()).getAll(), context, builder);
//    }

    private static <S extends ISuggestionProvider> SuggestionProvider<S> getPigeonNameSuggestionsRevive() {
        return (context, builder) -> getPigeonNameSuggestions(PigeonReviveStorage.get(((CommandSource) context.getSource()).getWorld()).getAll(), context, builder);
    }

    public static <S extends ISuggestionProvider> CompletableFuture<Suggestions> getPigeonNameSuggestions(Collection<? extends IBirdData> possibilities, final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {
            String ownerName = context.getArgument("owner_name", String.class);
            if (ownerName == null) {
                return Suggestions.empty();
            }
            return ISuggestionProvider.suggest(possibilities.stream()
                            .filter(data -> ownerName.equals(data.getOwnerName()))
                            .map(IBirdData::getBirdName)
                            .filter((str) -> !Strings.isNullOrEmpty(str))
                            .collect(Collectors.toList()),
                    builder);
        } else if (context.getSource() instanceof ISuggestionProvider) {
            return context.getSource().getSuggestionsFromServer((CommandContext<ISuggestionProvider>) context, builder);
        } else {
            return Suggestions.empty();
        }
    }

    private static int respawn(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        CommandSource source = ctx.getSource();
        source.asPlayer(); // Check source is a player
        ServerWorld world = source.getWorld();

        UUID ownerUuid = ctx.getArgument("pigeon_owner", UUID.class);
        UUID uuid = ctx.getArgument("pigeon_uuid", UUID.class);

        PigeonReviveStorage respawnStorage = PigeonReviveStorage.get(world);
        PigeonReviveData respawnData = respawnStorage.getData(uuid);

        if (respawnData == null) {
            throw COLOR_INVALID.create(uuid.toString());
        }

        return respawn(respawnStorage, respawnData, source);
    }

    private static int respawn2(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        CommandSource source = ctx.getSource();
        source.asPlayer(); // Check source is a player
        ServerWorld world = source.getWorld();

        String ownerName = ctx.getArgument("owner_name", String.class);
        String pigeonName = ctx.getArgument("pigeon_name", String.class);

        PigeonReviveStorage respawnStorage = PigeonReviveStorage.get(world);
        List<PigeonReviveData> respawnData = respawnStorage.getPigeons(ownerName).filter((data) -> data.getBirdName().equalsIgnoreCase(pigeonName)).collect(Collectors.toList());

        if (respawnData.isEmpty()) {
            throw COLOR_INVALID.create(pigeonName);
        }
        if (respawnData.size() > 1) {
            StringJoiner joiner = new StringJoiner(", ");
            for (PigeonReviveData data : respawnData) {
                joiner.add(Objects.toString(data.getBirdId()));
            }
            throw TOO_MANY_OPTIONS.create(joiner.toString(), respawnData.size());
        }
        return respawn(respawnStorage, respawnData.get(0), source);
    }

    private static int respawn(PigeonReviveStorage respawnStorage, PigeonReviveData respawnData, final CommandSource source) throws CommandSyntaxException {
        PigeonEntity pigeon = respawnData.respawn(source.getWorld(), source.asPlayer(), source.asPlayer().getPosition().up());

        if (pigeon != null) {
            respawnStorage.remove(respawnData.getBirdId());
            source.sendFeedback(new TranslationTextComponent("commands.pigeonrevive.uuid.success", respawnData.getBirdName()), false);
            return 1;
        }
        source.sendFeedback(new TranslationTextComponent("commands.pigeonrevive.uuid.failure", respawnData.getBirdName()), false);
        return 0;
    }

//    private static int locate(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
//        CommandSource source = ctx.getSource();
//        source.asPlayer(); // Check source is a player
//        ServerWorld world = source.getWorld();
//
//        UUID ownerUuid = ctx.getArgument("pigeon_owner", UUID.class);
//
//        UUID uuid = ctx.getArgument("pigeon_uuid", UUID.class);
//
//        PigeonLocationStorage locationStorage = PigeonLocationStorage.get(world);
//        PigeonLocationData locationData = locationStorage.getData(uuid);
//
//        if (locationData == null) {
//            throw COLOR_INVALID.create(uuid.toString());
//        }
//
//        return locate(locationStorage, locationData, source);
//    }
//
//    private static int locate2(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
//        CommandSource source = ctx.getSource();
//        source.asPlayer(); // Check source is a player
//        ServerWorld world = source.getWorld();
//
//        String ownerName = ctx.getArgument("owner_name", String.class);
//
//        String pigeonName = ctx.getArgument("pigeon_name", String.class);
//        PigeonLocationStorage locationStorage = PigeonLocationStorage.get(world);
//        List<PigeonLocationData> locationData = locationStorage.getAll().stream()
//                .filter(data -> ownerName.equals(data.getOwnerName())).filter((data) -> data.getPigeonName().equalsIgnoreCase(pigeonName)).collect(Collectors.toList());
//
//        if (locationData.isEmpty()) {
//            throw COLOR_INVALID.create(pigeonName);
//        }
//        if (locationData.size() > 1) {
//            StringJoiner joiner = new StringJoiner(", ");
//            for (PigeonLocationData data : locationData) {
//                joiner.add(Objects.toString(data.getBirdId()));
//            }
//            throw TOO_MANY_OPTIONS.create(joiner.toString(), locationData.size());
//        }
//        return locate(locationStorage, locationData.get(0), source);
//    }
//
//    private static int locate(PigeonLocationStorage respawnStorage, PigeonLocationData locationData, final CommandSource source) throws CommandSyntaxException {
//        PlayerEntity player = source.asPlayer();
//        if (locationData.getDimension().equals(player.world.getDimensionKey())) {
//            source.sendFeedback(new TranslationTextComponent("", locationData.getName(player.world)), false);
//
//        }
//        return 1;
//    }
}