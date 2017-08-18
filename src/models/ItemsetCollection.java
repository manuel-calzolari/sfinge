package models;

import java.util.ArrayList;

public class ItemsetCollection extends ArrayList<Itemset> {
	private static final long serialVersionUID = 1L;
	
    public Itemset GetUniqueItems()
    {
        Itemset unique = new Itemset();

        for (Itemset itemset : this)
            for (String item : itemset)
            	if (!unique.contains(item))
            		unique.add(item);

        return unique;
    }
	
    public double FindSupport(String item)
    {
        int matchCount = 0;
        for (Itemset itemset : this)
        	if (itemset.contains(item))
        		matchCount++;

        double support = ((double)matchCount / (double)this.size()) * 100.0;
        return support;
    }

    public double FindSupport(Itemset itemset)
    {
        int matchCount = 0;
        for (Itemset i : this)
        	if (i.contains(itemset))
        		matchCount++;

        double support = ((double)matchCount / (double)this.size()) * 100.0;
        return support;
    }
}
