package com.example.mypass.InputsUI

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IconAndText(icon: ImageVector,iconDes:String, text:String){
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon,iconDes)
        Text(modifier = Modifier
            .padding(5.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight =FontWeight.Bold,
            text = ":"
        )
        SelectionContainer {
            if(iconDes == "User"){
                DisableSelection {
                    Text(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight =FontWeight.Bold,
                        text = text
                    )
                }
            }else{
                Text(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    text = text
                )
            }

        }
    }
}