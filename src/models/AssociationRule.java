package models;

public class AssociationRule {
    private Itemset X;
    private Itemset Y;
    private double Support;
    private double Confidence;
    
    public AssociationRule()
    {
        X = new Itemset();
        Y = new Itemset();
        Support = 0.0;
        Confidence = 0.0;
    }
    
	public Itemset getX() {
		return X;
	}
	
	public void setX(Itemset x) {
		X = x;
	}
	
	public Itemset getY() {
		return Y;
	}
	
	public void setY(Itemset y) {
		Y = y;
	}
	
	public double getSupport() {
		return Support;
	}
	
	public void setSupport(double support) {
		Support = support;
	}
	
	public double getConfidence() {
		return Confidence;
	}
	
	public void setConfidence(double confidence) {
		Confidence = confidence;
	}
	
	public String ToString()
    {
        return (X + " => " + Y + " (support: " + Math.round(Support) + "%, confidence: " + Math.round(Confidence) + "%)");
    }
}
