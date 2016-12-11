import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple DTO for the address book example.
 *
 * Serializable and cloneable Java Object that are typically persisted
 * in the database and can also be easily converted to different formats like JSON.
 */
// Backend DTO class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.
public class Change implements Serializable, Cloneable {



    public String getChangeId() {
        return ChangeId;
    }

    public String getDatetime() {
        return Datetime;
    }

    public String getrQ() {
        return rQ;
    }

    public String getSubject() {
        return Subject;
    }

    public String getUserId() {
        return UserId;
    }

    public Long getId() {
        return id;
    }

    public void setDatetime(String time) {


        this.Datetime=time;
    }

    public void setrQ(String rQ) {
        this.rQ = rQ;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setChangeId(String changId) {
        ChangeId = changId;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getrQiD() {
        return rQiD;
    }

    public void setrQiD(String rQiD) {
        this.rQiD = rQiD;
    }

    public String getLinkToRq() {
        return LinkToRq;
    }

    public void setLinkToRq(String linkToRq) {
        LinkToRq = linkToRq;
    }

    public String getTest_Type() {
        return Test_Type;
    }

    public void setTest_Type(String test_Type) {
        Test_Type = test_Type;
    }


    private Long id;
    private String ChangeId = "";
    private String UserId = "";
    private String Subject = "";
    private String rQ = "";
    private String Datetime="";
    private String rQiD="";
    private String LinkToRq="";

    private String Test_Type="";
    private ArrayList afFileList =new ArrayList<String>();

    public ArrayList getAfFileList() {
        return afFileList;
    }

    public void setAfFileList(ArrayList afFileList) {
        this.afFileList = afFileList;
    }






    @Override
    public Change clone() throws CloneNotSupportedException {
        try {
            return (Change) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString(){
     return "Change " + getChangeId() + " by " + getUserId() + " on " + getDatetime() + "\n" +
                        "\n" +
                        "\t" + getSubject() + "\t"+ getTest_Type()+"\n";

    }


    public String getDetails(){

        int i =0;

        String result  = new String("");
        try {

            if (getrQ().equals("true")) {
                result.concat("Change " + getChangeId() + " by " + getUserId() + " on " + getDatetime() + "\n" +
                        "\n" +
                        "\t" + getSubject() + "\n");
                result.concat(" Change " + getChangeId() + " by " + getUserId() + " on " + getDatetime() + "\n" +
                        "\n" +
                        "\t" + getSubject() + "\n" +
                        "\t(auto-submit " + getrQiD() + " after successfully running remote remote.all)\n" +
                        "\t" + getLinkToRq());

            } else
                result.concat("Change " + getChangeId() + " by " + getUserId() + " on " + getDatetime() + "\n" +
                        "\n" +
                        "\t" + getSubject() + "\n");

            i=1;
        }catch (Exception e){


        }
        finally {

            if(i==1)
            return result;
            else
                return "Change " + getChangeId() + " by " + getUserId() + " on " + getDatetime() + "\n" +
                        "\n" +
                        "\t" + getSubject() + "\n";

        }

    }


}
