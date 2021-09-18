package com.example.gl.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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

import com.example.gl.R;
import com.example.gl.mainActivity.eExtantionType;
import com.example.gl.model.DevelopersModel;
import com.example.gl.model.GameSingleModel;
import com.example.gl.model.GenresModel;
import com.example.gl.model.PlatformModel;
import com.example.gl.model.PublisherModel;
import com.example.gl.response.IMyCallback;
import com.example.gl.response.ResponseFromAPI;

import org.jetbrains.annotations.NotNull;

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
    public Spinner m_SpinnerGenres;
    private Spinner m_SpinnerPlatforms;
    private Spinner m_SpinnerPublisher;
    private Spinner m_SpinnerReleaseFrom;
    private Spinner m_SpinnerReleaseTo;
    private Spinner m_SpinnerDeveloprs;

    private ViewGroup m_Cointener;
    private EditText m_TextSearch;
    private View m_RootView;
    private LayoutInflater m_LayoutInflater;

    //Buttons search and clear
    private Button m_ButtonSearch;
    private Button m_ButtonClear;

    private Bundle m_Bundle;

    //sign if the date is ilegal
    private boolean m_FlagDate = true;

    //hash maps for id platform and set to filter search
    private HashMap<eExtantionType, String > m_HashMap;
    private HashMap<String, Integer> m_IdPlatformAndNames;
    private String m_EditText = null;

    //For take year and checking
    private String m_YearFrom = null;
    private String m_YearTo = null;

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
        m_RootView =  inflater.inflate(R.layout.fragment_search_fragments, container, false);
        m_Cointener = container;
        m_LayoutInflater = inflater;
        m_Bundle = savedInstanceState;

        m_SpinnerPlatforms = (Spinner)m_RootView.findViewById(R.id.spinner_platform);
        m_SpinnerPublisher = (Spinner)m_RootView.findViewById(R.id.spinner_publisher);
        m_SpinnerGenres = (Spinner)m_RootView.findViewById(R.id.spinner_gener);
        m_SpinnerReleaseFrom = (Spinner)m_RootView.findViewById(R.id.spinner_year_of_release_from);
        m_SpinnerReleaseTo = (Spinner)m_RootView.findViewById(R.id.spinner_year_of_release_to);
        m_SpinnerDeveloprs = (Spinner)m_RootView.findViewById(R.id.spinner_developrs);

        m_ButtonSearch = (Button) m_RootView.findViewById(R.id.button_search);
        m_ButtonClear =  (Button) m_RootView.findViewById(R.id.button_clear);

        m_TextSearch =  m_RootView.findViewById(R.id.search_input_text);
        m_HashMap = new HashMap<>();
        m_IdPlatformAndNames = new HashMap<>();

        callToDeveloprsSpinner();
        callToPlatformsSpinner();
        callToPublishersSpinner();
        callToGenresSpinner();
        spinnerYearsOfRelease();

        m_ButtonSearch.setOnClickListener(this);
        m_ButtonClear.setOnClickListener(this);

        m_SpinnerDeveloprs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                if(!parent.getItemAtPosition(position).equals("Choose Developrs"))
                {

                    String item = m_SpinnerDeveloprs.getSelectedItem().toString();
                    m_HashMap.put(eExtantionType.Developrs, item);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        m_SpinnerPlatforms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if(!parent.getItemAtPosition(position).equals("Choose Platform"))
                {

                    String item = m_SpinnerPlatforms.getSelectedItem().toString();
                    Log.d("Item", String.valueOf(m_IdPlatformAndNames.get(item)));
                    m_HashMap.put(eExtantionType.Platform, String.valueOf(m_IdPlatformAndNames.get(item)));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        m_SpinnerReleaseFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!parent.getItemAtPosition(position).equals("Choose Year"))
                {

                    String item = m_SpinnerReleaseFrom.getSelectedItem().toString();
                    m_YearFrom = item + "-01-01";

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        m_SpinnerReleaseTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!parent.getItemAtPosition(position).equals("Choose Year")){

                    if (m_HashMap.containsKey(eExtantionType.ReleaseDate))
                    {

                        String item = m_SpinnerReleaseTo.getSelectedItem().toString();
                        m_YearTo = item + "-12-31";

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        m_SpinnerGenres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!parent.getItemAtPosition(position).equals("Choose Genre"))
                {
                    String item = m_SpinnerGenres.getSelectedItem().toString();
                    m_HashMap.put(eExtantionType.Genre, item);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });

        m_SpinnerPublisher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!parent.getItemAtPosition(position).equals("Choose Publisher")){

                    String item = m_SpinnerPublisher.getSelectedItem().toString();
                    m_HashMap.put(eExtantionType.Publisher, item);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        return m_RootView;

    }

    //Platform
    private void spinnerPlatform(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, List<PlatformModel> i_List)
    {

        List<String> platformNamesList = new ArrayList<>();
        platformNamesList.add(0,"Choose Platform");

                for(PlatformModel platform: i_List)
                {

                    platformNamesList.add(platform.getName());
                    m_IdPlatformAndNames.put(platform.getName(),platform.getId());

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.my_selected_item, platformNamesList);
                adapter.setDropDownViewResource(R.layout.my_selected_item_dropdown);
                 m_SpinnerPlatforms.setAdapter(adapter);

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

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(m_LayoutInflater.getContext(), R.layout.my_selected_item, ReleaseYears);

        adapter.setDropDownViewResource(R.layout.my_selected_item_dropdown);
        m_SpinnerReleaseFrom.setAdapter(adapter);
        m_SpinnerReleaseTo.setAdapter(adapter);

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
        m_SpinnerPublisher.setAdapter(adapter);

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
        m_SpinnerGenres.setAdapter(adapter);

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
        m_SpinnerDeveloprs.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {

        ResultBySearch filteredGamesFragment = null;

        switch (v.getId())
        {

            case R.id.button_search:

                if (m_TextSearch.getText().toString().equals("What is on your mind?") || m_TextSearch.getText().toString().equals(""))
                {

                    if (m_HashMap != null)
                    {

                        if (checkingYearIsIllegal())//true is enter
                        {

                            Bundle bundle = new Bundle();
                            bundle.putSerializable(KEY_HASH, m_HashMap);
                            bundle.putString(KEY_STRING, m_EditText);
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

                    if (m_FlagDate == false)
                    {

                        Toast.makeText(m_RootView.getContext(),"Wrong Release Date Input, Please Fix it",Toast.LENGTH_LONG).show();

                    }
                    else
                    {

                        m_HashMap = null;
                        Bundle bundle = new Bundle();
                        String m_EditText = m_TextSearch.getText().toString();
                        m_EditText = m_EditText.replace(' ', '-').toLowerCase();

                        bundle.putString(KEY_STRING, m_EditText);
                        bundle.putSerializable(KEY_HASH, m_HashMap);
                        filteredGamesFragment  = new ResultBySearch();
                        filteredGamesFragment.setArguments(bundle);

                    }

                }

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, filteredGamesFragment).addToBackStack(null).commit();
                break;

            case R.id.button_clear:

                  m_SpinnerGenres.setSelection(0);
                  m_SpinnerPlatforms.setSelection(0);
                  m_SpinnerPublisher.setSelection(0);
                  m_SpinnerReleaseFrom.setSelection(0);
                  m_SpinnerReleaseTo.setSelection(0);
                  m_TextSearch.setText("What is on your mind?");
                  m_FlagDate = true;
                  m_HashMap = new HashMap<>();
                  m_IdPlatformAndNames = new HashMap<>();

                break;

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callToPlatformsSpinner(){

        ResponseFromAPI.getInstance().GetListPlatformsName(new IMyCallback() {
            @Override
            public <T> void onSuccess(@NonNull List<T> spinner)
            {

                spinnerPlatform(m_LayoutInflater, m_Cointener, m_Bundle, (List<PlatformModel>)spinner);

            }

            @Override
            public void onSuccess(@NonNull GameSingleModel gamesSingle) {

            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Toast.makeText(m_RootView.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT);

            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callToPublishersSpinner()
    {

        ResponseFromAPI.getInstance().GetListPublishersName(new IMyCallback() {
            @Override
            public <T> void onSuccess(@NonNull List<T> spinner) {

                spinnerPublisher(m_LayoutInflater, m_Cointener, m_Bundle, (List<PublisherModel>)spinner);

            }

            @Override
            public void onSuccess(@NonNull GameSingleModel gamesSingle) {

            }


            @Override
            public void onError(@NonNull Throwable throwable) {
                Toast.makeText(m_RootView.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT);

            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callToDeveloprsSpinner(){

        ResponseFromAPI.getInstance().GetListDeveloprsName(new IMyCallback() {
            @Override
            public <T> void onSuccess(@NonNull List<T> spinner) {
                
                spinnerDeveloprs(m_LayoutInflater, m_Cointener, m_Bundle, (List<DevelopersModel>) spinner);

            }

            @Override
            public void onSuccess(@NonNull GameSingleModel gamesSingle) {

            }

            @Override
            public void onError(@NonNull Throwable throwable) {

                Toast.makeText(m_RootView.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT);

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callToGenresSpinner()
    {

        ResponseFromAPI.getInstance().GetGenres(new IMyCallback() {

            @Override
            public <T> void onSuccess(@NonNull List<T> spinner) {

                spinnerGenres(m_LayoutInflater, m_Cointener, m_Bundle, (List<GenresModel>)spinner);

            }

            @Override
            public void onSuccess(@NonNull GameSingleModel gamesSingle) {

            }


            @Override
            public void onError(@NonNull Throwable throwable) {
                Toast.makeText(m_RootView.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT);

            }

        });

    }

    private boolean checkingYearIsIllegal()
    {

        m_FlagDate = false;
        String date = null;

          if (m_YearFrom == null &&  m_YearTo == null)// Year From AND TO Year is null
                 {

                     m_FlagDate = !m_FlagDate;

                 }
                 else if (m_YearFrom == null && m_YearTo != null)// From is null AND to Yeas isn't null, we take only year of to
                 {

                     date = String.format(m_SpinnerReleaseTo.getSelectedItem().toString() + "-01-01" + "," + m_YearTo);
                     m_FlagDate = !m_FlagDate;

                 }
                 else if( m_YearFrom != null && m_YearTo != null)// To AND From isn't null
                 {

                     if (Integer.parseInt(m_YearFrom.substring(0,4)) < Integer.parseInt(m_YearTo.substring(0,4))) // who is bigger, if To is bigger, return true else false
                     {

                         date = String.format(m_YearFrom + "," + m_YearTo);
                         m_FlagDate = !m_FlagDate;
                     }

                 }
                 else {//From isn't null and To is null

              date = String.format(m_YearFrom + "," + m_SpinnerReleaseFrom.getSelectedItem().toString() + "-12-31");
              m_FlagDate = !m_FlagDate;

          }

            m_HashMap.put(eExtantionType.ReleaseDate, date);
            return m_FlagDate;

        }

}