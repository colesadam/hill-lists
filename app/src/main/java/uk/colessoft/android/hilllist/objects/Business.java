package uk.colessoft.android.hilllist.objects;

import java.util.List;

public class Business {
	private float latitude;
	private float longitude;
	private String companyname;
	private int resultNumber;
	private String telephone;
	private List<String> address;
	private String postCode;
	private String scootLink;
	
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public void setResultNumber(int resultNumber) {
		this.resultNumber=resultNumber;
		
	}
	public int getResultNumber(){
		return this.resultNumber;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setAddress(List<String> address) {
		this.address = address;
	}
	public List<String> getAddress() {
		return address;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setScootLink(String scootLink) {
		this.scootLink = scootLink;
	}
	public String getScootLink() {
		return scootLink;
	}
	
	
}
