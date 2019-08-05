package uk.colessoft.android.hilllist.domain

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

import java.util.ArrayList

class ScootXMLHandler : DefaultHandler() {
    private var buff: StringBuffer? = null
    var scootBusinesses = ScootBusinesses()
    private var tempBusiness: Business? = null
    private var buffering = false
    private var tempAddress = ArrayList<String>()

    @Throws(SAXException::class)
    override fun startDocument() {
        // Some sort of setting up work
    }

    @Throws(SAXException::class)
    override fun endDocument() {
        // Some sort of finishing up work
    }

    @Throws(SAXException::class)
    override fun startElement(namespaceURI: String, localName: String,
                              qName: String, atts: Attributes) {
        when (localName) {
            "companyname" -> {
                buff = StringBuffer("")
                buffering = true
            }
            "longitude" -> {
                buff = StringBuffer("")
                buffering = true
            }
            "latitude" -> {
                buff = StringBuffer("")
                buffering = true
            }
            "result" -> {
                buff = StringBuffer("")
                buffering = true
            }
            "telephone" -> {
                buff = StringBuffer("")
                buffering = true
            }
            "postcode" -> {
                buff = StringBuffer("")
                buffering = true
            }
            "linkback" -> {
                buff = StringBuffer("")
                buffering = true
            }
            "l1address" -> {

                buff = StringBuffer("")
                buffering = true
            }
            "l2address" -> {

                buff = StringBuffer("")
                buffering = true
            }
            "l3address" -> {

                buff = StringBuffer("")
                buffering = true
            }
            "l4address" -> {

                buff = StringBuffer("")
                buffering = true
            }
            "l5address" -> {

                buff = StringBuffer("")
                buffering = true
            }
            "l6address" -> {

                buff = StringBuffer("")
                buffering = true
            }
        }
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        if (buffering) {
            buff!!.append(ch, start, length)
        }
    }

    @Throws(SAXException::class)
    override fun endElement(namespaceURI: String, localName: String, qName: String) {
        when (localName) {
            "result" -> {
                buffering = false
                tempBusiness = Business()
                tempBusiness!!.resultNumber = Integer.parseInt(buff!!.toString())
                if (this.scootBusinesses.businesses == null)
                    this.scootBusinesses.businesses = ArrayList()
                this.scootBusinesses.businesses!!.add(tempBusiness!!)
            }
            "companyname" -> {
                buffering = false
                tempBusiness!!.companyname = buff!!.toString()
            }
            "longitude" -> {
                buffering = false
                tempBusiness!!.longitude = java.lang.Float.parseFloat(buff!!.toString())
            }
            "latitude" -> {

                buffering = false
                tempBusiness!!.latitude = java.lang.Float.parseFloat(buff!!.toString())
            }
            "telephone" -> {

                buffering = false
                tempBusiness!!.telephone = buff!!.toString()
            }
            "postcode" -> {

                buffering = false
                tempBusiness!!.postCode = buff!!.toString()
            }
            "linkback" -> {

                buffering = false
                tempBusiness!!.scootLink = buff!!.toString()
            }
            "l1address" -> {
                tempAddress = ArrayList()
                buffering = false
                tempAddress.add(buff!!.toString())
            }
            "l2address" -> {
                buffering = false
                tempAddress.add(buff!!.toString())
            }
            "l3address" -> {
                buffering = false
                tempAddress.add(buff!!.toString())
            }
            "l4address" -> {
                buffering = false
                tempAddress.add(buff!!.toString())
            }
            "l5address" -> {
                buffering = false
                tempAddress.add(buff!!.toString())
            }
            "l6address" -> {
                buffering = false
                tempAddress.add(buff!!.toString())
                tempBusiness!!.address = tempAddress
            }
        }// Do something with the full text content that we've just parsed
    }
}