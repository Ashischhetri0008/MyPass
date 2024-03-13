package com.example.mypass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mypass.ui.theme.MyPassTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPassTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column {
                        Greeting("Facebook","123@gmail.com","12345678")
                        Greeting("Facebook","123@gmail.com","12345678")
                    }
                }

            }
        }
    }
}

@Composable
fun Greeting(site: String,email: String,pass: String, modifier: Modifier = Modifier) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal=12.dp, vertical = 12.dp),
        shape = RoundedCornerShape(15.dp)
        ){
        Column(modifier = Modifier.padding(10.dp)) {
            Text(modifier = Modifier.padding(5.dp),text = site)
            Text(modifier = Modifier.padding(5.dp),text = "Email : $email")
            Text(modifier = Modifier.padding(5.dp),text = "key : $pass")
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyPassTheme {
        Greeting("Facebook","123@gmail.com","12345678")
    }
}