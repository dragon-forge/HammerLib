package org.zeith.hammerlib.net.lft;

import com.google.common.base.Predicates;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.thread.SidedThreadGroup;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.net.HLTargetPoint;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class TransportSession
{
	final Class<? extends ITransportAcceptor> acceptor;
	final List<byte[]> pending;

	final ITransportAcceptor acceptori;

	final PipedOutputStream pos;
	final PipedInputStream pis;

	public final String id;

	final int length;
	final LogicalSide createSide;

	private Thread readThread;

	public TransportSession(String id, Class<? extends ITransportAcceptor> acceptor, List<byte[]> data, ITransportAcceptor ai, int length)
	{
		this.id = id;
		this.acceptor = acceptor;
		this.pending = data;
		this.acceptori = ai;
		this.length = length;

		createSide = LogicalSidePredictor.getCurrentLogicalSide();

		PipedInputStream pis = null;
		PipedOutputStream pos = null;

		if(ai != null)
		{
			pis = new PipedInputStream(length);
			try
			{
				pos = new PipedOutputStream(pis);
			} catch(IOException e)
			{
			}
		}

		this.pis = pis;
		this.pos = pos;

		if(ai != null)
		{
			SidedThreadGroup stg = createSide == LogicalSide.SERVER ? SidedThreadGroups.SERVER : SidedThreadGroups.CLIENT;
			readThread = stg.newThread(() -> ai.read(this.pis, length));
			readThread.start();

			NetTransport.indexSession(this);
		}
	}

	/**
	 * Generates a packet that will start the transport session when sent.
	 */
	PacketTransportInfo genPacket()
	{
		NetTransport.indexSession(this);
		return new PacketTransportInfo(id, acceptor.getName(), length);
	}

	public PacketTransportInfo createPacket()
	{
		return genCopy(this).genPacket();
	}

	public void sendTo(ServerPlayerEntity player)
	{
		Network.sendTo(createPacket(), player);
	}

	public void sendToServer()
	{
		Network.sendToServer(createPacket());
	}

	public void sendToAll()
	{
		sendToPlayersIf(Predicates.alwaysTrue());
	}

	public void sendToDimension(RegistryKey<World> dim)
	{
		sendToPlayersIf(mp -> mp.level.dimension().compareTo(dim) == 0);
	}

	public void sendToNearby(HLTargetPoint tp)
	{
		sendToPlayersIf(mp -> mp.level.dimension().compareTo(tp.dim) == 0 && Math.sqrt(mp.position().distanceTo(tp)) <= tp.range);
	}

	public void sendToPlayersIf(Predicate<ServerPlayerEntity> predicate)
	{
		MinecraftServer mcs = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
		if(mcs != null)
			for(ServerPlayerEntity mp : mcs.getPlayerList().getPlayers())
				if(predicate.test(mp))
					sendTo(mp);
	}

	public void sendToMultiplePlayers(ServerPlayerEntity... players)
	{
		for(int i = 0; i < players.length; ++i)
			sendTo(players[i]);
	}

	void accept(byte[] data)
	{
		if(pos != null)
			try
			{
				pos.write(data);
				pos.flush();
			} catch(IOException e)
			{
			}
	}

	void end(PacketContext ctx)
	{
		ITransportAcceptor acp = this.acceptori;

		if(readThread != null)
		{
			try
			{
				readThread.join(1000L); // let's not lock the current thread for longer than 1 second, please?
			} catch(InterruptedException e)
			{
				e.printStackTrace();

				// if we hang for too long, let's interrupt the thread.
				// The reading should be done as the data gets fed and processed right after, not build up and then bulk-read/decode.
				readThread.interrupt();

				HammerLib.LOG.error("------- Transport acceptor " + acceptor + " failed to read passed data in time it was given, aborting the transmit completion.");
				acp = null;
			}
			readThread = null;
		}

		if(acp != null) acp.onTransmissionComplete(ctx);

		Map<String, TransportSession> m = NetTransport.SESSIONS.get(createSide);
		if(m != null) m.remove(id);
	}

	public static TransportSession genCopy(TransportSession session)
	{
		List<byte[]> matrix = new ArrayList<>(session.pending);
		for(int i = 0; i < matrix.size(); ++i)
			matrix.set(i, matrix.get(i).clone());
		return new TransportSession(UUID.randomUUID().toString(), session.acceptor, matrix, null, session.length);
	}
}