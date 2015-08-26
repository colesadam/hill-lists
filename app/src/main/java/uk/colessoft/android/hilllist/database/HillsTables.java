package uk.colessoft.android.hilllist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.util.SparseArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class HillsTables {

	private static Pattern pattern = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

	public static long start;

	public static final String KEY_TITLE = "title";

	private static final String HILLS_CSV = "DoBIH_v13_1.csv";

	public static final String HILLS_TABLE = "hills";
	public static final String HILLTYPES_TABLE = "hilltypes";
	public static final String TYPES_LINK_TABLE = "typeslink";
	public static final String KEY_IMPORTANCE = "importance";
	public static final String KEY_DESC = "description";
	public static final String KEY_ID = "_id";
	public static final String KEY_HILLNAME = "Hillname";
	public static final String KEY_SECTION = "Section";
	public static final String KEY_REGION = "Region";
	public static final String KEY_AREA = "Area";
	public static final String KEY_CLASSIFICATION = "Classification";
	public static final String KEY_MAP = "Map";
	public static final String KEY_MAP25 = "Map25";
	public static final String KEY_HEIGHTM = "Metres";
	public static final String KEY_HEIGHTF = "Feet";
	public static final String KEY_GRIDREF = "Gridref";
	public static final String KEY_GRIDREF10 = "Gridref10";
	public static final String KEY_DROP = "sDrop";
	public static final String KEY_COLGRIDREF = "Colgridref";
	public static final String KEY_COLHEIGHT = "Colheight";
	public static final String KEY_FEATURE = "Feature";
	public static final String KEY_OBSERVATIONS = "Observations";
	public static final String KEY_SURVEY = "Survey";
	public static final String KEY_CLIMBED = "Climbed";
	public static final String KEY_COUNTRY = "Country";
	public static final String KEY_COUNTY = "County";
	public static final String KEY_REVISION = "Revision";
	public static final String KEY_COMMENTS = "Comments";
	public static final String KEY_STREETMAP = "Streetmap";
	public static final String KEY_GEOGRAPH = "Geograph";
	public static final String KEY_HILLBAGGING = "HillBagging";// "\"Hill-bagging\"";
	public static final String KEY_XCOORD = "xcoord";
	public static final String KEY_YCOORD = "ycoord";
	public static final String KEY_LATITUDE = "Latitude";
	public static final String KEY_LONGITUDE = "Longitude";
	public static final String KEY_XSECTION = "_Section";
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
	public static final String KEY_corbettTopOfMunro = "ctm";
	public static final String KEY_corbettTopOfCorbett = "ctc";
	public static final String KEY_graham = "g";
	public static final String KEY_grahamTop = "gt";
	public static final String KEY_grahamTopOfMunro = "gtm";
	public static final String KEY_grahamTopOfCorbett = "gtc";
	public static final String KEY_grahamTopOfGraham = "gtg";
	public static final String KEY_grahamTopOfHump = "gth";
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

	public static final int NAME_COLUMN = 1;
	public static final int marilyn_COLUMN = 32;
	public static final int marilynTwinTop_COLUMN = 33;
	public static final int hump_COLUMN = 34;
	public static final int humpTwinTop_COLUMN = 35;
	public static final int munro_COLUMN = 36;
	public static final int munroTop_COLUMN = 37;
	public static final int murdo_COLUMN = 38;
	public static final int furth_COLUMN = 39;
	public static final int corbett_COLUMN = 40;
	public static final int corbettTop_COLUMN = 41;
	public static final int corbettTopOfMunro_COLUMN = 42;
	public static final int corbettTopOfCorbett_COLUMN = 43;
	public static final int graham_COLUMN = 44;
	public static final int grahamTop_COLUMN = 45;
	public static final int grahamTopOfMunro_COLUMN = 46;
	public static final int grahamTopOfCorbett_COLUMN = 47;
	public static final int grahamTopOfGraham_COLUMN = 48;
	public static final int grahamTopOfHump_COLUMN = 49;
	public static final int donald_COLUMN = 50;
	public static final int donaldTop_COLUMN = 51;
	public static final int hewitt_COLUMN = 52;
	public static final int nuttall_COLUMN = 53;
	
	public static final int dillon_COLUMN=54;
	public static final int arderin_COLUMN=55;
	public static final int vandeleurLynam_COLUMN=56;
	
	public static final int sim_COLUMN = 57;
	public static final int dewey_COLUMN = 58;
	public static final int donaldDewey_COLUMN = 59;
public static final int highland5_COLUMN = 60;

public static final int myrddynDeweyCOLUMN=61;



	public static final int h490_COLUMN = 62;
	public static final int wainwright_COLUMN = 63;
	public static final int wainwrightOutlyingFell_COLUMN = 64;
	public static final int birkett_COLUMN = 65;
	public static final int countyTopHistoric_COLUMN = 66;
	public static final int countyTopCurrent_COLUMN = 67;
	public static final int countyTopCurrentTwin_COLUMN = 68;
	public static final int countyTopAdministrative_COLUMN = 69;
	public static final int countyTopLondonBorough_COLUMN = 70;
	public static final int countyTopLondonBoroughTwin_COLUMN = 71;
	public static final int subMarilyn_COLUMN = 72;
	public static final int subHump_COLUMN = 73;
	public static final int subMurdo_COLUMN = 74;
	public static final int subCorbett_COLUMN = 75;
	public static final int subGrahamTop_COLUMN = 76;
	public static final int subHewitt_COLUMN = 77;
	public static final int subDewey_COLUMN = 78;
	public static final int subDonaldDewey_COLUMN = 79;
	
	
	
	public static final int subHighland5_COLUMN = 80;
	
	public static final int subMyrddynDewey_COLUMN=81;
	
	
	public static final int subh490_COLUMN = 82;
	public static final int deletedMunroTop_COLUMN = 83;
	public static final int deletedCorbett_COLUMN = 84;
	public static final int deletedNuttall_COLUMN = 85;
	public static final int deletedDonaldTop_COLUMN = 86;
	public static final int buxtonAndLewis_COLUMN = 87;
	public static final int bridge_COLUMN = 88;
	public static final int trail100_COLUMN = 89;
	public static final int carns_COLUMN=90;
	public static final int binnions_COLUMN=91;
	public static final int otherList_COLUMN = 92;
	public static final int un_COLUMN = 93;

	// private static final Map<Integer, String> hillTypesArray;
	private static final SparseArray<TempHillClassification> hillTypesArray;
	static {
		hillTypesArray = new SparseArray<TempHillClassification>();

		hillTypesArray.append(munro_COLUMN,new HillsTables.TempHillClassification( "Munro",20,"Scottish hills at least 3000 feet in height regarded by the SMC as distinct and separate mountains."));
		hillTypesArray.append(marilyn_COLUMN,new TempHillClassification(  "Marilyn",19,"British and Irish hills of any height with a drop of at least 150 metres on all sides."));
		hillTypesArray.append(marilynTwinTop_COLUMN,new TempHillClassification(  "Marilyn Twin Top",0,""));
		hillTypesArray.append(hump_COLUMN,new TempHillClassification(  "HuMP",2,"British and Irish Hills of any height with a drop of at least 100 metres or more on all sides."));
		hillTypesArray.append(humpTwinTop_COLUMN,new TempHillClassification(  "HuMP Twin Top",0,""));
		
		hillTypesArray.append(munroTop_COLUMN,new TempHillClassification(  "Munro Top",0,""));
		hillTypesArray.append(murdo_COLUMN,new TempHillClassification(  "Murdo",16,"Scottish hills at least 3000 feet in height with a drop of at least 30 metres on all sides."));
		hillTypesArray.append(furth_COLUMN,new TempHillClassification(  "Furth",15,"Summits equivalent to the Munros and Tops in England, Wales and Ireland."));
		hillTypesArray.append(corbett_COLUMN,new TempHillClassification(  "Corbett",18,"Scottish hills between 2500 and 2999 feet high with a drop of at least 500 feet (152.4m) on all sides."));
		hillTypesArray.append(corbettTop_COLUMN,new TempHillClassification(  "Corbett Top",0,""));
		hillTypesArray.append(corbettTopOfMunro_COLUMN,new TempHillClassification(  "Corbett Top of Munro",0,""));
		hillTypesArray.append(corbettTopOfCorbett_COLUMN,new TempHillClassification( 
				"Corbett Top of Corbett",0,""));
		hillTypesArray.append(graham_COLUMN,new TempHillClassification(  "Graham",17,"Scottish hills between 2000 and 2499 feet high with a drop of at least 150 metres on all sides."));
		hillTypesArray.append(grahamTop_COLUMN,new TempHillClassification(  "Graham Top",0,""));
		hillTypesArray.append(grahamTopOfMunro_COLUMN,new TempHillClassification(  "Graham Top of Munro",0,""));
		hillTypesArray.append(grahamTopOfCorbett_COLUMN,new TempHillClassification( 
				"Graham Top of Corbett",0,""));
		hillTypesArray.append(grahamTopOfGraham_COLUMN,new TempHillClassification(  "Graham Top of Graham",0,""));
		hillTypesArray.append(grahamTopOfHump_COLUMN,new TempHillClassification(  "Graham Top of HuMP",0,""));
		hillTypesArray.append(donald_COLUMN,new TempHillClassification(  "Donald",14,"Hills in the Scottish Lowlands at least 2000 feet high."));
		hillTypesArray.append(donaldTop_COLUMN,new TempHillClassification(  "Donald Top",0,""));
		hillTypesArray.append(hewitt_COLUMN,new TempHillClassification(  "Hewitt",13,"Hills in England, Wales and Ireland at least 2000 feet high with a drop of at least 30 metres on all sides."));
		hillTypesArray.append(nuttall_COLUMN,new TempHillClassification(  "Nuttall",12,"Hills in England and Wales at least 2000 feet high with a drop of at least 15 metres on all sides."));
		hillTypesArray.append(dillon_COLUMN,new TempHillClassification( "Dillon",11,"Hills in Ireland at least 2000 feet high published in The Mountains of Ireland."));
		hillTypesArray.append(arderin_COLUMN,new TempHillClassification( "Arderin",10,"Hills in Ireland at least 500 metres high with a drop of at least 30m on all sides."));
		hillTypesArray.append(vandeleurLynam_COLUMN,new TempHillClassification(  "Vandeleur-Lynam",9,"Hills in Ireland at least 600 metres high with a drop of at least 15 metres on all sides."));
		hillTypesArray.append(sim_COLUMN,new TempHillClassification(  "Sim",8,"Hills in Britain and Ireland at least 600 metres high with a drop of at least 30 metres on all sides."));
		hillTypesArray.append(dewey_COLUMN,new TempHillClassification(  "Dewey",7,"Hills in England,Wales and IOM > 500m high and < 609.6m with a drop of at least 30m on all sides."));
		hillTypesArray.append(donaldDewey_COLUMN,new TempHillClassification(  "Donald Dewey",0,""));
		hillTypesArray.append(highland5_COLUMN,new TempHillClassification(  "Highland Five",0,""));
		hillTypesArray.append(myrddynDeweyCOLUMN,new TempHillClassification(  "Myrddyn Dewey",0,""));
		hillTypesArray.append(h490_COLUMN,new TempHillClassification(  "490-499m with 30m drop",0,""));
		hillTypesArray.append(wainwright_COLUMN,new TempHillClassification(  "Wainwright",6,"The 214 hills listed in volumes 1-7 of Wainwright''s A Pictorial Guide to the Lakeland Fells."));
		hillTypesArray.append(wainwrightOutlyingFell_COLUMN,new TempHillClassification( 
				"Wainwright Outlying Fells",7,"Hills listed in The Outlying Fells of Lakeland."));
		hillTypesArray.append(birkett_COLUMN,new TempHillClassification(  "Birkett",5,"Lake District hills over 1,000ft listed in Bill Birkett''s Complete Lakeland Fells."));
		hillTypesArray
				.append(countyTopHistoric_COLUMN,new TempHillClassification(  "County Top - Historic",0,""));
		hillTypesArray.append(countyTopCurrent_COLUMN,new TempHillClassification( 
				"County Top � Current County and Unitary Authority",0,""));
		hillTypesArray.append(countyTopCurrentTwin_COLUMN,new TempHillClassification(  "Current County Twin Top",0,""));
		hillTypesArray.append(countyTopAdministrative_COLUMN,new TempHillClassification( 
				"County Top � Administrative",0,""));
		hillTypesArray.append(countyTopLondonBorough_COLUMN,new TempHillClassification( 
				"County Top � London Borough",0,""));
		hillTypesArray.append(countyTopLondonBoroughTwin_COLUMN,new TempHillClassification(  "London Borough Twin Top",0,""));
		hillTypesArray.append(subMarilyn_COLUMN,new TempHillClassification(  "Sub Marilyn",0,""));
		hillTypesArray.append(subHump_COLUMN,new TempHillClassification(  "Sub HuMP",0,""));
		hillTypesArray.append(subMurdo_COLUMN,new TempHillClassification(  "Sub Murdo",0,""));
		hillTypesArray.append(subCorbett_COLUMN,new TempHillClassification(  "Sub Corbett",0,""));
		hillTypesArray.append(subGrahamTop_COLUMN,new TempHillClassification(  "Sub Graham Top",0,""));
		hillTypesArray.append(subHewitt_COLUMN,new TempHillClassification(  "Sub Hewitt",0,""));
		hillTypesArray.append(subDewey_COLUMN,new TempHillClassification(  "Sub Dewey",0,""));
		hillTypesArray.append(subDonaldDewey_COLUMN,new TempHillClassification(  "Sub Donald Dewey",0,""));
		hillTypesArray.append(subHighland5_COLUMN,new TempHillClassification(  "Sub Highland Five",0,""));
		hillTypesArray.append(subMyrddynDewey_COLUMN,new TempHillClassification(  "Sub Myrddyn Dewey",0,""));
		hillTypesArray.append(subh490_COLUMN,new TempHillClassification(  "Sub Hills 490-499m",0,""));
		hillTypesArray.append(deletedMunroTop_COLUMN,new TempHillClassification(  "Deleted Munro Top",0,""));
		hillTypesArray.append(deletedCorbett_COLUMN,new TempHillClassification(  "Deleted Corbett",0,""));
		hillTypesArray.append(deletedNuttall_COLUMN,new TempHillClassification(  "Deleted Nuttall",0,""));
		hillTypesArray.append(deletedDonaldTop_COLUMN,new TempHillClassification(  "Deleted Donald Top",0,""));
		hillTypesArray.append(buxtonAndLewis_COLUMN,new TempHillClassification(  "Buxton and Lewis",0,""));
		hillTypesArray.append(bridge_COLUMN,new TempHillClassification(  "Bridge",0,""));
		hillTypesArray.append(trail100_COLUMN,new TempHillClassification(  "Trail 100",0,""));
		hillTypesArray.append(carns_COLUMN,new TempHillClassification(  "Carns",0,""));
		hillTypesArray.append(binnions_COLUMN,new TempHillClassification(  "Binnions",0,""));
		hillTypesArray.append(otherList_COLUMN,new TempHillClassification(  "Other",0,""));
		hillTypesArray.append(un_COLUMN,new TempHillClassification(  "Unclassified",0,""));

	}

	// Database creation SQL statement
	private static final String HILLS_CREATE = "CREATE TABLE '" + HILLS_TABLE
			+ "' ('" + KEY_ID + "' integer primary key,'" + KEY_HILLNAME
			+ "','" + KEY_SECTION + "','" + KEY_REGION + "','" + KEY_AREA
			+ "','" + KEY_CLASSIFICATION + "','" + KEY_MAP + "','" + KEY_MAP25
			+ "','" + KEY_HEIGHTM + "','" + KEY_HEIGHTF + "','" + KEY_GRIDREF
			+ "','" + KEY_GRIDREF10 + "','" + KEY_DROP + "','" + KEY_COLGRIDREF
			+ "','" + KEY_COLHEIGHT + "','" + KEY_FEATURE + "','"
			+ KEY_OBSERVATIONS + "','" + KEY_SURVEY + "','" + KEY_CLIMBED
			+ "','" + KEY_COUNTRY + "','" + KEY_COUNTY + "','" + KEY_REVISION
			+ "','" + KEY_COMMENTS + "','" + KEY_STREETMAP + "','"
			+ KEY_GEOGRAPH + "','" + KEY_HILLBAGGING + "','" + KEY_XCOORD
			+ "','" + KEY_YCOORD + "','" + KEY_LATITUDE + "','" + KEY_LONGITUDE
			+ "','" + KEY_XSECTION + "');";

	

	

	private static final String HILLTYPES_CREATE = "CREATE TABLE "
			+ HILLTYPES_TABLE + "(" + KEY_ID + " integer primary key,"+KEY_TITLE+","+KEY_IMPORTANCE+" integer,"+KEY_DESC+")";

	private static final String TYPESLINK_CREATE = "CREATE TABLE "
			+ TYPES_LINK_TABLE + "(" + KEY_ID
			+ " integer primary key autoincrement," + KEY_HILL_ID
			+ " references " + HILLS_TABLE + "(" + KEY_ID + ")," + KEY_TYPES_ID
			+ " references " + HILLTYPES_TABLE + "(" + KEY_ID + "))";

	public static void onCreate(SQLiteDatabase database, Context context) {
		// create tables

		start=System.nanoTime();
		database.execSQL("PRAGMA foreign_keys=ON;");
		database.execSQL(HILLS_CREATE);
		database.execSQL(HILLTYPES_CREATE);
		database.execSQL(TYPESLINK_CREATE);

		// populate static hills data

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
			reader.readLine();

			// read each line of text file

			StringBuilder sqlBuilder= new StringBuilder("INSERT INTO " + HILLS_TABLE
					+ " VALUES(");
			for(int i=1;i<marilyn_COLUMN;i++)
				sqlBuilder.append("?,");

			String sql=sqlBuilder.toString();
			sql=sql.substring(0,sql.length()-1);
			sql=sql+")";

			SQLiteStatement stmt=database.compileStatement(sql);
			Log.d(HillsTables.class.getName(),
					"####Starting insert of hills table after "+(System.nanoTime()-start)/1000000+" ms");
			while ((line = reader.readLine()) != null) {
				profilingLog("Read next line");
				String[] lineArray = pattern.split(line);
				profilingLog("Split line");
				// read lines up to marilyn column - the first type column

				for (String entry : lineArray) {
					if (col < marilyn_COLUMN) {
						stmt.bindString(col,entry.replace("'", "''"));
					}
					col++;

				}
				profilingLog("Modified Columns");

				col = 1;

				stmt.executeInsert();
				stmt.clearBindings();
				profilingLog("Executed statement");


			}
			database.setTransactionSuccessful();
			database.endTransaction();
			Log.d(HillsTables.class.getName(),
					"####Finished inserting base hill information after "+(System.nanoTime()-start)/1000000+ "ms");
		} catch (IOException e) {
			Log.e(HillsTables.class.getName(),
					"Failed to populate hills database table", e);
		}

		// Populate static hill types data
		database.beginTransaction();
		for (int i = 0; i < hillTypesArray.size(); i++) {
			database.execSQL("INSERT INTO " + HILLTYPES_TABLE + " VALUES ("
					+ hillTypesArray.keyAt(i) + ",'"
					+ hillTypesArray.valueAt(i).name + "',"+hillTypesArray.valueAt(i).importance+",'"+hillTypesArray.valueAt(i).description+"')");
		}
		database.setTransactionSuccessful();
		database.endTransaction();
		Log.d(HillsTables.class.getName(),
				"#####Finished inserting hill type information after "+(System.nanoTime()-start)/1000000+" ms");

		// Populate link table for hill types

		try {
			is = context.getAssets().open(HILLS_CSV);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));

			// read main hill data into hills table
			database.beginTransaction();
			String line = null;

			int col = 32;

			// read and ignore title row
			reader.readLine();
			// read each line of text file
			String hillId = "";
			StringTokenizer st;
			while ((line = reader.readLine()) != null) {

				String[] lineArray = pattern.split(line);
				// read lines up to marilyn column - the first type column
				StringBuilder insertHillTypesLinkBuilder;
				hillId = lineArray[0];
				for (int i = col-1; i < lineArray.length; i++) {
					insertHillTypesLinkBuilder = new StringBuilder();
					insertHillTypesLinkBuilder.append("INSERT INTO "
							+ TYPES_LINK_TABLE + " VALUES (null,");
					// get next token check to see if it applies
					String nextToken = lineArray[i];

					// The csv column has 1 if the classification applies, 0
					// if
					// not.
					if ("1".equals(nextToken)) {
						// we know that col should reference the correct
						// value
						// in the types table
						insertHillTypesLinkBuilder
								.append(hillId + "," + (i + 1) + ")");
						database.execSQL(insertHillTypesLinkBuilder.toString());

					}
				}
			}
			database.setTransactionSuccessful();
			database.endTransaction();
			Log.d(HillsTables.class.getName(),
					"####Finished inserting hill links information after "+(System.nanoTime()-start)/1000000+" ms");
		} catch (IOException e) {
			Log.e(HillsTables.class.getName(),
					"Failed to populate link database table", e);
		}

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
	
	private static class TempHillClassification{
	
		String name;
		int importance;
		String description;
		public TempHillClassification( String name,
				int importance,String description) {
	
		
			this.name = name;
			this.importance = importance;
			this.description=description;
		}


		
		
	}
	private static void profilingLog(String message){
		Log.d(HillsTables.class.getName(),
				"####"+message+" at "+(System.nanoTime()-start)+" nanoseconds");
	}

}
