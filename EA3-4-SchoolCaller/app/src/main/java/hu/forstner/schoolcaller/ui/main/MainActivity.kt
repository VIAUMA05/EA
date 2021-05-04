package hu.forstner.schoolcaller.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import hu.forstner.schoolcaller.R
import hu.forstner.schoolcaller.data.model.People
import hu.forstner.schoolcaller.databinding.ActivityMainBinding
import hu.forstner.schoolcaller.ui.calling.CallingActivity
import android.util.Pair as UtilPair


class MainActivity : AppCompatActivity(), PeopleAdapter.ListItemClickListener {

    private lateinit var viewModel : MainViewModel
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setContentView(binding.root)

        val recyclerView = binding.rvPeople
        viewModel.people.observe(this, Observer<MutableList<People>> { peopleList ->
            val recyclerViewAdapter = PeopleAdapter(this, peopleList, this)
            recyclerView.setLayoutManager(LinearLayoutManager(this))
            recyclerView.setAdapter(recyclerViewAdapter)
        });

        viewModel.loadPeople()
    }

    override fun onListItemClick(position: Int) {
        val callingIntent = Intent(this, CallingActivity::class.java)

        val headView = (binding.rvPeople.findViewHolderForAdapterPosition(position) as PeopleAdapter.ItemViewHolder).imgHead as View
        val p1 = UtilPair.create(headView, "profile")
        val nameView = (binding.rvPeople.findViewHolderForAdapterPosition(position) as PeopleAdapter.ItemViewHolder).tvName as View
        val p2 = UtilPair.create(nameView, "name")
        val options = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, p1, p2)

        val b = Bundle().also {
            it.putInt("position", position)
            callingIntent.putExtras(it)
        }

        startActivity(callingIntent, options.toBundle())
    }


}