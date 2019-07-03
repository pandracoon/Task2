package com.example.task2.ui.main.Tab2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import androidx.fragment.app.Fragment;
import com.example.task2.R;

public class Fragment2 extends Fragment {

  GridView gridViewImages;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_fragment2, container, false);

    gridViewImages = (GridView) view.findViewById(R.id.gridViewImages);
    ImageGridAdapter imageGridAdapter = new ImageGridAdapter(this.getContext());
    gridViewImages.setAdapter(imageGridAdapter);

    return view;

  }

}