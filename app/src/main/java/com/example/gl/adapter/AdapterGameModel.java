package com.example.gl.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gl.R;
import com.example.gl.fragment.GameDetailsFragments;
import com.example.gl.model.GameModel;
import com.example.gl.model.GameSingleModel;
import com.example.gl.response.IMyCallback;
import com.example.gl.response.ResponseFromAPI;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdapterGameModel extends RecyclerView.Adapter<AdapterGameModel.ViewHolder>
{

    private final String KEY_GAME_BY_ID = "GAME_BY_ID";
    private List<GameModel> m_GameModelList;
    private Context m_NewContext;

    public AdapterGameModel(List<GameModel> i_GameModelList, Context i_NewContext)
    {

        this.m_GameModelList = i_GameModelList;
        this.m_NewContext = i_NewContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        GameModel games = m_GameModelList.get(position);

        //Set name of the game
        holder.m_titleNameGame.setText(games.getName());

        //Set image of the game
        String imgaeURL = games.getBackground_image();

        if (imgaeURL != null)
        {

            //Source image of game
              Picasso.with(m_NewContext).load(imgaeURL).resize(512,512).into(holder.m_ImageView);

        }
        else
        {

            //Null image
            Picasso.with(m_NewContext).load(R.mipmap.ic_null_image).resize(512,512).into(holder.m_ImageView);

        }

        //get position of card view and send to game details for to present about the game
        CardView cardView = holder.m_CardView;


        /** When the users click on card view, the API call to data about game
         *  by ID of game and return all data, and send to details fragment.*/
        cardView.setOnClickListener(new View.OnClickListener()
        {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {

                ResponseFromAPI.getInstance().GetGameById(games.getId().toString(), new IMyCallback() {

                    @Override
                    public <T> void onSuccess(@NonNull List<T> games) {

                    }

                    @Override
                    public void onSuccess(@NonNull GameSingleModel gamesSingle)
                    {

                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(KEY_GAME_BY_ID,  gamesSingle);
                        GameDetailsFragments gameDetailsFragments = new GameDetailsFragments();
                        gameDetailsFragments.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  gameDetailsFragments).addToBackStack(null).commit();

                    }

                    @Override
                    public void onError(@NonNull Throwable throwable)
                    {

                        if (throwable instanceof IOException)
                        {

                            Toast.makeText(m_NewContext, throwable.getMessage(), Toast.LENGTH_LONG).show();

                        }
                        else
                        {

                            Toast.makeText(m_NewContext, throwable.getMessage(), Toast.LENGTH_LONG).show();

                        }

                    }

                });

            }

        });

    }

    @Override
    public int getItemCount()
    {

        return m_GameModelList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView m_titleNameGame;
        ImageView m_ImageView;
        CardView m_CardView;

        public ViewHolder(@NonNull View itemView)
        {

            super(itemView);

            m_titleNameGame = itemView.findViewById(R.id.text_view_name_game);
            m_ImageView = itemView.findViewById(R.id.image_view_profile_game);
            m_CardView = itemView.findViewById(R.id.card_view);

        }

    }

}
