package com.example.mypass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun DetailsView(itemId: Int?){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column {
            Text(modifier = Modifier
                .padding(top=10.dp)
                .align(Alignment.CenterHorizontally)
                , text = itemId.toString())
            Details("Facebook","123@gmail.com","12345678")
            Details("Facebook","123@gmail.com","12345678")
        }
    }
}
@Composable
fun Details(site: String,email: String,pass: String, modifier: Modifier = Modifier) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 12.dp),
        shape = RoundedCornerShape(15.dp)
    ){
        Column(modifier = Modifier.padding(10.dp)) {
            Text(modifier = Modifier.padding(5.dp),text = site)
            Text(modifier = Modifier.padding(5.dp),text = "Email : $email")
            Text(modifier = Modifier.padding(5.dp),text = "key : $pass")
        }

    }

}

@Composable
@Preview
fun DetailsViewPreview(){
    DetailsView(1)
}