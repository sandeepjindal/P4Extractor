import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.vaadin.addon.JFreeChartWrapper;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by sandeep on 25/8/15.
 */

public class PieChaRt  {


    DefaultPieDataset defaultPieDataset = new DefaultPieDataset();

    public void setDataset(ArrayList<Change> list)
    {
        int rq=0;
        int noRq =0;

      for(Change change: list){
          try {
              if (change.getrQ().equals("true"))
                  rq++;
              else
                  noRq++;
          }
          catch (Exception e){}
      }




        defaultPieDataset.setValue("RQ",new Double(rq));
        defaultPieDataset.setValue("NoRQ",new Double(noRq));

    }



    public JFreeChartWrapper getJFreeChartWrapper(){


        JFreeChart chart = ChartFactory.createPieChart(
                " ",  // chart title
                defaultPieDataset,             // data
                true,               // include legend
                true,
                false
        );
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("RQ", Color.blue);
        plot.setSectionPaint("NoRQ", Color.red);
        plot.setExplodePercent("RQ", 0.05);
        plot.setSimpleLabels(true);
        plot.setBackgroundPaint(Color.white);
        plot.setOutlineVisible(false);
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
        plot.setLabelGenerator(gen);

        JFreeChartWrapper jf = new JFreeChartWrapper(chart) {
            @Override
            public void attach() {
                super.attach();
                setResource("src", getSource());
            }
        };
        return jf;
    }
}
