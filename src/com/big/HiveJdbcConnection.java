import java.sql.*;

/**
 * Created by sandeep on 18/8/15.
 */
public abstract class HiveJdbcConnection {



    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
    public static Statement stmt;
    public static String tableName = "p4changes";

    public static void createConnection(){


        try {
                Class.forName(driverName);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.exit(1);
            }


        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:hive2://localhost:10000/p4", "sandeep", "");
            stmt = con.createStatement();
            stmt.execute("set mapred.tasktracker.map.tasks.maximum=8");
            stmt.execute("set mapred.map.child.java.opts=-Xmx1024m");
            stmt.execute("set mapred.tasktracker.reduce.tasks.maximum=6");
            stmt.execute("set mapred.reduce.child.java.opts=-Xmx1024m");
        }
        catch (SQLException dd){

        }
        try {

            stmt.executeQuery("drop table " + tableName);
        }catch (SQLException ex){
            System.out.println("table p4change doesn't exist\n");


        }
        try {

            String sql = "CREATE EXTERNAL TABLE "+tableName+" ( changeId BIGINT, userId STRING, time timestamp, subject STRING, test_type String ,rQ boolean, rQiD STRING, LinkToRq STRING, changeList ARRAY<STRING>) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\t' COLLECTION ITEMS TERMINATED BY '|' MAP KEYS TERMINATED BY ':' STORED AS TEXTFILE location '/tmp/hive/new/'";
            System.out.println(sql);
            ResultSet res = stmt.executeQuery(sql);
        }catch (SQLException e){

            System.out.println("table cannot be created\n");
        }


    }
}
