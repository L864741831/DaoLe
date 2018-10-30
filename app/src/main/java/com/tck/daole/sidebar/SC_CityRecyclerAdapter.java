package com.tck.daole.sidebar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tck.daole.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * kylin on 2017年12月23日16:32:51
 */

public class SC_CityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;

    private static final int VIEW_TYPE_COUNT = 2;
    private Context mContext;
    private List<Games> games=new ArrayList<>();
    private List<Games> games1=new ArrayList<>();
    private List<String> characterList; // 字母List
    private List<Contact> resultList; // 最终结果（包含分组的字母）

//    private List<String> mHotCitys,changyongCitys;

    private HashMap<String, Integer> letterIndexes;

    private OnCityClickListener onCityClickListener;

//    public static final int LOCATING    = 111;
//    public static final int FAILED      = 666;
//    public static final int SUCCESS     = 888;
//    private int locateState = LOCATING;
//    private String locatedCity;

    private final int ITEM_TYPE_CHARACTER = 5;
    private final int ITEM_TYPE_CITY = 6;
    private List<Contact> SourceDateList=new ArrayList<>();
    private CharacterParser characterParser;


    public SC_CityRecyclerAdapter(Context mContext, List<Games> mCitys) {
        this.mContext = mContext;
        this.games.addAll((mCitys == null ? new ArrayList<Games>() : mCitys));
        mLayoutInflater = LayoutInflater.from(mContext);

        this.games.add(0, new Games("搜索", "0"));
//        this.games.add(1, new Games("定位", "1"));
//        this.games.add(2, new Games("常用", "2"));
//        this.games.add(3, new Games("最热", "3"));
        characterParser=new CharacterParser();

        handleCity();
    }

    private void handleCity() {
        resultList = new ArrayList<>();
        characterList = new ArrayList<>();
        letterIndexes = new HashMap<>();
        initData(games);


//        mHotCitys = new ArrayList<>();
//        mHotCitys.add("北京市");
//        mHotCitys.add("上海市");
//        mHotCitys.add("深圳市");
//        mHotCitys.add("广州市");
//        mHotCitys.add("杭州市");
//        mHotCitys.add("天津市");
//        mHotCitys.add("武汉市");
//        mHotCitys.add("成都市");
//
//        changyongCitys = new ArrayList<>();

//        try {
//            String changyongStr=(String)SPUtil.getData(mContext,"changyongCity","");
//            if (StringUtil.isSpace(changyongStr)) {
//                JSONArray array = new JSONArray();
//                array.put("郑州市");
//                changyongStr=array.toString();
//            }
//            Log.e("changyongStr",changyongStr);
//            changyongCitys =  new Gson().fromJson(changyongStr,List.class);
//            Collections.reverse(changyongCitys); // 倒序排列
////            changyongCitys=beanCity.changyongCitys;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        SourceDateList.addAll(resultList);
    }

    private void initData(List<Games> list) {


        letterIndexes.put("搜索", 0);
//        letterIndexes.put("定位", 1);
//        letterIndexes.put("常用", 2);
//        letterIndexes.put("最热", 3);

        for (int index = 1; index < list.size(); index++) {
            //当前城市拼音首字母
            String name = PinyinUtils.getFirstLetter(list.get(index).getPinyin());
            String character = (name.charAt(0) + "").toUpperCase(Locale.ENGLISH);
            if (!characterList.contains(character)) {
                if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) { // 是字母
                    characterList.add(character);
                    resultList.add(new Contact(character, ITEM_TYPE_CHARACTER));
                } else {
                    if (!characterList.contains("#")) {
                        characterList.add("#");
                        resultList.add(new Contact("#", ITEM_TYPE_CHARACTER));
                    }
                }
            }
            resultList.add(new Contact(list.get(index).getName(),list.get(index).gameId, ITEM_TYPE_CITY));
        }

        for (int i = 0; i < resultList.size(); i++) {
            Contact contact = resultList.get(i);
            if (contact.getmType()==ITEM_TYPE_CHARACTER){
                letterIndexes.put(contact.getmName(),i+1);
            }
        }
    }

    public void setData(List<Games> mCitys){
        this.games1.clear();

        this.games1.add(0, new Games("搜索", "0"));
//        this.games.add(1, new Games("定位", "1"));
//        this.games.add(2, new Games("常用", "2"));
//        this.games.add(3, new Games("最热", "3"));
        this.games1.addAll(mCitys);

        resultList.clear();
        initData(this.games1);
        notifyDataSetChanged();
    }


//    /**
//     * 更新定位状态
//     *
//     * @param state
//     */
//    public void updateLocateState(int state, String city) {
//        this.locateState = state;
//        this.locatedCity = city;
//        notifyDataSetChanged();
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CHARACTER) {
            return new CharacterHolder(mLayoutInflater.inflate(R.layout.item_sc_character, parent, false));
        } else if (viewType == ITEM_TYPE_CITY) {
            View allCityView = mLayoutInflater.inflate(R.layout.item_sc_city, parent, false);

            return new CityHolder(allCityView);
        } else {//if (viewType == 0) {//搜索
            View searchView = mLayoutInflater.inflate(R.layout.item_sc_search_city, parent, false);
            return new SearchViewHolder(searchView);
        }
//        else if (viewType == 1) {//定位
//            View locateView = mLayoutInflater.inflate(R.layout.item_sc_locate_city, parent, false);
//            locateView.findViewById(R.id.tv_located_city).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (locateState == FAILED) {
//                        //重新定位
//                        if (onCityClickListener != null) {
//                            onCityClickListener.onLocateClick();
//                        }
//                    } else if (locateState == SUCCESS) {
//                        //返回定位城市
//                        if (onCityClickListener != null) {
//                            onCityClickListener.onCityClick(locatedCity);
//                        }
//                    }
//                }
//            });
//            return new LocateViewHolder(locateView);
//        } else if (viewType == 2) {//常用
//            return new ChangYongHolder(mLayoutInflater.inflate(R.layout.item_sc_hot_city, parent, false), mContext);
//        } else {//最热
//            return new HotCityHolder(mLayoutInflater.inflate(R.layout.item_sc_hot_city, parent, false), mContext);
//        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CharacterHolder) {
            ((CharacterHolder) holder).mCharater.setText(resultList.get(position - 1).getmName());
        }
        else if (holder instanceof CityHolder) {
            ((CityHolder) holder).mCityName.setText(resultList.get(position - 1).getmName());
            ((CityHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //返回定位城市
                    if (onCityClickListener != null) {
                        onCityClickListener.onCityClick(resultList.get(position - 1).gameId,position);
                    }
                }
            });
        }
//        else if (holder instanceof LocateViewHolder) {
//            ((LocateViewHolder) holder).mTvLocatedCity.setText(locatedCity);
//        }
//        else if (holder instanceof HotCityHolder) {
//            ((HotCityHolder) holder).setData(mHotCitys, onCityClickListener);
//            ((HotCityHolder) holder).character.setText("热门城市");
//        } else if (holder instanceof ChangYongHolder) {
//            ((ChangYongHolder) holder).setData(changyongCitys, onCityClickListener);
//            ((ChangYongHolder) holder).character.setText("常用城市");
//        }
        else if (holder instanceof SearchViewHolder) {
            ((SearchViewHolder) holder).words.setOnMyTextAfterEditListener(new ClearEditText.OnMyTextAfterEditListener() {
                @Override
                public void OnAfterEdit() {

                }

                @Override
                public void onMyTextChanged(CharSequence text) {
                    String newText = text.toString().trim();
                    filterData(newText);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return resultList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position < VIEW_TYPE_COUNT - 1 ? position : resultList.get(position - 1).getmType();
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    public interface OnCityClickListener {
        void onCityClick(String name,int position);

//        void onLocateClick();
    }


    /**
     * 获取字母索引的位置
     */
    public int getPositionForSection(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    public class CharacterHolder extends RecyclerView.ViewHolder {
        TextView mCharater;
        CharacterHolder(View itemView) {
            super(itemView);
            mCharater = (TextView) itemView.findViewById(R.id.character);
        }
    }

    public class CityHolder extends RecyclerView.ViewHolder {
        TextView mCityName;
        CityHolder(View itemView) {
            super(itemView);
            mCityName = (TextView) itemView.findViewById(R.id.city_name);
        }
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
//        TextView cancel;
        ClearEditText words;
        SearchViewHolder(View itemView) {
            super(itemView);
            words = (ClearEditText) itemView.findViewById(R.id.words);
//            cancel = (TextView) itemView.findViewById(R.id.cancel);
        }
    }

//    public class LocateViewHolder extends RecyclerView.ViewHolder {
//        TextView mTvLocatedCity;
//        LocateViewHolder(View itemView) {
//            super(itemView);
//            mTvLocatedCity = (TextView) itemView.findViewById(R.id.tv_located_city);
//        }
//    }
//
//    public class ChangYongHolder extends RecyclerView.ViewHolder {
//        private final RecyclerView changyongCity;
//        private TextView character;
//        private Context mContext;
//
//        ChangYongHolder(View itemView, Context mContext) {
//            super(itemView);
//            changyongCity = (RecyclerView) itemView.findViewById(R.id.recy_hot_city);
//            character = (TextView) itemView.findViewById(R.id.character);
//            this.mContext =mContext;
//            changyongCity.setLayoutManager(new GridLayoutManager(mContext,4));
//        }
//
//        void setData(List<String> mchangyongCity, SC_CityRecyclerAdapter.OnCityClickListener listener){
//            SC_HotCityAdapter hotCityAdapter = new SC_HotCityAdapter(mContext, mchangyongCity,listener);
//            changyongCity.setAdapter(hotCityAdapter);
//        }
//    }
//
//    public class HotCityHolder extends RecyclerView.ViewHolder {
//        private final RecyclerView mRecyHotCity;
//        private TextView character;
//        private Context mContext;
//
//        HotCityHolder(View itemView, Context mContext) {
//            super(itemView);
//            mRecyHotCity = (RecyclerView) itemView.findViewById(R.id.recy_hot_city);
//            character = (TextView) itemView.findViewById(R.id.character);
//            this.mContext =mContext;
//            mRecyHotCity.setLayoutManager(new GridLayoutManager(mContext,4));
//        }
//
//        void setData(List<String> mHotCity, SC_CityRecyclerAdapter.OnCityClickListener listener){
//            SC_HotCityAdapter hotCityAdapter = new SC_HotCityAdapter(mContext, mHotCity,listener);
//            mRecyHotCity.setAdapter(hotCityAdapter);
//        }
//    }

//    class BeanCity{
//        List<String> changyongCitys;
//    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        List<Games> filterDataList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            resultList.clear();
            resultList.addAll(SourceDateList);
            notifyDataSetChanged();
        } else {
            for (Games game : games) {
                String name = game.getName();
                if ((name.contains(filterStr) || characterParser.getSelling(name).startsWith(filterStr))
//                        &&!name.contains("最热")
//                        &&!name.contains("常用")
//                        &&!name.contains("定位")
                        &&!name.contains("搜索")) {
                    filterDataList.add(game);
                }
            }
            setData(filterDataList);
        }
    }


//    /**
//     * 当ListView数据发生变化时,调用此方法来更新ListView
//     */
//    public void updateListView(List<Games> list){
//        this.games = list;
//        notifyDataSetChanged();
//    }
}
