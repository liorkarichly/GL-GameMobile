package com.example.gl.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.gl.R;
import com.example.gl.mainActivity.eExtantionType;
import com.example.gl.model.DevelopersModel;
import com.example.gl.model.GameSingleModel;
import com.example.gl.model.GenresModel;
import com.example.gl.model.PlatformModel;
import com.example.gl.model.PublisherModel;
import com.example.gl.response.IMyCallback;
import com.example.gl.response.ResponseFromAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragments#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SearchFragments extends Fragment implements View.OnClickListener
{

    private final String KEY_STRING = "SEARCH_QUERY";
    private final String KEY_HASH = "HASHMAP";

    //Spinners
    public Spinner mSpinnerGenres;
    private Spinner mSpinnerPlatforms;
    private Spinner mSpinnerPublisher;
    private Spinner mSpinnerReleaseFrom;
    private Spinner mSpinnerReleaseTo;
    private Spinner mSpinnerDeveloprs;

    private ViewGroup mCointener;
    private EditText mTextSearch;
    private View mRootView;
    private LayoutInflater mLayoutInflater;

    //Buttons search and clear
    private Button mButtonSearch;
    private Button mButtonClear;
    private Bundle mBundle;

    //sign if the date is ilegal
    private boolean mFlagDate = true;

    //hash maps for id platform and set to filter search
    private HashMap<eExtantionType, String > mHashMap;
    private HashMap<String, Integer> mIdPlatformAndNames;
    private String mEditText = null;

    //For take year and checking
    private String mYearFrom = null;
    private String mYearTo = null;

    public SearchFragments()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment SearchFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragments newInstance() {
        SearchFragments fragment = new SearchFragments();

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
        mRootView =  inflater.inflate(R.layout.fragment_search_fragments, container, false);
        mCointener = container;
        mLayoutInflater = inflater;
        mBundle = savedInstanceState;

        mSpinnerPlatforms = (Spinner) mRootView.findViewById(R.id.spinner_platform);
        mSpinnerPublisher = (Spinner) mRootView.findViewById(R.id.spinner_publisher);
        mSpinnerGenres = (Spinner) mRootView.findViewById(R.id.spinner_gener);
        mSpinnerReleaseFrom = (Spinner) mRootView.findViewById(R.id.spinner_year_of_release_from);
        mSpinnerReleaseTo = (Spinner) mRootView.findViewById(R.id.spinner_year_of_release_to);
        mSpinnerDeveloprs = (Spinner) mRootView.findViewById(R.id.spinner_developrs);

        mButtonSearch = (Button) mRootView.findViewById(R.id.button_search);
        mButtonClear =  (Button) mRootView.findViewById(R.id.button_clear);

        mTextSearch =  mRootView.findViewById(R.id.search_input_text);
        mHashMap = new HashMap<>();
        mIdPlatformAndNames = new HashMap<>();

        callToDeveloprsSpinner();
        callToPlatformsSpinner();
        callToPublishersSpinner();
        callToGenresSpinner();
        spinnerYearsOfRelease();

        mButtonSearch.setOnClickListener(this);
        mButtonClear.setOnClickListener(this);

        mSpinnerDeveloprs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                if(!parent.getItemAtPosition(position).equals("Choose Developrs"))
                {

                    String item = mSpinnerDeveloprs.getSelectedItem().toString();
                    mHashMap.put(eExtantionType.Developrs, item);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        mSpinnerPlatforms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if(!parent.getItemAtPosition(position).equals("Choose Platform"))
                {

                    String item = mSpinnerPlatforms.getSelectedItem().toString();
                    Log.d("Item", String.valueOf(mIdPlatformAndNames.get(item)));
                    mHashMap.put(eExtantionType.Platform, String.valueOf(mIdPlatformAndNames.get(item)));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        mSpinnerReleaseFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!parent.getItemAtPosition(position).equals("Choose Year"))
                {

                    String item = mSpinnerReleaseFrom.getSelectedItem().toString();
                    mYearFrom = item + "-01-01";

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        mSpinnerReleaseTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!parent.getItemAtPosition(position).equals("Choose Year")){

                    if (mHashMap.containsKey(eExtantionType.ReleaseDate))
                    {

                        String item = mSpinnerReleaseTo.getSelectedItem().toString();
                        mYearTo = item + "-12-31";

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        mSpinnerGenres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!parent.getItemAtPosition(position).equals("Choose Genre"))
                {
                    String item = mSpinnerGenres.getSelectedItem().toString();
                    mHashMap.put(eExtantionType.Genre, item);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });

        mSpinnerPublisher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!parent.getItemAtPosition(position).equals("Choose Publisher")){

                    String item = mSpinnerPublisher.getSelectedItem().toString();
                    mHashMap.put(eExtantionType.Publisher, item);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        return mRootView;

    }

    //Platform
    private void spinnerPlatform(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, List<PlatformModel> i_List)
    {

        List<String> platformNamesList = new ArrayList<>();
        platformNamesList.add(0,"Choose Platform");

                for(PlatformModel platform: i_List)
                {

                    platformNamesList.add(platform.getName());
                    mIdPlatformAndNames.put(platform.getName(),platform.getId());

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.my_selected_item, platformNamesList);
                adapter.setDropDownViewResource(R.layout.my_selected_item_dropdown);
                 mSpinnerPlatforms.setAdapter(adapter);

    }

    //Year OF Release
    private void spinnerYearsOfRelease()
    {

        List<String> ReleaseYears = new ArrayList<>();
        ReleaseYears.add(0,"Choose Year");
        ReleaseYears.add("2000");
        ReleaseYears.add("2001");
        ReleaseYears.add("2002");
        ReleaseYears.add("2003");
        ReleaseYears.add("2004");
        ReleaseYears.add("2005");
        ReleaseYears.add("2006");
        ReleaseYears.add("2007");
        ReleaseYears.add("2008");
        ReleaseYears.add("2009");
        ReleaseYears.add("2010");
        ReleaseYears.add("2011");
        ReleaseYears.add("2012");
        ReleaseYears.add("2013");
        ReleaseYears.add("2014");
        ReleaseYears.add("2015");
        ReleaseYears.add("2016");
        ReleaseYears.add("2017");
        ReleaseYears.add("2018");
        ReleaseYears.add("2019");
        ReleaseYears.add("2020");
        ReleaseYears.add("2021");

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(mLayoutInflater.getContext(), R.layout.my_selected_item, ReleaseYears);

        adapter.setDropDownViewResource(R.layout.my_selected_item_dropdown);
        mSpinnerReleaseFrom.setAdapter(adapter);
        mSpinnerReleaseTo.setAdapter(adapter);

    }

    //Publisher
    private void spinnerPublisher(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, List<PublisherModel> i_List)
    {

        List<String> publisherNamesList = new ArrayList<>();
        publisherNamesList.add(0, "Choose Publisher");

        for(PublisherModel publisherModel: i_List)
        {

            publisherNamesList.add(publisherModel.getName());

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.my_selected_item, publisherNamesList);
        adapter.setDropDownViewResource(R.layout.my_selected_item_dropdown);
        mSpinnerPublisher.setAdapter(adapter);

    }

    //Genres
    private  void spinnerGenres(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, List<GenresModel> i_List) {

        List<String> genresModelNamesList = new ArrayList<>();
        genresModelNamesList.add(0, "Choose Genre");

        for(GenresModel genresModel: i_List)
        {

            genresModelNamesList.add(genresModel.getName());

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.my_selected_item, genresModelNamesList);
        adapter.setDropDownViewResource(R.layout.my_selected_item_dropdown);
        mSpinnerGenres.setAdapter(adapter);

    }

    //Developrs
    private  void spinnerDeveloprs(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, List<DevelopersModel> i_List) {

        List<String> developrModelNamesList = new ArrayList<>();

        developrModelNamesList.add(0, "Choose Developrs");

        for(DevelopersModel developersModel : i_List)
        {

            Log.d("Name: ", developersModel.getName());
            developrModelNamesList.add(developersModel.getName());

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.my_selected_item, developrModelNamesList);
        adapter.setDropDownViewResource(R.layout.my_selected_item_dropdown);
        mSpinnerDeveloprs.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {

        ResultBySearch filteredGamesFragment = null;

        switch (v.getId())
        {

            case R.id.button_search:

                if (mTextSearch.getText().toString().equals("What is on your mind?") || mTextSearch.getText().toString().equals(""))
                {

                    if (mHashMap != null)
                    {

                        if (checkingYearIsIllegal())//true is enter
                        {

                            Bundle bundle = new Bundle();
                            bundle.putSerializable(KEY_HASH, mHashMap);
                            bundle.putString(KEY_STRING, mEditText);
                            filteredGamesFragment = new ResultBySearch();
                            filteredGamesFragment.setArguments(bundle);

                        }
                        else
                        {

                            filteredGamesFragment = new ResultBySearch();

                        }

                    }

                }
                else
                {

                    if (mFlagDate == false)
                    {

                        Toast.makeText(mRootView.getContext(),"Wrong Release Date Input, Please Fix it",Toast.LENGTH_LONG).show();

                    }
                    else
                    {

                        mHashMap = null;
                        Bundle bundle = new Bundle();
                        String m_EditText = mTextSearch.getText().toString();
                        m_EditText = m_EditText.replace(' ', '-').toLowerCase();

                        bundle.putString(KEY_STRING, m_EditText);
                        bundle.putSerializable(KEY_HASH, mHashMap);
                        filteredGamesFragment  = new ResultBySearch();
                        filteredGamesFragment.setArguments(bundle);

                    }

                }

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, filteredGamesFragment).addToBackStack(null).commit();
                break;

            case R.id.button_clear:

                  mSpinnerGenres.setSelection(0);
                  mSpinnerPlatforms.setSelection(0);
                  mSpinnerPublisher.setSelection(0);
                  mSpinnerReleaseFrom.setSelection(0);
                  mSpinnerReleaseTo.setSelection(0);
                  mTextSearch.setText("What is on your mind?");
                  mFlagDate = true;
                  mHashMap = new HashMap<>();
                  mIdPlatformAndNames = new HashMap<>();

                break;

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callToPlatformsSpinner(){

        ResponseFromAPI.getInstance().GetListPlatformsName(new IMyCallback() {
            @Override
            public <T> void onSuccess(@NonNull List<T> spinner)
            {

                spinnerPlatform(mLayoutInflater, mCointener, mBundle, (List<PlatformModel>)spinner);

            }

            @Override
            public void onSuccess(@NonNull GameSingleModel gamesSingle) {

            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Toast.makeText(mRootView.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT);

            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callToPublishersSpinner()
    {

        ResponseFromAPI.getInstance().GetListPublishersName(new IMyCallback() {
            @Override
            public <T> void onSuccess(@NonNull List<T> spinner) {

                spinnerPublisher(mLayoutInflater, mCointener, mBundle, (List<PublisherModel>)spinner);

            }

            @Override
            public void onSuccess(@NonNull GameSingleModel gamesSingle) {

            }


            @Override
            public void onError(@NonNull Throwable throwable) {
                Toast.makeText(mRootView.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT);

            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callToDeveloprsSpinner(){

        ResponseFromAPI.getInstance().GetListDeveloprsName(new IMyCallback() {
            @Override
            public <T> void onSuccess(@NonNull List<T> spinner) {
                
                spinnerDeveloprs(mLayoutInflater, mCointener, mBundle, (List<DevelopersModel>) spinner);

            }

            @Override
            public void onSuccess(@NonNull GameSingleModel gamesSingle) {

            }

            @Override
            public void onError(@NonNull Throwable throwable) {

                Toast.makeText(mRootView.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT);

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callToGenresSpinner()
    {

        ResponseFromAPI.getInstance().GetGenres(new IMyCallback() {

            @Override
            public <T> void onSuccess(@NonNull List<T> spinner) {

                spinnerGenres(mLayoutInflater, mCointener, mBundle, (List<GenresModel>)spinner);

            }

            @Override
            public void onSuccess(@NonNull GameSingleModel gamesSingle) {

            }


            @Override
            public void onError(@NonNull Throwable throwable) {
                Toast.makeText(mRootView.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT);

            }

        });

    }

    private boolean checkingYearIsIllegal()
    {

        mFlagDate = false;
        String date = null;

          if (mYearFrom == null &&  mYearTo == null)// Year From AND TO Year is null
                 {

                     mFlagDate = !mFlagDate;

                 }
                 else if (mYearFrom == null && mYearTo != null)// From is null AND to Yeas isn't null, we take only year of to
                 {

                     date = String.format(mSpinnerReleaseTo.getSelectedItem().toString() + "-01-01" + "," + mYearTo);
                     mFlagDate = !mFlagDate;

                 }
                 else if( mYearFrom != null && mYearTo != null)// To AND From isn't null
                 {

                     if (Integer.parseInt(mYearFrom.substring(0,4)) < Integer.parseInt(mYearTo.substring(0,4))) // who is bigger, if To is bigger, return true else false
                     {

                         date = String.format(mYearFrom + "," + mYearTo);
                         mFlagDate = !mFlagDate;
                     }

                 }
                 else {//From isn't null and To is null

              date = String.format(mYearFrom + "," + mSpinnerReleaseFrom.getSelectedItem().toString() + "-12-31");
              mFlagDate = !mFlagDate;

          }

            mHashMap.put(eExtantionType.ReleaseDate, date);
            return mFlagDate;

        }

}