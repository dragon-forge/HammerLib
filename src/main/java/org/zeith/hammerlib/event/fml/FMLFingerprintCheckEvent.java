package org.zeith.hammerlib.event.fml;

import com.google.common.base.Suppliers;
import cpw.mods.jarhandling.SecureJar;
import cpw.mods.niofs.union.UnionFileSystem;
import lombok.Getter;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.ModLifecycleEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class FMLFingerprintCheckEvent
		extends ModLifecycleEvent
{
	private final Optional<String> gotFingerprint, trustData;
	protected final FMLModContainer ctr;
	
	protected final Supplier<Set<String>> filesViolated;
	protected Supplier<Boolean> anyFilesViolated;
	
	@Getter
	protected String expectedSignature;
	
	public FMLFingerprintCheckEvent(FMLModContainer container)
	{
		super(container);
		this.ctr = container;
		
		SecureJar jar = container.getModInfo().getOwningFile().getFile().getSecureJar();
		Path root = ((UnionFileSystem) jar.getRootPath().getFileSystem()).getRoot();
		
		SecureJar.ModuleDataProvider mdp = jar.moduleDataProvider();
		var mf = mdp.getManifest();
		Supplier<List<String>> entries = Suppliers.memoize(() -> mf
				.getEntries()
				.entrySet()
				.stream()
				.filter(e -> e.getValue().keySet().stream().anyMatch(str -> Objects.toString(str).contains("Digest")))
				.map(Map.Entry::getKey)
				.toList()
		);
		
		anyFilesViolated = Suppliers.memoize(() ->
		{
			if(!FMLEnvironment.production) return false;
			
			try(var walk = Files.walk(root))
			{
				Set<String> allEntries = new HashSet<>(entries.get());
				
				boolean violated = walk
						.filter(Files::isRegularFile)
						.peek(pth -> allEntries.remove(pth.toString()))
						.anyMatch(e -> jar.verifyPath(e) == SecureJar.Status.INVALID);
				
				return violated || !allEntries.isEmpty();
			} catch(IOException e)
			{
				throw new UncheckedIOException(e);
			}
		});
		
		filesViolated = Suppliers.memoize(() ->
		{
			if(!FMLEnvironment.production) return Collections.emptySet();
			
			try(var walk = Files.walk(root))
			{
				Set<String> allEntries = new HashSet<>(entries.get());
				
				Set<String> violated = new HashSet<>(walk
						.filter(Files::isRegularFile)
						.peek(pth -> allEntries.remove(pth.toString()))
						.filter(e -> jar.verifyPath(e) == SecureJar.Status.INVALID)
						.map(root::relativize)
						.map(Path::toString)
						.collect(Collectors.toSet()));
				violated.addAll(allEntries);
				
				return Set.copyOf(violated);
			} catch(IOException e)
			{
				throw new UncheckedIOException(e);
			}
		});
		
		ModFileInfo mfi = (ModFileInfo) container.getModInfo().getOwningFile();
		
		gotFingerprint = mfi.getCodeSigningFingerprint();
		
		Optional<String> trustData = Optional.empty();
		try
		{
			trustData = mfi.getTrustData();
		} catch(Exception ignored)
		{
		}
		this.trustData = trustData;
	}
	
	private void expectSigned(String signature)
	{
		this.expectedSignature = signature;
	}
	
	public boolean isJarSigned()
	{
		return trustData.isPresent() || gotFingerprint.isPresent();
	}
	
	public FMLModContainer getModContainer()
	{
		return ctr;
	}
	
	public Optional<String> fingerprint()
	{
		return gotFingerprint;
	}
	
	public Optional<String> trustData()
	{
		return trustData;
	}
	
	public Set<String> getInvalidSignedFiles()
	{
		return filesViolated.get();
	}
	
	public boolean anyInvalidFiles()
	{
		return anyFilesViolated.get();
	}
	
	public boolean isViolated(String expectFingerprint)
	{
		expectSigned(expectedSignature);
		expectFingerprint = expectFingerprint.replace(":", "").toLowerCase(Locale.ROOT);
		var gotFingerprint = fingerprint().map(f -> f.replace(":", "").toLowerCase(Locale.ROOT)).orElse(null);
		return !Objects.equals(gotFingerprint, expectFingerprint) || !getInvalidSignedFiles().isEmpty();
	}
}