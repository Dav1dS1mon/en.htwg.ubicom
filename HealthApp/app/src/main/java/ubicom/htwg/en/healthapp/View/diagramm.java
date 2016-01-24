package ubicom.htwg.en.healthapp.View;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

import ubicom.htwg.en.healthapp.Controller.BioHarnessBT.MainActivity;
import ubicom.htwg.en.healthapp.Controller.Recorder;
import ubicom.htwg.en.healthapp.Model.menu;
import ubicom.htwg.en.healthapp.R;

public class diagramm extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagramm);

        long diffTime = menu.getDiffTime() / 1000;
        List<Location> coordinates = Recorder.coordinates;
        List<Bundle> data = MainActivity.data;
        GraphicalView mChart;

        int avg_route = data.size() / coordinates.size();
        int avg_time = data.size() / (int) diffTime;

        Integer[] heartrate = new Integer[(int) diffTime];
        Float[] breathrate = new Float[(int) diffTime];

        int counter = 0;

        for (int i = 0; i < diffTime; i = i + avg_time) {
            try {
                heartrate[counter] = (Integer.parseInt(data.get(i).getString("HeartRate")));
            } catch (Exception e) {
                heartrate[counter] = (Integer.parseInt(data.get(i + 1).getString("HeartRate")));
            }

            try {
                breathrate[counter] = (Float.parseFloat(data.get(i).getString("RespirationRate")));
            } catch (Exception e) {
                breathrate[counter] = (Float.parseFloat(data.get(i + 1).getString("RespirationRate")));
            }

            counter++;
        }

        Integer[] time = new Integer[(int) diffTime];

        for (int i = 0; i < diffTime; i++) {
            time[i] = i;
        }


        XYSeries series_heart = new XYSeries("Heartrate");
        XYSeries series_breath = new XYSeries("Breathrate");
        for (int i = 0; i < avg_time; i++) {
            series_heart.add(time[i], heartrate[i]);
            series_breath.add(time[i], Integer.parseInt(breathrate[i].toString().substring(breathrate[i].toString().lastIndexOf(".") + 1)));
        }
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series_heart);
        dataset.addSeries(series_breath);
        XYSeriesRenderer renderer_heart = new XYSeriesRenderer();
        XYSeriesRenderer renderer_breath = new XYSeriesRenderer();

        renderer_heart.setColor(Color.RED);
        renderer_heart.setPointStyle(PointStyle.CIRCLE);
        renderer_heart.setFillPoints(true);
        renderer_heart.setLineWidth(10);
        renderer_heart.setChartValuesTextSize(30);
        renderer_heart.setDisplayChartValues(true);
        renderer_breath.setColor(Color.GREEN);
        renderer_breath.setPointStyle(PointStyle.CIRCLE);
        renderer_breath.setFillPoints(true);
        renderer_breath.setLineWidth(10);
        renderer_breath.setChartValuesTextSize(30);
        renderer_breath.setDisplayChartValues(true);

        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setYLabels(0);
        multiRenderer.setYAxisMax(200.00);
        multiRenderer.setYAxisMin(0.0);
        multiRenderer.setChartTitle("Analysis Training");
        multiRenderer.setXTitle("Time of Training");
        multiRenderer.setYTitle("Heartrate/Breathrate");
        multiRenderer.setAxisTitleTextSize(32);
        multiRenderer.setLabelsTextSize(16);
        multiRenderer.setZoomButtonsVisible(true);
        for (int i = 0; i < avg_time; i++) {
            multiRenderer.addXTextLabel(i, time[i].toString());
        }
        multiRenderer.addSeriesRenderer(renderer_heart);
        multiRenderer.addSeriesRenderer(renderer_breath);

        mChart = ChartFactory.getCubeLineChartView(this, dataset, multiRenderer, 0.3f);
        //mChart.setBackgroundColor(Color.BLACK);
        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        layout.addView(mChart);
    }
}
