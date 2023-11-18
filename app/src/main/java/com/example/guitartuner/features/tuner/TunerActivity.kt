package com.example.guitartuner.features.tuner


import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_tuner.*
import kotlinx.android.synthetic.main.tuner_view.*
import org.kodein.di.Kodein
import com.example.guitartuner.R
import com.example.guitartuner.common.view.InjectedActivity
import com.example.guitartuner.common.viewmodel.buildViewModel
import com.example.guitartuner.features.tuner.di.tunerActivityModule




class TunerActivity : InjectedActivity() {

    override fun activityModule() = Kodein.Module("tuner") {
        import(tunerActivityModule())
    }

    private val viewModel: TunerViewModel by buildViewModel()

    private val recordAudioPermission = RecordAudioPermissionHandler(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_tuner)
        if (recordAudioPermission.isPermissionGranted().not()) {
            recordAudioPermission.requestPermission()
        } else {
            setupUi()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onActivityDestroyed()
    }

    private fun setupUi() {
        initViewModel()
        initButtons()
    }

    private fun initViewModel(): Unit = with(viewModel) {
        tuner_view.setNoteViewState(noteViewState, this@TunerActivity)

        tunerViewState.observe(this@TunerActivity, Observer { viewState ->
            viewState?.let {

                tuner_view.clearView()
                motionLayout?.transitionToStart()
            }
        })
    }


    private fun initButtons() {

        val diceImage: ImageView = findViewById(R.id.imageView)
        val mediaPlayer = MediaPlayer.create(this, R.raw.e_sound)
        val mediaPlayer1 = MediaPlayer.create(this, R.raw.h_sound)
        val mediaPlayer2 = MediaPlayer.create(this, R.raw.g_sound)
        val mediaPlayer3 = MediaPlayer.create(this, R.raw.d_sound)
        val mediaPlayer4 = MediaPlayer.create(this, R.raw.a_sound)
        val mediaPlayer5 = MediaPlayer.create(this, R.raw.e1_sound)

        val e1Button: Button = findViewById(R.id.button6)
        e1Button.setOnClickListener {

            diceImage.setImageResource(R.drawable.guitar1on)
            mediaPlayer5.start()
            Handler().postDelayed(
                { diceImage.setImageResource(R.drawable.guitar00) },
                2000
            ) //2 seconds



        }
        val aButton: Button = findViewById(R.id.button5)
        aButton.setOnClickListener {

            diceImage.setImageResource(R.drawable.guitar2on)
            mediaPlayer4.start()
            Handler().postDelayed(
                { diceImage.setImageResource(R.drawable.guitar00) },
                2000
            ) //2 seconds

        }

        val dButton: Button = findViewById(R.id.button4)
        dButton.setOnClickListener {
            diceImage.setImageResource(R.drawable.guitar3on)
            mediaPlayer3.start()
            Handler().postDelayed(
                { diceImage.setImageResource(R.drawable.guitar00) },
                2000
            ) //2 seconds
        }

        val gButton: Button = findViewById(R.id.button3)
        gButton.setOnClickListener {
            diceImage.setImageResource(R.drawable.guitar4on)
            mediaPlayer2.start()
            Handler().postDelayed(
                { diceImage.setImageResource(R.drawable.guitar00) },
                2000
            ) //2 seconds
        }

        val hButton: Button = findViewById(R.id.button2)
        hButton.setOnClickListener {
            diceImage.setImageResource(R.drawable.guitar5on)
            mediaPlayer1.start()
            Handler().postDelayed(
                { diceImage.setImageResource(R.drawable.guitar00) },
                2000
            ) //2 seconds
        }

        val eButton: Button = findViewById(R.id.button)
        eButton.setOnClickListener {
            diceImage.setImageResource(R.drawable.guitar6on)
            mediaPlayer.start()
            Handler().postDelayed(
                { diceImage.setImageResource(R.drawable.guitar00) },
                2000
            ) //2 seconds
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ): Unit = when {
        requestCode != recordAudioPermission.requestCode -> Unit
        recordAudioPermission.isPermissionGranted() -> setupUi()
        recordAudioPermission.userCheckedNeverAskAgain() ->
            recordAudioPermission.showSettingsReasonAndRequest()
        else -> recordAudioPermission.requestPermission()
    }
}