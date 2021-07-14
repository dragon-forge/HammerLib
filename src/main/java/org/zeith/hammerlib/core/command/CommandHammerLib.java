package org.zeith.hammerlib.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.server.command.ModIdArgument;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.RenderItemsPacket;

public class CommandHammerLib
{
	public static void register(CommandDispatcher<CommandSource> $)
	{
		$.register(Commands.literal("hammerlib")
				.then(Commands.literal("client")
						.then(Commands.literal("render")
								.then(Commands.literal("items")
										.then(Commands.literal("all")
												.executes(cs ->
												{
													ServerPlayerEntity player = cs.getSource().getPlayerOrException();

													Network.sendTo(new RenderItemsPacket(2, 128, ""), player);

													return 1;
												})
												.then(Commands.argument("resolution", IntegerArgumentType.integer(1, 8192))
														.executes(cs ->
														{
															ServerPlayerEntity player = cs.getSource().getPlayerOrException();

															Network.sendTo(new RenderItemsPacket(2, IntegerArgumentType.getInteger(cs, "resolution"), ""), player);

															return 1;
														})
												)
										)
										.then(Commands.literal("from")
												.then(Commands.literal("mod")
														.then(Commands.argument("mod", ModIdArgument.modIdArgument())
																.executes(cs ->
																{
																	ServerPlayerEntity player = cs.getSource().getPlayerOrException();

																	Network.sendTo(new RenderItemsPacket(1, 128, cs.getArgument("mod", String.class)), player);

																	return 1;
																})
																.then(Commands.argument("resolution", IntegerArgumentType.integer(1, 8192))
																		.executes(cs ->
																		{
																			ServerPlayerEntity player = cs.getSource().getPlayerOrException();

																			Network.sendTo(new RenderItemsPacket(1, IntegerArgumentType.getInteger(cs, "resolution"), cs.getArgument("mod", String.class)), player);

																			return 1;
																		})
																)
														)
												)
										)
								)
						)
				)
		);
	}
}