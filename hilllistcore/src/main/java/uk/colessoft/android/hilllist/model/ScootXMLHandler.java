package uk.colessoft.android.hilllist.model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ScootXMLHandler extends DefaultHandler {
	private StringBuffer buff = null;
	private ScootBusinesses scootBusinesses = new ScootBusinesses();
	private Business tempBusiness;
	private boolean buffering = false;
	private ArrayList tempAddress = new ArrayList();

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
		switch (localName) {
			case "companyname":
				buff = new StringBuffer("");
				buffering = true;
				break;
			case "longitude":
				buff = new StringBuffer("");
				buffering = true;
				break;
			case "latitude":
				buff = new StringBuffer("");
				buffering = true;
				break;
			case "result":
				buff = new StringBuffer("");
				buffering = true;
				break;
			case "telephone":
				buff = new StringBuffer("");
				buffering = true;
				break;
			case "postcode":
				buff = new StringBuffer("");
				buffering = true;
				break;
			case "linkback":
				buff = new StringBuffer("");
				buffering = true;
				break;
			case "l1address":

				buff = new StringBuffer("");
				buffering = true;
				break;
			case "l2address":

				buff = new StringBuffer("");
				buffering = true;
				break;
			case "l3address":

				buff = new StringBuffer("");
				buffering = true;
				break;
			case "l4address":

				buff = new StringBuffer("");
				buffering = true;
				break;
			case "l5address":

				buff = new StringBuffer("");
				buffering = true;
				break;
			case "l6address":

				buff = new StringBuffer("");
				buffering = true;
				break;
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
		switch (localName) {
			case "result":
				buffering = false;
				tempBusiness = new Business();
				tempBusiness.setResultNumber(Integer.parseInt(buff.toString()));
				if (this.getScootBusinesses().getBusinesses() == null)
					this.getScootBusinesses().setBusinesses(
							new ArrayList<>());
				this.scootBusinesses.getBusinesses().add(tempBusiness);
				break;
			case "companyname":
				buffering = false;
				tempBusiness.setCompanyname(buff.toString());

				// Do something with the full text content that we've just parsed
				break;
			case "longitude":
				buffering = false;
				tempBusiness.setLongitude(Float.parseFloat(buff.toString()));
				break;
			case "latitude":

				buffering = false;
				tempBusiness.setLatitude(Float.parseFloat(buff.toString()));
				break;
			case "telephone":

				buffering = false;
				tempBusiness.setTelephone(buff.toString());
				break;
			case "postcode":

				buffering = false;
				tempBusiness.setPostCode(buff.toString());
				break;
			case "linkback":

				buffering = false;
				tempBusiness.setScootLink(buff.toString());
				break;
			case "l1address":
				tempAddress = new ArrayList();
				buffering = false;
				tempAddress.add(buff.toString());

				break;
			case "l2address":
				buffering = false;
				tempAddress.add(buff.toString());
				break;
			case "l3address":
				buffering = false;
				tempAddress.add(buff.toString());
				break;
			case "l4address":
				buffering = false;
				tempAddress.add(buff.toString());
				break;
			case "l5address":
				buffering = false;
				tempAddress.add(buff.toString());
				break;
			case "l6address":
				buffering = false;
				tempAddress.add(buff.toString());
				tempBusiness.setAddress(tempAddress);
				break;
		}
	}

	public ScootBusinesses getScootBusinesses() {
		return scootBusinesses;
	}

	public void setScootBusinesses(ScootBusinesses scootBusinesses) {
		this.scootBusinesses = scootBusinesses;
	}
}