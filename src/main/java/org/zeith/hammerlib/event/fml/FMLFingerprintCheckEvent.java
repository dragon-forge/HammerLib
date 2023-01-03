package org.zeith.hammerlib.event.fml;

import com.google.common.base.Suppliers;
import cpw.mods.jarhandling.SecureJar;
import cpw.mods.niofs.union.UnionFileSystem;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FMLFingerprintCheckEvent
		extends ModLifecycleEvent
{
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private final Optional<String> gotFingerprint;
	protected final ModContainer ctr;
	
	protected final Supplier<Set<String>> filesViolated;
	
	public FMLFingerprintCheckEvent(ModContainer container)
	{
		super(container);
		this.ctr = container;
		
		SecureJar jar = container.getModInfo().getOwningFile().getFile().getSecureJar();
		Path root = ((UnionFileSystem) jar.getRootPath().getFileSystem()).getRoot();
		
		filesViolated = Suppliers.memoize(() ->
		{
			if(!FMLEnvironment.production) return Collections.emptySet();
			
			try(var walk = Files.walk(root))
			{
				return walk.filter(Files::isRegularFile)
						.filter(e -> jar.verifyPath(e) == SecureJar.Status.INVALID)
						.map(root::relativize)
						.map(Path::toString)
						.collect(Collectors.toSet());
			} catch(IOException e)
			{
				throw new UncheckedIOException(e);
			}
		});
		
		gotFingerprint = ((ModFileInfo) container.getModInfo().getOwningFile())
				.getCodeSigningFingerprint();
	}
	
	public ModContainer getModContainer()
	{
		return ctr;
	}
	
	public Optional<String> fingerprint()
	{
		return gotFingerprint;
	}
	
	public Set<String> getInvalidSignedFiles()
	{
		return filesViolated.get();
	}
	
	public boolean isViolated(String expectFingerprint)
	{
		expectFingerprint = expectFingerprint.replace(":", "").toLowerCase(Locale.ROOT);
		var gotFingerprint = fingerprint().map(f -> f.replace(":", "").toLowerCase(Locale.ROOT)).orElse(null);
		return !Objects.equals(gotFingerprint, expectFingerprint) || !getInvalidSignedFiles().isEmpty();
	}
}