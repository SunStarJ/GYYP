package com.sunstar.gyyp.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.widget.Toast;

import com.sunstar.gyyp.ProjectApplication;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * WXPayEntryActivity 微信支付 包名与类名不能变
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectApplication.wxApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (baseResp.errCode) {
                case 0:
                    Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT);
                    finish();
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "支付取消", Toast.LENGTH_SHORT);
                    finish();
                    break;
                case -2:
                    Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT);
                    finish();
                    break;
            }
            finish();
        }
    }
}