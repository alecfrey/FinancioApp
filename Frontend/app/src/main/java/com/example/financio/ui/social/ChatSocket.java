package com.example.financio.ui.social;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.financio.LoginActivity;
import com.example.financio.*;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.w3c.dom.Text;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Public class ChatSocket
 *
 * This class extends from SocialFragment and is accessed after adding a friend by userID.
 */
public class ChatSocket extends SocialFragment {

    private ImageButton imgBtnBack;
    private TextView tvFriendName;

    private EditText etEnterMessage;
    private Button btnSendMessage;
    private TextView tvReceivedMsg;
    private TextView tvConnection;
    private ImageButton imgBtnClearChat;
    private TextView tvSentMsg;

    private TextView tvReceivedName;
    private TextView tvSentName;

    private TextView tvReceivedTime;
    private TextView tvSentTime;

    private WebSocketClient cc;

    private String loginUsername;
    private boolean connectionFailed = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates fragment view and all XML views are assigned variables.
     * OnClickListeners are defined in this method.
     *
     * WebSockets are used when clicking on send message button.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        imgBtnBack = view.findViewById(R.id.imgBtnLeaveChat);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.replace(R.id.social_chat, new SocialFragment());
                fr.addToBackStack(null);
                fr.setReorderingAllowed(true);
                fr.commit();
            }
        });

        etEnterMessage = view.findViewById(R.id.etNewMessage);
        btnSendMessage = view.findViewById(R.id.btnSendMessage);
        tvReceivedMsg = view.findViewById(R.id.tvReceivedMsg);
        tvConnection = view.findViewById(R.id.tvConnection);
        imgBtnClearChat = view.findViewById(R.id.imgBtnClearChat);
        tvSentMsg = view.findViewById(R.id.tvSentMsg);
        tvReceivedName = view.findViewById(R.id.tvReceivedName);
        tvSentName = view.findViewById(R.id.tvSentName);
        tvReceivedTime = view.findViewById(R.id.tvReceivedTime);
        tvSentTime = view.findViewById(R.id.tvSentTime);

        tvFriendName = view.findViewById(R.id.tvChatFriendName);
        if (!selectedUser.getFullName().isEmpty()) {
            tvFriendName.setText(selectedUser.getFullName());
        } else {
            tvFriendName.setText(selectedUser.getUsername());
        }
        tvReceivedName.setText(selectedUser.getUsername() + "\n");

        if (!LoginActivity.getUsername().equals("")) {
            loginUsername = LoginActivity.getUsername();
            tvSentName.setText(loginUsername + "\n");

        } else {
            loginUsername = "Error";
            connectionFailed = true;
            tvSentName.setText("Error: Couldn't find login user" + "\n");
        }

        Draft[] drafts = {
                new Draft_6455()
        };

        try {
            Log.d("Socket:", "Trying socket");
            String url = "ws://10.0.2.2:8080/websocket/" + LoginActivity.getUsername();
           // tvConnection.setText("\nServer: " + selectedUser.getName() + " has joined the chat!");
            cc = new WebSocketClient(new URI(url), (Draft) drafts[0]) {
                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);
                    String s = tvReceivedMsg.getText().toString();
                    if (message.contains("Joined the Chat")) {
                        // Currently not showing connecting to chat textview
                        /**
                        tvConnection.append("Server: " + message + "\n");
                        if (tvConnection.getLineCount() > 2) {
                            tvConnection.setMovementMethod(new ScrollingMovementMethod());
                        }
                         */
                    } else {
                        Date now = new Date(System.currentTimeMillis());
                        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
                        String currentTime = formatter.format(now);

                        if (!loginUsername.equals("Error") && message.contains(loginUsername)) {
                            tvSentTime.setText("Last Message Sent: " + currentTime);
                            int index = message.indexOf(':');
                            tvSentMsg.append("~ " + message.substring(index + 2, message.length()) + "\n");
                            if (tvSentMsg.getLineCount() > 9) {
                                tvSentMsg.setMovementMethod(new ScrollingMovementMethod());
                            }

                        } else if (message.contains(selectedUser.getUsername())){
                            tvReceivedTime.setText("Last Message Received: " + currentTime);
                            int index = message.indexOf(':');
                            tvReceivedMsg.append("~ " + message.substring(index + 2, message.length()) + "\n");
                            if (tvReceivedMsg.getLineCount() > 9) {
                                tvReceivedMsg.setMovementMethod(new ScrollingMovementMethod());
                            }
                        } else if (connectionFailed) {
                            Toast t = Toast.makeText(getContext(), "Error connected users.", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                @Override
                public void onError(Exception e) {
                    Log.d("Exception:", e.toString());
                }
            };
        } catch (URISyntaxException e) {
            Log.d("Exception:", e.getMessage().toString());
            e.printStackTrace();
        }
        cc.connect();

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etEnterMessage.getText().toString().isEmpty()) {
                    closeKeyboard(view, getContext());
                    try {
                        cc.send(etEnterMessage.getText().toString());
                        etEnterMessage.getText().clear();
                    } catch (Exception e) {
                        Toast t = Toast.makeText(getContext(), "Error sending message.", Toast.LENGTH_SHORT);
                        t.show();
                        //Log.d("ExceptionSendMessage:", e.getMessage().toString());
                    }
                }
            }
        });

        imgBtnClearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvReceivedMsg.setText("");
                tvSentMsg.setText("");
                tvReceivedTime.setText("");
                tvSentTime.setText("");
                Toast.makeText(getActivity(), "Chat history cleared.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    /**
     * Closes keyboard after sending message.
     * @param v
     * @param c
     */
    private void closeKeyboard(View v, Context c) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
