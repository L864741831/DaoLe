package com.tck.daole.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tck.daole.App;
import com.tck.daole.R;
import com.tck.daole.adapter.ShopcartAdapter;
import com.tck.daole.entity.GoodsInfo;
import com.tck.daole.entity.ShopCat;
import com.tck.daole.entity.StoreInfo;
import com.tck.daole.thread.HttpInterface;
import com.tck.daole.thread.MApiResultCallback;
import com.tck.daole.thread.ThreadPoolManager;
import com.tck.daole.util.SPUtil;
import com.tck.daole.util.NetUtil;
import com.tck.daole.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.Call;


/**
 * 模仿淘宝购物车界面
 */
public class ShopcatActivity extends Activity implements ShopcartAdapter.CheckInterface,
        ShopcartAdapter.ModifyCountInterface, ShopcartAdapter.GroupEdtorListener ,View.OnClickListener{
    TextView title;
    TextView subtitle;
    LinearLayout topBar;
    ExpandableListView exListView;
    TextView tvTotalPrice;
    CheckBox allChekbox;
    TextView tvDelete;
    TextView tvGoToPay;

    RelativeLayout llShar;
    LinearLayout llInfo;

    TextView tvShare;
    TextView tvSave;
    LinearLayout llCart;
    LinearLayout cart_empty;
    ImageView img_back;




    private Context context;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private ShopcartAdapter selva;
    private List<StoreInfo> groups = new ArrayList<>();// 组元素数据列表
    private Map<String, List<GoodsInfo>> children = new HashMap<>();// 子元素数据列表
    private int flag = 0;


    protected HttpInterface httpInterface;

//    private String sp_token = "";

    List<ShopCat.ShopName> shop_name_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shop_cat);

        cart_empty = (LinearLayout) findViewById(R.id.layout_cart_empty);
        img_back = (ImageView) findViewById(R.id.img_back);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        llCart = (LinearLayout) findViewById(R.id.ll_cart);
        exListView = (ExpandableListView) findViewById(R.id.exListView);
        allChekbox = (CheckBox) findViewById(R.id.all_chekbox);
        llInfo = (LinearLayout) findViewById(R.id.ll_info);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvGoToPay = (TextView) findViewById(R.id.tv_go_to_pay);
        llShar = (RelativeLayout) findViewById(R.id.ll_shar);
        tvShare = (TextView) findViewById(R.id.tv_share);
        tvSave = (TextView) findViewById(R.id.tv_save);
        tvDelete = (TextView) findViewById(R.id.tv_delete);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.View).setVisibility(View.GONE);
        }

        httpInterface = new HttpInterface(this);


        context = this;

//        ButterKnife.bind(this);
        //这里把编辑隐藏
        subtitle.setVisibility(View.VISIBLE);



        initEvents();

        initDatas();
        initListener();

    }

    private void initEvents() {
        selva = new ShopcartAdapter(groups, children, this);
        selva.setCheckInterface(this);// 关键步骤1,设置复选框接口
        selva.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
        selva.setmListener(this);
        exListView.setAdapter(selva);

        for (int i = 0; i < selva.getGroupCount(); i++) {
            exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setCartNum();
    }

    /**
     * 设置购物车产品数量
     */
    private void setCartNum() {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setChoosed(allChekbox.isChecked());
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (GoodsInfo goodsInfo : childs) {
                count += 1;
            }
        }

        //购物车已清空
        if (count == 0) {
            clearCart();
        } else {
            //不修改购物车数量
            /**
             *
             */
            //title.setText("购物车" + "(" + count + ")");
        }
    }

    private void clearCart() {
        //不修改购物车数量
        /**
         *
         */
        //title.setText("购物车" + "(" + 0 + ")");
        subtitle.setVisibility(View.GONE);
        llCart.setVisibility(View.GONE);
        cart_empty.setVisibility(View.VISIBLE);
    }

    /**
     * 模拟数据<br>
     * 遵循适配器的数据列表填充原则，组元素被放在一个List中，对应的组元素下辖的子元素被放在Map中，<br>
     * 其键是组元素的Id(通常是一个唯一指定组元素身份的值)
     */
    private void initDatas() {


//        sp_token = SPUtil.getData(ShopcatActivity.this, "token", "").toString();
        if (App.islogin) {
            getShopCat(App.token);
        } else {
            startActivity(new Intent(ShopcatActivity.this, LoginActivity.class));
            finish();
        }

//        for (int i = 0; i < 3; i++) {
//            groups.add(new StoreInfo(i + "", "店铺名字" + (i + 1) + "号店"));
//            List<GoodsInfo> products = new ArrayList<GoodsInfo>();
//            for (int j = 0; j <= i; j++) {
//                int[] img = {R.drawable.goods1, R.drawable.goods2, R.drawable.goods3, R.drawable.goods4, R.drawable.goods5, R.drawable.goods6};
//                products.add(new GoodsInfo(j + "", "商品", groups.get(i)
//                        .getName() + "的第" + (j + 1) + "个商品", 12.00 + new Random().nextInt(23), new Random().nextInt(5) + 1, "豪华", "1", img[i * j], 6.00 + new Random().nextInt(13)));
//            }
//            children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
//        }

//        for (int i = 0; i < 3; i++) {
//            groups.add(new StoreInfo(i + "", "店铺名字" + (i + 1) + "号店"));
//            List<GoodsInfo> products = new ArrayList<GoodsInfo>();
//            for (int j = 0; j <= i; j++) {
//                int[] img = {R.drawable.goods1, R.drawable.goods2, R.drawable.goods3, R.drawable.goods4, R.drawable.goods5, R.drawable.goods6};
//                products.add(new GoodsInfo(j + "", "商品", groups.get(i)
//                        .getName() + "的第" + (j + 1) + "个商品", 12.00 + new Random().nextInt(23), new Random().nextInt(5) + 1, "豪华", "1", img[i * j], 6.00 + new Random().nextInt(13)));
//            }
//            children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
//        }
//
//
//        initEvents();

    }

    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
     */
    protected void doDelete() {
        List<StoreInfo> toBeDeleteGroups = new ArrayList<StoreInfo>();// 待删除的组元素列表
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<GoodsInfo> toBeDeleteProducts = new ArrayList<GoodsInfo>();// 待删除的子元素列表
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                if (childs.get(j).isChoosed()) {
                    toBeDeleteProducts.add(childs.get(j));
                }
            }


            StringBuffer sBuffer = new StringBuffer("");
            //遍历删除
            for (int k = 0; k < toBeDeleteProducts.size(); k++) {
                //Log.i("删除",toBeDeleteProducts.get(k).getId());

                sBuffer.append(toBeDeleteProducts.get(k).getId()+",");
            }

            //Log.i("删除",sBuffer+"");
            if (StringUtil.isSpace(sBuffer.toString())){
                continue;
            }
            //删除最后一个逗号
            sBuffer.deleteCharAt(sBuffer.length() - 1);
            //调用删除接口
            deleteShopCat(App.token,sBuffer+"");

            childs.removeAll(toBeDeleteProducts);
        }
        groups.removeAll(toBeDeleteGroups);
        //记得重新设置购物车
        setCartNum();
        selva.notifyDataSetChanged();

    }

    @Override
    public void doIncrease(int groupPosition, int childPosition,
                           View showCountView, boolean isChecked) {
        GoodsInfo product = (GoodsInfo) selva.getChild(groupPosition,
                childPosition);
        int currentCount = product.getCount();
        currentCount++;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition,
                           View showCountView, boolean isChecked) {

        GoodsInfo product = (GoodsInfo) selva.getChild(groupPosition,
                childPosition);
        int currentCount = product.getCount();
        if (currentCount == 1)
            return;
        currentCount--;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void childDelete(int groupPosition, int childPosition) {
        children.get(groups.get(groupPosition).getId()).remove(childPosition);
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        if (childs.size() == 0) {
            groups.remove(groupPosition);
        }
        selva.notifyDataSetChanged();
        //     handler.sendEmptyMessage(0);
        calculate();
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setChoosed(isChecked);
        }
        if (isAllCheck())
            allChekbox.setChecked(true);
        else
            allChekbox.setChecked(false);
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosiTion,
                           boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            // 不全选中
            if (childs.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        //获取店铺选中商品的总金额
        if (allChildSameState) {
            group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }

        if (isAllCheck()) {
            allChekbox.setChecked(true);// 全选
        } else {
            allChekbox.setChecked(false);// 反选
        }
        selva.notifyDataSetChanged();
        calculate();

    }

    private boolean isAllCheck() {

        for (StoreInfo group : groups) {
            if (!group.isChoosed())
                return false;

        }

        return true;
    }

    /**
     * 全选与反选
     */
    private void doCheckAll() {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setChoosed(allChekbox.isChecked());
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                childs.get(j).setChoosed(allChekbox.isChecked());
            }
        }
        selva.notifyDataSetChanged();
        calculate();
    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;

/*        List<ShopCat.ShopName> shop_name_list = new ArrayList<>();
        List<ShopCat.ShopContent> shop_content_list  = new ArrayList<>();*/

        //先清空两个存商店和商品的List
        shop_name_list.clear();


        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            List<ShopCat.ShopContent> shop_content_list  = new ArrayList<>();
            for (int j = 0; j < childs.size(); j++) {
                GoodsInfo product = childs.get(j);

                if (product.isChoosed()) {

                    totalCount++;
                    totalPrice += product.getPrice() * product.getCount();

                    shop_content_list.add(new ShopCat.ShopContent(product.getName(),product.getColor(),product.getDesc(),product.getSize(),product.getCount(),product.getPrice()));

                    Log.i("遍历添加商品",product.getDesc());

                }

            }





            //Log.i("遍历商品数量111",shop_content_list.size()+"");

            if(shop_content_list.size() != 0){

                Log.i("遍历商品添加入商店","添加"+group.getName());
                shop_name_list.add(new ShopCat.ShopName(group.getId(),group.getName(),shop_content_list,group.getLowestPrice(),group.getServicePrice()));

            }


            Log.i("遍历商店数量",shop_name_list.size()+"");


        }




        for(int k = 0; k < shop_name_list.size(); k++){
            //Log.i("遍历商店",shop_name_list.get(k).name);
            //Log.i("遍历商店的数量",shop_name_list.size()+"");
            for (int m = 0; m < shop_name_list.get(k).business.size(); m++){
                //Log.i("遍历选中的商品",shop_name_list.get(k).business.get(m).takeProjectName);
            }
        }



/*        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        df.format(你要格式化的数字);*/

        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        df.format(totalPrice);

        tvTotalPrice.setText("￥" + totalPrice);
        /**
         * 这里不该表结算状态
         *
         *
         */
        //tvGoToPay.setText("去支付(" + totalCount + ")");
        //计算购物车的金额为0时候清空购物车的视图
        if (totalCount == 0) {
            setCartNum();
        } else {
            /**
             * 这里不改变标题购物车数量
             *
             *
             */
            //title.setText("购物车" + "(" + totalCount + ")");
        }
    }


    protected void initListener() {
        allChekbox.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        tvGoToPay.setOnClickListener(this);
        subtitle.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    //@OnClick({R.id.all_chekbox, R.id.tv_delete, R.id.tv_go_to_pay, R.id.subtitle, R.id.tv_save, R.id.tv_share, R.id.img_back})
    public void onClick(View view) {
        AlertDialog alert;
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.all_chekbox:
                doCheckAll();
                break;
            case R.id.tv_delete:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要移除的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                doDelete();


                            }
                        });
                alert.show();
                break;
            case R.id.tv_go_to_pay:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要支付的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("总计:\n" + totalCount + "种商品\n" + totalPrice + "元");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //传入订单页
                                //shop_name_list

                                Log.i("传给订单",shop_name_list.size()+"");

                                if(shop_name_list.size()!=1){
                                    Toast.makeText(ShopcatActivity.this,"不能同时结算两家店的商品",Toast.LENGTH_SHORT).show();
                                }else{
                                    Gson gson = new Gson();
                                    String dingdan_json = gson.toJson(shop_name_list.get(0));
                                    System.out.println("带泛型的list转化为json==" + dingdan_json);
                                    Log.i("传给订单页的json数据", dingdan_json);




                                    Intent intent = new
                                            Intent(ShopcatActivity.this,SpellOrderActivity.class);
                                    //在Intent对象当中添加一个键值对
                                    intent.putExtra("dingdan_json",dingdan_json);   //商店和商品
                                    intent.putExtra("totalPrice",totalPrice);   //总价格
                                    startActivity(intent);
                                }



                                return;
                            }
                        });
                alert.show();
                break;
            case R.id.subtitle:
                if (flag == 0) {
                    llInfo.setVisibility(View.GONE);
                    tvGoToPay.setVisibility(View.GONE);
                    llShar.setVisibility(View.VISIBLE);
                    subtitle.setText("完成");
                } else if (flag == 1) {
                    llInfo.setVisibility(View.VISIBLE);
                    tvGoToPay.setVisibility(View.VISIBLE);
                    llShar.setVisibility(View.GONE);
                    subtitle.setText("编辑");
                }
                flag = (flag + 1) % 2;//其余得到循环执行上面2个不同的功能
                break;
            case R.id.tv_share:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要分享的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ShopcatActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_save:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要保存的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ShopcatActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void groupEdit(int groupPosition) {
        groups.get(groupPosition).setIsEdtor(true);
        selva.notifyDataSetChanged();
    }

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            //删除购物车后动态改变数量
//            setCartNum();
//        }
//    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selva = null;
        groups.clear();
        totalPrice = 0;
        totalCount = 0;
        children.clear();
    }


    //获得购物车列表
    public void getShopCat(final String token) {

//        for (int i = 0; i < 3; i++) {
//            groups.add(new StoreInfo(i + "", "店铺名字" + (i + 1) + "号店"));
//            List<GoodsInfo> products = new ArrayList<GoodsInfo>();
//            for (int j = 0; j <= i; j++) {
//                int[] img = {R.drawable.goods1, R.drawable.goods2, R.drawable.goods3, R.drawable.goods4, R.drawable.goods5, R.drawable.goods6};
//                products.add(new GoodsInfo(j + "", "商品", groups.get(i)
//                        .getName() + "的第" + (j + 1) + "个商品", 12.00 + new Random().nextInt(23), new Random().nextInt(5) + 1, "豪华", "1", img[i * j], 6.00 + new Random().nextInt(13)));
//            }
//            children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
//        }
//
//
//        initEvents();
//        if (NetUtil.isNetWorking(this)) {
//
//            //Log.e("获取购物车", "11111");
//
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.getShopCatList(token, new MApiResultCallback() {
                        @Override
                        public void onSuccess(final String result) {
                            Log.e("获取购物车.成功", result);

                            Message message=new Message();
                            message.what=0;
                            message.getData().putString("result",result);
                            handler.sendMessage(message);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ShopCat data = new Gson().fromJson(result, ShopCat.class);
//                                    Log.e("获取购物车.成功", data.message);
//
//                                    List<ShopCat.ShopName> shopname_list = data.list;
//                                    for (int i = 0; i < shopname_list.size(); i++) {
//                                        groups.add(new StoreInfo(shopname_list.get(i).AdminStoreOid, shopname_list.get(i).name));
//                                        List<GoodsInfo> products = new ArrayList<GoodsInfo>();
//                                        Log.i("ShopCat",shopname_list.get(i).name );
//
//                                        List<ShopCat.ShopContent> shop_content = shopname_list.get(i).business;
//                                        for (int j = 0; j < shop_content.size(); j++) {
//                                            products.add(new GoodsInfo(shop_content.get(j).oid,
//                                                    shop_content.get(j).takeProjectName
//                                                    ,""
//                                                    ,1.11,2,"","",
//                                                    1,1.2));
//                                            Log.i("ShopCat",shop_content.get(j).takeProjectName );
//                                        }
//                                        children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
//
//                                    }
//
//                                    initEvents();
//
//
//
//                                }
//                            });


                        }

                        @Override
                        public void onFail(String response) {

                            Log.e("获取购物车.异常", response);

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            Log.e("onTokenError", response);
                        }
                    });
                }
            });
//        } else {
//            Toast.makeText(ShopcatActivity.this, R.string.system_busy, Toast.LENGTH_SHORT).show();
//            //toast(R.string.system_busy);
//        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
//                    for (int i = 0; i < 3; i++) {
//                        groups.add(new StoreInfo(i + "", "店铺名字" + (i + 1) + "号店"));
//                        List<GoodsInfo> products = new ArrayList<GoodsInfo>();
//                        for (int j = 0; j <= i; j++) {
//                            int[] img = {R.drawable.goods1, R.drawable.goods2, R.drawable.goods3, R.drawable.goods4, R.drawable.goods5, R.drawable.goods6};
//                            products.add(new GoodsInfo(j + "", "商品", groups.get(i)
//                                    .getName() + "的第" + (j + 1) + "个商品", 12.00 + new Random().nextInt(23), new Random().nextInt(5) + 1, "豪华", "1", img[i * j], 6.00 + new Random().nextInt(13)));
//                        }
//                        children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
//                    }
//
//
//                    initEvents();

                    ShopCat data = new Gson().fromJson(msg.getData().get("result")+"", ShopCat.class);

                    Log.i("ShopCat",msg.getData().get("result")+"" );


                    List<ShopCat.ShopName> shopname_list = data.list;
                    for (int i = 0; i < shopname_list.size(); i++) {
                        groups.add(new StoreInfo(shopname_list.get(i).AdminStoreOid, shopname_list.get(i).name,shopname_list.get(i).lowestPrice,shopname_list.get(i).servicePrice));
                        List<GoodsInfo> products = new ArrayList<GoodsInfo>();
                        //Log.i("ShopCat",shopname_list.get(i).name );

                        List<ShopCat.ShopContent> shop_content = shopname_list.get(i).business;
                        for (int j = 0; j < shop_content.size(); j++) {
                            products.add(new GoodsInfo(shop_content.get(j).oid,
                                    shop_content.get(j).projectOid
                                    ,shop_content.get(j).takeProjectName
                                    ,shop_content.get(j).price,shop_content.get(j).purchaseNumber,shop_content.get(j).mainImage,shop_content.get(j).detail,1,
                                    2.3));
                            //Log.i("ShopCat",shop_content.get(j).takeProjectName );
                        }
                        children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key

                    }
                    if (groups.size()>0){
                        cart_empty.setVisibility(View.GONE);
                    }else {
                        cart_empty.setVisibility(View.VISIBLE);
                    }
//                    setCartNum();
                    initEvents();
                    break;
            }
        }
    };





    public void deleteShopCat(final String token,final String orderOid){

        Log.e("删除商品的oid",orderOid);


        if (NetUtil.isNetWorking(this)) {

            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.deleteShop(token,orderOid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("删除商品.成功", result);

                        }

                        @Override
                        public void onFail(String response) {

                            Log.e("删除商品.异常", response);

                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            Log.e("onTokenError", response);
                        }
                    });
                }
            });
        } else {
            Toast.makeText(ShopcatActivity.this,R.string.system_busy,Toast.LENGTH_SHORT).show();
            //toast(R.string.system_busy);
        }

    }










}
