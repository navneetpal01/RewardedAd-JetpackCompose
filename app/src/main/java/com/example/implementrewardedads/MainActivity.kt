package com.example.implementrewardedads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.tv.AdRequest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.implementrewardedads.ui.theme.ImplementRewardedAdsTheme
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImplementRewardedAdsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main(this)
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Main(context : Context, modifier : Modifier = Modifier){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var rewardedAd : RewardedAd? = null
        val btnText = remember { mutableStateOf("Loading Rewared Ads") }
        val btnEnable = remember { mutableStateOf(false) }
        fun loadRewardedAd(context : Context){
            RewardedAd.load(
                context,
                "ca-app-pub-3940256099942544/5224354917",
                com.google.android.gms.ads.AdRequest.Builder().build(),
                object : RewardedAdLoadCallback(){
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        rewardedAd = null
                    }

                    override fun onAdLoaded(p0: RewardedAd) {
                        super.onAdLoaded(p0)
                        rewardedAd = p0
                        btnText.value = "Show rewarded ad"
                        btnEnable.value = true
                    }
                }
            )
        }
        fun showRewardedAd(context : Context,onAdDismissed : () -> Unit){
            if (rewardedAd != null){
                rewardedAd!!.show(context as Activity, OnUserEarnedRewardListener { 
                    Toast.makeText(context,"Rewarded",Toast.LENGTH_SHORT).show()
                    loadRewardedAd(context)
                    onAdDismissed()
                    rewardedAd = null
                    btnText.value = "Loading rewarded Ad"
                    btnEnable.value = false
                })
            }
        }
        loadRewardedAd(context)
        val coroutineScope = rememberCoroutineScope()
        Button(
            enabled = btnEnable.value,
            onClick = {
                coroutineScope.launch {
                    showRewardedAd(context){
                        Toast.makeText(context,"Rewarded Ad Shown!",Toast.LENGTH_LONG).show()
                    }
                }
            }
        ) {
           Text(text = btnText.value)
        }
    }
}



