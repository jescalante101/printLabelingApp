package com.example.fibra_labeling.fibrafil.ui.screen.home.component
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R

@Composable
fun HomeHeader(){
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(3f)
        ) {
            Text(
                text = "Hello, Nika \uD83D\uDC4B",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = MaterialTheme.typography.headlineLarge.fontWeight
            )

            Text(
                text = "Welcome to McDonald's",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
            )
        }
        OutlinedCard(
            modifier = Modifier.weight(1f),
            onClick = {},
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_user),
                    contentDescription = "Profile",
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                )
            }
        }
    }
}