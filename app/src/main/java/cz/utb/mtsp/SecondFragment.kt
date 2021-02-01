package cz.utb.mtsp

import Database
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(R.layout.fragment_second) {

    val txtView: TextView? = null
    private lateinit var listView : ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val myInflatedView: View =
            inflater.inflate(R.layout.fragment_second, container, false)

        val button: Button = myInflatedView.findViewById(R.id.button_second) as Button
        button.setOnClickListener {
            deleteHistory()
        }

        return myInflatedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getHistory()
    }

    private fun deleteHistory() {
        val activity: Activity? = activity
        if (activity is MainActivity) {
            val db = Database(activity)
            val data = db.deleteData()
            listView = activity.findViewById(R.id.list_view_history) as ListView
            listView.adapter = null
        }

    }

    private fun getHistory() {
        val activity: Activity? = activity
        if (activity is MainActivity) {
            val db = Database(activity)
            val data = db.readData()

            listView = activity.findViewById<ListView>(R.id.list_view_history)
            val listItems = arrayOfNulls<String>(data.size)
            for (i in 0 until data.size) {
                listItems[i] = data[i].name.toString() + "    " + data[i].date.toString() + "\n"
            }
            val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, listItems)
            listView.adapter = adapter

        }
    }




}