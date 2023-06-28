import org.zeith.api.blocks.redstone.*;

public class TestBundles
{
	public static void main(String[] args)
	{
		SimpleRedstoneBundle bundle = new SimpleRedstoneBundle();
		bundle.setSerializedBundleSignal(65535);
		
		bundle.setSignal(BundleColor.BLACK, false);
		
		System.out.println(bundle.getSerializedBundleSignal());
		
		for(var c : BundleColor.values())
			if(bundle.hasSignal(c))
				System.out.println("Found " + c);
	}
}