package neon.desafio.banktransfer.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import neon.desafio.banktransfer.R;
import neon.desafio.banktransfer.model.UserVO;

@EBean
public class SendMoneyAdapter extends RecyclerView.Adapter<SendMoneyAdapter.ViewHolder> {

    private Listener listener;
    @RootContext
    Context mContext;
    private List<UserVO> userVOList;

    SendMoneyAdapter(Context context) {
        this.mContext = context;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onItemClick(UserVO user);
    }

    public void setUserVOList(List<UserVO> userVOList) {
        this.userVOList = userVOList;
    }

    @Override
    public int getItemCount() {
        return userVOList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View lineView = inflater.inflate(R.layout.view_send_money_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(lineView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserVO user = userVOList.get(position);

        TextView userName = holder.userName;
        TextView userPhone = holder.userPhone;
        CircleImageView userImage = holder.userImage;
        ConstraintLayout itemRoot = holder.itemRoot;

        userName.setText(user.getUserName());
        userPhone.setText(user.getUserPhone());
        Picasso.with(mContext)
                .load(user.getUserImageUrl())
                .error(R.drawable.profile_image)
                .into(userImage);

        itemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onItemClick(user);
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userName;
        public TextView userPhone;
        public CircleImageView userImage;
        public ConstraintLayout itemRoot;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.list_item_name);
            userPhone = itemView.findViewById(R.id.list_item_phone);
            userImage = itemView.findViewById(R.id.list_item_image);
            itemRoot = itemView.findViewById(R.id.view_list_item_root);
        }
    }
}
