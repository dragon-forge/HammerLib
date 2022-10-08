package org.zeith.hammerlib.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.ModIdArgument;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.RenderItemsPacket;

public class CommandHammerLib
{
	public static void register(CommandDispatcher<CommandSourceStack> $)
	{
		$.register(Commands.literal("hammerlib")
				.then(Commands.literal("client")
						.then(Commands.literal("render")
								.then(Commands.literal("items")
										.then(Commands.literal("held")
												.executes(cs ->
												{
													ServerPlayer player = cs.getSource().getPlayerOrException();
													
													Network.sendTo(new RenderItemsPacket(0, 256, ""), player);
													
													return 1;
												})
												.then(Commands.argument("resolution", IntegerArgumentType.integer(16, 16384))
														.executes(cs ->
														{
															ServerPlayer player = cs.getSource().getPlayerOrException();
															
															Network.sendTo(new RenderItemsPacket(0, IntegerArgumentType.getInteger(cs, "resolution"), ""), player);
															
															return 1;
														})
												)
										)
										.then(Commands.literal("all")
												.executes(cs ->
												{
													ServerPlayer player = cs.getSource().getPlayerOrException();

													Network.sendTo(new RenderItemsPacket(2, 256, ""), player);

													return 1;
												})
												.then(Commands.argument("resolution", IntegerArgumentType.integer(16, 16384))
														.executes(cs ->
														{
															ServerPlayer player = cs.getSource().getPlayerOrException();

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
																	ServerPlayer player = cs.getSource().getPlayerOrException();

																	Network.sendTo(new RenderItemsPacket(1, 256, cs.getArgument("mod", String.class)), player);

																	return 1;
																})
																.then(Commands.argument("resolution", IntegerArgumentType.integer(16, 16384))
																		.executes(cs ->
																		{
																			ServerPlayer player = cs.getSource().getPlayerOrException();

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