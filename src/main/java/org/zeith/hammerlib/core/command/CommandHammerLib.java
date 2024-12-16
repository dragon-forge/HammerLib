package org.zeith.hammerlib.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.command.ModIdArgument;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.PacketReloadFoils;
import org.zeith.hammerlib.net.packets.RenderItemsPacket;

import java.util.function.*;

public class CommandHammerLib
{
	private static <T> LiteralArgumentBuilder<CommandSourceStack> renderer(
			String renderTypeName, int renderTypeId
	)
	{
		return renderer(renderTypeName, renderTypeId, null, null, null);
	}
	
	private static <T> LiteralArgumentBuilder<CommandSourceStack> renderer(
			String renderTypeName, int renderTypeId,
			ArgumentType<T> argType,
			BiFunction<CommandContext<CommandSourceStack>, String, T> getArg, Function<T, String> toData
	)
	{
		return renderer(renderTypeName, renderTypeId, argType, getArg, toData, null);
	}
	
	private static <T> LiteralArgumentBuilder<CommandSourceStack> renderer(
			String renderTypeName, int renderTypeId,
			ArgumentType<T> argType,
			BiFunction<CommandContext<CommandSourceStack>, String, T> getArg, Function<T, String> toData,
			SuggestionProvider<CommandSourceStack> suggestions
	)
	{
		var root = Commands.literal(renderTypeName);
		ArgumentBuilder<CommandSourceStack, ?> b = root;
		
		if(argType != null)
		{
			var rb = Commands.argument("arg", argType);
			if(suggestions != null) rb = rb.suggests(suggestions);
			b = rb;
		}
		
		b.executes(cs ->
				{
					ServerPlayer player = cs.getSource().getPlayerOrException();
					String data = "";
					if(argType != null) data = toData.apply(getArg.apply(cs, "arg"));
					Network.sendTo(new RenderItemsPacket(renderTypeId, 256, data), player);
					return 1;
				})
				.then(Commands.argument("resolution", IntegerArgumentType.integer(16, 16384))
						.executes(cs ->
						{
							ServerPlayer player = cs.getSource().getPlayerOrException();
							String data = "";
							if(argType != null) data = toData.apply(getArg.apply(cs, "arg"));
							Network.sendTo(new RenderItemsPacket(renderTypeId, IntegerArgumentType.getInteger(cs, "resolution"), data), player);
							return 1;
						})
				);
		
		if(b != root)
			root = root.then(b);
		
		return root;
	}
	
	public static void register(CommandDispatcher<CommandSourceStack> $)
	{
		$.register(Commands.literal("hammerlib")
				.then(Commands.literal("client")
						.then(Commands.literal("render")
								.then(Commands.literal("items")
										.then(renderer("held", 0))
										.then(renderer("all", 2))
										.then(Commands.literal("from")
												.then(renderer("mod", 1, ModIdArgument.modIdArgument(), (cs, name) -> cs.getArgument(name, String.class), UnaryOperator.identity()))
												.then(renderer("creative_mode_tab", 3,
																ResourceLocationArgument.id(), ResourceLocationArgument::getId, ResourceLocation::toString,
																(context, builder) -> SharedSuggestionProvider.suggestResource(
																		context.getSource().registryAccess().lookup(Registries.CREATIVE_MODE_TAB)
																				.stream()
																				.flatMap(reg -> reg.keySet().stream()),
																		builder
																)
														)
												)
										)
								)
						)
						.then(Commands.literal("reload")
								.then(Commands.literal("foil")
										.executes(cs ->
										{
											ServerPlayer player = cs.getSource()
													.getPlayerOrException();
											
											Network.sendTo(player, new PacketReloadFoils());
											
											return 1;
										})
								)
						)
				)
		);
	}
}