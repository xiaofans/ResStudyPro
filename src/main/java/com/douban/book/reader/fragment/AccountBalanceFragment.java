package com.douban.book.reader.fragment;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.UserInfo;
import com.douban.book.reader.event.UserInfoUpdatedEvent;
import com.douban.book.reader.fragment.share.FeedbackEditFragment_;
import com.douban.book.reader.manager.UserManager;
import com.douban.book.reader.span.IconFontSpan;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.LoginPrompt;
import com.douban.book.reader.util.RichText;
import com.douban.book.reader.util.Utils;
import com.douban.book.reader.view.ReadViewPager;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(2130903090)
public class AccountBalanceFragment extends BaseFragment {
    @ViewById(2131558579)
    TextView mAccountLeft;
    @ViewById(2131558582)
    Button mBtnDeposit;
    @ViewById(2131558583)
    Button mBtnRedeem;
    @ViewById(2131558581)
    TextView mCreditLeft;
    @ViewById(2131558584)
    TextView mFeedbackEntry;
    @Bean
    UserManager mUserManager;

    @AfterViews
    void init() {
        setTitle((int) R.string.title_my_account);
        this.mBtnDeposit.setText(RichText.textWithIcon((int) R.drawable.v_chinese_yuan, (int) R.string.btn_deposit));
        this.mBtnRedeem.setText(RichText.textWithIcon((int) R.drawable.v_redeem, (int) R.string.btn_redeem));
        this.mFeedbackEntry.setText(RichText.buildUpon((int) R.string.text_deposit_problem_feedback).appendLink((int) R.string.text_deposit_problem_feedback_link));
        loadUserInfo();
        updateUserInfoView();
        LoginPrompt.showIfNeeded(getActivity());
    }

    @Click({2131558584})
    void onFeedbackEntryClicked() {
        FeedbackEditFragment_.builder().build().showAsActivity((Fragment) this);
    }

    @Click({2131558582})
    void onBtnDepositClick() {
        AccountDepositFragment_.builder().build().showAsActivity((Fragment) this);
    }

    @Click({2131558583})
    void onBtnRedeemClick() {
        RedeemFragment_.builder().build().showAsActivity((Fragment) this);
    }

    @Click({2131558575})
    void onBtnDepositRecord() {
        DepositRecordFragment_.builder().build().showAsActivity((Fragment) this);
    }

    @Click({2131558576})
    void onBtnPurchaseRecord() {
        PurchaseRecordFragment_.builder().build().showAsActivity((Fragment) this);
    }

    @Click({2131558577})
    void onBtnCouponRecord() {
        CouponRecordFragment_.builder().build().showAsActivity((Fragment) this);
    }

    @Click({2131558578})
    void onBtnRedemptionRecord() {
        RedemptionRecordFragment_.builder().build().showAsActivity((Fragment) this);
    }

    @Background
    void loadUserInfo() {
        try {
            this.mUserManager.getCurrentUserFromServer();
        } catch (Exception e) {
            Logger.e(this.TAG, e);
        }
    }

    public void onEventMainThread(UserInfoUpdatedEvent event) {
        updateUserInfoView();
    }

    private void updateUserInfoView() {
        UserInfo userInfo = UserManager.getInstance().getUserInfo();
        if (userInfo == null) {
            finish();
            return;
        }
        this.mAccountLeft.setText(formatAmountStr(R.drawable.v_chinese_yuan, userInfo.amountLeft));
        this.mCreditLeft.setText(formatAmountStr(R.drawable.v_doucoin, userInfo.creditLeft));
    }

    private RichText formatAmountStr(int iconResId, int amount) {
        return new RichText().appendIcon(new IconFontSpan(iconResId).ratio(0.7f).paddingRight(ReadViewPager.EDGE_RATIO).verticalOffsetRatio(0.15f)).append(Utils.formatPrice(amount));
    }
}
