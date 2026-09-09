package com.example.listycity

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme
import java.util.stream.DoubleStream.builder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val cityRepository = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   CityListScreen(
                       cities = cityRepository.cities,
                       onAddCity = {cityRepository.addCity(it)} ,
                       onDeleteCity = {cityRepository.deleteCity(it)},
                       modifier = Modifier.padding(innerPadding)
                   )
                }
            }
        }
    }
}


class CityRepository {

    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Calgary", "Toronto"
    )

    val cities: List<String>
        get() = _cities

    fun addCity(city:String){
        _cities.add(city)
    }

    fun deleteCity(city:String){
        _cities.remove(city)
    }

}


@Composable
fun CityListScreen(
    cities:List<String>,
    onAddCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier

) {
    var newCityName by remember { mutableStateOf("") }
    var showTextField by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {

            Spacer(modifier = Modifier.width(8.dp))

            // var showDialog by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    showTextField = true
                }
            ) {
                Text("Add City")
            }


            var deleteDialog by remember { mutableStateOf(false) }

            Button(
                onClick = {
                    deleteDialog = true
                }
            ) {
                Text("Delete City")
            }

            if(deleteDialog){

                AlertDialog(
                    onDismissRequest = {
                        deleteDialog = false

                    },
                    title = {
                        Text("Are you sure you wanna delete this city?")
                    },
                    confirmButton ={
                        TextButton(
                            onClick = {
                                onDeleteCity(selectedCity)
                                deleteDialog = false
                            }

                        ) { Text("Yes")}
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                deleteDialog = false
                            }
                        ) { Text("No")}
                    }
                        )
                    }


            }




        LazyColumn(modifier = modifier.weight(1f)) {
            items(cities) {
                city ->
                CityRow(
                    city = city,
                    onClick = {
                        showTextField = false
                        Modifier.background(color = Color.Blue)
                        selectedCity = city

                    }
                )
            }
        }

        if (showTextField) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {

            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City name") },
                modifier = Modifier.weight(1f),

                )
            Button(
                onClick = {
                    if (newCityName.isNotBlank() && newCityName !in cities) {
                        onAddCity(newCityName)
                        newCityName = ""
                        showTextField = false
                    }

                }
            ) { Text("Confirm") }
        }
    }
}
}

@Composable
fun CityRow(
    city:String,
    onClick : () -> Unit){


        Text(
            text = city,

            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
                .clickable {
                    onClick()
                }
        )







}


