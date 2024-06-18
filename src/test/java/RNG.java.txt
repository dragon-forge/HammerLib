import java.util.Random;

public class RNG
{
	public static void main(String[] args)
	{
		char[] chars = "0123456789".toCharArray();
		char[][] output = {
				new char[4],
				new char[8],
				new char[4],
				new char[8]
		};
		
		Random rand = new Random();
		
		for(char[] ar : output)
		{
			for(int i = 0; i < ar.length; ++i)
			{
				ar[i] = chars[rand.nextInt(chars.length)];
			}
			
			System.out.println(new String(ar));
		}
	}
}