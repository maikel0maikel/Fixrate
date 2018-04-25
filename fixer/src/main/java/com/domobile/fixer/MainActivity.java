package com.domobile.fixer;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.domobile.fixer.adapter.CurrencyAdapter;
import com.domobile.fixer.bean.Currency;
import com.domobile.fixer.utils.KeybordTools;
import com.domobile.widget.LoadingDialog;
import com.domobile.widget.recyclerView.OnRecyclerItemClickListener;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements ICurrencyControll.View, CurrencyAdapter.CurrencyInputListener {
    private CurrencyAdapter mAdapter;
    private ICurrencyControll.Controller mController;
    private ViewStub mEmptyStub;
    private LoadingDialog dialog;
    RecyclerView recyclerView;
    //private boolean isEmptyInflate = false;
    ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            } else {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = 0;
//                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mAdapter.getCurrencyList(), i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mAdapter.getCurrencyList(), i, i - 1);
                }
            }
            mAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d("onSwiped","onSwiped:"+direction);
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);
        }

    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setBackgroundDrawable(null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.currency_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                KeybordTools.closeKeybord(MainActivity.this);
            }
        });
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                Log.d("xxx", "");
                KeybordTools.closeKeybord(MainActivity.this);

            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
//                if (vh.getLayoutPosition() != 0 && vh.getLayoutPosition() != 1) {
                mItemTouchHelper.startDrag(vh);
//                }
            }
        });
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        mAdapter = new CurrencyAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setInputListener(this);
        ViewStub stub = findViewById(R.id.nav_stub);
        stub.inflate();
        mEmptyStub = findViewById(R.id.empty_stub);
//        mEmptyStub.setOnInflateListener(new ViewStub.OnInflateListener() {
//            @Override
//            public void onInflate(ViewStub stub, View inflated) {
//                isEmptyInflate = true;
//            }
//        });
        new CurrencyController(this).start();
    }

    @Override
    public void setPresenter(ICurrencyControll.Controller controller) {
        mController = controller;
    }

    @Override
    public void loading() {
        showLoadingDialog();
    }

    @Override
    public void loadOrUpdateSuccess(boolean isUpdate) {
        if (isUpdate&&!isFinishing()){
            Toast.makeText(this,R.string.label_update_success,Toast.LENGTH_SHORT).show();
        }
        notifyView();
    }


    @Override
    public void loadOrUpdateFailure(String error) {
        notifyView();
    }

    private void notifyView() {
        if (mAdapter.getItemCount() == 0) {
            if (mEmptyStub.getParent() != null) {
                mEmptyStub.inflate();
            }
        } else {
            mEmptyStub.setVisibility(View.GONE);
        }
        dismissLoadingDialog();
    }

    @Override
    public void notifyItem(Currency currency) {
        mAdapter.addCurrency(currency);
    }

    @Override
    public void notifyDataSetChanged() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }


    @Override
    public void errorInput() {
        if (!isFinishing()){
            Toast.makeText(this,R.string.label_intput_error,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void closeSoft() {
        KeybordTools.closeKeybord(MainActivity.this);
    }

    public void updateCurrency(View view) {
        if (!isFinishing()) {
            mController.refresh();
        }
    }

    private void showLoadingDialog() {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
        }
        if (!dialog.isShowing() && !isFinishing()) {
            dialog.show();
        }
    }

    private void dismissLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onDone(String rate,String oldRate,String rateName) {
        mController.recalculate(rate,oldRate,rateName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.saveOrder();
        mController.closeRequest();
    }
}
