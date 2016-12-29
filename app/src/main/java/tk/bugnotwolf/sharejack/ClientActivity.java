package tk.bugnotwolf.sharejack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tk.bugnotwolf.sharejack.serverevents.StreamListener;
import tk.bugnotwolf.sharejack.serverevents.StreamStatus;
import tk.bugnotwolf.sharejack.serverevents.WebSocketListener;


public class ClientActivity extends AppCompatActivity {
    Button playButton;
    Button disconnectStreamButton;
    Button connectStreamButton;
    Button muteButton;
    private boolean muted;

    private MusicPlayer musicPlayer = new MusicPlayer(this);
    private StreamListener streamListener = new WebSocketListener() {
        @Override
        public void onStatusUpdate(StreamStatus status) {
            int msec = status.getCurrentTime() * 1000;
            musicPlayer.getPlayer().seekTo(msec); // TODO avoid implementation dependent player
            if (status.isPlaying()) {
                musicPlayer.startAudio();
            } else {
                musicPlayer.pauseAudio();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        playButton = (Button) findViewById(R.id.playButton);
        connectStreamButton = (Button) findViewById(R.id.connectstreamButton);
        disconnectStreamButton = (Button) findViewById(R.id.disconnectStreamButton);
        muteButton = (Button) findViewById(R.id.muteButton);

        connectStreamButton.setEnabled(true);
        disconnectStreamButton.setEnabled(false);
        playButton.setEnabled(false);
        muteButton.setEnabled(false);
    }

    public void connectStreamButton(View view) {
        musicPlayer.setFromServer("https://sharejack.tk/audio/ADC17605.mp3");
        streamListener.connect("wss://sharejack.tk/socket.io/");
        connectStreamButton.setEnabled(false);
        playButton.setEnabled(true);
        disconnectStreamButton.setEnabled(true);
        muteButton.setEnabled(true);
    }

    public void disconnectStreamButton(View view) {
        musicPlayer.releaseMP();
        streamListener.disconnect();
        connectStreamButton.setEnabled(true);
        playButton.setEnabled(false);
        disconnectStreamButton.setEnabled(false);
        muteButton.setEnabled(false);
    }

    public void playButton(View view) {
        streamListener.play();
        musicPlayer.startAudio();
        playButton.setEnabled(false);
    }

    public void muteButton(View view) {
        if (muted) {
            musicPlayer.rebootStream();
        } else {
            musicPlayer.releaseMP();
        }
    }
}
