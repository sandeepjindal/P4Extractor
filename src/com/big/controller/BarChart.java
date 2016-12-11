import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.vaadin.addon.JFreeChartWrapper;

import java.text.SimpleDateFormat;
import java.util.*;

public class BarChaRt
{

    final String NO_RQ = "NO_RQ";
    final String TOTAL = "TOTAL";
    final String RQ = "RQ";
    public enum Month {
        JAN(1),FEB(2),MAR(3),APR(4),MAY(5),JUN(6),JUL(7),AUG(8),SEP(9),OCT(10),NOV(11),DEC(12);


        private static final Map<Integer,Month> lookup
                = new HashMap<Integer,Month>();

        static {
            for(Month w : EnumSet.allOf(Month.class))
                lookup.put(w.getCode(), w);
        }

        private int code;

        private Month(int code) {
            this.code = code;
        }

        public int getCode() { return code; }

        public static Month get(int code) {
            return lookup.get(code);
        }
    }



    DefaultCategoryDataset dataset=new DefaultCategoryDataset();

    public void setDataset(ArrayList<Change> list)
    {
        int StartingMonth=1;
        int flag=0;
        int total=5;
        Collections.sort(list, new Comparator<Change>() {

            @Override
            public int compare(Change o1, Change o2) {


                    return (int) (o2.getDatetime().compareTo(o1.getDatetime()));
            }


        });
        int rq=0;
        int noRq=0;

        for(Change change :list){

            SimpleDateFormat format =   new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            Date date;
            try {
                date = format.parse(change.getDatetime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int month = cal.get(Calendar.MONTH)+1;
                if(flag==0)
                {

                    StartingMonth=month;
                    flag=1;
                }
                if( month==StartingMonth ) {
                    try {
                        if (change.getrQ().equals("true"))
                            rq++;
                        else
                            noRq++;
                    } catch (Exception e) {
                    }
                }
                else{

                    dataset.addValue( noRq , NO_RQ ,Month.get(StartingMonth));
                    dataset.addValue( rq , RQ ,Month.get(StartingMonth));
                    dataset.addValue(rq+noRq,TOTAL,Month.get(StartingMonth));
                    rq=0;
                    noRq=0;
                    StartingMonth--;
                    if(StartingMonth < 1)
                    {
                        StartingMonth=12;

                    }
                    try {
                        if (change.getrQ().equals("true"))
                            rq++;
                        else
                            noRq++;
                    } catch (Exception e) {
                    }
                    total--;
                    if(total  <1)
                        break;

                }

            }catch (Exception e){}




        }


        if(total !=0)
        {

            dataset.addValue( rq , NO_RQ ,Month.get(StartingMonth));
            dataset.addValue( noRq , RQ ,Month.get(StartingMonth));
            dataset.addValue(rq+noRq,TOTAL,Month.get(StartingMonth));
        }


    }




    public JFreeChartWrapper getJFreeChartWrapper() {
        JFreeChart barChart = ChartFactory.createBarChart3D(
                "Performance Statistics",
                "month",
                "Number of commit",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

              JFreeChartWrapper jf = new JFreeChartWrapper(barChart) {
                  @Override
                    public void attach() {
                          super.attach();
                           setResource("src", getSource());
                }
        };
        return jf;
    }
}
