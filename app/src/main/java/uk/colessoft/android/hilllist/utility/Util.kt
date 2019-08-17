package uk.colessoft.android.hilllist.utility


import android.content.Context
import android.content.res.AssetManager
import android.util.TypedValue

import java.io.IOException
import java.io.InputStream
import java.util.HashMap
import java.util.Properties


object Util {
    @Throws(IOException::class)
    fun getClassification(key: String, context: Context): String {
        val properties = Properties()
        val assetManager = context.assets
        val inputStream = assetManager.open("classifications.properties")
        properties.load(inputStream)
        return properties.getProperty(key)

    }

    fun getThemeAccentColor(context: Context, colour: Int): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(colour, value, true)
        return value.data
    }

    @JvmField
    val classesMap: HashMap<String, String> = object : HashMap<String, String>() {
        init {
            put("Ma","Marilyn")
            put("Ma=","Marilyn twin-top")
            put("Hu","Hump")
            put("Hu=","Hump twin-top")
            put("Tu","Tump")
            put("Tu=","Twin Tump")
            put("Sim","Simm")
            put("M","Munro")
            put("MT","Munro Top")
            put("F","Furth")
            put("C","Corbett")
            put("G","Graham")
            put("D","Donald")
            put("DT","Donald Top")
            put("Mur","Murdo")
            put("CT","Corbett Top")
            put("GT","Graham Top")
            put("Hew","Hewitt")
            put("N","Nuttall")
            put("5","Dewey")
            put("5D","Donald Dewey")
            put("5H","Highland Five")
            put("4","400-499m Tump")
            put("3","300-399m Tump")
            put("2","200-299m Tump")
            put("1","100-199m Tump")
            put("1=","100-199m Tump twin-top")
            put("0","0-99m Tump")
            put("W","Wainwright")
            put("WO","Wainwright Outlying Fell")
            put("B","Birkett")
            put("CoH","Historic County Top")
            put("CoH=","Historic County twin-top")
            put("CoU","Current County/UA Top")
            put("CoU=","Current County/UA twin-top")
            put("CoA","Administrative County Top")
            put("CoA=","Administrative County twin-top")
            put("CoL","London Borough Top")
            put("CoL=","London Borough twin-top")
            put("SIB","Significant Island of Britain")
            put("sMa","Submarilyn")
            put("sHu","Subhump")
            put("sSim","Subsimm")
            put("s5","Subdodd")
            put("s5D","Sub Donald Dewey")
            put("s5H", "Sub Highland 5")
            put("s5M","Submyrddyn Dewey")
            put("s4","Sub 490-499m hill")
            put("Sy","Synge")
            put("BL","Buxton & Lewis")
            put("Fel","Fellranger")
            put("Bg","Bridge")
            put("T100","Trail 100")
            put("xMT","Deleted Munro Top")
            put("xC","Deleted Corbett")
            put("xG","Deleted Graham")
            put("xN","Deleted Nuttall")
            put("xDT","Deleted Donald Top")
            put("Dil","Dillon")
            put("VL","Vandeleur-Lynam")
            put("A","Arderin")
            put("5M","Myrddyn Dewey")
            put("Ca","Carn")
            put("Bin","Binnion")
            put("O","Other list, see Comments field")
            put("Un","Unclassified")
        }
    }


}
