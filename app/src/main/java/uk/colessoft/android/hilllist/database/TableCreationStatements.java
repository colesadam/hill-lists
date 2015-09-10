package uk.colessoft.android.hilllist.database;

public class TableCreationStatements {

    public static final String HILLS_CREATE = "CREATE TABLE '" + TableNames.HILLS_TABLE
            + "' ('" + ColumnKeys.KEY_ID + "' integer primary key,'" + ColumnKeys.KEY_HILLNAME
            + "','" + ColumnKeys.KEY_SECTION + "','" + ColumnKeys.KEY_REGION + "','" + ColumnKeys.KEY_AREA
            + "','" + ColumnKeys.KEY_CLASSIFICATION + "','" + ColumnKeys.KEY_MAP + "','" + ColumnKeys.KEY_MAP25
            + "','" + ColumnKeys.KEY_HEIGHTM + "','" + ColumnKeys.KEY_HEIGHTF + "','" + ColumnKeys.KEY_GRIDREF
            + "','" + ColumnKeys.KEY_GRIDREF10 + "','" + ColumnKeys.KEY_DROP + "','" + ColumnKeys.KEY_COLGRIDREF
            + "','" + ColumnKeys.KEY_COLHEIGHT + "','" + ColumnKeys.KEY_FEATURE + "','"
            + ColumnKeys.KEY_OBSERVATIONS + "','" + ColumnKeys.KEY_SURVEY + "','" + ColumnKeys.KEY_CLIMBED
            + "','" + ColumnKeys.KEY_COUNTRY + "','" + ColumnKeys.KEY_COUNTY + "','" + ColumnKeys.KEY_REVISION
            + "','" + ColumnKeys.KEY_COMMENTS + "','" + ColumnKeys.KEY_STREETMAP + "','"
            + ColumnKeys.KEY_GEOGRAPH + "','" + ColumnKeys.KEY_HILLBAGGING + "','" + ColumnKeys.KEY_XCOORD
            + "','" + ColumnKeys.KEY_YCOORD + "','" + ColumnKeys.KEY_LATITUDE + "','" + ColumnKeys.KEY_LONGITUDE
            + "','" + ColumnKeys.KEY_XSECTION + "');";

    public static final String HILLTYPES_CREATE = "CREATE TABLE "
            + TableNames.HILLTYPES_TABLE + "(" + ColumnKeys.KEY_ID + " integer primary key," + ColumnKeys.KEY_TITLE + "," + ColumnKeys.KEY_IMPORTANCE + " integer," + ColumnKeys.KEY_DESC + ")";

    public static final String TYPESLINK_CREATE = "CREATE TABLE "
            + TableNames.TYPES_LINK_TABLE + "(" + ColumnKeys.KEY_ID
            + " integer primary key autoincrement," + ColumnKeys.KEY_HILL_ID
            + " references " + TableNames.HILLS_TABLE + "(" + ColumnKeys.KEY_ID + ")," + ColumnKeys.KEY_TYPES_ID
            + " references " + TableNames.HILLTYPES_TABLE + "(" + ColumnKeys.KEY_ID + "))";
}
