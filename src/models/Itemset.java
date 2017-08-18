package models;

import java.util.ArrayList;

public class Itemset extends ArrayList<String> {
	private static final long serialVersionUID = 1L;
	
	private double Support;

	public double getSupport() {
		return Support;
	}

	public void setSupport(double support) {
		Support = support;
	}
	
    public Boolean contains(Itemset itemset)
    {
    	Itemset temp = (Itemset)this.clone();
    	temp.retainAll(itemset);
        return (temp.size() == itemset.size());
    }
}
