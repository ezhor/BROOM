package com.arensis.broom;

import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private final int width = 1920;
    private final int height = 1080;

    // View that contains the Surface Texture
    private TextureView m_surface;

    // Object that connects to our server and gets H264 frames
    private H264Provider h264Provider;

    // Media decoder
    private MediaCodec m_codec;

    // Async task that takes H264 frames and uses the decoder to update the Surface Texture
    //private DecodeFramesTask m_frameTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find our desired SurfaceTexture to display the stream
        m_surface = (TextureView) findViewById(R.id.textureView);

        // Add the SurfaceTextureListener
        m_surface.setSurfaceTextureListener(this);

    }

    @Override
    // Invoked when a TextureView's SurfaceTexture is ready for use
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        // when the surface is ready, we make a H264 provider Object.  When its constructor
        // runs it starts an AsyncTask to log into our server and start getting frames
        // I have dummed down this demonstration to access the local h264 video from the raw resources dir
        //provider = new H264Provider(getResources().openRawResource(R.raw.video));

        ConnectionTask task = new ConnectionTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    // Invoked when the SurfaceTexture's buffer size changed
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    // Invoked when the specified SurfaceTexture is about to be destroyed
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    // Invoked when the specified SurfaceTexture is updated through updateTextImage()
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    private class ConnectionTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            ServerSocket serverSocket;
            Socket socket;

            try {
                serverSocket = new ServerSocket(2222);
                socket = serverSocket.accept();
                h264Provider = new H264Provider(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Create the format settings for the MediaCodec
            MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
            // Set the SPS frame
            format.setByteBuffer("csd-0", ByteBuffer.wrap(h264Provider.getSPS()));
            // Set the PPS frame
            format.setByteBuffer("csd-1", ByteBuffer.wrap(h264Provider.getPPS()));
            // Set the buffer size
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, width * height);

            try {
                // Get an instance of MediaCodec and give it its Mime type
                m_codec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
                // Configure the codec
                m_codec.configure(format, new Surface(m_surface.getSurfaceTexture()), null, 0);
                // Start the codec
                m_codec.start();
                // Create the AsyncTask to get the frames and decode them using the Codec
                decodeFrames();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        private void decodeFrames() {
            while (!isCancelled()) {
                // Get the next frame
                byte[] frame = h264Provider.nextFrame();

                // Now we need to give it to the Codec to decode into the surface

                // Get the input buffer from the decoder
                // Pass in -1 here as in this example we don't have a playback time reference
                int inputIndex = m_codec.dequeueInputBuffer(-1);

                // If the buffer number is valid use the buffer with that index
                if (inputIndex >= 0) {
                    ByteBuffer buffer = m_codec.getInputBuffer(inputIndex);

                    try {
                        buffer.put(frame);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    // Tell the decoder to process the frame
                    m_codec.queueInputBuffer(inputIndex, 0, frame.length, 0, 0);
                }

                MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                int outputIndex = m_codec.dequeueOutputBuffer(info, 0);
                if (outputIndex >= 0) {
                    // Output the image to our SufaceTexture
                    m_codec.releaseOutputBuffer(outputIndex, true);
                }

                // wait for the next frame to be ready, our server makes a frame every 250ms
                try {
                    Thread.sleep(250);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}