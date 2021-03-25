package com.project.module_order.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.project.common_resource.OrderPhotoListModel;
import com.project.common_resource.PhotoModel;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.adapter.OrderPhotosListAdapter;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @fileName: OrderDetailActivity
 * @author: liuboyu
 * @date: 2021/3/24 2:32 PM
 * @description: 订单详情
 */
@Route(path = ArouterConfig.Order.ORDER_DETAIL)
public class OrderDetailActivity extends CreateOrderActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        initPhotoList();
    }

    public void setData() {
        binding.tvName.setText("刘伯羽");
        binding.llPolicyCategory.setVisibility(View.GONE);
        binding.tvPolicyCategory.setVisibility(View.VISIBLE);
        binding.tvPolicyCategory.setText("公猪");
        binding.tvChoose.setVisibility(View.GONE);
        binding.tvCode.setText("KJLAKJDFLKJASKJFAK");
        binding.tvDateStart.setText("2021-3-25");
        binding.tvDateEnd.setText("2021-3-26");
        binding.tvMaster.setText("刘伯羽2");
        binding.tvDateCommit.setText("2021-3-25 10:00");
        binding.llMeasureWay.setVisibility(View.GONE);
        binding.tvMeasureWay.setVisibility(View.VISIBLE);
        binding.tvMeasureWay.setText("测长度");
        binding.lineCount.setVisibility(View.VISIBLE);
        binding.llCount.setVisibility(View.VISIBLE);
        binding.tvCount.setText("11只");
        binding.lineCommitDate.setVisibility(View.VISIBLE);
        binding.llCommitDate.setVisibility(View.VISIBLE);
        binding.llButtons.setVisibility(View.GONE);
        binding.tvName.setEnabled(false);
        binding.tvName.setClickable(false);
        binding.tvDateStart.setClickable(false);
        binding.tvDateEnd.setClickable(false);
    }

    private void initPhotoList() {
        Random random = new Random();

        PhotoModel photoModel1 = new PhotoModel();
        photoModel1.setAddr("物资学院路1");
        photoModel1.setCompany("百度1");
        photoModel1.setDate("2021-02-31");
        photoModel1.setName("刘伯羽1");
        photoModel1.setNum("15011447166");
        photoModel1.setPos("12312312312,q2312313");
        photoModel1.setResult(random.nextInt(100) + "kg");
        photoModel1.setUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201909%2F14%2F20190914195208_zoqpg.thumb.400_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1618897558&t=5c78d7e6fdc9f173f909b09688886cd0");

        PhotoModel photoModel2 = new PhotoModel();
        photoModel2.setAddr("物资学院路2");
        photoModel2.setCompany("百度2");
        photoModel2.setDate("2021-02-32");
        photoModel2.setName("刘伯羽2");
        photoModel2.setNum("15011447166");
        photoModel2.setPos("12312312312,q2312313");
        photoModel2.setResult(random.nextInt(100) + "kg");
        photoModel2.setUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.deyi.com%2Fforum%2F201909%2F16%2F094938893f6925fd639b85.jpg&refer=http%3A%2F%2Fimg.deyi.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1618897558&t=bb2aa56c643f896b9afd5435a3c6d087");

        PhotoModel photoModel3 = new PhotoModel();
        photoModel3.setAddr("物资学院路3");
        photoModel3.setCompany("百度3");
        photoModel3.setDate("2021-02-33");
        photoModel3.setName("刘伯羽3");
        photoModel3.setNum("15011447166");
        photoModel3.setPos("12312312312,q2312313");
        photoModel3.setResult(random.nextInt(100) + "kg");
        photoModel3.setUrl("https://pics2.baidu.com/feed/8d5494eef01f3a29e78cea677be85c345d607c22.jpeg?token=1a816f5c4652a5e59ec004141f57f51f&s=F0F630C5CC1263DC8A3001DB03005097");

        List<PhotoModel> photo = new ArrayList<>();
        photo.add(photoModel1);
        photo.add(photoModel2);
        photo.add(photoModel3);

        final List<OrderPhotoListModel> models = new ArrayList<>();
        models.add(new OrderPhotoListModel(random.nextInt(100) + "kg", photo));
        models.add(new OrderPhotoListModel(random.nextInt(100) + "kg", photo));
        models.add(new OrderPhotoListModel(random.nextInt(100) + "kg", photo));
        models.add(new OrderPhotoListModel(random.nextInt(100) + "kg", photo));
        models.add(new OrderPhotoListModel(random.nextInt(100) + "kg", photo));
        models.add(new OrderPhotoListModel(random.nextInt(100) + "kg", photo));
        models.add(new OrderPhotoListModel(random.nextInt(100) + "kg", photo));
        mAdapter = new OrderPhotosListAdapter();
        binding.recyclerView.setAdapter(mAdapter);
        mAdapter.bindData(true, models);
        binding.tvTitlePhotos.setVisibility(View.VISIBLE);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int index) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) models);
                bundle.putInt("index", index);

                Intent intent = new Intent(OrderDetailActivity.this, PrePhotoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

//                ARouter.getInstance().build(ArouterConfig.Order.ORDER_PHOTO_PRE)
//                        .withObject("list", models)
//                        .withInt("index", index)
//                        .navigation();
            }
        });
    }
}
