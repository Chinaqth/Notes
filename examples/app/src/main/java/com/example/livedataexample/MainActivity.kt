package com.example.livedataexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val mViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel.changeData("BBBB")
        mViewModel.liveData.observe(this) {
            Log.d("QQQ",it)
        }
        findViewById<Button>(R.id.button).setOnClickListener {
            thread {
                for (i in 0..10000) {
                    mViewModel.changeDataWorkThread(i.toString())
                }
            }
        }
    }

}