package com.dk.pharmeasy;

import java.util.Vector;

import com.dk.dao.ItemDataDAO;
import com.dk.pharmeasy.R;
import com.dk.utils.BaseBL;
import com.dk.utils.DataListener;
import com.dk.utils.DatabaseHandler;
import com.dk.utils.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements DataListener {
	private CustomPagerAdapter mCustomPagerAdapter;
	private ViewPager mViewPager;
	private ProgressDialog loader;
	private String dataURL = "https://www.1mg.com/api/v1/search/autocomplete?name=b&pageSize=10000000&_=1435404923427";
	private DatabaseHandler dbHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		
		mCustomPagerAdapter = new CustomPagerAdapter(this);
		 
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mCustomPagerAdapter);
		
		dbHandler = new DatabaseHandler(MainActivity.this);
		
		Vector<ItemDataDAO> vecData = dbHandler.getAllData();
		if(vecData != null && vecData.size() >0) {
			Log.d("Size of data", ""+vecData.size());
			mCustomPagerAdapter.setData(vecData);
		} else {
			callService();
		}
	}
	
	private void callService()
	{
		if(dataURL != null && !dataURL.equalsIgnoreCase("")) {
			if(Utils.isNetworkAvailable(MainActivity.this)) {
				showLoader();
				new BaseBL(this).execute(dataURL);
			} else {
				Toast.makeText(MainActivity.this, "Internet is not available. Please check and try again.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void showLoader()
	{
		loader = ProgressDialog.show(MainActivity.this, "", "Loading...");
	}
	
	private void hideLoader()
	{
		if(loader != null) {
			loader.dismiss();
			loader = null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dataRetrieved(Object objData) {
		hideLoader();
		if(objData != null) {
			if(objData instanceof Vector<?>) {
				dbHandler.insertData((Vector<ItemDataDAO>) objData);
				mCustomPagerAdapter.setData((Vector<ItemDataDAO>) objData);
			}
		}
	}
	
	class CustomPagerAdapter extends PagerAdapter {
		 
	    private Context mContext;
	    private LayoutInflater mLayoutInflater;
	    private Vector<ItemDataDAO> vecData;
	 
	    public CustomPagerAdapter(Context context) {
	        mContext = context;
	        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	    
	    public void setData(Vector<ItemDataDAO> vecData)
	    {
	    	this.vecData = vecData;
	    	this.notifyDataSetChanged();
	    }
	 
	    @Override
	    public int getCount() {
	        return vecData != null ? vecData.size() : 0;
	    }
	 
	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view == ((LinearLayout) object);
	    }
	 
	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {
	        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
	        
	        TextView tvName = (TextView) itemView.findViewById(R.id.tvNameValue);
	        TextView tvLabel = (TextView) itemView.findViewById(R.id.tvLabelValue);
	        TextView tvPackSize = (TextView) itemView.findViewById(R.id.tvPackSizeValue);
	        TextView tvMfdBy = (TextView) itemView.findViewById(R.id.tvMfdByValue);
	        TextView tvMrp = (TextView) itemView.findViewById(R.id.tvMrpValue);
	        
	        if(vecData != null && vecData.size() >= position) {
	        	ItemDataDAO objItem = vecData.elementAt(position);
	        	tvName.setText(objItem.getName());
	        	tvLabel.setText(objItem.getLabel());
	        	tvPackSize.setText(objItem.getPackSize());
	        	tvMfdBy.setText(objItem.getMfby());
	        	tvMrp.setText(objItem.getMrp());
	        }
	 
	        container.addView(itemView);
	 
	        return itemView;
	    }
	 
	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        container.removeView((LinearLayout) object);
	    }
	}
}
