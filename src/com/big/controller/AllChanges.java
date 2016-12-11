import org.apache.commons.beanutils.BeanUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//import backend.Change;
//import backend.HiveJdbcConnection;

/** Separate Java service class.
 * Backend implementation for the address book application, with "detached entities"
 * simulating real world DAO. Typically these something that the Java EE
 * or Spring backend services provide.
 */
// Backend service class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.
public class AllChanges {

    private static AllChanges instance;

    private static Statement stmt= com.backend.HiveJdbcConnection.stmt;

    private static String tableName = HiveJdbcConnection.tableName;

    private HashMap<Long, Change> changeList = new HashMap<>();
    private long nextId = 0;

    public synchronized List<Change> findAll(String stringFilter) {
        ArrayList arrayList = new ArrayList();

        String gridFilters = stringFilter;

            if (gridFilters.contains(","))
            {

                String[] elephantList =gridFilters.split(",");
                for (String  string :elephantList )
                {

                    for (Change change : changeList.values()) {
                        try {
                            boolean passesFilter = (string == null || string.isEmpty())
                                    || change.toString().toLowerCase()
                                    .contains(string.toLowerCase());
                            if (passesFilter) {
                                arrayList.add(change.clone());
                            }
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(AllChanges.class.getName()).log(
                                    Level.SEVERE, null, ex);
                        }
                    }
                    Collections.sort(arrayList, new Comparator<Change>() {

                        @Override
                        public int compare(Change o1, Change o2) {

                            return (int) (o1.getId()-o2.getId());
                        }
                    });
                }
            }else
            {


                for (Change change : changeList.values()) {
                    try {
                        boolean passesFilter = (gridFilters == null || gridFilters.isEmpty())
                                || change.toString().toLowerCase()
                                .contains(gridFilters.toLowerCase());
                        if (passesFilter) {
                            arrayList.add(change.clone());
                        }
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(AllChanges.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
            }

        }



        return arrayList;
    }

    public synchronized long count() {
        return changeList.size();
    }

    public synchronized void delete(Change value) {
        changeList.remove(value.getChangeId());
    }

    public synchronized void save(Change entry) {
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (Change) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


        changeList.put(entry.getId(), entry);
    }

    public static AllChanges getDatabetween(Date from, Date to) {
        try {


            Calendar cal = Calendar.getInstance();
            cal.setTime(from);
            cal.set(Calendar.MILLISECOND, 0);
            java.sql.Timestamp fromDate=new java.sql.Timestamp(from.getTime());
            java.sql.Timestamp toDate=new java.sql.Timestamp(to.getTime());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateWithoutTime = sdf.format(from);
            String dateWithoutTime1 = sdf.format(to);



            String   sql = "select changeId, userId, time, subject,test_type, rQ, rQiD, LinkToRq  FROM " + tableName + " Where time BETWEEN '"+dateWithoutTime+"' And "+"'"+dateWithoutTime1+"'";
            System.out.println("Running: " + sql);
            ResultSet res= stmt.executeQuery(sql);


            final AllChanges allChanges = new AllChanges();

            Random r = new Random(0);


            while (res.next()) {

                Change change = new Change();
                change.setChangeId(res.getString("changeid"));
                change.setUserId(res.getString(2));
                change.setSubject(res.getString(4));
                change.setTest_Type(res.getString(5));
                change.setrQ(res.getString(6));
                change.setDatetime(res.getString(3));
                change.setrQiD(res.getString(7));
                change.setLinkToRq(res.getString(8));
                ArrayList<String> list = new ArrayList<>();
                try {
                    //for (int i = 0; i >= 0; i++) {
                  //      String sql1 = "SELECT changeId, changeList[" + i + "] as af FROM " + tableName + " WHERE changeId=" + change.getChangeId() + "";
                //        ResultSet res1 = stmt.executeQuery(sql1);
              //          String near =res1.getString(1);
            //            list.add(near);
              //      }
                }catch (Exception e){

                }
                allChanges.save(change);
            }
            instance = allChanges;

        } catch (SQLException e) {
            e.printStackTrace();
        }




        return instance;

    }

    public static AllChanges getModifiedData(Date fromDateValue, Date toDateValue, String userNameValue) {
        try {


            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDateValue);
            cal.set(Calendar.MILLISECOND, 0);
            java.sql.Timestamp fromDate=new java.sql.Timestamp(fromDateValue.getTime());
            java.sql.Timestamp toDate=new java.sql.Timestamp(toDateValue.getTime());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateWithoutTime = sdf.format(fromDate);
            String dateWithoutTime1 = sdf.format(toDate);



            String   sql = "select changeId, userId, time, subject, test_type, rQ, rQiD, LinkToRq  FROM " + tableName + " Where time BETWEEN '"+dateWithoutTime+"' And "+"'"+dateWithoutTime1+"' And userid LIKE '"+userNameValue+"%'";
            System.out.println("Running: " + sql);
            ResultSet res= stmt.executeQuery(sql);


            final AllChanges allChanges = new AllChanges();

            Random r = new Random(0);


            while (res.next()) {

                Change change = new Change();
                change.setChangeId(res.getString("changeid"));
                change.setUserId(res.getString(2));
                change.setSubject(res.getString(4));
                change.setTest_Type(res.getString(5));
                change.setrQ(res.getString(6));
                change.setDatetime(res.getString(3));
                change.setrQiD(res.getString(7));
                change.setLinkToRq(res.getString(8));
                ArrayList<String> list = new ArrayList<>();
                try {
                    //for (int i = 0; i >= 0; i++) {
                    //      String sql1 = "SELECT changeId, changeList[" + i + "] as af FROM " + tableName + " WHERE changeId=" + change.getChangeId() + "";
                    //        ResultSet res1 = stmt.executeQuery(sql1);
                    //          String near =res1.getString(1);
                    //            list.add(near);
                    //      }
                }catch (Exception e){

                }
                allChanges.save(change);
            }
            instance = allChanges;

        } catch (SQLException e) {
            e.printStackTrace();
        }




        return instance;
    }
}
