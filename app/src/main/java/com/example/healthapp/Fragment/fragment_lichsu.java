package com.example.healthapp.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthapp.DAO.SucKhoeDAO;
import com.example.healthapp.Database.Connection;
import com.example.healthapp.Model.SucKhoe;
import com.example.healthapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class fragment_lichsu extends Fragment {
    private PieChart pieChart;
    private LineChart lineChartBuocChan, lineChartThoiGian, lineChartKcal;
    private TextView setTime;
    private String selectedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lichsu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChart = view.findViewById(R.id.pieChart);
        lineChartBuocChan = view.findViewById(R.id.lineChartBuocChan);
        lineChartThoiGian = view.findViewById(R.id.lineChartThoiGian);
        lineChartKcal = view.findViewById(R.id.lineChartKcal);
        setTime = view.findViewById(R.id.setTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = dateFormat.format(new Date());
        setTime.setText(selectedDate);

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        setupPieChart();
        setupLineChart();
        setupLineChartThoiGian();
        setupLineChartKcal();
        loadPieChartData();
        loadLineChartData();
        loadLineChartThoiGianData();
        loadLineChartKcalData();
    }
    private void setupPieChart() {
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.parseColor("#558883"));
        pieChart.setTransparentCircleRadius(61f);

        Legend legend = pieChart.getLegend();
        // Thiết lập chú thích ở giữa dưới
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    }

    private void loadPieChartData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());

        ArrayList<PieEntry> entries = new ArrayList<>();

        Connection connection = Connection.getInstance(getContext());
        SucKhoeDAO db = connection.sucKhoeDAO();
        SucKhoe sucKhoe = db.getSucKhoeByDate(today);
        if (sucKhoe != null) {
            entries.add(new PieEntry(parseInteger(sucKhoe.getBuocChan())));
            entries.add(new PieEntry(parseInteger(sucKhoe.getTime())));
            entries.add(new PieEntry(parseInteger(sucKhoe.getKcal())));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        pieChart.setData(data);

        Legend legend = pieChart.getLegend();
        String[] labels = {"Bước", "Thời gian", "Kcal"};

        // Tạo danh sách mới để truyền vào setCustom()
        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < labels.length; i++) {
            legendEntries.add(new LegendEntry(labels[i], Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, ColorTemplate.COLORFUL_COLORS[i]));
        }
        legend.setCustom(legendEntries);

        pieChart.invalidate();  // Làm mới biểu đồ
    }

    private void setupLineChart() {
        lineChartBuocChan.getDescription().setEnabled(false);
        lineChartBuocChan.setTouchEnabled(true);
        lineChartBuocChan.setDragEnabled(true);
        lineChartBuocChan.setScaleEnabled(true);
        lineChartBuocChan.setPinchZoom(true);

        Legend legend = lineChartBuocChan.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    }

    private void loadLineChartData() {
        new AsyncTask<Void, Void, List<SucKhoe>>() {
            @Override
            protected List<SucKhoe> doInBackground(Void... voids) {
                Connection connection = Connection.getInstance(getContext());
                SucKhoeDAO db = connection.sucKhoeDAO();
                return db.getLast7BuocChan();
            }

            @Override
            protected void onPostExecute(List<SucKhoe> buocChanList) {
                List<Entry> entries = new ArrayList<>();
                List<String> dateList = new ArrayList<>();

                for (int i = 0; i < buocChanList.size(); i++) {
                    SucKhoe sucKhoe = buocChanList.get(i);
                    // Convert time to float if necessary
                    int BuocChanValue = Integer.parseInt(sucKhoe.getBuocChan()); // Ensure getTime() returns a valid time string
                    entries.add(new Entry(i, BuocChanValue));

                    // Format date as dd/MM
                    String[] dateParts = sucKhoe.getNgay().split("-");
                    String formattedDate = dateParts[2] + "/" + dateParts[1];
                    dateList.add(formattedDate);
                }

                LineDataSet dataSet = new LineDataSet(entries, "Bước Chân (bước)");
                dataSet.setColor(Color.parseColor("#FF910E0E"));
                dataSet.setLineWidth(1.5f);
                dataSet.setCircleColor(Color.parseColor("#FF910E0E"));
                dataSet.setCircleRadius(4f);
                dataSet.setDrawCircleHole(false);
                dataSet.setValueTextSize(10f);
                dataSet.setValueTextColor(Color.BLACK);

                LineData lineData = new LineData(dataSet);
                lineChartBuocChan.setData(lineData);

                // Customizing the x-axis labels with dates
                XAxis xAxis = lineChartBuocChan.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(dateList));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);

                // Disable right Y-axis
                YAxis rightYAxis = lineChartBuocChan.getAxisRight();
                rightYAxis.setEnabled(false);

                lineChartBuocChan.invalidate(); // Refresh the chart
            }
        }.execute();
    }

    private int parseInteger(String value) {
        // Chuyển đổi chuỗi thành số nguyên
        try {
            float floatValue = Float.parseFloat(value.replace(",", "."));
            return (int) floatValue;  // Lấy phần nguyên
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;  // Giá trị mặc định nếu có lỗi
        }
    }
    private void setupLineChartThoiGian() {
        lineChartThoiGian.getDescription().setEnabled(false);
        lineChartThoiGian.setTouchEnabled(true);
        lineChartThoiGian.setDragEnabled(true);
        lineChartThoiGian.setScaleEnabled(true);
        lineChartThoiGian.setPinchZoom(true);

        Legend legend = lineChartThoiGian.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    }

    private void loadLineChartThoiGianData() {
        new AsyncTask<Void, Void, List<SucKhoe>>() {
            @Override
            protected List<SucKhoe> doInBackground(Void... voids) {
                Connection connection = Connection.getInstance(getContext());
                SucKhoeDAO db = connection.sucKhoeDAO();
                return db.getLast7Kcal(); // Assuming this method returns a List<SucKhoe>
            }

            @Override
            protected void onPostExecute(List<SucKhoe> sucKhoeList) {
                List<Entry> entries = new ArrayList<>();
                List<String> dateList = new ArrayList<>();

                for (int i = 0; i < sucKhoeList.size(); i++) {
                    SucKhoe sucKhoe = sucKhoeList.get(i);
                    // Convert time to float if necessary
                    int timeValue = Integer.parseInt(sucKhoe.getTime()); // Ensure getTime() returns a valid time string
                    entries.add(new Entry(i, timeValue));

                    // Format date as dd/MM
                    String[] dateParts = sucKhoe.getNgay().split("-");
                    String formattedDate = dateParts[2] + "/" + dateParts[1];
                    dateList.add(formattedDate);
                }

                LineDataSet dataSet = new LineDataSet(entries, "Thời gian (giây)");
                dataSet.setColor(Color.parseColor("#FFFB3C00"));
                dataSet.setLineWidth(1.5f);
                dataSet.setCircleColor(Color.parseColor("#FFFB3C00"));
                dataSet.setCircleRadius(4f);
                dataSet.setDrawCircleHole(false);
                dataSet.setValueTextSize(10f);
                dataSet.setValueTextColor(Color.BLACK);

                LineData lineData = new LineData(dataSet);
                lineChartThoiGian.setData(lineData); // Ensure this is the correct chart

                // Customizing the x-axis labels with dates
                XAxis xAxis = lineChartThoiGian.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(dateList));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);

                // Disable right Y-axis
                YAxis rightYAxis = lineChartThoiGian.getAxisRight();
                rightYAxis.setEnabled(false);

                lineChartThoiGian.invalidate(); // Refresh the chart
            }
        }.execute();
    }


    private void setupLineChartKcal() {
        lineChartKcal.getDescription().setEnabled(false);
        lineChartKcal.setTouchEnabled(true);
        lineChartKcal.setDragEnabled(true);
        lineChartKcal.setScaleEnabled(true);
        lineChartKcal.setPinchZoom(true);

        Legend legend = lineChartKcal.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    }

    private void loadLineChartKcalData() {
        new AsyncTask<Void, Void, List<SucKhoe>>() {
            @Override
            protected List<SucKhoe> doInBackground(Void... voids) {
                Connection connection = Connection.getInstance(getContext());
                SucKhoeDAO db = connection.sucKhoeDAO();
                return db.getLast7Kcal();
            }

            @Override
            protected void onPostExecute(List<SucKhoe> sucKhoeList) {
                List<Entry> entries = new ArrayList<>();
                List<String> dateList = new ArrayList<>();

                for (int i = 0; i < sucKhoeList.size(); i++) {
                    SucKhoe sucKhoe = sucKhoeList.get(i);
                    // Convert kcal to float if necessary
                    int kcalValue = (int) Float.parseFloat(sucKhoe.getKcal());
                    entries.add(new Entry(i, kcalValue));
                    String[] d = sucKhoe.getNgay().split("-");
                    String date = d[2]+"/"+d[1];
                    dateList.add(date);
                }

                LineDataSet dataSet = new LineDataSet(entries, "Kcal");
                dataSet.setColor(Color.parseColor("#FFFFC107"));
                dataSet.setLineWidth(1.5f);
                dataSet.setCircleColor(Color.parseColor("#FFFFC107"));
                dataSet.setCircleRadius(4f);
                dataSet.setDrawCircleHole(false);
                dataSet.setValueTextSize(10f);
                dataSet.setValueTextColor(Color.BLACK);

                LineData lineData = new LineData(dataSet);
                lineChartKcal.setData(lineData);

                // Customizing the x-axis labels with dates
                XAxis xAxis = lineChartKcal.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(dateList));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);

                YAxis rightYAxis = lineChartKcal.getAxisRight();
                rightYAxis.setEnabled(false);

                lineChartKcal.invalidate(); // Refresh the chart
            }
        }.execute();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate = dateFormat.format(calendar.getTime());
                setTime.setText(selectedDate);

                ArrayList<PieEntry> entries = new ArrayList<>();

                Connection connection = Connection.getInstance(getContext());
                SucKhoeDAO db = connection.sucKhoeDAO();
                SucKhoe sucKhoe = db.getSucKhoeByDate(setTime.getText().toString());
                if (sucKhoe != null) {
                    entries.add(new PieEntry(parseInteger(sucKhoe.getBuocChan())));
                    entries.add(new PieEntry(parseInteger(sucKhoe.getTime())));
                    entries.add(new PieEntry(parseInteger(sucKhoe.getKcal())));
                }

                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                PieData data = new PieData(dataSet);
                data.setValueTextSize(10f);
                data.setValueTextColor(Color.YELLOW);
                pieChart.setData(data);

                Legend legend = pieChart.getLegend();
                String[] labels = {"Bước", "Thời gian", "Kcal"};

                // Tạo danh sách mới để truyền vào setCustom()
                ArrayList<LegendEntry> legendEntries = new ArrayList<>();
                for (int i = 0; i < labels.length; i++) {
                    legendEntries.add(new LegendEntry(labels[i], Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, ColorTemplate.COLORFUL_COLORS[i]));
                }
                legend.setCustom(legendEntries);

                pieChart.invalidate();  // Làm mới biểu đồ

            }
        }, year, month, day);
        datePickerDialog.show();
    }
    @Override
    public void onResume() {
        super.onResume();
        loadPieChartData();
        loadLineChartData();
        loadLineChartThoiGianData();
        loadLineChartKcalData();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = dateFormat.format(calendar.getTime());
        setTime.setText(selectedDate);
    }
}
