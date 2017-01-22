package com.flynnsam.airhornwidget.mediaplayer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import android.content.Context;
import android.media.MediaPlayer;

import com.flynnsam.airhornwidget.R;
import com.flynnsam.airhornwidget.provider.AirHornAppWidgetProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * Tests for the {@link MediaPlayerProvider} class.
 *
 * Created by Sam on 2017-01-22.
 */
@RunWith(MockitoJUnitRunner.class)
public class MediaPlayerProviderTest {

    @Mock
    Context mockContext;

    protected boolean playing;
    protected boolean playerAllocated;

    @Before
    public void initialize() {
        setMockPlayerDefaultBehavior();
        playing = false;
    }


    @Test
    public void testSimplePlayAndStop() {

        setMockPlayerDefaultBehavior();
        playing = false;

        // "Finish" immediately when starting.
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                playing = false;
                MediaPlayerProvider.CLOSE_CALLBACK.onCompletion(MediaPlayerProvider.player);
                return null;
            }
        }).when(MediaPlayerProvider.player).start();

        MediaPlayerProvider.play(mockContext);

        assertFalse(playing);
        assertFalse(playerAllocated);
    }

    @Test
    public void testTwoPlaysAndStop() {
        // "Finish" immediately when starting.
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                playing = false;
                MediaPlayerProvider.CLOSE_CALLBACK.onCompletion(MediaPlayerProvider.player);
                return null;
            }
        }).when(MediaPlayerProvider.player).start();

        MediaPlayerProvider.play(mockContext);

        assertFalse(playing);
        assertFalse(playerAllocated);

        MediaPlayerProvider.play(mockContext);

        assertFalse(playing);
        assertFalse(playerAllocated);
    }

    @Test
    public void testInterruptPlay() {

        MediaPlayerProvider.play(mockContext);

        assertTrue(playing);
        assertTrue(playerAllocated);

        // "Finish" immediately when starting.
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                playing = false;
                MediaPlayerProvider.CLOSE_CALLBACK.onCompletion(MediaPlayerProvider.player);
                return null;
            }
        }).when(MediaPlayerProvider.player).start();

        MediaPlayerProvider.play(mockContext);

        assertFalse(playing);
        assertFalse(playerAllocated);
    }

    protected void setMockPlayerDefaultBehavior() {

        // MediaPlayer.create(Context, int)
        when(MediaPlayer.create(mockContext, R.raw.airhorn)).then(new Answer<MediaPlayer>() {
            @Override
            public MediaPlayer answer(InvocationOnMock invocation) throws Throwable {
                playerAllocated = true;
                return mock(MediaPlayer.class);
            }
        });

        // mediaPlayer.isPlaying()
        when(MediaPlayerProvider.player.isPlaying()).thenReturn(playing);

        // mediaPlayer.stop()
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                playing = false;
                return null;
            }
        }).when(MediaPlayerProvider.player).stop();

        // mediaPlayer.start()
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                playing = true;
                return null;
            }
        }).when(MediaPlayerProvider.player).start();

        // mediaPlayer.setOnCompletionListener(MediaPlayer.OnCompletionListener)
        doNothing().when(MediaPlayerProvider.player).setOnCompletionListener(MediaPlayerProvider.CLOSE_CALLBACK);

        // mediaPlayer.release()
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                playerAllocated = false;
                return null;
            }
        }).when(MediaPlayerProvider.player).release();
    }
}
