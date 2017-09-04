package neon.desafio.banktransfer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.concretesolutions.canarinho.formatador.Formatador;
import de.hdodenhof.circleimageview.CircleImageView;
import neon.desafio.banktransfer.R;
import neon.desafio.banktransfer.adapter.SendMoneyAdapter;
import neon.desafio.banktransfer.event.ErrorEvent;
import neon.desafio.banktransfer.event.TransferMoneySuccessEvent;
import neon.desafio.banktransfer.model.UserVO;
import neon.desafio.banktransfer.repository.TransferRepository;
import neon.desafio.banktransfer.repository.UserRepository;
import neon.desafio.banktransfer.utils.AnimationUtil;
import neon.desafio.banktransfer.utils.CurrencyWatcher;
import neon.desafio.banktransfer.utils.DialogsUtils;
import neon.desafio.banktransfer.utils.MoneyUtils;

import static neon.desafio.banktransfer.event.ErrorEvent.Type.UNAUTHORIZED;

@EActivity
public class SendMoneyActivity extends BaseActivity {

    @ViewById(R.id.send_money_recycler_view)
    RecyclerView sendMoneyRecyclerView;
    @ViewById(R.id.send_money_toolbar_title)
    AppCompatTextView toolbarTitle;
    @ViewById(R.id.send_money_toolbar)
    Toolbar toolbar;

    @Bean
    UserRepository userRepository;
    @Bean
    SendMoneyAdapter adapter;
    @Bean
    TransferRepository transferRepository;

    AlertDialog alertDialog;
    EditText valueEditText;
    LinearLayoutCompat progressBar;
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsersData();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(null);
            toolbarTitle.setText(getString(R.string.send_money));
        }
    }

    @OptionsItem(android.R.id.home)
    void clickBack() {
        finish();
    }

    private void loadUsersData() {
        adapter.setUserVOList(userRepository.getList());

        adapter.setListener(new SendMoneyAdapter.Listener() {
            @Override
            public void onItemClick(UserVO user) {
                userClick(user);
            }
        });

        sendMoneyRecyclerView.setAdapter(adapter);
        sendMoneyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void userClick(final UserVO user) {
        alertDialog = DialogsUtils.newDialog(this, R.layout.send_money_dialog_layout, R.drawable.shape_rounded_alert);
        if (null == alertDialog.getWindow()) return;
        alertDialog.setCancelable(false);

        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        TextView nameTextView = alertDialog.findViewById(R.id.alert_name);
        TextView phoneTextView = alertDialog.findViewById(R.id.alert_phone);
        CircleImageView photoCircle = alertDialog.findViewById(R.id.alert_profile_image);
        valueEditText = alertDialog.findViewById(R.id.alert_value_edit);
        Button sendButton = alertDialog.findViewById(R.id.alert_send_button);
        progressBar = alertDialog.findViewById(R.id.progress_root);
        ImageView closeImage = alertDialog.findViewById(R.id.alert_close_button);
        errorTextView = alertDialog.findViewById(R.id.error_text_view);
        addCurrencyListener(valueEditText);

        nameTextView.setText(user.getUserName());
        phoneTextView.setText(user.getUserPhone());
        Picasso.with(this)
                .load(user.getUserImageUrl())
                .error(R.drawable.profile_image)
                .into(photoCircle);

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(valueEditText.getWindowToken(), 0);
                double value = MoneyUtils.getUnformattedValue(valueEditText.getText().toString());
                if(value == 0.0){
                    setErrorOnField(getString(R.string.alert_invalid_value_error));
                    return;
                }
                errorTextView.setVisibility(View.GONE);
                sendMoneyToApi(user.getId(), value);
            }
        });
    }

    private void setErrorOnField(String error) {
        if(valueEditText != null && errorTextView != null) {
            errorTextView.setText(error);
            AnimationUtil.shakeView(valueEditText);
            AnimationUtil.shakeView(errorTextView);
            errorTextView.setVisibility(View.VISIBLE);
        }
    }

    private void addCurrencyListener(EditText editText) {
        editText.addTextChangedListener(new CurrencyWatcher(editText, true, true));
    }

    private void sendMoneyToApi(String userId, double value) {
        showProgress();
        transferRepository.sentMoney(userId, value);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTransferSuccess(TransferMoneySuccessEvent transferMoneySuccessEvent) {
        if(transferMoneySuccessEvent.isSuccess()) {
            hideProgress();
            alertDialog.dismiss();
            DialogsUtils.newDialog(this, null, getString(R.string.transfer_success), "Ok");
        }
    }

    private void showProgress() {
        if(progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        if(progressBar != null)
            progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void handleErrorAndShouldContinueToDefault(ErrorEvent errorEvent) {
        hideProgress();

        switch(errorEvent.getErrorType()) {
            case UNAUTHORIZED:
                setErrorOnField(getString(R.string.alert_unauthorized_error));
                break;
            case NETWORK_ERROR:
                if(alertDialog != null)
                    alertDialog.dismiss();
                DialogsUtils.newDialog(this, getString(R.string.not_made_transfer), getString(R.string.connection_error), "Ok");
                break;
            default:
                if(alertDialog != null)
                    alertDialog.dismiss();
                DialogsUtils.newDialog(this, getString(R.string.not_made_transfer), getString(R.string.alert_generic_error), "Ok");
        }
    }
}
