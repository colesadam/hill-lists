package uk.colessoft.android.hilllist.database;


import android.util.SparseArray;

public class HillTypes {

    public static final SparseArray<TempHillClassification> HILL_TYPES_ARRAY;

    static {
        HILL_TYPES_ARRAY = new SparseArray<TempHillClassification>();

        HILL_TYPES_ARRAY.append(ColumnNumbers.munro_COLUMN, new TempHillClassification("Munro", 20, "Scottish hills at least 3000 feet in height regarded by the SMC as distinct and separate mountains."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.marilyn_COLUMN, new TempHillClassification("Marilyn", 19, "British and Irish hills of any height with a drop of at least 150 metres on all sides."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.marilynTwinTop_COLUMN, new TempHillClassification("Marilyn Twin Top", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.hump_COLUMN, new TempHillClassification("HuMP", 2, "British and Irish Hills of any height with a drop of at least 100 metres or more on all sides."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.humpTwinTop_COLUMN, new TempHillClassification("HuMP Twin Top", 0, ""));

        HILL_TYPES_ARRAY.append(ColumnNumbers.munroTop_COLUMN, new TempHillClassification("Munro Top", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.murdo_COLUMN, new TempHillClassification("Murdo", 16, "Scottish hills at least 3000 feet in height with a drop of at least 30 metres on all sides."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.furth_COLUMN, new TempHillClassification("Furth", 15, "Summits equivalent to the Munros and Tops in England, Wales and Ireland."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.corbett_COLUMN, new TempHillClassification("Corbett", 18, "Scottish hills between 2500 and 2999 feet high with a drop of at least 500 feet (152.4m) on all sides."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.corbettTop_COLUMN, new TempHillClassification("Corbett Top", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.corbettTopOfMunro_COLUMN, new TempHillClassification("Corbett Top of Munro", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.corbettTopOfCorbett_COLUMN, new TempHillClassification(
                "Corbett Top of Corbett", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.graham_COLUMN, new TempHillClassification("Graham", 17, "Scottish hills between 2000 and 2499 feet high with a drop of at least 150 metres on all sides."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.grahamTop_COLUMN, new TempHillClassification("Graham Top", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.grahamTopOfMunro_COLUMN, new TempHillClassification("Graham Top of Munro", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.grahamTopOfCorbett_COLUMN, new TempHillClassification(
                "Graham Top of Corbett", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.grahamTopOfGraham_COLUMN, new TempHillClassification("Graham Top of Graham", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.grahamTopOfHump_COLUMN, new TempHillClassification("Graham Top of HuMP", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.donald_COLUMN, new TempHillClassification("Donald", 14, "Hills in the Scottish Lowlands at least 2000 feet high."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.donaldTop_COLUMN, new TempHillClassification("Donald Top", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.hewitt_COLUMN, new TempHillClassification("Hewitt", 13, "Hills in England, Wales and Ireland at least 2000 feet high with a drop of at least 30 metres on all sides."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.nuttall_COLUMN, new TempHillClassification("Nuttall", 12, "Hills in England and Wales at least 2000 feet high with a drop of at least 15 metres on all sides."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.dillon_COLUMN, new TempHillClassification("Dillon", 11, "Hills in Ireland at least 2000 feet high published in The Mountains of Ireland."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.arderin_COLUMN, new TempHillClassification("Arderin", 10, "Hills in Ireland at least 500 metres high with a drop of at least 30m on all sides."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.vandeleurLynam_COLUMN, new TempHillClassification("Vandeleur-Lynam", 9, "Hills in Ireland at least 600 metres high with a drop of at least 15 metres on all sides."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.sim_COLUMN, new TempHillClassification("Sim", 8, "Hills in Britain and Ireland at least 600 metres high with a drop of at least 30 metres on all sides."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.dewey_COLUMN, new TempHillClassification("Dewey", 7, "Hills in England,Wales and IOM > 500m high and < 609.6m with a drop of at least 30m on all sides."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.donaldDewey_COLUMN, new TempHillClassification("Donald Dewey", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.highland5_COLUMN, new TempHillClassification("Highland Five", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.myrddynDeweyCOLUMN, new TempHillClassification("Myrddyn Dewey", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.h490_COLUMN, new TempHillClassification("490-499m with 30m drop", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.wainwright_COLUMN, new TempHillClassification("Wainwright", 6, "The 214 hills listed in volumes 1-7 of Wainwright''s A Pictorial Guide to the Lakeland Fells."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.wainwrightOutlyingFell_COLUMN, new TempHillClassification(
                "Wainwright Outlying Fells", 7, "Hills listed in The Outlying Fells of Lakeland."));
        HILL_TYPES_ARRAY.append(ColumnNumbers.birkett_COLUMN, new TempHillClassification("Birkett", 5, "Lake District hills over 1,000ft listed in Bill Birkett''s Complete Lakeland Fells."));
        HILL_TYPES_ARRAY
                .append(ColumnNumbers.countyTopHistoric_COLUMN, new TempHillClassification("County Top - Historic", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.countyTopCurrent_COLUMN, new TempHillClassification(
                "County Top � Current County and Unitary Authority", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.countyTopCurrentTwin_COLUMN, new TempHillClassification("Current County Twin Top", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.countyTopAdministrative_COLUMN, new TempHillClassification(
                "County Top � Administrative", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.countyTopLondonBorough_COLUMN, new TempHillClassification(
                "County Top � London Borough", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.countyTopLondonBoroughTwin_COLUMN, new TempHillClassification("London Borough Twin Top", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.subMarilyn_COLUMN, new TempHillClassification("Sub Marilyn", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.subHump_COLUMN, new TempHillClassification("Sub HuMP", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.subMurdo_COLUMN, new TempHillClassification("Sub Murdo", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.subCorbett_COLUMN, new TempHillClassification("Sub Corbett", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.subGrahamTop_COLUMN, new TempHillClassification("Sub Graham Top", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.subHewitt_COLUMN, new TempHillClassification("Sub Hewitt", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.subDewey_COLUMN, new TempHillClassification("Sub Dewey", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.subDonaldDewey_COLUMN, new TempHillClassification("Sub Donald Dewey", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.subHighland5_COLUMN, new TempHillClassification("Sub Highland Five", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.subMyrddynDewey_COLUMN, new TempHillClassification("Sub Myrddyn Dewey", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.subh490_COLUMN, new TempHillClassification("Sub Hills 490-499m", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.deletedMunroTop_COLUMN, new TempHillClassification("Deleted Munro Top", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.deletedCorbett_COLUMN, new TempHillClassification("Deleted Corbett", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.deletedNuttall_COLUMN, new TempHillClassification("Deleted Nuttall", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.deletedDonaldTop_COLUMN, new TempHillClassification("Deleted Donald Top", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.buxtonAndLewis_COLUMN, new TempHillClassification("Buxton and Lewis", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.bridge_COLUMN, new TempHillClassification("Bridge", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.trail100_COLUMN, new TempHillClassification("Trail 100", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.carns_COLUMN, new TempHillClassification("Carns", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.binnions_COLUMN, new TempHillClassification("Binnions", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.otherList_COLUMN, new TempHillClassification("Other", 0, ""));
        HILL_TYPES_ARRAY.append(ColumnNumbers.un_COLUMN, new TempHillClassification("Unclassified", 0, ""));

    }

    public static class TempHillClassification {

        public String name;
        public int importance;
        public String description;

        public TempHillClassification(String name,
                                      int importance, String description) {
            this.name = name;
            this.importance = importance;
            this.description = description;
        }
    }
}
