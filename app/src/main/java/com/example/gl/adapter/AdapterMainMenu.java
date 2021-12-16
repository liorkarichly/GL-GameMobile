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
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.O)
public class AdapterMainMenu extends RecyclerView.Adapter<AdapterMainMenu.ViewHolder>
{

    /** Key for send to other fragment  (GameSingle)*/
    private final String KEY_GAME_BY_ID = "GAME_BY_ID";

    /** List response*/
    private List<GameModel> m_GameModelList;

    /** Screen to send*/
    private Context m_NewContext;

    public AdapterMainMenu(List<GameModel> m_GameModelList, Context m_NewContext)
    {

        this.m_GameModelList = m_GameModelList;
        this.m_NewContext = m_NewContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        //Get the card view, location of the card view in program and send for set the values in card
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_main_menu, parent, false);

        return new AdapterMainMenu.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {

        GameModel games = m_GameModelList.get(position);

        //set name of game
        holder.m_TitleNameGame.setText(games.getName());

        //set image url on imageView
        String imgaeURL = games.getBackground_image();

        //Statment picture profile game if had or not
        if (imgaeURL != null)
        {

            //Source image of game
            Picasso.with(m_NewContext).load(imgaeURL).resize(390,250).into(holder.m_ImageView);

        }
        else
        {

            //Null image
            Picasso.with(m_NewContext).load(R.mipmap.ic_null_image).resize(390,250).into(holder.m_ImageView);

        }

        //get position of card view and send to game details for to present about the game
        CardView cardView = holder.m_CardView;

        /** When the users click on card view, the API call to data about game
         * by ID of game and return all data, and send to details fragment.*/
        cardView.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                //Get from api and send the id of the game when the user click about card view specific
                ResponseFromAPI.getInstance().GetGameById(games.getId().toString(), new IMyCallback() {

                    @Override
                    public <T> void onSuccess(@NonNull List<T> games) {

                    }

                    //Get full details game by id
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

                    //Response error to screen
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

        return 8;

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView m_TitleNameGame;
        ImageView m_ImageView;
        CardView m_CardView;

        public ViewHolder(@NonNull View itemView)
        {

            super(itemView);

            m_TitleNameGame = itemView.findViewById(R.id.text_view_name_game);
            m_ImageView = itemView.findViewById(R.id.image_view_profile_game);
            m_CardView = itemView.findViewById(R.id.card_view_main_menu);

        }

    }

}
