package neon.desafio.banktransfer.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;
import neon.desafio.banktransfer.R;
import neon.desafio.banktransfer.controller.SessionController;
import neon.desafio.banktransfer.event.ErrorEvent;
import neon.desafio.banktransfer.repository.TokenRepository;

@EActivity
public class MainActivity extends BaseActivity {

    @ViewById(R.id.activity_main_root)
    ConstraintLayout rootLayout;
    @ViewById(R.id.main_profile_name)
    TextView profileName;
    @ViewById(R.id.main_profile_mail)
    TextView profileEmail;
    @ViewById(R.id.main_profile_image)
    CircleImageView profileImageView;

    @Bean
    SessionController sessionController;
    @Bean
    TokenRepository tokenRepository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
        getToken();
    }

    private void getToken() {
        if(sessionController.getSessionToken() == null)
            tokenRepository.generateToken(sessionController.getLoggedUser().getUserName(),
                    sessionController.getLoggedUser().getUserEmail());
    }

    private void loadUserData() {
        profileName.setText(sessionController.getLoggedUser().getUserName());
        profileEmail.setText(sessionController.getLoggedUser().getUserEmail());
        Picasso.with(this).load(sessionController.getLoggedUser().getUserImageUrl())
                .error(R.drawable.profile_image).into(profileImageView);
    }

    @Click(R.id.main_send_money_button)
    public void sendMoneyClick() {
        SendMoneyActivity_.intent(this).start();
    }

    @Click(R.id.main_history_button)
    public void viewHistory() {
        HistoryActivity_.intent(this).start();
    }

    @Override
    protected void handleErrorAndShouldContinueToDefault(ErrorEvent errorEvent) {
        switch (errorEvent.getErrorType()){
            case NETWORK_ERROR:
                createErrorSnackBar(getString(R.string.connection_error),
                        getString(R.string.retry_button));
                break;
            default:
                createErrorSnackBar(getString(R.string.generic_token_error),
                        getString(R.string.retry_button));
                break;
        }
    }

    private void createErrorSnackBar(String error, String button) {
        Snackbar snackbar = Snackbar
                .make(rootLayout, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(button, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getToken();
                    }
                });
        snackbar.show();
    }
}
