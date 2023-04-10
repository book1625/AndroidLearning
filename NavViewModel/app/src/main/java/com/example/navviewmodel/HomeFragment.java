package com.example.navviewmodel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.SeekBar;

import com.example.navviewmodel.databinding.FragmentHomeBinding;
import com.google.android.material.internal.TextWatcherAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //教程說不要，改以下面的取代
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);

        //取得 view model
        MyViewModel myViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);

        //取得當前畫面的 binding
        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        //綁定 view model and binding
        binding.setData(myViewModel);
        binding.setLifecycleOwner(getActivity());

        //透過 binding 建立相關操作

        binding.floatButtonEnter.setOnClickListener(view -> {
            NavController controller = Navigation.findNavController(view);
            controller.navigate(R.id.action_homeFragment_to_detailFragment);
        });

        //載入 seekbar 初值
        binding.seekBar.setProgress(myViewModel.getNumber().getValue());

        //超過 seekbar 的範圍時，提供 Reset 操作
        if(myViewModel.getNumber().getValue() > binding.seekBar.getMax())
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
            adb.setTitle("Large Value");
            adb.setPositiveButton("Reset", (dialogInterface, i) -> {
                myViewModel.getNumber().setValue(0);
                binding.seekBar.setProgress(0);
            });
            adb.setNegativeButton("Cancel", (dialogInterface, i) -> {
                //do nothing...
            });

            adb.create().show();
        }

        //拖動 seekbar 時改值
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                myViewModel.getNumber().setValue(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //do nothing
            }
        });

        //教程說這樣可以自動出現鍵盤，但看起來是沒有，需自已點擊，但它也沒有出現教程說的，出現後需要自己處理消失，看起來一切就…正常
        binding.editName.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.editName, 0);

        //這裡處理名字監測，更新到 model與控制是否可以轉頁
        binding.floatButtonEnter.setEnabled(false);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = binding.editName.getText().toString().trim();
                binding.floatButtonEnter.setEnabled(!name.isEmpty());
                myViewModel.setName(name);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        binding.editName.addTextChangedListener(watcher);
        binding.editName.setText(myViewModel.getName());

        return binding.getRoot();
    }
}