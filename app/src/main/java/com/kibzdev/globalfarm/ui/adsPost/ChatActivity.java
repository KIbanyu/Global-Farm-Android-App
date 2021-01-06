package com.kibzdev.globalfarm.ui.adsPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.adapters.NewGroupChatAdapter;
import com.kibzdev.globalfarm.models.response.ChatResponse;
import com.kibzdev.globalfarm.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private String TAG = "--- Global Farm chat ---";
    private Context context;
    private EditText message;
    private ImageView btn_send;
    private RecyclerView listView;
    private List<ChatResponse.ChatMessages> chats;
    private int page = 0;
    private SwipeRefreshLayout swipeContainer;
    private NewGroupChatAdapter newGroupChatAdapter;
    private LinearLayout layoutImojis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        context = this;
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
//        toolbar.setTitle(getIntent().getStringExtra("groupName") + " chat");


        message = findViewById(R.id.message);
        btn_send = findViewById(R.id.btn_send);
        listView = findViewById(R.id.recycler_view);
        swipeContainer = findViewById(R.id.swipeContainer);
        layoutImojis = findViewById(R.id.layoutImojis);

        chats = new ArrayList<>();

        newGroupChatAdapter = new NewGroupChatAdapter(getApplicationContext(), chats);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(newGroupChatAdapter);

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (count > 0) {
                    layoutImojis.setVisibility(View.GONE);
                } else {
                    layoutImojis.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s)) {
                    layoutImojis.setVisibility(View.VISIBLE);
                } else {
                    layoutImojis.setVisibility(View.GONE);
                }
            }
        });


        btn_send.setOnClickListener(v -> {
            String Message = message.getText().toString().trim();

            if (TextUtils.isEmpty(Message)) {
                Toast.makeText(context, "Enter a message to continue", Toast.LENGTH_SHORT).show();
            } else {
//                sendMessage(Message);
                message.setText("");
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                fetchGroupChat("fetch", page);

                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeContainer.setRefreshing(false);
                    }
                }, 2000); // Delay in millis
            }
        });

//        fetchGroupChat("load", page);


    }

//    public void sendMessage(String message) {
//
//        ChatResponse.ChatMessages chatMessages = new ChatResponse.ChatMessages();
//        ChatResponse.ChatMessages.ChatUser chatUser = new ChatResponse.ChatMessages.ChatUser();
//
//        chatMessages.setMessage(message);
//        chatUser.setName(Prefs.getUserData().getFirstName());
//        chatUser.setPhone(Prefs.phone);
//        chatMessages.setDate(currentDate());
//        chatMessages.setFrom("self");
//        chatMessages.setUser(chatUser);
//
//        if (!chats.contains(chatMessages)) {
//            chats.add(chatMessages);
//            newGroupChatAdapter.notifyItemRangeRemoved(0, newGroupChatAdapter.getItemCount());
//            newGroupChatAdapter.notifyDataSetChanged();
//            listView.getLayoutManager().smoothScrollToPosition(listView, null, newGroupChatAdapter.getItemCount() - 1);
//        }
//
//        ChatRequest chatRequest = new ChatRequest();
//        ApiInterface apiSerice = RestAdapter.createAPI(prefs.getString(context, "token"));
//
//        chatRequest.setGroupType("fundraiser");
//        chatRequest.setMessageType("text");
//        chatRequest.setGroup(getIntent().getLongExtra("group", 0));
//        chatRequest.setMessage(message);
//
//        utils.Log(context, "Chat request" + new Gson().toJson(chatRequest));
//        Call<ChatResponse> call = apiSerice.sendMessage(chatRequest);
//        call.enqueue(new Callback<ChatResponse>() {
//
//            @Override
//            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
//                utils.Log(context, "Chat response" + new Gson().toJson(response));
//
//                if (response.body() != null) {
//
//                    if (response.body().getStatus().equals("00")) {
//
//                        if (response.body().getRefreshToken() != null) {
//                            prefs.saveString(context, "token", response.body().getRefreshToken());
//                        }
//
//                    } else if (response.body() != null && response.body().getStatus().equals("09")) {
//
//                        DialogProcessing dialog = new DialogProcessing();
//                        dialog.createConfirmDialog(context);
//                    } else if (response.body() != null && response.body().getStatus().equals("003")) {
//                        utils.sessionExpired(context);
//
//                    } else {
//
//                        showSuccessSnackbar(response.body().getMessage());
//                    }
//                } else {
//                    showSuccessSnackbar(context.getResources().getString(R.string.server_error));
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ChatResponse> call, Throwable t) {
//                showSuccessSnackbar("Server error");
//            }
//
//        });
//
//    }

    public void showSuccessSnackbar(String message) {

        ConstraintLayout view = findViewById(R.id.constraint_layout);
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(context.getResources().getColor(R.color.background_color));
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setDuration(30000);
        snackbar.setAction("OK", view1 -> snackbar.dismiss());
        snackbar.show();


    }

//    public void fetchGroupChat(String type, int pageNumber) {
//        if (!networkDetector.isConnectingToInternet()) {
//            utils.showSnackBar(context, getResources().getString(R.string.error_internet_connection));
//            return;
//        }
//
//        if (type.equalsIgnoreCase("load")) {
//            utils.showHideDialog(true, context);
//        }
//
//
//        ChatRequest chatRequest = new ChatRequest();
//        ApiInterface apiSerice = RestAdapter.createAPI(prefs.getString(context, "token"));
//
//        chatRequest.setGroupType("fundraiser");
//        chatRequest.setMessageType("");
//        chatRequest.setPage(pageNumber);
//        chatRequest.setSize(20);
//        chatRequest.setGroup(getIntent().getLongExtra("group", 0));
//        chatRequest.setMessage("");
//
//        Call<ChatResponse> call = apiSerice.fetchChat(chatRequest);
//        utils.Log(context, "Fetch chat request: " + new Gson().toJson(chatRequest));
//        call.enqueue(new Callback<ChatResponse>() {
//
//            @Override
//            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
//                utils.Log(context, "Chat fetch response" + new Gson().toJson(response));
//                utils.showHideDialog(false, context);
//
//                if (response.body() != null) {
//
//                    if (response.body().getStatus().equals("00")) {
//
//
//                        for (ChatResponse.ChatMessages item : response.body().getData()) {
//
//                            if (!chats.contains(item)) {
//
//                                if (type.equalsIgnoreCase("fetch")) {
//
//                                    chats.add(0, item);
//
//                                } else {
//                                    chats.add(item);
//                                }
//
//                                newGroupChatAdapter.notifyItemRangeRemoved(0, newGroupChatAdapter.getItemCount());
//                                newGroupChatAdapter.notifyDataSetChanged();
//                                if (newGroupChatAdapter.getItemCount() > 1) {
//                                    if (type.equals("load")) {
//                                        listView.getLayoutManager().smoothScrollToPosition(listView, null, newGroupChatAdapter.getItemCount() - 1);
//                                    } else {
//                                        listView.getLayoutManager().smoothScrollToPosition(listView, null, 0);
//                                    }
//
//                                }
//                            }
//                        }
//
//
//                        page = pageNumber + 1;
//
//                        if (response.body().getRefreshToken() != null) {
//                            prefs.saveString(context, "token", response.body().getRefreshToken());
//                        }
//
//                    } else if (response.body() != null && response.body().getStatus().equals("09")) {
//
//                        DialogProcessing dialog = new DialogProcessing();
//                        dialog.createConfirmDialog(context);
//                    } else if (response.body() != null && response.body().getStatus().equals("003")) {
//                        utils.sessionExpired(context);
//
//                    } else {
//
//                        showSuccessSnackbar(response.body().getMessage());
//                    }
//                } else {
//                    showSuccessSnackbar(context.getResources().getString(R.string.server_error));
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ChatResponse> call, Throwable t) {
//                utils.showHideDialog(false, context);
//                utils.showDialog(context.getResources().getString(R.string.server_error), context);
//            }
//
//        });
//
//
//    }
}