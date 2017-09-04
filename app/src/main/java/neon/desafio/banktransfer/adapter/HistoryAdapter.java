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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import neon.desafio.banktransfer.R;
import neon.desafio.banktransfer.model.TransferVO;
import neon.desafio.banktransfer.model.UserVO;

@EBean
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    @RootContext
    Context mContext;
    private ArrayList<TransferVO> transferVOs;

    HistoryAdapter(Context context) {
        this.mContext = context;
    }

    public void setTransferVOList(ArrayList<TransferVO> transferVOs) {
        this.transferVOs = transferVOs;
    }

    @Override
    public int getItemCount() {
        return transferVOs.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View lineView = inflater.inflate(R.layout.history_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(lineView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TransferVO transferVO = transferVOs.get(position);

        TextView userName = holder.userName;
        TextView userPhone = holder.userPhone;
        CircleImageView userImage = holder.userImage;
        TextView transferValue = holder.transferValue;

        userName.setText(transferVO.getUserVO().getUserName());
        userPhone.setText(transferVO.getUserVO().getUserPhone());
        transferValue.setText(transferVO.getFormmatedValue());
        Picasso.with(mContext)
                .load(transferVO.getUserVO().getUserImageUrl())
                .error(R.drawable.profile_image)
                .into(userImage);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userName;
        public TextView userPhone;
        public TextView transferValue;
        public CircleImageView userImage;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.list_item_name);
            userPhone = itemView.findViewById(R.id.list_item_phone);
            userImage = itemView.findViewById(R.id.list_item_image);
            transferValue = itemView.findViewById(R.id.list_item_value);
        }
    }
}
