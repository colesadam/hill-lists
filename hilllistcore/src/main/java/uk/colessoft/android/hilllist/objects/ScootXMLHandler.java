package uk.colessoft.android.hilllist.objects;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ScootXMLHandler extends DefaultHandler {
	StringBuffer buff = null;
	ScootBusinesses scootBusinesses = new ScootBusinesses();
	Business tempBusiness;
	boolean buffering = false;
	ArrayList tempAddress = new ArrayList();

	@Override
	public void startDocument() throws SAXException {
		// Some sort of setting up work
	}

	@Override
	public void endDocument() throws SAXException {
		// Some sort of finishing up work
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (localName.equals("companyname")) {
			buff = new StringBuffer("");
			buffering = true;
		} else if (localName.equals("longitude")) {
			buff = new StringBuffer("");
			buffering = true;
		} else if (localName.equals("latitude")) {
			buff = new StringBuffer("");
			buffering = true;
		} else if (localName.equals("result")) {
			buff = new StringBuffer("");
			buffering = true;
		} else if (localName.equals("telephone")) {
			buff = new StringBuffer("");
			buffering = true;
		} else if (localName.equals("postcode")) {
			buff = new StringBuffer("");
			buffering = true;
		} else if (localName.equals("linkback")) {
			buff = new StringBuffer("");
			buffering = true;
		} else if (localName.equals("l1address")) {

			buff = new StringBuffer("");
			buffering = true;
		} else if (localName.equals("l2address")) {

			buff = new StringBuffer("");
			buffering = true;
		} else if (localName.equals("l3address")) {

			buff = new StringBuffer("");
			buffering = true;
		} else if (localName.equals("l4address")) {

			buff = new StringBuffer("");
			buffering = true;
		} else if (localName.equals("l5address")) {

			buff = new StringBuffer("");
			buffering = true;
		} else if (localName.equals("l6address")) {

			buff = new StringBuffer("");
			buffering = true;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		if (buffering) {
			buff.append(ch, start, length);
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("result")) {
			buffering = false;
			tempBusiness = new Business();
			tempBusiness.setResultNumber(Integer.parseInt(buff.toString()));
			if (this.getScootBusinesses().getBusinesses() == null)
				this.getScootBusinesses().setBusinesses(
						new ArrayList<Business>());
			this.scootBusinesses.getBusinesses().add(tempBusiness);
		} else if (localName.equals("companyname")) {
			buffering = false;
			tempBusiness.setCompanyname(buff.toString());

			// Do something with the full text content that we've just parsed
		} else if (localName.equals("longitude")) {
			buffering = false;
			tempBusiness.setLongitude(Float.parseFloat(buff.toString()));
		} else if (localName.equals("latitude")) {

			buffering = false;
			tempBusiness.setLatitude(Float.parseFloat(buff.toString()));
		} else if (localName.equals("telephone")) {

			buffering = false;
			tempBusiness.setTelephone(buff.toString());
		} else if (localName.equals("postcode")) {

			buffering = false;
			tempBusiness.setPostCode(buff.toString());
		} else if (localName.equals("linkback")) {

			buffering = false;
			tempBusiness.setScootLink(buff.toString());
		} else if (localName.equals("l1address")) {
			tempAddress = new ArrayList();
			buffering = false;
			tempAddress.add(buff.toString());

		} else if (localName.equals("l2address")) {
			buffering = false;
			tempAddress.add(buff.toString());
		} else if (localName.equals("l3address")) {
			buffering = false;
			tempAddress.add(buff.toString());
		} else if (localName.equals("l4address")) {
			buffering = false;
			tempAddress.add(buff.toString());
		} else if (localName.equals("l5address")) {
			buffering = false;
			tempAddress.add(buff.toString());
		} else if (localName.equals("l6address")) {
			buffering = false;
			tempAddress.add(buff.toString());
			tempBusiness.setAddress(tempAddress);
		}
	}

	public ScootBusinesses getScootBusinesses() {
		return scootBusinesses;
	}

	public void setScootBusinesses(ScootBusinesses scootBusinesses) {
		this.scootBusinesses = scootBusinesses;
	}
}