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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    var staySelected by remember { mutableStateOf(true) }

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {

            Spacer(modifier = Modifier.width(2.dp))

            // var showDialog by remember { mutableStateOf(false) }
            Button(
                modifier = Modifier.size(width = 200.dp, height = 80.dp),

                onClick = {
                    showTextField = true
                }
            ) {
                Text(

                   text= "Add City",
                    fontSize = 30.sp

                )

            }


            var deleteDialog by remember { mutableStateOf(false) }

            Button(
                modifier = Modifier.size(width = 200.dp, height = 80.dp),
                onClick = {
                    deleteDialog = true
                }
            ) {
                Text(
                    text= "Delete City",
                    fontSize = 28.sp
                )
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
                                staySelected = false
                                deleteDialog = false
                            }

                        ) { Text("Yes")}
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                deleteDialog = false
                                staySelected = true
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

                        if (staySelected){
                            Modifier.background(color = Color.Blue)
                            selectedCity = city

                        }


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
                modifier = Modifier.size(width = 200.dp, height = 80.dp),
                onClick = {
                    if (newCityName.isNotBlank() && newCityName !in cities) {
                        onAddCity(newCityName)
                        newCityName = ""
                        showTextField = false
                    }

                }
            ) { Text(

                text= "Confirm",
                fontSize = 30.sp
            ) }
        }
    }
}
}

@Composable
fun CityRow(
    city:String,
    onClick : () -> Unit){

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 24.dp)
                .clickable {
                    onClick()
                }
                .background(color = Color.Blue)


        ) {



        Text(
            text = city,

            fontSize = 28.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)


        )

        }





}


