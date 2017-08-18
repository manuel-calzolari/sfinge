package components;

import models.Itemset;
import models.ItemsetCollection;

public class Bit {
    public static ItemsetCollection FindSubsets(Itemset itemset, int n)
    {
        ItemsetCollection subsets = new ItemsetCollection();

        int subsetCount = (int)Math.pow(2, itemset.size());
        for (int i = 0; i < subsetCount; i++)
        {
            if (n == 0 || GetOnCount(i, itemset.size()) == n)
            {
                String binary = DecimalToBinary(i, itemset.size());

                Itemset subset = new Itemset();
                for (int charIndex = 0; charIndex < binary.length(); charIndex++)
                {
                    if (binary.charAt(charIndex) == '1')
                    {
                        subset.add(itemset.get(charIndex));
                    }
                }
                subsets.add(subset);
            }
        }

        return (subsets);
    }

    public static int GetBit(int value, int position)
    {
        int bit = value & (int)Math.pow(2, position);
        return (bit > 0 ? 1 : 0);
    }

    public static String DecimalToBinary(int value, int length)
    {
        String binary = "";
        for (int position = 0; position < length; position++)
        {
            binary = GetBit(value, position) + binary;
        }
        return (binary);
    }

    public static int GetOnCount(int value, int length)
    {
        String binary = DecimalToBinary(value, length); 
        int Count = 0;
        for (char c : binary.toCharArray())
        	if (c == '1')
        		Count++;
        return Count;
    }
}
