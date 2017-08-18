package models;

/**
 * Model of the forms table.
 * 
 * @author Manuel Calzolari
 */
public class Form {
	private int id;
	private String title;
	private String desc;
	private String cdatetime; // DATETIME
	private String mdatetime; // DATETIME
	private int creator_id;
	private String passphrase;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCdatetime() {
		return cdatetime;
	}

	public void setCdatetime(String cdatetime) {
		this.cdatetime = cdatetime;
	}

	public String getMdatetime() {
		return mdatetime;
	}

	public void setMdatetime(String mdatetime) {
		this.mdatetime = mdatetime;
	}

	public int getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(int creator_id) {
		this.creator_id = creator_id;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
}
