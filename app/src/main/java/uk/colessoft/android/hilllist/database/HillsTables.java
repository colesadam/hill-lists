package uk.colessoft.android.hilllist.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.util.SparseArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class HillsTables {

    public static final String KEY_TITLE = "title";

    private static final String HILLS_CSV = "DoBIH_v15.3.csv";

    public static final String HILLS_TABLE = "hills";
    public static final String HILLTYPES_TABLE = "hilltypes";
    public static final String TYPES_LINK_TABLE = "typeslink";
    public static final String KEY_IMPORTANCE = "importance";
    public static final String KEY_DESC = "description";
    public static final String KEY_ID = "_id";
    public static final String KEY_HILLNAME = "Name";
    public static final String KEY_SECTION = "Section";
    public static final String KEY_SECTIONNAME = "Section name";
    public static final String KEY_AREA = "Area";
    public static final String KEY_CLASSIFICATION = "Classification";
    public static final String KEY_MAP = "Map 1:50k";
    public static final String KEY_MAP25 = "Map 1:25k";
    public static final String KEY_HEIGHTM = "Metres";
    public static final String KEY_HEIGHTF = "Feet";
    public static final String KEY_GRIDREF = "Grid ref";
    public static final String KEY_GRIDREF10 = "Grid ref 10";
    public static final String KEY_DROP = "Drop";
    public static final String KEY_COLGRIDREF = "Col grid ref";
    public static final String KEY_COLHEIGHT = "Col height";
    public static final String KEY_FEATURE = "Feature";
    public static final String KEY_OBSERVATIONS = "Observations";
    public static final String KEY_SURVEY = "Survey";
    public static final String KEY_CLIMBED = "Climbed";
    public static final String KEY_COUNTRY = "Country";
    public static final String KEY_COUNTY = "County";
    public static final String KEY_REVISION = "Revision";
    public static final String KEY_COMMENTS = "Comments";
    public static final String KEY_STREETMAP = "Streetmap/OSiViewer";
    public static final String KEY_GEOGRAPH = "Geograph/MountainViews";
    public static final String KEY_HILLBAGGING = "Hill-bagging";// "\"Hill-bagging\"";
    public static final String KEY_XCOORD = "Xcoord";
    public static final String KEY_YCOORD = "Ycoord";
    public static final String KEY_LATITUDE = "Latitude";
    public static final String KEY_LONGITUDE = "Longitude";
    public static final String KEY_XSECTION = "_Section";
    public static final String KEY_PARENTSMC = "ParentSMC";
    public static final String KEY_PARENTNAMESMC = "ParentNameSMC";
    public static final String KEY_ISLAND = "Island";
    public static final String KEY_TOPOSECTION = "TopoSection";
    public static final String KEY_GRIDREFXY = "GridRefXY";
    public static final String KEY_marilyn = "ma";
    public static final String KEY_marilynTwinTop = "twinma";
    public static final String KEY_hump = "hu";
    public static final String KEY_humpTwinTop = "twinhu";
    public static final String KEY_munro = "m";
    public static final String KEY_munroTop = "mt";
    public static final String KEY_murdo = "mur";
    public static final String KEY_furth = "f";
    public static final String KEY_corbett = "c";
    public static final String KEY_corbettTop = "ct";
    public static final String KEY_graham = "g";
    public static final String KEY_grahamTop = "gt";
    public static final String KEY_donald = "d";
    public static final String KEY_donaldTop = "dt";
    public static final String KEY_hewitt = "hew";
    public static final String KEY_nuttall = "n";
    public static final String KEY_sim = "sim";
    public static final String KEY_dewey = "5";
    public static final String KEY_donaldDewey = "5D";
    public static final String KEY_highland5 = "5H";
    public static final String KEY_h490 = "4";
    public static final String KEY_wainwright = "w";
    public static final String KEY_wainwrightOutlyingFell = "wo";
    public static final String KEY_birkett = "b";
    public static final String KEY_countyTopHistoric = "coh";
    public static final String KEY_countyTopCurrent = "cou";
    public static final String KEY_countyTopCurrentEqual = "coue";
    public static final String KEY_countyTopAdministrative = "coa";
    public static final String KEY_countyTopLondonBorough = "col";
    public static final String KEY_countyTopLondonBoroughEqual = "cole";
    public static final String KEY_subMarilyn = "sma";
    public static final String KEY_subHump = "shu";
    public static final String KEY_subMurdo = "smur";
    public static final String KEY_subCorbett = "sct";
    public static final String KEY_subGrahamTop = "sgt";
    public static final String KEY_subHewitt = "shew";
    public static final String KEY_subDewey = "s5";
    public static final String KEY_subDonaldDewey = "s5d";
    public static final String KEY_subHighland5 = "s5h";
    public static final String KEY_subh490 = "s4";
    public static final String KEY_deletedMunroTop = "xmt";
    public static final String KEY_deletedCorbett = "xc";
    public static final String KEY_deletedNuttall = "xn";
    public static final String KEY_deletedDonaldTop = "xdt";
    public static final String KEY_buxtonAndLewis = "bl";
    public static final String KEY_bridge = "bg";
    public static final String KEY_trail100 = "t100";
    public static final String KEY_otherList = "o";
    public static final String KEY_un = "un";

    public static final String KEY_HILL_ID = "hill_id";
    public static final String KEY_TYPES_ID = "type_id";



    // private static final Map<Integer, String> hillTypesArray;
//	private static final SparseArray<TempHillClassification> hillTypesArray;
//
//	static {
//		hillTypesArray = new SparseArray<TempHillClassification>();
//
//		hillTypesArray.append(munro_COLUMN,new HillsTables.TempHillClassification( "Munro",20,"Scottish hills at least 3000 feet in height regarded by the SMC as distinct and separate mountains."));
//		hillTypesArray.append(marilyn_COLUMN,new TempHillClassification(  "Marilyn",19,"British and Irish hills of any height with a drop of at least 150 metres on all sides."));
//
//
//		hillTypesArray.append(murdo_COLUMN,new TempHillClassification(  "Murdo",16,"Scottish hills at least 3000 feet in height with a drop of at least 30 metres on all sides."));
//		hillTypesArray.append(furth_COLUMN,new TempHillClassification(  "Furth",15,"Summits equivalent to the Munros and Tops in England, Wales and Ireland."));
//		hillTypesArray.append(corbett_COLUMN,new TempHillClassification(  "Corbett",18,"Scottish hills between 2500 and 2999 feet high with a drop of at least 500 feet (152.4m) on all sides."));
//
//		hillTypesArray.append(graham_COLUMN,new TempHillClassification(  "Graham",17,"Scottish hills between 2000 and 2499 feet high with a drop of at least 150 metres on all sides."));
//
//		hillTypesArray.append(donald_COLUMN,new TempHillClassification(  "Donald",14,"Hills in the Scottish Lowlands at least 2000 feet high."));
//
//		hillTypesArray.append(hewitt_COLUMN,new TempHillClassification(  "Hewitt",13,"Hills in England, Wales and Ireland at least 2000 feet high with a drop of at least 30 metres on all sides."));
//		hillTypesArray.append(nuttall_COLUMN,new TempHillClassification(  "Nuttall",12,"Hills in England and Wales at least 2000 feet high with a drop of at least 15 metres on all sides."));
//		hillTypesArray.append(dillon_COLUMN,new TempHillClassification( "Dillon",11,"Hills in Ireland at least 2000 feet high published in The Mountains of Ireland."));
//		hillTypesArray.append(arderin_COLUMN,new TempHillClassification( "Arderin",10,"Hills in Ireland at least 500 metres high with a drop of at least 30m on all sides."));
//		hillTypesArray.append(vandeleurLynam_COLUMN,new TempHillClassification(  "Vandeleur-Lynam",9,"Hills in Ireland at least 600 metres high with a drop of at least 15 metres on all sides."));
//		hillTypesArray.append(sim_COLUMN,new TempHillClassification(  "Sim",8,"Hills in Britain and Ireland at least 600 metres high with a drop of at least 30 metres on all sides."));
//		hillTypesArray.append(dewey_COLUMN,new TempHillClassification(  "Dewey",7,"Hills in England,Wales and IOM > 500m high and < 609.6m with a drop of at least 30m on all sides."));
//
//		hillTypesArray.append(wainwright_COLUMN,new TempHillClassification(  "Wainwright",6,"The 214 hills listed in volumes 1-7 of Wainwright''s A Pictorial Guide to the Lakeland Fells."));
//		hillTypesArray.append(wainwrightOutlyingFell_COLUMN,new TempHillClassification(
//				"Wainwright Outlying Fells",7,"Hills listed in The Outlying Fells of Lakeland."));
//		hillTypesArray.append(birkett_COLUMN,new TempHillClassification(  "Birkett",5,"Lake District hills over 1,000ft listed in Bill Birkett''s Complete Lakeland Fells."));
//		hillTypesArray
//				.append(countyTopHistoric_COLUMN,new TempHillClassification(  "County Top - Historic",0,""));
//		hillTypesArray
//				.append(countyTopHistoricTwin_COLUMN,new TempHillClassification(  "County Top Twin - Historic",0,""));
//		hillTypesArray.append(countyTopCurrent_COLUMN,new TempHillClassification(
//				"County Top - Current County and Unitary Authority",0,""));
//		hillTypesArray.append(countyTopCurrentTwin_COLUMN,new TempHillClassification(  "Current County Twin Top",0,""));
//		hillTypesArray.append(countyTopAdministrative_COLUMN,new TempHillClassification(
//				"County Top - Administrative",0,""));
//		hillTypesArray.append(countyTopAdministrativeTwin_COLUMN,new TempHillClassification(
//				"County Top Twin - Administrative",0,""));
//		hillTypesArray.append(donaldTop_COLUMN,new TempHillClassification(  "Donald Top",0,""));
//		hillTypesArray.append(countyTopLondonBorough_COLUMN,new TempHillClassification(
//				"County Top - London Borough",0,""));
//		hillTypesArray.append(grahamTop_COLUMN,new TempHillClassification(  "Graham Top",0,""));
//		hillTypesArray.append(countyTopLondonBoroughTwin_COLUMN,new TempHillClassification(  "London Borough Twin Top",0,""));
//		hillTypesArray.append(significantIsland_COLUMN,new TempHillClassification(  "Significant Island of Britain",0,""));
//		hillTypesArray.append(subMarilyn_COLUMN,new TempHillClassification(  "Sub Marilyn",0,""));
//		hillTypesArray.append(subHump_COLUMN,new TempHillClassification(  "Sub HuMP",0,""));
//		hillTypesArray.append(subSim_COLUMN,new TempHillClassification(  "Sub Sim",0,""));
//
//		hillTypesArray.append(subDewey_COLUMN,new TempHillClassification(  "Sub Dewey",0,""));
//		hillTypesArray.append(subDonaldDewey_COLUMN,new TempHillClassification(  "Sub Donald Dewey",0,""));
//		hillTypesArray.append(subHighland5_COLUMN,new TempHillClassification(  "Sub Highland Five",0,""));
//		hillTypesArray.append(subMyrddynDewey_COLUMN,new TempHillClassification(  "Sub Myrddyn Dewey",0,""));
//		hillTypesArray.append(subh490_COLUMN,new TempHillClassification(  "Sub Tump 490-499m",0,""));
//		hillTypesArray.append(deletedMunroTop_COLUMN,new TempHillClassification(  "Deleted Munro Top",0,""));
//		hillTypesArray.append(deletedCorbett_COLUMN,new TempHillClassification(  "Deleted Corbett",0,""));
//		hillTypesArray.append(deletedNuttall_COLUMN,new TempHillClassification(  "Deleted Nuttall",0,""));
//		hillTypesArray.append(deletedDonaldTop_COLUMN,new TempHillClassification(  "Deleted Donald Top",0,""));
//		hillTypesArray.append(buxtonAndLewis_COLUMN,new TempHillClassification(  "Buxton and Lewis",0,""));
//		hillTypesArray.append(bridge_COLUMN,new TempHillClassification(  "Bridge",0,""));
//		hillTypesArray.append(trail100_COLUMN,new TempHillClassification(  "Trail 100",0,""));
//		hillTypesArray.append(carns_COLUMN,new TempHillClassification(  "Carns",0,""));
//		hillTypesArray.append(binnions_COLUMN,new TempHillClassification(  "Binnions",0,""));
//		hillTypesArray.append(otherList_COLUMN,new TempHillClassification(  "Other",0,""));
//		hillTypesArray.append(un_COLUMN,new TempHillClassification(  "Unclassified",0,""));
//
//		hillTypesArray.append(marilynTwinTop_COLUMN,new TempHillClassification(  "Marilyn Twin Top",0,""));
//		hillTypesArray.append(hump_COLUMN,new TempHillClassification(  "HuMP",2,"British and Irish Hills of any height with a drop of at least 100 metres or more on all sides."));
//		hillTypesArray.append(humpTwinTop_COLUMN,new TempHillClassification(  "HuMP Twin Top",0,""));
//		hillTypesArray.append(tump_COLUMN,new TempHillClassification(  "Tump",0,""));
//		hillTypesArray.append(tumpTwinTop_COLUMN,new TempHillClassification(  "Tump Twin Top",0,""));
//		hillTypesArray.append(tump4_COLUMN,new TempHillClassification(  "400-499m Tump",0,""));
//		hillTypesArray.append(tump3_COLUMN,new TempHillClassification(  "300-399m Tump",0,""));
//		hillTypesArray.append(tump2_COLUMN,new TempHillClassification(  "200-299m Tump",0,""));
//		hillTypesArray.append(tump1_COLUMN,new TempHillClassification(  "100-199m Tump",0,""));
//		hillTypesArray.append(tump1Twin_COLUMN,new TempHillClassification(  "100-199m Tump Twin Top",0,""));
//		hillTypesArray.append(tump0_COLUMN,new TempHillClassification(  "0-99m Tump",0,""));
//		hillTypesArray.append(donaldDewey_COLUMN,new TempHillClassification(  "Donald Dewey",0,""));
//		hillTypesArray.append(highland5_COLUMN,new TempHillClassification(  "Highland Five",0,""));
//		hillTypesArray.append(myrddynDeweyCOLUMN,new TempHillClassification(  "Myrddyn Dewey",0,""));
//		hillTypesArray.append(h490_COLUMN,new TempHillClassification(  "490-499m with 30m drop",0,""));
//		hillTypesArray.append(munroTop_COLUMN,new TempHillClassification(  "Munro Top",0,""));
//		hillTypesArray.append(corbettTop_COLUMN,new TempHillClassification(  "Corbett Top",0,""));
//
//	}





    private static final String HILLTYPES_CREATE = "CREATE TABLE "
            + HILLTYPES_TABLE + "(" + KEY_ID + " integer primary key," + KEY_TITLE + " unique)";

    private static final String TYPESLINK_CREATE = "CREATE TABLE "
            + TYPES_LINK_TABLE + "(" + KEY_ID
            + " integer primary key autoincrement," + KEY_HILL_ID
            + " references " + HILLS_TABLE + "(" + KEY_ID + ")," + KEY_TYPES_ID
            + " references " + HILLTYPES_TABLE + "(" + KEY_ID + "))";

    public static void onCreate(SQLiteDatabase database, Context context) {
        // create tables

        database.execSQL("PRAGMA foreign_keys=ON;");
        database.execSQL(HILLTYPES_CREATE);
        database.execSQL(TYPESLINK_CREATE);

        // populate static hills data
        long startTime = System.currentTimeMillis();
        InputStream is;
        try {

            is = context.getAssets().open(HILLS_CSV);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));

            // read main hill data into hills table
            database.beginTransaction();
            String line = null;

            int col = 1;

            // read and ignore title row
            String headerRow = reader.readLine();
            String hillsTableStarter = "CREATE TABLE '" + HILLS_TABLE
                    + "' ('" + KEY_ID + "' integer primary key,";
            String[] headerArray = headerRow.split(",");
            StringBuilder createHillsTableBuilder = new StringBuilder();
            for (String header : headerArray) {
                if (!"Number".equals(header)) {
                    createHillsTableBuilder.append("'" + header + "',");
                }
            }
            String c = createHillsTableBuilder.toString();
            String createHillsTable = hillsTableStarter + c.substring(0, c.length() - 1) + ")";

            System.out.println(createHillsTable);
            database.execSQL(createHillsTable);


            StringBuffer insertHillsBuffer;
            // read each line of text file
            while ((line = reader.readLine()) != null) {
                insertHillsBuffer = new StringBuffer();
                insertHillsBuffer.append("INSERT INTO " + HILLS_TABLE
                        + " VALUES (");
                String[] lineArray = line
                        .split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                // StringTokenizer st = new StringTokenizer(line, ",");
                // read lines up to marilyn column - the first type column
                for (String entry : lineArray) {
                    insertHillsBuffer.append("'" + entry.replace("'", "''")
                            + "',");

                }
                insertHillsBuffer.deleteCharAt(insertHillsBuffer.length() - 1);

                insertHillsBuffer.append(")");
                String statement = insertHillsBuffer.toString();
                database.execSQL(statement);

            }
            database.setTransactionSuccessful();
            database.endTransaction();
            Log.d(HillsTables.class.getName(),
                    "#################Finished inserting base hill information after "
                            + (System.currentTimeMillis() - startTime) / 1000);
        } catch (IOException e) {
            Log.e(HillsTables.class.getName(),
                    "Failed to populate hills database table", e);
        }

        // Populate static hill types data
        database.beginTransaction();
        Cursor c = database.query(HILLS_TABLE, null, null, null, null, null, null, null);
        SQLiteStatement insertHillType = database.compileStatement("INSERT or IGNORE into " + HILLTYPES_TABLE + " VALUES(?,?)");
        SQLiteStatement insertHillTypeLink = database.compileStatement("INSERT into " + TYPES_LINK_TABLE + " (" + KEY_HILL_ID + "," + KEY_TYPES_ID + ") values (?,?)");


        if (c.moveToFirst()) {
            do {

                String[] classifications = c.getString(c.getColumnIndex(KEY_CLASSIFICATION)).replace("\"", "").split(",");
                for (String classification : classifications) {
                    insertHillType.bindString(2, classification);
                    insertHillType.bindLong(1, c.getColumnIndex(classification));
                    insertHillType.executeInsert();

                    insertHillTypeLink.bindLong(1, c.getInt(0));
                    insertHillTypeLink.bindLong(2, c.getColumnIndex(classification));
                    insertHillTypeLink.executeInsert();
                }


            } while (c.moveToNext());
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        c.close();
        Log.d(HillsTables.class.getName(),
                "#################Finished inserting hill types information after "
                        + (System.currentTimeMillis() - startTime) / 1000);




    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion, Context context) {
        Log.w(HillsTables.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old hills data");
        database.execSQL("DROP TABLE IF EXISTS " + HILLS_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + HILLTYPES_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + TYPES_LINK_TABLE);
        onCreate(database, context);
    }

    private static class TempHillClassification {

        String name;
        int importance;
        String description;

        public TempHillClassification(String name,
                                      int importance, String description) {


            this.name = name;
            this.importance = importance;
            this.description = description;
        }


    }

}
