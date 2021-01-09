package cz.utb.mtsp

import Database
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private val client = OkHttpClient()
    var txtView: TextView? = null
    var imageView: ImageView? = null
    val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        navController = findNavController(this, R.id.nav_host_fragment)
    }

    private fun getDataFromApi(url: String, parameter: String) {
        val request = Request.Builder()
            .url("$url?q=$parameter")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {

                val jsonArray = JSONArray(response.body()?.string())
                val jsonObject: JSONObject = jsonArray.getJSONObject(0)

                val showJson = jsonObject.get("show").toString()
                val show = JSONObject(showJson)
                val rating = JSONObject(show.getString("rating"))
                val image = JSONObject(show.getString("image"))

                if (response.isSuccessful) {
                    runOnUiThread {

                        val summary = htmlToStringFilter(show.getString("summary").toString())
                        val showObj = Show(show.getString("id").toInt(), show.getString("language"), show.getString("name"), show.getString("officialSite"), show.getString("premiered"), rating.getString("average"), summary)
                        txtView = findViewById<TextView>(R.id.textViewNameValue) as TextView
                        txtView?.text = showObj.name
                        txtView = findViewById<TextView>(R.id.textViewLanguageValue) as TextView
                        txtView?.text = showObj.language
                        txtView = findViewById<TextView>(R.id.textViewRatingValue) as TextView
                        txtView?.text = showObj.rating
                        txtView = findViewById<TextView>(R.id.textViewPremieredValue) as TextView
                        txtView?.text = showObj.premiered
                        txtView = findViewById<TextView>(R.id.textView) as TextView
                        txtView?.text = showObj.summary

                        imageView = findViewById<ImageView>(R.id.imageView) as ImageView
                        if (image !== null) {
                            val imageUrl : String = image.getString("medium").toString()
                            val newImageUrl : String = imageUrl.replace("http://", "https://")
                            Picasso.get().load(newImageUrl).into(imageView)
                        }

                        val db = Database(activity)
                        db.insertData(showObj)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Searched item could not be found.", Toast.LENGTH_SHORT).show()
                }

            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_search -> {
                navController.navigate(R.id.firstFragment)
                return true
            }
            R.id.action_history -> {
                navController.navigate(R.id.secondFragment)
                return true
            }
            R.id.action_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun searchItem(view: View) {
        val editText = findViewById<EditText>(R.id.editTextSearch) as EditText
        getDataFromApi("https://api.tvmaze.com/search/shows", editText.text.toString())


    }

    fun htmlToStringFilter(textToFilter: String?): String? {
        return Html.fromHtml(textToFilter).toString()
    }

}



