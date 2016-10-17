package com.dynamicadding.android_dynamicadding;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linear_add_people;
    private LinearLayout linear_parent;
    private String[] names = {"jaoom", "xhjphhhhhhhhhhh", "det", "olp", "ttwppppppppppppp", "sgh"};
    private String[] phones = {"111111", "222222", "333333", "444444", "555555", "666666"};
    private List<AddBean> mList = new ArrayList<AddBean>();
    private List<AddBean> addList = new ArrayList<AddBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AddBean addBean;
        for (int i = 0; i < names.length; i++) {
            addBean = new AddBean();
            addBean.setName(names[i]);
            addBean.setPhone(phones[i]);
            mList.add(addBean);
        }

        linear_add_people = (LinearLayout) findViewById(R.id.linear_add_people);
        linear_add_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("选择联系人");
                //    指定下拉列表的显示数据
//        final String[] array = Data.organizations.split(",");
                //    设置一个下拉的列表选择项
                builder.setItems(names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CreateView(linear_parent, mList.get(which));
                    }
                });
                builder.show();

            }
        });
        linear_parent = (LinearLayout) findViewById(R.id.linear_parent);
    }


    private void CreateView(final LinearLayout parent, AddBean bean) {
        if (parent == null) {
            return;
        }
        if (mList == null || mList.size() == 0) {
            return;
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        final View view = LayoutInflater.from(this).inflate(R.layout.item_add_phone, null);//也可以从XML中加载布局
        view.setLayoutParams(lp);//设置布局参数

        TextView tv1 = (TextView) view.findViewById(R.id.tv_content);
        tv1.setText(bean.getName());

        LinearLayout linear = (LinearLayout) view.findViewById(R.id.linear);
        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeView(view);

                AddBean bean = (AddBean) view.getTag();
                addList.remove(bean);

                for (int i = 0; i < addList.size(); i++) {
                    Log.i("AddViewActivity", "phone=" + addList.get(i).toString() + "\n");
                }

            }
        });

        addList.add(bean);
        view.setTag(bean);
        parent.addView(view);

    }


}
