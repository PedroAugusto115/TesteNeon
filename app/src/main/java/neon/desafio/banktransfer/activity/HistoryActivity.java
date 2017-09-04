package neon.desafio.banktransfer.activity;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;

import neon.desafio.banktransfer.R;
import neon.desafio.banktransfer.adapter.HistoryAdapter;
import neon.desafio.banktransfer.event.ErrorEvent;
import neon.desafio.banktransfer.event.TransferHistorySuccess;
import neon.desafio.banktransfer.model.TransferTotalChart;
import neon.desafio.banktransfer.model.TransferVO;
import neon.desafio.banktransfer.model.UserVO;
import neon.desafio.banktransfer.repository.TransferRepository;
import neon.desafio.banktransfer.repository.UserRepository;
import neon.desafio.banktransfer.utils.DialogsUtils;
import neon.desafio.banktransfer.utils.MoneyUtils;

import static android.view.View.GONE;

@EActivity
public class HistoryActivity extends BaseActivity {

    @ViewById(R.id.history_recycler_view)
    RecyclerView historyRecyclerView;
    @ViewById(R.id.history_toolbar_title)
    AppCompatTextView toolbarTitle;
    @ViewById(R.id.history_toolbar)
    Toolbar toolbar;
    @ViewById(R.id.history_empty)
    RelativeLayout emptyLayout;
    @ViewById(R.id.transfers_line_chart)
    BarChart transferChart;

    @Bean
    TransferRepository transferRepository;
    @Bean
    UserRepository userRepository;
    @Bean
    HistoryAdapter adapter;

    @InstanceState
    ArrayList<TransferVO> transferVOs;

    @InstanceState
    ArrayList<TransferTotalChart> totalByUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (transferVOs == null || transferVOs.size() == 0){
            getTransfersHistory();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(null);
            toolbarTitle.setText(getString(R.string.main_history));
        }
    }

    @OptionsItem(android.R.id.home)
    void clickBack() {
        finish();
    }

    private void getTransfersHistory() {
        transferRepository.getTransfersHistory();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTransferHistorySuccess(TransferHistorySuccess transferHistorySuccess) {
        transferVOs = new ArrayList<>(transferHistorySuccess.getTransferVOs());
        Collections.sort(transferVOs);

        if(transferVOs.size() == 0) {
            showEmptyState();
            return;
        }

        getTransferUsers();
        prepareChartArray();
        loadRecyclerView();
        loadChart();
    }

    private void prepareChartArray() {
        totalByUser = new ArrayList<>();
        for (UserVO user : userRepository.getList()) {
            TransferTotalChart transferTotal = new TransferTotalChart(user);
            for (TransferVO transfer : transferVOs) {
                if(transfer.getUserId().equals(user.getId()))
                    transferTotal.addValue(transfer.getValue());
            }
            if(transferTotal.getTotalTransfered() > 0.0)
                totalByUser.add(transferTotal);
        }
        Collections.sort(totalByUser);
    }

    private void getTransferUsers() {
        for (TransferVO transfer : transferVOs) {
            UserVO user = userRepository.getUserById(transfer.getUserId());
            transfer.setUserVO(user);
        }
    }

    private void loadRecyclerView() {
        adapter.setTransferVOList(transferVOs);
        historyRecyclerView.setAdapter(adapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadChart() {
        if(totalByUser.size() == 0){
            transferChart.setVisibility(GONE);
            return;
        }

        setupChart();

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < totalByUser.size(); i++) {
            yVals1.add(new BarEntry(i, (float) totalByUser.get(i).getTotalTransfered()));
        }

        BarDataSet set1;
        if (transferChart.getData() != null &&
                transferChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)transferChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            transferChart.getData().notifyDataChanged();
            transferChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setBarWidth(0.5f);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                data.setValueTextColor(getColor(R.color.colorBlueGreen));
            }
            data.setValueTextSize(9f);
            transferChart.setData(data);
            transferChart.setFitBars(true);
        }

        transferChart.getBarData().setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return MoneyUtils.formatCurrency(String.valueOf(value));
            }
        });

        transferChart.invalidate();
    }

    private void setupChart() {
        transferChart.setMaxVisibleValueCount(20);
        transferChart.setPinchZoom(false);
        transferChart.getLegend().setEnabled(false);
        transferChart.getDescription().setEnabled(false);
        transferChart.setDrawBarShadow(false);
        transferChart.setDrawGridBackground(false);
        transferChart.getAxisRight().setEnabled(false);

        XAxis xAxis = transferChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            xAxis.setTextColor(getColor(R.color.colorBlueGreen));
            transferChart.getAxisLeft().setTextColor(getColor(R.color.colorBlueGreen));
        }
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return totalByUser.get((int)value).getUserVO().getUserName();
            }
        });
        transferChart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return MoneyUtils.formatLabeledCurrency(String.valueOf(value));
            }
        });
    }

    private void showEmptyState() {
        historyRecyclerView.setVisibility(GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void handleErrorAndShouldContinueToDefault(ErrorEvent errorEvent) {
        switch (errorEvent.getErrorType()){
            case NOT_FOUND:
                showEmptyState();
                break;
            case UNAUTHORIZED:
                showEmptyState();
                DialogsUtils.newDialog(this, getString(R.string.history_error_title), getString(R.string.history_unauthorized), "Ok");
                break;
            case SERVER_ERROR:
                showEmptyState();
                DialogsUtils.newDialog(this, getString(R.string.history_error_title), getString(R.string.history_server_error), "Ok");
                break;
            default:
                showEmptyState();
                DialogsUtils.newDialog(this, getString(R.string.history_error_title), getString(R.string.history_generic_error), "Ok");
                break;
        }
    }
}
