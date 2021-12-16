package com.example.gl.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.gl.R;
import com.example.gl.mainActivity.ManagerCallsFromAPI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComingSoonFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComingSoonFragments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private final String KEY_MANAGER_CALL_API = "MANAGER_API";
    private ManagerCallsFromAPI mManagerCallsFromAPI;
    private View mRoot;
    private int mNextPage = 1;

    private NestedScrollView mNestedScrollView;
    private final boolean mFeatchDataToRecycleView = true;

    public ComingSoonFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CoomingSoonFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static ComingSoonFragments newInstance() {
        ComingSoonFragments fragment = new ComingSoonFragments();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.fragment_cooming_soon_fragments, container, false);
        mNestedScrollView = mRoot.findViewById(R.id.nested_scrool_view);

        //Get details from user and get the key
        savedInstanceState = this.getArguments();

        if (savedInstanceState != null)
        {

            mManagerCallsFromAPI = (ManagerCallsFromAPI) savedInstanceState.getSerializable(KEY_MANAGER_CALL_API);

        }

        mManagerCallsFromAPI.getM_ListGameModel().clear();
        mManagerCallsFromAPI.CallToCoomingSoonGame(mNextPage, mRoot, container, mFeatchDataToRecycleView);

        mNestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            try
            {

                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
                {

                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    mNextPage++;
                    mManagerCallsFromAPI.CallToCoomingSoonGame(mNextPage, mRoot, container, mFeatchDataToRecycleView);

                }

            }
            catch (Exception e)
            {


                Toast.makeText(mRoot.getContext(), e.getMessage(), Toast.LENGTH_LONG);

            }

        });

        return mRoot;

    }

}